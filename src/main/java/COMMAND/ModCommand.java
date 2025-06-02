package COMMAND;

import OBJECT.Mensaje;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Command for modifying a record
 */
public class ModCommand extends BaseCommand {
    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public ModCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }

    /**
     * Execute the modify command
     * @param mensaje The message containing the parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        List<String> params = mensaje.getParametros();
        Object negocioObject = getNegocioObject();
        // Para modificar, esperamos al menos un parámetro (el ID)
        if (params.isEmpty()) {
            throw new IllegalArgumentException("El comando Modificar espera al menos un parámetro (ID)");
        }
        // Encuentre el metodo modificar con el número correcto de parámetros
        Method[] methods = negocioObject.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("modificar") && method.getParameterCount() == params.size()) {
                // Convertir parámetros a los tipos correctos
                Object[] convertedParams = convertParameters(method, params);
                // Invocar el metodo
                return (Object[]) method.invoke(negocioObject, convertedParams);
            }
        }

        throw new NoSuchMethodException("No se encontró ningún método de modificación adecuado para " + table + " con " + params.size() + " parametros.");
    }
}
