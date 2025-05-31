package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEquipoTrabajos;
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

    private DEquipoTrabajos DATA;

    public NEquipoTrabajos() {}

    public Object[] guardar(String nombre, String descripcion, String estado) throws SQLException, ParseException {
        DATA = new DEquipoTrabajos(nombre, descripcion, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String nombre, String descripcion, String estado) throws SQLException, ParseException {
        DATA = new DEquipoTrabajos(nombre, descripcion, estado);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DEquipoTrabajos modelo = new DEquipoTrabajos()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DEquipoTrabajos();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DEquipoTrabajos();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
