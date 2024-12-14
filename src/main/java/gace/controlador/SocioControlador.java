package gace.controlador;


import gace.modelo.*;
import gace.modelo.dao.DAOFactory;
import gace.vista.VistaSocios;
import gace.vista.DatosUtil;

import java.util.ArrayList;
import java.util.List;


public class SocioControlador {
    private VistaSocios vistaSocios;
    private DatosUtil datosUtil;

    public SocioControlador(VistaSocios vistaSocios) {
        this.vistaSocios = vistaSocios;
        this.datosUtil = new DatosUtil();
    }

    public SocioControlador() {
        this.vistaSocios = new VistaSocios();
        this.datosUtil = new DatosUtil();
    }

    public int nouSoci(){
        String strSocio = vistaSocios.formSocio();
        if (strSocio == null) {
            datosUtil.mostrarError("Error al crear el socio");
            return 0;
        }
        String[] datosSocio = strSocio.split(",");
        if (datosSocio.length < 3) {
            datosUtil.mostrarError("Datos del socio incompletos");
            return 0;
        }
        int tipoSocio = Integer.parseInt(datosSocio[0]);
        int id = 0;
        switch (tipoSocio) {
            //EST
            case 1:
                SocioEstandar socioEst = nouSociEstandar(datosSocio[1], datosSocio[2]);
                if (socioEst == null) {
                    datosUtil.mostrarError("Error al crear el socio estándar");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioEst);
                break;
            //FED
            case 2:
                SocioFederado socioFed = nouSociFederado(datosSocio[1], datosSocio[2]);
                if (socioFed == null) {
                    datosUtil.mostrarError("Error al crear el socio estándar");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioFed);
                break;
            //INF
            case 3:
                SocioInfantil socioInf = nouSociInfantil(datosSocio[1], datosSocio[2]);
                if (socioInf == null) {
                    datosUtil.mostrarError("Error al crear el socio infantil");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioInf);
                vistaSocios.mostrarSocio(socioInf.toString());
                break;
            default:
                datosUtil.mostrarError("Tipo de socio no válido");
                return 0;
        }
        return id;
    }

    public Socio crearSocio(){
        int id = nouSoci();
        return DAOFactory.getSocioDao().buscar(id);
    }


    public SocioEstandar nouSociEstandar(String nombre,String apellido){
        String nif = vistaSocios.formNif();
        if(nif == null){
            datosUtil.mostrarError("Nif no válido.");
            return null;
        }
        if(existeNif(nif)){
            datosUtil.mostrarError("Nif ya existe.");
            return null;
        }
        Seguro seg = nuevoSeg();
        if(seg == null){
            datosUtil.mostrarError("Seguro no válido.");
            return null;
        }
        DAOFactory.getSeguroDao().insertar(seg);
        return new SocioEstandar( nombre, apellido, nif, seg);
    }

    public SocioFederado nouSociFederado(String nombre, String apellido){
        String nif = vistaSocios.formNif();
        if(nif == null){
            datosUtil.mostrarError("Nif no válido.");
            return null;
        }
        if(existeNif(nif)){
            datosUtil.mostrarError("Nif ya existe.");
            return null;
        }
        Federacion fed = pedirFed();
        if(fed == null){
            datosUtil.mostrarError("Federación no válida.");
            return null;
        }
        //DAOFactory.getFederacionDao().insertar(fed);
        return new SocioFederado(nombre, apellido, nif, fed);
    }

    public boolean existeNif(String nif){
        return DAOFactory.getSocioDao().hayNif(nif);
    }

    public SocioInfantil nouSociInfantil(String nombre, String apellido){
        int noTutor = vistaSocios.formTutor();
        if(noTutor == 0){
            return null;
        }
        if(!buscarTutor(noTutor)){
            return null;
        }
        return new SocioInfantil(nombre, apellido, noTutor);
    }



//    public SocioInfantil nouSociInfantil(String nombre, String apellido, int noTutor) {
//        if(noTutor == 0){
//            return null;
//        }
//        if(!buscarTutor(noTutor)){
//            return null;
//        }
//        return new SocioInfantil(nombre, apellido, noTutor);
//    }

