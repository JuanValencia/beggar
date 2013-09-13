 /*
 * Item.java
 *
 * Created on September 15, 2007, 10:24 AM
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
public class Item {
    public final static int NONE = 999;
    public final static int BLACK = 0;
    public final static int ORANGE = 1;
    public final static int GREY = 2;
    public final static int SILVER = 3;
    public final static int YELLOW = 4;
    public final static int GREEN = 5;
    public final static int BROWN = 6;
    public final static int WHITE = 7;
    public final static int RED = 8;
    public final static int BLUE = 9;
    
    
    
        
    /** Creates a new instance of Item */
  
    public Item(Item item, int threat) {
        // TODO make sure this function always hits all the important parts.
        name = item.getName();
        i = item.getImage();
        offsetX = item.offsetX;
        offsetY = item.offsetY;
        attackMin = item.attackMin;
        attackMax = item.attackMax;
        AC = item.AC;
        frequency = item.getFrequency();
        
        ArrayList<Effect> temp = item.getEffects();
        effects = new ArrayList<Effect>(10);
        if (temp!= null) {
            for(int m=0; m<temp.size();m++) {
                effects.add(new Effect(temp.get(m)));
            }
        }
        createMaterials();
        materialType = item.getMaterialType();
        
        broken = item.broken;
        
        black = item.black;
        silver = item.silver;
        white = item.white;
        yellow = item.yellow;
        
        orange = item.orange;
        green = item.green;
        grey = item.grey;
        brown = item.brown;       
        
        int s;
        int itemPower;
        int rand;
        if (materialType.equals("Metal")) {
            s = metals.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = metals.get(rand).name + " " + name;
            material = metals.get(rand);
        } else if (materialType.equals("Paper")) {
            s = papers.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = papers.get(rand).name + " " + name;
            material = papers.get(rand);
        } else if (materialType.equals("Leather")) {
            s = leathers.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = leathers.get(rand).name + " " + name;
            material = leathers.get(rand);
        } else if (materialType.equals("Wood")) {
            s = woods.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = leathers.get(rand).name + " " + name;
            material = woods.get(rand);
        } else if (materialType.equals("Crystal")) {
            s = crystals.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = leathers.get(rand).name + " " + name;
            material = crystals.get(rand);
        } else if (materialType.equals("Stone")) {
            s = stones.size();
            itemPower = (int)(s * threat/100);
            rand = (int)(Math.random() * itemPower);
            if (rand >= s) {
                rand = s - 1;
            }
            //name = leathers.get(rand).name + " " + name;
            material = stones.get(rand);
        }
        
        switch (material.color) {
            case NONE: break;
            case BLACK: i = black == null ? i : black; break;
            case SILVER: i = silver == null ? i : silver; break;
            case WHITE: i = white == null ? i : white; break;
            case YELLOW: i = yellow == null ? i : yellow; break;
            
            case ORANGE: i = orange == null ? i : orange; break;
            case GREEN: i = green == null ? i : green; break;
            case GREY: i = grey == null ? i : grey; break;
            case BROWN: i = brown == null ? i : brown; break;
            
            case RED: i = red == null ? i : red; break;
            case BLUE: i = blue == null ? i : blue; break;
        }
        
        type = item.getType();
        used = 0;
        containedBy = item.getContainedBy();
        baseWeight = item.getBaseWeight();
    }
    
    
    public Item(Item item, Material m) {
        name = item.getName();
        i = item.getImage();
        offsetX = item.offsetX;
        offsetY = item.offsetY;
        attackMin = item.attackMin;
        attackMax = item.attackMax;
        AC = item.AC;
        frequency = item.getFrequency();
        
        ArrayList<Effect> temp = item.getEffects();
        effects = new ArrayList<Effect>(10);
        if (temp!= null) {
            for(int n=0; n<temp.size();n++) {
                effects.add(new Effect(temp.get(n)));
            }
        }
        createMaterials();
        materialType = item.getMaterialType();
        
        broken = item.broken;
        
        black = item.black;
        silver = item.silver;
        white = item.white;
        yellow = item.yellow;
        
        orange = item.orange;
        green = item.green;
        grey = item.grey;
        brown = item.brown;       
        
        red = item.red;
        blue = item.blue;
        
        int s;
        int itemPower;
        int rand;
        
        material = m;
        
        switch (m.color) {
            case NONE: break;
            case BLACK: i = black == null ? i : black; break;
            case SILVER: i = silver == null ? i : silver; break;
            case WHITE: i = white == null ? i : white; break;
            case YELLOW: i = yellow == null ? i : yellow; break;
            
            case ORANGE: i = orange == null ? i : orange; break;
            case GREEN: i = green == null ? i : green; break;
            case GREY: i = grey == null ? i : grey; break;
            case BROWN: i = brown == null ? i : brown; break;
            
            case RED: i = red == null ? i : red; break;
            case BLUE: i = blue == null ? i : blue; break;
        }
        
        type = item.getType();
        used = 0;
        containedBy = item.getContainedBy();
        baseWeight = item.getBaseWeight();
    }
    
    public Item(String rawStringFromFile) {
        //System.err.println(rawStringFromFile);
        String[] split = rawStringFromFile.split(",");
        
        name = split[0].substring(1, split[0].length() - 1);
        i = Toolkit.getDefaultToolkit().createImage(removeQuotes(split[1]));
        String temp = removeQuotes(split[2]);
        broken = extractImage(temp);
        offsetX = Integer.valueOf(split[4]);
        offsetY = Integer.valueOf(split[5]);
        
        temp = removeQuotes(split[6]);
        black = extractImage(temp);
        
        temp = removeQuotes(split[7]);
        silver = extractImage(temp);
        
        temp = removeQuotes(split[8]);
        white = extractImage(temp);
        
        temp = removeQuotes(split[9]);
        yellow = extractImage(temp);
        
        temp = removeQuotes(split[10]);
        orange = extractImage(temp);
        
        temp = removeQuotes(split[11]);
        green = extractImage(temp);
        
        temp = removeQuotes(split[12]);
        grey = extractImage(temp);
        
        temp = removeQuotes(split[13]);
        brown = extractImage(temp);
        
        temp = removeQuotes(split[14]);
        red = extractImage(temp);
        
        temp = removeQuotes(split[15]);
        blue = extractImage(temp);
        
        attackMin = Integer.valueOf(split[17]);
        attackMax = Integer.valueOf(split[18]);
        AC = Integer.valueOf(split[19]);
        
        effects = new ArrayList<Effect>(10);
        String[] stringEffects = removeQuotes(split[20]).split(":");
        for (int m = 0; m < stringEffects.length; m++) {
            if (!stringEffects[m].equals("None")) {
                effects.add(new Effect(stringEffects[m]));
            }
        }
        
        frequency = Integer.valueOf(split[21]);
        createMaterials();
        
        materialType = removeQuotes(split[22]);
        material = null;
        type = removeQuotes(split[23]);
        used = 0;
        containedBy = removeQuotes(split[24]);
        baseWeight = Double.valueOf(split[25]);
    } 
    
    private String removeQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }
    
    private Image extractImage(String s) {
        if (s.equals("None")) {
            return null;
        }
        else return Toolkit.getDefaultToolkit().createImage(s);
    }
    
    private void createMaterials() {
        metals = new ArrayList<Material>(15);
        
        metals.add(new Material("Aluminum", 2.5, 1.0, 0, NONE));
        metals.add(new Material("Tin", 7.2, 1.5, 1, GREY)); 
        metals.add(new Material("Copper", 5.9, 2.0, 2, BROWN));        
        metals.add(new Material("Bronze", 6.0, 2.5, 3, ORANGE));                      
        metals.add(new Material("Iron", 7.8, 3.0, 4, GREY));        
        metals.add(new Material("Nickel", 8.8, 4.0, 5, SILVER));
        metals.add(new Material("Steel", 7.8, 5.0, 6, GREY));        
        metals.add(new Material("Silver", 8.0, 7.0, 7, SILVER));
        metals.add(new Material("Gold", 8.0, 10.0, 8, YELLOW));        
        metals.add(new Material("Platinum", 8.4, 15.0, 9, WHITE));
        metals.add(new Material("Meteoric", 10.0, 5.0, 10, BLACK));        
        metals.add(new Material("Titanium", 4.5, 25.0, 11, SILVER));
        metals.add(new Material("Mithril", 2.5, 50.0, 12, WHITE));
        
        papers = new ArrayList<Material>(10);
        
        papers.add(new Material("Leaf", .05, 0.0, 0, GREEN));       
        papers.add(new Material("Paper", .05, 1.0, 1, NONE));        
        papers.add(new Material("Tagboard", .1, 1.5, 2, WHITE));
        papers.add(new Material("Cardboard", .2, 2.0, 3, YELLOW));
        papers.add(new Material("Bark", .3, 1.0, 4, BROWN));
        papers.add(new Material("Leather", 1.0, 2.0, 5, BLACK));
        papers.add(new Material("Papyrus", .5, 5.0, 6, ORANGE));
        papers.add(new Material("Stone", 5.0, 1.0, 7, GREY));
        
        leathers = new ArrayList<Material>(15);
        
        leathers.add(new Material("Mole skin", .5, 1.0, 0, YELLOW));          
        leathers.add(new Material("Leather", 1.0, 2.0, 1, ORANGE));     
        leathers.add(new Material("Hardened Leather", 1.5, 3.0, 2, BROWN)); 
        leathers.add(new Material("Snake skin", .3, 4.0, 3, GREEN));     
        leathers.add(new Material("Elephant Hide", 3.0, 5.0, 6, GREY));     
        leathers.add(new Material("Troll hide", 3.5, 6.0, 8, ORANGE));     
        leathers.add(new Material("Wyrm Hide", 4.0, 60.0, 9, BLACK));  
        leathers.add(new Material("Dragon Scale", .5, 75.0, 10, GREEN));
        
        woods = new ArrayList<Material>(15);
        
        woods.add(new Material("Fir", 1.0, 4.0, 0, BROWN));
        woods.add(new Material("Pine", 1.2, 5.0, 1, YELLOW));
        woods.add(new Material("Teak", 1.3, 8.0, 2, BROWN));
        woods.add(new Material("Walnut", 1.7, 10.0, 3, YELLOW));
        woods.add(new Material("Red Oak", 2.0, 15.0, 4, ORANGE));
        woods.add(new Material("Maple", 2.0, 10.0, 5, YELLOW));
        woods.add(new Material("Mahogeny", 2.0, 25.0, 6, BROWN));
        woods.add(new Material("Cherry", 1.3, 30.0, 7, ORANGE));
        woods.add(new Material("Rosewood", 1.0, 50.0, 8, ORANGE));
        woods.add(new Material("Heart Pine", 1.6, 70.0, 9, YELLOW));
        woods.add(new Material("Deathwood", 2.0, 80.0, 10, BLACK));
        
        crystals = new ArrayList<Material>(15);
        
        crystals.add(new Material("Glass", 2.0, 1.0, 0, NONE));
        crystals.add(new Material("Opal", 2.0, 5.0, 1, BLACK));
        crystals.add(new Material("Ruby", 2.0, 10.0, 2, RED));
        crystals.add(new Material("Moonstone", 2.0, 15.0, 3, BLUE));
        crystals.add(new Material("Amethyst", 2.0, 25.0, 4, BLACK));
        crystals.add(new Material("Quartz", 2.0, 5.0, 5, NONE));
        crystals.add(new Material("Garnet", 2.0, 15.0, 6, RED));
        crystals.add(new Material("Emerald", 2.0, 40.0, 7, GREEN));
        crystals.add(new Material("Topaz", 2.0, 60.0, 8, BLUE));
        crystals.add(new Material("Saphire", 2.0, 70.0, 9, BLUE));
        crystals.add(new Material("Ruby", 2.0, 80.0, 10, RED));
        crystals.add(new Material("Diamond", 2.0, 100.0, 11, NONE));
        
        stones = new ArrayList<Material>(15);
        
        stones.add(new Material("Shale", 2.0, .1, 0, BROWN));
        stones.add(new Material("Sandstone", 2.0, .1, 0, YELLOW));
        stones.add(new Material("Limestone", 2.0, .1, 0, WHITE));
        stones.add(new Material("Marble", 5.0, 20, 2, GREEN));
        stones.add(new Material("Basalt", 4.0, 2, 2, BLACK));
        stones.add(new Material("Granite", 5.0, 3, 2, GREY));
        stones.add(new Material("Diorite", 4.0, 4, 2, ORANGE));
    }
    
    public double getWeight() {
        return baseWeight * material.weight;
    }

    public double getBaseWeight() {
        return baseWeight;
    }

    public String getType() {
        return type;
    }

    public String getContainedBy() {
        return containedBy;
    }
    

    public ArrayList<Effect> getEffects() {
        return effects;
    }
    
    public int getAttackBonus() {
        return attackMin + ((int)(Math.random() * (attackMax - attackMin)) + 1) + material.hardness;
    }
    public Item setAttackBonus(int min, int max) {
        attackMin = min;
        attackMax = max;
        return this;
    }
    
    public int getAC() {
        return AC + material.hardness;
    }
    
    public Image getImage() {
        return i;
    }
    public String getName() {
        String timesUsed = "";
        if (used > 0) {
            if (used ==1) {
                timesUsed = "  (Used " + used + " time)";
            } else {
                timesUsed = "  (Used " + used + " times)";
            }
        }
        
        if (material != null) {
            return material.name + " " + name + timesUsed;
        } else {
            return name + timesUsed;
        }
    }
    public int getOffsetX() {
        return offsetX;
    }
    public int getOffsetY() {
        return offsetY;
    }
    
    public void setImage(Image image) {
        i = image;
    }
    public void setName(String newName) {
        name = newName;
    }
    public void setOffsetX(int offX) {
        offsetX = offX;
    }
    public void setOffsetY(int offY) {
        offsetY = offY;
    }
    
    public String toString() {
        return material.name + " " + name;
    }
    
    public int getFrequency() {
        return frequency;
    }

    public String getMaterialType() {
        return materialType;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAttackMin() {
        return attackMin + material.hardness;
    }

    public int getAttackMax() {
        return attackMax + material.hardness;
    }
    
    public boolean isHelmet() {
        return type.contains("Helmet");
    }
    public boolean isWieldable() {
        return type.contains("Wieldable");
    }
    public boolean isRing() {
        return type.contains("Ring");
    }
    public boolean isBelt() {
        return type.contains("Belt");
    }
    public boolean isBody() {
        return type.contains("Body");
    }
    public boolean isCape() {
        return type.contains("Cape");
    }
    public boolean isBoots() {
        return type.contains("Boots");
    }
    public boolean isGloves() {
        return type.contains("Gloves");
    }
    
    public void use() {
        for (int m = 0; m<effects.size(); m++) {
            effects.get(m).use();
            used++;
        }
    }
    
    private Image i;
    private String name;
    private int offsetX;
    private int offsetY;
    
    private int attackMin;
    private int attackMax;
    public int AC;
    private int frequency;
    private String materialType;
    private String type;
    private double baseWeight;
    
    private ArrayList<Effect> effects;
    private ArrayList<Material> metals;
    private ArrayList<Material> papers;
    private ArrayList<Material> leathers;
    private ArrayList<Material> woods;
    private ArrayList<Material> crystals;
    private ArrayList<Material> stones;
    private Material material;
    
    private Image broken;
    private Image black;
    private Image silver;
    private Image white;
    private Image yellow;
    private Image orange;
    private Image green;
    private Image grey;
    private Image brown;
    private Image red;
    private Image blue;
    
    private String containedBy;
    
    private int used;
    
}
