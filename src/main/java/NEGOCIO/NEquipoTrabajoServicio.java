package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEmpleadoEquipoTrabajos;
import DATA.DEquipoTrabajoServicio;
import DATA.DEquipoTrabajos;
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
public class NEquipoTrabajoServicio {

    private DEquipoTrabajoServicio DATA;
    private NEquipoTrabajos NEquipoTrabajos = new NEquipoTrabajos();
    private NServicio NServicio = new NServicio();

    public NEquipoTrabajoServicio() {}

    public Object[] guardar(int equipo_id, int servicio_id, String estado) throws SQLException, ParseException {
        if (equipo_id <= 0 || servicio_id <= 0 || estado == null || estado.isEmpty()) {
            return new Object[]{false, "Los valores de equipo_id, servicio_id y estado deben ser válidos."};
        }
        // Verificar si el equipo_id existe en la base de datos
        Object[] equipoTrabajoExists = NEquipoTrabajos.ver(equipo_id);
        if (equipoTrabajoExists == null || equipoTrabajoExists.length == 0) {
            return new Object[]{false, "El equipo_id no existe: " + equipo_id + ". Consulte la lista de equipos de trabajo."+ Help.EQUIPO_TRABAJO+"_LIS[]"};
        }
        // Verificar si el servicio_id existe en la base de datos
        Object[] servicioExists = NServicio.ver(servicio_id);
        if (servicioExists == null || servicioExists.length == 0) {
            return new Object[]{false, "El servicio_id no existe: " + servicio_id + ". Consulte la lista de servicios."+ Help.SERVICIO+"_LIS[]"};
        }
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
