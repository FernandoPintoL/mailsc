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
import java.util.List;

/**
 *
 * @author fpl
 */
public class DReporte {

    public DReporte() {
    }
    private final String QUERY_LIST_PRODUCTO_ALMACEN = """
                                                        SELECT 
                                                            p.id_producto AS id_producto,
                                                            p.nombre AS producto_nombre,
                                                            a.id_almacen AS id_almacen,
                                                            a.codigo AS codigo,
                                                            i.stock
                                                        FROM 
                                                            producto p
                                                        JOIN 
                                                            inventario i ON p.id_producto = i.producto_id
                                                        JOIN 
                                                            almacen a ON i.almacen_id = a.id_almacen
                                                        ORDER BY 
                                                            p.id_producto, a.id_almacen;
                                                       """;

    private final String QUERY_PRODUCTO_VENDIDO = """
                                                        SELECT 
                                                            p.id_producto AS id_producto,
                                                            p.nombre AS producto_nombre,
                                                            p.precio AS precio_unitario,
                                                            SUM(dc.cantidad) AS cantidad_vendida,
                                                            SUM(dc.subtotal) AS total_vendido
                                                        FROM 
                                                            producto p
                                                        JOIN 
                                                            detallecompra dc ON p.id_producto = dc.producto_id
                                                        JOIN 
                                                            compra c ON dc.compra_id = c.id_compra
                                                        GROUP BY 
                                                            p.id_producto, p.nombre, p.precio
                                                        ORDER BY 
                                                            total_vendido DESC;
                                                       """;
    private SQLConnection connection;
    private PreparedStatement ps;
    private ResultSet set;

    private void init_conexion() {
        connection = new SQLConnection(
                ConstPSQL.user,
                ConstPSQL.pass,
                ConstGlobal.SERVIDOR,
                ConstGlobal.PORT_DB,
                ConstPSQL.dbName);
    }

    public List<String[]> listarProductoAlmacen() throws SQLException {
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_LIST_PRODUCTO_ALMACEN);
            set = ps.executeQuery();
            while (set.next()) {
                datas.add(new String[]{
                    String.valueOf(set.getInt("id_producto")),
                    String.valueOf(set.getInt("id_almacen")),
                    String.valueOf(set.getInt("codigo")),
                    String.valueOf(set.getString("producto_nombre")),
                    String.valueOf(set.getDouble("stock"))
                });
            }
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println("ERRPR LISTAR PRODUCTOS ALMACEN");
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
                e.printStackTrace(); // Imprimir cualquier error al cerrar recursos
            }
        }
        return datas;
    }

    public List<String[]> listarProductoVendido() throws SQLException {
        List<String[]> datas = new ArrayList<>();
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_PRODUCTO_VENDIDO);
            set = ps.executeQuery();
            while (set.next()) {
                datas.add(new String[]{
                    String.valueOf(set.getInt("id_producto")),
                    String.valueOf(set.getString("producto_nombre")),
                    String.valueOf(set.getDouble("precio_unitario")),
                    String.valueOf(set.getDouble("cantidad_vendido")),
                    String.valueOf(set.getDouble("total_vendido"))
                });
            }
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println("ERRPR LISTAR PRODUCTOS VENDIDOS");
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
                e.printStackTrace(); // Imprimir cualquier error al cerrar recursos
            }
        }
        return datas;
    }

    /*public ResultSet productoVendido() throws SQLException {
        //List<String[]> datas = new ArrayList<>();
        ResultSet data;
        try {
            init_conexion();
            ps = connection.connect().prepareStatement(QUERY_PRODUCTO_VENDIDO);
            set = ps.executeQuery();
            data = set;
            while (set.next()) {
                datas.add(new String[]{
                    String.valueOf(set.getInt("id_producto")),
                    String.valueOf(set.getString("producto_nombre")),
                    String.valueOf(set.getDouble("total_vendido"))
                });
            }
        } catch (SQLException e) {
            // Imprimir el error en consola con detalles
            System.out.println("ERRPR LISTAR PRODUCTOS VENDIDOS");
            System.out.println("Mensaje: " + e.getMessage()); // Mensaje del error SQL
            System.out.println("Estado SQL: " + e.getSQLState()); // Estado SQL asociado al error
            System.out.println("Código de error: " + e.getErrorCode()); // Código de error del proveedor
            e.printStackTrace(); // Esto imprime toda la traza del error, útil para depurar
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            try {
                if (set != null) {
                    set.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
                e.printStackTrace(); // Imprimir cualquier error al cerrar recursos
            }
        }
        return set;
    }*/

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }

}
