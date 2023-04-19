package cs.utdallas.edu.app.database;

import cs.utdallas.edu.app.database.data.SensorTable;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryMaker {
    private static SessionFactory factory;

    private SessionFactoryMaker() {
    }

    private static void configureFactory() {
        try {
            factory = new Configuration()
                    .addAnnotatedClass(SensorTable.class)
                    .configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getFactory() {
        if (factory == null) {
            configureFactory();
        }
        return factory;
    }

}
