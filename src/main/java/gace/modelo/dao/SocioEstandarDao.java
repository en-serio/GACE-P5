package gace.modelo.dao;

import gace.modelo.*;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;

public class SocioEstandarDao implements DAO<SocioEstandar> {

    public void insertar(SocioEstandar socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(socio);
            tx.commit();
        } catch (Exception e) {
            System.err.println("Error al Crear Socio Estandar: " + e.getMessage());
            if (tx != null) tx.rollback();
        }
    }

    public void modificar(SocioEstandar socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Modificar Socio Estandar: " + e.getMessage());
        }
    }

    public void eliminar(int idSocio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            SocioEstandar socio = session.get(SocioEstandar.class, idSocio);
            if (socio != null) {
                session.remove(socio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Eliminar Socio Estandar: " + e.getMessage());
        }
    }

    public SocioEstandar buscar(int idSocio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SocioEstandar.class, idSocio);
        } catch (Exception e) {
            System.err.println("Error al Buscar Socio Estandar: " + e.getMessage());
            return null;
        }
    }

    public List<SocioEstandar> buscarLista(List<Integer> ids) {
        List<SocioEstandar> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioEstandar WHERE idSocio IN (:ids)";
            Query<SocioEstandar> query = session.createQuery(hql, SocioEstandar.class);
            query.setParameter("ids", ids);
            socios = query.list();
        } catch (Exception e) {
            System.err.println("Error al Buscar lista de socios Estandar: " + e.getMessage());
            return null;
        }
        return socios;
    }

    public int comprobarEst(String nif) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT idSocio FROM SocioEstandar WHERE nif = :nif";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("nif", nif);
            Integer idSocio = query.uniqueResult();
            return idSocio != null ? idSocio : -1;
        } catch (Exception e) {
            System.err.println("Error al Comprobar Nif: " + e.getMessage());
            return -1;
        }
    }

    public SocioEstandar buscar(String nif) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioEstandar WHERE nif = :nif";
            Query<SocioEstandar> query = session.createQuery(hql, SocioEstandar.class);
            query.setParameter("nif", nif);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error al Buscar por Nif: " + e.getMessage());
            return null;
        }
    }

    public List<SocioEstandar> listar() {
        List<SocioEstandar> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioEstandar e " +
                    "JOIN FETCH e.seguro seg ";
            Query<SocioEstandar> query = session.createQuery(hql, SocioEstandar.class);
            socios = query.list();
            if (socios.isEmpty()) {
                return null;
            }
            return socios;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}