    public boolean buscarTutor(int noTutor) {
        Socio socio = DAOFactory.getSocioEstandarDao().buscar(noTutor);
        if(socio == null){
            socio = DAOFactory.getSocioFederadoDao().buscar(noTutor);
            if(socio == null){
                return false;
            }
        }
        return true;
    }

    public boolean mostrarSocios(int mostrarFiltro, int filtro) {
        int opcionSocios = 0;
        if(mostrarFiltro == 1){
            opcionSocios = vistaSocios.requerirFiltro();
        }else {
            opcionSocios = filtro;
        }
        switch (opcionSocios) {
            case 1:
                //error de tipos de socio amb el socioEstandarDao
                //list = DAOFactory.getSocioEstandarDao().listar();
                List<SocioEstandar> list = DAOFactory.getSocioEstandarDao().listar();
                if(list == null){
                    datosUtil.mostrarError("No hay socios estándar");
                    return false;
                }
                for(Socio socio : list) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 2:
                List<SocioFederado> listFed = DAOFactory.getSocioFederadoDao().listar();
                if(listFed == null){
                    datosUtil.mostrarError("No hay socios federados");
                    return false;
                }
                for(Socio socio : listFed) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 3:
                List<SocioInfantil> listInf = DAOFactory.getSocioInfantilDao().listar();
                if(listInf == null){
                    datosUtil.mostrarError("No hay socios infantiles");
                    return false;
                }
                for(Socio socio : listInf) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 4:
                List<Socio> todos = DAOFactory.getSocioDao().listar();
                if(todos == null){
                    datosUtil.mostrarError("No hay socios");
                    return false;
                }
                for(Socio socio : todos) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 0:
                break;
            default:
                datosUtil.mostrarError("Opción no válida. Intente de nuevo.");
        }
        return true;
    }

    public Socio buscarSocio(int noSocio) {
        return DAOFactory.getSocioDao().buscar(noSocio);
    }


    public Federacion pedirFed(){
        int accion = datosUtil.pedirOpcion("¿Desea seleccionar una federación ya existente o crear una nueva?", "Seleccionar", "Crear nueva");
        if(accion == -1){
            return null;
        }
        if(accion == 1){
            Federacion fed = seleccionarFed();
            if(fed != null){
                return fed;
            }
        }
        return nuevaFed();
    }

    public Federacion seleccionarFed(){
        ArrayList<Federacion> fedes = null;
        fedes = DAOFactory.getFederacionDao().listar();
        if(fedes.isEmpty()){
            datosUtil.mostrarError("No hay federaciones");
            return null;
        }else{
            for(Federacion fed : fedes){
                vistaSocios.mostrarSocio(fed.toString());
            }
            String codigo = datosUtil.devString("Introduce el código de la federación");
            if(codigo == null){
                return null;
            }
            return DAOFactory.getFederacionDao().buscar(codigo);
        }
    }
    public Federacion nuevaFed(){
        String fed = vistaSocios.formFederacion();
        String[] datosFed = fed.split(",");
        if (datosFed.length < 2) {
            datosUtil.mostrarError("Datos de la federación incompletos");
            return null;
        }
        Federacion federacion = new Federacion(datosFed[0], datosFed[1]);
        DAOFactory.getFederacionDao().insertar(federacion);
        return federacion;
    }

    public Seguro nuevoSeg(){
        String seg = vistaSocios.formSeguro();
        if (seg == null) {
            return null;
        }
        String[] datosSeg = seg.split(",");
        if (datosSeg.length < 2) {
            datosUtil.mostrarError("Datos del seguro incompletos");
            return null;
        }
        boolean tipo = Integer.parseInt(datosSeg[0]) == 1;
        return new Seguro(tipo, Double.parseDouble(datosSeg[1]));
    }

//    public boolean seleccionarSocio(ArrayList<Socio> socios){
//        String codigo = vistaSocios.pedirSocio();
//        for(Socio socio : socios){
//            if(socio.getNoSocio().equals(codigo)) {
//                vistaSocios.mostrarSocio("Es este el socio que desea eliminar " + socio.toString() + "?");
//                if (vistaSocios.confirmar()) {
//                    listaSocios.getListaSocios().remove(socio);
//                    datosUtil.mostrarError("Socio eliminado");
//                    return true;
//                }
//                return false;
//            }
//        }
//        return false;
//    }

