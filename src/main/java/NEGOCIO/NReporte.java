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
            reporte.generarDocumento("DOCUMENTO DE CONTRATO",data ,path);
        }
        //return contrato;
        return data;
    }
}
