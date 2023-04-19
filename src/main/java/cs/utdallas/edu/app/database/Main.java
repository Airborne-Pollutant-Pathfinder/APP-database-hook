package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.api.BreezometerAPIClient;
import cs.utdallas.edu.app.database.data.SensorTable;
import org.hibernate.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String dbUrl = System.getenv("APP_DB_URL");
        String dbUsername = System.getenv("APP_DB_USERNAME");
        String dbPassword = System.getenv("APP_DB_PASSWORD");

        SessionFactory factory = SessionFactoryMaker.getFactory();

        try {
            Session session = factory.openSession();

            // Initialize API clients
            APIRepository apiRepository = APIRepository.builder()
                    .registerAllSupportedPollutants(new BreezometerAPIClient())
                    .build();

            // Start fetch data task
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(new FetchDataTask(session, apiRepository), 0, 5, TimeUnit.MINUTES);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}