package gace.modelo.utils;


import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BBDDUtil {

    private static Connection connection = null;
    private static final Dotenv dotenv = Dotenv.load();

    // Carga la URL, usuario y contraseña desde el archivo .env
    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    /**
     * Método para obtener una conexión a la base de datos.
     * Si la conexión ya existe y está abierta, la reutiliza.
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si ocurre un error al conectarse
     */
    public static Connection getConexion() {
        try{
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Conexión a la base de datos establecida.");
            }
        }catch (SQLException e){
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Método para cerrar la conexión a la base de datos.
     * Asegura que la conexión se cierra correctamente cuando no se necesita más.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}
