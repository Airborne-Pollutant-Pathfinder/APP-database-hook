package edu.utdallas.cs.app.database;

import edu.utdallas.cs.app.database.api.APIRepository;
import edu.utdallas.cs.app.database.api.openaq.OpenAQAPIClient;
import edu.utdallas.cs.app.database.sse.CapturedPollutantUpdate;
import edu.utdallas.cs.app.database.sse.SSEPublisher;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SessionFactory factory = SessionFactoryMaker.getFactory();

        // Initialize API clients
        APIRepository apiRepository = APIRepository.builder()
                .registerAllSupportedPollutants(new OpenAQAPIClient())
                .build();

        // Initialize Spring Boot for SSE
        ApplicationContext context = SpringApplication.run(Main.class, args);
        SSEPublisher<CapturedPollutantUpdate> publisher = context.getBean(SSEPublisher.class);

        // Start fetch data task
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new FetchDataTask(factory, apiRepository, publisher), 0, 2, TimeUnit.SECONDS);
    }
}