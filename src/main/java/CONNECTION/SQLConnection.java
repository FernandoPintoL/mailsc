package CONNECTION;

import UTILS.ConstPSQL;
import java.sql.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Conexión con PostgreSQL con implementación de pool de conexiones básico
 *
 * @author angela
 */
public class SQLConnection implements AutoCloseable {

    private static final String DRIVER = ConstPSQL.PSQL;
    private static final int MAX_POOL_SIZE = 10;
    private static final ConcurrentLinkedQueue<Connection> connectionPool = new ConcurrentLinkedQueue<>();

    private Connection connection;
    private final String user;
    private final String pass;
    private final String host;
    private final int port;
    private final String name;
    private final String url;
    private static boolean poolInitialized = false;

    /**
     * Constructor para la conexión a la base de datos
     * @param user Usuario de la base de datos
     * @param pass Contraseña del usuario
     * @param host Dirección del servidor
     * @param port Puerto de la base de datos
     * @param name Nombre de la base de datos
     */
    public SQLConnection(String user, String pass, String host, int port, String name) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.port = port;
        this.name = name;
        this.url = DRIVER + host + ":" + port + "/" + name;

        if (!poolInitialized) {
            initializePool();
        }
    }

    /**
     * Inicializa el pool de conexiones con un número predefinido de conexiones
     */
    private synchronized void initializePool() {
        try {
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.add(conn);
                }
            }
            poolInitialized = true;
        } catch (Exception e) {
            System.err.println("Error al inicializar el pool de conexiones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crea una nueva conexión a la base de datos
     * @return Conexión a la base de datos
     */
    private Connection createNewConnection() {
        try {
            // Crear propiedades para la conexión sin SSL
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", user);
            props.setProperty("password", pass);
            // Desactivar SSL ya que el servidor no lo soporta
            props.setProperty("ssl", "false");

            return DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.err.println("Error al crear una nueva conexión a la base de datos.");
            System.err.println("Mensaje de error: " + e.getMessage());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Código de error: " + e.getErrorCode());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una conexión del pool o crea una nueva si el pool está vacío
     * @return Conexión a la base de datos
     */
    public Connection connect() {
        try {
            connection = connectionPool.poll();

            if (connection == null || connection.isClosed()) {
                connection = createNewConnection();
            }

            return connection;
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión del pool.");
            System.err.println("Mensaje de error: " + e.getMessage());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Código de error: " + e.getErrorCode());
            e.printStackTrace();
            return createNewConnection();
        }
    }

    /**
     * Cierra la conexión actual y la devuelve al pool
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Devolver la conexión al pool en lugar de cerrarla
                connectionPool.add(connection);
                connection = null;
            }
        } catch (SQLException ex) {
            System.err.println("Error al devolver la conexión al pool: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Implementación de AutoCloseable para usar en try-with-resources
     */
    @Override
    public void close() {
        closeConnection();
    }

    /**
     * Cierra todas las conexiones en el pool (usar al finalizar la aplicación)
     */
    public static void closeAllConnections() {
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión del pool: " + e.getMessage());
            }
        }
        poolInitialized = false;
    }
}
