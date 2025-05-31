package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEmpleadoEquipoTrabajos;
import DATA.DEquipoTrabajoServicio;
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
public class NEquipoTrabajoServicio {

    private DEquipoTrabajoServicio DATA;

    public NEquipoTrabajoServicio() {}

    public Object[] guardar(int equipo_id, int servicio_id, String estado) throws SQLException, ParseException {
        DATA = new DEquipoTrabajoServicio(equipo_id, servicio_id, estado);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String estado) throws SQLException, ParseException {
        DATA = new DEquipoTrabajoServicio(0, 0, estado);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DEquipoTrabajoServicio modelo = new DEquipoTrabajoServicio()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DEquipoTrabajoServicio();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DEquipoTrabajoServicio();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
