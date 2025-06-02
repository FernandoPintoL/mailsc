/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fpl
 */
public class DReporte extends DataBaseHelper {
    public DReporte() {
    }

    // Consulta mejorada para contratos con información detallada
    public final String QUERY_CONTRATO = """
            SELECT 
                c.id AS id_contrato,
                c.descripcion AS descripcion_contrato,
                c.fecha_inicio AS fecha_inicio,
                c.fecha_fin AS fecha_fin,
                c.costo_total AS contrato_monto,
                c.estado AS contrato_estado,
                c.estado_pago AS estado_pago,
                c.monto_pagado AS monto_pagado,
                c.monto_saldo AS monto_saldo,
                cl.nombre AS nombre_cliente,
                cl.ci AS ci_cliente,
                cl.telefono AS telefono_cliente,
                cl.direccion AS direccion_cliente,
                cl.tipo_cliente AS tipo_cliente,
                s.nombre AS nombre_servicio,
                s.descripcion AS descripcion_servicio,
                s.precio AS precio_servicio,
                s.frecuencia AS frecuencia_servicio,
                s.estado AS estado_servicio,
                et.nombre AS nombre_equipo_trabajo
            FROM
                contratos c
            JOIN
                clientes cl ON c.cliente_id = cl.id
            JOIN
                servicios s ON c.servicio_id = s.id
            LEFT JOIN
                equipo_trabajo_servicios ets ON s.id = ets.servicio_id
            LEFT JOIN
                equipo_trabajos et ON ets.equipo_trabajo_id = et.id
            LEFT JOIN
                empleado_equipo_trabajos eet ON et.id = eet.equipo_trabajo_id
            WHERE
                c.id = ?;
            """;

    // Consulta para inventario de productos con detalles
    public final String QUERY_INVENTARIO_PRODUCTOS = """
            SELECT 
                p.id AS id_producto,
                p.nombre AS nombre_producto,
                p.descripcion AS descripcion_producto,
                p.precio AS precio_producto,
                p.stock AS stock_producto,
                p.created_at AS fecha_creacion,
                p.updated_at AS fecha_actualizacion,
                ps.id AS id_producto_servicio,
                ps.cantidad AS cantidad_usada,
                s.nombre AS nombre_servicio,
                s.descripcion AS descripcion_servicio
            FROM
                productos p
            LEFT JOIN
                producto_servicios ps ON p.id = ps.producto_id
            LEFT JOIN
                servicios s ON ps.servicio_id = s.id
            ORDER BY
                p.nombre, s.nombre;
            """;

    // Consulta para servicios con estadísticas
    public final String QUERY_SERVICIOS_ESTADISTICAS = """
            SELECT 
                s.id AS id_servicio,
                s.nombre AS nombre_servicio,
                s.descripcion AS descripcion_servicio,
                s.precio AS precio_servicio,
                s.frecuencia AS frecuencia_servicio,
                s.estado AS estado_servicio,
                COUNT(c.id) AS total_contratos,
                SUM(c.costo_total) AS ingresos_totales,
                COUNT(DISTINCT cl.id) AS total_clientes
            FROM
                servicios s
            LEFT JOIN
                contratos c ON s.id = c.servicio_id
            LEFT JOIN
                clientes cl ON c.cliente_id = cl.id
            GROUP BY
                s.id, s.nombre, s.descripcion, s.precio, s.frecuencia, s.estado
            ORDER BY
                total_contratos DESC;
            """;

    // Consulta para incidencias por contrato
    public final String QUERY_INCIDENCIAS_CONTRATO = """
            SELECT 
                c.id AS id_contrato,
                c.descripcion AS descripcion_contrato,
                c.fecha_inicio AS fecha_inicio_contrato,
                c.fecha_fin AS fecha_fin_contrato,
                c.estado AS estado_contrato,
                cl.nombre AS nombre_cliente,
                i.id AS id_incidencia,
                i.descripcion AS descripcion_incidencia,
                i.tipo AS tipo_incidencia,
                i.prioridad AS prioridad_incidencia,
                ci.estado AS estado_incidencia,
                ci.descripcion AS descripcion_solucion,
                ci.fecha_solucion AS fecha_solucion
            FROM
                contratos c
            JOIN
                clientes cl ON c.cliente_id = cl.id
            JOIN
                contrato_incidencias ci ON c.id = ci.contrato_id
            JOIN
                incidencias i ON ci.incidencia_id = i.id
            WHERE
                c.id = ?
            ORDER BY
                i.prioridad DESC, ci.fecha_solucion;
            """;

