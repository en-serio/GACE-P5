package gace.modelo.dao;

import gace.modelo.Seguro;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SeguroDao {

    public void insertar(Seguro seguro) {

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(seguro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al insertar el seguro: " + e.getMessage());
        }
    }

    public void modificar(Seguro seguro) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(seguro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al modificar el seguro: " + e.getMessage());
        }
    }

    public void eliminar(int idSeguro) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Seguro seguro = session.get(Seguro.class, idSeguro);
            if (seguro != null) {
                session.remove(seguro);
                System.out.println("Seguro eliminado exitosamente.");
            } else {
                System.out.println("Seguro no encontrado.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al eliminar el seguro: " + e.getMessage());
        }
    }

    public Seguro buscar(int idSeguro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Seguro.class, idSeguro);
        } catch (Exception e) {
            System.err.println("Error al buscar el seguro: " + e.getMessage());
            return null;
        }
    }

    // Método para listar todos los Seguros en la base de datos
    public List<Seguro> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Seguro";
            Query<Seguro> query = session.createQuery(hql, Seguro.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error al listar los seguros: " + e.getMessage());
            return null;
        }
    }
}
