package COMMAND;

import OBJECT.Mensaje;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Command for listing records
 */
public class LisCommand extends BaseCommand {
    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public LisCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }

    /**
     * Ejecutar el comando de lista
     * @param mensaje El mensaje que contiene los parámetros
     * @return Resultado de la ejecución del comando
     * @throws Exception Si se produce un error durante la ejecución
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        Object negocioObject = getNegocioObject();
        // Para la lista, no esperamos ningún parámetro.
        if (!mensaje.getParametros().isEmpty()) {
            throw new IllegalArgumentException("El comando de lista no espera ningún parámetro");
        }
        // Encuentra el metodo listar
        Method method = negocioObject.getClass().getMethod("listar");
        // Invocar el metodo
        List<String[]> data = (List<String[]>) method.invoke(negocioObject);
        // Devuelve los datos en el formato esperado por la clase Consulta
        return new Object[] { data, "Success" };
    }
}
