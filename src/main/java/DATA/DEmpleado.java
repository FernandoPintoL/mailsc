/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpl
 */
public class DEmpleado extends BaseDAO<DEmpleado> {
    private int id;
    private String ci;
    private String nombre;
    private String telefono;
    private String puesto;
    private String estado;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
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
    public String getPuesto() {
        return puesto;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
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
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    private static final String TABLE = "empleados";
    private static final String QUERY_ID = "id";
    private static final String Q_CI = "ci";
    private static final String QUERY_INSERT = String.format(
            "INSERT INTO %s (ci, nombre, telefono, puesto, estado, created_at) VALUES (?,?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET ci=?, nombre=?, telefono=?, puesto=?, estado=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM "+TABLE+" WHERE id=?";
    private static final String QUERY_LIST_ALL = "SELECT * FROM "+TABLE;
    private static final String QUERY_FIND_BY_CI = String.format("SELECT * FROM %s WHERE %s=?", TABLE, Q_CI);
    public DEmpleado() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
    }
    public DEmpleado(String ci, String nombre, String telefono, String puesto, String estado) {
        super(TABLE);
        this.ci = ci;
        this.nombre = nombre;
        this.telefono = telefono;
        this.puesto = puesto;
        this.estado = estado;
        this.created_at = LocalDateTime.now();
    }
    @Override
    protected String[] entityToStringArray(DEmpleado entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getCi(),
                entity.getNombre(),
                entity.getTelefono(),
                entity.getPuesto(),
                entity.getEstado(),
                createdAtStr,
                updatedAtStr
        };
    }
    @Override
    protected DEmpleado stringArrayToEntity(String[] data) {
        DEmpleado modelo = new DEmpleado();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setCi(data[1]);
            modelo.setNombre(data[2]);
            modelo.setTelefono(data[3]);
            modelo.setPuesto(data[4]);
            modelo.setEstado(data[5]);
            modelo.setCreated_at(toLocalDateTime(data[7]));
            modelo.setUpdated_at(toLocalDateTime(data[8]));
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir datos del modelo: " + e.getMessage());
        }
        return modelo;
    }
    @Override
    protected String getInsertQuery() {
        return QUERY_INSERT;
    }
    @Override
    protected String getUpdateQuery() {
        return QUERY_UPDATE;
    }
    @Override
    protected String getDeleteQuery() {
        return QUERY_DELETE;
    }
    @Override
    protected String getFindByIdQuery() {
        return QUERY_FIND_BY_ID;
    }
    @Override
    protected String getListAllQuery() {
        return QUERY_LIST_ALL;
    }
    @Override
    protected void prepareStatementForEntity(DEmpleado entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getCi());
        ps.setString(2, entity.getNombre());
        ps.setString(3, entity.getTelefono());
        ps.setString(4, entity.getPuesto());
        ps.setString(5, entity.getEstado());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() > 6) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(6, toTimestamp(entity.getUpdated_at()));
            ps.setInt(7, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(6, toTimestamp(entity.getCreated_at()));
        }
    }
    @Override
    protected void prepareStatementForId(int id) throws SQLException {
        if (ps != null) {
            ps.setInt(1, id);
        }
    }
    /**
     * Método para preparar una consulta por CI
     *
     * @param ci CI a buscar
     * @throws SQLException Si ocurre un error al preparar la consulta
     */
    protected void prepareStatementForCi(String ci) throws SQLException {
        if (ps != null) {
            ps.setString(1, ci);
        }
    }
    /**
     * Busca un cliente por su CI
     *
     * @param ci CI del cliente a buscar
     * @return Cliente encontrado o null si no existe
     */
    public DEmpleado findByCi(String ci) {
        DEmpleado modelo = null;
        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }

            ps = connection.prepareStatement(QUERY_FIND_BY_CI);
            prepareStatementForCi(ci);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String[] data = arrayData(resultSet);
                modelo = stringArrayToEntity(data);
            }
        } catch (SQLException e) {
            logSQLException("Error al buscar empleado por CI", e);
        } finally {
            closeResources(false);
        }

        return modelo;
    }
    /**
     * Guarda un cliente en la base de datos
     *
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     */
    public Object[] guardar() throws SQLException {
        // Verificar si ya existe un cliente con la misma CI
        DEmpleado existente = findByCi(this.ci);
        if (existente != null) {
            return new Object[]{false, "El modelo ya está registrado con ID: " + existente.getId()};
        }
        return save(this);
    }
    /**
     * Modifica un cliente en la base de datos
     *
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException   Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] modificar() throws SQLException, ParseException {
        return update(this);
    }
    /**
     * Elimina un cliente de la base de datos
     *
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public Object[] eliminar() throws SQLException {
        return delete(this.id);
    }
    /**
     * Busca un cliente por su ID
     *
     * @return Array de strings con los datos del cliente o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] ver() throws SQLException {
        DEmpleado modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    /**
     * Busca un cliente por su CI
     *
     * @param ci CI del cliente a buscar
     * @return Array de strings con los datos del cliente o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] existe(String ci) throws SQLException {
        DEmpleado modelo = findByCi(ci);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    /**
     * Lista todos los clientes
     *
     * @return Lista de arrays de strings con los datos de los clientes
     * @throws SQLException Si ocurre un error de SQL
     */
    public List<String[]> listar() throws SQLException {
        List<DEmpleado> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DEmpleado modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}
