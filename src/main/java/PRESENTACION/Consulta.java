package PRESENTACION;

import COMMAND.*;
import NEGOCIO.*;
import UTILS.ConstGlobal;
import UTILS.Help;
import UTILS.Subject;
import CONNECTION.Pop3;
import CONNECTION.Smtp;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.SocketException;

import OBJECT.Mensaje;

public class Consulta {
    private final NCliente NEGOCIO_CLIENTE;
    private final NContrato NEGOCIO_CONTRATO;
    private final NEmpleado NEGOCIO_EMPLEADO;
    private final NEmpleadoEquipoTrabajos NEGOCIO_EMPLEADO_EQUIPO_TRABAJO;
    private final NEquipoTrabajos NEGOCIO_EQUIPO_TRABAJO;
    private final NEquipoTrabajoServicio NEGOCIO_EQUIPO_TRABAJO_SERVICIO;
    private final NIncidencia NEGOCIO_INCIDENCIA;
    private final NContratoIncidencia NEGOCIO_CONTRATO_INCIDENCIA;
    private final NProducto NEGOCIO_PRODUCTO;
    private final NProductoServicio NEGOCIO_PRODUCTO_SERVICIO;
    private final NServicio NEGOCIO_SERVICIO;
    private final NReporte NEGOCIO_REPORTE;
    private Pop3 pop3;
    private Object response;

    public Consulta() throws IOException {
        // negocio o bussinness
        NEGOCIO_CLIENTE = new NCliente();
        NEGOCIO_CONTRATO = new NContrato();
        NEGOCIO_EMPLEADO = new NEmpleado();
        NEGOCIO_EMPLEADO_EQUIPO_TRABAJO = new NEmpleadoEquipoTrabajos();
        NEGOCIO_EQUIPO_TRABAJO = new NEquipoTrabajos();
        NEGOCIO_EQUIPO_TRABAJO_SERVICIO = new NEquipoTrabajoServicio();
        NEGOCIO_INCIDENCIA = new NIncidencia();
        NEGOCIO_CONTRATO_INCIDENCIA = new NContratoIncidencia();
        NEGOCIO_PRODUCTO = new NProducto();
        NEGOCIO_PRODUCTO_SERVICIO = new NProductoServicio();
        NEGOCIO_SERVICIO = new NServicio();
        NEGOCIO_REPORTE = new NReporte();
    }

    public int getCantidadMails() throws IOException {
        pop3 = new Pop3(ConstGlobal.SERVIDOR, ConstGlobal.PORT_POP3);
        pop3.login(ConstGlobal.USER, ConstGlobal.PASS);
        pop3.list();
        String number = pop3.number;
        //System.out.println("number:::: "+number);
        pop3.retr(Integer.parseInt(number));
        pop3.quit();
        pop3.close();
        return Integer.parseInt(number);
    }