    public boolean pedirSocio(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        vistaSocios.mostrarSocio(socio.toString());
        return true;
    }

    public boolean eliminarSocio(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        vistaSocios.mostrarSocio(socio.toString());
        List<Inscripcion> insc = null;
        if(socio instanceof SocioEstandar) {
            insc = DAOFactory.getInscripcionDao().ListarXSocioEst(socio);
        }else if(socio instanceof SocioFederado){
            insc = DAOFactory.getInscripcionDao().ListarXSocioFed(socio);
        }else{
            insc = DAOFactory.getInscripcionDao().ListarXSocioInf(socio);
        }
        if(insc != null){
            datosUtil.mostrarError("No se puede eliminar el socio, tiene inscripciones");
            return false;
        }
        if(vistaSocios.confirmar("¿Está seguro de que desea eliminar este socio?")){
            DAOFactory.getSocioDao().eliminar(socio.getIdSocio());
            datosUtil.mostrarError("Socio eliminado");
            return true;
        }
        return false;
    }

    public Socio obtenerSocio(){
        int formaBuscar = datosUtil.pedirOpcion("¿Como desea buscar?", "NIF", "Número de socio");
        int opcion = datosUtil.pedirOpcion("Deseas disponer de ayudas?","Sí","No");
        if(opcion == 1){
            mostrarSocios(0,4);
        }
        if (formaBuscar == -1) {
            return null;
        }
        Socio socio = null;
        if(formaBuscar == 1){
            socio = buscarNIF();
        }else{
            socio = buscarNoSocio();
        }
        if(socio == null){
            return null;
        }
        return socio;
    }

    public Socio buscarNoSocio(){
        int noSocio = vistaSocios.pedirSocio();
        if(noSocio == 0){
            return null;
        }
        Socio socio = DAOFactory.getSocioDao().buscar(noSocio);
        if(socio == null){
            datosUtil.mostrarError("Socio no encontrado");
            return null;
        }
        return socio;
    }

    public Socio buscarNIF(){
        String nif = vistaSocios.pedirNif();
        if(nif == null){
            return null;
        }
        Socio socio = DAOFactory.getSocioDao().buscar(nif);
        if(socio == null){
            datosUtil.mostrarError("Socio no encontrado");
            return null;
        }
        return socio;
    }

    public boolean modificarSeguro(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        if(!(socio instanceof SocioEstandar)){
            datosUtil.mostrarError("Error Socio no Estandar");
            return false;
        }
        String strSeg = vistaSocios.formSeguro();
        if (strSeg == null) {
            return false;
        }
        String[] datosSeg = strSeg.split(",");
        if (datosSeg.length < 2) {
            datosUtil.mostrarError("Datos del seguro incompletos");
            return false;
        }
        boolean tipo = Integer.parseInt(datosSeg[0]) == 1;
        Seguro seg = new Seguro(tipo, Double.parseDouble(datosSeg[1]));
        DAOFactory.getSeguroDao().insertar(seg);
        ((SocioEstandar) socio).setSeguro(seg);
        DAOFactory.getSocioEstandarDao().modificar((SocioEstandar) socio);
        return true;
    }
    public boolean modificarFederacion(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        if(!(socio instanceof SocioFederado)){
            datosUtil.mostrarError("Error socio no Federado");
            return false;
        }
        Federacion fed = pedirFed();
        ((SocioFederado) socio).setFederacion(fed);
        DAOFactory.getSocioFederadoDao().modificar((SocioFederado) socio);
        return true;
    }
}
