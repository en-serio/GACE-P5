package gace.modelo.dao;

import gace.modelo.*;
import gace.modelo.utils.BBDDUtil;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.tool.schema.internal.IndividuallySchemaMigratorImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscripcionDao implements DAO<Inscripcion>{

    public void insertar(Inscripcion inscripcion) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(inscripcion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al insertar inscripcion: " + e.getMessage());
        }
    }

    public void modificar(Inscripcion inscripcion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(inscripcion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al insertar inscripcion: " + e.getMessage());
        }
    }

    public void eliminar(int idInscripcion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Inscripcion insc = session.get(Inscripcion.class, idInscripcion);
            if (insc != null) {
                session.remove(insc);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al insertar inscripcion: " + e.getMessage());
        }
    }

    public void eliminar(String codigo) {
        Inscripcion insc = buscar(codigo);
        if (insc != null) {
            eliminar(insc.getIdInscripcion());
        }
    }

    public Inscripcion buscar(int idInscripcion) {
        Session session = null;
        Inscripcion insc = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            insc = session.get(Inscripcion.class, idInscripcion);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return insc;
    }

    public List<Inscripcion> listarXExc(Excursion excursion) {
        Session session = null;
        List<Inscripcion> inscripciones = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Inscripcion i WHERE i.excursion = :excursion";
            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("excursion", excursion);
            inscripciones = query.getResultList();
            if (inscripciones.isEmpty()) {
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return inscripciones;
    }

    public Inscripcion buscar(String codigo) {
        Session session = null;
        Inscripcion insc = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.socio s " +
                    "JOIN FETCH i.excursion e " +
                    "LEFT JOIN FETCH s.estandar se " +
                    "LEFT JOIN FETCH s.federado sf " +
                    "LEFT JOIN FETCH s.infantil si " +
                    "WHERE i.codigo = :codigo";

            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("codigo", codigo);

            insc = query.uniqueResult();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return insc;
    }

    //TODO SE USA??
    public List<Inscripcion> listarInscXExc(Excursion excursion) {
        Session session = null;
        List<Inscripcion> inscripciones = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.socio s " +
                    "JOIN FETCH i.excursion e " +
                    "WHERE e.id = :excursionId";

            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("excursionId", excursion.getId());

            inscripciones = query.getResultList();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return inscripciones;
    }

    //TODO SE PUEDE DEJAR EN UNA SOLA FUNCINO??
    public List<Inscripcion> ListarXSocioEst(Socio socio) {
        Session session = null;
        List<Inscripcion> inscripciones = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.socio s " +
                    "JOIN FETCH i.excursion e " +
                    "WHERE s.idSocio = :socioId " +
                    "AND FUNCTION('MONTH', e.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
                    "AND FUNCTION('YEAR', e.fecha) = FUNCTION('YEAR', CURRENT_DATE)";
            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("socioId", socio.getIdSocio());

            inscripciones = query.getResultList();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return inscripciones.isEmpty() ? null : inscripciones;
    }

    public List<Inscripcion> ListarXSocioInf(Socio socio) {
        Session session = null;
        List<Inscripcion> inscripciones = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.socio s " +
                    "JOIN FETCH i.excursion e " +
                    "WHERE s.idSocio = :socioId " +
                    "AND FUNCTION('MONTH', i.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
                    "AND FUNCTION('YEAR', i.fecha) = FUNCTION('YEAR', CURRENT_DATE)";
            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("socioId", socio.getIdSocio());

            inscripciones = query.getResultList();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return inscripciones.isEmpty() ? null : inscripciones;
    }

    public List<Inscripcion> ListarXSocioFed(Socio socio) {
        Session session = null;
        List<Inscripcion> inscripciones = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.socio s " +
                    "JOIN FETCH i.excursion e " +
                    "WHERE s.idSocio = :socioId " +
                    "AND FUNCTION('MONTH', i.fecha) = FUNCTION('MONTH', CURRENT_DATE) " +
                    "AND FUNCTION('YEAR', i.fecha) = FUNCTION('YEAR', CURRENT_DATE)";
            Query<Inscripcion> query = session.createQuery(hql, Inscripcion.class);
            query.setParameter("socioId", socio.getIdSocio());

            inscripciones = query.getResultList();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return inscripciones.isEmpty() ? null : inscripciones;
    }
}

