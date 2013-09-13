/*
 * TileTheme.java
 *
 * Created on October 14, 2007, 8:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class TileTheme {
    
    public final static Tile DEFAULT = new Tile("", ".", Toolkit.getDefaultToolkit().createImage("img/blank.png"), "Walkable", "Nothin special.", null);
    public final static Tile DOOR = new Tile("Door", ".", Toolkit.getDefaultToolkit().createImage("img/door.png"), "Door", "A regular old door.",  Toolkit.getDefaultToolkit().createImage("img/dooropen.png"));
    public final static Tile WATER = new Tile("Water", "=", Toolkit.getDefaultToolkit().createImage("img/water.png"), "Water", "Look at the clear blue...", null);
    
    /** Creates a new instance of TileTheme */
    public TileTheme(String n) {
        name = n;
        walkableTiles = new ArrayList<Tile>(20);
        solidTiles = new ArrayList<Tile>(20);
        doorTiles = new ArrayList<Tile>(10);
    }

    public void add(Tile t) {
        if(t.type.contains("Walkable")) {
            walkableTiles.add(t);
        }
        if(t.type.contains("Solid")) {
            solidTiles.add(t);
        }
        if(t.type.contains("Door")) {
            doorTiles.add(t);
        }
               
    }
    
    public String getName() {
        return name;
    }
    
    public Tile getRandomWalkableTile() {
        int s = walkableTiles.size();
        if (s == 0) {
            return new Tile(DEFAULT);
        }
        int rand = (int)(Math.random() * s);
        Tile t = DEFAULT;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (walkableTiles.get(m).getFrequency() > rand && t == DEFAULT) {
                t = walkableTiles.get(m);
            }
        }
        
        return new Tile(t);    
    }
    
    public Tile getRandomSolidTile() {
        int s = solidTiles.size();
        if (s == 0) {
            return new Tile(DEFAULT);
        }
        int rand = (int)(Math.random() * s);
        Tile t = DEFAULT;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (solidTiles.get(m).getFrequency() > rand && t == DEFAULT) {
                t = solidTiles.get(m);
            }
        }
        if(t == DEFAULT) {
            t = solidTiles.get(0);
        }
        
        return new Tile(t);     
    }
    
    public Tile getRandomDoor() {
        int s = doorTiles.size();
        if (s == 0) {
            return new Tile(DOOR);
        } else if (s == 1) {
            return new Tile(doorTiles.get(0));
        }
        int rand = (int)(Math.random() * s);
        
        Tile t = DOOR;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (doorTiles.get(m).getFrequency() > rand && t == DOOR) {
                t = doorTiles.get(m);
                //System.err.println(rand + "," +doorTiles.get(m).getFrequency());
            }
        }
        return new Tile(t);    
    }
    
    public Tile getWater() {
        return new Tile(WATER);
    }

    public Tile getTile(char c) {
        Tile ans = new Tile("Grass", String.valueOf(c), Toolkit.getDefaultToolkit().createImage("img/grass.png"), "connection", "This is a way out", null);
        ArrayList<Tile> possibleTiles = new ArrayList<Tile>(10);
        for(int a = 0; a<walkableTiles.size();a++) {
            if (walkableTiles.get(a).symbol.charAt(0) == c) {
                ans = walkableTiles.get(a);
                possibleTiles.add(ans);
            }
        }
        for(int a = 0; a<solidTiles.size();a++) {
            if (solidTiles.get(a).symbol.charAt(0) == c) {
                ans = solidTiles.get(a);
                possibleTiles.add(ans);
            }
        }
        for(int a = 0; a<doorTiles.size();a++) {
            if (doorTiles.get(a).symbol.charAt(0) == c) {
                ans = doorTiles.get(a);
                possibleTiles.add(ans);
            }
        }
        //System.err.println(possibleTiles);
        int s = possibleTiles.size();
        if (s == 0) {
            return new Tile("", String.valueOf(c), DEFAULT.i, "connection", "Let's go traveling!", null);
        }
        
        int rand;
        Tile t = DEFAULT;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (possibleTiles.get(m).getFrequency() > rand && t == DEFAULT) {
                t = possibleTiles.get(m);
            }
        }
        if(t == DEFAULT) {
            t = possibleTiles.get(0);
        }
        
        return new Tile(t);     
    }
    
    public String toString() {
        return name + ": \n" + walkableTiles + "\n" + solidTiles + "\n" + doorTiles;
    }
    
    private String name;
    private ArrayList<Tile> walkableTiles;
    private ArrayList<Tile> solidTiles;
    private ArrayList<Tile> doorTiles;
    
}
