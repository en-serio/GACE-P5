package gace.modelo.dao;

import gace.modelo.Federacion;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;

public class FederacionDao implements DAO<Federacion> {

    public void insertar(Federacion federacion) {
        Federacion fed = buscar(federacion.getCodigo());
        if(fed == null){
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(federacion);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                System.err.println("Error al insertar federación: " + e.getMessage());
            }
        }else{
            System.err.println("Ya existe una federación con el código: "+federacion.getCodigo());
        }

    }

    
    public void modificar(Federacion federacion) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(federacion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al modificar federación: " + e.getMessage());
        }
    }

    
    public void eliminar(int idFederacion) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Federacion federacion = session.get(Federacion.class, idFederacion);
            if (federacion != null) {
                session.remove(federacion);
                transaction.commit();
            } else {
                System.err.println("No se encontró la federación con ID: " + idFederacion);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al eliminar federación: " + e.getMessage());
        }
    }

    public void eliminar(String codigo) {
        Federacion federacion = buscar(codigo);
        if (federacion != null) {
            eliminar(federacion.getIdFederacion());
        }
    }

    public Federacion buscar(int idFederacion) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Federacion.class, idFederacion);
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
        return null;
    }

    public Federacion buscar(String codigo) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "FROM Federacion f WHERE f.codigo = :codigo";
            return session.createQuery(sql, Federacion.class).setParameter("codigo", codigo).getSingleResult();
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
        return null;
    }

    public ArrayList<Federacion> listar() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Federacion> query = session.createQuery("FROM Federacion", Federacion.class);
            return (ArrayList<Federacion>) query.list();
        } catch (Exception e) {
            System.err.println(e.getCause()+e.getMessage());
        }
        return null;
    }
}
