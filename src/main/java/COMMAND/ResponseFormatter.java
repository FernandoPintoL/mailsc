package COMMAND;

import OBJECT.Mensaje;
import UTILS.Help;
import java.util.List;

/**
 * Class for formatting responses in a centralized way
 */
public class ResponseFormatter {
    
    /**
     * Format a list response
     * @param header The header array
     * @param lista The list of data
     * @param mensaje The message being processed
     * @return Formatted response
     */
    public static String formatList(String[] header, List<String[]> lista, Mensaje mensaje) {
        return Help.listMensaje(mensaje.tableAction(), header, lista);
    }
    
    /**
     * Format a list response with PDF
     * @param header The header array
     * @param lista The list of data
     * @param mensaje The message being processed
     * @param pdfFile The PDF file path
     * @return Formatted response
     */
    public static String formatListWithPdf(String[] header, List<String[]> lista, Mensaje mensaje, String pdfFile) {
        return Help.listMensajeToPdf(mensaje.tableAction(), header, lista);
    }
    
    /**
     * Format a view response
     * @param header The header array
     * @param data The data array
     * @param mensaje The message being processed
     * @return Formatted response
     */
    public static String formatView(String[] header, String[] data, Mensaje mensaje) {
        return Help.ver(mensaje.tableAction(), header, data);
    }
    
    /**
     * Format a view response with report
     * @param data The data array
     * @param mensaje The message being processed
     * @return Formatted response
     */
    public static String formatViewWithReport(String[] data, Mensaje mensaje) {
        return Help.verWithReport(mensaje.tableAction(), data);
    }
    
    /**
     * Get the appropriate header for a table
     * @param table The table name
     * @return The header array
     */
    public static String[] getHeaderForTable(String table) {
        switch (table) {
            case Help.CLIENTE:
                return Help.clienteHeader;
            case Help.EMPLEADO:
                return Help.empleadoHeader;
            case Help.PRODUCTO:
                return Help.productoHeader;
            case Help.PRODUCTO_SERVICIOS:
                return Help.productoServicioHeader;
            case Help.SERVICIO:
                return Help.servicioHeader;
            case Help.EQUIPO_TRABAJO_SERVICIO:
                return Help.equipoTrabajoServicioHeader;
            case Help.CONTRATOS:
                return Help.contratoHeader;
            case Help.EQUIPO_TRABAJO:
                return Help.equipoTrabajoHeader;
            case Help.EMPLEADO_EQUIPO_TRABAJO:
                return Help.empleadoEquipoTrabajoHeader;
            case Help.INCIDENCIAS:
                return Help.incidenciasHeader;
            case Help.CONTRATO_INCIDENCIA:
                return Help.contratoIncidenciaHeader;
            default:
                return new String[]{"ID", "Nombre"};
        }
    }
}