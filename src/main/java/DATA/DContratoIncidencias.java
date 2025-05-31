package DATA;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DContratoIncidencias extends BaseDAO<DContratoIncidencias> {
    private int id;
    private int contrato_id;
    private int incidencia_id;
    private String estado;
    private LocalDateTime fecha_solucion;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private static final String TABLE = "contrato_incidencias";
    private static final String QUERY_ID = "id";
    //private final String Q_CI = "ci";
    private static final String QUERY_INSERT = String.format(
            "INSERT INTO %s (estado, fecha_solucion, created_at, contrato_id, incidencia_id) VALUES (?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET estado=?, fecha_solucion=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_LIST_ALL = "SELECT * FROM " + TABLE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContrato_id() {
        return contrato_id;
    }

    public void setContrato_id(int contrato_id) {
        this.contrato_id = contrato_id;
    }

    public int getIncidencia_id() {
        return incidencia_id;
    }

    public void setIncidencia_id(int incidencia_id) {
        this.incidencia_id = incidencia_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_solucion() {
        return fecha_solucion;
    }

    public void setFecha_solucion(LocalDateTime fecha_solucion) {
        this.fecha_solucion = fecha_solucion;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public DContratoIncidencias() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
    }
    public DContratoIncidencias(int contrato_id, int incidencia_id, String estado, LocalDateTime fecha_solucion) {
        super(TABLE);
        this.contrato_id = contrato_id;
        this.incidencia_id = incidencia_id;
        this.estado = estado;
        this.fecha_solucion = fecha_solucion;
        this.created_at = LocalDateTime.now();
    }

    @Override
    protected String[] entityToStringArray(DContratoIncidencias entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getEstado(),
                (entity.getFecha_solucion() != null) ? entity.getFecha_solucion().toString() : "",
                createdAtStr,
                updatedAtStr,
                String.valueOf(entity.getContrato_id()),
                String.valueOf(entity.getIncidencia_id()),
        };
    }
    @Override
    protected DContratoIncidencias stringArrayToEntity(String[] data) {
        DContratoIncidencias modelo = new DContratoIncidencias();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setEstado(data[1]);
            modelo.setFecha_solucion(toLocalDateTime(data[2]));
            modelo.setCreated_at(toLocalDateTime(data[3]));
            modelo.setUpdated_at(toLocalDateTime(data[4]));
            modelo.setContrato_id(Integer.parseInt(data[5]));
            modelo.setIncidencia_id(Integer.parseInt(data[6]));
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir datos del modelo: " + e.getMessage());
        }
        return modelo;
    }
    @Override
    protected String getInsertQuery() {
        return QUERY_INSERT;
    }
    @Override
    protected String getUpdateQuery() {
        return QUERY_UPDATE;
    }
    @Override
    protected String getDeleteQuery() {
        return QUERY_DELETE;
    }
    @Override
    protected String getFindByIdQuery() {
        return QUERY_FIND_BY_ID;
    }
    @Override
    protected String getListAllQuery() {
        return QUERY_LIST_ALL;
    }
    @Override
    protected void prepareStatementForEntity(DContratoIncidencias entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getEstado());
        ps.setTimestamp(2, toTimestamp(entity.getFecha_solucion()));
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() == 4) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(3, toTimestamp(entity.getUpdated_at()));
            ps.setInt(4, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(3, toTimestamp(entity.getCreated_at()));
            ps.setString(4, String.valueOf(entity.getContrato_id()));
            ps.setString(5, String.valueOf(entity.getIncidencia_id()));
        }
    }
    @Override
    protected void prepareStatementForId(int id) throws SQLException {
        if (ps != null) {
            ps.setInt(1, id);
        }
    }
    public Object[] guardar() throws SQLException {
        return save(this);
    }
    public Object[] modificar() throws SQLException, ParseException {
        return update(this);
    }
    public Object[] eliminar() throws SQLException {
        return delete(this.id);
    }
    public String[] ver() throws SQLException {
        DContratoIncidencias modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public String[] existe() throws SQLException {
        DContratoIncidencias modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public List<String[]> listar() throws SQLException {
        List<DContratoIncidencias> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DContratoIncidencias modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}
