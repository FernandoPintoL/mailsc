/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import UTILS.ConstGlobal;
import UTILS.ConstPSQL;
import CONNECTION.SQLConnection;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class DIncidencia {
    
    int id;
    String nombre;
    String descripcion;
    LocalDateTime created_at;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public DIncidencia() {}
    public DIncidencia(String descripcion, String nombre) {
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    public DIncidencia(int id,String descripcion, String nombre) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    private final String TABLE = "incidencias";
    private final String QUERY_ID = "id";
    private final String QUERY_INSERT = String.format(
            "INSERT INTO %s ( nombre, descripcion, , created_at) VALUES (?,?,?,?,?)", TABLE);
    private final String QUERY_UPDATE = String.format(
            "UPDATE %s SET nombre=?, descripcion=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_ELIMINAR = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_VER = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    //private final String QUERY_PRODUCTO_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, Q_PRODUCTO_ID);
    private final String QUERY_LIST = "SELECT * FROM " + TABLE;
    private final String MESSAGE_TRYCATCH = " ERROR MODELO: " + TABLE.toUpperCase() + " ";
    private SQLConnection connection;
    private String[] arrayData(ResultSet set) throws SQLException {
        return new String[]{
            String.valueOf(set.getInt("id")),
            String.valueOf(set.getInt("cliente_id")),
            String.valueOf(set.getInt("contrato_id")),
            String.valueOf(set.getString("descripcion")),
            String.valueOf(set.getString("estado")),
            String.valueOf(set.getTimestamp("created_at")),
            String.valueOf(set.getTimestamp("fecha_resolucion"))
        };
    }
    private void prepareInsert(PreparedStatement ps) throws SQLException {
        try {
            // Intentar establecer los valores
            ps.setString(1, nombre);
            ps.setString(2, getDescripcion());
            ps.setTimestamp(3, Timestamp.valueOf(getCreated_at() != null ? getCreated_at() : LocalDateTime.now()));
        } catch (SQLException e) {
            // Manejar la excepción SQL
            System.out.println(MESSAGE_TRYCATCH + TABLE);
            e.printStackTrace(); // Imprimir la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error SQL: " + e.getErrorCode());
        }
    }
    private void prepareUpdate(PreparedStatement ps) throws SQLException {
        try{
            ps.setString(1, nombre);
            ps.setString(2, getDescripcion());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, getId());
        }catch (SQLException e) {
            // Manejar la excepción SQL
            System.out.println(MESSAGE_TRYCATCH + TABLE);
            e.printStackTrace(); // Imprimir la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error SQL: " + e.getErrorCode());
        }
    }
    private void init_conexion() {
        connection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
    }
    public Object[] guardar() throws SQLException, ParseException {
        boolean isSuccess = false;
        String mensaje;
        init_conexion();
        try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_INSERT)) {
            prepareInsert(ps);
            isSuccess = ps.executeUpdate() > 0;
            mensaje = isSuccess ? TABLE + " REGISTRO INSERTADO EXITOSAMENTE." : MESSAGE_TRYCATCH + " ERROR AL GUARDAR.";
        } catch (SQLException e) {
            // Imprimir detalles del error SQLException
            mensaje = MESSAGE_TRYCATCH+" (GUARDAR) Error en la base de datos: " + e.getMessage();
            System.out.println(MESSAGE_TRYCATCH + " GUARDAR");
            e.printStackTrace(); // Imprime toda la traza del error para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje detallado del error
            System.out.println("Estado SQL: " + e.getSQLState()); // Código de estado SQL
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor de la BD
        }
        return new Object[]{isSuccess, mensaje};
    }
    public Object[] modificar() throws SQLException, ParseException {
        boolean isSuccess = false;
        String mensaje;
        init_conexion();
        try {
            String[] exists = ver();
            if(exists == null){
                System.out.println(MESSAGE_TRYCATCH+" NO EXISTE ");
                return new Object[]{false, " IDS INGRESADOS NO SE ENCUENTRAN REGISTRADADAS EN LA TABLA: "+TABLE.toUpperCase()};
            }
            try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_UPDATE)) {
                prepareUpdate(ps);
                isSuccess = ps.executeUpdate() > 0;
                mensaje = isSuccess ? TABLE + " ACTUALIZADO EXITOSAMENTE." : MESSAGE_TRYCATCH + " ERROR AL ACTUALIZAR.";
            }
        } catch (SQLException e) {
            System.out.println(MESSAGE_TRYCATCH + " MODIFICAR");
            mensaje = MESSAGE_TRYCATCH + " MODIFICAR";
            e.printStackTrace(); // Imprime toda la traza del error para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje detallado del error
            System.out.println("Estado SQL: " + e.getSQLState()); // Código de estado SQL
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor de la BD
        }
        return new Object[]{isSuccess, mensaje};
    }
    public boolean eliminar() {
        boolean eliminado = false;
        init_conexion();
        try {
            String[] exists = ver();
            if(exists == null){
                System.out.println("EL ADMINSITRATIVO NO EXISTE");
                return false;
            }
            try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_ELIMINAR)) {
                ps.setInt(1, getId());
                eliminado = ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            // Muestra detalles de la excepción SQL
            System.err.println("Error de SQL: " + e.getMessage());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Código de Error: " + e.getErrorCode());
            e.printStackTrace(); // Imprime la pila de llamadas para más detalles
        }
        return eliminado;
    }
    public List<String[]> listar() throws SQLException {
        List<String[]> datas = new ArrayList<>();
        init_conexion();
        try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_LIST);
             ResultSet set = ps.executeQuery()) {
            while (set.next()) {
                datas.add(arrayData(set));
            }
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println(MESSAGE_TRYCATCH + " LISTAR");
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
        }
        return datas;
    }
    public String[] ver() throws SQLException {
        String[] data = null;
        init_conexion();
        try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_VER)) {
            ps.setInt(1, getId());
            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    data = arrayData(set);
                }
            }
        } catch (SQLException e) {
            // Imprimir detalles de la excepción SQLException
            System.out.println(MESSAGE_TRYCATCH + " VER");
            e.printStackTrace();  // Muestra la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());  // Mensaje detallado del error
            System.out.println("Código de estado SQL: " + e.getSQLState());  // Código SQL estándar del error
            System.out.println("Código de error del proveedor: " + e.getErrorCode());  // Código de error específico del proveedor de la base de datos

        }
        return data;
    }
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
