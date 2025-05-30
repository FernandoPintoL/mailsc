/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DATA;

import CONNECTION.SQLConnection;
import UTILS.ConstGlobal;
import UTILS.ConstPSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fpl
 */
public class DReporte extends DataBaseHelper{

    public DReporte() {
    }
    public final String QUERY_CONTRATO = """
                                        SELECT 
                                            c.id AS id_contrato,
                                            c.fecha_inicio AS fecha_inicio,
                                            c.fecha_fin AS fecha_fin,
                                            c.precio_total AS contrato_monto,
                                            c.estado AS contrato_estado,
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
                                            et.nombre AS nombre_equipo_trabajo,
                                            e.ci AS ci_empleado,
                                            e.nombre AS nombre_empleado
                                        FROM
                                            contratos    c
                                        JOIN
                                            clientes cl ON c.cliente_id = cl.id
                                        JOIN
                                            servicios s ON c.servicio_id = s.id
                                        JOIN
                                            equipo_trabajos et ON c.equipo_trabajo_id = et.id
                                        JOIN
                                            empleados e ON c.empleado_id = e.id    
                                        WHERE
                                            c.id = ?;
                                        """;

    public String[] contrato (int id) throws SQLException {
        String[] data = null;
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_CONTRATO);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                //data = arrayData(resultSet);
                data = new String[]{
                        "ID CONTRATO: "+ resultSet.getInt("id_contrato"),
                        "FECHA INICIO: "+ resultSet.getObject("fecha_inicio"),
                        "FECHA FIN: "+ resultSet.getObject("fecha_fin"),
                        "PRECIO: "+ resultSet.getObject("contrato_monto"),
                        "ESTADO CONTRATO: "+ resultSet.getObject("contrato_estado"),
                        "NOMBRE DEL CLIENTE: "+ resultSet.getObject("nombre_cliente"),
                        "CI CLIENTE: "+ resultSet.getObject("ci_cliente"),
                        "TELEFONO CLIENTE: "+ resultSet.getObject("telefono_cliente"),
                        "DIRECCION CLIENTE: "+ resultSet.getObject("direccion_cliente"),
                        "TIPO CLIENTE: "+ resultSet.getObject("tipo_cliente"),
                        "NOMBRE SERVICIO: "+ resultSet.getObject("nombre_servicio"),
                        "DESCRIPCION SERVICIO: "+ resultSet.getObject("descripcion_servicio"),
                        "PRECIO SERVICIO: "+ resultSet.getObject("precio_servicio"),
                        "FRECUENCIA SERVICIO: "+ resultSet.getObject("frecuencia_servicio"),
                        "ESTADO SERVICIO: "+ resultSet.getObject("estado_servicio"),
                        "NOMBRE EQUIPO TRABAJO: "+ resultSet.getObject("nombre_equipo_trabajo"),
                        "CI EMPLEADO: "+ resultSet.getObject("ci_empleado"),
                        "NOMBRE EMPLEADO: "+ resultSet.getObject("nombre_empleado"),
                };
                System.out.println("CONSULTA DE CONTRATO");
                System.out.println(Arrays.toString(data));
                datas.add(data);
                System.out.println("longitud: "+datas.size());
            }
        } catch (SQLException e) {
            // Imprimir detalles de la excepción SQLException
            System.out.println("ERROR CONTRATO VER");
            e.printStackTrace();  // Muestra la traza completa del error
            System.out.println("Mensaje de error: " + e.getMessage());  // Mensaje detallado del error
            System.out.println("Código de estado SQL: " + e.getSQLState());  // Código SQL estándar del error
            System.out.println("Código de error del proveedor: " + e.getErrorCode());  // Código de error específico del proveedor de la base de datos

        } finally {
            // Cerrar recursos para evitar fugas de memoria
            closeConnections();
        }
        return data;
    }
}
