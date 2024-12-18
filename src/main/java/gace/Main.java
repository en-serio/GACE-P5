package gace;

import gace.controlador.ExcursionControlador;
import gace.controlador.MenuControlador;
import gace.controlador.SocioControlador;
import gace.modelo.utils.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Escena.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root, 900,400);
            scene.getStylesheets().addAll(Objects.requireNonNull(this.getClass().getResource("/styles/style.css")).toExternalForm());
            SocioControlador sc = new SocioControlador();
            primaryStage.setScene(scene);
            primaryStage.setTitle("GACE - Gestión de Actividades Culturales y Excursiones");
            primaryStage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}