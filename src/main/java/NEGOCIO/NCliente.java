package NEGOCIO;

import DATA.DCliente;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
/**
 * Clase de negocio para la gestión de clientes
 * Implementa la lógica de negocio relacionada con clientes
 * 
 * @author fpl
 */
public class NCliente {

    /**
     * Constructor por defecto
     */
    public NCliente() {}
    /**
     * Guarda un nuevo cliente en la base de datos
     * 
     * @param ci Cédula de identidad del cliente
     * @param nombre Nombre del cliente
     * @param direccion Dirección del cliente
     * @param telefono Teléfono del cliente
     * @param tipo_cliente Tipo de cliente
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] guardar(String ci, String nombre, String direccion, String telefono, String tipo_cliente) throws SQLException, ParseException {
        try (DCliente cliente = new DCliente(ci, nombre, telefono, direccion, tipo_cliente)) {
            return cliente.guardar();
        }
    }
    /**
     * Modifica un cliente existente en la base de datos
     * 
     * @param id ID del cliente a modificar
     * @param ci Nueva cédula de identidad del cliente
     * @param nombre Nuevo nombre del cliente
     * @param direccion Nueva dirección del cliente
     * @param telefono Nuevo teléfono del cliente
     * @param tipo_cliente Nuevo tipo de cliente
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] modificar(int id, String ci, String nombre, String telefono, String direccion, String tipo_cliente) throws SQLException, ParseException {
        try (DCliente cliente = new DCliente(ci, nombre, telefono, direccion, tipo_cliente)) {
            cliente.setId(id);
            String[] ver = ver(id);
            if (ver == null) {
                return new Object[]{false, "El cliente con ID " + id + " no existe."};
            }
            return cliente.modificar();
        }
    }
    /**
     * Elimina un cliente de la base de datos
     * 
     * @param id ID del cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws SQLException Si ocurre un error de SQL
     */
    public Object[] eliminar(int id) throws SQLException {
        try (DCliente modelo = new DCliente()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    /**
     * Busca un cliente por su ID
     * 
     * @param id ID del cliente a buscar
     * @return Array de strings con los datos del cliente o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] ver(int id) throws SQLException {
        try (DCliente cliente = new DCliente()) {
            cliente.setId(id);
            return cliente.ver();
        }
    }
    /**
     * Lista todos los clientes
     * 
     * @return Lista de arrays de strings con los datos de los clientes
     * @throws SQLException Si ocurre un error de SQL
     */
    public List<String[]> listar() throws SQLException {
        try (DCliente cliente = new DCliente()) {
            return cliente.listar();
        }
    }
    /**
     * Busca un cliente por su CI
     * 
     * @param ci CI del cliente a buscar
     * @return Array de strings con los datos del cliente o null si no existe
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] buscarPorCi(String ci) throws SQLException {
        try (DCliente cliente = new DCliente()) {
            return cliente.existe(ci);
        }
    }
}
