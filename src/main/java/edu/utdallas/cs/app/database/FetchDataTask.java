package edu.utdallas.cs.app.database;

import edu.utdallas.cs.app.database.api.APIAdapter;
import edu.utdallas.cs.app.database.api.APIReading;
import edu.utdallas.cs.app.database.api.APIRepository;
import edu.utdallas.cs.app.database.sse.CapturedPollutantUpdate;
import edu.utdallas.cs.app.database.sse.SSEPublisher;
import edu.utdallas.cs.app.database.table.CapturedPollutant;
import edu.utdallas.cs.app.database.table.Pollutant;
import edu.utdallas.cs.app.database.table.Sensor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class FetchDataTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchDataTask.class);

    private final SessionFactory sessionFactory;
    private final APIRepository apiRepository;
    private final SSEPublisher<CapturedPollutantUpdate> publisher;

    public FetchDataTask(SessionFactory sessionFactory, APIRepository apiRepository, SSEPublisher<CapturedPollutantUpdate> publisher) {
        this.sessionFactory = sessionFactory;
        this.apiRepository = apiRepository;
        this.publisher = publisher;
    }

    @Override
    public void run() {
        try (Session session = sessionFactory.withOptions()
                .jdbcTimeZone(TimeZone.getTimeZone("UTC"))
                .openSession()) {
            List<Sensor> sensors = fetchListOfSensors(session);

            Collection<Integer> updatedSensorIds = new HashSet<>();
            AtomicInteger total = new AtomicInteger();

            for (Sensor sensor : sensors) {
                /* todo in the future, if this solution is not scalable, a queued approach will need to be used, where
                   we enqueue all these fetch data requests to a separate thread and execute there. If that is still
                   not enough, a multi-threaded approach would need to be used to handle the queue concurrently. */
                for (PollutantType pollutant : apiRepository.getSupportedPollutants()) {
                    CapturedPollutant capturedPollutant = new CapturedPollutant();

                    for (APIAdapter client : apiRepository.getAdapters(pollutant)) {
                        try {
                            Optional<APIReading> dataOpt = client.fetchData(sensor, pollutant);
                            if (dataOpt.isPresent()) {
                                APIReading data = dataOpt.get();
                                capturedPollutant.setSensor(sensor);
                                capturedPollutant.setPollutant(fetchPollutant(session, pollutant));
                                capturedPollutant.setDatetime(data.getDateTime());
                                capturedPollutant.setValue(data.getValue());

                                saveCapturedPollutant(session, capturedPollutant);
                                publisher.publish(CapturedPollutantUpdate.fromDatabaseRow(capturedPollutant));

                                updatedSensorIds.add(sensor.getId());
                                total.getAndIncrement();

                                break; // stop trying to get data for the pollutant the first moment we get valid data
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            LOGGER.info("Fetched " + total + " pollutants for " + updatedSensorIds.size() + " sensors.");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    private Pollutant fetchPollutant(Session session, PollutantType pollutant) {
        Transaction tx = session.beginTransaction();
        Pollutant pollutantEntity = session.createQuery("SELECT p FROM Pollutant p WHERE p.id = :id", Pollutant.class)
                .setParameter("id", pollutant.getPollutantId())
                .uniqueResult();
        tx.commit();
        return pollutantEntity;
    }

    private List<Sensor> fetchListOfSensors(Session session) {
        Transaction tx = session.beginTransaction();
        List<Sensor> sensors = session.createQuery("SELECT s FROM Sensor s", Sensor.class)
                .list();
        tx.commit();
        return sensors;
    }

    private void saveCapturedPollutant(Session session, CapturedPollutant capturedPollutant) {
        Transaction tx = session.beginTransaction();
        session.persist(capturedPollutant);
        tx.commit();
    }
}
