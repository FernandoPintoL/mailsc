package NEGOCIO;

import DATA.DIncidencia;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NIncidencia {

    private DIncidencia DATA;

    public NIncidencia() {}

    public Object[] guardar(int cliente_id, int contrato_id, String descripcion, String estado) throws SQLException, ParseException {
        DATA = new DIncidencia(cliente_id, contrato_id,descripcion, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String descripcion, String estado) throws SQLException, ParseException {
        DATA = new DIncidencia(id, descripcion, estado);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public boolean eliminar(int id) throws SQLException {
        DATA = new DIncidencia();
        DATA.setId(id);
        boolean response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DIncidencia();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DIncidencia();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
