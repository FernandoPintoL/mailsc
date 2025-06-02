package COMMAND;

import OBJECT.Mensaje;
import UTILS.Help;

/**
 * Class for handling errors in a centralized way
 */
public class ErrorHandler {
    
    /**
     * Handle a general exception
     * @param exception The exception that occurred
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleException(Exception exception, Mensaje mensaje) {
        if (exception instanceof NumberFormatException) {
            return handleNumberFormatException(mensaje);
        } else if (exception instanceof IllegalArgumentException) {
            return handleIllegalArgumentException(exception.getMessage(), mensaje);
        } else {
            return handleGenericException(exception.toString(), mensaje);
        }
    }
    
    /**
     * Handle a NumberFormatException (error converting parameters)
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleNumberFormatException(Mensaje mensaje) {
        return Help.errorMensaje("Error convercion", 
                "Error al convertir elementos del parametro", 
                mensaje.toString());
    }
    
    /**
     * Handle an IllegalArgumentException (invalid parameters)
     * @param errorMessage The error message
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleIllegalArgumentException(String errorMessage, Mensaje mensaje) {
        return Help.errorMensaje("Error Parametros", 
                errorMessage, 
                mensaje.toString());
    }
    
    /**
     * Handle a generic exception (connection error, etc.)
     * @param exception The exception message
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleGenericException(String exception, Mensaje mensaje) {
        return Help.errorMensaje("Error de conexión", 
                "Error al conectarse al servidor, intente nuevamente.", 
                exception + " No se pudo ejecutar el comando: " + mensaje.toString());
    }
    
    /**
     * Handle an error with the command format
     * @param token The command token
     * @return Error message formatted for email
     */
    public static String handleCommandFormatError(String token) {
        return Help.errorMensaje("Error de comandos", 
                "No se pudo decifrar el comando", 
                " comando: " + token);
    }
    
    /**
     * Handle an error with the table or action
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleTableOrActionNotFound(Mensaje mensaje) {
        return Help.errorMensaje("Error TABLA o ACCCION", 
                "Tabla O Accion no encontrada", 
                mensaje.toString());
    }
    
    /**
     * Handle an error with the parameter length
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleParameterLengthError(Mensaje mensaje) {
        return Help.errorMensaje("Error Parametros", 
                "Cantidad de parametros incorrectos.", 
                mensaje.toString());
    }
}