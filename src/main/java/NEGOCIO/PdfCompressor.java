package NEGOCIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Clase para comprimir archivos PDF
 * Utiliza iText para reducir el tamaño de los archivos PDF
 */
public class PdfCompressor {

    /**
     * Comprime un archivo PDF si su tamaño excede el límite especificado
     * 
     * @param pdfPath Ruta del archivo PDF a comprimir
     * @param maxSizeBytes Tamaño máximo permitido en bytes
     * @return Ruta del archivo comprimido (puede ser el mismo archivo si no se necesitó compresión)
     * @throws IOException Si ocurre un error durante la compresión
     */
    public static String compressIfNeeded(String pdfPath, long maxSizeBytes) throws IOException {
        Path path = Paths.get(pdfPath);
        
        // Verificar si el archivo existe
        if (!Files.exists(path)) {
            throw new IOException("El archivo PDF no existe: " + pdfPath);
        }
        
        // Obtener el tamaño del archivo
        long fileSize = Files.size(path);
        
        // Si el archivo es más pequeño que el límite, no es necesario comprimirlo
        if (fileSize <= maxSizeBytes) {
            System.out.println("El archivo PDF no necesita compresión: " + fileSize + " bytes");
            return pdfPath;
        }
        
        System.out.println("Comprimiendo archivo PDF: " + fileSize + " bytes (límite: " + maxSizeBytes + " bytes)");
        
        // Determinar el nivel de compresión basado en cuánto excede el límite
        int compressionLevel = determineCompressionLevel(fileSize, maxSizeBytes);
        
        // Generar nombre para el archivo comprimido
        String compressedPath = getCompressedFilePath(pdfPath);
        
        // Comprimir el archivo
        boolean success = compressPdf(pdfPath, compressedPath, compressionLevel);
        
        if (success) {
            // Verificar el tamaño del archivo comprimido
            long compressedSize = Files.size(Paths.get(compressedPath));
            System.out.println("Archivo PDF comprimido: " + compressedSize + " bytes (reducción: " + 
                    String.format("%.2f", (1 - (double)compressedSize/fileSize) * 100) + "%)");
            
            // Si aún es demasiado grande, intentar con un nivel de compresión más alto
            if (compressedSize > maxSizeBytes && compressionLevel < 9) {
                System.out.println("El archivo sigue siendo demasiado grande, intentando con mayor compresión");
                Files.delete(Paths.get(compressedPath)); // Eliminar el archivo comprimido anterior
                return compressIfNeeded(pdfPath, maxSizeBytes); // Intentar de nuevo con mayor compresión
            }
            
            return compressedPath;
        } else {
            System.out.println("No se pudo comprimir el archivo PDF, devolviendo el original");
            return pdfPath;
        }
    }
    
    /**
     * Determina el nivel de compresión basado en cuánto excede el archivo el límite de tamaño
     * 
     * @param fileSize Tamaño actual del archivo en bytes
     * @param maxSizeBytes Tamaño máximo permitido en bytes
     * @return Nivel de compresión (1-9)
     */
    private static int determineCompressionLevel(long fileSize, long maxSizeBytes) {
        // Calcular cuántas veces excede el límite
        double ratio = (double) fileSize / maxSizeBytes;
        
        // Asignar un nivel de compresión basado en el ratio
        if (ratio <= 1.5) return 5;      // Compresión media para archivos ligeramente grandes
        else if (ratio <= 3) return 7;   // Compresión alta para archivos moderadamente grandes
        else return 9;                   // Compresión máxima para archivos muy grandes
    }
    
    /**
     * Genera un nombre de archivo para la versión comprimida
     * 
     * @param originalPath Ruta del archivo original
     * @return Ruta del archivo comprimido
     */
    private static String getCompressedFilePath(String originalPath) {
        Path path = Paths.get(originalPath);
        String fileName = path.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String directory = path.getParent().toString();
        
        return directory + "\\" + baseName + "_compressed" + extension;
    }
    
    /**
     * Comprime un archivo PDF utilizando iText
     * 
     * @param inputPath Ruta del archivo de entrada
     * @param outputPath Ruta del archivo de salida
     * @param compressionLevel Nivel de compresión (1-9)
     * @return true si la compresión fue exitosa, false en caso contrario
     */
    private static boolean compressPdf(String inputPath, String outputPath, int compressionLevel) {
        try {
            // Leer el PDF original
            PdfReader reader = new PdfReader(inputPath);
            
            // Configurar opciones de compresión
            reader.consolidateNamedDestinations();
            
            // Crear un ByteArrayOutputStream para almacenar el PDF comprimido temporalmente
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            // Crear un PdfStamper para aplicar la compresión
            PdfStamper stamper = new PdfStamper(reader, baos);
            
            // Configurar el nivel de compresión
            stamper.setFullCompression();
            stamper.getWriter().setCompressionLevel(compressionLevel);
            
            // Comprimir imágenes y flujos
            int total = reader.getXrefSize();
            for (int i = 0; i < total; i++) {
                stamper.getWriter().addToBody(reader.getPageN(i), reader.getPageOrigRef(i));
            }
            
            // Cerrar el stamper y el reader
            stamper.close();
            reader.close();
            
            // Escribir el PDF comprimido al archivo de salida
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(baos.toByteArray());
            }
            
            return true;
        } catch (IOException | DocumentException e) {
            System.err.println("Error al comprimir el PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}