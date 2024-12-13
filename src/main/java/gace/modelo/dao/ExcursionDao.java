package gace.modelo.dao;

import gace.modelo.Excursion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import gace.modelo.utils.HibernateUtil;

import java.util.ArrayList;

public class ExcursionDao implements DAO<Excursion> {

    public void insertar(Excursion excursion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(excursion);
            transaction.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void modificar(Excursion excursion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(excursion);
            transaction.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void eliminar(int id_excursion) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Excursion excursion = session.get(Excursion.class, id_excursion);
            if(excursion == null) {
                return;
            }
            session.remove(excursion);
            transaction.commit();
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
    }
        //revisar cal commit?
    public void eliminar(String codigo) {
        Excursion exc = buscar(codigo);
        if (exc != null) {
            eliminar(exc.getId());
        }
    }

    public Excursion buscar(int id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Excursion.class, id);
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
        return null;
    }

    public Excursion buscar(String codigo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Excursion e WHERE e.codigo = :codigo";
            return session.createQuery(hql, Excursion.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al buscar excursión: " + e.getMessage());
        }
        return null;
    }


    public ArrayList<Excursion> listar() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Excursion> query = session.createQuery("FROM Excursion", Excursion.class);
            return (ArrayList<Excursion>) query.list();
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
        return null;
    }
    public int cancelar(Excursion exc) {
        int inscripcionesBorradas = 0;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            String sql = "CALL EliminarExcursionConInscripciones(:id)";
            NativeQuery<?> query = session.createNativeQuery(sql, Integer.class);
            query.setParameter("id", exc.getId());

            Object result = query.getSingleResult();
            if (result != null) {
                inscripcionesBorradas = ((Number) result).intValue();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            System.err.println("Error: " + e.getCause() + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
        return inscripcionesBorradas;
    }

}
