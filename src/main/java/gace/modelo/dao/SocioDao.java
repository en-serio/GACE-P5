package gace.modelo.dao;

import gace.modelo.*;
import gace.modelo.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SocioDao {

    public void insertar(Socio socio) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(socio);
//            if (socio instanceof SocioEstandar) {
//                DAOFactory.getSocioEstandarDao().insertar((SocioEstandar) socio);
//            } else if (socio instanceof SocioFederado) {
//                DAOFactory.getSocioFederadoDao().insertar((SocioFederado) socio);
//            } else {
//                DAOFactory.getSocioInfantilDao().insertar((SocioInfantil) socio);
//            }

            transaction.commit();
        } catch (Exception e) {
            System.err.println("Error al insertar el socio: " + e.getMessage());
            if (transaction != null) transaction.rollback();
        }
    }

    public void modificar(Socio socio) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(socio);
            if (socio instanceof SocioEstandar) {
                DAOFactory.getSocioEstandarDao().modificar((SocioEstandar) socio);
            } else if (socio instanceof SocioFederado) {
                DAOFactory.getSocioFederadoDao().modificar((SocioFederado) socio);
            } else {
                DAOFactory.getSocioInfantilDao().modificar((SocioInfantil) socio);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al modificar el socio: " + e.getMessage());
        }
    }

    public void eliminar(int idSocio) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Socio socio = session.get(Socio.class, idSocio);
//            if (socio instanceof SocioEstandar) {
//                DAOFactory.getSocioEstandarDao().eliminar(idSocio);
//            } else if (socio instanceof SocioFederado) {
//                DAOFactory.getSocioFederadoDao().eliminar(idSocio);
//            } else if (socio instanceof SocioInfantil) {
//                DAOFactory.getSocioInfantilDao().eliminar(idSocio);
//            }

            if (socio != null) {
                session.remove(socio);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al eliminar el socio: " + e.getMessage());
        }
    }

    public Socio buscar(int idSocio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Socio.class, idSocio);
        } catch (Exception e) {
            System.err.println("Error al buscar el socio: " + e.getMessage());
            return null;
        }
    }

    public Socio buscar(String nif) {
        int id = DAOFactory.getSocioEstandarDao().comprobarEst(nif);
        if (id != -1) {
            return DAOFactory.getSocioEstandarDao().buscar(id);
        }

        id = DAOFactory.getSocioFederadoDao().comprobarFed(nif);
        if (id != -1) {
            return DAOFactory.getSocioFederadoDao().buscar(id);
        }

        return null;
    }

    public boolean hayNif(String nif) {
        return buscar(nif) != null;
    }

    //todo ?
//    public List<Socio> buscarLista(List<Integer> ids) {
//        List<Socio> socios = new ArrayList<>();
//
//        socios.addAll(DAOFactory.getSocioEstandarDao().buscarLista((ArrayList<Integer>) ids));
//        socios.addAll(DAOFactory.getSocioFederadoDao().buscarLista((ArrayList<Integer>) ids));
//        socios.addAll(DAOFactory.getSocioInfantilDao().buscarLista((ArrayList<Integer>) ids));
//
//        socios.sort(Comparator.comparingInt(Socio::getIdSocio));
//        return socios.isEmpty() ? null : socios;
//    }

    // Método para listar todos los Socios
    public List<Socio> listar() {
        List<Socio> socios = new ArrayList<>();
        List<SocioEstandar> auxest = DAOFactory.getSocioEstandarDao().listar();
        if(auxest != null) {
            socios.addAll(auxest);
        }
        List<SocioFederado> auxfed = DAOFactory.getSocioFederadoDao().listar();
        if(auxfed != null) {
            socios.addAll(auxfed);
        }
        List<SocioInfantil> auxinf = DAOFactory.getSocioInfantilDao().listar();
        if(auxinf != null) {
            socios.addAll(auxinf);
        }

        socios.sort(Comparator.comparingInt(Socio::getIdSocio));
        return socios.isEmpty() ? null : socios;
    }
}
