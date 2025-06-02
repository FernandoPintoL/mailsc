package NEGOCIO;

import DATA.DContratoIncidencias;
import UTILS.Help;

import java.time.LocalDateTime;
import java.util.List;

public class NContratoIncidencia {
    private DContratoIncidencias model;
    private final NContrato contrato = new NContrato();
    private final NIncidencia incidencia = new NIncidencia();
    public NContratoIncidencia() {
        model = new DContratoIncidencias();
    }
    public Object[] guardar(int contrato_id, int incidencia_id, String estado, String descripcion,LocalDateTime fecha_solucion) throws Exception {
        if (contrato_id <= 0 || incidencia_id <= 0) {
            return new Object[]{false, "El contrato_id y la incidencia_id deben ser mayores a 0."};
        }
        if (estado == null || estado.isEmpty()) {
            return new Object[]{false, "El estado no puede ser nulo o vacío."};
        }
        if (descripcion == null || descripcion.isEmpty()) {
            return new Object[]{false, "La descripción no puede ser nula o vacía."};
        }
        if (fecha_solucion == null) {
            return new Object[]{false, "La fecha de solución no puede ser nula."};
        }
        if (fecha_solucion.isBefore(LocalDateTime.now())) {
            return new Object[]{false, "La fecha de solución no puede ser anterior a la fecha actual."};
        }
        // verficar si el contrato_id y incidencia_id existen en la base de datos
        Object[] contratoExists = contrato.ver(contrato_id);
        if (contratoExists == null || contratoExists.length == 0) {
            return new Object[]{false, "El contrato_id no existe : " + contrato_id + " Consulte la lista de contratos con "+ Help.CONTRATOS +"_LIS[]"};
        }
        // verificar si la incidencia_id existe en la base de datos
        Object[] incidenciaExists = incidencia.ver(incidencia_id);
        if (incidenciaExists == null || incidenciaExists.length == 0) {
            return new Object[]{false, "La incidencia_id no existe : " + incidencia_id+ " Consulte la lista de incidencias con "+Help.INCIDENCIAS+"_LIS[]"};
        }
        model = new DContratoIncidencias(contrato_id, incidencia_id, estado, descripcion, fecha_solucion);
        Object[] response = model.guardar();
        model.desconectar();
        return response;
    }
    public Object[] modificar(int id, String estado, String descripcion, LocalDateTime fecha_solucion) throws Exception {
        if (id <= 0) {
            return new Object[]{false, "El id debe ser mayor a 0."};
        }
        if (estado == null || estado.isEmpty()) {
            return new Object[]{false, "El estado no puede ser nulo o vacío."};
        }
        if (descripcion == null || descripcion.isEmpty()) {
            return new Object[]{false, "La descripción no puede ser nula o vacía."};
        }
        if (fecha_solucion == null) {
            return new Object[]{false, "La fecha de solución no puede ser nula."};
        }
        if (fecha_solucion.isBefore(LocalDateTime.now())) {
            return new Object[]{false, "La fecha de solución no puede ser anterior a la fecha actual."};
        }
        // verificar si el id existe en la base de datos
        String[] existingData = ver(id);
        if (existingData == null || existingData.length == 0) {
            return new Object[]{false, "El id no existe : " + id + " Consulte la lista de incidencias con " + Help.INCIDENCIAS + "_LIS[]"};
        }
        model = new DContratoIncidencias(0, 0, estado, descripcion, fecha_solucion);
        model.setId(id);
        Object[] response = model.modificar();
        model.desconectar();
        return response;
    }
    public Object[] eliminar(int id) throws Exception {
        model.setId(id);
        Object[] response = model.eliminar();
        model.desconectar();
        return response;
    }
    public String[] ver(int id) throws Exception {
        model.setId(id);
        String[] data = model.ver();
        model.desconectar();
        return data;
    }
    public List<String[]> listar() throws Exception {
        List<String[]> lista = model.listar();
        model.desconectar();
        return lista;
    }

}
