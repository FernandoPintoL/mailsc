package NEGOCIO;

import DATA.DProducto;
import UTILS.Help;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Clase de negocio para la gestión de productos
 * Implementa la lógica de negocio relacionada con productos
 * 
 * @author fpl
 */
public class NProducto {

    /**
     * Constructor por defecto
     */
    public NProducto() {}

    /**
     * Guarda un nuevo producto en la base de datos
     * 
     * @param nombre Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio Precio del producto
     * @param stock Cantidad en stock
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] guardar(String nombre, String descripcion, Double precio, Double stock) throws SQLException, ParseException {
        if (nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty() || precio == null || precio <= 0 || stock == null || stock < 0) {
            return new Object[]{false, "Los campos Nombre, Descripción, Precio y Stock no pueden estar vacíos o ser inválidos."};
        }
        try (DProducto producto = new DProducto(nombre, descripcion, precio, stock)) {
            return producto.guardar();
        }
    }

    /**
     * Modifica un producto existente en la base de datos
     * 
     * @param id ID del producto a modificar
     * @param nombre Nuevo nombre del producto
     * @param descripcion Nueva descripción del producto
     * @param precio Nuevo precio del producto
     * @param stock Nueva cantidad en stock
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] modificar(int id, String nombre, String descripcion, Double precio, Double stock) throws SQLException, ParseException {
        if (id <= 0 || nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty() || precio == null || precio <= 0 || stock == null || stock < 0) {
            return new Object[]{false, "El ID debe ser mayor a cero y los campos Nombre, Descripción, Precio y Stock no pueden estar vacíos o ser inválidos."};
        }
        // Verificar si el ID existe en la base de datos
        String[] productoExists = ver(id);
        if (productoExists == null || productoExists.length == 0) {
            return new Object[]{false, "El ID no existe: " + id + ". Consulte la lista de productos."+ Help.PRODUCTO+"_LIS[]"};
        }
        try (DProducto producto = new DProducto(nombre, descripcion, precio, stock)) {
            producto.setId(id);
            return producto.modificar();
        }
    }

    /**
     * Elimina un producto de la base de datos
     * 
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws SQLException Si ocurre un error de SQL
     */
    public Object[] eliminar(int id) throws SQLException {
        try (DProducto producto = new DProducto()) {
            producto.setId(id);
            return producto.eliminar();
        }
    }

    /**
     * Busca un producto por su ID
     * 
     * @param id ID del producto a buscar
     * @return Array de strings con los datos del producto o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] ver(int id) throws SQLException {
        try (DProducto producto = new DProducto()) {
            producto.setId(id);
            return producto.ver();
        }
    }

    /**
     * Lista todos los productos
     * 
     * @return Lista de arrays de strings con los datos de los productos
     * @throws SQLException Si ocurre un error de SQL
     */
    public List<String[]> listar() throws SQLException {
        try (DProducto producto = new DProducto()) {
            return producto.listar();
        }
    }

    /**
     * Busca un producto por su nombre
     * 
     * @param nombre Nombre del producto a buscar
     * @return Array de strings con los datos del producto o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] buscarPorNombre(String nombre) throws SQLException {
        try (DProducto producto = new DProducto()) {
            return producto.existe(nombre);
        }
    }

    public Object[] verStock(int productoId) throws SQLException {
    try (DProducto producto = new DProducto()) {
            producto.setId(productoId);
            return producto.verStock();
        }
    }

    public Object[] modificarStock(int productoId, double v) {
    try (DProducto producto = new DProducto()) {
            producto.setId(productoId);
            return producto.modificarStock(v);
        }
    }
}
