package NEGOCIO;

import DATA.DContrato;
import UTILS.Help;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NContrato {
    private DContrato DATA;
    private final NCliente NCliente = new NCliente();
    private final NServicio NServicio = new NServicio();
    public NContrato() {}

    public Object[] guardar(int cliente_id, int servicio_id, String descripcion, double precio_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin) throws SQLException, ParseException {
        if (cliente_id <= 0 || servicio_id <= 0 || descripcion == null || descripcion.isEmpty() || precio_total < 0 || estado == null || estado.isEmpty()) {
            return new Object[]{false, "Los valores de cliente_id, servicio_id, descripcion, precio_total y estado deben ser válidos."};
        }
        // Verificar si el cliente_id existe en la base de datos
        Object[] clienteExists = NCliente.ver(cliente_id);
        if (clienteExists == null || clienteExists.length == 0) {
            return new Object[]{false, "El cliente_id no existe: " + cliente_id + ". Consulte la lista de clientes."+ Help.CLIENTE+"_LIS[]"};
        }
        // Verificar si el servicio_id existe en la base de datos
        Object[] servicioExists = NServicio.ver(servicio_id);
        if (servicioExists == null || servicioExists.length == 0) {
            return new Object[]{false, "El servicio_id no existe: " + servicio_id + ". Consulte la lista de servicios."+Help.SERVICIO+"_LIS[]"};
        }
        DATA = new DContrato(cliente_id, servicio_id, descripcion, precio_total, estado, fecha_inicio, fecha_fin);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String descripcion, double precio_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin) throws SQLException, ParseException {
        if (id <= 0 || descripcion == null || descripcion.isEmpty() || precio_total < 0 || estado == null || estado.isEmpty()) {
            return new Object[]{false, "El id debe ser mayor a cero y los campos descripcion, precio_total y estado no pueden ser nulos o vacíos."};
        }
        // Verificar si el id existe en la base de datos
        String[] contratoExists = ver(id);
        if (contratoExists == null || contratoExists.length == 0) {
            return new Object[]{false, "El id no existe: " + id + ". Consulte la lista de contratos."+ Help.CONTRATOS+"_LIS[]"};
        }
        DATA = new DContrato(0, 0, descripcion, precio_total, estado, fecha_inicio, fecha_fin);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }

    public Object[] eliminar(int id) throws SQLException {
        try (DContrato modelo = new DContrato()) {
            modelo.setId(id);
            return modelo.eliminar();
        }
    }
    
    public String[] ver(int id) throws SQLException {
        DATA = new DContrato();
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }

    public List<String[]> listar() throws SQLException {
        DATA = new DContrato();
        ArrayList<String[]> categoria = (ArrayList<String[]>) DATA.listar();
        DATA.desconectar();
        return categoria;
    }    
}
