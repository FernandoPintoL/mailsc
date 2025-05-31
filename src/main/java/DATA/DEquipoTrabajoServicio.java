/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpl
 */
public class DEquipoTrabajoServicio extends BaseDAO<DEquipoTrabajoServicio> {
    private int id;
    private int equipo_trabajo_id;
    private int servicio_id;
    private String estado;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEquipo_trabajo_id() {
        return equipo_trabajo_id;
    }
    public void setEquipo_trabajo_id(int equipo_trabajo_id) {
        this.equipo_trabajo_id = equipo_trabajo_id;
    }
    public int getServicio_id() {
        return servicio_id;
    }
    public void setServicio_id(int servicio_id) {
        this.servicio_id = servicio_id;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getUpdated_at() {return updated_at;}
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}
    private static final String TABLE = "equipo_servicios";
    private static final String QUERY_ID = "id";
    private static final String QUERY_INSERT = String.format(
            "INSERT INTO %s (estado, created_at, equipo_trabajo_id, servicio_id) VALUES (?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET estado=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_LIST_ALL = "SELECT * FROM " + TABLE;
    public DEquipoTrabajoServicio() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
    public DEquipoTrabajoServicio(int equipo_trabajo_id, int servicio_id, String estado) {
        super(TABLE);
        this.equipo_trabajo_id = equipo_trabajo_id;
        this.servicio_id = servicio_id;
        this.estado = estado;
        this.created_at = LocalDateTime.now();
    }
    @Override
    protected String[] entityToStringArray(DEquipoTrabajoServicio entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                String.valueOf(entity.getEquipo_trabajo_id()),
                String.valueOf(entity.getServicio_id()),
                entity.getEstado(),
                createdAtStr,
                updatedAtStr
        };
    }
    @Override
    protected DEquipoTrabajoServicio stringArrayToEntity(String[] data) {
        DEquipoTrabajoServicio modelo = new DEquipoTrabajoServicio();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setEquipo_trabajo_id(Integer.parseInt(data[1]));
            modelo.setServicio_id(Integer.parseInt(data[2]));
            modelo.setEstado(data[3]);
            modelo.setCreated_at(toLocalDateTime(data[4]));
            modelo.setUpdated_at(toLocalDateTime(data[5]));
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
    protected void prepareStatementForEntity(DEquipoTrabajoServicio entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getEstado());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() == 3) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(2, toTimestamp(entity.getUpdated_at()));
            ps.setInt(3, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(2, toTimestamp(entity.getCreated_at()));
            ps.setString(3, String.valueOf(entity.getEquipo_trabajo_id()));
            ps.setString(4, String.valueOf(entity.getServicio_id()));
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
        DEquipoTrabajoServicio modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public String[] existe() throws SQLException {
        DEquipoTrabajoServicio modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public List<String[]> listar() throws SQLException {
        List<DEquipoTrabajoServicio> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DEquipoTrabajoServicio modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}