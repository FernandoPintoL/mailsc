package NEGOCIO;

import DATA.DContrato;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NContrato {

    private DContrato DATA;

    public NContrato() {}

    public Object[] guardar(int producto_id, int servicio_id, double cantidad, String descripcion) throws SQLException, ParseException {
        DATA = new DContrato(producto_id, servicio_id, cantidad, descripcion);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, int producto_id, int servicio_id, double cantidad, String descripcion) throws SQLException, ParseException {
        DATA = new DContrato(producto_id, servicio_id, cantidad, descripcion);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public boolean eliminar(int id) throws SQLException {
        DATA = new DContrato();
        DATA.setId(id);
        boolean response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DContrato();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DContrato();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
