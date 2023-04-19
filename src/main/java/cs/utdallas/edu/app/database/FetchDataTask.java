package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.data.SensorTable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public final class FetchDataTask implements Runnable {
    private final Session session;
    private final APIRepository apiRepository;

    public FetchDataTask(Session session, APIRepository apiRepository) {
        this.session = session;
        this.apiRepository = apiRepository;
    }

    @Override
    public void run() {
        System.out.println("Hello, world!");

        Transaction tx = session.beginTransaction();
        List<SensorTable> sensors = session.createQuery("SELECT s FROM SensorTable s", SensorTable.class)
                .list();
        tx.commit();
        System.out.println(sensors);

        System.out.println("Finished");
    }
}
