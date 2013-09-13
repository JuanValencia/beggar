/*
 * ItemInMotion.java
 *
 * Created on November 3, 2007, 11:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class ItemInMotion {
    
    public Item item;
    public double startX;
    public double startY;
    public double endX;
    public double endY;
    private double difX;
    private double difY;
    private double incX;
    private double incY;
    public ItemInMotion(Item itemToAdd, double sX, double sY, double eX, double eY) {
        item = itemToAdd;
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
        difX = endX - startX;
        difY = endY - startY;
        incX = Math.abs(difX/7);
        incY = Math.abs(difY/7);
    }
    public void update(ArrayList<ItemInMotion> container) {
        if (withinRange(startX, endX) && withinRange(startY, endY)) {
            container.remove(this);
            return;
        }
        
        
        if (!withinRange(startX, endX)) {
            startX += (incX * (difX/Math.abs(difX)));
        }
        if (!withinRange(startY, endY)) {
            startY += (incY * (difY/Math.abs(difY)));
        }
    }
    private boolean withinRange(double start, double end) {
        double dif = end - start;
        dif = Math.abs(dif);
        if (dif < .2) {
            return true;
        }
        return false;
    }
    
}
