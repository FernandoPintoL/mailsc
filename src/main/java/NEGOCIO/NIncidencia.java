package NEGOCIO;

import DATA.DCliente;
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
public class NIncidencia {

    private DIncidencia DATA;

    public NIncidencia() {}

    public Object[] guardar(String nombre, String descripcion) throws SQLException, ParseException {
        DATA = new DIncidencia(nombre, descripcion);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String nombre, String descripcion) throws SQLException, ParseException {
        if (id <= 0 || nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty()) {
            return new Object[]{false, "Los campos id, nombre y descripcion no pueden ser nulos o vacíos."};
        }
        // Verificar si el ID existe en la base de datos
        String[] incidenciaExists = ver(id);
        if (incidenciaExists == null || incidenciaExists.length == 0) {
            return new Object[]{false, "El ID no existe: " + id + ". Consulte la lista de incidencias."+ Help.INCIDENCIAS +"_LIS[]"};
        }
        DATA = new DIncidencia(nombre, descripcion);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DIncidencia modelo = new DIncidencia()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
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