    // Consulta para empleados por equipo de trabajo
    public final String QUERY_EMPLEADOS_EQUIPO = """
            SELECT 
                et.id AS id_equipo,
                et.nombre AS nombre_equipo,
                et.descripcion AS descripcion_equipo,
                e.id AS id_empleado,
                e.nombre AS nombre_empleado,
                e.ci AS ci_empleado,
                e.telefono AS telefono_empleado,
                e.puesto AS cargo_empleado,
                COUNT(DISTINCT s.id) AS total_servicios,
                COUNT(DISTINCT c.id) AS total_contratos
            FROM
                equipo_trabajos et
            JOIN
                empleado_equipo_trabajos eet ON et.id = eet.equipo_trabajo_id
            JOIN
                empleados e ON eet.empleado_id = e.id
            LEFT JOIN
                equipo_trabajo_servicios ets ON et.id = ets.equipo_trabajo_id
            LEFT JOIN
                servicios s ON ets.servicio_id = s.id
            LEFT JOIN
                contratos c ON s.id = c.servicio_id
            WHERE
                et.id = ?
            GROUP BY
                et.id, et.nombre, et.descripcion, e.id, e.nombre, e.ci, e.telefono, e.puesto
            ORDER BY
                e.puesto, e.nombre;
            """;

    // Metodo para obtener información detallada de un contrato
    public String[] contrato(int id) throws SQLException {
        String[] data = null;
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.prepareStatement(QUERY_CONTRATO);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                data = new String[]{
                        "N° Contrato: " + resultSet.getInt("id_contrato"),
                        "Descripción Contrato: " + resultSet.getString("descripcion_contrato"),
                        "Fecha de Inicio: " + resultSet.getDate("fecha_inicio"),
                        "Fecha Fin: " + resultSet.getDate("fecha_fin"),
                        "Costo Total: " + resultSet.getDouble("contrato_monto"),
                        "Estado del Contrato: " + resultSet.getString("contrato_estado"),
                        "Estado del Pago: " + resultSet.getString("estado_pago"),
                        "Monto de Pagado: " + resultSet.getDouble("monto_pagado"),
                        "Saldo a pagar: " + resultSet.getDouble("monto_saldo"),
                        "Nombre del Cliente: " + resultSet.getString("nombre_cliente"),
                        "CI del Cliente: " + resultSet.getString("ci_cliente"),
                        "Telefono del Cliente: " + resultSet.getString("telefono_cliente"),
                        "Dirección del Cliente: " + resultSet.getString("direccion_cliente"),
                        "Tipo de Cliente: " + resultSet.getString("tipo_cliente"),
                        "Nombre del Servicio: " + resultSet.getString("nombre_servicio"),
                        "Descripcion del Servicio: " + resultSet.getString("descripcion_servicio"),
                        "Precio del Servicio: " + resultSet.getDouble("precio_servicio"),
                        "Frecuencia del Servicio: " + resultSet.getString("frecuencia_servicio"),
                        "Estado del Servicio: " + resultSet.getString("estado_servicio"),
                        "Nombre de Equipo Trabajo: " + resultSet.getString("nombre_equipo_trabajo")
                };
                System.out.println("CONSULTA DE CONTRATO");
                System.out.println(Arrays.toString(data));
                datas.add(data);
                System.out.println("longitud: " + datas.size());
            }
        } catch (SQLException e) {
            System.out.println("ERROR CONTRATO VER");
            e.printStackTrace();
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Código de estado SQL: " + e.getSQLState());
            System.out.println("Código de error del proveedor: " + e.getErrorCode());
        } finally {
            closeResources(true);
        }
        return data;
    }

    // Metodo para obtener el inventario de productos con detalles
    public ArrayList<String[]> inventarioProductos() throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.prepareStatement(QUERY_INVENTARIO_PRODUCTOS);
            resultSet = ps.executeQuery();

            // Agregar encabezados
            data.add(new String[]{
                    "ID", "NOMBRE", "DESCRIPCIÓN", "PRECIO", "STOCK", "SERVICIO ASOCIADO", "CANTIDAD USADA"
            });

            while (resultSet.next()) {
                String servicioAsociado = resultSet.getString("nombre_servicio");
                if (servicioAsociado == null) servicioAsociado = "No asociado";

                String cantidadUsada = resultSet.getString("cantidad_usada");
                if (cantidadUsada == null) cantidadUsada = "0";

                data.add(new String[]{
                        String.valueOf(resultSet.getInt("id_producto")),
                        resultSet.getString("nombre_producto"),
                        resultSet.getString("descripcion_producto"),
                        String.valueOf(resultSet.getDouble("precio_producto")),
                        String.valueOf(resultSet.getDouble("stock_producto")),
                        servicioAsociado,
                        cantidadUsada
                });
            }
        } catch (SQLException e) {
            System.out.println("ERROR INVENTARIO PRODUCTOS");
            e.printStackTrace();
            System.out.println("Mensaje de error: " + e.getMessage());
        } finally {
            closeResources(true);
        }
        return data;
    }

    // Metodo para obtener estadísticas de servicios
    public ArrayList<String[]> serviciosEstadisticas() throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.prepareStatement(QUERY_SERVICIOS_ESTADISTICAS);
            resultSet = ps.executeQuery();

            // Agregar encabezados
            data.add(new String[]{
                    "ID", "NOMBRE", "DESCRIPCIÓN", "PRECIO", "FRECUENCIA", "ESTADO",
                    "TOTAL CONTRATOS", "INGRESOS TOTALES", "TOTAL CLIENTES"
            });

            while (resultSet.next()) {
                data.add(new String[]{
                        String.valueOf(resultSet.getInt("id_servicio")),
                        resultSet.getString("nombre_servicio"),
                        resultSet.getString("descripcion_servicio"),
                        String.valueOf(resultSet.getDouble("precio_servicio")),
                        resultSet.getString("frecuencia_servicio"),
                        resultSet.getString("estado_servicio"),
                        String.valueOf(resultSet.getInt("total_contratos")),
                        String.valueOf(resultSet.getDouble("ingresos_totales")),
                        String.valueOf(resultSet.getInt("total_clientes"))
                });
            }
        } catch (SQLException e) {
            System.out.println("ERROR SERVICIOS ESTADISTICAS");
            e.printStackTrace();
            System.out.println("Mensaje de error: " + e.getMessage());
        } finally {
            closeResources(true);
        }
        return data;
    }

    // Metodo para obtener incidencias por contrato
    public ArrayList<String[]> incidenciasContrato(int contratoId) throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.prepareStatement(QUERY_INCIDENCIAS_CONTRATO);
            ps.setInt(1, contratoId);
            resultSet = ps.executeQuery();

            // Agregar encabezados
            data.add(new String[]{
                    "ID INCIDENCIA", "DESCRIPCIÓN", "TIPO", "PRIORIDAD", "ESTADO",
                    "SOLUCIÓN", "FECHA SOLUCIÓN"
            });

            while (resultSet.next()) {
                data.add(new String[]{
                        String.valueOf(resultSet.getInt("id_incidencia")),
                        resultSet.getString("descripcion_incidencia"),
                        resultSet.getString("tipo_incidencia"),
                        resultSet.getString("prioridad_incidencia"),
                        resultSet.getString("estado_incidencia"),
                        resultSet.getString("descripcion_solucion"),
                        String.valueOf(resultSet.getObject("fecha_solucion"))
                });
            }
        } catch (SQLException e) {
            System.out.println("ERROR INCIDENCIAS CONTRATO");
            e.printStackTrace();
            System.out.println("Mensaje de error: " + e.getMessage());
        } finally {
            closeResources(true);
        }
        return data;
    }

    // Metodo para obtener empleados por equipo de trabajo
    public ArrayList<String[]> empleadosEquipo(int equipoId) throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.prepareStatement(QUERY_EMPLEADOS_EQUIPO);
            ps.setInt(1, equipoId);
            resultSet = ps.executeQuery();

            // Agregar encabezados
            data.add(new String[]{
                    "ID EMPLEADO", "NOMBRE", "CI", "TELÉFONO", "CARGO",
                    "TOTAL SERVICIOS", "TOTAL CONTRATOS"
            });

            while (resultSet.next()) {
                data.add(new String[]{
                        String.valueOf(resultSet.getInt("id_empleado")),
                        resultSet.getString("nombre_empleado"),
                        resultSet.getString("ci_empleado"),
                        resultSet.getString("telefono_empleado"),
                        resultSet.getString("cargo_empleado"),
                        String.valueOf(resultSet.getInt("total_servicios")),
                        String.valueOf(resultSet.getInt("total_contratos"))
                });
            }
        } catch (SQLException e) {
            System.out.println("ERROR EMPLEADOS EQUIPO");
            e.printStackTrace();
            System.out.println("Mensaje de error: " + e.getMessage());
        } finally {
            closeResources(true);
        }
        return data;
    }
}
