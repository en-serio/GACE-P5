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
        Label label = new Label("¡Bienvenido a la aplicación!");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 400, 200); // Tamaño 400x200
        primaryStage.setTitle("Ventana de Bienvenida");
        primaryStage.setScene(scene);
        primaryStage.show();



        /*MenuControlador controlador = new MenuControlador();

        boolean running = true;
        while (running) {
            if(!controlador.menu()){
                System.out.println("Saliendo...");
                running = false;
            }
        }
        controlador.cerrarTeclado();
        HibernateUtil.shutdown();*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}