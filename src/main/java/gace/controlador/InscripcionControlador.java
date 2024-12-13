package gace.controlador;

import gace.modelo.*;
import gace.modelo.dao.DAOFactory;
import gace.modelo.dao.InscripcionDao;
import gace.vista.DatosUtil;
import gace.vista.VistaInscripciones;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InscripcionControlador {
    private final VistaInscripciones vistaInscripciones;
    private SocioControlador socioControlador;
    private ExcursionControlador excursionControlador;
    private InscripcionDao inscripcionDao;
    private DatosUtil datosUtil;

    public InscripcionControlador(ExcursionControlador excursionControlador, SocioControlador socioControlador) {
        this.vistaInscripciones = new VistaInscripciones();
        this.socioControlador = socioControlador;
        this.excursionControlador = excursionControlador;
        this.datosUtil = new DatosUtil();
    }
    public InscripcionControlador(){
        this.vistaInscripciones = new VistaInscripciones();
    }

    public void setInscripcionControlador(SocioControlador socioControlador) {
        this.socioControlador = socioControlador;
    }

    public void setExcursionControlador(ExcursionControlador excursionControlador) {
        this.excursionControlador = excursionControlador;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {

    }
    public void mostrarInscripcion(Inscripcion inscripcion) {

    }
    public void eliminarInscripcion(Inscripcion inscripcion){ //repasar condicion de permite eliminar si?

    }


/*    public boolean mostrarExcVacia(int ayuda){
        ArrayList<Excursion> excursionesSin = new ArrayList<>();
        for (Excursion excursion : this.excursionControlador.getListaExcursion().getListaExcursiones()) {
            boolean tieneInscripcion = false;
            for (Inscripcion inscripcion : this.listaInscripcion.getListaInsc()) {
                if (inscripcion.getExcursion()!= null && inscripcion.getExcursion().equals(excursion)) {
                    tieneInscripcion = true;
                    break;
                }
            }
            if (!tieneInscripcion) {
                excursionesSin.add(excursion);
            }
        }
        if(excursionesSin.isEmpty()){
            datosUtil.mostrarError("No hay excursiones sin inscripciones");
            return false;
        }
        if(ayuda == 1){
            excursionControlador.mostrar(excursionesSin);
        }
        return excursionControlador.seleccionarExc(excursionesSin);
    }*/


    public int solicitarAyudaVisual() {
        return vistaInscripciones.vistaAyuda();
    }

    public boolean novaInscripcio(int ayuda) {
        if (ayuda == 1) {
            if(!excursionControlador.mostrarExcursiones()){
                return false;
            }
        }
        int idExcursion = vistaInscripciones.pedirExcursionInsc();
        if (idExcursion == -1) {
            return false;
        }
        Excursion exc = this.excursionControlador.buscarExcursion(idExcursion);
        if(exc == null){
            datosUtil.mostrarError("Excursión no encontrada");
            return false;
        }
        int tipo = datosUtil.pedirOpcion("¿Elegir socio o Crear uno nuevo?", "Elegir socio", "Crear nuevo socio");
        if(tipo == 2) {
            Socio soc = socioControlador.crearSocio();
        } else if (tipo == 0) {
            return false;
        }
        if(ayuda == 1){
            boolean noHay = socioControlador.mostrarSocios(0,4);
            if(!noHay){
                return false;
            }
        }else{
            List<Socio> todos = DAOFactory.getSocioDao().listar();
            if(todos == null){
                datosUtil.mostrarError("No hay socios");
                return false;
            }
        }
        int strSocio = vistaInscripciones.pedirSocioInsc();
        if (strSocio == 0) {
            return false;
        }
        Socio soc = this.socioControlador.buscarSocio(strSocio);
        if(soc == null){
            datosUtil.mostrarError("Socio no encontrado");
            return false;
        }
        String codigo = getCodigoExcursion(soc.getIdSocio(), exc.getCodigo());
        Inscripcion ins = new Inscripcion(codigo, soc, exc);

        DAOFactory.getInscripcionDao().insertar(ins);
        vistaInscripciones.mostrarInscripciones(ins.toString());
        return true;
    }

    public String getCodigoExcursion(int socNoSocio, String excCodigo){
        return socNoSocio + excCodigo;
    }

    public boolean mostrarInscripcionesXExc() {
        boolean hayExc = excursionControlador.mostrarExcursiones();
        if(!hayExc){
            return false;
        }
        Excursion exc = new Excursion();
        int opcion = datosUtil.pedirOpcion("Seleccionar Excursión", "Por Código", "Por ID");
        if(opcion == 0){
            return false;
        }else if(opcion == 1) {
            String codigo = datosUtil.devString("Introduce el código de la excursión");
            exc = DAOFactory.getExcursionDao().buscar(codigo);
            if (exc == null) {
                datosUtil.mostrarError("Excursión no encontrada");
                return false;
            }
        }else{
            int idExcursion = datosUtil.leerEntero(99999, "Introduce el ID de la excursión:\n");
            exc = DAOFactory.getExcursionDao().buscar(idExcursion);
            if (exc == null) {
                datosUtil.mostrarError("Excursión no encontrada");
                return false;
            }
        }
        List<Inscripcion> inscripcions = DAOFactory.getInscripcionDao().listarXExc(exc);
        /*
        Aquesta funció ha de buscar tots els socis en una única cerca, no borrar no utilitzar
        ArrayList<Inscripcion> inscripcions = DAOFactory.getInscripcionDao().listarInscXExc(exc);
        */
        if(inscripcions == null){
            datosUtil.mostrarError("No hay inscripciones para esta excursión");
            return false;
        }
        for (Inscripcion inscripcion : inscripcions) {
            vistaInscripciones.mostrarInscripciones(inscripcion.toString());
        }
        return true;
    }

    public Inscripcion buscarInscripcion() {
        int buscar = datosUtil.pedirOpcion("Buscar Inscripcion", "Por Excursion", "Por Socio");
        if(buscar == 0){
            return null;
        }
        Excursion excursion = null;
        Socio socio = null;
        int idInscripcion = 0;
        List<Inscripcion> insc = null;
        Inscripcion inscripcion = null;
        if(buscar == 1){
            if(!mostrarInscripcionesXExc()){
                return null;
            }
        } else {
            socioControlador.mostrarSocios(0, 4);
            socio = socioControlador.obtenerSocio();
            if(socio == null){
                return null;
            }else if(socio instanceof SocioEstandar) {
                insc = DAOFactory.getInscripcionDao().ListarXSocioEst(socio);
            }else if(socio instanceof SocioFederado){
                insc = DAOFactory.getInscripcionDao().ListarXSocioFed(socio);
            }else{
                insc = DAOFactory.getInscripcionDao().ListarXSocioInf(socio);
            }
            if(insc == null){
                datosUtil.mostrarError("No hay inscripciones para este socio");
                return null;
            }
            mostrarLista(insc);
            idInscripcion = datosUtil.leerEntero(99999, "Introduce el ID de la inscripción: ");
            return DAOFactory.getInscripcionDao().buscar(idInscripcion);
        }
        idInscripcion = datosUtil.leerEntero(99999, "Introduce el ID de la inscripción: ");
        return DAOFactory.getInscripcionDao().buscar(idInscripcion);
    }

    public void mostrarLista(List<Inscripcion> inscripciones){
        for(Inscripcion inscripcion : inscripciones){
            vistaInscripciones.mostrarInscripciones(inscripcion.toString());
        }
    }

    public Inscripcion buscarLista(ArrayList<Inscripcion> inscripciones, Socio socio){
        for(Inscripcion inscripcion : inscripciones){
            if(inscripcion.getSocio().equals(socio)){
                vistaInscripciones.mostrarInscripciones(inscripcion.toString());
                return inscripcion;
            }
        }
        return null;
    }
    public Inscripcion buscarLista(ArrayList<Inscripcion> inscripciones, Excursion exc){
        for(Inscripcion inscripcion : inscripciones){
            if(inscripcion.getExcursion().equals(exc)){
                vistaInscripciones.mostrarInscripciones(inscripcion.toString());
                return inscripcion;
            }
        }
        return null;
    }

    public boolean eliminarSocio(){
        Socio socio = socioControlador.obtenerSocio();
        if(socio == null){
            return false;
        }

        if(datosUtil.confirmar("¿Estás seguro de que quieres eliminar este socio?")){
            DAOFactory.getSocioDao().eliminar(socio.getIdSocio());
            datosUtil.mostrarError("Socio eliminado");
            return true;
        }
        return false;
    }


    public boolean eliminarInscripcion(){
        Inscripcion insc = buscarInscripcion();
        if(insc == null){
            datosUtil.mostrarError("Inscripción no encontrada");
            return false;
        }
        if(!compararFecha(insc.getExcursion().getFecha())){
            datosUtil.mostrarError("No se puede eliminar una inscripción de una excursión que ya ha pasado");
            return false;
        }
        DAOFactory.getInscripcionDao().eliminar(insc.getIdInscripcion());
        return true;
    }

    public boolean compararFecha(Date fecha){
        Date fechaActual = new Date();
        return fecha.after(fechaActual) || !fecha.equals(fechaActual);
    }

//    public boolean mostrarSinInscripciones(int ayuda) {
//        ArrayList<Socio> sociosSin =  new ArrayList<>();
//        for(Socio socio : socioControlador.getLista().getListaSocios()) {
//            boolean tieneInscripcion = false;
//            for(Inscripcion inscripcion : this.listaInscripcion.getListaInsc()) {
//                if(inscripcion.getSocio().equals(socio)){
//                    tieneInscripcion = true;
//                    break;
//                }
//            }
//            if(!tieneInscripcion){
//                sociosSin.add(socio);
//            }
//        }
//        if(ayuda == 1){
//            socioControlador.mostrarListaSociosSelec(sociosSin);
//        }
//        return socioControlador.seleccionarSocio(sociosSin);
//    }
    public boolean calcularCuota(){
        Socio socio = socioControlador.obtenerSocio();
        if(socio == null){
            return false;
        }
        List<Inscripcion> inscripcions = null;
        if(socio instanceof SocioEstandar){
            inscripcions = DAOFactory.getInscripcionDao().ListarXSocioEst(socio);
        }else if(socio instanceof SocioFederado){
            inscripcions = DAOFactory.getInscripcionDao().ListarXSocioFed(socio);
        } else {
            inscripcions = DAOFactory.getInscripcionDao().ListarXSocioInf(socio);
        }
        double totalExc = 0;
        int nExc =0;
        if(inscripcions != null){
            for(Inscripcion insc : inscripcions){
                System.out.println(insc.toString());
                totalExc += insc.getExcursion().getPrecio();
            }
            nExc = inscripcions.size();
        }
        double cuota = socio.calcularCuota();
        totalExc = socio.costeExcursion(totalExc);
        vistaInscripciones.mostrarCuota(cuota, totalExc, nExc);
        return true;
    }

}
