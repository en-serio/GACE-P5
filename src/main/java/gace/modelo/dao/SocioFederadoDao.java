package gace.modelo.dao;

import gace.modelo.*;
import gace.modelo.utils.BBDDUtil;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SocioFederadoDao implements DAO<SocioFederado>{

    public void insertar(SocioFederado socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Crear Socio Federado: " + e.getMessage());
        }
    }

    public void modificar(SocioFederado socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Modificar Socio Federado: " + e.getMessage());
        }
    }

    public void eliminar(int idSocio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            SocioFederado socio = session.get(SocioFederado.class, idSocio);
            if (socio != null) {
                session.remove(socio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Eliminar Socio Federado: " + e.getMessage());
        }
    }

    public SocioFederado buscar(int idSocio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SocioFederado.class, idSocio);
        } catch (Exception e) {
            System.err.println("Error al Buscar inscripcion: " + e.getMessage());
            return null;
        }
    }

    public int comprobarFed(String nif) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT idSocio FROM SocioFederado WHERE nif = :nif";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("nif", nif);
            Integer idSocio = query.uniqueResult();
            return idSocio != null ? idSocio : -1;
        } catch (Exception e) {
            System.err.println("Error al Comprobar Nif: " + e.getMessage());
            return -1;
        }
    }

    public SocioFederado buscar(String nif) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioFederado WHERE nif = :nif";
            Query<SocioFederado> query = session.createQuery(hql, SocioFederado.class);
            query.setParameter("nif", nif);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error al Buscar por Nif: " + e.getMessage());
            return null;
        }
    }

    public List<SocioFederado> buscarLista(ArrayList<Integer> ids){
        List<SocioFederado> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioFederado WHERE idSocio IN (:ids)";
            Query<SocioFederado> query = session.createQuery(hql, SocioFederado.class);
            query.setParameter("ids", ids);
            socios = query.list();
        } catch (Exception e) {
            System.err.println("Error al Buscar Socios: " + e.getMessage());
            return null;
        }
        return socios;
    }

    public List<SocioFederado> listar() {
        List<SocioFederado> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioFederado f " +
                    "JOIN FETCH f.federacion fed ";
            Query<SocioFederado> query = session.createQuery(hql, SocioFederado.class);
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
