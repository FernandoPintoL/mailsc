package CONNECTION;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

import NEGOCIO.PdfCompressor;
import UTILS.ConstSMPT;
import com.sun.source.tree.TryTree;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author angela
 */
public class Smtp extends Conexion {

    public Smtp(String servidor, int port) throws IOException {
        super(servidor, port);
    }

    String comando = "";

    public void helo() throws IOException {
        comando = ConstSMPT.HELO + servidor + ConstSMPT.FINLINE;
        System.out.println("C: " + comando);
        salida.writeBytes(comando);
        System.out.println("S: " + entrada.readLine());
    }

    public void mailFrom(String emailEmisor) throws IOException {
        try {
            comando = ConstSMPT.MAIL_FROM + emailEmisor + ConstSMPT.FINLINE;
            System.out.println("C: " + comando);
            salida.writeBytes(comando);
            System.out.println("S: " + entrada.readLine());
            System.out.println("MAIL FROM");
        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace(System.out); // Muestra detalles completos del stack trace en la consola
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(System.out); // Para excepciones que no sean de tipo IOException
        }
    }

    public void rcptTo(String emailReceptor) throws IOException {
        try {
            comando = ConstSMPT.RCPT_TO + emailReceptor + ConstSMPT.FINLINE;
            System.out.println("C: " + comando);
            salida.writeBytes(comando);
            System.out.println("S: " + entrada.readLine());
            System.out.println("RCPTTO");
        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace(System.out); // Muestra detalles completos del stack trace en la consola
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(System.out); // Para excepciones que no sean de tipo IOException
        }
    }

    public void data() throws IOException {
        try {
            comando = ConstSMPT.DATA;
            System.out.println("C: " + comando);
            salida.writeBytes(comando);
            System.out.println("S: " + entrada.readLine());
            System.out.println("DATA");
        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace(System.out); // Muestra detalles completos del stack trace en la consola
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(System.out); // Para excepciones que no sean de tipo IOException
        }
    }

    public void subject(String title, String message) throws IOException {
        try {
            comando = ConstSMPT.SUBJECT + title + ConstSMPT.FINLINE;
            comando += message + ConstSMPT.FINLINE;
            comando += ConstSMPT.FINSUBJECT;
            System.out.println("C: " + comando);
            salida.writeBytes(comando);
            System.out.println("S: " + entrada.readLine());
            System.out.println("SUBJECT");
        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace(System.out); // Muestra detalles completos del stack trace en la consola
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(System.out); // Para excepciones que no sean de tipo IOException
        }
    }

    public void quit() throws IOException {
        try {
            comando = ConstSMPT.QUIT;
            System.out.println("C: " + comando);
            salida.writeBytes(comando);
            System.out.println("S: " + entrada.readLine());
        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace(System.out); // Muestra detalles completos del stack trace en la consola
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(System.out); // Para excepciones que no sean de tipo IOException
        }
    }

    public void sendMail(String emisor, String rcpto, String subject, String mensaje) throws IOException {
        mailFrom(emisor);
        rcptTo(rcpto);
        data();
        subject(subject, mensaje);
        quit();
    }

