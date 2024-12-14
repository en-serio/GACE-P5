package gace;

import gace.controlador.MenuControlador;
import gace.modelo.utils.HibernateUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private MenuControlador controlador = new MenuControlador();
    @Override
    public void start(Stage primaryStage) {
        controlador.menu(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}