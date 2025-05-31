package NEGOCIO;

import DATA.DProductoServicio;
import DATA.DServicios;
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
