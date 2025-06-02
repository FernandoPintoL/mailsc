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
        String helpInfo = "Para ver la lista completa de comandos disponibles, envíe un correo con la palabra HELP.";
        return Help.errorMensaje("Error de comandos", 
                "No se pudo decifrar el comando. " + helpInfo, 
                " comando: " + token);
    }

    /**
     * Handle an error with the table or action
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleTableOrActionNotFound(Mensaje mensaje) {
        // Obtener la lista de comandos disponibles
        String availableCommands = getAvailableCommandsInfo();

        return Help.errorMensaje("Error TABLA o ACCCION", 
                "Tabla o Acción no encontrada. Verifique que esté utilizando un comando válido.", 
                mensaje.toString() + "<br><br>" + availableCommands);
    }

    /**
     * Handle an error with the parameter length
     * @param mensaje The message being processed
     * @return Error message formatted for email
     */
    public static String handleParameterLengthError(Mensaje mensaje) {
        // Obtener información específica sobre el comando
        String commandInfo = getCommandInfo(mensaje.tableAction());

        return Help.errorMensaje("Error Parametros", 
                "Cantidad de parámetros incorrectos. Verifique la sintaxis correcta del comando.", 
                mensaje.toString() + "<br><br>" + commandInfo);
    }

    /**
     * Obtiene información sobre los comandos disponibles
     * @return String con información sobre los comandos disponibles
     */
    private static String getAvailableCommandsInfo() {
        StringBuilder info = new StringBuilder("<h3>Comandos disponibles:</h3>");
        info.append("<ul>");

        // Agregar información sobre los comandos de gestión
        info.append("<li><b>Gestión de Clientes:</b> CLI_ADD, CLI_MOD, CLI_DEL, CLI_VER, CLI_LIS</li>");
        info.append("<li><b>Gestión de Empleados:</b> EMP_ADD, EMP_MOD, EMP_DEL, EMP_VER, EMP_LIS</li>");
        info.append("<li><b>Gestión de Productos:</b> PRO_ADD, PRO_MOD, PRO_DEL, PRO_VER, PRO_LIS</li>");
        info.append("<li><b>Gestión de Servicios:</b> SRV_ADD, SRV_MOD, SRV_DEL, SRV_VER, SRV_LIS</li>");
        info.append("<li><b>Gestión de Contratos:</b> CNTR_ADD, CNTR_DEL, CNTR_VER, CNTR_LIS, CNTR_REP</li>");

        // Agregar información sobre los comandos de reportes
        info.append("<li><b>Reportes:</b> CONTRATO_REP, INVENTARIO_REP, SERVICIOS_REP, INCIDENCIAS_REP, EMPLEADOS_REP</li>");

        info.append("</ul>");
        info.append("<p>Para más detalles sobre cada comando, envíe un correo con la palabra HELP.</p>");

        return info.toString();
    }

    /**
     * Obtiene información específica sobre un comando
     * @param commandKey La clave del comando
     * @return String con información sobre el comando
     */
    private static String getCommandInfo(String commandKey) {
        if (commandKey == null || commandKey.isEmpty()) {
            return "Comando no especificado.";
        }

        StringBuilder info = new StringBuilder("<h3>Información del comando:</h3>");

        // Información específica para cada tipo de comando
        if (commandKey.startsWith("CLI_")) {
            info.append(getClienteCommandInfo(commandKey));
        } else if (commandKey.startsWith("EMP_")) {
            info.append(getEmpleadoCommandInfo(commandKey));
        } else if (commandKey.startsWith("PRO_")) {
            info.append(getProductoCommandInfo(commandKey));
        } else if (commandKey.startsWith("SRV_")) {
            info.append(getServicioCommandInfo(commandKey));
        } else if (commandKey.startsWith("CNTR_")) {
            info.append(getContratoCommandInfo(commandKey));
        } else if (commandKey.startsWith("REP_")) {
            info.append(getReporteCommandInfo(commandKey));
        } else {
            info.append("No hay información específica disponible para este comando.");
        }

        return info.toString();
    }

    private static String getClienteCommandInfo(String commandKey) {
        switch (commandKey) {
            case "CLI_ADD":
                return "CLI_ADD[ci,nombre,telefono,direccion,tipo_cliente]<br>Ejemplo: CLI_ADD[123456789,JOSE PEDRO,123456789,CALLE FALSA #3,RESIDENCIAL]";
            case "CLI_MOD":
                return "CLI_MOD[id,ci,nombre,telefono,direccion,tipo_cliente]<br>Ejemplo: CLI_MOD[1,123456789,JOSE PEDRO,123456789,CALLE FALSA #3,RESIDENCIAL]";
            case "CLI_DEL":
                return "CLI_DEL[id]<br>Ejemplo: CLI_DEL[1]";
            case "CLI_VER":
                return "CLI_VER[id]<br>Ejemplo: CLI_VER[1]";
            case "CLI_LIS":
                return "CLI_LIS[]<br>Ejemplo: CLI_LIS[]";
            default:
                return "Comando de cliente no reconocido.";
        }
    }

    private static String getEmpleadoCommandInfo(String commandKey) {
        switch (commandKey) {
            case "EMP_ADD":
                return "EMP_ADD[ci,nombre,telefono,puesto,estado]<br>Ejemplo: EMP_ADD[123456789,JOSE PEDRO,123456789,OPERARIO DE LIMPIEZA,ACTIVO]";
            case "EMP_MOD":
                return "EMP_MOD[id,ci,nombre,telefono,puesto,estado]<br>Ejemplo: EMP_MOD[1,123456789,JOSE PEDRO,123456789,OPERARIO DE LIMPIEZA,ACTIVO]";
            case "EMP_DEL":
                return "EMP_DEL[id]<br>Ejemplo: EMP_DEL[1]";
            case "EMP_VER":
                return "EMP_VER[id]<br>Ejemplo: EMP_VER[1]";
            case "EMP_LIS":
                return "EMP_LIS[]<br>Ejemplo: EMP_LIS[]";
            default:
                return "Comando de empleado no reconocido.";
        }
    }

    private static String getProductoCommandInfo(String commandKey) {
        switch (commandKey) {
            case "PRO_ADD":
                return "PRO_ADD[nombre,descripcion,precio,stock]<br>Ejemplo: PRO_ADD[PRODUCTO,DETALLE PRODUCTO,10,100]";
            case "PRO_MOD":
                return "PRO_MOD[id,nombre,descripcion,precio,stock]<br>Ejemplo: PRO_MOD[1,PRODUCTO,DETALLE PRODUCTO,10,100]";
            case "PRO_DEL":
                return "PRO_DEL[id]<br>Ejemplo: PRO_DEL[1]";
            case "PRO_VER":
                return "PRO_VER[id]<br>Ejemplo: PRO_VER[1]";
            case "PRO_LIS":
                return "PRO_LIS[]<br>Ejemplo: PRO_LIS[]";
            default:
                return "Comando de producto no reconocido.";
        }
    }

    private static String getServicioCommandInfo(String commandKey) {
        switch (commandKey) {
            case "SRV_ADD":
                return "SRV_ADD[nombre,descripcion,precio,frecuencia,estado]<br>Ejemplo: SRV_ADD[SERVICIO #1,DESCRIPCION,10,DIARIA,ACTIVO]";
            case "SRV_MOD":
                return "SRV_MOD[id,nombre,descripcion,precio,frecuencia,estado]<br>Ejemplo: SRV_MOD[1,SERVICIO #1,DESCRIPCION,10,DIARIA,ACTIVO]";
            case "SRV_DEL":
                return "SRV_DEL[id]<br>Ejemplo: SRV_DEL[1]";
            case "SRV_VER":
                return "SRV_VER[id]<br>Ejemplo: SRV_VER[1]";
            case "SRV_LIS":
                return "SRV_LIS[]<br>Ejemplo: SRV_LIS[]";
            default:
                return "Comando de servicio no reconocido.";
        }
    }

    private static String getContratoCommandInfo(String commandKey) {
        switch (commandKey) {
            case "CNTR_ADD":
                return "CNTR_ADD[cliente_id,servicio_id,descripcion,estado,fecha_inicio,fecha_fin,monto_pagado]<br>Ejemplo: CNTR_ADD[1,1,DESCRIPCION CONTRATO,ACTIVO,2023-01-01,2023-12-31,100]";
            case "CNTR_DEL":
                return "CNTR_DEL[id]<br>Ejemplo: CNTR_DEL[1]";
            case "CNTR_VER":
                return "CNTR_VER[id]<br>Ejemplo: CNTR_VER[1]";
            case "CNTR_LIS":
                return "CNTR_LIS[]<br>Ejemplo: CNTR_LIS[]";
            case "CNTR_REP":
                return "CNTR_REP[id]<br>Ejemplo: CNTR_REP[1]";
            default:
                return "Comando de contrato no reconocido.";
        }
    }

    private static String getReporteCommandInfo(String commandKey) {
        switch (commandKey) {
            case "CONTRATO_REP":
                return "CONTRATO_REP[id_contrato]<br>Ejemplo: CONTRATO_REP[1]";
            case "INVENTARIO_REP":
                return "INVENTARIO_REP[]<br>Ejemplo: INVENTARIO_REP[]";
            case "SERVICIOS_REP":
                return "SERVICIOS_REP[]<br>Ejemplo: SERVICIOS_REP[]";
            case "INCIDENCIAS_REP":
                return "INCIDENCIAS_REP[id_contrato]<br>Ejemplo: INCIDENCIAS_REP[1]";
            case "EMPLEADOS_REP":
                return "EMPLEADOS_REP[id_equipo]<br>Ejemplo: EMPLEADOS_REP[1]";
            default:
                return "Comando de reporte no reconocido.";
        }
    }
}
