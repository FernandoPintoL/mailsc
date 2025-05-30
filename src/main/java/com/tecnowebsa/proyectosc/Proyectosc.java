/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tecnowebsa.proyectosc;

import CONSULTAS.Consulta;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fpl
 */
public class Proyectosc {
    private static final int INTERVALO_ESPERA_MS = 5000; // Intervalo de espera en milisegundos

    public static void main(String[] args) {
        try {
            Consulta manage = new Consulta();
            int cantidadMail = manage.getCantidadMails();
            while (true) {
                int nuevaCantidadMail = manage.getCantidadMails();
                if (cantidadMail != nuevaCantidadMail) {
                    System.out.println("SE ENCONTRÓ UN NUEVO MENSAJE");
                    cantidadMail = nuevaCantidadMail;
                    manage.newMensaje();
                } else {
                    System.out.println("Esperando nuevos mensajes...");
                }
                Thread.sleep(INTERVALO_ESPERA_MS);
            }
        } catch (IOException ex) {
            System.err.println("Error de IO: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.err.println("Ejecución interrumpida: " + ex.getMessage());
            Logger.getLogger(Proyectosc.class.getName()).log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            System.err.println("Error inesperado: " + ex.getMessage());
            Logger.getLogger(Proyectosc.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try {
            Consulta manage = new Consulta();
            int cantidadMail = manage.getCantidadMails();
            while (true) {
                int newCantidadEmail = manage.getCantidadMails();
                if (cantidadMail != newCantidadEmail) { 
                    System.out.println("Se encontro un nuevo Mensaje".toUpperCase());
                    cantidadMail = newCantidadEmail;
                    manage.newMensaje();
                } else {
                    System.out.println("...");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                    Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
            //Logger.getLogger(CORPEGRANOS.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
