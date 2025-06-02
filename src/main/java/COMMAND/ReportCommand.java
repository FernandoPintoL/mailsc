package COMMAND;

import NEGOCIO.NReporte;
import OBJECT.Mensaje;
import UTILS.Help;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Command for generating reports
 */
public class ReportCommand extends BaseCommand {

    // Constantes para los tipos de reportes
    private static final String TIPO_CONTRATO = "CONTRATO";
    private static final String TIPO_INVENTARIO = "INVENTARIO";
    private static final String TIPO_SERVICIOS = "SERVICIOS";
    private static final String TIPO_INCIDENCIAS = "INCIDENCIAS";
    private static final String TIPO_EMPLEADOS = "EMPLEADOS";

    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public ReportCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }

    /**
     * Execute the report command
     * @param mensaje The message containing the parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        System.out.println("Ejecutando comando de reporte");
        List<String> params = mensaje.getParametros();
        System.out.println("Parámetros del mensaje: " + params);

        // Verificar que haya al menos un parámetro (tipo de reporte)
        /*if (params.isEmpty()) {
            throw new IllegalArgumentException("El comando de informe espera al menos un parámetro (tipo de informe)");
        }*/

        // Obtener el tipo de reporte (primer parámetro)
        String tipoReporte = mensaje.getTable();
        System.out.println("Tipo de reporte: " + tipoReporte);

        // Get the NReporte object
        NReporte reporteObject = (NReporte) negocioObjects.get("NEGOCIO_REPORTE");

        // Get current time for the filename
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH-mm-ss");
        String horaFormateada = horaActual.format(formatter_time);

        // Generar el reporte según el tipo
        switch (tipoReporte) {
            case TIPO_CONTRATO:
                System.out.println("Generando reporte de contrato");
                // Convertir el ID a entero
                int contratoId = Integer.parseInt(params.get(0).trim());
                System.out.println("ID del contrato: " + contratoId);

                // Generar el nombre del archivo PDF
                String contratoPath = Help.PATH + "CONTRATO_" + contratoId + "_" + horaFormateada + ".pdf";
                System.out.println("Ruta del archivo PDF: " + contratoPath);

                // Generar el reporte
                String[] contratoData = reporteObject.contrato(contratoId, contratoPath);
                System.out.println("Datos del contrato: " + (contratoData != null ? String.join(", ", contratoData) : "null"));

                // Retornar los datos y el nombre del archivo
                return new Object[] { contratoData, contratoPath };

            case TIPO_INVENTARIO:
                // Generar el nombre del archivo PDF
                String inventarioPath = Help.PATH + "INVENTARIO_" + horaFormateada + ".pdf";
                System.out.println("Ruta del archivo PDF de inventario: " + inventarioPath);

                // Generar el reporte
                ArrayList<String[]> inventarioData = reporteObject.inventarioProductos(inventarioPath);
                System.out.println("Datos del inventario: " + (inventarioData != null ? inventarioData.size() + " registros" : "null"));

                // Retornar los datos y el nombre del archivo
                return new Object[] { inventarioData, inventarioPath };

            case TIPO_SERVICIOS:
                // Generar el nombre del archivo PDF
                String serviciosPath = Help.PATH + "SERVICIOS_" + horaFormateada + ".pdf";

                // Generar el reporte
                ArrayList<String[]> serviciosData = reporteObject.serviciosEstadisticas(serviciosPath);

                // Retornar los datos y el nombre del archivo
                return new Object[] { serviciosData, serviciosPath };

            case TIPO_INCIDENCIAS:
                // Verificar que haya un segundo parámetro (ID del contrato)
                if (params.size() < 2) {
                    throw new IllegalArgumentException("El reporte de incidencias requiere un parámetro de ID de contrato. Ejemplo: REP_INCIDENCIAS[1]");
                }

                // Convertir el ID a entero
                int incidenciasContratoId = Integer.parseInt(params.get(1).trim());

                // Generar el nombre del archivo PDF
                String incidenciasPath = Help.PATH + "INCIDENCIAS_CONTRATO_" + incidenciasContratoId + "_" + horaFormateada + ".pdf";

                // Generar el reporte
                ArrayList<String[]> incidenciasData = reporteObject.incidenciasContrato(incidenciasContratoId, incidenciasPath);

                // Retornar los datos y el nombre del archivo
                return new Object[] { incidenciasData, incidenciasPath };

            case TIPO_EMPLEADOS:
                // Verificar que haya un segundo parámetro (ID del equipo)
                /*if (params.size() < 2) {
                    throw new IllegalArgumentException("El reporte de empleados requiere un parámetro de ID de equipo. Ejemplo: REP_EMPLEADOS[1]");
                }*/

                // Convertir el ID a entero
                int equipoId = Integer.parseInt(params.get(0).trim());
                System.out.println("ID del equipo: " + equipoId);
                // Generar el nombre del archivo PDF
                String empleadosPath = Help.PATH + "EMPLEADOS_EQUIPO_" + equipoId + "_" + horaFormateada + ".pdf";
                System.out.println("Ruta del archivo PDF de empleados: " + empleadosPath);
                // Generar el reporte
                ArrayList<String[]> empleadosData = reporteObject.empleadosEquipo(equipoId, empleadosPath);
                System.out.println("Datos de empleados: " + (empleadosData != null ? empleadosData.size() + " registros" : "null"));
                // Retornar los datos y el nombre del archivo
                return new Object[] { empleadosData, empleadosPath };

            default:
                throw new IllegalArgumentException("Tipo de reporte desconocido: " + tipoReporte +
                    ". Los tipos válidos son: CONTRATO, INVENTARIO, SERVICIOS, INCIDENCIAS, EMPLEADOS");
        }
    }
}
