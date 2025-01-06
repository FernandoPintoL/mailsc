/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import UTILS.ConstGlobal;
import UTILS.ConstPSQL;
import CONNECTION.SQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fpl
 */
public class DContrato {

    int id;
    int cliente_id;
    int servicio_id;
    int equipo_trabajo_id;
    int empleado_id;
    String descripcion;
    double precio_total;
    String estado;
    LocalDateTime created_at;
    LocalDateTime fecha_inicio;
    LocalDateTime fecha_fin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(int servicio_id) {
        this.servicio_id = servicio_id;
    }

    public int getEquipo_trabajo_id() {
        return equipo_trabajo_id;
    }

    public void setEquipo_trabajo_id(int equipo_trabajo_id) {
        this.equipo_trabajo_id = equipo_trabajo_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(int empleado_id) {
        this.empleado_id = empleado_id;
    }

    public DContrato() {
    }

    public DContrato(int cliente_id, int servicio_id, int equipo_trabajo_id, int empleado_id, String descripcion, double precio_total, String estado, LocalDateTime fecha_inicio, LocalDateTime fecha_fin) {
        this.cliente_id = cliente_id;
        this.servicio_id = servicio_id;
        this.equipo_trabajo_id = equipo_trabajo_id;
        this.empleado_id = empleado_id;
        this.descripcion = descripcion;
        this.precio_total = precio_total;
        this.estado = estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    private final String TABLE = "contratos";
    private final String QUERY_ID = "id";
    //private final String Q_CI = "ci";
    private final String QUERY_INSERT = String.format("INSERT INTO %s (descripcion, precio_total, estado, fecha_inicio, fecha_fin, created_at, cliente_id, servicio_id, equipo_trabajo_id, empleado_id) VALUES (?,?,?,?,?,?,?,?,?,?)", TABLE);
    private final String QUERY_UPDATE = String.format("UPDATE %s SET descripcion=?, precio_total=?, estado=?, fecha_inicio=?, fecha_fin=?, updated_at=?, equipo_trabajo_id=?, empleado_id=? WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_ELIMINAR = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_VER = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    //private final String QUERY_CI = String.format("SELECT * FROM %s WHERE %s=?", TABLE, Q_CI);
    private final String QUERY_LIST = "SELECT * FROM " + TABLE;
    private final String MESSAGE_TRYCATCH = " ERROR MODELO: " + TABLE.toUpperCase() + " ";
    private SQLConnection connection;
    private PreparedStatement ps;
    private ResultSet set;

    private String[] arrayData(ResultSet set) throws SQLException {
        return new String[]{
                String.valueOf(set.getInt("id")),
                String.valueOf(set.getString("descripcion")),
                String.valueOf(set.getString("precio_total")),
                String.valueOf(set.getString("estado")),
                String.valueOf(set.getString("fecha_inicio")),
                String.valueOf(set.getString("fecha_fin")),
                String.valueOf(set.getTimestamp("created_at")),
                String.valueOf(set.getTimestamp("cliente_id")),
                String.valueOf(set.getTimestamp("servicio_id")),
                String.valueOf(set.getTimestamp("equipo_trabajo_id")),
                String.valueOf(set.getTimestamp("empleado_id"))
        };
    }

    void preparerState() throws SQLException {
        try {
            // Intentar establecer los valores
            ps.setString(1, getDescripcion());
            ps.setDouble(2, getPrecio_total());
            ps.setString(3, getEstado());
            ps.setTimestamp(4, Timestamp.valueOf(getFecha_inicio()));
            ps.setTimestamp(5, Timestamp.valueOf(getFecha_fin()));
            ps.setTimestamp(6, Timestamp.valueOf(getCreated_at()));
            ps.setInt(7, getCliente_id());
            ps.setInt(8, getServicio_id());
            ps.setInt(9, getEquipo_trabajo_id());
            ps.setInt(10, getEmpleado_id());
        } catch (SQLException e) {
            // Manejar la excepción SQL
            System.out.println(MESSAGE_TRYCATCH + TABLE);
            e.printStackTrace(); // Imprimir la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Estado SQL: " + e.getSQLState());
            System.out.println("Código de error SQL: " + e.getErrorCode());
        }
    }

    private void init_conexion() {
        connection = new SQLConnection(ConstPSQL.user, ConstPSQL.pass, ConstGlobal.SERVIDOR, ConstGlobal.PORT_DB, ConstPSQL.dbName);
    }

    public Object[] guardar() throws SQLException, ParseException {
        boolean isSuccess = false;
        String mensaje = "";
        //String[] exists = null;
        try {
            /*exists = existe(getCi());
            if (exists != null) {
                mensaje = "La persona ya está registrada. ID: ".toUpperCase()+exists[0];
                return new Object[]{false, mensaje, null};
            }*/
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_INSERT);
            preparerState();
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
            if (isSuccess) {
                mensaje = TABLE + " Registro insertado exitosamente.".toUpperCase();
            } else {
                mensaje = MESSAGE_TRYCATCH + " Error al intentar guardar los datos.".toUpperCase();
                //throw new SQLException("No se pudo insertar el registro en la base de datos.".toUpperCase());
            }
        } catch (SQLException e) {
            // Imprimir detalles del error SQLException
            mensaje = MESSAGE_TRYCATCH + " (GUARDAR) Error en la base de datos: " + e.getMessage();
            System.out.println(MESSAGE_TRYCATCH + " GUARDAR");
            e.printStackTrace(); // Imprime toda la traza del error para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje detallado del error
            System.out.println("Estado SQL: " + e.getSQLState()); // Código de estado SQL
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor de la BD
        } finally {
            // Cerrar el PreparedStatement
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement: " + TABLE + e.getMessage());
                mensaje = MESSAGE_TRYCATCH + " (GUARDAR) Error al cerrar PreparedStatement: " + TABLE + e.getMessage();
            }
        }
        return new Object[]{isSuccess, mensaje};
    }

    public Object[] modificar() throws SQLException, ParseException {
        boolean isSuccess = false;
        String mensaje = "";
        try {
            String[] exists = ver();
            if (exists == null) {
                System.out.println(MESSAGE_TRYCATCH + " NO EXISTE ");
                return new Object[]{false, " IDS INGRESADOS NO SE ENCUENTRAN REGISTRADADAS EN LA TABLA: " + TABLE.toUpperCase()};
            }
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_UPDATE);
            preparerState();
            ps.setInt(7, getId());
            int execute = ps.executeUpdate();
            isSuccess = execute > 0;
            if (isSuccess) {
                mensaje = TABLE + " - (MODIFICAR) actualizacion exitosamente.".toUpperCase();
            } else {
                mensaje = MESSAGE_TRYCATCH + " - (MODIFICAR) Error al actualizar los datos.".toUpperCase();
                //throw new SQLException("No se pudo Error al actualizar los datos.".toUpperCase());
            }
        } catch (SQLException e) {
            System.out.println(MESSAGE_TRYCATCH + " MODIFICAR");
            mensaje = MESSAGE_TRYCATCH + " MODIFICAR";
            e.printStackTrace(); // Imprime toda la traza del error para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje detallado del error
            System.out.println("Estado SQL: " + e.getSQLState()); // Código de estado SQL
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor de la BD
        } finally {
            // Cerrar el PreparedStatement
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(" (MODIFICAR) Error al cerrar PreparedStatement: ".toUpperCase() + TABLE + e.getMessage());
                mensaje = MESSAGE_TRYCATCH + " (MODIFICAR) Error al cerrar PreparedStatement: ".toUpperCase() + TABLE + e.getMessage();
            }
        }
        return new Object[]{isSuccess, mensaje};
    }

    public boolean eliminar() {
        boolean eliminado = false;
        try {
            String[] exists = ver();
            if (exists == null) {
                System.out.println("EL ADMINSITRATIVO NO EXISTE");
                return false;
            }
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_ELIMINAR);
            ps.setInt(1, getId());
            if (ps.executeUpdate() == 0) {
                eliminado = false;
                System.err.println(MESSAGE_TRYCATCH + " No se pudo eliminar la persona");
                //throw new SQLException("Error al intentar eliminar el registro.");
            }
            eliminado = true; // Si el executeUpdate tuvo éxito, se actualiza el valor
        } catch (SQLException e) {
            eliminado = false;
            // Muestra detalles de la excepción SQL
            System.err.println("Error de SQL: " + e.getMessage());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Código de Error: " + e.getErrorCode());
            e.printStackTrace(); // Imprime la pila de llamadas para más detalles
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                eliminado = false;
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return eliminado;
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_LIST);
            set = ps.executeQuery();
            while (set.next()) {
                datas.add(arrayData(set));
            }
            System.out.println(datas);
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println(MESSAGE_TRYCATCH + " LISTAR");
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString() + TABLE);
                e.printStackTrace(); // Imprimir cualquier error al cerrar recursos
            }
        }
        return datas;
    }

    public String[] ver() throws SQLException {
        String[] data = null;
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_VER);
            ps.setInt(1, getId());
            set = ps.executeQuery();
            if (set.next()) {
                data = arrayData(set);
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
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + TABLE + e.getMessage());
            }
        }
        return data;
    }
    
    /*public String[] existe(String ci) throws SQLException {
        String[] data = null;
        setCi(ci);
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_CI);
            ps.setString(1, getCi());
            set = ps.executeQuery();
            if (set.next()) {
                data = arrayData(set);
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
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + TABLE + e.getMessage());
            }
        }
        return data;
    }*/

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
