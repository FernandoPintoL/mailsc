package CONNECTION;

import UTILS.ConstPSQL;
import java.sql.*;

/**
 * conexion con postgresql
 *
 * @author angela
 */
public class SQLConnection {

    private static final String DRIVER = ConstPSQL.PSQL;

    private Connection connection;
    private String user;
    private String pass;
    private String host;
    private int port;
    private String name;
    private String url;

    //constructor
    public SQLConnection(String user, String pass, String host, int port, String name) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.port = port;
        this.name = name;
        this.url = DRIVER + host + ":" + port + "/" + name;
    }

    public Connection connect() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            return connection;
        } catch (SQLException e) {
            System.out.println("Error al intentar conectar a la base de datos.");
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error: " + e.getErrorCode());
            e.printStackTrace(); // Opcional, muestra la traza completa para depuración
            return null;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Class SqlConnection.java dice:"
                    + "Ocurrio un error al momento de cerrar la conexion closeConnection()");
        }
    }

}
