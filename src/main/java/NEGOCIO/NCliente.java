package NEGOCIO;

import DATA.DCliente;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author angela
 */
public class NCliente {

    private DCliente DATA;

    public NCliente() {}

    public Object[] guardar(String ci, String nombre, String direccion, String telefono, String tipo_cliente) throws SQLException, ParseException {
        DATA = new DCliente(ci, nombre, direccion, telefono, tipo_cliente);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String ci, String nombre, String direccion, String telefono, String tipo_cliente) throws SQLException, ParseException {
        DATA = new DCliente(ci, nombre, direccion, telefono, tipo_cliente);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public boolean eliminar(int id) throws SQLException {
        DATA = new DCliente();
        DATA.setId(id);
        boolean response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DCliente();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }

    public String[] ver(int id) throws SQLException {
        DATA = new DCliente();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }
}
