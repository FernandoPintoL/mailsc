/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpl
 */
public class DIncidencia extends BaseDAO<DIncidencia> {
    
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getUpdated_at() {return updated_at;}
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    private static final String TABLE = "incidencias";
    private static final String QUERY_ID = "id";
    private static final String QUERY_INSERT = String.format(
            "INSERT INTO %s ( nombre, descripcion, , created_at) VALUES (?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET nombre=?, descripcion=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM "+TABLE+" WHERE id=?";
    private static final String QUERY_LIST_ALL = "SELECT * FROM "+TABLE;
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    public DIncidencia() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
    }
    public DIncidencia(String nombre, String descripcion) {
        super(TABLE);
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.created_at = LocalDateTime.now();
    }
    @Override
    protected String[] entityToStringArray(DIncidencia entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getNombre(),
                entity.getDescripcion(),
                createdAtStr,
                updatedAtStr
        };
    }
    @Override
    protected DIncidencia stringArrayToEntity(String[] data) {
        DIncidencia modelo = new DIncidencia();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setNombre(data[1]);
            modelo.setDescripcion(data[2]);
            modelo.setCreated_at(toLocalDateTime(data[3]));
            modelo.setUpdated_at(toLocalDateTime(data[4]));
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
    protected void prepareStatementForEntity(DIncidencia entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getNombre());
        ps.setString(2, entity.getDescripcion());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() > 3) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(3, toTimestamp(entity.getUpdated_at()));
            ps.setInt(4, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(3, toTimestamp(entity.getCreated_at()));
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
        DIncidencia modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public String[] existe() throws SQLException {
        DIncidencia modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public List<String[]> listar() throws SQLException {
        List<DIncidencia> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DIncidencia modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}
