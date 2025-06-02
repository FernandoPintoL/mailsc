package COMMAND;

import NEGOCIO.*;
import OBJECT.Mensaje;
import UTILS.Help;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de fábrica para crear objetos de comando
 * Esta clase es responsable de crear la implementación adecuada del comando
 * Basada en la tabla de mensajes y la acción
 */
public class CommandFactory {
    private static final Map<String, CommandCreator> commandCreators = new HashMap<>();

    // Initialize command creators
    static {
        // Registrar creadores de comandos para cada tabla y acción
        registerTableCommands(Help.CLIENTE, NCliente.class);
        registerTableCommands(Help.EMPLEADO, NEmpleado.class);
        registerTableCommands(Help.PRODUCTO, NProducto.class);
        registerTableCommands(Help.PRODUCTO_SERVICIOS, NProductoServicio.class);
        registerTableCommands(Help.SERVICIO, NServicio.class);
        registerTableCommands(Help.EQUIPO_TRABAJO_SERVICIO, NEquipoTrabajoServicio.class);
        registerTableCommands(Help.CONTRATOS, NContrato.class);
        registerTableCommands(Help.EQUIPO_TRABAJO, NEquipoTrabajos.class);
        registerTableCommands(Help.EMPLEADO_EQUIPO_TRABAJO, NEmpleadoEquipoTrabajos.class);
        registerTableCommands(Help.INCIDENCIAS, NIncidencia.class);
        registerTableCommands(Help.CONTRATO_INCIDENCIA, NContratoIncidencia.class);

    }
    /**
     * Registrar comandos para una tabla con su clase de lógica empresarial
     * @param table The table name
     * @param businessClass The business logic class
     */
    private static void registerTableCommands(String table, Class<?> businessClass) {
        registerCommand(table + "_" + Help.ADD, (negocioObjects) -> new AddCommand(table, negocioObjects));
        registerCommand(table + "_" + Help.MOD, (negocioObjects) -> new ModCommand(table, negocioObjects));
        registerCommand(table + "_" + Help.DEL, (negocioObjects) -> new DelCommand(table, negocioObjects));
        registerCommand(table + "_" + Help.VER, (negocioObjects) -> new VerCommand(table, negocioObjects));
        registerCommand(table + "_" + Help.LIS, (negocioObjects) -> new LisCommand(table, negocioObjects));
        // Registrar comando de reporte para todas las tablas que lo soportan
        if (table.equals(Help.CONTRATOS)) {
            registerCommand(table + "_" + Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
        }
        // Registrar comandos para los nuevos tipos de reportes
        registerCommand("CONTRATO_"+Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
        registerCommand("INVENTARIO_"+Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
        registerCommand("SERVICIOS_"+Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
        registerCommand("INCIDENCIAS_"+Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
        registerCommand("EMPLEADOS_"+Help.REP, (negocioObjects) -> new ReportCommand(table, negocioObjects));
    }
    /**
     * Registrar un creador de comandos con su clave
     * @param key The command key (table_action)
     * @param creator The command creator function
     */
    public static void registerCommand(String key, CommandCreator creator) {
        commandCreators.put(key, creator);
    }
    /**
     * Get a command for the given message
     * @param mensaje The message containing the table and action
     * @param negocioObjects Map of business logic objects
     * @return The appropriate Command implementation or null if not found
     */
    public static Command getCommand(Mensaje mensaje, Map<String, Object> negocioObjects) {
        String key = mensaje.tableAction();
        // como saber si el mensaje tiene parametros correctos y devolver el comando correcto y parametros correctos
        if (key == null || key.isEmpty()) {
            System.out.println("Mensaje no contiene una clave válida: " + mensaje);
            return null; // No se encontró la clave
        }
        // Buscar el creador de comandos correspondiente
        if (!commandCreators.containsKey(key)) {
            System.out.println("No se encontró el comando para la clave: " + key);
            return null; // No se encontró el comando
        }
        CommandCreator creator = commandCreators.get(key);
        if (creator == null) {
            System.out.println("No se encontró el creador de comandos para la clave: " + key);
            return null; // No se encontró el creador de comandos
        }
        return creator.createCommand(negocioObjects);
    }
    /**
     * Interfaz funcional para crear comandos
     */
    @FunctionalInterface
    public interface CommandCreator {
        Command createCommand(Map<String, Object> negocioObjects);
    }
}
