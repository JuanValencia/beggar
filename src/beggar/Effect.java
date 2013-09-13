/*
 * Effect.java
 *
 * Created on October 21, 2007, 4:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

/**
 *
 * @author Juan
 */
public class Effect {
    
    public final static int NONE = 0;
    public final static int SELF = 1;
    public final static int TOUCH = 2;
    public final static int BOLT = 3;
    public final static int BALL = 4;
    
    /** Creates a new instance of Effect */
    public Effect(String effect) {
        name = effect;
        type = NONE;
        String parse = effect.split(" ")[1];
        int parsedValue = Integer.parseInt(parse);
        parse = effect.split(" ")[2];
        int parsedDuration = Integer.parseInt(parse);
        parse = effect.split(" ")[3];
        int parsedSuccess = Integer.parseInt(parse);
        parse = effect.split(" ")[4];
        int parsedUses = Integer.parseInt(parse);
        parse = effect.split(" ")[5];
        
        if (effect.contains("self")) {
            type = SELF;
        } else if (effect.contains("touch")) {
            type = TOUCH;
        } else if (effect.contains("bolt")) {
            type = BOLT;
        } else if (effect.contains("ball")) {
            type = BALL;
        }
        
        if (effect.contains("Heal")) {
            healNum = parsedValue;
        }
        
        if (effect.contains("Poison")) {
            poisonNum = parsedValue;
        }
        
        if (effect.contains("Antidote")) {
            antidoteNum = parsedValue;
        }
        
        usesLeft = parsedUses;
        successrate = parsedSuccess;
        bonusStat = parse;
        duration = parsedDuration;
    }
    
    public Effect(Effect e) {
        name = e.getName();
        type = NONE;
        String parse = name.split(" ")[1];
        int parsedValue = Integer.parseInt(parse);
        parse = name.split(" ")[2];
        int parsedDuration = Integer.parseInt(parse);
        parse = name.split(" ")[3];
        int parsedSuccess = Integer.parseInt(parse);
        parse = name.split(" ")[4];
        int parsedUses = Integer.parseInt(parse);
        parse = name.split(" ")[5];
        
        if (name.contains("self")) {
            type = SELF;
        } else if (name.contains("touch")) {
            type = TOUCH;
        } else if (name.contains("bolt")) {
            type = BOLT;
        } else if (name.contains("ball")) {
            type = BALL;
        }
        
        if (name.contains("Heal")) {
            healNum = parsedValue;
        }
        if (name.contains("Poison")) {
            poisonNum = parsedValue;
        }
        if (name.contains("Antidote")) {
            antidoteNum = parsedValue;
        }
        
        usesLeft = parsedUses;
        successrate = parsedSuccess;
        bonusStat = parse;
        duration = parsedDuration;
        
        name = e.getName();
    }

    public String getName() {
        return name;
    }
    public int getHealPoisonAmount() {
        return antidoteNum;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getType() {
        return type;
    }
    
    public String toString() {
        return name + ": "+ duration;
    }

    public int getSuccessRate() {
        return successrate;
    }
    
    public int getHealAmount() {
        return healNum;
    }

    public int getPoisonAmount() {
        if (duration > 0) {
            return poisonNum;
        }
        return 0;
    }
    

    boolean isDone() {
        if (duration <= 0) {
            return true;
        }
        return false;
    }

    void reduce() {
        duration--;
    }
    
    void use() {
        usesLeft--;
    }
    
    boolean hasUses() {
        return usesLeft == 0 ? false : true;
    }
    
    String getMessage() {
        if (name.contains("Healself")) {
            return "The tips of your toes begin to tingle!";
        }
        if (name.contains("Poison")) {
            return "Your heart quickens...";
        }
        if (name.contains("Antidote")) {
            return "Your heart slows...";
        }
        else {
            return "Something happened.";
        }
    }
    
    private String name;
    private int type;
    
    private int healNum;
    private int poisonNum;
    private int antidoteNum;
    
    private int useNum;
    private int duration;
    private int successrate;
    private int usesLeft;
    private String bonusStat;
    
}
