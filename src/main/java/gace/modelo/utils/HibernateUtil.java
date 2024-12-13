package gace.modelo.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import gace.modelo.*;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Excursion.class)
                    .addAnnotatedClass(Federacion.class)
                    .addAnnotatedClass(Inscripcion.class)
                    .addAnnotatedClass(Seguro.class)
                    .addAnnotatedClass(Socio.class)
                    .addAnnotatedClass(SocioEstandar.class)
                    .addAnnotatedClass(SocioFederado.class)
                    .addAnnotatedClass(SocioInfantil.class)
                    .setProperty(AvailableSettings.SHOW_SQL, true)
                    .setProperty(AvailableSettings.FORMAT_SQL, true)
                    .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        getSessionFactory().close();
    }
}
