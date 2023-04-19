package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.table.Sensor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public final class FetchDataTask implements Runnable {
    private final SessionFactory sessionFactory;
    private final APIRepository apiRepository;
    private long lastRun = 0;

    public FetchDataTask(SessionFactory sessionFactory, APIRepository apiRepository) {
        this.sessionFactory = sessionFactory;
        this.apiRepository = apiRepository;
    }

    @Override
    public void run() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            List<Sensor> sensors = session.createQuery("SELECT s FROM Sensor s", Sensor.class)
                    .list();
            tx.commit();

            for (Sensor sensor : sensors) {
                for (PollutantType pollutant : apiRepository.getSupportedPollutants()) {
                    apiRepository.getClients(pollutant).forEach(client -> {
                        try {
                            client.fetchData(lastRun, sensor, pollutant);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            System.out.println("Finished fetching data");
            lastRun = System.currentTimeMillis();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
