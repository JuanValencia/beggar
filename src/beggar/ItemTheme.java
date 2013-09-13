/*
 * ItemTheme.java
 *
 * Created on October 14, 2007, 8:39 PM
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
public class ItemTheme {
    
    /** Creates a new instance of ItemTheme */
    public ItemTheme(String n) {
        name = n;
        items = new ArrayList<Item>(20);
    }
    
    public String getName() {
        return name;
    }
    
    public void add(Item item) {
        items.add(item);
    }
    
    public Item getRandomItem(int threat) {
        int s = items.size();
        int rand;
        Item item = null;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (items.get(m).getFrequency() > rand && item == null) {     
                item = items.get(m);
            }
        }
        if (item == null) {
            return null;
        }
        return new Item(item, threat);
    }
    
    public Item getNewItem(String s, Material material) {
        Item ans = null;
        for(int m = 0;m<items.size();m++) {
            if (items.get(m).getName().equals(s)) {
                return new Item(items.get(m), material);
            }
        }
        return null;
    }
    public Item getNewItem(String s) {
        for(int m = 0;m<items.size();m++) {
            if (items.get(m).getName().equals(s)) {
                //TODO make it so that you can get the material from the item name!!!!!
                //right now it determines the material by the threat level.
                return new Item(items.get(m), 20);
            }
        }
        return null;
    }
    
    private String name;
    private ArrayList<Item> items;
    
}
