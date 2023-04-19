package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.api.APIReading;
import cs.utdallas.edu.app.database.api.APIRepository;
import cs.utdallas.edu.app.database.table.CapturedPollutant;
import cs.utdallas.edu.app.database.table.Pollutant;
import cs.utdallas.edu.app.database.table.Sensor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import spark.Spark;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class FetchDataTask implements Runnable {
    private final SessionFactory sessionFactory;
    private final APIRepository apiRepository;
    private final String webhookSecret;
    private long lastRun = 0;

    public FetchDataTask(SessionFactory sessionFactory, APIRepository apiRepository, String webhookSecret) {
        this.sessionFactory = sessionFactory;
        this.apiRepository = apiRepository;
        this.webhookSecret = webhookSecret;
    }

    @Override
    public void run() {
        try (Session session = sessionFactory.openSession()) {
            List<Sensor> sensors = fetchListOfSensors(session);

            Collection<Integer> updatedSensorIds = new HashSet<>();
            AtomicInteger total = new AtomicInteger();

            for (Sensor sensor : sensors) {
                /* todo in the future, if this solution is not scalable, a queued approach will need to be used, where
                   we enqueue all these fetch data requests to a separate thread and execute there. If that is still
                   not enough, a multi-threaded approach would need to be used to handle the queue concurrently. */
                for (PollutantType pollutant : apiRepository.getSupportedPollutants()) {
                    CapturedPollutant capturedPollutant = new CapturedPollutant();

                    apiRepository.getClients(pollutant).forEach(client -> {
                        try {
                            Optional<APIReading> dataOpt = client.fetchData(lastRun, sensor, pollutant);
                            if (dataOpt.isPresent()) {
                                APIReading data = dataOpt.get();
                                capturedPollutant.setSensor(sensor);
                                capturedPollutant.setPollutant(fetchPollutant(session, pollutant));
                                capturedPollutant.setDatetime(data.getDateTime());
                                capturedPollutant.setValue(data.getValue());

                                saveCapturedPollutant(session, capturedPollutant);
                                updatedSensorIds.add(sensor.getId());
                                total.getAndIncrement();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            // Send webhook
            String sensorIds = String.join(",", updatedSensorIds.stream().map(Object::toString).toArray(String[]::new));
            String message = String.format("Database updated for sensors %s", sensorIds);
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            Spark.post("/webhook", (req, res) -> {
                String secret = req.headers("X-Webhook-Secret");
                if (!webhookSecret.equals(secret)) {
                    res.status(401);
                    return "Unauthorized";
                }
                return encodedMessage;
            });

            System.out.println("Fetched " + total + " pollutants for " + updatedSensorIds.size() + " sensors.");
            lastRun = System.currentTimeMillis();
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
