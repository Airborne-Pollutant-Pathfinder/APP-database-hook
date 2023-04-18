package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIClient;
import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.api.APIRepositoryImpl;
import cs.utdallas.edu.app.database.api.BreezometerAPIClient;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String dbUrl = System.getenv("APP_DB_URL");
        String dbUsername = System.getenv("APP_DB_USERNAME");
        String dbPassword = System.getenv("APP_DB_PASSWORD");

        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            DSLContext dsl = DSL.using(connection);

            // Initialize API clients
            APIRepository apiRepository = APIRepository.builder()
                    .registerAllSupportedPollutants(new BreezometerAPIClient())
                    .build();

            // Start fetch data task
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(new FetchDataTask(dsl, apiRepository), 0, 5, TimeUnit.MINUTES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}