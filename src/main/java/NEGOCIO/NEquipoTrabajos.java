package NEGOCIO;

import DATA.DEmpleado;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NEquipoTrabajos {

    private DEmpleado DATA;

    public NEquipoTrabajos() {}

    public Object[] guardar(String ci, String nombre, String telefono, String puesto, String estado) throws SQLException, ParseException {
        DATA = new DEmpleado(ci, nombre, telefono, puesto, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String ci, String nombre, String telefono, String puesto, String estado) throws SQLException, ParseException {
        DATA = new DEmpleado(ci, nombre, telefono, puesto, estado);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public boolean eliminar(int id) throws SQLException {
        DATA = new DEmpleado();
        DATA.setId(id);
        boolean response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DEmpleado();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DEmpleado();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
