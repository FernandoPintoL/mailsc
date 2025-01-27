package DATA;

import CONNECTION.SQLConnection;
import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

public class DataBaseHelper {
    SQLConnection connection;
    PreparedStatement ps;
    ResultSet resultSet;
    void init_conexion() {
        connection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
    }
    public String[] arrayData(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = set.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] data = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            if(set.getString(i) == null){
                data[i - 1] = "";
            }else{
                data[i - 1] = set.getString(i);
            }
        }
        System.out.println("ARRAY DATA RESULT SET: ");
        System.out.println(Arrays.toString(data));
        return data;
    }
    boolean executeCreateUpdate(String query) throws SQLException {
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
        } finally {
            closeConnections();
        }
        return isSuccess;
    }
    boolean executeDelete(String query) throws SQLException {
        boolean isSuccess = false;
        try{
            init_conexion();
            ps = connection.connect().prepareStatement(query);
            prepareStatementForId();
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
        }catch (SQLException e){
            System.out.println("Error al ejecutar la accion: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error: " + e.getErrorCode());
            return  false;
        } finally {
            closeConnections();
        }
        return isSuccess;
    }
    String[] executeQuery(String query) throws SQLException {
        ResultSet resultQuery = null;
        String[] data = null;
        try{
            System.out.println("Ejecutando query: " + query);
            init_conexion();
            ps = connection.connect().prepareStatement(query);
            prepareStatementForId();
            resultSet = ps.executeQuery();
            System.out.println("getRow: "+resultQuery.getRow());
            if (resultSet.next()) {
                data = arrayData(resultSet);
                System.out.println(Arrays.toString(data));
            }
        }catch (SQLException e){
            System.out.println("Error al ejecutar la accion: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error: " + e.getErrorCode());
        } finally {
            closeConnections();
        }
        return data;
    }
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
    public void closeConnections() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar PreparedStatement: " + e.getMessage());
        }
    }
    void prepareStatement(){}
    void prepareStatementForId(){}
}
