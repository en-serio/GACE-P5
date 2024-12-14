package gace.controlador;

import gace.modelo.utils.BBDDUtil;
import gace.vista.DatosUtil;
import gace.vista.PrimVista;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.plaf.synth.Region;
import java.sql.Connection;

public class MenuControlador {
    private DatosUtil datosUtil;
    private ExcursionControlador excursionControlador;
    private SocioControlador socioControlador;
    private InscripcionControlador inscripcionControlador;

    @FXML
    private AnchorPane contenedorCentral;


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


    public void menu(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Escena.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("GACE - Gestión de Actividades Culturales y Excursiones");
            primaryStage.show();

            this.contenedorCentral = (AnchorPane) scene.lookup("#contenedorCentral");
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/MenuVista.fxml"));
            Parent menuRoot = menuLoader.load();

            contenedorCentral.getChildren().clear();
            contenedorCentral.getChildren().add(menuRoot);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

//    public boolean menu() {
//        int opcion = datosUtil.mostrarMenu();
//
//        switch (opcion) {
//            case 1:
//                menuSocio();
//                break;
//            case 2:
//                menuExcursion();
//                break;
//            case 3:
//                menuInscripcion();
//                break;
//            case 4:
//                if (pruebaConexion()) {
//                    System.out.println("Conexión establecida.");
//                } else {
//                    System.out.println("Error al conectar.");
//                }
//                break;
//            case 0:
//                datosUtil.mostrarError("Saliendo del programa...");
//                return false;
//            default:
//                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
//                break;
//        }
//        return true;
//    }


    public void salir(){
        System.exit(0);
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

