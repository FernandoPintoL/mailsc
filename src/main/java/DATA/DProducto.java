package DATA;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Producto
 *
 * @author fpl
 */
public class DProducto extends BaseDAO<DProducto> {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private double stock;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private static final String TABLE = "productos";
    private static final String QUERY_ID = "id";
    private static final String Q_NOMBRE = "nombre";
    private static final String QUERY_INSERT =
            "INSERT INTO productos (nombre, descripcion, precio, stock, created_at) VALUES (?,?,?,?,?)";
    private static final String QUERY_UPDATE =
            "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, updated_at=? WHERE id=?";
    private static final String QUERY_DELETE = "DELETE FROM productos WHERE id=?";
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM productos WHERE id=?";
    private static final String QUERY_FIND_BY_NOMBRE = "SELECT * FROM productos WHERE nombre=?";
    private static final String QUERY_LIST_ALL = "SELECT * FROM productos";
    /**
     * Constructor por defecto
     */
    public DProducto() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
    }
    /**
     * Constructor con parámetros
     *
     * @param nombre      Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio      Precio del producto
     * @param stock       Stock disponible
     */
    public DProducto(String nombre, String descripcion, double precio, double stock) {
        super(TABLE);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.created_at = LocalDateTime.now();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
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

    @Override
    protected String[] entityToStringArray(DProducto entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getNombre(),
                entity.getDescripcion(),
                String.valueOf(entity.getPrecio()),
                String.valueOf(entity.getStock()),
                createdAtStr,
                updatedAtStr
        };
    }

    @Override
    protected DProducto stringArrayToEntity(String[] data) {
        DProducto producto = new DProducto();
        try {
            producto.setId(Integer.parseInt(data[0]));
            producto.setNombre(data[1]);
            producto.setDescripcion(data[2]);
            producto.setPrecio(Double.parseDouble(data[3]));
            producto.setStock(Double.parseDouble(data[4]));
            producto.setCreated_at(toLocalDateTime(data[5]));
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir datos de producto: " + e.getMessage());
        }
        return producto;
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
    protected void prepareStatementForEntity(DProducto entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT
        ps.setString(1, entity.getNombre());
        ps.setString(2, entity.getDescripcion());
        ps.setDouble(3, entity.getPrecio());
        ps.setDouble(4, entity.getStock());

        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() > 5) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(5, toTimestamp(entity.getUpdated_at()));
            ps.setInt(6, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(5, toTimestamp(entity.getCreated_at()));
        }
    }

    @Override
    protected void prepareStatementForId(int id) throws SQLException {
        if (ps != null) {
            ps.setInt(1, id);
        }
    }

    /**
     * Método para preparar una consulta por nombre
     *
     * @param nombre Nombre a buscar
     * @throws SQLException Si ocurre un error al preparar la consulta
     */
    protected void prepareStatementForNombre(String nombre) throws SQLException {
        if (ps != null) {
            ps.setString(1, nombre);
        }
    }

    /**
     * Guarda un producto en la base de datos
     *
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException   Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] guardar() throws SQLException, ParseException {
        // Verificar si ya existe un producto con el mismo nombre
        DProducto existente = findByNombre(this.nombre);
        if (existente != null) {
            return new Object[]{false, "El producto ya está registrado con ID: " + existente.getId()};
        }

        return save(this);
    }

    /**
     * Modifica un producto en la base de datos
     *
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException   Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] modificar() throws SQLException, ParseException {
        // Verificar si existe el producto
        DProducto existente = findById(this.id);
        if (existente == null) {
            return new Object[]{false, "El producto con ID " + this.id + " no existe"};
        }

        return update(this);
    }

    /**
     * Elimina un producto de la base de datos
     *
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public Object[] eliminar() {
        return delete(this.id);
    }

    /**
     * Busca un producto por su ID
     *
     * @return Array de strings con los datos del producto o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] ver() throws SQLException {
        DProducto producto = findById(this.id);
        return producto != null ? entityToStringArray(producto) : null;
    }

    /**
     * Busca un producto por su nombre
     *
     * @param nombre Nombre del producto a buscar
     * @return Array de strings con los datos del producto o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] existe(String nombre) throws SQLException {
        DProducto producto = findByNombre(nombre);
        return producto != null ? entityToStringArray(producto) : null;
    }

    /**
     * Busca un producto por su nombre
     *
     * @param nombre Nombre del producto a buscar
     * @return Producto encontrado o null si no existe
     */
    public DProducto findByNombre(String nombre) {
        DProducto producto = null;

        try {
            if (connection == null || connection.isClosed()) {
                init_conexion();
            }

            ps = connection.prepareStatement(QUERY_FIND_BY_NOMBRE);
            prepareStatementForNombre(nombre);

            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String[] data = arrayData(resultSet);
                producto = stringArrayToEntity(data);
            }
        } catch (SQLException e) {
            logSQLException("Error al buscar producto por nombre", e);
        } finally {
            closeResources(false);
        }

        return producto;
    }

    /**
     * Lista todos los productos
     *
     * @return Lista de arrays de strings con los datos de los productos
     * @throws SQLException Si ocurre un error de SQL
     */
    public List<String[]> listar() throws SQLException {
        List<DProducto> productos = findAll();
        List<String[]> result = new ArrayList<>();

        for (DProducto producto : productos) {
            result.add(entityToStringArray(producto));
        }

        return result;
    }
}
