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
    private double costo_total;
    private String estado;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;
    private String estado_pago;
    private double monto_pagado;
    private double monto_saldo;
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
    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }
    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }
    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
    public LocalDateTime getUpdated_at() {return updated_at;}
    public void setUpdated_at(LocalDateTime updated_at) {this.updated_at = updated_at;}

    public double getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(double costo_total) {
        this.costo_total = costo_total;
    }

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }

    public double getMonto_pagado() {
        return monto_pagado;
    }

    public void setMonto_pagado(double monto_pagado) {
        this.monto_pagado = monto_pagado;
    }

    public double getMonto_saldo() {
        return monto_saldo;
    }

    public void setMonto_saldo(double monto_saldo) {
        this.monto_saldo = monto_saldo;
    }

    private static final String TABLE = "contratos";
    private static final String QUERY_ID = "id";
    private static final String QUERY_INSERT = String.format("INSERT INTO %s (descripcion, costo_total, estado, fecha_inicio, fecha_fin, estado_pago, monto_pagado, monto_saldo, created_at, cliente_id, servicio_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)", TABLE);
    private static final String QUERY_UPDATE = String.format("UPDATE %s SET descripcion=?, costo_total=?, estado=?, fecha_inicio=?, fecha_fin=?, estado_pago=?, monto_pagado=?, monto_saldo=?, updated_at=? WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_DELETE = String.format("DELETE FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_FIND_BY_ID = String.format("SELECT * FROM %s WHERE %s=?", TABLE, QUERY_ID);
    private static final String QUERY_LIST_ALL = "SELECT * FROM " + TABLE;
    public DContrato() {
        super(TABLE);
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public DContrato(int cliente_id, int servicio_id, String descripcion, double precio_total, String estado, LocalDateTime fecha_inicio, LocalDateTime fecha_fin, String estado_pago, double monto_pagado, double monto_saldo) {
        super(TABLE);
        this.cliente_id = cliente_id;
        this.servicio_id = servicio_id;
        this.descripcion = descripcion;
        this.costo_total = precio_total;
        this.estado = estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado_pago = estado_pago;
        this.monto_pagado = monto_pagado;
        this.monto_saldo = monto_saldo;
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
                String.valueOf(entity.getCosto_total()),
                entity.getEstado(),
                entity.getFecha_inicio().toString(),
                entity.getFecha_fin().toString(),
                entity.getEstado_pago(),
                String.valueOf(entity.getMonto_pagado()),
                String.valueOf(entity.getMonto_saldo()),
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
            modelo.setCosto_total(Double.parseDouble(data[2]));
            modelo.setEstado(data[3]);
            modelo.setFecha_inicio(Timestamp.valueOf(data[4]).toLocalDateTime());
            modelo.setFecha_fin(Timestamp.valueOf(data[5]).toLocalDateTime());
            modelo.setEstado_pago(data[6]);
            modelo.setMonto_pagado(Double.parseDouble(data[7]));
            modelo.setMonto_saldo(Double.parseDouble(data[8]));
            modelo.setCreated_at(toLocalDateTime(data[9]));
            modelo.setUpdated_at(toLocalDateTime(data[10]));
            modelo.setCliente_id(Integer.parseInt(data[11]));
            modelo.setServicio_id(Integer.parseInt(data[12]));
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
        ps.setDouble(2, entity.getCosto_total());
        ps.setString(3, entity.getEstado());
        ps.setTimestamp(4, toTimestamp(entity.getFecha_inicio()));
        ps.setTimestamp(5, toTimestamp(entity.getFecha_fin()));
        ps.setString(6, entity.getEstado_pago());
        ps.setDouble(7, entity.getMonto_pagado());
        ps.setDouble(8, entity.getMonto_saldo());
        // Para UPDATE, el último parámetro es el ID
        if (ps.getParameterMetaData().getParameterCount() == 10) {
            entity.setUpdated_at(LocalDateTime.now());
            ps.setTimestamp(9, toTimestamp(entity.getUpdated_at()));
            ps.setInt(10, entity.getId());
        } else {
            // Para INSERT, el último parámetro es created_at
            ps.setTimestamp(9, toTimestamp(entity.getCreated_at()));
            ps.setInt(10, entity.getCliente_id());
            ps.setInt(11, entity.getServicio_id());
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
