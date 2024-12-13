package gace.controlador;

import gace.modelo.utils.BBDDUtil;
import gace.vista.DatosUtil;
import gace.vista.PrimVista;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;

public class MenuControlador {
    private DatosUtil datosUtil;
    private ExcursionControlador excursionControlador;
    private SocioControlador socioControlador;
    private InscripcionControlador inscripcionControlador;


    public MenuControlador() {
        this.datosUtil = new DatosUtil();
        this.socioControlador = new SocioControlador();
        this.excursionControlador = new ExcursionControlador();
        this.inscripcionControlador = new InscripcionControlador(this.excursionControlador, this.socioControlador);
    }

    public MenuControlador(DatosUtil datosUtil) {
        this.datosUtil = datosUtil;
        this.socioControlador = new SocioControlador();
        this.excursionControlador = new ExcursionControlador();
        this.inscripcionControlador = new InscripcionControlador(this.excursionControlador, this.socioControlador);
    }

    public DatosUtil getDatosUtil() {
        return datosUtil;
    }

    public ExcursionControlador getExcursionControlador() {
        return excursionControlador;
    }

    public SocioControlador getSocioControlador() {
        return socioControlador;
    }

    public InscripcionControlador getInscripcionControlador() {
        return inscripcionControlador;
    }

    public void abrirNuevaVentana() {
        // Crear una instancia de la nueva ventana
        PrimVista ventana = new PrimVista();
        // Mostrar la ventana
        ventana.show();
    }

    public boolean menu() {
        int opcion = datosUtil.mostrarMenu();

        switch (opcion) {
            case 1:
                menuSocio();
                break;
            case 2:
                menuExcursion();
                break;
            case 3:
                menuInscripcion();
                break;
            case 4:
                if (pruebaConexion()) {
                    System.out.println("Conexión establecida.");
                } else {
                    System.out.println("Error al conectar.");
                }
                break;
            case 0:
                datosUtil.mostrarError("Saliendo del programa...");
                return false;
            default:
                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
                break;
        }
        return true;
    }

    public boolean menuSocio(){
        int opcion = datosUtil.menuSocios();
        switch (opcion) {
            case 1:
                socioControlador.nouSoci();
                break;
            case 2:
                socioControlador.mostrarSocios(1, 0);
                break;
            case 3:
                socioControlador.eliminarSocio();
                break;
            case 4:
                socioControlador.pedirSocio();
                break;
            case 5:
                socioControlador.modificarSeguro();
                break;
            case 6:
                socioControlador.modificarFederacion();
                break;
            case 7:
                inscripcionControlador.calcularCuota();
                break;
            case 0:
                return false;
            default:
                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
                break;
        }
        return true;
    }

    public boolean menuExcursion(){
        int opcion = datosUtil.menuExcursiones();
        switch (opcion) {
            case 1:
                excursionControlador.novaExcursio();
                break;
            case 2:
                excursionControlador.mostrarExcursiones();
                break;
            case 3:
                excursionControlador.eliminarExcursion();
                break;
            case 4:
                excursionControlador.pedirExcursion();
                break;
            case 5:
                excursionControlador.cancelarExcursion();
                break;
            case 0:
                return false;
            default:
                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
                break;
        }
        return true;
    }

    public boolean menuInscripcion(){
        int opcion = datosUtil.menuInscripciones();
        switch (opcion) {
            case 1:
                inscripcionControlador.novaInscripcio(1);
                break;
            case 2:
                inscripcionControlador.mostrarInscripcionesXExc();
                break;
            case 3:
                inscripcionControlador.eliminarInscripcion();
                break;
            case 4:
                inscripcionControlador.buscarInscripcion();
                break;
            case 0:
                return false;
            default:
                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
                break;
        }
        return true;
    }



    public void cerrarTeclado() {
        datosUtil.cerrarTeclado();
    }


    public boolean pruebaConexion() {
        Connection conexion = null;
        conexion = BBDDUtil.getConexion();
        System.out.println("Conexión abierta exitosamente.");
        BBDDUtil.closeConnection();
        System.out.println("Conexión cerrada exitosamente.");
        return true;
    }
}

