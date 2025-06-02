package NEGOCIO;

import DATA.DProductoServicio;
import DATA.DServicios;
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
public class NServicio {

    private DServicios DATA;

    public NServicio() {}

    public Object[] guardar(String nombre, String descripcion, double precio, String frecuencia, String estado) throws SQLException, ParseException {
        DATA = new DServicios(nombre, descripcion, precio, frecuencia, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String nombre, String descripcion, double precio, String frecuencia, String estado) throws SQLException, ParseException {
        if (id <= 0 || nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty() || precio < 0 || frecuencia == null || frecuencia.isEmpty() || estado == null || estado.isEmpty()) {
            return new Object[]{false, "Los campos id, nombre, descripcion, precio, frecuencia y estado no pueden ser nulos o vacíos."};
        }
        // Verificar si el ID existe en la base de datos
        String[] servicioExists = ver(id);
        if (servicioExists == null || servicioExists.length == 0) {
            return new Object[]{false, "El ID no existe: " + id + ". Consulte la lista de servicios."+ Help.SERVICIO+"_LIS[]"};
        }
        DATA = new DServicios(nombre, descripcion, precio, frecuencia, estado);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DServicios modelo = new DServicios()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DServicios();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DServicios();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
