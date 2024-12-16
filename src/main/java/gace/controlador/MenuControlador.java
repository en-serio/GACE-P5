package gace.controlador;

import gace.modelo.Excursion;
import gace.modelo.utils.BBDDUtil;
import gace.vista.DatosUtil;
import gace.vista.PrimVista;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class MenuControlador {
    //private DatosUtil datosUtil;
    //private ExcursionControlador excursionControlador;
    //private SocioControlador socioControlador;
    //private InscripcionControlador inscripcionControlador;

    @FXML
    private StackPane contenedorPane;

    @FXML
    private Text textPr;

    @FXML
    public void menuSocio() {
        cargarVista("/vista/MenuSocio.fxml");
    }
    @FXML
    public void menuInscripcion() {
        cargarVista("/vista/MenuInscripcion.fxml");
    }
    @FXML
    public void menuExcursion() {
        ExcursionControlador eCont = (ExcursionControlador) cargarVista("/vista/MenuExcursion.fxml");
        eCont.mostrarExcursiones();
    }

    @FXML
    private void inicio(){
        cargarVista("/vista/Inicial.fxml");
    }
    private Object cargarVista(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            StackPane cont = loader.load();
            contenedorPane.getChildren().clear();
            contenedorPane.getChildren().add(cont);

            return loader.getController();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @FXML
    public void initialize() {
        // Cargar la vista de inicio cuando la aplicación arranca
        cargarVista("/vista/Inicial.fxml");
    }

/*
//    public MenuControlador() {
//        this.datosUtil = new DatosUtil();
//        this.socioControlador = new SocioControlador();
//        this.excursionControlador = new ExcursionControlador();
//        this.inscripcionControlador = new InscripcionControlador(this.excursionControlador, this.socioControlador);
//    }
//
//    public MenuControlador(DatosUtil datosUtil) {
//        this.datosUtil = datosUtil;
//        this.socioControlador = new SocioControlador();
//        this.excursionControlador = new ExcursionControlador();
//        this.inscripcionControlador = new InscripcionControlador(this.excursionControlador, this.socioControlador);
//    }

//    public DatosUtil getDatosUtil() {
//        return datosUtil;
//    }
//
//    public ExcursionControlador getExcursionControlador() {
//        return excursionControlador;
//    }
//
//    public SocioControlador getSocioControlador() {
//        return socioControlador;
//    }
//
//    public InscripcionControlador getInscripcionControlador() {
//        return inscripcionControlador;
//    }

//    public void abrirNuevaVentana() {
//        // Crear una instancia de la nueva ventana
//        PrimVista ventana = new PrimVista();
//        // Mostrar la ventana
//        ventana.show();
//    }


    public void menu(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Escena.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("GACE - Gestión de Actividades Culturales y Excursiones");
            primaryStage.show();

            this.contenedorCentral = (AnchorPane) scene.lookup("#contenedorCentral");


            contenedorCentral.getChildren().clear();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Cargar el menú de socios
    public void cargarMenuSocio() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MenuSocio.fxml"));
            Parent root = loader.load();

            contenedorCentral.getChildren().clear();

            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(root);

            StackPane.setAlignment(root, Pos.CENTER);

            stackPane.setPadding(new Insets(20));

            contenedorCentral.getChildren().add(stackPane);

            AnchorPane.setTopAnchor(stackPane, 0.0);
            AnchorPane.setBottomAnchor(stackPane, 0.0);
            AnchorPane.setLeftAnchor(stackPane, 0.0);
            AnchorPane.setRightAnchor(stackPane, 0.0);

        } catch (Exception e) {
            System.err.println("Error al cargar MenuSocio.fxml: " + e.getMessage());
        }
    }



    @FXML
    public void menuSocio() {
        cargarMenuSocio();
    }
    public void initialize() {
        if (contenedorCentral != null) {
            System.out.println("contenedorCentral ha sido inicializado correctamente.");
            System.out.println(contenedorCentral);
        } else {
            System.err.println("contenedorCentral es null en initialize.");
        }
    }

    public void salir() {
        System.exit(0);
    }

    @FXML
    private void menuExcursion(){
        try{
            AnchorPane menuExc = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/vista/MenuExcursion.fxml")));
            contenedorCentral.getChildren().clear();
            contenedorCentral.getChildren().add(menuExc);
            AnchorPane.setTopAnchor(menuExc, 0.0);
            AnchorPane.setRightAnchor(menuExc, 0.0);
            AnchorPane.setBottomAnchor(menuExc, 0.0);
            AnchorPane.setLeftAnchor(menuExc, 0.0);
//            for (Excursion exc : excs) {
//                Label label = new Label("ID: " + exc.getId() + " Código: " + exc.getCodigo() + " " + exc.getDescripcion());
//
//                HBox socioBox = new HBox(5, label);
//                socioBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
//
//                listaExcursion.getChildren().add(socioBox);
//            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return;
    }
//        excursionControlador.menuExcursion(contenedorCentral, escena);
//        return true;
//    }
//    public boolean menuSocio(){
//        System.out.println("menuSocio");
//        return true;
//    }
/*
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

    }*/
/*
    public void menuInscripcion() {
        System.out.println("menuInscripcion");
    }
*/
//    public boolean menuInscripcion() {
//        int opcion = datosUtil.menuInscripciones();
//        switch (opcion) {
//            case 1:
//                inscripcionControlador.novaInscripcio(1);
//                break;
//            case 2:
//                inscripcionControlador.mostrarInscripcionesXExc();
//                break;
//            case 3:
//                inscripcionControlador.eliminarInscripcion();
//                break;
//            case 4:
//                inscripcionControlador.buscarInscripcion();
//                break;
//            case 0:
//                return false;
//            default:
//                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
//                break;
//        }
//        return true;
//    }
//        int opcion = datosUtil.menuInscripciones();
//        switch (opcion) {
//            case 1:
//                //inscripcionControlador.novaInscripcio(1);
//                break;
//            case 2:
//                inscripcionControlador.mostrarInscripcionesXExc();
//                break;
//            case 3:
//                inscripcionControlador.eliminarInscripcion();
//                break;
//            case 4:
//                inscripcionControlador.buscarInscripcion();
//                break;
//            case 0:
//                return false;
//            default:
//                datosUtil.mostrarError("Opción no válida. Inténtelo de nuevo.");
//                break;
//        }
//        return true;
//    }

//    public void cerrarTeclado() {
//        datosUtil.cerrarTeclado();
//    }
//
//    public boolean pruebaConexion() {
//        Connection conexion = null;
//        conexion = BBDDUtil.getConexion();
//        System.out.println("Conexión abierta exitosamente.");
//        BBDDUtil.closeConnection();
//        System.out.println("Conexión cerrada exitosamente.");
//        return true;
//    }
}
