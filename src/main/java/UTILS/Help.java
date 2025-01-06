package UTILS;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author angela
 */
public class Help {

    public static final String ADD = "ADD";
    public static final String MOD = "MOD";
    public static final String DEL = "DEL";
    public static final String ACT = "ACT";
    public static final String DES = "DES";
    public static final String LIS = "LIS";
    public static final String VER = "VER";
    public static final String CAN = "CAN";
    public static final String REP = "REP";
    public static final String END = "END";

    // List de todas las tablas
    public static final String CLIENTE = "CLI";
    public static final String CONTRATOS = "CNTR";  
    public static final String EMPLEADO_EQUIPO_TRABAJO = "EMP_EQP_TRA"; 
    public static final String EMPLEADO = "EMP";
    public static final String EQUIPO_TRABAJO_SERVICIO = "EQP_TRA_SRV"; 
    public static final String EQUIPO_TRABAJO = "EQP_TRA"; 
    public static final String FACTURAS = "FACT"; 
    public static final String INCIDENCIAS = "INC";
    public static final String INVENTARIO = "INV";
    public static final String PRODUCTO_SERVICIOS = "PRO_SRV";
    public static final String PRODUCTO = "PRO";
    public static final String PROVEEDOR = "PRV";
    public static final String SERVICIO = "SRV";


    
    public static final String PATH = "/home/fpl/NetBeansProjects/COPERGRANOS/";


    //help
    public static final String HELP = "HELP";

    public static final int LENPARAM1 = 1;
    public static final int LENPARAM2 = 2;
    public static final int LENPARAM3 = 3;
    public static final int LENPARAM4 = 4;
    public static final int LENPARAM5 = 5;
    public static final int LENPARAM6 = 6;
    public static final int LENPARAM7 = 7;
    public static final int LENPARAM8 = 8;
    public static final int LENPARAM9 = 9;
    public static final int LENPARAM10 = 10;
    public static final String[] clienteHeader = {"ID", "CI","NOMBRE","TELEFONO","DIRECCION","TIPO CLIENTE", "CREADO"};
    public static final String[] contratoHeader = {"ID", "DESCRIPCION","PRECIO TOTAL","ESTADO","FECHA INICIO","FECHA FIN"};
    public static final String[] empleadoEquipoTrabajoHeader = {"ID", "EMPLEADO","EQUIPO TRABAJO","ESTADO"};
    public static final String[] empleadoHeader = {"ID", "CI","NOMBRE","TELEFONO","PUESTO","ESTADO", "CREADO"};
    public static final String[] equipoTrabajoServicioHeader = {"ID","EQUIPO TRABAJO","SERVICIO","ESTADO"};
    public static final String[] equipoTrabajoHeader = {"ID","EMPLEADO","NOMBRE","DESCRIPCION","ESTADO"};
    public static final String[] facturasHeader = {"ID","CONTRATO","PRECIO TOTAL","ESTADO","FECHA PAGO"};
    public static final String[] incidenciasHeader = {"ID","CLIENTE","DESCRIPCION","ESTADO"};
    public static final String[] inventarioHeader = {"ID", "PRODUCTO","TIPO MOVIMIENTO","CANTIDAD","DESCRIPCION"};
    public static final String[] productoServicioHeader = {"ID", "PRODUCTO","SERVICIO","CANTIDAD","DESCRIPCION"};
    public static final String[] productoHeader = {"ID", "NOMBRE","DESCRIPCION","PRECIO","STOCK"};
    public static final String[] proveedorHeader = {"ID", "CI","NOMBRE","TELEFONO","DIRECCION"};
    public static final String[] servicioHeader = {"ID", "NOMBRE","DESCRIPCION","PRECIO","FRECUENCIA","ESTADO"};


    public static String ContenidoHelp() {
        return table();
    }

