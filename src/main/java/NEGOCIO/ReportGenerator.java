/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NEGOCIO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase para la generación de reportes en formato PDF
 * @author fpl
 */
public class ReportGenerator {

    private Document document;
    private PdfWriter writer;
    private final FooterPageEvent footerPageEvent;
    private Font headerFont;
    private Font titleFont;
    private Font subTitleFont;

    /**
     * Constructor por defecto
     * Inicializa el documento y las fuentes
     */
    public ReportGenerator() {
        document = new Document(PageSize.A4, 36, 36, 54, 36); // Márgenes: izquierda, derecha, arriba, abajo
        headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
        subTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        footerPageEvent = new FooterPageEvent();
    }

    /**
     * Método para crear un gráfico de torta a partir de cualquier tipo de datos.
     * Utiliza la clase ChartGenerator para generar el gráfico.
     *
     * @param data             Los datos en formato ArrayList<String[]>.
     * @param chartFilePath    La ruta donde se guardará el archivo de la imagen.
     * @param labelIndex       El índice de la columna a usar como etiqueta.
     * @param valueIndex       El índice de la columna a usar como valor numérico.
     * @return true si el gráfico se generó correctamente, false en caso contrario
     */
    public boolean createPieChart(ArrayList<String[]> data, String chartFilePath, int labelIndex, int valueIndex) {
        if (data == null || data.isEmpty() || chartFilePath == null || chartFilePath.isEmpty()) {
            System.err.println("Error: Datos o ruta de archivo inválidos");
            return false;
        }

        try {
            // Utilizar la clase ChartGenerator para crear el gráfico
            return ChartGenerator.getInstance()
                    .withDimensions(800, 600)
                    .with3D(true) // Usar gráfico 3D para mejor visualización
                    .createPieChart(
                            data, 
                            chartFilePath, 
                            labelIndex, 
                            valueIndex, 
                            "Distribución de Stock por Producto"
                    );
        } catch (Exception e) {
            System.err.println("Error al generar el gráfico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Genera un reporte PDF para una compra o transacción
     * 
     * @param cabecera Array con los datos de la cabecera [id, precio, fecha, estado, cliente, administrativo]
     * @param data Lista de arrays con los datos de la tabla
     * @param pdfFilePath Ruta donde se guardará el archivo PDF
     * @return true si el PDF se generó correctamente, false en caso contrario
     */
    public boolean generatePdfReportCompra(String[] cabecera, ArrayList<String[]> data, String pdfFilePath) {
        // Validar parámetros
        if (cabecera == null || cabecera.length < 6 || pdfFilePath == null || pdfFilePath.isEmpty()) {
            System.err.println("Error: Parámetros inválidos para generar el reporte de compra");
            return false;
        }

        // Reiniciar el documento para evitar problemas con documentos anteriores
        document = new Document(PageSize.A4, 36, 36, 54, 36);

        try (FileOutputStream fos = new FileOutputStream(pdfFilePath)) {
            // Crear el escritor de PDF
            writer = PdfWriter.getInstance(document, fos);

            // Agregar el evento de pie de página
            writer.setPageEvent(footerPageEvent);

            // Abrir el documento
            document.open();

            // Extraer datos de la cabecera
            String idCompra = cabecera[0];
            String precioTotal = cabecera[1];
            String fecha = cabecera[2];
            String estado = cabecera[3];
            String nombreCliente = cabecera[4];
            String nombreAdministrativo = cabecera[5];

            // Agregar título al documento
            Paragraph title = new Paragraph("COMPRA #" + idCompra, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Agregar información de la cabecera
            document.add(new Paragraph("ID Compra: " + idCompra, subTitleFont));
            document.add(new Paragraph("Fecha: " + fecha, subTitleFont));
            document.add(new Paragraph("Precio Total: " + precioTotal, subTitleFont));
            document.add(new Paragraph("Cliente: " + nombreCliente, subTitleFont));
            document.add(new Paragraph("Administrativo: " + nombreAdministrativo, subTitleFont));
            document.add(new Paragraph("Estado: " + estado, subTitleFont));
            document.add(new Paragraph("\n"));

            // Verificar si hay datos para la tabla
            if (data == null || data.isEmpty()) {
                document.add(new Paragraph("NO HAY DATOS DISPONIBLES", titleFont));
            } else {
                // Crear la tabla con los datos
                addTableToDocument(data);
            }

            // Cerrar el documento
            document.close();
            writer = null; // Liberar el recurso

            System.out.println("PDF generado: " + pdfFilePath);
            return true;
        } catch (DocumentException e) {
            System.err.println("Error al generar el documento PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Asegurar que el documento se cierre en caso de error
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return false;
    }

    /**
     * Método auxiliar para agregar una tabla al documento
     * 
     * @param data Lista de arrays con los datos de la tabla
     * @throws DocumentException Si ocurre un error al agregar la tabla
     */
    private void addTableToDocument(ArrayList<String[]> data) throws DocumentException {
        if (data == null || data.isEmpty()) {
            return;
        }

        // Determinar el número de columnas a partir del primer elemento
        int numColumns = data.get(0).length;
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        // Configurar las celdas por defecto
        table.getDefaultCell().setPadding(5);
        table.getDefaultCell().setBorderWidth(1);
        table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        // Los encabezados ya están incluidos en la primera fila de data
        // No es necesario agregarlos de nuevo, solo formatearlos como encabezados
        String[] headers = data.get(0);
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(BaseColor.BLUE);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        // Agregar filas de datos (empezando desde la segunda fila)
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            for (String cellData : row) {
                PdfPCell cell = new PdfPCell(new Phrase(cellData != null ? cellData : ""));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(4);
                table.addCell(cell);
            }
        }

        // Agregar la tabla al documento
        document.add(table);
    }
    /**
     * Genera un documento PDF simple con un título y una lista de datos
     * 
     * @param tituloDocumento Título del documento
     * @param data Array con los datos a mostrar
     * @param pdfFilePath Ruta donde se guardará el archivo PDF
     * @return true si el PDF se generó correctamente, false en caso contrario
     */
    public boolean generarDocumento(String tituloDocumento, String[] data, String pdfFilePath) {
        // Validar parámetros
        if (tituloDocumento == null || pdfFilePath == null || pdfFilePath.isEmpty()) {
            System.err.println("Error: Título o ruta de archivo inválidos");
            return false;
        }

        // Reiniciar el documento para evitar problemas con documentos anteriores
        document = new Document(PageSize.A4, 36, 36, 54, 36);

        try (FileOutputStream fos = new FileOutputStream(pdfFilePath)) {
            // Crear el escritor de PDF
            writer = PdfWriter.getInstance(document, fos);

            // Agregar el evento de pie de página
            writer.setPageEvent(footerPageEvent);

            // Abrir el documento
            document.open();

            // Agregar título al documento
            Paragraph title = new Paragraph(tituloDocumento, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Verificar si hay datos para mostrar
            if (data == null || data.length == 0) {
                document.add(new Paragraph("NO HAY DATOS DISPONIBLES", subTitleFont));
            } else {
                // Agregar cada elemento del array como un párrafo
                for (String item : data) {
                    if (item != null) {
                        Paragraph paragraph = new Paragraph(item);
                        paragraph.setAlignment(Element.ALIGN_LEFT);
                        paragraph.setSpacingAfter(5);
                        document.add(paragraph);
                    }
                }
            }

            // Cerrar el documento
            document.close();
            writer = null; // Liberar el recurso

            System.out.println("PDF generado: " + pdfFilePath);
            return true;
        } catch (DocumentException e) {
            System.err.println("Error al generar el documento PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Asegurar que el documento se cierre en caso de error
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return false;
    }

    /**
     * Genera un reporte PDF con datos tabulares y opcionalmente un gráfico
     * 
     * @param data Lista de arrays con los datos de la tabla
     * @param pdfFilePath Ruta donde se guardará el archivo PDF
     * @param chartFilePath Ruta del archivo de imagen del gráfico (puede ser null)
     * @param reportTitle Título del reporte (si es null, se usa un título por defecto)
     * @return true si el PDF se generó correctamente, false en caso contrario
     */
    public boolean generatePdfReport(ArrayList<String[]> data, String pdfFilePath, String chartFilePath, String reportTitle) {
        // Validar parámetros
        if (pdfFilePath == null || pdfFilePath.isEmpty()) {
            System.err.println("Error: Ruta de archivo inválida");
            return false;
        }

        // Usar título por defecto si no se proporciona uno
        String title = (reportTitle != null && !reportTitle.isEmpty()) 
                      ? reportTitle : "REPORTE DE DATOS";

        // Reiniciar el documento para evitar problemas con documentos anteriores
        document = new Document(PageSize.A4, 36, 36, 54, 36);

        try (FileOutputStream fos = new FileOutputStream(pdfFilePath)) {
            // Crear el escritor de PDF
            writer = PdfWriter.getInstance(document, fos);

            // Agregar el evento de pie de página
            writer.setPageEvent(footerPageEvent);

            // Abrir el documento
            document.open();

            // Agregar título al documento
            Paragraph titleParagraph = new Paragraph(title, titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(new Paragraph("\n"));

            // Verificar si hay datos para la tabla
            if (data == null || data.isEmpty()) {
                document.add(new Paragraph("NO HAY DATOS DISPONIBLES", subTitleFont));
            } else {
                // Crear la tabla con los datos
                addTableToDocument(data);
            }

            // Insertar el gráfico si existe
            if (chartFilePath != null && !chartFilePath.isEmpty()) {
                try {
                    File chartFile = new File(chartFilePath);
                    if (chartFile.exists() && chartFile.isFile()) {
                        document.add(new Paragraph("\n"));
                        Image chartImage = Image.getInstance(chartFilePath);
                        chartImage.scaleToFit(500, 300); // Escalar la imagen si es necesario
                        chartImage.setAlignment(Element.ALIGN_CENTER);
                        document.add(chartImage);
                    } else {
                        System.err.println("Advertencia: El archivo de gráfico no existe: " + chartFilePath);
                    }
                } catch (IOException e) {
                    System.err.println("Error al cargar la imagen del gráfico: " + e.getMessage());
                    // Continuar sin el gráfico
                }
            }

            // Cerrar el documento
            document.close();
            writer = null; // Liberar el recurso

            System.out.println("PDF generado: " + pdfFilePath);
            return true;
        } catch (DocumentException e) {
            System.err.println("Error al generar el documento PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Asegurar que el documento se cierre en caso de error
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return false;
    }

    /**
     * Sobrecarga del método generatePdfReport para mantener compatibilidad con código existente
     * 
     * @param data Lista de arrays con los datos de la tabla
     * @param pdfFilePath Ruta donde se guardará el archivo PDF
     * @param chartFilePath Ruta del archivo de imagen del gráfico (puede ser null)
     * @return true si el PDF se generó correctamente, false en caso contrario
     */
    public boolean generatePdfReport(ArrayList<String[]> data, String pdfFilePath, String chartFilePath) {
        return generatePdfReport(data, pdfFilePath, chartFilePath, "REPORTE DE DATOS");
    }
}
