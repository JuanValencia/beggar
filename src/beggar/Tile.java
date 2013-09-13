/*
 * Tile.java
 *
 * Created on October 14, 2007, 10:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class Tile { 
    
    public String name;
    public Image i;
    public Image altImage;
    public Image background;
    public Image behind;
    
    public ArrayList<Item> list;
    
    public boolean isWalkable;
    private boolean isDoor;
    public boolean isSolid;
    public boolean isTransparent;
    
    public String description;
    public String symbol;
    public String type;
    
    private String drop;
    private int maxDurability;
    private int durability;
    
    public int frequency;
    private boolean isRevealed;
    
    private ItemTheme itemThemeForDrops;
    
    /** Creates a new instance of Tile - old*/
    
    public Tile(String n, String sym, Image img, String t, String desc, Image alt) {
        name = n;
        i = img;
        altImage = alt;
        symbol = sym;
        type = t;
        description = desc;
        list = new ArrayList<Item>();
        setTypes(type);
        frequency = 50;
        isRevealed = false;
        maxDurability = 0;
        durability = 0;
        drop = "None";
        behind = null;
    }
    
    public Tile(Tile t) {
        name = t.name;
        i = t.i;
        symbol = t.symbol;
        type = t.type;
        description = t.description;
        list = new ArrayList<Item>();
        setTypes(type);
        frequency = t.getFrequency();
        altImage = t.altImage;
        behind = t.behind;
        isRevealed = false;
        isTransparent = t.isTransparent;
        maxDurability = t.getMaxDurability();
        durability = maxDurability;
        drop = t.getDrop();
        itemThemeForDrops = t.itemThemeForDrops;
    }
    
    public Tile(String rawString, ItemTheme dropTheme) {
        int place = 0;
        String[] split = rawString.split(",");
        
        name = removeQuotes(split[place]);
        
        place++; //1
        i = Toolkit.getDefaultToolkit().createImage(removeQuotes(split[place]));
        place++; //2
        
        symbol = removeQuotes(split[place]);
        place++; //3
        
        type = removeQuotes(split[place]);
        setTypes(type);
        place++; //4
        
        description = removeQuotes(split[place]);
        place++; //5
        
        list = new ArrayList<Item>();
        if (removeQuotes(split[place]).equals("None"))  {
            altImage = null;
        } else {
            altImage = Toolkit.getDefaultToolkit().createImage(removeQuotes(split[place]));
        }
       
        place++; //6
        
        if (removeQuotes(split[place]).equals("None"))  {
            behind = null;
        } else {
            behind = Toolkit.getDefaultToolkit().createImage(removeQuotes(split[place]));
        }
        
        place++; //7
        place++; //8
        frequency = Integer.parseInt(split[place]);
        
        place++; //9    
        maxDurability = Integer.parseInt(split[place]);
        durability = maxDurability;
        
        place++; //10
        drop = removeQuotes(split[place]);
        
        isRevealed = false;
        
        itemThemeForDrops = dropTheme;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public String getDrop() {
        return drop;
    }


    
    private void setTypes(String types) {
        isDoor = false;
        if(types.contains("Walkable")) {
            isWalkable = true;
            isTransparent = true;
            isSolid = false;
        } else if (types.contains("connection")) {
            isWalkable = true;
            isTransparent = true;
        } else if (types.contains("Door")) {
            isWalkable = false;
            isDoor = true;
            isTransparent = false;
            isSolid = true;
        } else if (types.contains("Solid")) {
            isSolid = true;
            isTransparent = false;
            isWalkable = false;
        } else if (types.contains("Water")) {
            isSolid = false;
            isTransparent = true;
            isWalkable = false;
        }
        if (types.contains("Transparent")) {
            isTransparent = true;
        }
    }
    
    private String removeQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }
    public void addItem(Item item) {
        list.add(item);
    }
    public ArrayList<Item> getItems() {
        return list;
    }

    public int getFrequency() {
        return frequency;
    }
    
    public boolean isRevealed() {
        return isRevealed;
    }
    
    public void remember() {
        isRevealed = true;
    }
    
    public boolean isThrowable() {
        return !isSolid;
    }
    
    public String toString() {
        return name + ": " + symbol + ": " + description + ": " + type + ": " + isTransparent;
    }
    
    public String open(Player p) {
        if (isDoor) {
            Image temp;
            temp = i;
            i = altImage;
            altImage = temp;
            isWalkable = true;
            isTransparent = true;
            return "You've opened the door.";
        }
        return "There is no door here! Check your code!";
    }

    public void kick(Player p, MessageHolder m) {
        if (durability <= 0) {
            m.message("You kick at nothing...", Color.ORANGE);
            return;
        }
        
        int total = p.kick();
        double percent = (double)total/(double)maxDurability *100;
        //System.err.println(total + ", " + percent);
        durability -= total;
        
        if (durability <= 0) {
            //Add a name to tiles and have an appropriate fall message
            //drop the right item
            //change this tile to be blank
            m.message("Your kick destroys the " + name + "!", Color.GREEN);
            destroy();
            return;
        } else if (percent < 10) {
            m.message("You've nicked the " + name + ".", Color.ORANGE);
            return;
        } else if (percent < 50) {
            m.message("Soon, the " + name + " will fall to your kick!", Color.ORANGE);
            return;
        } else if (percent < 100) {
            m.message("Your kick did some serious damage to the " + name + "!", Color.ORANGE);
            return;
        }
    }
    public void attack(Player p, MessageHolder m) {
        if (durability <= 0) {
            m.message("You swing mindlessly...", Color.ORANGE);
            return;
        }
        
        int total = p.attack();
        double percent = (double)total/(double)maxDurability *100;
        //System.err.println(total + ", " + percent);
        durability -= total;
        
        if (durability <= 0) {
            //Add a name to tiles and have an appropriate fall message
            //drop the right item
            //change this tile to be blank
            
            m.message("With a mighty blow you bring down the " + name + "!", Color.GREEN);
            destroy();
            return;
        } else if (percent < 10) {
            m.message("You've nicked the " + name + ".", Color.ORANGE);
            return;
        } else if (percent < 50) {
            m.message("Aha! Take that you stupid " + name + "!", Color.ORANGE);
            return;
        } else if (percent < 100) {
            m.message("I will smite the " + name + " into the ground!", Color.ORANGE);
            return;
        }
    }
    
    public void hit(Item item, MessageHolder m) {
        if (durability <= 0) {
            m.message("The " + item.getName() + " falls to the ground.", Color.ORANGE);
            return;
        }
        
        int total = item.getAttackBonus();
        double percent = (double)total/(double)maxDurability *100;
        durability -= total;
        
        if (durability <= 0) {
            m.message("Home RUN! You knocked the " + name + " down with a thrown " + item.getName() + "!", Color.GREEN);
            destroy();
            return;
        } else if (percent < 10) {
            m.message("You've nicked the " + name + ".", Color.ORANGE);
            return;
        } else if (percent < 50) {
            m.message("That throw did some damage to the " + name + "!", Color.ORANGE);
            return;
        } else if (percent < 100) {
            m.message("I meant to hit the " + name + " with the " + item.getName() + "!", Color.ORANGE);
            return;
        }
    }
    
    private void destroy() {
        
        Item toAdd = itemThemeForDrops.getNewItem(drop);
        int numberOfItemsToAdd = (int)(Math.random() * (double)(maxDurability/10));
        for (int m = 0; m<numberOfItemsToAdd; m++) {
            list.add(toAdd);
            toAdd = itemThemeForDrops.getNewItem(drop);
        }
        list.add(toAdd);
        
        Tile t = TileTheme.DEFAULT;
        name = t.name;
        i = behind;
        symbol = t.symbol;
        type = t.type;
        description = t.description;
        
        setTypes(type);
        frequency = t.getFrequency();
        altImage = t.altImage;
        isTransparent = t.isTransparent;
        maxDurability = t.getMaxDurability();
        durability = maxDurability;
        drop = t.getDrop();
    }
        
}
