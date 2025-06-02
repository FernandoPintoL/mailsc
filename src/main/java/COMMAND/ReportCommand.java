package COMMAND;

import NEGOCIO.NReporte;
import OBJECT.Mensaje;
import UTILS.Help;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Command for generating reports
 */
public class ReportCommand extends BaseCommand {
    
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
        List<String> params = mensaje.getParametros();
        
        // For report, we expect only one parameter (the ID)
        if (params.size() != 1) {
            throw new IllegalArgumentException("Report command expects exactly one parameter (ID)");
        }
        
        // Get the NReporte object
        NReporte reporteObject = (NReporte) negocioObjects.get("NEGOCIO_REPORTE");
        
        // Convert the ID parameter to int
        int id = Integer.parseInt(params.get(0).trim());
        
        // Get current time for the filename
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH-mm-ss");
        String horaFormateada = horaActual.format(formatter_time);
        
        // Generate the PDF filename
        String name_pdf = Help.PATH + "CONTRATO_" + id + "_" + horaFormateada + ".pdf";
        
        // Generate the report
        String[] data = reporteObject.contrato(id, name_pdf);
        
        // Return the data and the PDF filename
        return new Object[] { data, name_pdf };
    }
}