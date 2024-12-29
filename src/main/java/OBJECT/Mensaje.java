package OBJECT;

import java.util.*;

/**
 *
 * @author angela
 */
public class Mensaje {

    String table;
    String action;
    List<String> parametros = new LinkedList<>();
    String emisor;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getParametros() {
        return parametros;
    }

    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    @Override
    public String toString() {
        return "Mensaje { " + "\r\n   table = " + table + ",\r\n   action = " + action + ",\r\n   emisor = " + emisor + ",\r\n   parametros = " + parametros + "\r\n}";
    }

    public String tableAction() {
        return table.toUpperCase() + "_" + action.toUpperCase();
    }

    public String evento() {
        return table.toUpperCase() + "_" + action.toUpperCase() + parametros;
    }
}
