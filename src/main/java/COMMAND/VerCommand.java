package COMMAND;

import OBJECT.Mensaje;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Command for viewing a record
 */
public class VerCommand extends BaseCommand {

    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public VerCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }

    /**
     * Execute the view command
     * @param mensaje The message containing the parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        List<String> params = mensaje.getParametros();
        Object negocioObject = getNegocioObject();

        // Para la vista, solo esperamos un parámetro (el ID)
        if (params.size() != 1) {
            throw new IllegalArgumentException("El comando de vista espera exactamente un parámetro (ID)");
        }

        // Encuentra el método ver
        Method method = negocioObject.getClass().getMethod("ver", int.class);

        // Convertir el parámetro ID a int
        int id = Integer.parseInt(params.get(0).trim());

        // Invocar el metodo
        String[] data = (String[]) method.invoke(negocioObject, id);

        // Si el ID no existe, devuelve una lista de todos los registros
        if (data == null) {
            Method listarMethod = negocioObject.getClass().getMethod("listar");
            List<String[]> allRecords = (List<String[]>) listarMethod.invoke(negocioObject);
            return new Object[] { allRecords, "No se encontró ningún registro con el ID: " + id + ". Aquí está la lista de todos los registros." };
        }

        // Devuelve los datos en el formato esperado por la clase Consulta
        return new Object[] { data, "Success" };
    }
}
