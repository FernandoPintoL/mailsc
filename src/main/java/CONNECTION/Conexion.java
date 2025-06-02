
package CONNECTION;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * 
 * @author angela
 * clase de conexión con el servidor
 */


public class Conexion {
    protected Socket socket;
    protected String servidor;
    protected int port;
    protected BufferedReader entrada;
    protected DataOutputStream salida;


    //conexion con el servidor y muestre la respuesta del servidor
    public Conexion(String servidor, int port) throws IOException{
        this.port = port;
        this.servidor = servidor;
        socket = new Socket(this.servidor,this.port);

        // Configurar timeouts y opciones de socket para mejorar la estabilidad
        socket.setSoTimeout(60000);         // 60 segundos de timeout para operaciones de lectura
        socket.setKeepAlive(true);          // Mantener la conexión activa
        socket.setTcpNoDelay(true);         // Deshabilitar el algoritmo de Nagle para enviar datos inmediatamente
        socket.setSendBufferSize(8192);     // Buffer de envío más grande

        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        salida = new DataOutputStream(socket.getOutputStream());

        if(socket != null && entrada != null && salida != null){

            //System.out.println("Conectado a " + servidor + " Protocol " + this.port + " succesfull!!..");

            entrada.readLine();
        }   
    }

    //cierre
    public void close() throws IOException {

        salida.close();
        entrada.close();
        socket.close();

   }

    //lectura de mensajes del servidor

    static protected String getMultiline(BufferedReader in) throws IOException{
        String lines = "";

        while(true){
            String line = in.readLine();
            if(line == null){
                throw new IOException("S: servidor cerró inesperadamente la conexión");
            }

            if(line.equals(".")){
                break;
            }

            if((line.length()>0)&& (line.charAt(0)=='.')){
                line = line.substring(1);
            }

            lines = lines + "\r\n" + line;
        }
        return lines;
    }



}