    /**
     * Process a new message
     *
     * @throws IOException If an error occurs during processing
     */
    public void newMensaje() throws IOException {
        String token = pop3.Token();
        String email = pop3.getEmail();
        // Check if the message is a help request
        if (token.toUpperCase().contains(Help.HELP)) {
            System.out.println("Mensaje de ayuda recibido: " + token);
            sendMail(email, "AYUDA", Help.ContenidoHelp());
            return;
        }
        // Parse the message
        Mensaje msj = Subject.subject(token, email);
        if (msj == null) {
            System.out.println("Error al parsear el mensaje: " + token);
            sendMail(email, "Error de comandos", ErrorHandler.handleCommandFormatError(token));
            return;
        }
        try {
            System.out.println("Mensaje recibido: " + msj);
            // Process the message
            negocioAction(msj);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initialize the map of business logic objects
     *
     * @return Map of business logic objects
     */
    private Map<String, Object> initializeNegocioObjects() {
        Map<String, Object> negocioObjects = new HashMap<>();
        negocioObjects.put("NEGOCIO_" + Help.CLIENTE, NEGOCIO_CLIENTE);
        negocioObjects.put("NEGOCIO_" + Help.CONTRATOS, NEGOCIO_CONTRATO);
        negocioObjects.put("NEGOCIO_" + Help.EMPLEADO, NEGOCIO_EMPLEADO);
        negocioObjects.put("NEGOCIO_" + Help.EMPLEADO_EQUIPO_TRABAJO, NEGOCIO_EMPLEADO_EQUIPO_TRABAJO);
        negocioObjects.put("NEGOCIO_" + Help.EQUIPO_TRABAJO, NEGOCIO_EQUIPO_TRABAJO);
        negocioObjects.put("NEGOCIO_" + Help.EQUIPO_TRABAJO_SERVICIO, NEGOCIO_EQUIPO_TRABAJO_SERVICIO);
        negocioObjects.put("NEGOCIO_" + Help.INCIDENCIAS, NEGOCIO_INCIDENCIA);
        negocioObjects.put("NEGOCIO_" + Help.CONTRATO_INCIDENCIA, NEGOCIO_CONTRATO_INCIDENCIA);
        negocioObjects.put("NEGOCIO_" + Help.PRODUCTO, NEGOCIO_PRODUCTO);
        negocioObjects.put("NEGOCIO_" + Help.PRODUCTO_SERVICIOS, NEGOCIO_PRODUCTO_SERVICIO);
        negocioObjects.put("NEGOCIO_" + Help.SERVICIO, NEGOCIO_SERVICIO);
        negocioObjects.put("NEGOCIO_REPORTE", NEGOCIO_REPORTE);
        return negocioObjects;
    }

    /**
     * Process the message and execute the appropriate command
     *
     * @param msj The message to process
     * @throws Exception If an error occurs during processing
     */
    private void negocioAction(Mensaje msj) throws Exception {
        try {
            // Get the business logic objects
            Map<String, Object> negocioObjects = initializeNegocioObjects();
            // Get the command from the factory
            Command command = CommandFactory.getCommand(msj, negocioObjects);
            if (command == null) {
                System.out.println("No se encontró el comando para la acción: " + msj.getAction() + " en la tabla: " + msj.getTable());
                // Command not found
                sendMail(msj.getEmisor(), "PARAMETROS INCORRECTOS | " + msj.getParametros(),
                        ErrorHandler.handleTableOrActionNotFound(msj));
                return;
            }
            // Execute the command
            Object[] result = command.execute(msj);
            // Process the result based on the command type
            processCommandResult(msj, result);
        } catch (NumberFormatException e) {
            // Error converting parameters
            sendMail(msj.getEmisor(), "Error al convertir un parametros".toUpperCase(),
                    ErrorHandler.handleNumberFormatException(msj));
        } catch (NoSuchMethodException e) {
            // Error with parameter length
            sendMail(msj.getEmisor(), "PARAMETROS INCORRECTOS | " + msj.getParametros(),
                    ErrorHandler.handleParameterLengthError(msj));
        } catch (SQLException | IOException ex) {
            // Error connecting to the database
            sendMail(msj.getEmisor(), "Error de conexion".toUpperCase(),
                    ErrorHandler.handleGenericException(ex.toString(), msj));
        } catch (Exception e) {
            // Check if the cause is NoSuchMethodException
            if (e.getCause() instanceof NoSuchMethodException) {
                // Error with parameter length
                sendMail(msj.getEmisor(), "PARAMETROS INCORRECTOS | " + msj.getParametros(),
                        ErrorHandler.handleParameterLengthError(msj));
            } else {
                // Generic error
                sendMail(msj.getEmisor(), "Error".toUpperCase(),
                        ErrorHandler.handleException(e, msj));
            }
        }
    }

    /**
     * Process the result of a command execution
     *
     * @param msj    The message that was processed
     * @param result The result of the command execution
     * @throws IOException If an error occurs during processing
     */
    private void processCommandResult(Mensaje msj, Object[] result) throws IOException {
        System.out.println("Procesando resultado del comando: " + msj);
        if (result == null || result.length == 0) {
            return;
        }
        String action = msj.getAction().toUpperCase();
        String table = msj.getTable().toUpperCase();

        // Get the appropriate header for the table
        String[] header = ResponseFormatter.getHeaderForTable(table);

        switch (action) {
            case Help.VER -> {
                // View a single record
                String[] data = (String[]) result[0];
                if (data == null) {
                    sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + msj.getParametros().get(0));
                } else {
                    String response = ResponseFormatter.formatView(header, data, msj);
                    sendMail(msj.getEmisor(), msj.evento(), response);
                }
            }
            case Help.LIS -> {
                // List all records
                List<String[]> lista = (List<String[]>) result[0];
                String response = ResponseFormatter.formatList(header, lista, msj);
                sendMail(msj.getEmisor(), msj.evento(), response);
            }
            case Help.REP -> {
                System.out.println("Generando reporte");
                // Generate a report
                Object reportData = result[0];
                System.out.println("DATA TYPE: " + (reportData != null ? reportData.getClass().getName() : "null"));
                String pdfFile = (String) result[1];
                System.out.println("PDF FILE: " + pdfFile);

                if (reportData == null) {
                    sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCUENTRAN DATOS REGISTRADOS PARA ESTA CONSULTA".toUpperCase());
                } else {
                    System.out.println("Enviando reporte por correo: " + msj.getEmisor());
                    String response;

                    // Determinar el tipo de reporte y formatear adecuadamente
                    if (reportData instanceof String[]) {
                        // Reporte de tipo CONTRATO
                        System.out.println("Formateando reporte de tipo String[]");
                        response = ResponseFormatter.formatViewWithReport((String[]) reportData, msj);
                    } else if (reportData instanceof List<?>) {
                        // Reportes de tipo INVENTARIO, SERVICIOS, INCIDENCIAS, EMPLEADOS
                        System.out.println("Formateando reporte de tipo List<String[]>");
                        List<String[]> listData = (List<String[]>) reportData;
                        response = ResponseFormatter.formatListWithReport(listData, msj);
                    } else {
                        // Tipo de reporte desconocido
                        System.err.println("Tipo de reporte desconocido: " + reportData.getClass().getName());
                        response = "Tipo de reporte no soportado: " + reportData.getClass().getName();
                    }
                    System.out.println("RESPONSE: " + response);
                    try {
                        // Generar una imagen con el resultado de la consulta
                        String imageFile = null;

                        if (reportData instanceof List<?>) {
                            List<String[]> listData = (List<String[]>) reportData;
                            if (!listData.isEmpty()) {
                                // Crear nombre para el archivo de imagen basado en el PDF
                                String baseName = pdfFile.substring(0, pdfFile.lastIndexOf("."));
                                imageFile = baseName + "_chart.png";

                                // Generar imagen con los datos
                                System.out.println("Generando imagen del reporte: " + imageFile);
                                boolean success = false;

                                try {
                                    // Usar ChartGenerator para crear una imagen con los datos
                                    ChartGenerator chartGen = ChartGenerator.getInstance()
                                            .withDimensions(800, 600)
                                            .with3D(true)
                                            .withBackgroundColor(Color.WHITE)
                                            .withTitleFont(new Font("Arial", Font.BOLD, 16));

                                    // Convertir List a ArrayList para el método createPieChart
                                    ArrayList<String[]> chartData = new ArrayList<>(listData);

                                    // Determinar qué tipo de gráfico crear según los datos
                                    // Para inventario: columna 1 (NOMBRE) como etiqueta y columna 4 (STOCK) como valor
                                    success = chartGen.createPieChart(chartData, imageFile, 1, 4, 
                                            "Resultados de la consulta: " + msj.evento());

                                    if (!success) {
                                        System.err.println("No se pudo generar la imagen del gráfico");
                                        imageFile = null;
                                    }
                                } catch (Exception ex) {
                                    System.err.println("Error al generar la imagen: " + ex.getMessage());
                                    ex.printStackTrace();
                                    imageFile = null;
                                }
                            }
                        }

                        // Enviar la imagen con los resultados de la consulta
                        if (imageFile != null && new File(imageFile).exists()) {
                            sendMailImage(msj.getEmisor(), msj.evento() + " - Resultados", 
                                    "Adjunto encontrará una imagen con los resultados de la consulta solicitada.", imageFile);
                        } else {
                            // Si no se pudo generar la imagen, enviar los resultados como texto
                            sendMail(msj.getEmisor(), msj.evento() + " - Resultados", response);
                        }

                        // Luego enviar el archivo PDF
                        /*sendMailPdf(msj.getEmisor(), msj.evento() + " - Archivo adjunto",
                                "Adjunto encontrará el archivo PDF con el reporte solicitado.", pdfFile);*/
                    } catch (Exception e) {
                        System.err.println("Error al enviar el correo con imagen/PDF: " + e.getMessage());
                        sendMail(msj.getEmisor(), msj.evento(), "ERROR AL ENVIAR EL REPORTE: " + e.getMessage());
                    }
                }
            }
            default -> {
                // ADD, MOD, DEL
                Boolean success = (Boolean) result[0];
                String message = (String) result[1];
                // si no es exitoso, enviar mensaje de error con tables
                if (!success && (action.equals(Help.DEL) || action.equals(Help.MOD))) {
                    message += "<h3>" + message.toUpperCase() + " | VERIFIQUE DATOS REALES CON: " + table + "_LIS[]</h3>";
                }

                sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
            }
        }
    }
    /**
     * Send an email
     *
     * @param rcpt    The recipient
     * @param titulo  The subject
     * @param mensaje The message body
     * @throws IOException If an error occurs during sending
     */
    private void sendMail(String rcpt, String titulo, String mensaje) throws IOException {
//        System.out.println("rcpt: " + rcpt + " , titulo: " + titulo + " , mensaje: " + mensaje);
        Smtp smtp = new Smtp(ConstGlobal.SERVIDOR, ConstGlobal.PORT_SMPT);
        smtp.sendMail(ConstGlobal.EMAIL, "<" + rcpt + ">", titulo, mensaje);
    }

    /**
     * Send an email with a PDF attachment
     *
     * @param rcpt         The recipient
     * @param titulo       The subject
     * @param mensaje      The message body
     * @param name_pdfFile The PDF file path
     * @throws IOException If an error occurs during sending
     */
    private void sendMailPdf(String rcpt, String titulo, String mensaje, String name_pdfFile) throws IOException {
        try {
            // Código para enviar el correo con PDF
            Smtp smtp = new Smtp(ConstGlobal.SERVIDOR, ConstGlobal.PORT_SMPT);
            smtp.sendMailWithPdf(ConstGlobal.EMAIL, "<" + rcpt + ">", titulo, mensaje, name_pdfFile);
        } catch (SocketException e) {
            System.err.println("Error de conexión con el servidor SMTP: " + e.getMessage());
            throw new IOException("Error al enviar el correo: conexión reiniciada por el servidor", e);
        } catch (IOException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Send an email with an image attachment
     *
     * @param rcpt         The recipient
     * @param titulo       The subject
     * @param mensaje      The message body
     * @param name_imageFile The image file path
     * @throws IOException If an error occurs during sending
     */
    private void sendMailImage(String rcpt, String titulo, String mensaje, String name_imageFile) throws IOException {
        try {
            // Código para enviar el correo con imagen
            Smtp smtp = new Smtp(ConstGlobal.SERVIDOR, ConstGlobal.PORT_SMPT);
            smtp.sendMailWithImage(ConstGlobal.EMAIL, "<" + rcpt + ">", titulo, mensaje, name_imageFile);
        } catch (SocketException e) {
            System.err.println("Error de conexión con el servidor SMTP: " + e.getMessage());
            throw new IOException("Error al enviar el correo: conexión reiniciada por el servidor", e);
        } catch (IOException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw e;
        }
    }
}
