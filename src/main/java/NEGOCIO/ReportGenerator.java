/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NEGOCIO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 *
 * @author fpl
 */
public class ReportGenerator {
    
    Document document;
    PdfPTable table;
    PdfWriter writer;
    FooterPageEvent footerPageEvent;
    Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE); // Texto blanco
    ReportGenerator(){
        document = new Document();
    }
    
    /**
     * Método para crear un gráfico de torta a partir de cualquier tipo de datos.
     *
     * @param data             Los datos en formato ArrayList<String[]>.
     * @param chartFilePath    La ruta donde se guardará el archivo de la imagen.
     * @param labelIndex       El índice de la columna a usar como etiqueta.
     * @param valueIndex       El índice de la columna a usar como valor numérico.
     */
    
    // Método para crear el gráfico de torta
    public void createPieChart(ArrayList<String[]> data, String chartFilePath, int labelIndex, int valueIndex) {
        try {
            DefaultPieDataset dataset = new DefaultPieDataset();

            // Agregar los datos al gráfico usando los índices especificados
            for (String[] row : data) {
                String label = "ALMACEN: "+row[2]+" : "+row[labelIndex] + " : " +row[valueIndex]; // Etiqueta (por ejemplo, nombre del producto)
                double value = Double.parseDouble(row[valueIndex]); // Valor numérico (por ejemplo, stock)
                dataset.setValue(label, value);
            }

            // Crear el gráfico de torta
            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Distribución de Stock por Producto",
                    dataset,
                    true,
                    true,
                    false
            );

            // Guardar el gráfico como archivo PNG
            ChartUtils.saveChartAsPNG(new File(chartFilePath), pieChart, 800, 600);
            System.out.println("Gráfico de torta guardado en: " + chartFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generatePdfReportCompra(String[] cabecera, ArrayList<String[]> data, String name_pdfFilePath) {
        try{
            // Crear el documento y el escritor de PDF
            writer = PdfWriter.getInstance(document, new FileOutputStream(name_pdfFilePath));
            // Agregar el evento de pie de página
            footerPageEvent = new FooterPageEvent();
            writer.setPageEvent(footerPageEvent);
            document.open();
            // Agregar título al documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("COMPRA #"+cabecera[0], titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));
            String id_compra = cabecera[0];
            String precioTotal = cabecera[1];
            String fecha = cabecera[2];
            String estado = cabecera[3];
            String nombre_cliente = cabecera[4];
            String nombre_administrativo = cabecera[5];
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph p_id_compra = new Paragraph("ID Compra: "+id_compra, subTitleFont);
            Paragraph p_fecha = new Paragraph("Fecha: "+fecha, subTitleFont);
            Paragraph p_precioTotal = new Paragraph("Precio Total: "+precioTotal, subTitleFont);
            Paragraph p_nombre_cliente = new Paragraph("Cliente: "+nombre_cliente, subTitleFont);
            Paragraph p_nombre_administrativo = new Paragraph("Administrativo: "+nombre_administrativo, subTitleFont);
            Paragraph p_estado = new Paragraph("Estado: "+estado, subTitleFont);
            document.add(p_id_compra);
            document.add(p_fecha);
            document.add(p_precioTotal);
            document.add(p_nombre_cliente);
            document.add(p_nombre_administrativo);
            document.add(p_estado);
            document.add(new Paragraph("\n"));
            if(data.isEmpty()){
                String nodata = "NO HAY DATOS DISPONIBLES";
                document.add(new Paragraph(nodata, titleFont));
            }else{
                // Determinar el número de columnas a partir del primer elemento
                int numColumns = data.get(0).length;
                table = new PdfPTable(numColumns);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);
                table.getDefaultCell().setPadding(5);
                table.getDefaultCell().setBorderWidth(1);
                table.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
                table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                for (String header : data.get(0)) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(BaseColor.BLUE);
                    table.addCell(headerCell);
                }
                for (int i = 1; i < data.size(); i++) {
                    for (String cellData : data.get(i)) {
                        PdfPCell cellDatas = new PdfPCell(new Phrase(cellData));
                        cellDatas.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cellDatas);
                    }
                }
                document.add(table);
            }
            // Cerrar el documento
            document.close();
            System.out.println("PDF generado: " + name_pdfFilePath);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void generatePdfReport(ArrayList<String[]> data, String name_pdfFilePath, String chartFilePath) {
        try {
            // Crear el documento y el escritor de PDF
            writer = PdfWriter.getInstance(document, new FileOutputStream(name_pdfFilePath));
            // Agregar el evento de pie de página
            footerPageEvent = new FooterPageEvent();
            writer.setPageEvent(footerPageEvent);
            document.open();

            // Agregar título al documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("REPORTE DE PRODUCTOS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));
            if (data.isEmpty()) {
                document.add(new Paragraph("NO HAY DATOS DISPONIBLES"));
            } else {
                // Determinar el número de columnas a partir del primer elemento
                int numColumns = data.get(0).length;
                table = new PdfPTable(numColumns);                
                // Agregar encabezados de columna (opcional, si tu ArrayList no tiene encabezados)
                // Agregar encabezados de columna con fondo verde y texto blanco

                for (String header : data.get(0)) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(BaseColor.BLUE);
                    table.addCell(headerCell);
                }
                // Agregar datos al cuerpo de la tabla (a partir del segundo elemento)
                for (int i = 1; i < data.size(); i++) {
                    for (String cellData : data.get(i)) {
                        PdfPCell cellDatas = new PdfPCell(new Phrase(cellData));
                        cellDatas.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cellDatas);
                    }
                }
                // Agregar la tabla al documento
                document.add(table);
            }
            
            // Insertar el gráfico de torta en el PDF
            document.add(new Paragraph("\n"));
            Image chartImage = Image.getInstance(chartFilePath);
            chartImage.scaleToFit(500, 300); // Escalar la imagen si es necesario
            chartImage.setAlignment(Element.ALIGN_CENTER);
            document.add(chartImage);
            
            // Cerrar el documento
            document.close();
            System.out.println("PDF generado: " + name_pdfFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
