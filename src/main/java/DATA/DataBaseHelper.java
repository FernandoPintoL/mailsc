package DATA;

import CONNECTION.SQLConnection;
import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseHelper {

    SQLConnection connection;
    PreparedStatement ps;
    ResultSet set;

    void init_conexion() {
        connection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
    }

    boolean executeUpdate(String query) throws SQLException {
        boolean isSuccess = false;
        try{
            init_conexion();
            ps = connection.connect().prepareStatement(query);
            prepareStatement();
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
        }catch (SQLException e){
            System.out.println("Error al ejecutar la accion: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error: " + e.getErrorCode());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return isSuccess;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }

    void prepareStatement(){}
}
