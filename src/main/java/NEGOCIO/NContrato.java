package NEGOCIO;

import DATA.DCliente;
import DATA.DContrato;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NContrato {

    private DContrato DATA;

    public NContrato() {}

    public Object[] guardar(int cliente_id, int servicio_id, String descripcion, double precio_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin) throws SQLException, ParseException {
        DATA = new DContrato(cliente_id, servicio_id, descripcion, precio_total, estado, fecha_inicio, fecha_fin);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String descripcion, double precio_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin) throws SQLException, ParseException {
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
