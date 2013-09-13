/*
 * Game.java
 *
 * Created on October 14, 2007, 3:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.*;

/**
 *
 * @author Juan
 */
public class Game {
    
    /** Creates a new instance of Game */
    public Game(String name) {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        List list;
        
        maps = new ArrayList<Map>(20);
        tileThemes = new ArrayList<TileTheme>(20);
        spriteThemes = new ArrayList<SpriteTheme>(20);
        itemThemes = new ArrayList<ItemTheme>(20);
        
        try {
            doc = builder.build(name);            
            
            //code for making Tilethemes (I think it's ok)
            //makeTileThemes();
            
            //code for making spriteTheme
            makeSpriteThemes();
    
            //code for making itemTheme
            makeItemThemes();
            
            
            //code for making Tilethemes (I think it's ok)
            makeTileThemes(getItemTheme("Drop"));
            
            //code for making maps
            list = doc.getRootElement().getChildren("map");
            makeMaps(list);
            
            connectMapsTogether();
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JDOMException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public TileTheme getTileTheme(String n) {
        for(int m = 0;m<tileThemes.size();m++) {
            if (tileThemes.get(m).getName().equalsIgnoreCase(n)) {
                return tileThemes.get(m);
            }
        }
        return new TileTheme("");
    }
    
    public SpriteTheme getSpriteTheme(String n) {
        for(int m = 0;m<spriteThemes.size();m++) {
            if (spriteThemes.get(m).getName().equalsIgnoreCase(n)) {
                
                return spriteThemes.get(m);
            }
        }
        return new SpriteTheme("");
    }
    public ItemTheme getItemTheme(String n) {
        for(int m = 0;m<itemThemes.size();m++) {
            if (itemThemes.get(m).getName().equalsIgnoreCase(n)) {
                return itemThemes.get(m);
            }
        }
        return new ItemTheme("");
    }
    
    private String extractSpriteThemes(String rawString) {
        String ans = extractPart(rawString, 19);
        ans = ans.substring(1, ans.length() - 1);
        return ans;
    }
    private String extractItemThemes(String rawString) {
        String ans = extractPart(rawString, 3);
        ans = ans.substring(1, ans.length() - 1);
        return ans;
    }
    private String extractTileThemes(String rawString) {
        String ans = extractPart(rawString, 7);
        ans = removeQuotes(ans);
        return ans;
    }
    
    private String extractPart(String raw, int part) {
        String[] split = raw.split(",");
        return split[part];
    }
    private String removeQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }
    
    public Map getCurrentMap() {
        return maps.get(0);
    }

    private void makeTileThemes(ItemTheme drop) {
        TileTheme tempTheme;
        String theme;
        String str;
        ArrayList<String> fileStrings = new ArrayList<String>(100);
        
        try {
            BufferedReader in = new BufferedReader(new FileReader("gamefiles/tile.csv"));
            str = in.readLine();
            while ((str = in.readLine()) != null) {
                fileStrings.add(str);
            }
            in.close();
        } catch (IOException e) {
        }
        
        ArrayList<String> variousThemes = new ArrayList<String>(100);
        for(int m = 0;m<fileStrings.size();m++) {
            str = fileStrings.get(m);            
            String themes = extractTileThemes(str);
            String[] eachTheme = themes.split(" ");
            
            for(int n = 0; n < eachTheme.length; n++) {
                if (!variousThemes.contains(eachTheme[n])) {
                    variousThemes.add(eachTheme[n]);
                }
            }
        }
        
        for(int m = 0; m<variousThemes.size(); m++) {
            tempTheme = new TileTheme(variousThemes.get(m));
            for (int n =0; n< fileStrings.size(); n++) {
                if (extractTileThemes(fileStrings.get(n)).contains(variousThemes.get(m))) {
                    tempTheme.add(new Tile(fileStrings.get(n), drop));
                }
            }
            //System.err.println(tempTheme);
            tileThemes.add(tempTheme);
        }     
    }

    private void makeSpriteThemes() {
        SpriteTheme tempTheme;
        String theme;
        String str;
        ArrayList<String> fileStrings = new ArrayList<String>(100);
        
        try {
            BufferedReader in = new BufferedReader(new FileReader("gamefiles/sprite.csv"));
            str = in.readLine();
            while ((str = in.readLine()) != null) {
                fileStrings.add(str);
            }
            in.close();
        } catch (IOException e) {
        }
        
        ArrayList<String> variousThemes = new ArrayList<String>(100);
        for(int m = 0;m<fileStrings.size();m++) {
            str = fileStrings.get(m);            
            String themes = extractSpriteThemes(str);
            String[] eachTheme = themes.split(" ");
            
            for(int n = 0; n < eachTheme.length; n++) {
                if (!variousThemes.contains(eachTheme[n])) {
                    variousThemes.add(eachTheme[n]);
                }
            }
        }
        
        for(int m = 0; m<variousThemes.size(); m++) {
            tempTheme = new SpriteTheme(variousThemes.get(m));
            for (int n =0; n< fileStrings.size(); n++) {
                if (extractSpriteThemes(fileStrings.get(n)).contains(variousThemes.get(m))) {
                    tempTheme.add(new Sprite(fileStrings.get(n)));
                }
            }
            spriteThemes.add(tempTheme);
        }     
    }
    
    private void makeItemThemes() {
        
        ItemTheme tempTheme;
        String theme;
        String str;
        ArrayList<String> fileStrings = new ArrayList<String>(100);
        
        try {
            BufferedReader in = new BufferedReader(new FileReader("gamefiles/item.csv"));
            str = in.readLine();
            while ((str = in.readLine()) != null) {
                fileStrings.add(str);
            }
            in.close();
        } catch (IOException e) {
        }
        ArrayList<String> variousThemes = new ArrayList<String>(100);
        for(int m = 0;m<fileStrings.size();m++) {
            str = fileStrings.get(m);            
            String themes = extractItemThemes(str);
            String[] eachTheme = themes.split(" ");
            
            for(int n = 0; n < eachTheme.length; n++) {
                if (!variousThemes.contains(eachTheme[n])) {
                    variousThemes.add(eachTheme[n]);
                }
            }
        }
        
        for(int m = 0; m<variousThemes.size(); m++) {
            tempTheme = new ItemTheme(variousThemes.get(m));
            for (int n =0; n< fileStrings.size(); n++) {
                if (extractItemThemes(fileStrings.get(n)).contains(variousThemes.get(m))) {
                    tempTheme.add(new Item(fileStrings.get(n)));
                }
            }
            itemThemes.add(tempTheme);
        }   
    }

    private void makeMaps(List list) {
        
        Element el;
        Element temp;
        
        for (int m = 0; m < list.size(); m++) {
            el = (Element)list.get(m);
            temp = el.getChild("width");
            int w = Integer.valueOf(temp.getTextTrim());
            temp = el.getChild("height");
            int h = Integer.valueOf(temp.getTextTrim());
            
            temp = el.getChild("name");
            String n = temp.getTextTrim();
            
            temp = el.getChild("icon");
            Image i = Toolkit.getDefaultToolkit().createImage(temp.getTextTrim());
                        
            String sTheme = el.getChild("theme").getTextTrim();
            String generate = el.getChild("generation").getTextTrim();
            String sEnemyTheme = el.getChild("enemyTheme").getTextTrim();
            String sItemTheme = el.getChild("itemTheme").getTextTrim();
            String connection = el.getChild("connection").getTextTrim();
            String story = el.getChild("story").getTextTrim();
            String symbol  = el.getChild("symbol").getTextTrim();
            int threat = Integer.valueOf(el.getChild("threat").getTextTrim());
            String backgroundImageName = el.getChild("background").getTextTrim();
            
            maps.add(new Map(n, w, h, i, getTileTheme(sTheme), getSpriteTheme(sEnemyTheme), getItemTheme(sItemTheme), generate, connection, story, symbol, backgroundImageName, threat));
        }
    }

    private void connectMapsTogether() {
        for (int a = 0; a < maps.size(); a++) {
            maps.get(a).connect(maps);
        }
    }

    public Map getMap(String place) {
        for (int a = 0; a < maps.size(); a++) {
            if (maps.get(a).getName().equalsIgnoreCase(place)) {
                return maps.get(a);
            }
        }
        
        return null;
    }
    
    private ArrayList<Map> maps;
    private ArrayList<TileTheme> tileThemes;
    private ArrayList<SpriteTheme> spriteThemes;
    private ArrayList<ItemTheme> itemThemes;
    
}
