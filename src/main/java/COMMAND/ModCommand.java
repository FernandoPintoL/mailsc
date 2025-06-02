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
        if (params.size() < 1) {
            throw new IllegalArgumentException("El comando Modificar espera al menos un parámetro (ID)");
        }
        // Primero, verifique si existe el ID
        int id = Integer.parseInt(params.get(0).trim());
        Method verMethod = negocioObject.getClass().getMethod("ver", int.class);
        String[] existingData = (String[]) verMethod.invoke(negocioObject, id);
        // Si el ID no existe, devuelve una lista de todos los registros
        if (existingData == null) {
            System.out.println("No se encontró ningún registro con el ID: " + id);
            Method listarMethod = negocioObject.getClass().getMethod("listar");
            List<String[]> allRecords = (List<String[]>) listarMethod.invoke(negocioObject);
            return new Object[] { false, "No se encontró ningún registro con el ID: " + id + ". Aquí está la lista de todos los registros.", allRecords};
        }

        // Encuentre el método modificar con el número correcto de parámetros
        Method[] methods = negocioObject.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("modificar") && method.getParameterCount() == params.size()) {
                // Convertir parámetros a los tipos correctos
                Object[] convertedParams = convertParameters(method, params);
                // Invocar el método
                return (Object[]) method.invoke(negocioObject, convertedParams);
            }
        }

        throw new NoSuchMethodException("No se encontró ningún método de modificación adecuado para " + table + " con " + params.size() + " parametros.");
    }

    /**
     * Convert string parameters to the right types based on method parameter types
     * @param method The method to invoke
     * @param params The string parameters
     * @return Converted parameters
     */
    private Object[] convertParameters(Method method, List<String> params) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] convertedParams = new Object[params.size()];

        for (int i = 0; i < params.size(); i++) {
            String param = params.get(i).trim();
            Class<?> type = paramTypes[i];

            if (type == int.class || type == Integer.class) {
                convertedParams[i] = Integer.parseInt(param);
            } else if (type == double.class || type == Double.class) {
                convertedParams[i] = Double.parseDouble(param);
            } else if (type == boolean.class || type == Boolean.class) {
                convertedParams[i] = Boolean.parseBoolean(param);
            } else {
                convertedParams[i] = param;
            }
        }

        return convertedParams;
    }
}
