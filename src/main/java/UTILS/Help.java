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

    // List de todos las tablas
    public static final String CLIENTE = "CLI";
    public static final String CONTRATOS = "CNTR";  
    public static final String EMPLEADO_EQUIPO_TRABAJO = "EMP_EQP_TRA"; 
    public static final String EMPLEADO = "EMP";
    public static final String EQUIPO_TRABAJO_SERVICIO = "EQP_TRA_SRV"; 
    public static final String EQUIPO_TRABAJO = "EQP_TRA";
    public static final String INCIDENCIAS = "INC";
    public static final String CONTRATO_INCIDENCIA = "CNTR_INC";
    public static final String PRODUCTO_SERVICIOS = "PRO_SRV";
    public static final String PRODUCTO = "PRO";
    public static final String SERVICIO = "SRV";
    public static final String PATH = "D:\\PROYECTOSC\\proyectosc"; //NO OLVIDAR CAMBIAR EN PRODUCCION
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
    public static final String[] clienteHeader = {"ID", "CI","NOMBRE","TELEFONO","DIRECCION","TIPO CLIENTE","CREADO", "ACTUALIZADO"};
    public static final String[] contratoHeader = {
            "ID CONTRATO",
            "DESCRIPCION",
            "PRECIO TOTAL",
            "ESTADO",
            "FECHA INICIO",
            "FECHA FIN",
            "CREADO",
            "ACTUALIZADO",
            "ID CLIENTE",
            "ID SERVICIO"
    };
    public static final String[] empleadoHeader = {"ID", "CI","NOMBRE","TELEFONO","PUESTO","ESTADO", "CREADO", "ACTUALIZADO"};
    public static final String[] contratoIncidenciaHeader = {"ID", "ID CONTRATO","ID INCIDENCIA","ESTADO", "CREADO", "ACTUALIZADO"};
    public static final String[] empleadoEquipoTrabajoHeader = {"ID", "ID EMPLEADO","ID EQUIPO TRABAJO","ESTADO", "OCUPACION", "CREADO", "ACTUALIZADO"};
    public static final String[] equipoTrabajoServicioHeader = {"ID","ID EQUIPO TRABAJO","ID SERVICIO","ESTADO", "CREADO", "ACTUALIZADO"};
    public static final String[] equipoTrabajoHeader = {"ID","NOMBRE","DESCRIPCION","ESTADO", "CREADO", "ACTUALIZADO"};
    public static final String[] incidenciasHeader = {"ID","NOMBRE","DESCRIPCION","CREADO","ACTUALIZADO"};
    public static final String[] productoServicioHeader = {"ID","CANTIDAD","CREADO","ACTUALIZADO"};
    public static final String[] productoHeader = {"ID", "NOMBRE","DESCRIPCION","PRECIO","STOCK", "CREADO", "ACTUALIZADO"};
    public static final String[] servicioHeader = {"ID", "NOMBRE","DESCRIPCION","PRECIO","FRECUENCIA","ESTADO", "CREADO", "ACTUALIZADO"};
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
    public static String verWithReport(String title, String[] data) {
        String response = "<h1>" + title.toUpperCase() + " - HELP</h1>"
                + "<table style=\" border-collapse: collapse; width: 100%; border: 1px solid black;\"> \r\n\r\n";
        for (String d : data) {
            response += "<p>"+d+"</p>";
        }
        return response;
    }
    public static String verListWithReport(String title, String[] header, String[] cad) {
        String response = "<h1>" + title.toUpperCase() + " - HELP</h1>"
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
        cliente.addCaso(new Option("<b>GUARDAR CLIENTE</b> ", CLIENTE + "_ADD[ci,nombre,telefono,direccion,tipo_cliente]; | <b>Ej.: CLI_ADD[123456789,JOSE PEDRO,123456789,CALLE FALSA #3,(RESIDENCIAL, COMERCIAL, INDUSTRIAL, INSTITUCIONAL, EVENTOS, MANTENIMIENTO)]</b>"));
        cliente.addCaso(new Option("MODIFICAR CLIENTE", CLIENTE + "_MOD[id,ci,nombre,telefono,direccion,tipo_cliente]; | <b>Ej.: CLI_MOD[1,123456789,JOSE PEDRO,123456789,CALLE FALSA #3,(RESIDENCIAL, COMERCIAL, INDUSTRIAL, INSTITUCIONAL, EVENTOS, MANTENIMIENTO)]</b>"));
        cliente.addCaso(new Option("ELIMINAR CLIENTE", CLIENTE + "_DEL[id];"));
        cliente.addCaso(new Option("VER CLIENTE", CLIENTE + "_VER[id];"));
        cliente.addCaso(new Option("LISTAR CLIENTE", CLIENTE + "_LIS[];"));
        
        //EMPLEADO #1
        CasosUso empleado = new CasosUso("CU1. GESTIONAR EMPLEADO ");
        empleado.addCaso(new Option("<b>GUARDAR EMPLEADOS</b> ", EMPLEADO + "_ADD[ci,nombre,telefono,puesto,estado]; | <b>Ej.: EMP_ADD[123456789,JOSE PEDRO,123456789,(OPERARIO DE LIMPIEZA, GERENTE GENERAL, SUPERVISOR DE LIMPIEZA),(ACTIVO', INACTIVO, SUSPENDIDO)]</b>"));
        empleado.addCaso(new Option("MODIFICAR EMPLEADOS", EMPLEADO + "_MOD[id,ci,nombre,telefono,puesto,estado]; | <b>Ej.: EMP_MOD[1,123456789,JOSE PEDRO,123456789,(OPERARIO DE LIMPIEZA, GERENTE GENERAL, SUPERVISOR DE LIMPIEZA),(ACTIVO', INACTIVO, SUSPENDIDO)]</b>"));
        empleado.addCaso(new Option("ELIMINAR EMPLEADOS" , EMPLEADO + "_DEL[id];"));
        empleado.addCaso(new Option("VER EMPLEADOS", EMPLEADO + "_VER[id];"));
        empleado.addCaso(new Option("LISTAR EMPLEADOS", EMPLEADO + "_LIS[];"));
        
        //PRODUCTO #2
        CasosUso producto = new CasosUso("CU2. GESTIONAR PRODUCTO ");
        producto.addCaso(new Option("<b>GUARDAR PRODUCTO</b> ", PRODUCTO + "_ADD[nombre,descripcion,precio,stock]; | <b>Ej.: "+PRODUCTO+"_ADD[PRODUCTO,DETALLE PRODUCTO,0,0]</b>"));
        producto.addCaso(new Option("MODIFICAR PRODUCTO", PRODUCTO + "_MOD[id,nombre,descripcion,precio,stock]; | <b>Ej.: "+PRODUCTO+"_MOD[1,PRODUCTO,DETALLE PRODUCTO,0,0]</b>"));
        producto.addCaso(new Option("ELIMINAR PRODUCTO", PRODUCTO + "_DEL[id];"));
        producto.addCaso(new Option("VER PRODUCTO", PRODUCTO + "_VER[id];"));
        producto.addCaso(new Option("LISTAR PRODUCTO", PRODUCTO + "_LIS[];"));

        //PRODUCTO SERVICIO #2
        CasosUso producto_servicio = new CasosUso("CU2. GESTIONAR PRODUCTO SERVICIO");
        producto_servicio.addCaso(new Option("<b>GUARDAR SERVICIO</b> ", PRODUCTO_SERVICIOS + "_ADD[producto_id, servicio_id, cantidad]; | <b>Ej.: "+EQUIPO_TRABAJO_SERVICIO+"_ADD[1,1,10]</b>"));
        producto_servicio.addCaso(new Option("MODIFICAR SERVICIO", PRODUCTO_SERVICIOS + "_MOD[id,cantidad]; | <b>Ej.: "+PRODUCTO_SERVICIOS+"_MOD[1,10]</b>"));
        producto_servicio.addCaso(new Option("ELIMINAR SERVICIO", PRODUCTO_SERVICIOS + "_DEL[id];"));
        producto_servicio.addCaso(new Option("VER SERVICIO", PRODUCTO_SERVICIOS + "_VER[id];"));
        producto_servicio.addCaso(new Option("LISTAR SERVICIO", PRODUCTO_SERVICIOS + "_LIS[];"));
        
        //SERVICIOS #4
        CasosUso servicio = new CasosUso("CU4. GESTIONAR SERVICIO ");
        servicio.addCaso(new Option("<b>GUARDAR SERVICIO</b> ", SERVICIO + "_ADD[nombre,descripcion,precio,frecuencia,estado]; | <b>Ej.: "+SERVICIO+"_ADD[SERVICIO #1,DESCRIPCION,10,(DIARIA, SEMANAL, MENSUAL),(ACTIVO, DESACTIVO)]</b>"));
        servicio.addCaso(new Option("MODIFICAR SERVICIO", SERVICIO + "_MOD[id,nombre,descripcion,precio,frecuencia,estado]; | <b>Ej.: "+SERVICIO+"_MOD[1,SERVICIO #1,DESCRIPCION,0,(DIARIA, SEMANAL, MENSUAL),(ACTIVO, DESACTIVO)]</b>"));
        servicio.addCaso(new Option("ELIMINAR SERVICIO", SERVICIO + "_DEL[id];"));
        servicio.addCaso(new Option("VER SERVICIO", SERVICIO + "_VER[id];"));
        servicio.addCaso(new Option("LISTAR SERVICIO", SERVICIO + "_LIS[];"));

        //EQUIPO TRABAJO SERVICIO #4
        CasosUso equipo_trabajo_servicio = new CasosUso("CU4. GESTIONAR EQUIPO DE TRABAJO SERVICIO");
        equipo_trabajo_servicio.addCaso(new Option("<b>GUARDAR EQUIPO DE TRABAJO AL SERVICIO</b> ", EQUIPO_TRABAJO_SERVICIO + "_ADD[equipo_trabajo_id, servicio_id, estado]; | <b>Ej.: "+EQUIPO_TRABAJO_SERVICIO+"_ADD[1,1,(ASIGNADO, RETIRADO)]</b>"));
        equipo_trabajo_servicio.addCaso(new Option("MODIFICAR EQUIPO DE TRABAJO AL SERVICIO", EQUIPO_TRABAJO_SERVICIO + "_MOD[id,estado]; | <b>Ej.: "+EQUIPO_TRABAJO_SERVICIO+"_MOD[1,(ASIGNADO, RETIRADO)]</b>"));
        equipo_trabajo_servicio.addCaso(new Option("ELIMINAR EQUIPO DE TRABAJO AL SERVICIO", EQUIPO_TRABAJO_SERVICIO + "_DEL[id];"));
        equipo_trabajo_servicio.addCaso(new Option("VER EQUIPO DE TRABAJO AL SERVICIO", EQUIPO_TRABAJO_SERVICIO + "_VER[id];"));
        equipo_trabajo_servicio.addCaso(new Option("LISTAR EQUIPO DE TRABAJO AL SERVICIO", EQUIPO_TRABAJO_SERVICIO + "_LIS[];"));
        
        //CONTRATOS #5
        CasosUso contratos = new CasosUso("CU5. GESTIONAR CONTRATOS ");
        contratos.addCaso(new Option("<b>GUARDAR CONTRATOS</b> ", CONTRATOS + "_ADD[descripcion, precio_total, estado, fecha_inicio, fecha_fin, cliente_id, servicio_id, equipo_trabajo_id, empleado_id]; | <b>Ej.: "+CONTRATOS+"_ADD[DESCRIPCION CONTRATO,0,(INICIADO, ACTIVO, FINALIZADO,DESACTIVO), yyyy-mm-dd, yyyy-mm-dd, ID DEL CLIENTE, ID DEL TIPO DE SERVICIO, ID DEL EQUIPO DE TRABAJO, ID DEL EMPLEADO]</b>"));
        contratos.addCaso(new Option("ELIMINAR CONTRATOS", CONTRATOS + "_DEL[id];"));
        contratos.addCaso(new Option("VER CONTRATOS", CONTRATOS + "_VER[id];"));
        contratos.addCaso(new Option("LISTAR CONTRATOS", CONTRATOS + "_LIS[];"));
        
        //EQUIPOS DE TRABAJO #6
        CasosUso equipo_trabajo = new CasosUso("CU6. GESTIONAR EQUIPO DE TRABAJO ");
        equipo_trabajo.addCaso(new Option("<b>GUARDAR EQUIPO DE TRABAJO</b> ", EQUIPO_TRABAJO + "_ADD[empleado_id, nombre del equipo, descripcion, estado]; | <b>Ej.: "+EQUIPO_TRABAJO+"_ADD[1,LIMPIEZA #1,DESCRIPCION DEL GRUPO, (ACTIVO, INACTIVO)]</b>"));
        equipo_trabajo.addCaso(new Option("MODIFICAR EQUIPO DE TRABAJO", EQUIPO_TRABAJO + "_MOD[id, nombre del equipo, descripcion, estado]; | <b>Ej.: "+EQUIPO_TRABAJO+"_MOD[1,LIMPIEZA #1,DESCRIPCION DEL GRUPO, (ACTIVO, INACTIVO)]</b>"));
        equipo_trabajo.addCaso(new Option("ELIMINAR EQUIPO DE TRABAJO", EQUIPO_TRABAJO + "_DEL[id];"));
        equipo_trabajo.addCaso(new Option("VER EQUIPO DE TRABAJO", EQUIPO_TRABAJO + "_VER[id];"));
        equipo_trabajo.addCaso(new Option("LISTAR EQUIPO DE TRABAJO", EQUIPO_TRABAJO + "_LIS[];"));

        // EMPLEADO A EQUIPO DE TRABAJO #6
        CasosUso empleado_equipo_trabajo = new CasosUso("CU6. GESTIONAR EMPLEADO A EQUIPO DE TRABAJO ");
        empleado_equipo_trabajo.addCaso(new Option("<b>GUARDAR EMPLEADO A EQUIPO DE TRABAJO</b> ", EMPLEADO_EQUIPO_TRABAJO + "_ADD[empleado_id, equipo_trabajo_id, estado]; | <b>Ej.: "+EMPLEADO_EQUIPO_TRABAJO+"_ADD[1,1,(ASIGNADO, RETIRADO)]</<b>"));
        empleado_equipo_trabajo.addCaso(new Option("MODIFICAR EMPLEADO A EQUIPO DE TRABAJO", EMPLEADO_EQUIPO_TRABAJO + "_MOD[id,estado]; | <b>Ej.: "+EMPLEADO_EQUIPO_TRABAJO+"_MOD[1,(ASIGNADO, RETIRADO)]</b>"));
        empleado_equipo_trabajo.addCaso(new Option("ELIMINAR EMPLEADO A EQUIPO DE TRABAJO", EMPLEADO_EQUIPO_TRABAJO + "_DEL[id];"));
        empleado_equipo_trabajo.addCaso(new Option("VER EMPLEADO A EQUIPO DE TRABAJO", EMPLEADO_EQUIPO_TRABAJO + "_VER[id];"));
        empleado_equipo_trabajo.addCaso(new Option("LISTAR EMPLEADO A EQUIPO DE TRABAJO", EMPLEADO_EQUIPO_TRABAJO + "_LIS[];"));
        
        //INCIDENCIAS Y QUEJAS #7
        CasosUso incidencias = new CasosUso("CU7. GESTIONAR INCIDENCIAS Y QUEJAS");
        incidencias.addCaso(new Option("<b>GUARDAR INCIDENCIAS Y QUEJAS</b> ", INCIDENCIAS + "_ADD[cliente_id, contrato_id, descripcion, estado]; | <b>Ej.: "+EQUIPO_TRABAJO+"_ADD[1,1,DESCRIPCION DEL INCIDENTE, (PENDIENTE, RESUELTA)]</b>"));
        incidencias.addCaso(new Option("MODIFICAR INCIDENCIAS   Y QUEJAS", INCIDENCIAS + "_MOD[id, descripcion, estado, fecha_resolucion]; | <b>Ej.: "+EQUIPO_TRABAJO+"_MOD[1,1,DESCRIPCION DEL INCIDENTE, (PENDIENTE, RESUELTA)]</b>"));
        incidencias.addCaso(new Option("ELIMINAR INCIDENCIAS Y QUEJAS", INCIDENCIAS + "_DEL[id];"));
        incidencias.addCaso(new Option("VER INCIDENCIAS Y QUEJAS", INCIDENCIAS + "_VER[id];"));
        incidencias.addCaso(new Option("LISTAR INCIDENCIAS Y QUEJAS", INCIDENCIAS + "_LIS[];"));

        //CONTRATOS INCIDENCIAS #7
        CasosUso contratos_incidencias = new CasosUso("CU7. GESTIONAR CONTRATOS INCIDENCIAS");
        contratos_incidencias.addCaso(new Option("<b>GUARDAR CONTRATOS INCIDENCIAS</b> ", CONTRATO_INCIDENCIA+"_ADD[contrato_id, incidencia_id, estado]; | <b>Ej.: CNTR_INC_ADD[1,1,(PENDIENTE, RESUELTA)]</b>"));
        contratos_incidencias.addCaso(new Option("MODIFICAR CONTRATOS INCIDENCIAS", CONTRATO_INCIDENCIA+"_MOD[id, estado]; | <b>Ej.: CNTR_INC_MOD[1,(PENDIENTE, RESUELTA)]</b>"));
        contratos_incidencias.addCaso(new Option("ELIMINAR CONTRATOS INCIDENCIAS", CONTRATO_INCIDENCIA+"_DEL[id];"));
        contratos_incidencias.addCaso(new Option("VER CONTRATOS INCIDENCIAS", CONTRATO_INCIDENCIA+"_VER[id];"));
        contratos_incidencias.addCaso(new Option("LISTAR CONTRATOS INCIDENCIAS", CONTRATO_INCIDENCIA+"_LIS[];"));

        //REPORTES #8
        CasosUso reporte = new CasosUso("CU8. GESTIONAR REPORTES ");
        reporte.addCaso(new Option("<b>GENERAR CONTRATO</b>", "REP_CONTRATOS[CONTRATO_ID];"));

        casos.add(cliente);        
        casos.add(empleado);
        casos.add(producto);
        casos.add(producto_servicio);
        casos.add(servicio);
        casos.add(equipo_trabajo_servicio);
        casos.add(contratos);
        casos.add(equipo_trabajo);
        casos.add(empleado_equipo_trabajo);
        casos.add(incidencias);
        casos.add(contratos_incidencias);
        casos.add(reporte);
        return casos;
    }
}
