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
     * @param negocioObjects Mapa de objetos de lógica de negocio
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
        // quiero saber el nombre de la clase del objeto de negocio
        System.out.println("Objeto de negocio: " + negocioObject.getClass().getName());
        // Find the guardar method with the right number of parameters
        Method[] methods = negocioObject.getClass().getMethods();
        for (Method method : methods) {
            System.out.println("Método encontrado: " + method.getName() + " con " + method.getParameterCount() + " parámetros.");
            if (method.getName().equals("guardar") && method.getParameterCount() == params.size()) {
                System.out.println("Método de almacenamiento encontrado: " + method.getName());
                // Convert parameters to the right types
                Object[] convertedParams = convertParameters(method, params);
                // Invoke the method
                return (Object[]) method.invoke(negocioObject, convertedParams);
            }
        }
        
        throw new NoSuchMethodException("No se encontró ningún método de almacenamiento adecuado para " + table + " con " + params.size() + " parametros.");
    }
}