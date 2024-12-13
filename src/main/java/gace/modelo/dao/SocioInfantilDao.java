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

public class SocioInfantilDao implements DAO<SocioInfantil> {

    public void insertar(SocioInfantil socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Crear Socio Infantil: " + e.getMessage());
        }
    }


    public void modificar(SocioInfantil socio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error al Modificar Socio Infantil: " + e.getMessage());
        }
    }


    public void eliminar(int idSocio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            SocioInfantil socio = session.get(SocioInfantil.class, idSocio);
            if (socio != null) {
                session.remove(socio);
                transaction.commit();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }



    //TODO: LAS FUNCIONES DE ABAJO A HIBERNATE
    public SocioInfantil buscar(int idSocio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SocioInfantil.class, idSocio);
        } catch (Exception e) {
            System.err.println("Error al Buscar Socio Infantil: " + e.getMessage());
            return null;
        }
    }

    public List<SocioInfantil> buscarLista(ArrayList<Integer> ids){
        List<SocioInfantil> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioInfantil WHERE idSocio IN (:ids)";
            Query<SocioInfantil> query = session.createQuery(hql, SocioInfantil.class);
            query.setParameter("ids", ids);
            socios = query.list();
        } catch (Exception e) {
            System.err.println("Error al Buscar Lista de Socios: " + e.getMessage());
            return null;
        }
        return socios;
    }

    public List<SocioInfantil> listar() {
        List<SocioInfantil> socios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM SocioInfantil";
            Query<SocioInfantil> query = session.createQuery(hql, SocioInfantil.class);
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
