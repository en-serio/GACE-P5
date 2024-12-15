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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Escena.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("GACE - Gestión de Actividades Culturales y Excursiones");
            primaryStage.show();

            this.contenedorCentral = (AnchorPane) scene.lookup("#contenedorCentral");
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/vista/MenuVista.fxml"));
            Parent menuRoot = menuLoader.load();

            contenedorCentral.getChildren().clear();
            contenedorCentral.getChildren().add(menuRoot);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Cargar el menú de socios
    public void cargarMenuSocio() {
        try {
            // Cargar la vista desde el archivo MenuSocio.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MenuSocio.fxml"));
            Parent menuSocioRoot = loader.load();

            // Limpiar el contenedor central y añadir la nueva vista
            contenedorCentral.getChildren().clear();
            contenedorCentral.getChildren().add(menuSocioRoot);
        } catch (Exception e) {
            System.err.println("Error al cargar MenuSocio.fxml: " + e.getMessage());
        }
    }

    //Vinculado al botón "Socios" en MenuVista.fxml
    @FXML
    public void menuSocio() {
        cargarMenuSocio();
    }

    public void salir() {
        System.exit(0);
    }

    public boolean menuExcursion() {
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

    public boolean menuInscripcion() {
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
