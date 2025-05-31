package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEmpleadoEquipoTrabajos;
import DATA.DEquipoTrabajos;
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
public class NEmpleadoEquipoTrabajos {

    private DEmpleadoEquipoTrabajos DATA;

    public NEmpleadoEquipoTrabajos() {}

    public Object[] guardar(int empleado_id, int equipo_trabajo, String estado, String ocupacion) throws SQLException, ParseException {
        DATA = new DEmpleadoEquipoTrabajos(empleado_id, equipo_trabajo, estado, ocupacion);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String estado, String ocupacion) throws SQLException, ParseException {
        DATA = new DEmpleadoEquipoTrabajos(0, 0, estado, ocupacion);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DEmpleadoEquipoTrabajos modelo = new DEmpleadoEquipoTrabajos()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DEmpleadoEquipoTrabajos();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DEmpleadoEquipoTrabajos();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
