/*
 * Player.java
 *
 * Created on September 12, 2007, 6:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import beggar.Item;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class Player {
    
    
    /** Creates a new instance of Player */
    public Player() {
        x = 1;
        y = 1;
        
        baseMinAttack = 1;
        baseMaxAttack = 2;
        
        i = Toolkit.getDefaultToolkit().createImage("img/player.png");
        
        inv = new ArrayList<ItemStack>(30);
        equipInv = new ArrayList<Item>(30);
        
        //the lower the speed, the less time it takes to do things.
        speed = 30;
        hp = 20;
        maxHP = 20;
        light = 8;
        level = 1;
        
        strength = 10;
        dexterity= 10;
        agility= 10;
        constitution= 10;
        intelligence= 10;
        wisdom= 10;
        charisma= 10;
        luck= 10;
        
        isResting = false;
        effects = new ArrayList<Effect>(20);
        
        throwSkill = 20;
    }
    
    public void rest() {
        isResting = true;
    }
    public void wake() {
        isResting = false;
    }
    
    public boolean isResting() {
        return isResting;
    }
    
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getSpeed() {
        return speed + getBurdenToSpeed();
    }
    public int getHP() {
        return hp;
    }
    public int getExp() {
        return exp;
    }
    public int getNextLevelExp() {
        return (int)(5 * (Math.exp(level)));
    }
    public void giveExp(int e) {
        exp += e;
        if (exp > (5 * (Math.exp(level)))) {
            levelUp();
        }
    }
    
    public void levelUp() {
        level++;
        
        speed = speed - (agility/10);
        int rand = (int)(Math.random() * (strength + dexterity));
        if (rand > 35) {
            baseMinAttack++;
        }
        if (rand > 18) {
            baseMaxAttack++;
        }
        rand = (int)(Math.random() * (constitution + luck)/2);
        maxHP = maxHP + rand;
        hp = maxHP;
        
    }
    
    public void passTime(double t, MessageHolder messages) {
        regen(t);
        int healPoison = 0;
        int toHeal = 0;
        int toPoison = 0;
        for(int m = 0; m<effects.size(); m++) {
            healPoison += effects.get(m).getHealPoisonAmount();
            toHeal += effects.get(m).getHealAmount();
            toPoison += effects.get(m).getPoisonAmount();
        }
        if (healPoison >= toPoison) {
            healPoison = healPoison - toPoison;
            toPoison = 0;
        } else {
            toPoison = toPoison - healPoison;
            healPoison = 0;
        }
        
        for(int n = 0; n<effects.size(); n++) {
            while (effects.get(n).getPoisonAmount() * effects.get(n).getDuration() > 0 && healPoison >0) {
                effects.get(n).reduce();
                healPoison--;
            }
        }
        
        for(int m = 0; m<effects.size(); m++) {
            effects.get(m).reduce();
            if (effects.get(m).isDone()) {
                effects.remove(m);
            }
        }
        
        int maxHeal = maxHP - hp;
        if (toHeal != 0) {
            if (toHeal > maxHeal) {
                hp += maxHeal;
                if (maxHeal != 0) {
                    messages.message("You've just been healed for " + maxHeal + "!", Color.GREEN);
                }
            } else {
                hp += toHeal;
                messages.message("You've just been healed for " + toHeal + "!", Color.GREEN);
            }
        }
        if (toPoison !=0) {
            hp -= toPoison;
            messages.message("Poison runs hot in your blood for " + toPoison + " damage...", Color.PINK);
        }
    }
    
    private void applyEffects(ArrayList<Effect> e, MessageHolder m) {
        int successRate;
        int rand;
        for (int n = 0; n<e.size(); n++) {
            successRate = e.get(n).getSuccessRate();
            rand = (int)(Math.random() * 100);
            if (rand < successRate) {
                addEffect(e.get(n));
                m.message(e.get(n).getMessage(), Color.MAGENTA);
            } else {
                m.message("Something about that didn't seem quite right.");
            }
        }
    }
    
    public void regen(double t) {
        int rand = (int)(Math.random() * (10 - constitution/10));
        if (rand == 1 && hp != maxHP) {
            hp++;
        }
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
    
    public Image getImage() {
        return i;
    }
    public ArrayList<ItemStack> getInv() {
        return inv;
    }
    public ArrayList<Item> getEquippedInv() {
        return equipInv;
    }
    
    public void move(int nx, int ny) {
        x = nx;
        y = ny;
    }
    
    public void giveItem(Item i) {
        for(int m = 0; m<inv.size();m++) {
            if (i.getName().equals(inv.get(m).get().getName()) && !isEquipped(inv.get(m).get())) {
                inv.get(m).add(i);
                sortItems();
                return;
            }
        }
        inv.add(new ItemStack(i));
        sortItems();
    }
    public Item takeItem(Item i) {
        //TODO check to make sure it's not equipped.
        for(int m = 0; m<inv.size();m++) {
            inv.get(m).remove(i);
            removeEmptyStacks();
        }
        return null;
    }
    
    public double getCarryingCapacity() {
        return 20.0 * strength;
    }
    public int getBurdenToSpeed() {
        double weightcarried = getWeightCarried();
        double totalCarryingCapacity = getCarryingCapacity();
        if (weightcarried/totalCarryingCapacity < .5) {
            return 0;
        } else if (weightcarried < totalCarryingCapacity) {
            double total = totalCarryingCapacity/2;
            double left = totalCarryingCapacity - weightcarried;
            return (int)(2 * speed * (total - left)/total);
        } else {
            double total = totalCarryingCapacity/2;
            double left = totalCarryingCapacity - weightcarried;
            return (int)(3 * speed * (total - left)/total);
        }
    }
    
    public double getWeightCarried() {
        double weightcarried = 0;
        for (int m = 0; m < inv.size(); m++) {
            int s = inv.get(m).size();
            double each = inv.get(m).get().getWeight();
            weightcarried += (s * each);
        }
        return weightcarried;
    }
    
    public void sortItems() {
        /* TODO figure this out!!! */
        ArrayList<ItemStack> toBe = new ArrayList<ItemStack>(20);
        
        //Be careful because if something belong to more than one sorted category, you'll end up adding it
        //to toBe twice which will give you two pointers to the itemStack when you add it to the inventory.
        
        for(int m = 0; m<inv.size();m++) {
            if (isEquipped(inv.get(m).get())) {
                toBe.add(inv.get(m));
            }
        }
        
        for(int m = 0; m<inv.size();m++) {
            if (inv.get(m).get().getType().contains("Potion") && !toBe.contains(inv.get(m))) {
                //System.err.println("Scrolls copied out!");
                toBe.add(inv.get(m));
                //System.err.println(toBe + " " + inv);
            }
        }
        
        for(int m = 0; m<inv.size();m++) {
            if (inv.get(m).get().getType().contains("Scroll")&& !toBe.contains(inv.get(m))) {
                //System.err.println("Scrolls copied out!");
                toBe.add(inv.get(m));
                //System.err.println(toBe + " " + inv);
            }
        }
        
        for(int m = 0; m<inv.size();m++) {
            if (inv.get(m).get().getType().contains("Wand")&& !toBe.contains(inv.get(m))) {
                //System.err.println("Scrolls copied out!");
                toBe.add(inv.get(m));
                //System.err.println(toBe + " " + inv);
            }
        }
        
        
        //Now remove the items from the inv.
        
        for(int m = 0; m<toBe.size();m++) {
            //System.err.println("Removed!");
            inv.remove(toBe.get(m));
            //System.err.println(toBe + " " + inv);
            
        }
        
        //Now add them back in in a sorted order!
        for(int m = 0; m<toBe.size();m++) {
            //System.err.println("Put back!");
            
            inv.add(toBe.get(m));
            //System.err.println(toBe + " " + inv);
        }
        
        
    }
    
    boolean equipHead(Item chosenItem) {
        if (chosenItem.isHelmet()) {
            if (head == null) {
                head = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (head == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(head);
                head = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipLeftArm(Item chosenItem) {
        if (chosenItem.isWieldable()) {
            if (lArm == null) {
                lArm = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (lArm == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(lArm);
                lArm = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipRightArm(Item chosenItem) {
        if (chosenItem.isWieldable()) {
            if (rArm == null) {
                rArm = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (rArm == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(rArm);
                rArm = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipLRing(Item chosenItem) {
        if (chosenItem.isRing()) {
            if (ringA == null) {
                ringA = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (ringA == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(ringA);
                ringA = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipRRing(Item chosenItem) {
        if (chosenItem.isRing()) {
            if (ringB == null) {
                ringB = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (ringB == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(ringB);
                ringB = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipBelt(Item chosenItem) {
        if (chosenItem.isBelt()) {
            if (belt == null) {
                belt = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (belt == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(belt);
                belt = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipBody(Item chosenItem) {
        if (chosenItem.isBody()) {
            if (body == null) {
                body = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (body == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(body);
                body = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipCape(Item chosenItem) {
        if (chosenItem.isCape()) {
            if (cape == null) {
                cape = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (cape == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(cape);
                cape = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipBoots(Item chosenItem) {
        if (chosenItem.isBoots()) {
            if (boots == null) {
                boots = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (boots == chosenItem) {
                unEquip(chosenItem);
            } else {
                unEquip(boots);
                boots = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    boolean equipGloves(Item chosenItem) {
        if (chosenItem.isGloves()) {
            if (gloves == null) {
                gloves = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            } else if (gloves == chosenItem) {
                unEquip(chosenItem);
            } else {  //if you have gloves, and they aren't identical to the chosen item
                unEquip(gloves);
                gloves = chosenItem;
                equipInv.add(chosenItem);
                takeItem(chosenItem);
                inv.add(new ItemStack(chosenItem));
            }
            calculateAC();
            sortItems();
            return true;
        } else {
            return false;
        }
    }
    
    int calculateAC() {
        AC = 0;
        int size = equipInv.size();
        for (int m = 0;m<size;m++) {
            AC = AC + equipInv.get(m).getAC();
        }
        return AC;
    }
    boolean isEquipped(Item check) {
        return equipInv.contains(check);
    }
    void unEquip(Item item) {
        if (item == head) { takeItem(item); giveItem(item); head = null; equipInv.remove(item); calculateAC();}
        if (item == lArm) { takeItem(item); giveItem(item); lArm = null; equipInv.remove(item); calculateAC();}
        if (item == rArm) { takeItem(item); giveItem(item); rArm = null; equipInv.remove(item); calculateAC();}
        if (item == ringA) { takeItem(item); giveItem(item); ringA = null; equipInv.remove(item); calculateAC();}
        if (item == ringB) { takeItem(item); giveItem(item); ringB = null; equipInv.remove(item); calculateAC();}
        
        if (item == belt) { takeItem(item); giveItem(item); belt = null; equipInv.remove(item); calculateAC();}
        if (item == body) { takeItem(item); giveItem(item); body = null; equipInv.remove(item); calculateAC();}
        if (item == cape) { takeItem(item); giveItem(item); cape = null; equipInv.remove(item); calculateAC();}
        if (item == boots) { takeItem(item); giveItem(item); boots = null; equipInv.remove(item); calculateAC();}
        if (item == gloves) { takeItem(item); giveItem(item); gloves = null; equipInv.remove(item); calculateAC();}
    }
    
    /**
     * This is called when you want to know how much damage the character will dish out.  It is basically a value that is calculated based on his stats and some randomness.
     * @return the amount of damage that the player's attack was worth.  Whoever recieves this attack might block some of that damage.
     */
    int attack() {
        int dif = baseMaxAttack - baseMinAttack;
        int rand = (int) (Math.random() * (dif+1));
        int weaponBonus = 0;
        int strengthBonus = strength/10;
        if (lArm != null) {
            weaponBonus = weaponBonus + lArm.getAttackBonus();
        }
        if (rArm != null) {
            weaponBonus = weaponBonus + rArm.getAttackBonus();
        }
        return baseMinAttack + rand + weaponBonus + strengthBonus;
    }
    
    public String getAttackRangeString() {
        String ans ="";
        int weaponBonus = 0;
        int strengthBonus = strength/10;
        if (lArm != null) {
            weaponBonus = weaponBonus + lArm.getAttackMin();
        }
        if (rArm != null) {
            weaponBonus = weaponBonus + rArm.getAttackMin();
        }
        int min = baseMinAttack + weaponBonus + strengthBonus;
        weaponBonus = 0;
        if (lArm != null) {
            weaponBonus = weaponBonus + lArm.getAttackMax();
        }
        if (rArm != null) {
            weaponBonus = weaponBonus + rArm.getAttackMax();
        }
        int max = baseMaxAttack + weaponBonus + strengthBonus;
        ans += min;
        ans += " - ";
        ans += max;
        return ans;
    }
    
    /**
     * This is called when a player is recieving an attack.
     * @param attack
     * @param accuracy
     * @return The amount of actual damage the player took.
     */
    int recieveAttack(int attack, int accuracy, ArrayList<Effect> e, MessageHolder m) {
        int percentToHit = accuracy - AC;
        int toHit = (int)(Math.random() * percentToHit);
        if (toHit > 10) {
            hp = hp - attack;
            applyEffects(e, m);
            return attack;
        } else if (toHit > 90) {
            hp = hp - attack - attack;
            applyEffects(e, m);
            return attack + attack;
        }
        return 0;
    }
    
    public void throwItem(Map map, Item item, int sx, int sy, MessageHolder m) {
        //TODO figure out the error rates, plot a trajectory, and then put it in map as an itemInMotion
        if (throwSkill <  70) {
            throwSkill++;
        }
        
        if (isEquipped(item)) {
            unEquip(item);
        }
        
        //The most any reasonable throw will be of by is 30 degrees.  (I could make it based on Strength)
        int degrees = 30;
        double rand = Math.random();
        rand = rand * throwSkill;
        rand = 1 - (rand/100);
        degrees = (int)(degrees * rand);
        //will miss in one direction or another.  But every 1/3rd time will hit. (no hero throws that badly!)
        int posOrNeg = (int)(Math.random() * 3) - 1;
        degrees = degrees * posOrNeg;
        //System.err.println("I was off by these degrees:" + degrees);
        double weight = item.getWeight();
        //System.err.println("The item I threw weighed:" + weight);
        int distance = (int)(strength * 3 / weight);
        //System.err.println("I could potentially throw it this far:" + distance);
        
        //Now calculate where the item will land based off of these parameters
        int difX = sx - getX();
        int difY = sy - getY();
        int absX = Math.abs(difX);
        int absY = Math.abs(difY);
        
        int wayX = 0;
        int wayY = 0;
        
        if (difX != 0) {
            wayX = difX/Math.abs(difX);
        }
        if (difY != 0) {
            wayY = difY/Math.abs(difY);
        }        
        if (difY == 0) {
            //absX becomes the hypotenuse, you know the angle
            float hyp = absX;
            float angle = Math.abs(degrees);
            if (hyp > distance) {
                m.message("You feel kind of puny...", Color.ORANGE);
            }
            hyp = hyp > distance ? distance : hyp;
            int newX = (int)Math.round(Math.cos(angle/360 * 2 * Math.PI) * hyp * wayX);
            int newY = (int)Math.round(Math.sin(angle/360 * 2 * Math.PI) * hyp);
            if (degrees < 0) {
                newY = - newY;
            }
            //System.err.println("you will throw x " + newX + ", and y " + newY);
            throwItemPerfect(map, item, newX, newY, m);
        } else if (difX == 0) {
            float hyp = absY;
            float angle = Math.abs(degrees);
            if (hyp > distance) {
                m.message("You feel kind of puny...", Color.ORANGE);
            }
            hyp = hyp > distance ? distance : hyp;
            int newY = (int)Math.round(Math.cos(angle/360 * 2 * Math.PI) * hyp) * wayY;
            int newX = (int)Math.round(Math.sin(angle/360 * 2 * Math.PI) * hyp);
            if (degrees > 0) {
                newX = - newX;
            }
            //System.err.println("you will throw x " + newX + ", and y " + newY);
            throwItemPerfect(map, item, newX, newY, m);
        } else {
            float hyp = (float)Math.sqrt(absX*absX + absY*absY);
            float oldAngle = (float)Math.asin(absY/hyp);
            if (hyp > distance) {
                m.message("You feel kind of puny...", Color.ORANGE);
            }
            hyp = hyp > distance ? distance : hyp;
            //System.err.println("the hyp is" + hyp + ", and the old Angle is " + ((oldAngle/(2*Math.PI))) * 360 );
            float angle = (degrees + (float)((oldAngle/(2*Math.PI))) * 360);
            //System.err.println(angle);
            int newX = (int)Math.round(Math.cos(angle/360 * 2 * Math.PI) * hyp * wayX);
            int newY = (int)Math.round(Math.sin(angle/360 * 2 * Math.PI) * hyp * wayY);
            //System.err.println("you will throw x " + newX + ", and y " + newY);
            
            
            throwItemPerfect(map, item, newX, newY, m);
        }
    }
    
    public void throwItem(Map map, Item item, Sprite target, MessageHolder m) {
        throwItem(map, item, target.getX(), target.getY(), m);
    }
    
    private void throwItemPerfect(Map map, Item item, int offX, int offY, MessageHolder m) {
        //System.err.println("The " + item + " will land on " + (x + offX) + ", " + (y + offY));
        //TODO send things to the map's items in motion'
        //TODO player takes damage when he tries to throw something that's too heavy!
        if (offX == 0 && offY == 0) {
            takeItem(item);
            map.addItem(item, x, y);
            return;
        }
        
        if (offX == 0) {
            int total = Math.abs(offY);
            int direction = offY/Math.abs(offY);
            for (int a = 0; a <= total; a++) {
                if (!map.isThrowable(x, y+(a*direction))) {
                    //System.err.println("Something's in the way!!!");
                    map.hit(item, x, y+(a*direction), m);
                    takeItem(item);
                    map.addItem(item, x, map.isLandable(x, y +(a*direction)) ? y +(a*direction): y + ((a-1)*direction));
                    map.addItemInMotion(item, x, y, x, map.isLandable(x, y +(a*direction)) ? y +(a*direction): y + ((a-1)*direction));
                    return;
                }
                
                //System.err.println("OK: " + x  + ", " + (y + (a*direction)));
            }
        } else if (offY == 0) {
            int total = Math.abs(offX);
            int direction = offX/Math.abs(offX);
            for (int a = 0; a <= total; a++) {
                if (!map.isThrowable(x +(a*direction) , y)) {
                    //System.err.println("Something's in the way!!!");
                    map.hit(item, x +(a*direction) , y, m);
                    takeItem(item);
                    map.addItem(item, map.isLandable(x +(a*direction) , y) ? x +(a*direction): x + ((a-1)*direction), y);
                    map.addItemInMotion(item, x, y, map.isLandable(x +(a*direction) , y) ? x +(a*direction): x + ((a-1)*direction), y);
                    return;
                }
                
                //System.err.println("OK: " + (x + (a*direction)) + ", " + y);
            }
        } else if ( Math.abs(offX) >  Math.abs(offY) ) {
            int totalX = Math.abs(offX);
            int directionX = offX/Math.abs(offX);
            int totalY = Math.abs(offY);
            int directionY = offY/Math.abs(offY);
            float slope = (float)offY/(float)offX;
            for (int a = 0; a <= totalX; a++) {
                float b = y - (slope * x);
                if (!map.isThrowable(x + (a*directionX), Math.round(slope * (x + (a*directionX)) + b))) {
                    //System.err.println("Something's in the way!!!");
                    map.hit(item, x + (a*directionX), Math.round(slope * (x + (a*directionX)) + b), m);
                    boolean land = map.isLandable(x + (a*directionX), Math.round(slope * (x + (a*directionX)) + b));
                    //System.err.println(land);
                    takeItem(item);
                    map.addItem(item, land ? x + (a*directionX):x + ((a-1)*directionX), land ? Math.round(slope * (x + (a*directionX)) + b):Math.round(slope * (x + ((a-1)*directionX)) + b));
                    map.addItemInMotion(item, x, y, land ? x + (a*directionX):x + ((a-1)*directionX), land ? Math.round(slope * (x + (a*directionX)) + b):Math.round(slope * (x + ((a-1)*directionX)) + b));
                    return;
                }
                //System.err.println("OK: " + (x + (a*directionX)) + ", " + Math.round(slope * (x + (a*directionX)) + b));
            }
        } else if (  Math.abs(offX) <=  Math.abs(offX) ) {
            int totalX = Math.abs(offX);
            int directionX = offX/Math.abs(offX);
            int totalY = Math.abs(offY);
            int directionY = offY/Math.abs(offY);
            float slope = (float) offX / (float) offY;
            for (int a = 0; a <= totalY; a++) {
                float b = x - (slope * y);
                if (!map.isThrowable(Math.round(slope * (y + (a*directionY)) + b), y + (a*directionY))) {
                    //System.err.println("Something's in the way!!!");
                    map.hit(item, Math.round(slope * (y + (a*directionY)) + b), y + (a*directionY), m);
                    boolean land = map.isLandable(Math.round(slope * (y + (a*directionY)) + b), y + (a*directionY));
                    
                    //System.err.println(land);
                    takeItem(item);
                    map.addItem(item, land ? Math.round(slope * (y + (a*directionY)) + b):Math.round(slope * (y + ((a-1)*directionY)) + b), land ? y + (a*directionY):y + ((a-1)*directionY));
                    map.addItemInMotion(item, x, y, land ? Math.round(slope * (y + (a*directionY)) + b):Math.round(slope * (y + ((a-1)*directionY)) + b), land ? y + (a*directionY):y + ((a-1)*directionY));
                    return;
                }
                //System.err.println("OK: " + Math.round(slope * (y + (a*directionY)) + b) + ", " + (y + (a*directionY)));
            }
        }
        map.hit(item, x + offX, y + offY, m);
        takeItem(item);
        map.addItem(item, x + offX, y + offY);
        map.addItemInMotion(item, x, y, x + offX, y + offY);
    }
    
    public int kick(Sprite s) {
        int total = kick();
        int result = s.kickFor(total);
        return result;
    }
    public int kick() {
        int dif = baseMaxAttack - baseMinAttack;
        int rand = (int) (Math.random() * (dif+1));
        Item boot = boots;
        int weaponBonus = (boots == null ? 0 : boots.getAC());
        int strengthBonus = strength/10;
        int total = baseMinAttack + rand + weaponBonus + strengthBonus;
        return total;
    }
    
    public int getAccuracy() {
        return (dexterity*3) + (agility*2);
    }
    
    boolean canSee(int sX, int sY, Map m) {
        if (!m.inBounds(sX, sY)) {
            return false;
        }
        boolean answer = true;
        boolean answera = true;
        boolean answerb = true;
        boolean answerc = true;
        boolean answerd = true;
        int difX = sX - x;
        int difY = sY - y;
        if (Math.sqrt((difX * difX) + (difY*difY)) > light) {
            return false;
        }
        int absX = Math.abs(difX);
        int absY = Math.abs(difY);
        
        int wayX = 0;
        int wayY = 0;
        
        if (difX != 0) {
            wayX = difX/Math.abs(difX);
        }
        if (difY != 0) {
            wayY = difY/Math.abs(difY);
        }
        int mx = sX;
        int my = sY;
        
        if (difY == 0) {
            answer = true;
            for (int i = 1; i < absX; i++) {
                if (answer == false) {
                    answera = false;
                }
                answer = m.isTransparent(x + (i*wayX), y);
            }
            if (answer == false) {
                answera = false;
            }
        }
        if (difX == 0) {
            answer = true;
            for (int i = 1; i < absY; i++) {
                answer = m.isTransparent(x, y + (i* wayY));
                if (answer == false) {
                    answerb = false;
                }
            }
        }
        
        if (absX > absY) {
            answer = true;
            for (int i = 1; i < absX; i++) {
                float slope = (float) difY / (float) difX;
                float b = y - (slope * x);
                answer = m.isTransparent(x + (i*wayX), Math.round(slope * (x + (i*wayX)) + b));
                if (answer == false) {
                    answerc = false;
                }
            }
        }
        
        if (absX <= absY) {
            answer = true;
            for (int i = 1; i < absY; i++) {
                float slope = (float) difX / (float) difY;
                float b = x - (slope * y);
                answer = m.isTransparent(Math.round(slope * (y + (i*wayY)) + b), y + (i*wayY));
                if (answer == false) {
                    answerd = false;
                }
            }
        }
        
        return answera && answerb && answerc && answerd;
    }
    
    public int getMaxHP() {
        return maxHP;
    }
    
    public void addEffect(Effect e) {
        effects.add(new Effect(e));
    }
    
    void removeEmptyStacks() {
        for (int m=0; m<inv.size();m++) {
            if(inv.get(m).size() == 0) {
                inv.remove(m);
            }
        }
    }

    public Item getHead() {
        return head;
    }

    public Item getBelt() {
        return belt;
    }

    public Item getBody() {
        return body;
    }

    public Item getBoots() {
        return boots;
    }

    public Item getCape() {
        return cape;
    }

    public Item getGloves() {
        return gloves;
    }

    public Item getLArm() {
        return lArm;
    }

    public Item getRArm() {
        return rArm;
    }

    public Item getRingA() {
        return ringA;
    }

    public Item getRingB() {
        return ringB;
    }
    

    
    
    
    
    private int x;
    private int y;
    
    private int speed;
    private int baseMinAttack;
    private int baseMaxAttack;
    private int hp;
    private int maxHP;
    private int AC;
    private int exp;
    private int level;
    
    private int strength;
    private int dexterity;
    private int agility;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int luck;
    private int light;
    
    private Image i;
    private ArrayList<ItemStack> inv;
    //private ArrayList<Item> inv;
    private ArrayList<Item> equipInv;
    private Item head;
    private Item lArm;
    private Item rArm;
    private Item ringA;
    private Item ringB;
    private Item belt;
    private Item body;
    private Item cape;
    private Item boots;
    private Item gloves;
    
    private boolean isResting;
    
    private ArrayList<Effect> effects;
    
    private double throwSkill;
    
}
