package COMMAND;

import OBJECT.Mensaje;
import UTILS.Help;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Command for adding a new record
 */
public class AddCommand extends BaseCommand {
    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public AddCommand(String table, Map<String, Object> negocioObjects) {
        super(table, negocioObjects);
    }
    
    /**
     * Execute the add command
     * @param mensaje The message containing the parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public Object[] execute(Mensaje mensaje) throws Exception {
        List<String> params = mensaje.getParametros();
        Object negocioObject = getNegocioObject();
        // Find the guardar method with the right number of parameters
        Method[] methods = negocioObject.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("guardar") && method.getParameterCount() == params.size()) {
                // Convert parameters to the right types
                Object[] convertedParams = convertParameters(method, params);
                // Invoke the method
                return (Object[]) method.invoke(negocioObject, convertedParams);
            }
        }
        
        throw new NoSuchMethodException("No suitable guardar method found for " + table + " with " + params.size() + " parameters");
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