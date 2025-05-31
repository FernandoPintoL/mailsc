package CONNECTION;

import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

/**
 * Clase para probar la conexión a la base de datos
 */
public class TestDBConnection {
    
    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");
        
        // Crear una instancia de SQLConnection con los parámetros de conexión
        SQLConnection sqlConnection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
        
        // Intentar establecer una conexión
        java.sql.Connection connection = sqlConnection.connect();
        
        if (connection != null) {
            System.out.println("¡Conexión exitosa a la base de datos!");
            
            // Mostrar información de la conexión
            try {
                System.out.println("URL de conexión: " + connection.getMetaData().getURL());
                System.out.println("Usuario: " + connection.getMetaData().getUserName());
                System.out.println("Producto de base de datos: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Versión: " + connection.getMetaData().getDatabaseProductVersion());
                
                // Cerrar la conexión
                sqlConnection.closeConnection();
                System.out.println("Conexión cerrada correctamente.");
            } catch (java.sql.SQLException e) {
                System.err.println("Error al obtener metadatos de la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("No se pudo establecer la conexión a la base de datos.");
        }
        
        // Cerrar todas las conexiones del pool
        SQLConnection.closeAllConnections();
    }
}