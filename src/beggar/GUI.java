/*
 * GUI.java
 *
 * @author Juan Valencia
 *
 *
 * Notes:
 *  All of the variables I use are declared at the bottom of this file except for final statics.
 *
 *  The doSomething() loop is essentially the redraw function.  It has several helper functions that should
 *  be pretty straight forward.
 *
 *  In general the classes for items, the map, player, etc... are fairly simple.
 *  The majority of the code/communication/translation is found here, in GUI.java or in Game.java which is
 *  essentially a parser for the game data.
 *
 *  Hopefully the code is pretty easy to read, I would recommend starting by reading the variable descriptions
 *  where I declare them at the bottom of the file.  I've made a lot of variables accessible to the whole class.
 *  In general, I found that giving them values at load time increases speed.  It may not be the cleanest academically
 *  but oh well, maybe I'll fix it later.
 *
 *  The coordinate system is mapped to the viewable area in GUI.java  As far as the Map, player, items, etc are concerned,
 *  They have simple int indexes to indicate what tile they are in.  Those can be translated to onscreen pixel positions
 *  using the transX() and transY() functions.  There is also the global variables offsetX and offsetY which I use in GUI.java
 *  to help the viewport figure out what to draw. (The viewport is figured out as you draw)
 *
 *  The tiles are made from .png images.  The default is 20 pixels x 20 pixels,
 *  though I suppose it wouldn't be hard to change.  They also have transparent backgrounds so that I can layer them.
 *  I've been using paint.net to make the actual images.
 *
 * Created on September 11, 2007, 11:12 PM
 */

package beggar;

import beggar.Item;
import java.awt.Color;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author  Juan
 */
public class GUI extends javax.swing.JFrame {
    
    private final static String[] CHOICES = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
    private final static String GAME = "gamefiles/test.xml";
    private final static int VIEWABLEITEMS = 17;
    private final static Color HIGHLIGHT = new Color(240, 240, 240, 100);
    
    class MyTask extends TimerTask {
        
        public MyTask() {
        }
        public void run() {
            doSomething();
        }
    }
    public void doSomething() {
        if (!wait) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, screenX, screenY+40);
            
            if (isMenu) {
                drawMenu();
            } else if (isOption) {
                drawOptions();
            } else if (isInstructions) {
                drawInstructionsFromMenu();
            } else if (isStory) {
                drawStory();
            } else {
                //These methods wouldbe good as buffered images?
                drawCharacterBox();
                drawInfo();
                
                drawMap();
                drawSprites();
                
                drawItemsInMotion();
                drawPlayer();
                
                
                drawMessages();
                drawFocus();
                drawTarget();                
                drawPrompt();
                
                
                drawActionMenu();
            }
            
            if(p.getHP()<1) {
                drawYouDied();
            }
            
