package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.generated.tables.Sensors;
import cs.utdallas.edu.app.database.generated.tables.records.SensorsRecord;
import org.jooq.DSLContext;
import org.jooq.Result;

public final class FetchDataTask implements Runnable {
    private final DSLContext dsl;
    private final APIRepository apiRepository;

    public FetchDataTask(DSLContext dsl, APIRepository apiRepository) {
        this.dsl = dsl;
        this.apiRepository = apiRepository;
    }

    @Override
    public void run() {
        System.out.println("Hello, world!");

        dsl.selectOne().fetch();
        System.out.println("Connected to database");

        // ERROR: The below line causes the application to pause. This could be because jOOQ doesn't support Spatial Data Types.
        dsl.selectFrom(Sensors.SENSORS).forEach(System.out::println);

        System.out.println("Finished");
    }
}
