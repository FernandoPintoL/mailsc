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

    public Object[] guardar(int cliente_id, int servicio_id, String descripcion, String estado, Timestamp fecha_inicio, Timestamp fecha_fin, double monto_pagado) throws SQLException, ParseException {
        if (cliente_id <= 0 || servicio_id <= 0 || descripcion == null || descripcion.isEmpty() || estado == null || estado.isEmpty()) {
            return new Object[]{false, "Los campos cliente_id, servicio_id, descripcion y estado no pueden ser nulos o vacíos."};
        }
        if (fecha_inicio == null || fecha_fin == null) {
            return new Object[]{false, "Las fechas de inicio y fin no pueden ser nulas."};
        }
        if (fecha_inicio.after(fecha_fin)) {
            return new Object[]{false, "La fecha de inicio no puede ser posterior a la fecha de fin."};
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
        double costo_total = Double.parseDouble((String) servicioExists[3]); // Asignar el monto del servicio como costo total
        double monto_saldo = costo_total - monto_pagado; // Calcular el saldo
        if (monto_pagado > costo_total) {
            return new Object[]{false, "El monto pagado no puede ser mayor al costo total. El costo total para este servicio es: " + costo_total};
        }
        if (monto_saldo < 0) {
            return new Object[]{false, "El monto saldo no puede ser negativo."};
        }
        String estado_pago = ""; // Inicializar el estado de pago
        if (monto_saldo == 0) {
            estado_pago = "Pagado"; // Si el saldo es cero, el estado de pago es "Pagado"
        } else {
            estado_pago = "Pendiente"; // Si hay saldo, el estado de pago es "Pendiente"
        }
        DATA = new DContrato(cliente_id, servicio_id, descripcion, costo_total, estado, fecha_inicio, fecha_fin, estado_pago, monto_pagado, monto_saldo);
        Object[] response = DATA.guardar();
        System.out.println(Arrays.toString(response));
        DATA.desconectar();
        return response;
    }

    public Object[] modificar(int id, String descripcion, double costo_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin, String estado_pago, double monto_pagado, double monto_saldo) throws SQLException, ParseException {
        if (id <= 0 || descripcion == null || descripcion.isEmpty() || costo_total < 0 || estado == null || estado.isEmpty()) {
            return new Object[]{false, "Los campos id, descripcion, costo_total y estado no pueden ser nulos o vacíos."};
        }
        // Verificar si el id existe en la base de datos
        String[] contratoExists = ver(id);
        if (contratoExists == null || contratoExists.length == 0) {
            return new Object[]{false, "El id no existe: " + id + ". Consulte la lista de contratos."+ Help.CONTRATOS+"_LIS[]"};
        }
        DATA = new DContrato(0, 0, descripcion, costo_total, estado, fecha_inicio, fecha_fin, estado_pago, monto_pagado, monto_saldo);
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
