/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import UTILS.ConstGlobal;
import UTILS.ConstPSQL;
import CONNECTION.SQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fpl
 */
public class DContrato extends BaseDAO<DContrato> {
    private int id;
    private int cliente_id;
    private int servicio_id;
    private String descripcion;
    private double precio_total;
    private String estado;
    private Timestamp fecha_inicio;
    private Timestamp fecha_fin;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCliente_id() {
        return cliente_id;
    }
    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }
    public int getServicio_id() {
        return servicio_id;
    }
    public void setServicio_id(int servicio_id) {
        this.servicio_id = servicio_id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getPrecio_total() {
        return precio_total;
    }
    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
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
    public Timestamp getFecha_inicio() {
        return fecha_inicio;
    }
    public void setFecha_inicio(Timestamp fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    public Timestamp getFecha_fin() {
        return fecha_fin;
    }
    public void setFecha_fin(Timestamp fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
    public LocalDateTime getUpdated_at() {return updated_at;}
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}
    private static final String TABLE = "contratos";
    private static final String QUERY_ID = "id";
    private static final String QUERY_INSERT = String.format("INSERT INTO %s (descripcion, precio_total, estado, fecha_inicio, fecha_fin, created_at, cliente_id, servicio_id) VALUES (?,?,?,?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format("UPDATE %s SET descripcion=?, precio_total=?, estado=?, fecha_inicio=?, fecha_fin=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_LIST_ALL = "SELECT * FROM " + TABLE;
    public DContrato() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.fecha_inicio = new Timestamp(System.currentTimeMillis());
        this.fecha_fin = new Timestamp(System.currentTimeMillis());
    }

    public DContrato(int cliente_id, int servicio_id, String descripcion, double precio_total, String estado, Timestamp fecha_inicio, Timestamp fecha_fin) {
        super(TABLE);
        this.cliente_id = cliente_id;
        this.servicio_id = servicio_id;
        this.descripcion = descripcion;
        this.precio_total = precio_total;
        this.estado = estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
    @Override
    protected String[] entityToStringArray(DContrato entity) {
        String createdAtStr = (entity.getCreated_at() != null) ? entity.getCreated_at().toString() : "";
        String updatedAtStr = (entity.getUpdated_at() != null) ? entity.getUpdated_at().toString() : "";
        return new String[]{
                String.valueOf(entity.getId()),
                entity.getDescripcion(),
                String.valueOf(entity.getPrecio_total()),
                entity.getEstado(),
                entity.getFecha_inicio().toString(),
                entity.getFecha_fin().toString(),
                createdAtStr,
                updatedAtStr,
                String.valueOf(entity.getCliente_id()),
                String.valueOf(entity.getServicio_id())
        };
    }
    @Override
    protected DContrato stringArrayToEntity(String[] data) {
        DContrato modelo = new DContrato();
        try {
            modelo.setId(Integer.parseInt(data[0]));
            modelo.setDescripcion(data[1]);
            modelo.setPrecio_total(Double.parseDouble(data[2]));
            modelo.setEstado(data[3]);
            modelo.setFecha_inicio(Timestamp.valueOf(data[4]));
            modelo.setFecha_fin(Timestamp.valueOf(data[5]));
            modelo.setCreated_at(toLocalDateTime(data[6]));
            modelo.setUpdated_at(toLocalDateTime(data[7]));
            modelo.setCliente_id(Integer.parseInt(data[8]));
            modelo.setServicio_id(Integer.parseInt(data[9]));
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
    protected void prepareStatementForEntity(DContrato entity) throws SQLException {
        if (ps == null) {
            return;
        }
        // Para INSERT y UPDATE
        ps.setString(1, entity.getDescripcion());
        ps.setString(2, String.valueOf(entity.getPrecio_total()));
        ps.setString(3, entity.getEstado());
        ps.setTimestamp(4, entity.getFecha_inicio());
        ps.setTimestamp(5, entity.getFecha_fin());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() == 7) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(6, toTimestamp(entity.getUpdated_at()));
            ps.setInt(7, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(6, toTimestamp(entity.getCreated_at()));
            ps.setInt(7, entity.getCliente_id());
            ps.setInt(8, entity.getServicio_id());
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
        DContrato modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public String[] existe() throws SQLException {
        DContrato modelo = findById(this.id);
        return modelo != null ? entityToStringArray(modelo) : null;
    }
    public List<String[]> listar() throws SQLException {
        List<DContrato> modelos = findAll();
        List<String[]> result = new ArrayList<>();
        for (DContrato modelo : modelos) {
            result.add(entityToStringArray(modelo));
        }
        return result;
    }
}
