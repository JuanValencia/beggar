/*
 * ItemStack.java
 *
 * Created on October 22, 2007, 9:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class ItemStack {
    
    private ArrayList<Item> stack;
    private String name;
    
    /** Creates a new instance of ItemStack */
    
    public ItemStack(Item item) {
        name = item.getName();
        stack = new ArrayList<Item>(20);
        stack.add(item);
    }

    public String getName() {
        if (stack.size() == 1) {
            return get().getName();
        }
        return get().getName() + " (" + stack.size() +")";
    }
    
    public boolean add(Item item) {
        return stack.add(item);
    }
    public Item get() {
        return stack.get(0);
    }
    public Item remove() {
        return stack.remove(0);
    }
    public Image getImage() {
        return get().getImage();
    }
    
    public Item remove(Item i) {
        for (int m = 0; m<stack.size(); m++){
            if (i == stack.get(m)) {
                return stack.remove(m);
            }
        }
        return null;
    }
    
    public String toString() {
        if (stack.size() == 1) {
            return get().toString();
        }
        return get().toString() + " (" + stack.size() +")";
    }

    int size() {
        return stack.size();
    }
}
