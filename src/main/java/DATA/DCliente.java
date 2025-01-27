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
public class DCliente extends DataBaseHelper{
    
    int id;
    String ci;
    String nombre;
    String telefono;
    String direccion;
    String tipo_cliente;
    LocalDateTime created_at;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCi() {
        return ci;
    }
    public void setCi(String ci) {
        this.ci = ci;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getTipo_cliente() {
        return tipo_cliente;
    }
    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }
    public LocalDateTime getCreated_at() {return created_at;}
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public DCliente(String ci, String nombre, String telefono, String direccion, String tipo_cliente) {
        this.ci = ci;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipo_cliente = tipo_cliente;
        this.created_at = LocalDateTime.now();
    }
    public DCliente() {}

    private final String TABLE = "clientes";
    private final String QUERY_ID = "id";
    private final String Q_CI = "ci";
    private final String QUERY_INSERT = String.format(
            "INSERT INTO %s (ci, nombre, telefono, direccion, tipo_cliente, created_at) VALUES (?,?,?,?,?,?)", TABLE);
    private final String QUERY_UPDATE = String.format(
            "UPDATE %s SET ci=?, nombre=?, telefono=?, direccion=?, tipo_cliente=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_ELIMINAR = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_VER = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_CI = String.format("SELECT * FROM %s WHERE %s=?", TABLE, Q_CI);
    private final String QUERY_LIST = "SELECT * FROM " + TABLE;
    private final String MESSAGE_TRYCATCH = " ERROR MODELO: " + TABLE.toUpperCase() + " ";
    @Override
    void prepareStatement(){
        try {
            // Intentar establecer los valores
            ps.setString(1, getCi());
            ps.setString(2, getNombre());
            ps.setString(3, getTelefono());
            ps.setString(4, getDireccion());
            ps.setString(5, getTipo_cliente());
            ps.setTimestamp(6, Timestamp.valueOf(getCreated_at()));
            if(getId() != 0){
                ps.setInt(7, getId());
            }
        } catch (SQLException e) {
            // Manejar la excepción SQL
            System.out.println(MESSAGE_TRYCATCH + TABLE);
            e.printStackTrace(); // Imprimir la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error SQL: " + e.getErrorCode());
        }
    }
    @Override
    void prepareStatementForId(){
        try {
            // Intentar establecer los valores
            ps.setInt(1, getId());
        } catch (SQLException e) {
            // Manejar la excepción SQL
            System.out.println(MESSAGE_TRYCATCH + TABLE);
            e.printStackTrace(); // Imprimir la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error SQL: " + e.getErrorCode());
        }
    }
    public Object[] guardar() throws SQLException {
        String[] exists = existe(getCi());
        String mensaje = "";
        boolean isSuccess = false;
        if (exists != null) {
            mensaje = "Estos datos ya se encuentran registrados ID: ".toUpperCase() + exists[0];
            return new Object[]{isSuccess, mensaje, null};
        }else{
            isSuccess = executeCreateUpdate(QUERY_INSERT);
            mensaje = isSuccess ? "Registro insertado exitosamente.".toUpperCase() : "Error al intentar guardar los datos.".toUpperCase();
            return new Object[]{isSuccess, mensaje};
        }
    }
    public Object[] modificar() throws SQLException, ParseException {
        boolean isSuccess = false;
        String mensaje = "";
        String[] exists = ver();
        if(exists == null){
            return new Object[]{false, " IDS INGRESADOS NO SE ENCUENTRAN REGISTRADADAS EN LA TABLA: "+TABLE.toUpperCase()};
        }else{
            isSuccess = executeCreateUpdate(QUERY_UPDATE);
            if (isSuccess) {
                mensaje = TABLE+" - (MODIFICAR) actualizacion exitosamente.".toUpperCase();
            } else {
                mensaje = MESSAGE_TRYCATCH+" - (MODIFICAR) Error al actualizar los datos.".toUpperCase();
            }
            return new Object[]{isSuccess, mensaje};
        }
    }
    public boolean eliminar() throws SQLException {
        boolean eliminado = false;
        String[] exists = ver();
        if(exists == null){
            System.out.println("EL ADMINSITRATIVO NO EXISTE");
            return false;
        }
        eliminado = executeDelete(QUERY_ELIMINAR);
        return eliminado;
    }
    public List<String[]> listar() throws SQLException {
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_LIST);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                datas.add(arrayData(resultSet));
            }
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println(MESSAGE_TRYCATCH + " LISTAR");
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            closeConnections();
        }
        return datas;
    }
    public String[] ver() throws SQLException {
        String[] data = null;
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_VER);
            ps.setInt(1, getId());
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                data = arrayData(resultSet);
                System.out.println(Arrays.toString(data));
            }
        } catch (SQLException e) {
            // Imprimir detalles de la excepción SQLException
            System.out.println(MESSAGE_TRYCATCH + " VER");
            e.printStackTrace();  // Muestra la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());  // Mensaje detallado del error
            System.out.println("Código de estado SQL: " + e.getSQLState());  // Código SQL estándar del error
            System.out.println("Código de error del proveedor: " + e.getErrorCode());  // Código de error específico del proveedor de la base de datos

        } finally {
            // Cerrar recursos para evitar fugas de memoria
            closeConnections();
        }
        return data;
    }
    public String[] existe(String ci) throws SQLException {
        String[] data = null;
        setCi(ci);
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_CI);
            ps.setString(1, getCi());
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                data = arrayData(resultSet);
                System.out.println(Arrays.toString(data));
            }
        } catch (SQLException e) {
            // Imprimir detalles de la excepción SQLException
            System.out.println(MESSAGE_TRYCATCH + " EXISTE ");
            e.printStackTrace();  // Muestra la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());  // Mensaje detallado del error
            System.out.println("Código de estado SQL: " + e.getSQLState());  // Código SQL estándar del error
            System.out.println("Código de error del proveedor: " + e.getErrorCode());  // Código de error específico del proveedor de la base de datos
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            closeConnections();
        }
        return data;
    }
}
