package NEGOCIO;

import DATA.DEmpleado;
import DATA.DEmpleadoEquipoTrabajos;
import DATA.DEquipoTrabajos;
import DATA.DIncidencia;
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
public class NEmpleadoEquipoTrabajos {

    private DEmpleadoEquipoTrabajos DATA;
    private NEmpleado NEmpleado = new NEmpleado();
    private NEquipoTrabajos NEquipoTrabajos = new NEquipoTrabajos();

    public NEmpleadoEquipoTrabajos() {}

    public Object[] guardar(int empleado_id, int equipo_trabajo, String estado, String ocupacion) throws SQLException, ParseException {
        if (empleado_id <= 0 || equipo_trabajo <= 0 || estado == null || estado.isEmpty() || ocupacion == null || ocupacion.isEmpty()) {
            return new Object[]{false, "Los valores de empleado_id, equipo_trabajo, estado y ocupacion deben ser válidos."};
        }
        // Verificar si el empleado_id existe en la base de datos
        Object[] empleadoExists = NEmpleado.ver(empleado_id);
        if (empleadoExists == null || empleadoExists.length == 0) {
            return new Object[]{false, "El empleado_id no existe: " + empleado_id + ". Consulte la lista de empleados."+ Help.EMPLEADO+"_LIS[]"};
        }
        // Verificar si el equipo_trabajo existe en la base de datos
        Object[] equipoTrabajoExists = NEquipoTrabajos.ver(equipo_trabajo);
        if (equipoTrabajoExists == null || equipoTrabajoExists.length == 0) {
            return new Object[]{false, "El equipo_trabajo no existe: " + equipo_trabajo + ". Consulte la lista de equipos de trabajo."+ Help.EQUIPO_TRABAJO+"_LIS[]"};
        }
        DATA = new DEmpleadoEquipoTrabajos(empleado_id, equipo_trabajo, estado, ocupacion);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String estado, String ocupacion) throws SQLException, ParseException {
        if (id <= 0 || estado == null || estado.isEmpty() || ocupacion == null || ocupacion.isEmpty()) {
            return new Object[]{false, "El id debe ser mayor a cero y los campos estado y ocupacion no pueden ser nulos o vacíos."};
        }
        // Verificar si el id existe en la base de datos
        String[] empleadoEquipoTrabajoExists = ver(id);
        if (empleadoEquipoTrabajoExists == null || empleadoEquipoTrabajoExists.length == 0) {
            return new Object[]{false, "El id no existe: " + id + ". Consulte la lista de empleados y equipos de trabajo."+ Help.EMPLEADO_EQUIPO_TRABAJO+"_LIS[]"};
        }
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