    public static String errorMensaje(String title, String error, String comando) {
        return "Content-Type: text/html; charset=\"UTF-8\" \r\n\r\n"
                + "<h1 >!!! OCURRIO UN ERROR !!! </h1>"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n"
                + "<tr> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> TITULO </th> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> ERROR </th> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> COMANDO </th> \r\n\r\n"
                + "</tr> \r\n\r\n"
                + "<tr> \r\n\r\n"
                + "<td style=\"text-align: left; padding: 8px; border: 1px solid black;\">" + title + "</td> \r\n\r\n"
                + "<td style=\"text-align: left; padding: 8px; border: 1px solid black;\"> " + error + " </td> \r\n\r\n"
                + "<td style=\"text-align: left; padding: 8px; border: 1px solid black;\"> " + comando + ";</td> \r\n\r\n"
                + "</tr> \r\n\r\n"
                + "<tr> \r\n\r\n"
                + "</table>"
                + "<h3 style=\"color:red\"> PARA MAS AYUDA ENVIE UN CORREO CON LA PALABRA <b> HELP </b> </h3>";
    }
    
    public static String listMensajeToPdf(String title, String[] header, List<String[]> listaObject) {
        String response = "<h1>" + title.toUpperCase() + " - HELP</h1>"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n";
        response += trStart;
        for (String head : header) {
            System.out.println("HEADER : "+head);
            response += thHeader(head);
        }
        response += trEnd;

        for (String[] cadenas : listaObject) {
            response += trStart;
            for (String cad : cadenas) {
                System.out.println("OBJETOS : "+cad);
                response += td(cad);
            }
            response += trEnd;
        }
        response += footerTable();
        return response;
    }
    

    public static String listMensaje(String title, String[] header, List<String[]> listaObject) {
        String response = "Content-Type: text/html; charset=\"UTF-8\" \r\n\r\n"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n";
        response += trStart;
        for (String head : header) {
            response += thHeader(head);
        }
        response += trEnd;

        for (String[] cadenas : listaObject) {
            response += trStart;
            for (String cad : cadenas) {
                response += td(cad);
            }
            response += trEnd;
        }
        response += footerTable();
        return response;
    }

    public static String ver(String title, String[] header, String[] cad) {
        String response = "Content-Type: text/html; charset=\"UTF-8\" \r\n\r\n"
                + "<h1>" + title.toUpperCase() + " - HELP</h1>"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n";
        response += trStart;
        for (String head : header) {
            response += thHeader(head);
        }
        response += trEnd;
        response += trStart;
        for (String c : cad) {
            response += td(c);
        }
        response += trEnd;
        response += footerTable();
        return response;
    }

    private static String thHeader(String title) {
        return "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> " + title + " </th> \r\n\r\n";
    }

    private static String headerTable(String title) {
        return "Content-Type: text/html; charset=\"UTF-8\" \r\n\r\n"
                + "<h1>" + title.toUpperCase() + " - HELP</h1>"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n"
                + "<tr> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> CASOS DE USOS </th> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> METODOS </th> \r\n\r\n"
                + "<th style=\"text-align: center; padding: 8px; background-color: green; color: white; border: 1px solid black;\"> COMANDOS </th> \r\n\r\n"
                + "</tr> \r\n\r\n";
    }

    private static String trStart = "<tr> \r\n\r\n";
    private static String trEnd = "</tr> \r\n\r\n";

    private static String table() {
        String header = headerTable("SISTEMA VIA EMAIL".toUpperCase());
        List<CasosUso> listCasosUso = getCasosUso();
        String body = "";
        for (CasosUso casoUso : listCasosUso) {
            String title = casoUso.titulo;
            for (Option option : casoUso.options) {
                body += trStart;
                body += td(title);
                body += td(option.title);
                body += td(option.parametros);
                body += trEnd;
            }
        }
        header += body;
        header += footerTable();
        return header;
    }

    private static String td(String content) {
        return "<td style=\"text-align: left; padding: 8px; border: 1px solid black;\"> " + content + " </td> \r\n\r\n";
    }

    private static String footerTable() {
        return "<tr> \r\n\r\n"
                + "</table>";
    }

