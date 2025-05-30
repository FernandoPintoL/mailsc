package DATA;

import CONNECTION.SQLConnection;
import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DContratoIncidencias {

    int id;
    int contrato_id;
    int incidencia_id;
    String estado;
    LocalDateTime fecha_solucion;
    LocalDateTime created_at;
    public DContratoIncidencias() {
        super();
    }
    public DContratoIncidencias(int contrato_id, int incidencia_id, String estado) {
        super();
        this.contrato_id = contrato_id;
        this.incidencia_id = incidencia_id;
        this.estado = estado;
    }
    private final String TABLE = "contrato_incidencias";
    private final String QUERY_ID = "id";
    //private final String Q_CI = "ci";
    private final String QUERY_INSERT = String.format(
            "INSERT INTO %s (contrato_id, incidencia_id, estado, created_at) VALUES (?,?,?,?)", TABLE);
    private final String QUERY_UPDATE = String.format(
            "UPDATE %s SET estado=?, fecha_solucion=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_ELIMINAR = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_VER = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private final String QUERY_LIST = "SELECT * FROM " + TABLE;
    private final String MESSAGE_TRYCATCH = " ERROR MODELO: " + TABLE.toUpperCase() + " ";
    private SQLConnection connection;
    private PreparedStatement ps;
    private ResultSet set;

    private String[] arrayData(ResultSet set) throws SQLException {
        return new String[]{
                String.valueOf(set.getInt("id")),
                String.valueOf(set.getString("contrato_id")),
                String.valueOf(set.getString("incidencia_id")),
                String.valueOf(set.getString("estado")),
                String.valueOf(set.getTimestamp("created_at"))
        };
    }

    void preparerInsertState(PreparedStatement ps) throws SQLException {
        try {
            // Intentar establecer los valores
            ps.setInt(1, contrato_id);
            ps.setInt(2, incidencia_id);
            ps.setString(3, estado);
            ps.setTimestamp(4, Timestamp.valueOf(created_at != null ? created_at : LocalDateTime.now()));
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
            ps.setString(1, estado);
            ps.setTimestamp(2, Timestamp.valueOf(fecha_solucion != null ? fecha_solucion : LocalDateTime.now()));
            ps.setTimestamp(3, Timestamp.valueOf(created_at != null ? created_at : LocalDateTime.now()));
            ps.setInt(4, id);
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
            preparerInsertState(ps);
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
        } finally {
            // Cerrar el PreparedStatement
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement: " + TABLE + e.getMessage());
                mensaje = MESSAGE_TRYCATCH+" (GUARDAR) Error al cerrar PreparedStatement: " + TABLE + e.getMessage();
            }
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
        } finally {
            // Cerrar el PreparedStatement
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(" (MODIFICAR) Error al cerrar PreparedStatement: ".toUpperCase() + TABLE + e.getMessage());
                mensaje = MESSAGE_TRYCATCH+" (MODIFICAR) Error al cerrar PreparedStatement: ".toUpperCase() + TABLE + e.getMessage();
            }
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
                ps.setInt(1, id);
                eliminado = ps.executeUpdate() > 0;
            }
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
        init_conexion();
        try (PreparedStatement ps = connection.connect().prepareStatement(QUERY_VER)) {
            ps.setInt(1, id);
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
