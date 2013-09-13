/*
 * Material.java
 *
 * Created on October 28, 2007, 5:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

/**
 *
 * @author Juan
 */
public class Material {
    
    /** Creates a new instance of Material */
    
    String name;
    int color;
    double weight;
    double value;
    int hardness;
    
    public Material(String n, double w, double v, int h, int c) {
        name = n;
        weight = w;
        value = v;
        hardness =  h;
        color = c;
    }
    
    
}
