package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEquipoTrabajos;
import DATA.DFacturas;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NFacturas {

    private DFacturas DATA;

    public NFacturas() {}

    public Object[] guardar(int contrato_id, double precio_total, String estado) throws SQLException, ParseException {
        DATA = new DFacturas(contrato_id, precio_total, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, int contrato_id, double precio_total, String estado) throws SQLException, ParseException {
        DATA = new DFacturas(contrato_id, precio_total, estado);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public boolean eliminar(int id) throws SQLException {
        DATA = new DFacturas();
        DATA.setId(id);
        boolean response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DFacturas();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DFacturas();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
