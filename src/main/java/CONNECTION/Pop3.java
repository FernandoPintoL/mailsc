
package CONNECTION;

import UTILS.ConstPOP3;
import UTILS.ConstSMPT;
import java.io.BufferedReader;
import java.io.IOException;


/**
 *
 * @author angela
 */
public class Pop3 extends Conexion{
    public Pop3(String servidor, int port) throws IOException {
        super(servidor, port);
    }

    String response = "";
    public String number = "";

    public void login(String user, String pass) throws IOException {
        //USER
        String comando = ConstPOP3.USER + user + ConstPOP3.FIN_LINE;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        entrada.readLine();
        //System.out.println("S : " + entrada.readLine());

        // PASS
        comando = ConstPOP3.PASS + pass + ConstPOP3.FIN_LINE;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        //System.out.println("S : " + entrada.readLine());
    }

    public void stat() throws IOException {
        String comando = ConstPOP3.STAT;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        //System.out.println("S : " + entrada.readLine());
        entrada.readLine();
    }

    public void list() throws IOException {
        String comando = ConstPOP3.LIST;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        number  = getLastMail(entrada);
        //System.out.println("S : " + getMultiline(entrada));        
    }

    public String retr(int numberMessage) throws IOException {
        String comando = ConstPOP3.RETR + numberMessage + ConstPOP3.FIN_LINE;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        response = getMultiline(entrada) + "\r\n";
        //System.out.println("S : " + response);
        return response;
    }

    public void quit() throws IOException {
        String comando = ConstPOP3.QUIT;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        //System.out.println("S : " + entrada.readLine());
        entrada.readLine();
    }

    public void dele(int number) throws IOException {
        String comando = ConstPOP3.DELE + number + ConstPOP3.FIN_LINE;
        //System.out.println("C : " + comando);
        salida.writeBytes(comando);
        //System.out.println("S : " + entrada.readLine());
        entrada.readLine();
    }

    public String Token() {
        if (!response.isEmpty()) {
            return getTokenSubject();
        }
        return "El Subject está vacio";
    }

    private String getTokenSubject() {
        int inicio = response.indexOf(ConstSMPT.Subject);
        if (inicio < 0) {
            inicio = response.indexOf(ConstSMPT.SUBJECT);
            inicio += ConstSMPT.SUBJECT.length();
        }else{
            inicio += ConstSMPT.Subject.length();
        }
        String subC = response.substring(inicio);
        String[] subCad = subC.split(String.valueOf("\r\n"));
        return subCad.length > 0 ? subCad[0].trim().toUpperCase() : "";
    }

    public String getEmail() {
        int index = response.indexOf(ConstSMPT.RETURNPATH);
        if (index > 0) {
            String sub = response.substring(index + ConstSMPT.RETURNPATH.length());
            int start = sub.indexOf(String.valueOf('<'));
            int end = sub.indexOf(String.valueOf('>'));
            String email = sub.substring(start+1,end);
            return email;
        }

        return "";
    }

    private String getLastMail(BufferedReader entrada) throws IOException {
        String number = "";
        String line = "";
        String anteriorLine = "";
        while (true) {
            anteriorLine = line;
            line = entrada.readLine();
            System.out.println("LINE::" + line);
            if (line == null) {
                throw new IOException(" S : Server unawares closed the connection.");
            }
            if (line.equals(".")) {
                break;
            }
            //System.out.println(line);
        }

        number = anteriorLine.substring(0, anteriorLine.indexOf(" "));
        number = number.trim();
        //System.out.println(number);
        return number;
    }
    
    
    
}