    /**
     * Envía un correo electrónico con un archivo PDF adjunto
     * Si el archivo PDF es demasiado grande, intenta comprimirlo antes de enviarlo
     * 
     * @param emisor Dirección de correo del emisor
     * @param receptor Dirección de correo del receptor
     * @param subject Asunto del correo
     * @param mensaje Cuerpo del mensaje en formato HTML
     * @param rutaPdf Ruta del archivo PDF a adjuntar
     * @throws IOException Si ocurre un error durante el envío
     */
    public void sendMailWithPdf(String emisor, String receptor, String subject, String mensaje, String rutaPdf) throws IOException {
        final int maxRetries = 3;
        final int retryDelayMs = 2000;
        final long maxPdfSizeBytes = 8_000_000; // 8MB límite (dejando margen para codificación Base64)
        final int maxChunkSize = 2048; // 2KB máximo por chunk para Base64 (más pequeño para evitar problemas de socket)

        // Variables para seguimiento de errores
        String lastErrorMessage = null;
        Exception lastException = null;
        Socket originalSocket = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Si estamos reintentando, necesitamos restablecer la conexión
                if (attempt > 1) {
                    try {
                        // Cerrar la conexión anterior si existe
                        if (socket != null && !socket.isClosed()) {
                            try {
                                socket.close();
                            } catch (Exception e) {
                                // Ignorar errores al cerrar
                            }
                        }

                        // Crear una nueva conexión
                        socket = new Socket(this.servidor, this.port);
                        socket.setSoTimeout(60000);
                        socket.setKeepAlive(true);
                        socket.setTcpNoDelay(true);
                        socket.setSendBufferSize(8192);

                        // Reinicializar los streams
                        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        salida = new DataOutputStream(socket.getOutputStream());

                        // Leer la respuesta inicial del servidor
                        entrada.readLine();

                        System.out.println("Conexión restablecida para reintento " + attempt);
                    } catch (IOException e) {
                        System.err.println("Error al restablecer la conexión: " + e.getMessage());
                        throw new IOException("No se pudo restablecer la conexión para reintento", e);
                    }
                }

                // Verificar si el archivo PDF existe
                if (!Files.exists(Paths.get(rutaPdf))) {
                    System.out.println("El archivo PDF no existe en la ruta especificada, se enviará el correo sin adjunto.");
                    sendMail(emisor, receptor, subject, mensaje);
                    return;
                }

                // Verificar tamaño del archivo y comprimir si es necesario
                String pdfToSend = rutaPdf;
                Path pdfPath = Paths.get(rutaPdf);
                long fileSize = Files.size(pdfPath);

                // Verificar si el archivo es demasiado pequeño (menos de 100 bytes)
                if (fileSize < 100) {
                    System.err.println("Advertencia: El archivo PDF es muy pequeño (" + fileSize + " bytes), podría estar corrupto.");
                }

                if (fileSize > maxPdfSizeBytes) {
                    System.out.println("El archivo PDF es grande (" + fileSize + " bytes), intentando comprimir...");
                    try {
                        pdfToSend = PdfCompressor.compressIfNeeded(rutaPdf, maxPdfSizeBytes);
                        pdfPath = Paths.get(pdfToSend);
                        fileSize = Files.size(pdfPath);

                        if (fileSize > maxPdfSizeBytes) {
                            System.out.println("Advertencia: El archivo PDF sigue siendo grande después de la compresión (" + 
                                    fileSize + " bytes). El correo podría no enviarse correctamente.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error al comprimir el PDF: " + e.getMessage());
                        // Continuar con el archivo original si la compresión falla
                        System.out.println("Continuando con el archivo original sin comprimir");
                    }
                }

                // Iniciar el envío del correo
                mailFrom(emisor);
                rcptTo(receptor);
                data();

                // Encabezados del correo
                String boundary = "==MyBoundary==" + System.currentTimeMillis();
                writeWithFlush("MIME-Version: 1.0" + ConstSMPT.FINLINE);
                writeWithFlush("Content-Type: multipart/mixed; boundary=\"" + boundary + "\"" + ConstSMPT.FINLINE);
                writeWithFlush(ConstSMPT.SUBJECT + subject + ConstSMPT.FINLINE);

                // Cuerpo del mensaje
                writeWithFlush("--" + boundary + ConstSMPT.FINLINE);
                writeWithFlush("Content-Type: text/html; charset=\"UTF-8\"" + ConstSMPT.FINLINE);
                writeWithFlush(ConstSMPT.FINLINE);
                writeWithFlush(mensaje + ConstSMPT.FINLINE);

                // Adjuntar el archivo PDF
                String namePdf = Paths.get(pdfToSend).getFileName().toString();
                System.out.println("Adjuntando archivo: " + namePdf + " (" + fileSize + " bytes)");
                writeWithFlush("--" + boundary + ConstSMPT.FINLINE);
                writeWithFlush("Content-Type: application/pdf; name=\"" + namePdf + "\"" + ConstSMPT.FINLINE);
                writeWithFlush("Content-Transfer-Encoding: base64" + ConstSMPT.FINLINE);
                writeWithFlush("Content-Disposition: attachment; filename=\"" + namePdf + "\"" + ConstSMPT.FINLINE);
                writeWithFlush(ConstSMPT.FINLINE);

                // Leer y codificar el archivo PDF en Base64 en chunks más pequeños
                try (InputStream input = Files.newInputStream(pdfPath)) {
                    byte[] buffer = new byte[1536]; // Buffer más pequeño (1.5KB) para generar chunks de Base64 de aprox. 2KB
                    Base64.Encoder encoder = Base64.getEncoder();
                    int bytesRead;
                    long totalBytesRead = 0;
                    int lineLength = 76; // Longitud estándar para líneas Base64

                    while ((bytesRead = input.read(buffer)) != -1) {
                        byte[] actualBytes = bytesRead == buffer.length ? buffer : Arrays.copyOf(buffer, bytesRead);
                        String encoded = encoder.encodeToString(actualBytes);

                        // Dividir la cadena codificada en líneas de longitud fija para mejor manejo
                        for (int i = 0; i < encoded.length(); i += lineLength) {
                            int end = Math.min(encoded.length(), i + lineLength);
                            writeWithFlush(encoded.substring(i, end) + ConstSMPT.FINLINE);

                            // Pequeña pausa cada 20 líneas para evitar sobrecarga
                            if (i > 0 && i % (lineLength * 20) == 0) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }

                        totalBytesRead += bytesRead;
                        // Mostrar progreso para archivos grandes
                        if (fileSize > 500_000 && totalBytesRead % 500_000 < buffer.length) {
                            System.out.println("Progreso: " + (totalBytesRead * 100 / fileSize) + "% (" + 
                                    totalBytesRead + "/" + fileSize + " bytes)");
                        }
                    }
                    System.out.println("Archivo codificado completamente: " + totalBytesRead + " bytes");
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo PDF: " + e.getMessage());
                    e.printStackTrace();
                    throw new IOException("Error al leer el archivo PDF: " + e.getMessage(), e);
                }

                // Final del multipart
                writeWithFlush("--" + boundary + "--" + ConstSMPT.FINLINE);
                writeWithFlush(ConstSMPT.FINSUBJECT);
                salida.flush();

                // Cerrar la conexión
                quit();

                // Limpiar archivos temporales si se crearon
                if (!pdfToSend.equals(rutaPdf) && Files.exists(Paths.get(pdfToSend))) {
                    try {
                        Files.deleteIfExists(Paths.get(pdfToSend));
                        System.out.println("Archivo temporal eliminado: " + pdfToSend);
                    } catch (IOException e) {
                        System.err.println("No se pudo eliminar el archivo temporal: " + e.getMessage());
                        // No es crítico, continuamos
                    }
                }

                System.out.println("Correo enviado exitosamente a: " + receptor);
                return; // Salir del método si todo fue exitoso
            } catch (IOException e) {
                lastErrorMessage = e.getMessage();
                lastException = e;

                System.err.println("Error al enviar el correo (intento " + attempt + "/" + maxRetries + "): " + e.getMessage());

                if (attempt == maxRetries) {
                    System.err.println("Se agotaron los reintentos. No se pudo enviar el correo.");
                    throw new IOException("No se pudo enviar el correo después de " + maxRetries + " intentos: " + lastErrorMessage, lastException);
                }

                System.out.println("Reintentando en " + retryDelayMs + " ms...");
                try {
                    Thread.sleep(retryDelayMs); // Esperar antes de reintentar
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                    throw new IOException("Interrupción durante el reintento", ie);
                }
            }
        }
    }

    /**
     * Escribe datos al socket en fragmentos pequeños para evitar sobrecarga
     * y realiza flush después de cada fragmento para asegurar que los datos se envíen
     * 
     * @param data Los datos a escribir
     * @throws IOException Si ocurre un error durante la escritura
     */
    private void writeWithFlush(String data) throws IOException {
        try {
            // Reducir el tamaño del chunk a 512 bytes para mayor estabilidad
            int chunkSize = 512;
            int length = data.length();

            for (int i = 0; i < length; i += chunkSize) {
                // Asegurar que no excedemos el límite del string
                int end = Math.min(length, i + chunkSize);
                String chunk = data.substring(i, end);

                // Intentar escribir el chunk con reintentos
                int maxRetries = 3;
                int retryCount = 0;
                boolean success = false;

                while (!success && retryCount < maxRetries) {
                    try {
                        salida.writeBytes(chunk);
                        salida.flush();
                        success = true;
                    } catch (IOException e) {
                        retryCount++;
                        if (retryCount >= maxRetries) {
                            throw e;
                        }
                        // Esperar antes de reintentar
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                // Añadir una pequeña pausa cada 4KB de datos
                if (i > 0 && i % 4096 == 0) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escribir datos al socket: " + e.getMessage());
            throw new IOException("Error al escribir datos al socket: " + e.getMessage(), e);
        }
    }

}
