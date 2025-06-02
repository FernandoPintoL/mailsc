package NEGOCIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase especializada para la generación de gráficos estadísticos
 * Implementa el patrón Singleton para optimizar recursos
 */
public class ChartGenerator {
    
    private static ChartGenerator instance;
    
    // Configuración por defecto
    private int width = 800;
    private int height = 600;
    private boolean is3D = false;
    private Color backgroundColor = Color.WHITE;
    private Font titleFont = new Font("SansSerif", Font.BOLD, 16);
    
    /**
     * Constructor privado (patrón Singleton)
     */
    private ChartGenerator() {
    }
    
    /**
     * Obtiene la instancia única de ChartGenerator
     * @return Instancia de ChartGenerator
     */
    public static synchronized ChartGenerator getInstance() {
        if (instance == null) {
            instance = new ChartGenerator();
        }
        return instance;
    }
    
    /**
     * Configura las dimensiones del gráfico
     * @param width Ancho en píxeles
     * @param height Alto en píxeles
     * @return La instancia actual para encadenamiento de métodos
     */
    public ChartGenerator withDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }
    
    /**
     * Configura si el gráfico debe ser 3D
     * @param is3D true para gráfico 3D, false para 2D
     * @return La instancia actual para encadenamiento de métodos
     */
    public ChartGenerator with3D(boolean is3D) {
        this.is3D = is3D;
        return this;
    }
    
    /**
     * Configura el color de fondo del gráfico
     * @param backgroundColor Color de fondo
     * @return La instancia actual para encadenamiento de métodos
     */
    public ChartGenerator withBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    
    /**
     * Configura la fuente del título
     * @param titleFont Fuente para el título
     * @return La instancia actual para encadenamiento de métodos
     */
    public ChartGenerator withTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        return this;
    }
    
    /**
     * Crea un gráfico de torta a partir de datos tabulares
     * 
     * @param data Lista de arrays de strings con los datos
     * @param chartFilePath Ruta donde se guardará el archivo de imagen
     * @param labelIndex Índice de la columna a usar como etiqueta
     * @param valueIndex Índice de la columna a usar como valor
     * @param title Título del gráfico
     * @return true si el gráfico se generó correctamente, false en caso contrario
     */
    public boolean createPieChart(ArrayList<String[]> data, String chartFilePath, 
                                 int labelIndex, int valueIndex, String title) {
        if (data == null || data.isEmpty() || chartFilePath == null || chartFilePath.isEmpty()) {
            System.err.println("Error: Datos o ruta de archivo inválidos");
            return false;
        }
        
        try {
            // Validar que los índices sean válidos
            if (data.get(0).length <= Math.max(labelIndex, valueIndex)) {
                System.err.println("Error: Índices fuera de rango");
                return false;
            }
            
            // Crear el dataset para el gráfico
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            
            // Agrupar datos por etiqueta para evitar duplicados
            Map<String, Double> aggregatedData = new HashMap<>();
            
            // Empezar desde 1 para omitir la fila de encabezados
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                
                // Validar que los índices sean válidos para esta fila
                if (row.length <= Math.max(labelIndex, valueIndex)) {
                    continue; // Omitir filas con datos incompletos
                }
                
                String label = row[labelIndex];
                
                // Validar que el valor sea numérico
                try {
                    double value = Double.parseDouble(row[valueIndex]);
                    
                    // Agregar al mapa, sumando valores para etiquetas duplicadas
                    aggregatedData.put(label, aggregatedData.getOrDefault(label, 0.0) + value);
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Valor no numérico en fila " + i + ": " + row[valueIndex]);
                }
            }
            
            // Agregar datos agregados al dataset
            for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
                if (entry.getValue() > 0) { // Solo incluir valores positivos
                    dataset.setValue(entry.getKey(), entry.getValue());
                }
            }
            
            // Si no hay datos válidos, retornar false
            if (dataset.getItemCount() == 0) {
                System.err.println("Error: No hay datos válidos para generar el gráfico");
                return false;
            }
            
            // Crear el gráfico de torta
            JFreeChart pieChart;
            if (is3D) {
                pieChart = ChartFactory.createPieChart3D(
                        title,
                        dataset,
                        true, // Mostrar leyenda
                        true, // Mostrar tooltips
                        false // No generar URLs
                );
            } else {
                pieChart = ChartFactory.createPieChart(
                        title,
                        dataset,
                        true, // Mostrar leyenda
                        true, // Mostrar tooltips
                        false // No generar URLs
                );
            }
            
            // Personalizar el gráfico
            pieChart.setBackgroundPaint(backgroundColor);
            pieChart.getTitle().setFont(titleFont);
            
            // Personalizar el plot
            PiePlot<?> plot = (PiePlot<?>) pieChart.getPlot();
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            plot.setNoDataMessage("No hay datos disponibles");
            plot.setCircular(true);
            plot.setLabelGap(0.02);
            
            // Guardar el gráfico como archivo PNG
            ChartUtils.saveChartAsPNG(new File(chartFilePath), pieChart, width, height);
            System.out.println("Gráfico de torta guardado en: " + chartFilePath);
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar el gráfico: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error al generar el gráfico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Crea un gráfico de barras a partir de datos tabulares
     * 
     * @param data Lista de arrays de strings con los datos
     * @param chartFilePath Ruta donde se guardará el archivo de imagen
     * @param categoryIndex Índice de la columna a usar como categoría
     * @param valueIndex Índice de la columna a usar como valor
     * @param title Título del gráfico
     * @param xAxisLabel Etiqueta del eje X
     * @param yAxisLabel Etiqueta del eje Y
     * @return true si el gráfico se generó correctamente, false en caso contrario
     */
    public boolean createBarChart(ArrayList<String[]> data, String chartFilePath,
                                 int categoryIndex, int valueIndex, String title,
                                 String xAxisLabel, String yAxisLabel) {
        // Implementación similar a createPieChart pero para gráficos de barras
        // Se omite por brevedad, pero seguiría el mismo patrón
        return false; // Placeholder
    }
}