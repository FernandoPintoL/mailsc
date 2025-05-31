package DATA;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Cliente
 *
 * @author fpl
 */
public class DCliente extends BaseDAO<DCliente> {
    private int id;
    private String ci;
    private String nombre;
    private String telefono;
    private String direccion;
    private String tipo_cliente;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    // Constantes para consultas SQL
    private static final String TABLE_NAME = "clientes";
    private static final String QUERY_INSERT =
            "INSERT INTO "+TABLE_NAME+" (ci, nombre, telefono, direccion, tipo_cliente, created_at) VALUES (?,?,?,?,?,?)";
    private static final String QUERY_UPDATE =
            "UPDATE "+TABLE_NAME+" SET ci=?, nombre=?, telefono=?, direccion=?, tipo_cliente=?, updated_at=? WHERE id=?";
    private static final String QUERY_DELETE = "DELETE FROM "+TABLE_NAME+" WHERE id=?";
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM "+TABLE_NAME+" WHERE id=?";
    private static final String QUERY_FIND_BY_CI = "SELECT * FROM "+TABLE_NAME+" WHERE ci=?";
    private static final String QUERY_LIST_ALL = "SELECT * FROM "+TABLE_NAME;
    /**
     * Constructor por defecto
     */
    public DCliente() {
        super(TABLE_NAME);
        this.created_at = LocalDateTime.now();
    }
    /**
     * Constructor con parámetros
     *
     * @param ci           Cédula de identidad del cliente
     * @param nombre       Nombre del cliente
     * @param telefono     Teléfono del cliente
     * @param direccion    Dirección del cliente
     * @param tipo_cliente Tipo de cliente
     */
    public DCliente(String ci, String nombre, String telefono, String direccion, String tipo_cliente) {
        super(TABLE_NAME);
        this.ci = ci;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipo_cliente = tipo_cliente;
        this.created_at = LocalDateTime.now();
    }
    // Getters y setters
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
    // extends
    @Override
    protected String[] entityToStringArray(DCliente entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getCi(),
                entity.getNombre(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getTipo_cliente(),
                createdAtStr,
                updatedAtStr
        };
    }
    @Override
    protected DCliente stringArrayToEntity(String[] data) {
        DCliente cliente = new DCliente();
        try {
            cliente.setId(Integer.parseInt(data[0]));
            cliente.setCi(data[1]);
            cliente.setNombre(data[2]);
            cliente.setTelefono(data[3]);
            cliente.setDireccion(data[4]);
            cliente.setTipo_cliente(data[5]);
            cliente.setCreated_at(toLocalDateTime(data[7]));
            cliente.setUpdated_at(toLocalDateTime(data[8]));
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir datos de cliente: " + e.getMessage());
        }
        return cliente;
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
    protected void prepareStatementForEntity(DCliente entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getCi());
        ps.setString(2, entity.getNombre());
        ps.setString(3, entity.getTelefono());
        ps.setString(4, entity.getDireccion());
        ps.setString(5, entity.getTipo_cliente());
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
    public DCliente findByCi(String ci) {
        DCliente cliente = null;

        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }

            ps = connection.prepareStatement(QUERY_FIND_BY_CI);
            prepareStatementForCi(ci);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String[] data = arrayData(resultSet);
                cliente = stringArrayToEntity(data);
            }
        } catch (SQLException e) {
            logSQLException("Error al buscar cliente por CI", e);
        } finally {
            closeResources(false);
        }

        return cliente;
    }
    /**
     * Guarda un cliente en la base de datos
     *
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     */
    public Object[] guardar() throws SQLException {
        // Verificar si ya existe un cliente con la misma CI
        DCliente existente = findByCi(this.ci);
        if (existente != null) {
            return new Object[]{false, "El cliente ya está registrado con ID: " + existente.getId()};
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
    public Object[] modificar() throws SQLException, ParseException {return update(this);
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
        DCliente cliente = findById(this.id);
        return cliente != null ? entityToStringArray(cliente) : null;
    }
    /**
     * Busca un cliente por su CI
     *
     * @param ci CI del cliente a buscar
     * @return Array de strings con los datos del cliente o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] existe(String ci) throws SQLException {
        DCliente cliente = findByCi(ci);
        return cliente != null ? entityToStringArray(cliente) : null;
    }
    /**
     * Lista todos los clientes
     *
     * @return Lista de arrays de strings con los datos de los clientes
     * @throws SQLException Si ocurre un error de SQL
     */
    public List<String[]> listar() throws SQLException {
        List<DCliente> clientes = findAll();
        List<String[]> result = new ArrayList<>();
        for (DCliente cliente : clientes) {
            result.add(entityToStringArray(cliente));
        }
        return result;
    }
}
