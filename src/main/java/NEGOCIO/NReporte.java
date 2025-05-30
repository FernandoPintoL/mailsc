/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NEGOCIO;

import DATA.DReporte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fpl
 */
public class NReporte {

    DReporte DATA;
    ReportGenerator reporte;

    public String[] contrato(int id, String path) throws SQLException {
        DATA = new DReporte();
        reporte = new ReportGenerator();
        String[] data = DATA.contrato(id);
        if(data != null){
            /*String[] encabezado = {
                    "ID CONTRATO",
                    "FECHA INICIO",
                    "FECHA FIN",
                    "CONTRATO MONTO",
                    "CONTRATO ESTADO",
                    "NOMBRE CLIENTE",
                    "CI CLIENTE",
                    "TELEFONO CLIENTE",
                    "DIRECCION CLIENTE",
                    "TIPO CLIENTE",
                    "NOMBRE SERVICIO",
                    "DESCRIPCION SERVICIO",
                    "PRECIO SERVICIO",
                    "FRECUENCIA SERVICIO",
                    "ESTADO SERVICIO",
                    "NOMBRE EQUIPO DE TRABAJO",
                    "CI EMPLEADO",
                    "NOMBRE DEL EMPLEADO"
            };
            query_contrato.addFirst(encabezado);*/
            reporte.generarDocumento("DOCUMENTO DE CONTRATO",data ,path);
        }
        //return contrato;
        return data;
    }
}
