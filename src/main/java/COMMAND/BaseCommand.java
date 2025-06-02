package COMMAND;

import OBJECT.Mensaje;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Base abstract class for all commands
 * Provides common functionality for all commands
 */
public abstract class BaseCommand implements Command {
    protected final String table;
    protected final Map<String, Object> negocioObjects;
    
    /**
     * Constructor
     * @param table The table name
     * @param negocioObjects Map of business logic objects
     */
    public BaseCommand(String table, Map<String, Object> negocioObjects) {
        this.table = table;
        this.negocioObjects = negocioObjects;
    }
    /**
     * Get the business logic object for the table
     * @return The business logic object
     */
    protected Object getNegocioObject() {
        return negocioObjects.get("NEGOCIO_" + table);
    }
    /**
     * Execute the command with the given message
     * @param mensaje The message containing the command parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    @Override
    public abstract Object[] execute(Mensaje mensaje) throws Exception;

    /**
     * Convierte los parámetros de cadena a los tipos correctos según los tipos de parámetros del metodo
     * @param method The method to invoke
     * @param params The string parameters
     * @return Converted parameters
     */
    public Object[] convertParameters(Method method, List<String> params) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] convertedParams = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            String param = params.get(i).trim();
            Class<?> type = paramTypes[i];
            try{
                // considerar convertir YYY-MM-DD a LocalDateTime si el tipo es LocalDateTime
                if (type == java.time.LocalDateTime.class) {
                    if (param.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        convertedParams[i] = java.time.LocalDate.parse(param).atStartOfDay();
                    } else {
                        convertedParams[i] = java.time.LocalDateTime.parse(param);
                    }
                } else if (type == int.class || type == Integer.class) {
                    convertedParams[i] = Integer.parseInt(param);
                } else if (type == double.class || type == Double.class) {
                    convertedParams[i] = Double.parseDouble(param);
                } else if (type == boolean.class || type == Boolean.class) {
                    convertedParams[i] = Boolean.parseBoolean(param);
                } else {
                    convertedParams[i] = param;
                }
            } catch (Exception e) {
                System.err.println("Error al convertir el parámetro '" + param + "' al tipo " + type.getName() + ": " + e.getMessage());
                throw new RuntimeException("Error al convertir el parámetro '" + param + "' al tipo " + type.getName(), e);
            }

        }

        return convertedParams;
    }
}