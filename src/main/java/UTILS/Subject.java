/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UTILS;


import java.util.LinkedList;
import java.util.List;
import OBJECT.Mensaje;
/**
 *
 * @author angela
 */
public class Subject {
    
 public static Mensaje subject(String subject,String emisor){
        if(!verificarExpresion(subject)){
            return null;
        }
        Mensaje sms = new Mensaje();
        sms.setEmisor(emisor);
        String table = sacarTable(subject);
        String actions = sacarAction(subject);
        List<String> parametros = sacarParametros(subject);
        
        sms.setTable(table);
        sms.setAction(actions);
        sms.setParametros(parametros);
        return sms;
    }

    private static String sacarTable(String subject) {
        String[] parts = subject.split("_");
        String table = parts[0];
        return table.trim().toUpperCase();
    }

   

    private static List<String> sacarParametros(String subject) {
        List<String> lista = new LinkedList<>();     
        int start = subject.indexOf("[");
        int end = subject.indexOf("]");
        String cadena = subject.substring(start+1, end);
        String[] parts = cadena.split(",");
        for (String part : parts) {
            if(part != String.valueOf("")){
                lista.add(part);
            }
        }
        System.out.println("start: "+start + " end : " + end + " cadena: " + cadena);
        return lista;
    }

    private static String sacarAction(String subject) {
        String[] parts = subject.split("_");
        String cad2 = parts[1];
        String[] seconds = cad2.split("\\[");
        return seconds[0].trim().toUpperCase();
    }
    
    public static boolean verificarExpresion(String cadena){
        System.out.println(cadena);
        int guionBajo = cadena.indexOf("_");
        int corcheteAbierto = cadena.indexOf("[");
        int corcheteCerrado = cadena.indexOf("]");
        if(guionBajo > 0 && corcheteAbierto > guionBajo && corcheteCerrado > corcheteAbierto ){
            return true;
        }else{
            return false;
        }
    }
       
}
