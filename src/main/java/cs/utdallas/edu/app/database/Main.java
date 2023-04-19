package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.api.openaq.OpenAQAPIClient;
import cs.utdallas.edu.app.database.table.Sensor;
import org.hibernate.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        SessionFactory factory = SessionFactoryMaker.getFactory();

        // Initialize API clients
        APIRepository apiRepository = APIRepository.builder()
                .registerAllSupportedPollutants(new OpenAQAPIClient())
                .build();

        // Start fetch data task
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new FetchDataTask(factory, apiRepository), 0, 5, TimeUnit.MINUTES);
    }
}