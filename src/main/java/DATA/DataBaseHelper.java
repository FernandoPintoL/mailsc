package DATA;

import CONNECTION.SQLConnection;
import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Clase base para operaciones de base de datos
 * Proporciona métodos comunes para ejecutar consultas y manejar resultados
 */
public class DataBaseHelper implements AutoCloseable {
    protected SQLConnection sqlConnection;
    protected Connection connection;
    protected PreparedStatement ps;
    protected ResultSet resultSet;

    /**
     * Constructor por defecto
     */
    public DataBaseHelper() {
        init_conexion();
    }

    /**
     * Inicializa la conexión a la base de datos
     */
    protected void init_conexion() {
        sqlConnection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
        connection = sqlConnection.connect();
    }

    /**
     * Convierte un ResultSet en un array de Strings
     * @param set ResultSet a convertir
     * @return Array de Strings con los datos del ResultSet
     * @throws SQLException Si ocurre un error al acceder a los datos
     */
    public String[] arrayData(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        System.out.println("Datos de ResultSet: " + metaData.toString());
        int columnCount = metaData.getColumnCount();
        System.out.println("Número de columnas: " + columnCount);
        String[] data = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            data[i - 1] = set.getString(i) == null ? "" : set.getString(i);
            System.out.println("Name " + i + ": " + metaData.getColumnName(i));
            System.out.println("Columna " + i + ": " + data[i - 1]);
        }
        System.out.println("Datos obtenidos: " + Arrays.toString(data));
        return data;
    }

    /**
     * Ejecuta una consulta de inserción o actualización
     * @param query Consulta SQL a ejecutar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    protected boolean executeCreateUpdate(String query) {
        boolean isSuccess = false;
        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }
            ps = connection.prepareStatement(query);
            prepareStatement();
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
        } catch (SQLException e) {
            logSQLException("Error al ejecutar la acción (create/update)", e);
        } finally {
            closeResources(false);
        }
        return isSuccess;
    }

    /**
     * Ejecuta una consulta de eliminación
     * @param query Consulta SQL a ejecutar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    protected boolean executeDelete(String query) {
        boolean isSuccess = false;
        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }
            ps = connection.prepareStatement(query);
            prepareStatementForId();
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
        } catch (SQLException e) {
            logSQLException("Error al ejecutar la acción (delete)", e);
            return false;
        } finally {
            closeResources(false);
        }
        return isSuccess;
    }

    /**
     * Ejecuta una consulta de selección
     * @param query Consulta SQL a ejecutar
     * @return Array de Strings con los datos del primer registro encontrado, o null si no hay resultados
     */
    protected String[] executeQuery(String query) {
        String[] data = null;
        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }
            ps = connection.prepareStatement(query);
            prepareStatementForId();
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                data = arrayData(resultSet);
            }
        } catch (SQLException e) {
            logSQLException("Error al ejecutar la consulta", e);
        } finally {
            closeResources(false);
        }
        return data;
    }

    /**
     * Registra una excepción SQL en la consola
     * @param message Mensaje descriptivo del error
     * @param e Excepción SQL
     */
    protected void logSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        System.err.println("Estado SQL: " + e.getSQLState());
        System.err.println("Código de error: " + e.getErrorCode());
        e.printStackTrace();
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void desconectar() {
        if (sqlConnection != null) {
            sqlConnection.closeConnection();
            sqlConnection = null;
            connection = null;
        }
    }

    /**
     * Cierra los recursos de la base de datos (ResultSet y PreparedStatement)
     * @param closeConnection Si es true, también cierra la conexión
     */
    public void closeResources(boolean closeConnection) {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (closeConnection && sqlConnection != null) {
                desconectar();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cierra todos los recursos cuando se usa try-with-resources
     */
    @Override
    public void close() {
        closeResources(true);
    }

    /**
     * Método para ser sobrescrito por las clases hijas
     * Prepara los parámetros del PreparedStatement para consultas de inserción/actualización
     * @throws SQLException Si ocurre un error al preparar los parámetros
     */
    protected void prepareStatement() throws SQLException {}

    /**
     * Método para ser sobrescrito por las clases hijas
     * Prepara los parámetros del PreparedStatement para consultas por ID
     * @throws SQLException Si ocurre un error al preparar los parámetros
     */
    protected void prepareStatementForId() throws SQLException {}

    /**
     * Cierra todas las conexiones del pool (llamar al finalizar la aplicación)
     */
    public static void closeAllConnections() {
        SQLConnection.closeAllConnections();
    }
}
