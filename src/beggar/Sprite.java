/*
 * Sprite.java
 *
 * Created on September 30, 2007, 1:06 PM
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
public class Sprite {
    
    final static int RANDOM = 2338;
    final static int NORMAL = 234;
    
    
    /** Creates a new instance of Sprite */
        
    public Sprite(Sprite s, int fx, int fy) {
        x = fx;
        y = fy;
        i = s.getImage();
        name = s.getName();
        lastWords = s.getLastWords();
        
        hp = s.getHP();     
        speed = s.getSpeed();
        movement = 0;
        baseMinAttack = s.getBaseMinAttack();
        baseMaxAttack = s.getBaseMaxAttack();
        AC = s.getAC();
        light = s.getLight();
        
        exp = s.getExp();
        AI = s.getAI();
        
        strength = s.getStrength();
        dexterity = s.getDexterity();
        agility = s.getAgility();
        constitution = s.getConstitution();
        intelligence = s.getIntelligence();
        wisdom = s.getWisdom();
        charisma = s.getCharisma();
        luck = s.getLuck();
        
        alignment = s.getAlignment();
        chat = s.getChat();
        friendly = s.isFriendly();
        
        ArrayList<Effect> temp = s.getEffects();
        effects = new ArrayList<Effect>(10);
        if (temp!= null) {
            for(int m=0; m<temp.size();m++) {
                effects.add(new Effect(temp.get(m)));
            }
        }
    }
    
    public Sprite(String rawString) {
        String[] s = rawString.split(",");
        
        x = 0;
        y = 0;
        
        name = remQuotes(s[0]);
        i = Toolkit.getDefaultToolkit().createImage(remQuotes(s[1]));
        
        hp =  makeInt(s[2]);
        speed =  makeInt(s[3]);
        movement =  0;
        
        baseMinAttack =  makeInt(s[4]);
        baseMaxAttack =  makeInt(s[5]);
        AC =  makeInt(s[6]);
        light =  makeInt(s[7]);
        
        strength =  makeInt(s[8]);
        dexterity =  makeInt(s[9]);
        agility =  makeInt(s[10]);
        constitution =  makeInt(s[11]);
        
        intelligence =  makeInt(s[12]);
        wisdom = makeInt(s[13]);
        charisma = makeInt(s[14]);
        luck = makeInt(s[15]);
                
        if (remQuotes(s[16]).equalsIgnoreCase("random")) {
            AI = RANDOM;
        } else if (remQuotes(s[16]).equalsIgnoreCase("normal")) {
            AI = NORMAL;
        } else {
            AI = RANDOM;
        }        
        
        exp = makeInt(s[17]);   
        
        lastWords = remQuotes(s[18]);
        theme = remQuotes(s[19]);
        frequency = makeInt(s[20]);
        
        chat = remQuotes(s[21]);
        alignment = makeInt(s[22]);
        if (remQuotes(s[23]).equalsIgnoreCase("no")) {
            friendly = false;
        } else {
            friendly = true;
        }
        
        effects = new ArrayList<Effect>(10);
        String[] stringEffects = remQuotes(s[24]).split(":");
        for (int m = 0; m < stringEffects.length; m++) {
            if (!stringEffects[m].equals("None")) {
                effects.add(new Effect(stringEffects[m]));
            }
        }
        
    }
    
    
    private String remQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }
    
    private int makeInt(String s) {
        int i = Integer.parseInt(s);
        return i;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }
    
    public String getChat() {
        return chat;
    }
    
    public int getAlignment() {
        return alignment;
    }
    
    public boolean isFriendly() {
        return friendly;
    }
    
    public int getFrequency(){
        return frequency;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public int getAI() {
        return AI;
    }
    
    public int getExp() {
        return exp;
    }
    
    public int getLight() {
        return light;
    }
    
    public int getAC() {
        return AC;
    }
    
    public int getBaseMaxAttack() {
        return baseMaxAttack;
    }
    
    public int getBaseMinAttack() {
        return baseMinAttack;
    }
    
    public int getHP() {
        return hp;
    }
    
    public int getSpeed() {
        return speed;
    }

    public String getLastWords() {
        return lastWords;
    }
          
    public String getName() {
        return name;
    }
    
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Image getImage() {
        return i;
    }
    public void setImage(Image im) {
        i = im;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setSpeed(int s) {
        speed = s;
    }
    public void setHP(int hitp) {
        hp = hitp;
    }
    
    public void setExp(int xp) {
        exp = xp;
    }
    
    public void setAC(int armor) {
        AC = armor;
    }
    
    public void setAI(int pattern) {
        AI = pattern;
    }
    
    public void update(double time, Map map, Player player, MessageHolder messages) {
        if(hp<1) {
            map.getEnemies().remove(this);            
            messages.message(lastWords, Color.BLUE);
            player.giveExp(exp);
            return;
        }
        movement += time;
        
        while (movement > speed) {
            
            if (AI == RANDOM) {
                randomUpdate(map, player, messages);
            }
            if (AI == NORMAL) {
                normalUpdate(map, player, messages);
            }
            
        }
    }
    private void randomUpdate(Map map, Player player, MessageHolder messages) {
        int fX = 0;
        int fY = 0;
        
        double rand = Math.random();
        if (rand< 0.5) {
            fX--;
        } else {
            fX++;
        }
        rand = Math.random();
        if (rand< 0.5) {
            fY--;
        } else {
            fY++;
        }
        handleMove(player, map, messages, x+fX, y+fY);
    }
    
    private void normalUpdate(Map map, Player player, MessageHolder messages) {
        if (canSee(player.getX(), player.getY(), map)) {
            int difX = x - player.getX();
            int difY = y - player.getY();
            int fX = 0;
            int fY = 0;
            if (difX > 0) {
                fX--;
            } 
            if (difY > 0) {
                fY--;
            } 
            if (difY < 0) {
                fY++;
            } 
            if (difX < 0) {
                fX++;
            }
            handleMove(player, map, messages, x+fX, y+fY);
        } else {
            randomUpdate(map, player, messages);
        }
    }
    
    public boolean canSee(int sx, int sy, Map map) {
        int difX = x - sx;
        int difY = y - sy;
        
        if (Math.sqrt((difX * difX) + (difY*difY)) > light) {
            return false;
        }
        
        return true;
    }
    
    void moveLeft(Map m, Player p, MessageHolder messages) {
        movement -= speed;
        handleMove(p, m, messages, x-1, y);
    }
    void moveRight (Map m , Player p, MessageHolder messages) {
        movement -= speed;
        handleMove(p, m, messages, x+1, y);
    }
    void moveUp(Map m, Player p, MessageHolder messages){
        movement -= speed;
        handleMove(p, m, messages, x, y-1);
    }
    void moveDown(Map m, Player p, MessageHolder messages) {
        movement -= speed;
        handleMove(p, m, messages, x, y+1);
    }
    void handleMove(Player p, Map m, MessageHolder messages, int nx, int ny) {
        if (m.isWalkable(nx, ny)) {
            if (x - nx != 0 && y - ny !=0) {
                movement -= (speed * 1.414); //Assume it's moving diagonally so 1 tile's worth of move * sqrt(2)
            } else {
                movement -= speed;
            }
            if (p.getX() != nx || p.getY() != ny) {
                x = nx;
                y = ny;
            }
        } else {
            movement -= 5;
        }
        if (p.getX() == nx && p.getY() == ny && !friendly) {
            int attack = attack();
            attack = p.recieveAttack(attack, calculateAccuracy(), effects, messages);
            if (attack == 0) {
                messages.message("The " + name + " missed horribly!", Color.LIGHT_GRAY);
            } else {
                messages.message("The " + name + " attacks for " + attack + " hp!", Color.RED);
            }
            p.wake();
        }
    }
    
    private int calculateAccuracy() {
        //TODO think up something more clever or look it up!!!        
        return (dexterity*3) + (agility*2) + luck;
    }
    
    int recieveAttack(int attack, int accuracy) {
        if (isFriendly()) {
            friendly = false;
            AI = NORMAL;
        }
        //TODO check this... and add to the sprite
        int percentToHit = accuracy - AC;
        int toHit = (int)(Math.random() * percentToHit);
        if (toHit > 10) {
            hp = hp - attack;
            return attack;
        } else if (toHit > 90) {
            hp = hp - attack - attack;
            return attack + attack;
        }
        return 0;
    } 
    
    public int hit(Item item) {
        if (isFriendly()) {
            friendly = false;
            AI = NORMAL;
        }
        int percentToHit = 100 - AC;
        int hitFor = item.getAttackBonus();
        int toHit = (int)(Math.random() * percentToHit);
        if (toHit > 10) {
            hp = hp - hitFor;
            return hitFor;
        } else if (toHit > 90) {
            hp = hp - hitFor - hitFor;
            return hitFor + hitFor;
        }
        return 0;
    } 
    
    public int kickFor(int ouch) {
        if (friendly) {
            friendly = !friendly;
            AI = NORMAL;
        }
        int percentToHit = 100 - AC;
        int toHit = (int)(Math.random() * percentToHit);
        if (toHit > 10) {
            hp = hp - ouch;
            return ouch;
        } else if (toHit > 90) {
            hp = hp - ouch - ouch;
            return ouch * 2;
        } else {
            return 0;
        }
        
    }
    
    int attack() {
        int dif = baseMaxAttack - baseMinAttack;
        int rand = (int) (Math.random() * (dif+1));
        int weaponBonus = 0;
        int strengthBonus = strength/10;
        //TODO figure out what a weapon bonus for an enemy looks like!
        
        return baseMinAttack + rand + weaponBonus + strengthBonus;
    }
    
    public int getStrength() {
        return strength;
    }    
    public void setStrength(int s) {
        strength = s;
    }
    
    public int getDexterity() {
        return dexterity;
    }    
    public void setDexterity(int d) {
        dexterity = d;
    }
    
    public int getAgility() {
        return agility;
    }    
    public void setAgility(int a) {
        agility = a;
    }
    
    public int getConstitution() {
        return constitution;
    }    
    public void setConstitution(int c) {
        constitution = c;
    }
    
    public int getIntelligence() {
        return intelligence;
    }    
    public void setIntelligence(int i) {
        intelligence = i;
    }
    
    public int getWisdom() {
        return wisdom;
    }    
    public void setWisdom(int w) {
        wisdom = w;
    }
    
    public int getCharisma() {
        return charisma;
    }    
    public void setCharisma(int c) {
        charisma = c;
    }
    
    public int getLuck() {
        return luck;
    }    
    public void setLuck(int l) {
        luck = l;
    }
    
    public String toString() {
        return name;
    }
    
    private int x;
    private int y;
    private Image i;
    private String name;
    private String lastWords;
    private String chat;
    
    private int hp;
    private int speed;
    private int movement;
    private int baseMinAttack;
    private int baseMaxAttack;
    private int AC;
    private int light;
    private int alignment;
    private boolean friendly;
    
    private int exp; 
    private int AI;
    
    private int strength;
    private int dexterity;
    private int agility;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int luck;
    
    private String theme;
    private int frequency;
    
    private ArrayList<Effect> effects;
    
}