    private static List<CasosUso> getCasosUso() {
        List<CasosUso> casos = new LinkedList<>();
        
        //CLIENTE #1
        CasosUso cliente = new CasosUso("CU1. GESTIONAR CLIENTE ");
        cliente.addCaso(new Option("GUARDAR ", CLIENTE + "_ADD[ci,nombre,direccion,telefono,tipo_cliente]; | Ej.: CLI_ADD[123456789,JOSE PEDRO,CALLE FALSA #3,123456789,(RESIDENCIAL, COMERCIAL, INDUSTRIAL, INSTITUCIONAL, EVENTOS, MANTENIMIENTO)]"));
        cliente.addCaso(new Option("MODIFICAR ", CLIENTE + "_MOD[id,ci,nombre,direccion,telefono,tipo_cliente]; | Ej.: CLI_MOD[1,123456789,JOSE PEDRO,CALLE FALSA #3,123456789,(RESIDENCIAL, COMERCIAL, INDUSTRIAL, INSTITUCIONAL, EVENTOS, MANTENIMIENTO)]"));
        cliente.addCaso(new Option("ELIMINAR ", CLIENTE + "_DEL[id];"));
        cliente.addCaso(new Option("VER ", CLIENTE + "_VER[id];"));
        cliente.addCaso(new Option("LISTAR ", CLIENTE + "_LIS[];"));
        
        //EMPLEADO #1
        CasosUso empleado = new CasosUso("CU1. GESTIONAR EMPLEADO ");
        empleado.addCaso(new Option("GUARDAR ", EMPLEADO + "_ADD[ci,nombre,telefono,puesto,estado]; | Ej.: EMP_ADD[123456789,JOSE PEDRO,123456789,(OPERARIO DE LIMPIEZA, GERENTE GENERAL, SUPERVISOR DE LIMPIEZA),(ACTIVO', INACTIVO, SUSPENDIDO)]"));
        empleado.addCaso(new Option("MODIFICAR ", EMPLEADO + "_MOD[id,ci,nombre,direccion,telefono,tipo_cliente]; | Ej.: EMP_MOD[1,123456789,JOSE PEDRO,123456789,(OPERARIO DE LIMPIEZA, GERENTE GENERAL, SUPERVISOR DE LIMPIEZA),(ACTIVO', INACTIVO, SUSPENDIDO)]"));
        empleado.addCaso(new Option("ELIMINAR " , EMPLEADO + "_DEL[id];"));
        empleado.addCaso(new Option("VER ", EMPLEADO + "_VER[id];"));
        empleado.addCaso(new Option("LISTAR ", EMPLEADO + "_LIS[];"));
        
        //PROVEEDOR #1
        CasosUso proveedor = new CasosUso("CU1. GESTIONAR PROVEEDOR ");
        proveedor.addCaso(new Option("GUARDAR ", PROVEEDOR + "_ADD[ci,nombre,telefono,direccion]; | Ej.: "+PROVEEDOR+"_ADD[123456789,JOSE PEDRO,123456789]"));
        proveedor.addCaso(new Option("MODIFICAR ", PROVEEDOR + "_MOD[id,ci,nombre,telefono,direccion]; | Ej.:"+PROVEEDOR+"_MOD[1,123456789,JOSE PEDRO,123456789]"));
        proveedor.addCaso(new Option("ELIMINAR ", PROVEEDOR + "_DEL[id];"));
        proveedor.addCaso(new Option("VER ", PROVEEDOR + "_VER[id];"));
        proveedor.addCaso(new Option("LISTAR ", PROVEEDOR + "_LIS[];"));
        
        //PRODUCTO #2
        CasosUso producto = new CasosUso("CU2. GESTIONAR PRODUCTO ");
        producto.addCaso(new Option("GUARDAR ", PRODUCTO + "_ADD[nombre,descripcion,precio,stock]; | "+PRODUCTO+"_ADD[PRODUCTO,DETALLE PRODUCTO,0,0]"));
        producto.addCaso(new Option("MODIFICAR ", PRODUCTO + "_MOD[id,nombre,descripcion,precio,stock]; | "+PRODUCTO+"_MOD[1,PRODUCTO,DETALLE PRODUCTO,0,0]"));
        producto.addCaso(new Option("ELIMINAR ", PRODUCTO + "_DEL[id];"));
        producto.addCaso(new Option("VER ", PRODUCTO + "_VER[id];"));
        producto.addCaso(new Option("LISTAR ", PRODUCTO + "_LIS[];"));
        
        
        //INVENTARIO #3
        CasosUso inventario = new CasosUso("CU3. GESTIONAR INVENTARIO ");
        inventario.addCaso(new Option("GUARDAR ", INVENTARIO + "_ADD[producto_id,tipo_movimiento,cantidad,descripcion]; | "+INVENTARIO+"_ADD[1,(INGRESO, SALIDA),0, DESCRIPCION DEL INVENTARIO]"));
        inventario.addCaso(new Option("MODIFICAR ", INVENTARIO + "_MOD[id,producto_id,tipo_movimiento,cantidad,descripcion]; | "+INVENTARIO+"_MOD[1,1,(INGRESO, SALIDA),0, DESCRIPCION DEL INVENTARIO]"));
        inventario.addCaso(new Option("ELIMINAR ", INVENTARIO + "_DEL[id];"));
        inventario.addCaso(new Option("VER ", INVENTARIO + "_VER[id];"));
        inventario.addCaso(new Option("LISTAR ", INVENTARIO + "_LIS[];"));
        
        //SERVICIOS #4
        CasosUso servicio = new CasosUso("CU4. GESTIONAR SERVICIO ");
        servicio.addCaso(new Option("GUARDAR ", SERVICIO + "_ADD[nombre,descripcion,precio,frecuencia,estado];"+SERVICIO+"_ADD[SERVICIO #1,DESCRIPCION,0,(DIARIA, SEMANAL, MENSUAL),0,(ACTIVO, DESACTIVO)]"));
        servicio.addCaso(new Option("MODIFICAR ", SERVICIO + "_MOD[id,producto_id,tipo_movimiento,cantidad,descripcion]; | "+SERVICIO+"_MOD[1SERVICIO #1,DESCRIPCION,0,(DIARIA, SEMANAL, MENSUAL),0,(ACTIVO, DESACTIVO)]"));
        servicio.addCaso(new Option("ELIMINAR ", SERVICIO + "_DEL[id];"));
        servicio.addCaso(new Option("VER ", SERVICIO + "_VER[id];"));
        servicio.addCaso(new Option("LISTAR ", SERVICIO + "_LIS[];"));
        
        //CONTRATOS #5
        CasosUso contratos = new CasosUso("CU5. GESTIONAR CONTRATOS ");
        contratos.addCaso(new Option("GUARDAR ", CONTRATOS + "_ADD[descripcion, precio_total, estado, fecha_inicio, fecha_fin];"+CONTRATOS+"_ADD[DESCRIPCION CONTRATO,0,(DIARIA, SEMANAL, MENSUAL),0,(ACTIVO, FINALIZADO,DESACTIVO), yyyy-mm-dd, yyyy-mm-ddd]"));
        contratos.addCaso(new Option("MODIFICAR ", CONTRATOS + "_MOD[id,descripcion, precio_total, estado, fecha_inicio, fecha_fin]; | "+CONTRATOS+"_MOD[1,DESCRIPCION CONTRATO,0,(DIARIA, SEMANAL, MENSUAL),0,(ACTIVO, FINALIZADO,DESACTIVO), yyyy-mm-dd, yyyy-mm-ddd]"));
        contratos.addCaso(new Option("ELIMINAR ", CONTRATOS + "_DEL[id];"));
        contratos.addCaso(new Option("VER ", CONTRATOS + "_VER[id];"));
        contratos.addCaso(new Option("LISTAR ", CONTRATOS + "_LIS[];"));
        
        //FACTURACION #5
        CasosUso factura = new CasosUso("CU5. GESTIONAR FACTURA ");
        factura.addCaso(new Option("GUARDAR ", FACTURAS + "_ADD[contrato_id, precio_total, estado];"+CONTRATOS+"_ADD[1,1,0,(PENDIENTE, PAGADO, CANCELADO)]"));
        factura.addCaso(new Option("MODIFICAR ", FACTURAS + "_MOD[id,contrato_id, precio_total, estado]; | "+CONTRATOS+"_MOD[1,1,0,(PENDIENTE, PAGADO, CANCELADO)]"));
        factura.addCaso(new Option("ELIMINAR ", FACTURAS + "_DEL[id];"));
        factura.addCaso(new Option("VER ", FACTURAS + "_VER[id];"));
        factura.addCaso(new Option("LISTAR ", FACTURAS + "_LIS[];"));
        
        
        //EQUIPOS DE TRABAJO #6
        CasosUso equipo_trabajo = new CasosUso("CU6. GESTIONAR EQUIPO DE TRABAJO ");
        equipo_trabajo.addCaso(new Option("GUARDAR ", EQUIPO_TRABAJO + "_ADD[empleado_id, nombre del equipo, descripcion, estado];"+EQUIPO_TRABAJO+"_ADD[1,LIMPIEZA #1,DESCRIPCION DEL GRUPO, (ACTIVO, INACTIVO)]"));
        equipo_trabajo.addCaso(new Option("MODIFICAR ", EQUIPO_TRABAJO + "_MOD[id,contrato_id, precio_total, estado]; | "+EQUIPO_TRABAJO+"_MOD[1,1,LIMPIEZA #1,DESCRIPCION DEL GRUPO, (ACTIVO, INACTIVO)]"));
        equipo_trabajo.addCaso(new Option("ELIMINAR ", EQUIPO_TRABAJO + "_DEL[id];"));
        equipo_trabajo.addCaso(new Option("VER ", EQUIPO_TRABAJO + "_VER[id];"));
        equipo_trabajo.addCaso(new Option("LISTAR ", EQUIPO_TRABAJO + "_LIS[];"));
        
        //INCIDENCIAS Y QUEJAS #7
        CasosUso incidencias = new CasosUso("CU7. GESTIONAR INCIDENCIAS Y QUEJAS");
        incidencias.addCaso(new Option("GUARDAR ", EQUIPO_TRABAJO + "_ADD[cliente_id, descripcion, estado];"+EQUIPO_TRABAJO+"_ADD[1,DESCRIPCION DEL INCIDENTE, (PENDIENTE, RESUELTA)]"));
        incidencias.addCaso(new Option("MODIFICAR ", EQUIPO_TRABAJO + "_MOD[id,cliente_id, descripcion, estado]; | "+EQUIPO_TRABAJO+"_MOD[1,1,DESCRIPCION DEL INCIDENTE, (PENDIENTE, RESUELTA)]"));
        incidencias.addCaso(new Option("ELIMINAR ", EQUIPO_TRABAJO + "_DEL[id];"));
        incidencias.addCaso(new Option("VER ", EQUIPO_TRABAJO + "_VER[id];"));
        incidencias.addCaso(new Option("LISTAR ", EQUIPO_TRABAJO + "_LIS[];"));
        
        /* //REPORTES #1
        CasosUso reporte = new CasosUso("CU1. GESTIONAR REPORTES ");
        reporte.addCaso(new Option("CONTROL DE STOCK", STOCK + "_REP[];"));
        reporte.addCaso(new Option("CANTIDAD Y MONTO DE PRODUCTOS VENDIDODOS", PRODUCTO_VENDIDO + "_REP[];"));*/
        casos.add(cliente);        
        casos.add(empleado);
        casos.add(proveedor);        
        casos.add(producto);
        casos.add(inventario);
        casos.add(servicio);
        casos.add(contratos);
        casos.add(equipo_trabajo);
        casos.add(incidencias);
        return casos;
    }


}
