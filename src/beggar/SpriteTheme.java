/*
 * SpriteTheme.java
 *
 * Created on October 14, 2007, 8:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import java.util.ArrayList;
import javax.swing.Spring;

/**
 *
 * @author Juan
 */
public class SpriteTheme {
    
    /** Creates a new instance of SpriteTheme */
    public SpriteTheme(String n) {
        name = n;
        sprites = new ArrayList<Sprite>(50);
    }
    
    public void add(Sprite s) {
        sprites.add(s);
    }
    
    public String getName() {
        return name;
    }
    
    public Sprite getRandomEnemy(int x, int y) {
        int s = sprites.size();
        int rand;
        Sprite enemy = null;
        for(int m = 0;m<s;m++) {
            rand = (int)(Math.random() * 10000);
            if (sprites.get(m).getFrequency() > rand && enemy == null) {
                enemy = sprites.get(m);
            }
        }
        if (enemy == null) {
            return null;
        }
        return new Sprite(enemy, x, y);
    }

    ArrayList getUniques() {
        int s = sprites.size();
        ArrayList<Sprite> uniques = new ArrayList<Sprite>();
        for(int m = 0;m<s;m++) {
            if (sprites.get(m).getFrequency() == 0) {
                uniques.add(sprites.get(m));
            }
        }
        return uniques;
    }
    
    private String name;
    private ArrayList<Sprite> sprites;
    
}
