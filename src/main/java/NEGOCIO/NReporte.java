/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NEGOCIO;

import DATA.DReporte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de negocio para la generación de reportes
 * Implementa la lógica de negocio para generar diferentes tipos de reportes
 * 
 * @author fpl
 */
public class NReporte {

    private DReporte DATA;
    private ReportGenerator reportGenerator;
    private ChartGenerator chartGenerator;

    /**
     * Constructor por defecto
     * Inicializa las dependencias necesarias
     */
    public NReporte() {
        DATA = new DReporte();
        reportGenerator = new ReportGenerator();
        chartGenerator = ChartGenerator.getInstance();
    }

    /**
     * Genera un reporte de contrato
     * @param id ID del contrato
     * @param path Ruta donde se guardará el PDF
     * @return Datos del contrato
     * @throws SQLException Si ocurre un error de SQL
     */
    public String[] contrato(int id, String path) throws SQLException {
        String[] data = DATA.contrato(id);
        if (data != null) {
            boolean success = reportGenerator.generarDocumento("DOCUMENTO DE CONTRATO", data, path);
            if (!success) {
                System.err.println("Advertencia: No se pudo generar el PDF del contrato");
            }
        } else {
            System.err.println("Error: No se encontraron datos para el contrato con ID " + id);
        }
        return data;
    }

    /**
     * Genera un reporte de inventario de productos
     * @param path Ruta donde se guardará el PDF
     * @return Datos del inventario
     * @throws SQLException Si ocurre un error de SQL
     */
    public ArrayList<String[]> inventarioProductos(String path) throws SQLException {
        ArrayList<String[]> data = DATA.inventarioProductos();
        System.out.println("Datos de inventario obtenidos: " + (data != null ? data.size() : "null"));

        if (data != null && !data.isEmpty()) {
            // Generar gráfico de torta para el inventario
            String chartPath = path.replace(".pdf", "_chart.png");
            System.out.println("Generando gráfico en: " + chartPath);

            // Usar ChartGenerator para crear el gráfico
            boolean chartSuccess = chartGenerator
                    .withDimensions(800, 600)
                    .with3D(true)
                    .createPieChart(data, chartPath, 1, 4, "Distribución de Stock por Producto");

            if (!chartSuccess) {
                System.err.println("Advertencia: No se pudo generar el gráfico de inventario");
            }

            // Generar el reporte PDF con el gráfico
            boolean pdfSuccess = reportGenerator.generatePdfReport(
                    data, 
                    path, 
                    chartSuccess ? chartPath : null, 
                    "REPORTE DE INVENTARIO DE PRODUCTOS"
            );

            if (!pdfSuccess) {
                System.err.println("Advertencia: No se pudo generar el PDF de inventario");
            }
        } else {
            System.err.println("Error: No se encontraron datos de inventario");
        }

        return data;
    }

    /**
     * Genera un reporte de estadísticas de servicios
     * @param path Ruta donde se guardará el PDF
     * @return Datos de estadísticas de servicios
     * @throws SQLException Si ocurre un error de SQL
     */
    public ArrayList<String[]> serviciosEstadisticas(String path) throws SQLException {
        ArrayList<String[]> data = DATA.serviciosEstadisticas();

        if (data != null && !data.isEmpty()) {
            // Generar el reporte PDF
            boolean success = reportGenerator.generatePdfReport(
                    data, 
                    path, 
                    null, 
                    "REPORTE DE ESTADÍSTICAS DE SERVICIOS"
            );

            if (!success) {
                System.err.println("Advertencia: No se pudo generar el PDF de estadísticas de servicios");
            }
        } else {
            System.err.println("Error: No se encontraron datos de estadísticas de servicios");
        }

        return data;
    }

    /**
     * Genera un reporte de incidencias por contrato
     * @param contratoId ID del contrato
     * @param path Ruta donde se guardará el PDF
     * @return Datos de incidencias del contrato
     * @throws SQLException Si ocurre un error de SQL
     */
    public ArrayList<String[]> incidenciasContrato(int contratoId, String path) throws SQLException {
        ArrayList<String[]> data = DATA.incidenciasContrato(contratoId);

        if (data != null && !data.isEmpty()) {
            // Obtener datos del contrato para el encabezado
            String[] contratoData = DATA.contrato(contratoId);
            String[] cabecera = new String[]{
                String.valueOf(contratoId),
                contratoData != null ? contratoData[4].split(": ")[1] : "N/A", // Precio
                contratoData != null ? contratoData[1].split(": ")[1] : "N/A", // Fecha inicio
                contratoData != null ? contratoData[5].split(": ")[1] : "N/A", // Estado
                contratoData != null ? contratoData[9].split(": ")[1] : "N/A", // Nombre cliente
                "Administrador"
            };

            // Generar el reporte PDF
            boolean success = reportGenerator.generatePdfReportCompra(cabecera, data, path);

            if (!success) {
                System.err.println("Advertencia: No se pudo generar el PDF de incidencias del contrato");
            }
        } else {
            System.err.println("Error: No se encontraron incidencias para el contrato con ID " + contratoId);
        }

        return data;
    }

    /**
     * Genera un reporte de empleados por equipo de trabajo
     * @param equipoId ID del equipo de trabajo
     * @param path Ruta donde se guardará el PDF
     * @return Datos de empleados del equipo
     * @throws SQLException Si ocurre un error de SQL
     */
    public ArrayList<String[]> empleadosEquipo(int equipoId, String path) throws SQLException {
        ArrayList<String[]> data = DATA.empleadosEquipo(equipoId);

        if (data != null && !data.isEmpty()) {
            // Generar el reporte PDF
            boolean success = reportGenerator.generatePdfReport(
                    data, 
                    path, 
                    null, 
                    "REPORTE DE EMPLEADOS - EQUIPO #" + equipoId
            );

            if (!success) {
                System.err.println("Advertencia: No se pudo generar el PDF de empleados del equipo");
            }
        } else {
            System.err.println("Error: No se encontraron empleados para el equipo con ID " + equipoId);
        }

        return data;
    }
}
