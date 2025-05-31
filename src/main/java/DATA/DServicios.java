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
public class DServicios extends BaseDAO<DServicios> {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String frecuencia;
    private String estado;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getFrecuencia() {
        return frecuencia;
    }
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
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

    private static final String TABLE = "servicios";
    private static final String QUERY_ID = "id";
    private static final String QUERY_INSERT = String.format(
            "INSERT INTO %s (nombre, descripcion, precio, frecuencia, estado, created_at) VALUES (?,?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format(
            "UPDATE %s SET nombre=?, descripcion=?, precio=?, frecuencia=?, estado=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_LIST_ALL = "SELECT * FROM " + TABLE;
    public DServicios() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
    }

    public DServicios(String nombre, String descripcion, double precio, String frecuencia, String estado) {
        super(TABLE);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.frecuencia = frecuencia;
        this.estado = estado;
        this.created_at = LocalDateTime.now();
    }
    @Override
    protected String[] entityToStringArray(DServicios entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getNombre(),
                entity.getDescripcion(),
                String.valueOf(entity.getPrecio()),
                entity.getFrecuencia(),
                entity.getEstado(),
                createdAtStr,
                updatedAtStr
        };
    }
    @Override
    protected DServicios stringArrayToEntity(String[] data) {
        DServicios modelo = new DServicios();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setNombre(data[1]);
            modelo.setDescripcion(data[2]);
            modelo.setPrecio(Double.parseDouble(data[3]));
            modelo.setFrecuencia(data[4]);
            modelo.setEstado(data[5]);
            modelo.setCreated_at(toLocalDateTime(data[6]));
            modelo.setUpdated_at(toLocalDateTime(data[7]));
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
    protected void prepareStatementForEntity(DServicios entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getNombre());
        ps.setString(2, entity.getDescripcion());
        ps.setDouble(3, entity.getPrecio());
        ps.setString(4, entity.getFrecuencia());
        ps.setString(5, entity.getEstado());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() > 5) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(6, toTimestamp(entity.getUpdated_at()));
            ps.setInt(7, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(6, toTimestamp(entity.getCreated_at()));
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
        DServicios modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public String[] existe() throws SQLException {
        DServicios modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public List<String[]> listar() throws SQLException {
        List<DServicios> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DServicios modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}
