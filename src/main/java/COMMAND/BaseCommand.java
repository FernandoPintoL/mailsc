package COMMAND;

import OBJECT.Mensaje;
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
}