            //Do some buffering for fun!
            graphics.dispose();
            strategy.show();
            graphics = (Graphics2D)strategy.getDrawGraphics();
        }
    }
    
    /** Creates new form GUI */
    public GUI() {
        
        fullScreen = false;
        screenX = 800;
        screenY = 600;
        
        game = new Game(GAME);
        
        possibleTargets = new ArrayList<Sprite>(10);
        target = null;
        
        try {
            FileReader fRead = new FileReader("gamefiles/config.ini");
            BufferedReader config = new BufferedReader(fRead);
            String line = config.readLine();
            
            while (line != null) {
                if (line.contains("fullscreen")) {
                    if (line.endsWith("true")) {
                        fullScreen = true;
                    } else if (line.endsWith("false")) {
                        fullScreen = false;
                    }
                }
                if (line.contains("screenX=")) {
                    screenX = Integer.parseInt(line.substring(8));
                }
                if (line.contains("screenY=")) {
                    screenY = Integer.parseInt(line.substring(8));
                }
                
                line = config.readLine();
            }
            
            config.close();
            
        }catch(Exception e) {
            System.err.println(e.toString());
        }
        
        hasStarted = false;
        isMenu = true;
        isOption = false;
        isInstructions = false;
        pauseForPickup = false;
        runNormal = true;
        youDied = false;
        pauseForFocus = false;
        wait = false;
        throwItem = false;
        
        font = new Font("Arial", Font.PLAIN, 10);
        
        menuFore = new Color(120,120,220);
        //menuBack = new Color(220,220,220);
        menuBack = new Color(244,244,222);
        menuBack = new Color(200,200,255);
        messages = new MessageHolder();
        messages.message("Welcome to Beggar!", Color.CYAN);
        gameTime = 0;
        view_width = 25;
        view_height = 19;
        title = Toolkit.getDefaultToolkit().createImage("img/title.png");
        fog = Toolkit.getDefaultToolkit().createImage("img/fog.png");
        rip = Toolkit.getDefaultToolkit().createImage("img/rip.png");
        p = new Player();
        //map = new Map(150, 150, Map.RANDOM);
        map = game.getCurrentMap();
        //set up the pieces.
        initComponents();
        //set up the buffering
        initBuffering();
        
        
        
        t = new Timer();
        t.schedule(new MyTask(), 0, 30);
        
        optionFullscreen = fullScreen;
        optionScreenX = screenX;
        optionScreenY = screenY;
        
    }
    private void restart() {
        try {
            wait(30);
        } catch (Exception e) {
            
        }
        hasStarted = false;
        isMenu = true;
        isOption = false;
        isInstructions = false;
        pauseForPickup = false;
        runNormal = true;
        youDied = false;
        pauseForFocus = false;
        wait = false;
        throwItem = false;
        
        messages = new MessageHolder();
        messages.message("Welcome to Beggar!", Color.CYAN);
        gameTime = 0;
        
        p = new Player();
        game = new Game(GAME);
        possibleTargets = new ArrayList<Sprite>(10);
        target = null;
        map = game.getCurrentMap();
    }
    /** Sets up the buffering */
    private void initBuffering() {
        //set up the graphics environment
        graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphDevice = graphEnv.getDefaultScreenDevice();
        graphicConf = graphDevice.getDefaultConfiguration();
        oldDisplay = graphDevice.getDisplayMode();
        currentDisplay = oldDisplay;
        displayModes = graphDevice.getDisplayModes();
        
        
        view_width = (screenX - 140)/20;
        view_height = (screenY - 120)/20;
        
        this.setBounds(0, 0, screenX, screenY);
        
        if (fullScreen) {
            try {
                graphDevice.setFullScreenWindow(this);
                graphDevice.setDisplayMode(new DisplayMode(screenX,screenY,graphDevice.getDisplayMode().getBitDepth(), graphDevice.getDisplayMode().getRefreshRate()));
                currentDisplay = graphDevice.getDisplayMode();
                
                view_width = (screenX - 140)/20;
                view_height = (screenY - 120)/20;
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }  else {
            this.setBounds(0, 0, screenX, screenY + 30);
        }
        //Get the bufferstrategy
        canvas1.setBounds(0,0,screenX, screenY);
        canvas1.setIgnoreRepaint(true);
        canvas1.createBufferStrategy(2);
        strategy = canvas1.getBufferStrategy();
        graphics = (Graphics2D)strategy.getDrawGraphics();
        
    }
    
    private void drawWindow(int x, int y, int w, int h) {
        drawWindow(x, y, w, h, menuBack);
    }
    
    private void drawWindow(int x, int y, int w, int h, Color c) {
        graphics.setColor(menuFore);
        graphics.fillRoundRect(x, y, w, h, 8, 8);
        graphics.setColor(c);
        graphics.fillRoundRect(x+3,y+3,w-6,h-6,8,8);
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(x+3,y+3,w-6,h-6,8,8);
    }
    
    
    
    private void drawMenu() {
        graphics.setColor(menuFore);
        graphics.drawImage(title, screenX/2-200, 0, null);
        
        if (hasStarted) {
            graphics.drawString("Re(s)ume the game!", screenX/2 - 100, 250);
        } else {
            graphics.drawString("(S)tart", screenX/2 - 100, 250);
        }
        graphics.drawString("(C)ontinue - Not implemented yet!", screenX/2 - 100, 275);
        graphics.drawString("(O)ptions", screenX/2 - 100, 300);
        graphics.drawString("(H)igh Scores - Not implemented yet!", screenX/2 - 100, 325);
        graphics.drawString("(I)nstructions", screenX/2 - 100, 350);
        graphics.drawString("(Q)uit", screenX/2 - 100, 375);
        
        
        
    }
    private void drawOptions() {
        isMenu = false;
        isOption = true;
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, screenX, screenY);
        
        graphics.setColor(Color.WHITE);
        graphics.drawImage(title, screenX/2-200, 0, null);
        graphics.drawString("(R)esolution:", screenX/2 - 100, 250);
        graphics.drawString("" + optionScreenX + ", " + optionScreenY, screenX/2 + 50, 250);
        
        graphics.drawString("(F)ullscreen:", screenX/2 - 100, 275);
        graphics.drawString(String.valueOf(optionFullscreen), screenX/2 + 50, 275);
        
        graphics.drawString("(S)ave options - This will exit program.", screenX/2 - 100, 325);
        graphics.drawString("(B)ack to the menu", screenX/2 - 100, 350);
        
    }
    private void drawInstructionsFromMenu()  {
        runNormal = false;
        isMenu = false;
        isInstructions = true;
        
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, screenX, screenY);
        
        graphics.setColor(Color.WHITE);
        graphics.drawString("Welcome to Beggar! Press '?' at any time to reach this menu!", screenX/2 - 200, 65);
        graphics.drawLine(0, 80, screenX, 80);
        
        graphics.drawString("You can use the number pad or arrows to move.", screenX/2 - 200, 100);
        graphics.drawString("i - shows inventory", screenX/2 - 150, 125);
        graphics.drawString("u - use", screenX/2 - 150, 150);
        graphics.drawString("e - equip", screenX/2 - 150, 175);
        graphics.drawString(" , - pickup", screenX/2 - 150, 200);
        graphics.drawString("d - drop", screenX/2 - 150, 225);
        graphics.drawString("t - travel (Go up/down stairs, into town, etc)", screenX/2 - 150, 250);
        graphics.drawString("r - rest for 10 turns", screenX/2 - 150, 275);
        graphics.drawString("f - cycle through possible targets (focus on a target)", screenX/2 - 150, 300);
        graphics.drawString("SPACE - Look around (ENTER while looking to select a target)", screenX/2 - 150, 325);
        graphics.drawString("ENTER - Brings up an option menu and lets you select from that menu", screenX/2 - 150, 350);
        graphics.drawString("z = Deselects target creature", screenX/2 - 150, 375);
        graphics.drawString("q - Go back to the menu", screenX/2 - 150, 400);
        graphics.drawString("ESC - Kills the game. ", screenX/2 - 150, 425);
        
        graphics.drawLine(0, 450, screenX, 450);
        graphics.drawString("Press 'z' to go back to the menu.", screenX/2 - 200, 475);
    }
    
    private void drawStory() {
        
        int sizeX = storyImage.getWidth(null);
        int sizeY = storyImage.getHeight(null);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,screenX, screenY);
        graphics.drawImage(storyImage, (screenX-sizeX)/2, (screenY - sizeY)/2, null);
    }
    
    private void drawCharacterBox() {
        //draw the box for character stuff
        drawWindow(0,0,100,135);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(40,20,20,20);
        graphics.drawRect(30,40,40,40);
        graphics.drawRect(5,40,20,40);
        graphics.drawRect(75,40,20,40);
        graphics.drawRect(10,10,20,20);
        graphics.drawRect(70,10,20,20);
        graphics.drawRect(30,85,40,10);
        graphics.drawRect(40,110,20,20);
        graphics.drawRect(10,110,20,20);
        graphics.drawRect(70,110,20,20);
    }
    private void drawInfo() {
        
        
        drawWindow(0,140,100,screenY-140 - 5);
        
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        int placeHold = 160;
        graphics.drawString("HP: " + p.getHP() + "\\" + p.getMaxHP(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Attack: "+p.getAttackRangeString(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("AC: " + p.calculateAC(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Str: "+ p.getStrength(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Dex: "+ p.getDexterity(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Agi: "+ p.getAgility(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Con: "+ p.getConstitution(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Int: "+ p.getIntelligence(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Wis: "+ p.getWisdom(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Cha: "+ p.getCharisma(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Luck: "+ p.getLuck(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Exp: "+ p.getExp() + "\\" + p.getNextLevelExp(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Lbs: "+ (int)p.getWeightCarried() + "\\" + (int)p.getCarryingCapacity(), 5, placeHold);
        placeHold += 15;
        graphics.drawString("Speed: "+p.getSpeed(), 5, placeHold);
        placeHold += 15;
        
        graphics.drawString("X: "+ String.valueOf(p.getX()) + " Y:"+ String.valueOf(p.getY()), 5, placeHold);
        
        
    }
    
    private void drawMap() {
        //draw the map
        if (view_width<map.getWidth() && view_height<map.getHeight()) {
            drawWindow(105,5,view_width * 20 + 7,view_height * 20 +7, Color.BLACK);
        } else if (view_width<map.getWidth()) {
            drawWindow(105,5,view_width * 20 + 7,map.getHeight() * 20 +7, Color.BLACK);
        } else if (view_height<map.getHeight()) {
            drawWindow(105,5,map.getWidth() * 20 + 7,view_height * 20 +7, Color.BLACK);
        } else {
            drawWindow(105,5,map.getWidth() * 20 + 7,map.getHeight() * 20 +7, Color.BLACK);
        }
        
        offsetX = p.getX() - (view_width/2);
        offsetY = p.getY() - (view_height/2);
        
        if (offsetX<0) {
            offsetX = 0;
        }
        
        if (offsetX >= map.getWidth() - view_width) {
            offsetX = map.getWidth() - view_width;
        }
        if (offsetX<0) {
            offsetX = 0;
        }
        
        if (offsetY<0) {
            offsetY = 0;
        }
        
        if (offsetY > map.getHeight() - view_height) {
            offsetY = map.getHeight() - view_height;
        }
        if (offsetY<0) {
            offsetY = 0;
        }
        
        //draw whatever items are on the ground on the map.
        for (int i=0;i<view_width && i<map.getWidth();i++) {
            for (int k=0;k<view_height && k<map.getHeight(); k++) {
                
                if (map.getTile(i+offsetX, k+offsetY).isRevealed()) {
                    graphics.drawImage(map.getBackgroundImage(), transX(i), transY(k), null);
                    graphics.drawImage(map.getImage(i+offsetX, k+offsetY), transX(i), transY(k), null);
                }
                boolean can = p.canSee(i+offsetX, k+offsetY, map);
                if (!can && map.getTile(i+offsetX, k+offsetY).isRevealed()) {
                    graphics.drawImage(fog, transX(i), transY(k), null);
                } else if (can) {
                    map.getTile(i+offsetX, k+offsetY).remember();
                    size = map.getItems(i+offsetX, k+offsetY).size();
                    for(int p = size; p>0; p--) {
                        tempItem = ((Item)(map.getItems(i+offsetX, k+offsetY).get(p-1)));
                        graphics.drawImage(tempItem.getImage(), transX(i)+ (20 - tempItem.getImage().getWidth(null))/2, transY(k) + (20 - tempItem.getImage().getHeight(null))/2, null);
                        
                    }
                }
            }
        }
        
    }
    private void drawSprites() {
        //This draws the sprites which are basically enemies and NPC's
        sprites = map.getEnemies();
        size = sprites.size();
        Sprite s;
        int transX;
        int transY;
        for (int m = 0; m<size;m++){
            try {
                s = sprites.get(m);
                transX = transX(s.getX()-offsetX);
                transY = transY(s.getY()-offsetY);
                if (transX > 105 && transX < view_width * 20 + 105 && transY > 5 && transY < view_height * 20 + 5
                        && transX < map.getWidth() * 20 + 105 && transY < map.getHeight() * 20 + 5  ) {
                    if (p.canSee(s.getX(), s.getY(), map)) {
                        if (!possibleTargets.contains(s)) {
                            possibleTargets.add(s);
                        }
                        graphics.drawImage(s.getImage(), transX, transY, null);
                    } else {
                        if (target == s) {
                            target = null;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
    
    private void drawPlayer() {
        graphics.drawImage(p.getImage(), transX(p.getX()-offsetX), transY(p.getY()-offsetY), null);
        drawEquipped();
    }
    
    private void drawItemsInMotion() {
        ArrayList<ItemInMotion> movingItems = map.getItemsInMotion();
        for (int m = 0; m<movingItems.size(); m++) {
            graphics.drawImage(movingItems.get(m).item.getImage(), transX(movingItems.get(m).startX-offsetX), transY(movingItems.get(m).startY-offsetY), null);
            movingItems.get(m).update(movingItems);
        }
        
    }
    
    private void drawFocus() {
        if (pauseForFocus) {
            int correctX;
            int windowWidth = 600;
            graphics.setColor(Color.WHITE);
            int playerX = transX(p.getX()-offsetX);
            int playerY = transY(p.getY()-offsetY);
            int x = playerX + focusX;
            int y = playerY + focusY;
            
            graphics.drawLine(x, y, x+5, y);
            graphics.drawLine(x, y, x, y+5);
            x += 20;
            graphics.drawLine(x, y, x-5, y);
            graphics.drawLine(x, y, x, y+5);
            y += 20;
            graphics.drawLine(x, y, x-5, y);
            graphics.drawLine(x, y, x, y-5);
            x -= 20;
            graphics.drawLine(x, y, x+5, y);
            graphics.drawLine(x, y, x, y-5);
            
            //graphics.drawRect(playerX + focusX, playerY + focusY, 20, 20);
            graphics.setColor(Color.WHITE);
            correctX = playerX+focusX+windowWidth>screenX ? playerX+focusX+windowWidth - screenX + 20 : 20;
            drawWindow(playerX + focusX - correctX, playerY + 40 + focusY, windowWidth, 30);
            graphics.setColor(Color.WHITE);
            graphics.drawString(map.getDescription(p.getX() + (focusX/20), p.getY() + (focusY/20)), playerX + focusX - correctX + 10, playerY + 40 + focusY + 18);
            graphics.setColor(Color.BLACK);
            graphics.drawString(map.getDescription(p.getX() + (focusX/20), p.getY() + (focusY/20)), playerX + focusX - correctX + 11, playerY + 40 + focusY + 19);
        }
    }
    
    private void drawTarget() {
        if (target != null) {
            if (map.getEnemy(target.getX(), target.getY()) != null) {
                graphics.setColor(Color.WHITE);
                int x = transX(target.getX()-offsetX);
                int y = transY(target.getY()-offsetY);
                graphics.drawLine(x, y, x+5, y);
                graphics.drawLine(x, y, x, y+5);
                x += 20;
                graphics.drawLine(x, y, x-5, y);
                graphics.drawLine(x, y, x, y+5);
                y += 20;
                graphics.drawLine(x, y, x-5, y);
                graphics.drawLine(x, y, x, y-5);
                x -= 20;
                graphics.drawLine(x, y, x+5, y);
                graphics.drawLine(x, y, x, y-5);
                //graphics.drawRect(x, y, 20, 20);}
            } else {
                target = null;
            }
        }
    }
    
    private void drawActionMenu() {
        if (actionMenu) {
            ArrayList<String> options;
            if(actionNonTarget) {
                options = map.getActionOptions(p, actionNonTargetX, actionNonTargetY);
            } else {
                options = map.getActionOptions(p, target);
            }
            if (options.size() > 0) {
                int correctX = target == null ? transX(p.getX()-offsetX) - 100 : transX(target.getX()-offsetX)- 100 ;
                int correctY = target == null ? transY(p.getY()-offsetY) + 20: transY(target.getY()-offsetY) + 20;
                drawWindow(correctX, correctY, 100 + 7, options.size() * 20 + 7);
                
                graphics.setColor(HIGHLIGHT);
                int choice = actionChoice % options.size();
                graphics.fillRect(correctX + 5, correctY + 5 + (20*choice), 100, 18);
                
                graphics.setColor(Color.BLACK);
                for (int q = 0; q < options.size(); q++) {
                    graphics.drawString(options.get(q), correctX+10, correctY+18+(20*q));
                }
            }
        }
    }
    
    private void drawEquipped() {
        //drawEquipped is a helper function to drawPlayer
        ArrayList<Item> invToDraw = p.getEquippedInv();
        int s = invToDraw.size();
        for (int m = s;m>0;m--) {
            tempItem = ((Item)invToDraw.get(m-1));
            graphics.drawImage(tempItem.getImage(), transX(p.getX()-offsetX)+tempItem.getOffsetX(), transY(p.getY()-offsetY)+tempItem.getOffsetY(), null);
            
        }
    }
    
    private void drawMessages() {
        int num = 6;
        MessageHolder.Message[] m = messages.getMessages(num);
        
        int s = m.length;
        graphics.setColor(m[0].color);
        graphics.drawString(m[0].message, 120, screenY - (15*num));
        for (int n =1;n<s;n++) {
            graphics.setColor(new Color(m[n].color.getRed()-(n*m[n].color.getRed()/7), m[n].color.getGreen()-(n*m[n].color.getGreen()/7), m[n].color.getBlue() - (n*m[n].color.getBlue()/7)));
            graphics.drawString(m[n].message, 120, screenY - (15*num-((n)*15)));
        }
        
        //graphics.setColor(Color.WHITE);
        //graphics.drawString(messages.getMessage(gameTime), 120, 465);
    }
    public void message(String s) {
        messages.message(s, gameTime);
    }
    
    public void message(String s, Color c) {
        messages.message(s, gameTime, c);
    }
    
    private void drawPrompt() {
        if(pauseForPickup == true) {
            drawWindow(140, 40, 460, 420);
            graphics.setColor(Color.BLACK);
            graphics.drawString("Which item would you like to pick up? (Choose a letter)", 160, 60);
            graphics.drawLine(145, 65, 590, 65);
            
            //TODO when there is multiple items on the ground, translate it into stacks or something
            ArrayList<Item> onGround = map.getItems(p.getX(), p.getY());
            drawItemListForPrompt(onGround);
            
            graphics.drawString("z) Exit this menu", 160, 450);
            
        } else if (pauseForInventory == true) {
            drawWindow(140, 40, 460, 420);
            graphics.setColor(Color.BLACK);
            if (dropNow) {
                graphics.drawString("Which item would you like to drop?", 160, 60);
            } else if (equipNow && !choosePart) {
                graphics.drawString("Which item would you like to equip or unequip?", 160, 60);
            } else if (useNow) {
                graphics.drawString("Which item would you like to use?", 160, 60);
            } else if (choosePart) {
                graphics.drawString("Where would you like to equip the " + chosenItem.toString() + "?", 160, 60);
            } else if (throwItem) {
                graphics.drawString("Choose an item to throw:", 160, 60);
            } else {
                graphics.drawString("Player Inventory:  Would you like to (d)rop, (e)quip or unequip, or (u)se?", 160, 60);
            }
            graphics.drawLine(145, 65, 590, 65);
            
            if (!choosePart) {
                drawItemListForPrompt(p.getInv(), p.getEquippedInv());
            } else {
                graphics.drawString("a) head - " + (p.getHead() == null ? "" : p.getHead().getName()), 160, 80 + 0);
                graphics.drawString("b) left arm - " + (p.getLArm() == null ? "" : p.getLArm().getName()), 160, 80 + 20);
                graphics.drawString("c) right arm - " + (p.getRArm() == null ? "" : p.getRArm().getName()), 160, 80 + 40);
                graphics.drawString("d) left ring finger - " + (p.getRingA() == null ? "" : p.getRingA().getName()), 160, 80 + 60);
                graphics.drawString("e) right ring finger - " + (p.getRingB() == null ? "" : p.getRingB().getName()), 160, 80 + 80);
                graphics.drawString("f) belt - " + (p.getBelt() == null ? "" : p.getBelt().getName()), 160, 80 + 100);
                graphics.drawString("g) body - " + (p.getBody() == null ? "" : p.getBody().getName()), 160, 80 + 120);
                graphics.drawString("h) cape - " + (p.getCape() == null ? "" : p.getCape().getName()), 160, 80 + 140);
                graphics.drawString("i) boots - " + (p.getBoots() == null ? "" : p.getBoots().getName()), 160, 80 + 160);
                graphics.drawString("j) gloves - " + (p.getGloves() == null ? "" : p.getGloves().getName()), 160, 80 + 180);
            }
            graphics.drawLine(145, 420, 590, 420);
            graphics.drawString("z) Exit this menu", 160, 450);
        }
    }
    private void drawItemListForPrompt(ArrayList<ItemStack> list, ArrayList<Item> equipped) {
        boolean isEquipped = false;
        int invshow = list.size()- (promptMultiplier * VIEWABLEITEMS);
        if (!(invshow<=VIEWABLEITEMS)) {
            invshow = VIEWABLEITEMS;
            tooManyItems = true;
        } else {
            tooManyItems = false;
        }
        for (int m=0; m<invshow;m++) {
            int actualItem = m + (VIEWABLEITEMS * promptMultiplier);
            //System.err.println("actual item" + actualItem);
            try {
                
                ItemStack tempstack = (list.get(actualItem));
                graphics.drawString(CHOICES[m] + ")", 160, 80 + (m*20));
                graphics.drawImage(tempstack.getImage(), 180 - (tempstack.getImage().getWidth(null)/2), 82 + (m*20) - (tempstack.getImage().getHeight(null)), null);
                if (equipped != null) {
                    isEquipped = equipped.contains(tempstack.get());
                } else {
                    isEquipped = false;
                }
                if (isEquipped) {
                    graphics.drawString("(equipped) " + list.get(actualItem).getName(), 200, 80 + (m*20));
                } else {
                    graphics.drawString(list.get(actualItem).getName(), 200, 80 + (m*20));
                }
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        graphics.drawLine(145, 420, 590, 420);
        if (tooManyItems) {
            if(promptMultiplier>0) { sExtra = " or press backspace to go back."; } else { sExtra = ":"; }
            graphics.drawString("Press space for more options" + sExtra, 160, 435);
        } else if (promptMultiplier>0) {
            graphics.drawString("Press backspace to go back.", 160, 435);
        }
    }
    
    private void drawItemListForPrompt(ArrayList<Item> list) {
        
        int invshow = list.size()- (promptMultiplier * VIEWABLEITEMS);
        if (!(invshow<=VIEWABLEITEMS)) {
            invshow = VIEWABLEITEMS;
            tooManyItems = true;
        } else {
            tooManyItems = false;
        }
        for (int m=0; m<invshow;m++) {
            int actualItem = m + (VIEWABLEITEMS * promptMultiplier);
            //System.err.println("actual item" + actualItem);
            try {
                
                Item tempItem = list.get(actualItem);
                graphics.drawString(CHOICES[m] + ")", 160, 80 + (m*20));
                graphics.drawImage(tempItem.getImage(), 180 - (tempItem.getImage().getWidth(null)/2), 82 + (m*20) - (tempItem.getImage().getHeight(null)), null);
                
                graphics.drawString(list.get(actualItem).getName(), 200, 80 + (m*20));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        graphics.drawLine(145, 420, 590, 420);
        if (tooManyItems) {
            if(promptMultiplier>0) { sExtra = " or press backspace to go back."; } else { sExtra = ":"; }
            graphics.drawString("Press space for more options" + sExtra, 160, 435);
        } else if (promptMultiplier>0) {
            graphics.drawString("Press backspace to go back.", 160, 435);
        }
    }
    
    private void drawYouDied() {
        drawWindow(screenX/4, screenY/4, screenX/2, screenY/2);
        graphics.drawImage(rip, screenX/4 + 15, screenY/4 + 15, null);
        
        graphics.setColor(Color.BLACK);
        graphics.drawString("You died!!", screenX/4 +130, screenY/4 + 30);
        graphics.drawString("Press (z) to restart.", screenX/4 + 30, screenY*3/4 - 15);
        
        runNormal = false;
        youDied = true;
    }
    
    private void drawGrid() {
        //draw a grid for help placing things
        graphics.setColor(Color.LIGHT_GRAY);
        for (int i=20;i<screenX;i=i+20) {
            graphics.drawLine(i, 0, i, screenY);
        }
        for (int i=20;i<screenY;i=i+20) {
            graphics.drawLine(0, i, screenX, i);
        }
    }
    private int transX(int x) {
        return 109 + 20*x;
    }
    private int transX(double x) {
        return 109 + (int)(20*x);
    }
    private int transY(int y) {
        return 9 + 20*y;
    }
    private int transY(double y) {
        return 9 + (int)(20*y);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        canvas1 = new java.awt.Canvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Beggar");
        setMinimumSize(new java.awt.Dimension(640, 480));
        setResizable(false);
        if (fullScreen) {
            setUndecorated(true);
        }
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        canvas1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        canvas1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                canvas1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        handleKeyTyped(evt);
    }//GEN-LAST:event_formKeyPressed
    
    private void canvas1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_canvas1KeyPressed
        handleKeyTyped(evt);
    }//GEN-LAST:event_canvas1KeyPressed
    
    private void handleKeyTyped(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        int code;
        if (isMenu) {
            handleMenuKeyProcess(key);
        } else if (isStory) {
            handleStoryKeyProcess(key);
        } else if (isOption) {
            handleOptionsKeyProcess(key);
        } else if (isInstructions) {
            handleInstructionKeyProcess(key);
        } else if (youDied) {
            handleDeathKeyProcess(key);
        } else if (runNormal) {
            handleNormalKeyProcess(key);
        } else if (actionMenu) {
            handleActionMenuKeys(key);
        } else if (pauseForFocus) {
            handleLookAndChoose(key);
        } else if (pauseForPickup == true) {
            handlePickupKeyProcess(key);
        } else if (pauseForInventory == true) {
            handleInventoryKeyProcess(key);
        }
        if (key == KeyEvent.VK_ESCAPE) { quit(); }
    }
    
    private void handleMenuKeyProcess(int key) {
        if (key == KeyEvent.VK_S) {
            isMenu = false;
            if (hasStarted) {
                isStory = false;
            } else {
                isStory = true;
            }
            String str;
            story = new ArrayList<String>(30);
            
            String file = map.getStoryFile();
            if (file != null) {
                storyImage = Toolkit.getDefaultToolkit().createImage(file);
            }
        }
        if (key == KeyEvent.VK_O) {
            drawOptions();
        }
        if (key == KeyEvent.VK_I) {
            drawInstructionsFromMenu();
        }
        if (key == KeyEvent.VK_Q) {
            quit();
        }
    }
    
    private void handleStoryKeyProcess(int key) {
        isStory = false;
        hasStarted = true;
        runNormal = true;
    }
    
    private void handleOptionsKeyProcess(int key) {
        if (key == KeyEvent.VK_R) {
            swapResolutionOption();
        }
        if (key == KeyEvent.VK_F) {
            optionFullscreen = !optionFullscreen;
        }
        if (key == KeyEvent.VK_S) {
            saveOptionsFromMenu();
        }
        if (key == KeyEvent.VK_B) {
            isMenu = true;
            isOption = false;
        }
    }
    
    private void handleInstructionKeyProcess(int key) {
        if (key == KeyEvent.VK_Z) {
            isMenu = true;
            isInstructions = false;
            runNormal = true;
        }
    }
    
    private void handleDeathKeyProcess(int key) {
        if (key == KeyEvent.VK_Z) {
            restart();
        }
    }
    
    private void handleActionMenuKeys(int key) {
        if (key == KeyEvent.VK_ENTER) {
            actionMenu = false;
            runNormal = true;
            ArrayList<String> options;
            if (actionNonTarget) {
                target = null;
                options = map.getActionOptions(p, actionNonTargetX, actionNonTargetY);
            } else {
                options = map.getActionOptions(p, target);
            }
            
            int choice = actionChoice%options.size();
            String action = options.get(choice);
            if (action.equals("Rest")) {
                rest(10);
            } else if (action.equals("Chat")) {
                chat();
            } else if (action.equals("Throw")) {
                throwItem();
            } else if (action.equals("Deselect")) {
                target = null;
            } else if (action.equals("Nothing")) {
                
            } else if (action.equals("Pickup")) {
                pickup();
            } else if (action.equals("Inventory")) {
                inventory();
            } else if (action.equals("Travel")) {
                travel();
            } else if (action.equals("Attack")) {
                if (target != null) {
                    attack(target.getX(), target.getY());
                } else {
                    map.attackTile(p, actionNonTargetX, actionNonTargetY, messages);                    
                    time(p.getSpeed());
                }
            } else if (action.equals("Kick")) {
                kick();
            }
        }
        if (key == KeyEvent.VK_UP|| key == KeyEvent.VK_NUMPAD8) {
            if (actionChoice == 0) {
                ArrayList<String> options;
                options = map.getActionOptions(p, target);
                actionChoice = options.size();
            }
            actionChoice--;
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2) {
            actionChoice++;
        }
        if (key == KeyEvent.VK_Z) {
            actionMenu = false;
            runNormal = true;
        }
    }
    
    private void handleLookAndChoose(int key) {
        if (key == KeyEvent.VK_SPACE) {
            runNormal = true;
            pauseForFocus = false;
        }
        if (key == KeyEvent.VK_ENTER) {
            if (map.getEnemy(p.getX() + (focusX/20), p.getY() + ((focusY)/20)) != null) {
                target = map.getEnemy(p.getX() + (focusX/20), p.getY() + ((focusY)/20));
                runNormal = true;
                pauseForFocus = false;
                actionMenu();
            } else {
                runNormal = true;
                pauseForFocus = false;
                actionMenu(p.getX() + (focusX/20), p.getY() + ((focusY)/20));
            }
        }
        
        //TODO Make sure you can see the place!!!!!
        
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD8) {
            if (p.canSee(p.getX() + (focusX/20), p.getY() + ((focusY-20)/20), map)) {
                focusY += -20;
            }
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2) {
            if (p.canSee(p.getX() + (focusX/20), p.getY() + ((focusY+20)/20), map)) {
                focusY += 20;
            }
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_NUMPAD4) {
            if (p.canSee(p.getX() + ((focusX-20)/20), p.getY() + ((focusY)/20), map)) {
                focusX += -20;
            }
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_NUMPAD6) {
            if (p.canSee(p.getX() + ((focusX+20)/20), p.getY() + ((focusY)/20), map)) {
                focusX += 20;
            }
        }
        if (key == KeyEvent.VK_NUMPAD7) {
            if (p.canSee(p.getX() + ((focusX-20)/20), p.getY() + ((focusY-20)/20), map)) {
                focusY += -20;
                focusX += -20;
            }
        }
        if (key == KeyEvent.VK_NUMPAD3) {
            if (p.canSee(p.getX() + ((focusX+20)/20), p.getY() + ((focusY+20)/20), map)) {
                focusY += 20;
                focusX += 20;
            }
        }
        if (key == KeyEvent.VK_NUMPAD1) {
            if (p.canSee(p.getX() + ((focusX-20)/20), p.getY() + ((focusY+20)/20), map)) {
                focusY += 20;
                focusX += -20;
            }
        }
        if (key == KeyEvent.VK_NUMPAD9) {
            if (p.canSee(p.getX() + ((focusX+20)/20), p.getY() + ((focusY-20)/20), map)) {
                focusY += -20;
                focusX += 20;
            }
        }
    }
    
    private void handleNormalKeyProcess(int key){
        if (key == KeyEvent.VK_Q) {
            isMenu = true;
            runNormal = false;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD8) {
            handleMove(p.getX(), p.getY()-1);
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2) {
            handleMove(p.getX(), p.getY()+1);
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_NUMPAD4) {
            handleMove(p.getX()-1, p.getY());
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_NUMPAD6) {
            handleMove(p.getX()+1, p.getY());
        }
        if (key == KeyEvent.VK_NUMPAD7) {
            handleMove(p.getX()-1, p.getY()-1);
        }
        if (key == KeyEvent.VK_NUMPAD3) {
            handleMove(p.getX()+1, p.getY()+1);
        }
        if (key == KeyEvent.VK_NUMPAD1) {
            handleMove(p.getX()-1, p.getY()+1);
        }
        if (key == KeyEvent.VK_NUMPAD9) {
            handleMove(p.getX()+1, p.getY()-1);
        }
        if (key == KeyEvent.VK_NUMPAD5) {
            time(p.getSpeed());
        }
        if (key == KeyEvent.VK_COMMA) {
            pickup();
        }
        if (key == KeyEvent.VK_I) {
            inventory();
        }
        if (key == KeyEvent.VK_D) {
            drop();
        }
        if (key == KeyEvent.VK_E) {
            equip();
        }
        if (key == KeyEvent.VK_U) {
            use();
        }
        if (key == KeyEvent.VK_T) {
            travel();
        }
        if (key == KeyEvent.VK_R) {
            rest(10);
        }
        if (key == KeyEvent.VK_C) {
            chat();
        }
        if (key == KeyEvent.VK_Z) {
            target = null;
        }
        if (key == KeyEvent.VK_SPACE) {
            look();
        }
        if (key == KeyEvent.VK_ENTER) {
            actionMenu();
        }
        if (key == KeyEvent.VK_F) {
            targetNext();
        }
        if (key == KeyEvent.VK_SLASH) {
            drawInstructionsFromMenu();
        }
    }
    
    private void handlePickupKeyProcess(int key) {
        if (key == KeyEvent.VK_Z) {
            pauseForPickup = false;
            runNormal = true;
        }
        if (key == KeyEvent.VK_SPACE && tooManyItems) {
            promptMultiplier++;
        }
        if (key == KeyEvent.VK_BACK_SPACE && promptMultiplier>0) {
            promptMultiplier--;
        }
        int whichItem = key-65;
        try { //Might want to be more specific.  Right now it just goes out of bounds and does nothin.
            if (whichItem<22 && whichItem>-1) {
                p.giveItem((Item)(map.getItems(p.getX(), p.getY()).get(whichItem+ (VIEWABLEITEMS * promptMultiplier))));
                messages.message("Picked up a " + map.getItems(p.getX(), p.getY()).get(whichItem+ (VIEWABLEITEMS * promptMultiplier)).toString(), gameTime, Color.YELLOW);
                map.getItems(p.getX(), p.getY()).remove(whichItem+ (VIEWABLEITEMS * promptMultiplier));
                
                if (map.getItems(p.getX(), p.getY()).size() <= 0) {
                    pauseForPickup = false;
                    runNormal = true;
                }
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    private void handleInventoryKeyProcess(int key) {
        if (key == KeyEvent.VK_Z) {
            pauseForInventory = false;
            runNormal = true;
        } else if (key == KeyEvent.VK_SPACE && tooManyItems) {
            promptMultiplier++;
        } else if (key == KeyEvent.VK_BACK_SPACE && promptMultiplier>0) {
            promptMultiplier--;
        } else if (dropNow == true) {
            int whichItem = key-65;
            try { //Might want to be more specific.  Right now it just goes out of bounds and does nothin.
                if (whichItem<VIEWABLEITEMS && whichItem>-1) {
                    chosenItem = p.getInv().get(whichItem + (VIEWABLEITEMS * promptMultiplier)).get();
                    if (p.isEquipped(chosenItem)) {
                        System.err.println(chosenItem);
                        p.unEquip(chosenItem);
                    }
                    p.takeItem(chosenItem);
                    p.removeEmptyStacks();
                    
                    map.addItem(chosenItem, p.getX(), p.getY());
                    messages.message("Just dropped a " + chosenItem.toString(), gameTime, Color.YELLOW);
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else if (equipNow == true && !choosePart) {
            int whichItem = key-65;
            try { //Might want to be more specific.  Right now it just goes out of bounds and does nothin.
                if (whichItem<VIEWABLEITEMS && whichItem>-1) {
                    chosenItem = p.getInv().get(whichItem + (VIEWABLEITEMS * promptMultiplier)).get();
                    if (p.isEquipped(chosenItem)) {
                        p.unEquip(chosenItem);
                        choosePart = false;
                        messages.message("You just took off a " + chosenItem.toString(), gameTime, Color.YELLOW);
                    } else {
                        choosePart = true;
                    }
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else if (useNow == true) {
            int whichItem = key-65;
            try { //Might want to be more specific.  Right now it just goes out of bounds and does nothin.
                if (whichItem<VIEWABLEITEMS && whichItem>-1) {
                    chosenItem = p.getInv().get(whichItem+ (VIEWABLEITEMS * promptMultiplier)).remove();
                    p.removeEmptyStacks();
                    applyItemEffects(chosenItem);
                    useNow = false;
                    pauseForInventory = false;
                    runNormal = true;
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else if (throwItem) {
            int whichItem = key-65;
            try { //Might want to be more specific.  Right now it just goes out of bounds and does nothin.
                if (whichItem<VIEWABLEITEMS && whichItem>-1) {
                    chosenItem = p.getInv().get(whichItem + (VIEWABLEITEMS * promptMultiplier)).get();
                    
                    //System.err.println(chosenItem);
                    messages.message("You just threw a " + chosenItem.getName() + ".", gameTime);
                    if (actionNonTarget) {
                        p.throwItem(map, chosenItem, actionNonTargetX, actionNonTargetY, messages);
                    } else {
                        p.throwItem(map, chosenItem, target, messages);
                    }
                    time(p.getSpeed());
                    
                    throwItem = false;
                    pauseForInventory = false;
                    runNormal = true;
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else if (choosePart == true) {
            int whichItem = key-65;
            boolean a = false;
            switch (whichItem) {
                case 0: a = p.equipHead(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 1: a = p.equipLeftArm(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 2: a = p.equipRightArm(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 3: a = p.equipLRing(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 4: a = p.equipRRing(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                
                case 5: a = p.equipBelt(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 6: a = p.equipBody(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 7: a = p.equipCape(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 8: a = p.equipBoots(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
                case 9: a = p.equipGloves(chosenItem); choosePart = false; pauseForInventory = false; runNormal = true; break;
            }
            if (a) {
                messages.message("You just equipped a " + chosenItem.toString(), gameTime, Color.YELLOW);
            } else {
                messages.message("You can't equip a " + chosenItem.toString() + " there!", gameTime, Color.PINK);
            }
            //these need to go after you check to see if dropping is active.  Otherwise you will drop item d as soon as you select it.
        } else if (key == KeyEvent.VK_D && !equipNow && !useNow && !throwItem) {
            dropNow = true;
        } else if (key == KeyEvent.VK_E && !dropNow && !useNow && !throwItem) {
            equipNow = true;
        } else if (key == KeyEvent.VK_U && !equipNow && !dropNow && !throwItem) {
            useNow = true;
        }
    }
    
    //TODO Need to check to make sure you can move somewhere before you actually do
    // For example if there is a sprite there.  You can't walk there!
    private boolean isWalkable(int x, int y) {
        return map.isWalkable(x, y);
    }
    private boolean isTransparent(int x, int y) {
        return map.isTransparent(x, y);
    }
    private void handleMove(int x, int y) {
        int code;
        int oldX = p.getX();
        int oldY = p.getY();
        if (isWalkable(x, y)) {
            p.move(x, y);
            time(p.getSpeed() * Math.sqrt(((oldX - x) * (oldX - x)) + ((oldY - y)*(oldY - y))));
            message(map.getDescription(x, y));
        } else {
            //Have game interpret what player means?
            code = map.getActionCode(x, y);
            action(code, x, y);
        }
    }
    
    private void pickup() {
        if (map.getItems(p.getX(), p.getY()).size() > 1) {
            pauseForPickup = true;
            runNormal = false;
            promptMultiplier = 0;
            tooManyItems = false;
        } else if  (map.getItems(p.getX(), p.getY()).size() > 0) {
            messages.message("You just picked up a " + ((Item)map.getItems(p.getX(), p.getY()).get(0)).getName() + "!" , gameTime, Color.YELLOW);
            p.giveItem((Item)map.getItems(p.getX(), p.getY()).remove(0));
            
        }
    }
    private void inventory() {
        choosePart = false;
        useNow = false;
        equipNow = false;
        dropNow = false;
        throwItem = false;
        pauseForInventory = true;
        runNormal = false;
        promptMultiplier = 0;
        tooManyItems = false;
        
    }
    private void drop() {
        inventory();
        dropNow = true;
    }
    private void equip() {
        inventory();
        equipNow = true;
    }
    private void use() {
        inventory();
        useNow = true;
    }
    private void look() {
        runNormal = false;
        pauseForFocus = true;
        focusX = 0;
        focusY = 0;
    }
    private void chat() {
        if (target != null) {
            time(p.getSpeed());
            message(target.getName() + " says \"" + target.getChat() + "\"", Color.GREEN);
            
        }
    }
    private void throwItem() {
        //System.err.println("Im throwing");
        throwItem = true;
        runNormal = false;
        choosePart = false;
        useNow = false;
        equipNow = false;
        dropNow = false;
        
        pauseForInventory = true;
    }
    
    private void quit() {
        saveOptions();
        System.exit(0);
    }
    private void travel() {
        String place = map.getPlace(p.getX(), p.getY());
        String oldMapName = map.getName();
        if (place != null) {
            wait = true;
            target = null;
            time(0);
            map = game.getMap(place);
            //TODO fix refresh bug that causes you to look out of bounds!
            String tileConnectionInfo = map.getConnectTile(oldMapName).type;
            String[] split = tileConnectionInfo.split(":");
            p.move(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            messages.message("We're going to " + place + "!", Color.CYAN);
            String file = map.getStoryFile();
            String str = null;
            if (file != null) {
                storyImage = Toolkit.getDefaultToolkit().createImage(file);
                isStory = true;
                runNormal = false;
            }
            wait = false;
        } else {
            messages.message("There's nowhere to go.", Color.PINK);
        }
    }
    
    private void rest(double r) {
        p.rest();
        message("ZZZ...", Color.GREEN);
        while (r>0) {
            time(50);
            r--;
            if (!p.isResting()) {
                message("You've been quite rudely awakened!", Color.GREEN);
                break;
            }
        }
        p.wake();
    }
    
    private void saveOptions() {
        try {
            FileOutputStream options = new FileOutputStream("gamefiles/config.ini");
            PrintStream p = new PrintStream( options );
            
            p.println("####################################################");
            p.println("#                                                  #");
            p.println("# Set fullscreen=true to turn fullscreen mode on!  #");
            p.println("#                                                  #");
            p.println("####################################################");
            p.println("");
            p.println("fullscreen=" + String.valueOf(fullScreen));
            p.println("");
            
            p.println("####################################################");
            p.println("#                                                  #");
            p.println("# screenX tells you how many pixels wide           #");
            p.println("# the screen shouldbe.                             #");
            p.println("# screenY tells you how many pixels tall           #");
            p.println("#                                                  #");
            p.println("####################################################");
            p.println("");
            p.println("screenX=" + String.valueOf(screenX));
            p.println("");
            p.println("screenY=" + String.valueOf(screenY));
            p.println("");
            
            p.close();
        } catch(Exception e) {
            System.err.println("Error writing to file");
        }
        
    }
    private void saveOptionsFromMenu() {
        fullScreen = optionFullscreen;
        screenX = optionScreenX;
        screenY = optionScreenY;
        saveOptions();
        System.exit(0);
    }
    private void swapResolutionOption() {
        if (optionScreenX == 800) {
            optionScreenX = 1280;
            optionScreenY = 1024;
        } else if (optionScreenX == 1280) {
            optionScreenX = 640;
            optionScreenY = 480;
        } else if (optionScreenX == 640) {
            optionScreenX = 800;
            optionScreenY = 600;
        }
    }
    
    private void action(int code, int x, int y) {
        if (code == Map.ENEMY) {
            String name = map.getEnemy(x, y).toString();
            int attack = map.getEnemy(x, y).recieveAttack(p.attack(), p.getAccuracy());
            if (attack == 0) {
                messages.message("You completely missed the " + name + "!", gameTime, Color.PINK);
            } else {
                messages.message("You hit the " + name + " for " + attack + " hp!", gameTime, Color.GREEN);
            }
            time(p.getSpeed());
        } else if(code == Map.DOOR) {
            message(map.openDoor(p, x, y), Color.YELLOW);
            time(p.getSpeed());
            return;
        } else if(code == Map.FRIEND) {
            message(map.getEnemy(x, y).getName() + " says " + map.getEnemy(x, y).getChat(), Color.GREEN);
            time(p.getSpeed());
            return;
        } else if (code == Map.NONE) {
            return;
        }
    }
    private void attack(int x, int y) {
        String name = map.getEnemy(x, y).toString();
        int attack = map.getEnemy(x, y).recieveAttack(p.attack(), p.getAccuracy());
        if (attack == 0) {
            messages.message("You completely missed the " + name + "!", gameTime, Color.PINK);
        } else {
            messages.message("You hit the " + name + " for " + attack + " hp!", gameTime, Color.GREEN);
        }
        time(p.getSpeed());
    }
    private void time(double t) {
        possibleTargets.clear();
        sprites = map.getEnemies();
        for (int m = 0;m<sprites.size();m++) {
            sprites.get(m).update(t, map, p, messages);
        }
        p.passTime(t, messages);
        gameTime = gameTime + t;
        gameTime = Math.round(gameTime);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    
    private void applyItemEffects(Item chosen) {
        ArrayList<Effect> effects = chosen.getEffects();
        if (effects.size() == 0) {
            message("You can't think of what to do with it.", Color.WHITE);
            p.giveItem(chosen);
            time(5);
            return;
        }
        
        for(int m =0;m<effects.size();m++) {
            if (!effects.get(m).hasUses()) {
                message("Nothing happens.", Color.WHITE);
                p.giveItem(chosen);
                time(10);
                return;
            }
            if (p.isEquipped(chosen)) {
                p.unEquip(chosen); // this inadvertantly puts the chosen item back in your inv.
                //remove it!!
                p.takeItem(chosen);
                message("You take off the " + chosen.getName() + " to use it...", Color.YELLOW);
            }
            
            if (effects.get(m).getType() == Effect.NONE) {
                time(5);
            } else if (effects.get(m).getType() == Effect.SELF) {
                p.addEffect(effects.get(m));
                message(effects.get(m).getMessage(), Color.MAGENTA);
                chosen.use();
                time(p.getSpeed() * 2);
                if (chosen.getType().contains("Scroll")) {
                    
                } else if (chosen.getType().contains("Potion")) {
                    Item potionContainer = map.getNewItem(chosen.getContainedBy(), chosen.getMaterial());
                    if (potionContainer != null) {
                        p.giveItem(potionContainer);
                    }
                } else {
                    p.giveItem(chosen);
                }
            } else if (effects.get(m).getType() == Effect.TOUCH) {
                
            } else if (effects.get(m).getType() == Effect.BOLT) {
                
            } else if (effects.get(m).getType() == Effect.BALL) {
                
            }
        }
    }
    
    public void targetNext() {
        if (possibleTargets.size() > 0) {
            if (possibleTargets.contains(target)) {
                int index = possibleTargets.indexOf(target);
                if (possibleTargets.size()-1 == index) {
                    target = possibleTargets.get(0);
                } else {
                    target = possibleTargets.get(index + 1);
                }
            } else {
                target = possibleTargets.get(0);
            }
        } else {
            target = null;
        }
    }
    
    public void actionMenu() {
        actionChoice = 0;
        runNormal = false;
        actionMenu = true;
        actionNonTarget = false;
    }
    public void actionMenu(int x, int y) {
        //System.err.println("I'm in the action meny for no target!");
        actionChoice = 0;
        runNormal = false;
        actionMenu = true;
        actionNonTarget = true;
        target = null;
        actionNonTargetX = x;
        actionNonTargetY = y;
    }
    
    public void kick() {
        if (target != null) {
            map.kick(p, p.getBoots(), target.getX(), target.getY(), messages);
        } else {
            map.kick(p, p.getBoots(), actionNonTargetX, actionNonTargetY, messages);
        }
        time(p.getSpeed());
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Canvas canvas1;
    // End of variables declaration//GEN-END:variables
    private GraphicsEnvironment graphEnv;
    private GraphicsDevice graphDevice;
    private GraphicsConfiguration graphicConf;
    private BufferStrategy strategy;
    private Graphics2D graphics;
    private DisplayMode oldDisplay;
    private DisplayMode currentDisplay;
    private DisplayMode[] displayModes;
    
    private int view_width;
    private int view_height;
    private int screenX;
    private int screenY;
    
    private boolean hasStarted;
    private boolean isMenu;
    private boolean isOption;
    private boolean isInstructions;
    private boolean isStory;
    private boolean youDied;
    private boolean pauseForPickup;
    private boolean pauseForInventory;
    private boolean runNormal;
    private boolean fullScreen;
    private boolean dropNow;
    private boolean useNow;
    private boolean equipNow;
    private boolean choosePart;
    private boolean pauseForFocus;
    private boolean wait;
    private boolean actionMenu;
    private boolean throwItem;
    
    private boolean actionNonTarget;
    int actionNonTargetX;
    int actionNonTargetY;
    
    private boolean optionFullscreen;
    private int optionScreenX;
    private int optionScreenY;
    
    private Image title;
    private Image rip;
    
    private Player p;
    private Game game;
    private Map map;
    private Timer t;
    private int size;
    
    private ArrayList<Sprite> sprites;
    private ArrayList<ItemStack> itemsForPickup;
    private ArrayList<ItemStack> inv;
    private ArrayList<ItemStack> invToDraw;
    
    private Font font;
    private int offsetX;
    private int offsetY;
    private Color menuFore;
    private Color menuBack;
    private Image fog;
    private int focusX;
    private int focusY;
    
    private int promptMultiplier;
    private boolean tooManyItems;
    private Item chosenItem;
    
    private ArrayList<String> story;
    private Image storyImage;
    private MessageHolder messages;
    private double gameTime;
    
    private String sExtra;
    private Item tempItem;
    private int actionChoice;
    
    private Sprite target;
    private ArrayList<Sprite> possibleTargets;
    
    
}






