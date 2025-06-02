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
    public Object[] guardar(String ci, String nombre, String direccion, String telefono, String tipo_cliente) throws SQLException, ParseException {
        if (ci == null || ci.isEmpty() || nombre == null || nombre.isEmpty() || telefono == null || telefono.isEmpty() || direccion == null || direccion.isEmpty() || tipo_cliente == null || tipo_cliente.isEmpty()) {
            return new Object[]{false, "Los campos CI, Nombre, Teléfono, Dirección y Tipo de Cliente no pueden estar vacíos."};
        }
        try (DCliente cliente = new DCliente(ci, nombre, telefono, direccion, tipo_cliente)) {
            return cliente.guardar();
        }
    }
    public Object[] modificar(int id, String ci, String nombre, String telefono, String direccion, String tipo_cliente) throws SQLException, ParseException {
        if (id <= 0 || ci == null || ci.isEmpty() || nombre == null || nombre.isEmpty() || telefono == null || telefono.isEmpty() || direccion == null || direccion.isEmpty() || tipo_cliente == null || tipo_cliente.isEmpty()) {
            return new Object[]{false, "El ID debe ser mayor a cero y los campos CI, Nombre, Teléfono, Dirección y Tipo de Cliente no pueden estar vacíos."};
        }
        // Verificar si el ID existe en la base de datos
        String[] clienteExists = ver(id);
        if (clienteExists == null || clienteExists.length == 0) {
            return new Object[]{false, "El ID no existe: " + id + ". Consulte la lista de clientes."};
        }
        try (DCliente cliente = new DCliente(ci, nombre, telefono, direccion, tipo_cliente)) {
            cliente.setId(id);
            return cliente.modificar();
        }
    }
    public Object[] eliminar(int id) throws SQLException {
        try (DCliente modelo = new DCliente()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    public String[] ver(int id) throws SQLException {
        try (DCliente cliente = new DCliente()) {
            cliente.setId(id);
            return cliente.ver();
        }
    }
    public List<String[]> listar() throws SQLException {
        try (DCliente cliente = new DCliente()) {
            return cliente.listar();
        }
    }
    public String[] buscarPorCi(String ci) throws SQLException {
        try (DCliente cliente = new DCliente()) {
            return cliente.existe(ci);
        }
    }
}
