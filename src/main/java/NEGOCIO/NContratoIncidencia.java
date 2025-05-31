package NEGOCIO;

import DATA.DContratoIncidencias;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class NContratoIncidencia {
    private DContratoIncidencias DATA;
    public NContratoIncidencia() {
        DATA = new DContratoIncidencias();
    }
    public Object[] guardar(int contrato_id, int incidencia_id, String estado, LocalDateTime fecha_solucion) throws Exception {
        DATA = new DContratoIncidencias(contrato_id, incidencia_id, estado, fecha_solucion);
        Object[] response = DATA.guardar();
        DATA.desconectar();
        return response;
    }
    public Object[] modificar(int id, String estado, LocalDateTime fecha_solucion) throws Exception {
        DATA = new DContratoIncidencias(0, 0, estado, fecha_solucion);
        DATA.setId(id);
        Object[] response = DATA.modificar();
        DATA.desconectar();
        return response;
    }
    public Object[] eliminar(int id) throws Exception {
        DATA.setId(id);
        Object[] response = DATA.eliminar();
        DATA.desconectar();
        return response;
    }
    public String[] ver(int id) throws Exception {
        DATA.setId(id);
        String[] data = DATA.ver();
        DATA.desconectar();
        return data;
    }
    public List<String[]> listar() throws Exception {
        List<String[]> lista = DATA.listar();
        DATA.desconectar();
        return lista;
    }

}
