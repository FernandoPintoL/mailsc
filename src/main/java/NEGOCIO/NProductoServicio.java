package NEGOCIO;

import DATA.DEquipoTrabajoServicio;
import DATA.DProducto;
import DATA.DProductoServicio;

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

    public Object[] guardar(int producto_id, int servicio_id, Double cantidad) throws SQLException, ParseException {
        if (producto_id <= 0 || servicio_id <= 0 || cantidad == null || cantidad <= 0) {
            return new Object[]{"Error", "Los valores de producto_id, servicio_id y cantidad deben ser mayores a cero."};
        }
        // Verificar si el producto_id existe en la base de datos
        Object[] productoExists = NProducto.ver(producto_id);
        if (productoExists == null || productoExists.length == 0) {
            return new Object[]{"Error", "El producto_id no existe: " + producto_id + ". Consulte la lista de productos."};
        }
        // Verificar si el servicio_id existe en la base de datos
        Object[] servicioExists = NServicio.ver(servicio_id);
        if (servicioExists == null || servicioExists.length == 0) {
            return new Object[]{"Error", "El servicio_id no existe: " + servicio_id + ". Consulte la lista de servicios."};
        }
        DATA = new DProductoServicio(producto_id, servicio_id, cantidad);
        Object[] response = DATA.guardar();
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
            return new Object[]{false, "El id no existe: " + id + ". Consulte la lista de productos y servicios."};
        }
        DATA = new DProductoServicio(0, 0, cantidad);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
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
