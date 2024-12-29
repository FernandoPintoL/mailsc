
package UTILS;


import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author angela
 */
public class CasosUso {
     String titulo;
    List<Option> options;

    public CasosUso(String title) {
        this.titulo = title;
        this.options = new LinkedList<Option>();
    }
    
    public void addCaso(Option option){
        options.add(option);
    }
    
    public void delete(Option option){
        options.remove(option);
    }
}
