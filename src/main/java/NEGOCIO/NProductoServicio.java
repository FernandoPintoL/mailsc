package NEGOCIO;

import DATA.DEquipoTrabajoServicio;
import DATA.DProducto;
import DATA.DProductoServicio;
import UTILS.Help;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NProductoServicio {

    private DProductoServicio DATA;
    private NProducto NProducto = new NProducto();
    private NServicio NServicio = new NServicio();

    public NProductoServicio() {}

    /**
     * Guarda un nuevo producto-servicio en la base de datos.
     *
     * @param producto_id ID del producto
     * @param servicio_id ID del servicio
     * @param cantidad Cantidad del producto-servicio
     * @return Objeto con el resultado [boolean éxito, String mensaje]
     * @throws SQLException Si ocurre un error de SQL
     * @throws ParseException Si ocurre un error al parsear fechas
     */
    public Object[] guardar(int producto_id, int servicio_id, Double cantidad) throws SQLException, ParseException {
        if (producto_id <= 0 || servicio_id <= 0 || cantidad == null || cantidad <= 0) {
            return new Object[]{false, "Los valores de producto_id, servicio_id y cantidad deben ser mayores a cero."};
        }
        // Verificar si el producto_id existe en la base de datos
        Object[] productoExists = NProducto.ver(producto_id);
        if (productoExists == null || productoExists.length == 0) {
            return new Object[]{false, "El producto_id no existe: " + producto_id + ". Consulte la lista de productos."};
        }
        // Verificar si el servicio_id existe en la base de datos
        Object[] servicioExists = NServicio.ver(servicio_id);
        if (servicioExists == null || servicioExists.length == 0) {
            return new Object[]{false, "El servicio_id no existe: " + servicio_id + ". Consulte la lista de servicios."};
        }
        // verificar el stock del producto no debe ser menor a la cantidad
        Object[] productoStock = NProducto.verStock(producto_id);
        if (productoStock == null || productoStock.length == 0 || Double.parseDouble((String) productoStock[2]) < cantidad) {
            return new Object[]{false, "El stock del producto_id " + producto_id + " es insuficiente para la cantidad solicitada."};
        }
        DATA = new DProductoServicio(producto_id, servicio_id, cantidad);
        Object[] response = DATA.guardar();
        // Actualizar el stock del producto
        Object[] modificacion = NProducto.modificarStock(producto_id, -cantidad);
        if (!(Boolean) modificacion[0]) {
            return new Object[]{false, "Error al actualizar el stock del producto: " + modificacion[1]};
        }
        // unir el mensaje de respuesta de guardar y modificar stock
        response[1] = response[1] + " | " + modificacion[1];
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, Double cantidad) throws SQLException, ParseException {
        if (id <= 0 || cantidad == null || cantidad <= 0) {
            return new Object[]{false, "El id debe ser mayor a cero y la cantidad no puede ser nula o menor o igual a cero."};
        }
        // Verificar si el id existe en la base de datos
        Object[] productoServicioExists = ver(id);
        if (productoServicioExists == null || productoServicioExists.length == 0) {
            return new Object[]{false, "El id no existe: " + id + ". Consulte la lista de productos y servicios. "+ Help.PRODUCTO_SERVICIOS+"_LIS[]"};
        }
        // obtener el servicio_id y producto_id del objeto existente
        int producto_id = Integer.parseInt(productoServicioExists[1].toString());
        int servicio_id = Integer.parseInt(productoServicioExists[2].toString());
        // obtener el stock del producto
        Object[] productoStock = NProducto.verStock(producto_id);
        if (productoStock == null || productoStock.length == 0) {
            return new Object[]{false, "El producto_id no existe: " + producto_id + ". Consulte la lista de productos."};
        }
        // verificar que el stock del producto no sea menor a la cantidad
        if (Double.parseDouble((String) productoStock[2]) < cantidad) {
            return new Object[]{false, "El stock del producto_id " + producto_id + " es insuficiente para la cantidad solicitada."};
        }
        // actualizar el stock del producto
        Object[] modificacionStock = NProducto.modificarStock(producto_id, -cantidad);
        if (!(Boolean) modificacionStock[0]) {
            return new Object[]{false, "Error al actualizar el stock del producto: " + modificacionStock[1]};
        }
        DATA = new DProductoServicio(0, 0, cantidad);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        // unir el mensaje de respuesta de modificar y modificar stock
        response[1] = response[1] + " | " + modificacionStock[1];
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DProductoServicio modelo = new DProductoServicio()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DProductoServicio();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DProductoServicio();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
