# Mejoras en el Sistema de Reportes

## Descripción General
Se han implementado mejoras significativas en el sistema de reportes para permitir consultas anidadas y generar reportes más detallados y útiles. Estas mejoras permiten generar reportes de inventario de productos, contratos detallados, estadísticas de servicios, incidencias por contrato y empleados por equipo de trabajo.

## Nuevos Tipos de Reportes

### 1. Reporte de Contrato
Muestra información detallada de un contrato, incluyendo datos del cliente, servicio, pagos y equipo de trabajo asignado.

**Comando:** `REPORT CONTRATO <ID_CONTRATO>`

**Ejemplo:** `REPORT CONTRATO 1`

### 2. Reporte de Inventario de Productos
Genera un reporte completo del inventario de productos, incluyendo detalles de cada producto, su stock y los servicios asociados. Incluye un gráfico de torta que muestra la distribución del stock.

**Comando:** `REPORT INVENTARIO`

### 3. Reporte de Estadísticas de Servicios
Muestra estadísticas detalladas de todos los servicios, incluyendo el número de contratos, ingresos totales y clientes por servicio.

**Comando:** `REPORT SERVICIOS`

### 4. Reporte de Incidencias por Contrato
Genera un reporte de todas las incidencias asociadas a un contrato específico, incluyendo detalles de cada incidencia, su estado y solución.

**Comando:** `REPORT INCIDENCIAS <ID_CONTRATO>`

**Ejemplo:** `REPORT INCIDENCIAS 1`

### 5. Reporte de Empleados por Equipo de Trabajo
Muestra todos los empleados asignados a un equipo de trabajo específico, incluyendo detalles de cada empleado y estadísticas de servicios y contratos.

**Comando:** `REPORT EMPLEADOS <ID_EQUIPO>`

**Ejemplo:** `REPORT EMPLEADOS 1`

## Características Técnicas

### Consultas Anidadas
Se han implementado consultas SQL anidadas para obtener información detallada y relacionada en un solo reporte. Estas consultas utilizan JOINs para relacionar diferentes tablas y obtener datos completos.

### Gráficos Estadísticos
Los reportes de inventario incluyen gráficos de torta generados con JFreeChart para visualizar la distribución del stock de productos.

### Formato PDF Mejorado
Los reportes se generan en formato PDF con mejor estructura, incluyendo encabezados, tablas formateadas y pies de página.

## Archivos Modificados

1. **DReporte.java**: Se agregaron nuevas consultas SQL anidadas y métodos para obtener diferentes tipos de datos.
2. **NReporte.java**: Se implementaron métodos para generar diferentes tipos de reportes.
3. **ReportCommand.java**: Se actualizó para soportar diferentes tipos de reportes a través de la interfaz de comandos.
4. **ReportGenerator.java**: Se mejoró para generar diferentes formatos de reportes PDF.

## Sugerencias para Futuros Reportes

1. **Reporte de Ventas por Período**: Mostrar estadísticas de ventas por día, semana, mes o año.
2. **Reporte de Rentabilidad de Servicios**: Analizar la rentabilidad de cada servicio considerando costos y precios.
3. **Reporte de Clientes Frecuentes**: Identificar y mostrar información de los clientes más frecuentes.
4. **Reporte de Productos Más Utilizados**: Mostrar los productos más utilizados en los servicios.
5. **Reporte de Incidencias por Tipo**: Analizar las incidencias agrupadas por tipo o prioridad.
6. **Reporte de Desempeño de Empleados**: Evaluar el desempeño de los empleados según los contratos y servicios atendidos.
7. **Reporte de Proyección de Ingresos**: Proyectar ingresos futuros basados en contratos activos.
8. **Reporte de Inventario Crítico**: Identificar productos con stock bajo que necesitan reposición.