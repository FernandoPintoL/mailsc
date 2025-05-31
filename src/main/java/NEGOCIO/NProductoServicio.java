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

    public NProductoServicio() {}

    public Object[] guardar(int producto_id, int servicio_id, Double cantidad) throws SQLException, ParseException {
        DATA = new DProductoServicio(producto_id, servicio_id, cantidad);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, Double cantidad) throws SQLException, ParseException {
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
