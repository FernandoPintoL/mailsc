package COMMAND;

import OBJECT.Mensaje;

/**
 * Interface for the Command Pattern
 * This interface defines the contract for all command implementations
 */
public interface Command {
    /**
     * Execute the command with the given message
     * @param mensaje The message containing the command parameters
     * @return Result of the command execution
     * @throws Exception If an error occurs during execution
     */
    Object[] execute(Mensaje mensaje) throws Exception;
}