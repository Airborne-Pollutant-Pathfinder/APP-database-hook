package edu.utdallas.cs.app.database;

import edu.utdallas.cs.app.database.api.APIRepository;
import edu.utdallas.cs.app.database.api.mints.MINTSAdapter;
import edu.utdallas.cs.app.database.api.openaq.OpenAQAdapter;
import edu.utdallas.cs.app.database.api.openweather.OpenWeatherAdapter;
import edu.utdallas.cs.app.database.sse.SSEPublisher;
import edu.utdallas.cs.app.database.sse.SensorUpdate;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {
    public static final int MINUTES_TO_FETCH = 5;

    public static void main(String[] args) {
        SessionFactory factory = SessionFactoryMaker.getFactory();

        // Initialize API clients
        APIRepository apiRepository = APIRepository.builder()
                .registerAllSupportedPollutants(new MINTSAdapter(
                        System.getenv("MINTS_BUCKET"),
                        System.getenv("MINTS_ORG"),
                        System.getenv("MINTS_TOKEN"),
                        System.getenv("MINTS_URL")))
                .registerAllSupportedPollutants(new OpenAQAdapter())
                .registerAllSupportedPollutants(new OpenWeatherAdapter(System.getenv("OPENWEATHER_TOKEN")))
                .build();

        // Initialize Spring Boot for SSE
        ApplicationContext context = SpringApplication.run(Main.class, args);
        SSEPublisher<SensorUpdate> publisher = context.getBean(SSEPublisher.class);

        // Start fetch data task
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new FetchDataTask(factory, apiRepository, publisher), 0, MINUTES_TO_FETCH, TimeUnit.MINUTES);
    }
}