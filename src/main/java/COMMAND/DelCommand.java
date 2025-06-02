package COMMAND;

import OBJECT.Mensaje;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Command for deleting a record
 */
public class DelCommand extends BaseCommand {

    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public DelCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }

    /**
     * Execute the delete command
     * @param mensaje The message containing the parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        List<String> params = mensaje.getParametros();
        Object negocioObject = getNegocioObject();

        // For delete, we expect only one parameter (the ID)
        if (params.size() != 1) {
            throw new IllegalArgumentException("Delete command expects exactly one parameter (ID)");
        }

        // Convert the ID parameter to int
        int id = Integer.parseInt(params.get(0).trim());

        // First, check if the ID exists
        Method verMethod = negocioObject.getClass().getMethod("ver", int.class);
        String[] existingData = (String[]) verMethod.invoke(negocioObject, id);

        // If the ID doesn't exist, return a list of all records
        if (existingData == null) {
            Method listarMethod = negocioObject.getClass().getMethod("listar");
            List<String[]> allRecords = (List<String[]>) listarMethod.invoke(negocioObject);
            return new Object[] { allRecords, "No se encontró ningún registro con el ID: " + id + ". Aquí está la lista de todos los registros." };
        }

        // Find the eliminar method
        Method method = negocioObject.getClass().getMethod("eliminar", int.class);

        // Invoke the method
        return (Object[]) method.invoke(negocioObject, id);
    }
}
