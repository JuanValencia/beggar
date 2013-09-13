/*
 * Map.java
 *
 * Created on September 13, 2007, 11:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beggar;

import beggar.Item;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class Map {
    
    final static int NONE = 0;
    final static int ENEMY = 1;
    final static int RANDOM = 2; 
    final static int DOOR = 3;  
    final static int FRIEND = 4;
    
    private class GameImage {
        public Image image;
        public String description;
        public GameImage(Image i, String d) {
            image = i;
            description = d;
        }
    }
    private class Place {
        public int x;
        public int y;
        public Place(int a, int b) {
            x = a;
            y = b;
        }
        public int distance(int a, int b) {
            int absoluteDifferenceX = Math.abs(x-a);
            int absoluteDifferenceY = Math.abs(y-b);
            return (int)Math.sqrt((double)(absoluteDifferenceX*absoluteDifferenceX + absoluteDifferenceY*absoluteDifferenceY));
        }
        public String toString() {
            return "" +x+","+y;
        }
    }
    private class Room {
        public Place topLeft;
        public Place bottomRight;
        public ArrayList<Place> doors;
        public Room(Place tl, Place br) {
            topLeft = tl;
            bottomRight = br;
            doors = new ArrayList<Place>(4);
        }
        public int getWidth() {
            return (Math.abs(topLeft.x - bottomRight.x)) + 1;
        }
        public int getHeight() {
            return (Math.abs(topLeft.y - bottomRight.y)) + 1;
        }
        public boolean sharesBorder(Room r) {
            if(topLeft.x == r.bottomRight.x && topLeft.y == r.bottomRight.y) {
                return false;
            }
            if(r.topLeft.x == bottomRight.x && r.topLeft.y == bottomRight.y) {
                return false;
            }
            if(bottomRight.x == r.topLeft.x && topLeft.y == r.bottomRight.y) {
                return false;
            }
            if(r.bottomRight.x == topLeft.x && r.topLeft.y == bottomRight.y) {
                return false;
            }
            
            boolean top = false;
            boolean right = false;
            boolean left = false;
            boolean bottom = false;
            if (topLeft.y == r.bottomRight.y) {
                if (topLeft.x <= r.topLeft.x && bottomRight.x >= r.topLeft.x) {
                    top = true;
                } else if (topLeft.x >= r.topLeft.x && topLeft.x <= r.bottomRight.x) {
                    top = true;
                }
            } else if (bottomRight.x == r.topLeft.x) {
                if (topLeft.y <= r.topLeft.y && bottomRight.y >= r.topLeft.y) {
                    right = true;
                } else if (topLeft.y >= r.topLeft.y && topLeft.y <= r.bottomRight.y) {
                    right = true;
                }
            } else if (topLeft.x == r.bottomRight.x) {
                if (topLeft.y <= r.topLeft.y && bottomRight.y >= r.topLeft.y) {
                    left = true;
                } else if (topLeft.y >= r.topLeft.y && topLeft.y <= r.bottomRight.y) {
                    left = true;
                }
            } else if (bottomRight.y == r.topLeft.y) {
                if (topLeft.x <= r.topLeft.x && bottomRight.x >= r.topLeft.x) {
                    bottom = true;
                } else if (topLeft.x >= r.topLeft.x && topLeft.x <= r.bottomRight.x) {
                    bottom = true;
                }
            }   
            return top || right || left || bottom;
        }
        public boolean hasDoorBetween(Room r) {
            for (int m = 0; m < doors.size(); m++) {
                for (int n = 0; n < r.doors.size(); n++) {
                    if (doors.get(m).x == r.doors.get(n).x && doors.get(m).y == r.doors.get(n).y) {
                        return true;
                    }
                }
            }
            return false;
        }
        public void drawDoorBetween(Room r) {
            Place newDoor = null;
            if (topLeft.y == r.bottomRight.y) {
                if (topLeft.x <= r.topLeft.x && bottomRight.x >= r.topLeft.x) {
                    int dif = Math.abs(bottomRight.x - r.topLeft.x);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(r.topLeft.x + dif, topLeft.y);
                } else if (topLeft.x >= r.topLeft.x && topLeft.x <= r.bottomRight.x) {
                    int dif = Math.abs(r.bottomRight.x - topLeft.x);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(topLeft.x + dif, topLeft.y);
                }
            } else if (bottomRight.x == r.topLeft.x) {
                if (topLeft.y <= r.topLeft.y && bottomRight.y >= r.topLeft.y) {
                    int dif = Math.abs(bottomRight.y - r.topLeft.y);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(r.topLeft.x, r.topLeft.y + dif);
                } else if (topLeft.y >= r.topLeft.y && topLeft.y <= r.bottomRight.y) {
                    int dif = Math.abs(r.bottomRight.y - topLeft.y);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(r.topLeft.x, topLeft.y + dif);
                }
            } else if (topLeft.x == r.bottomRight.x) {
                if (topLeft.y <= r.topLeft.y && bottomRight.y >= r.topLeft.y) {
                    int dif = Math.abs(bottomRight.y - r.topLeft.y);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(topLeft.x, r.topLeft.y + dif);
                } else if (topLeft.y >= r.topLeft.y && topLeft.y <= r.bottomRight.y) {
                    int dif = Math.abs(r.bottomRight.y - topLeft.y);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(topLeft.x, topLeft.y + dif);
                }
            } else if (bottomRight.y == r.topLeft.y) {
                if (topLeft.x <= r.topLeft.x && bottomRight.x >= r.topLeft.x) {
                    int dif = Math.abs(bottomRight.x - r.topLeft.x);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(r.topLeft.x + dif, r.topLeft.y);
                } else if (topLeft.x >= r.topLeft.x && topLeft.x <= r.bottomRight.x) {
                    int dif = Math.abs(r.bottomRight.x - topLeft.x);
                    dif = (int)(Math.random() * (dif-1)) + 1;
                    newDoor = new Place(topLeft.x + dif, r.topLeft.y);
                }
            }
            doors.add(newDoor);
            r.doors.add(newDoor);
            /*
            System.err.println("=================");
            System.err.println(this);
            System.err.println("--");
            System.err.println(r);
            System.err.println("--");
            System.err.println(newDoor);
             **/
        }
        public String toString() {
            return topLeft + "~" +new Place(bottomRight.x, topLeft.y) + "\n" + new Place(topLeft.x, bottomRight.y) + "~" + bottomRight;
        }
    }
    
    /** Creates a new instance of Map 
        Map is width X height in size.
     *  From left to right the indexes are 0 to (width - 1)
     *  From top to bottom, the indexes are 0 to (height - 1)
     */ 
  
    public Map(String mapName, int w, int h, Image icon, TileTheme theme, SpriteTheme sTheme, ItemTheme iTheme, String generate, String connect, String story, String sym, String bgImageName, int athreat) {
        name = mapName;
        width = w;
        height = h;
        threat = athreat;
        myPic = icon;
        connections = connect;
        tileTheme = theme;
        spriteTheme = sTheme;
        itemTheme = iTheme;       
        enemies = new ArrayList(100);
        itemsInMotion = new ArrayList(10);
        Sprite s;
        Item item;
        isStory = true;
        storyFile = story;
        symbol = sym;
        backgroundImage = Toolkit.getDefaultToolkit().createImage(bgImageName);
        
        if (generate.contains("random")) {
            // TODO write random code using the themes
            map = new Tile[w][h];
            enemies = new ArrayList(200);
            for (int i=0; i<w; i++) {
                for (int k=0; k<h; k++) {
                    if (i==0||k==0||i==w-1||k==h-1) {
                        map[i][k] = tileTheme.getRandomSolidTile();
                    } else {
                        //TODO only put in a tile sometimes.  Not all the time!
                        //Can put in a blank tile!! DEFAULT WALKABLE ALPHA CLEAR PIC
                        //Later on make tiles self aware, so they can access tiles next to them
                        //Good for growing things and such!
                        map[i][k] = tileTheme.getRandomWalkableTile();
                        
                        s = sTheme.getRandomEnemy(i, k);
                        if (s != null) {
                            enemies.add(s);
                        }
                        item = iTheme.getRandomItem(threat);
                        if (item != null) {
                            map[i][k].addItem(item);
                        }
                    }
                }
            }
        } else if (generate.contains("world")) {
            map = new Tile[width][height];
            int topology[][] = createTopology(width, height);
            
            for(int i = 0; i < width; i++) {
                for(int k = 0; k<height; k++) {
                    map[i][k] = topology[i][k] >= 2 ? tileTheme.getRandomWalkableTile() : tileTheme.getWater();
                }
            }
            populateEnemies();
            populateItems();
        } else if (generate.contains("halls")) {
            map = new Tile[width][height];
            int halls[][] = createHalls(width, height);
            for(int i = 0; i < width; i++) {
                for(int k = 0; k<height; k++) {
                    if (halls[i][k] == 1) {
                        map[i][k] = tileTheme.getRandomWalkableTile();
                    } else if (halls[i][k]==8) {
                        map[i][k] = tileTheme.getRandomSolidTile();
                    } else if (halls[i][k]==0) {
                        map[i][k] = tileTheme.getRandomDoor();
                    }
                }
            }
            populateEnemies();
            populateItems();
            //System.err.println(toString());
        } else if (generate.contains(".txt")) {
            //System.err.println(generate);
            String str;
            ArrayList<String> fileStrings = new ArrayList<String>(200);
            try {
                BufferedReader in = new BufferedReader(new FileReader(generate));
                while ((str = in.readLine()) != null) {
                    fileStrings.add(str);
                }
                in.close();
                //System.err.println(fileStrings);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            map = new Tile[width][height];
            for (int q = 0; q < fileStrings.size(); q++) {
                for (int n = 0; n < width; n++) {
                    //System.err.println(tileTheme.getTile(fileStrings.get(q).charAt(n)).i);
                    map[n][q] = tileTheme.getTile(fileStrings.get(q).charAt(n));
                }
            }
            //System.err.println(toString());
        } else if (generate.contains("dungeon")) {            
            map = new Tile[width][height];            
            
            
            
            for (int i=0; i<w; i++) {
                for (int k=0; k<h; k++) {
                    map[i][k] = tileTheme.getRandomSolidTile();
                }
            }
            
            int randx; 
            int randy; 
            Place a;
            
            int roomNum = width*height/400;
            Place[] places = new Place[roomNum];
            
            for (int q = 0; q<roomNum;q++) {
                randx = (int)(Math.random() * width);
                randy = (int)(Math.random() * height);
                a = new Place(randx, randy);
                map[a.x][a.y] = tileTheme.getRandomWalkableTile();
                places[q] = a;
            }
            
            for (int f = 0; f<places.length;f++) {
                drawRoom(places[f]);
            }
            
            a = places[0];
            Place b;
            int dx;
            int dy;
            
            for(int r = 1; r<places.length;r++) {                
                b = places[r];
                dx = b.x-a.x;
                dy = b.y-a.y;   
                boolean doorStart = false;
                boolean doorEnd = false;
                
                for(int q = 0; q < Math.abs(dx);q++) {
                    if (dx > 0) {
                        if (!(map[a.x + q][a.y].isWalkable) && !doorStart) {
                            map[a.x + q][a.y] = tileTheme.getRandomDoor();
                            doorStart = !doorStart;
                        } else if (!doorEnd && doorStart && map[a.x + q + 1][a.y].isWalkable) {
                            map[a.x + q][a.y] = tileTheme.getRandomDoor();
                            doorEnd = !doorEnd;
                            doorStart = !doorStart;
                        } else {
                            map[a.x + q][a.y] = tileTheme.getRandomWalkableTile();
                        }
                    } else {
                        if (!(map[a.x - q][a.y].isWalkable) && !doorStart) {
                            map[a.x - q][a.y] = tileTheme.getRandomDoor();
                            doorStart = !doorStart;
                        } else if (!doorEnd && doorStart && map[a.x - q - 1][a.y].isWalkable) {
                            map[a.x - q][a.y] = tileTheme.getRandomDoor();
                            doorEnd = !doorEnd;
                            doorStart = !doorStart;
                        } else {
                            map[a.x - q][a.y] = tileTheme.getRandomWalkableTile();
                        }
                    }
                    //System.err.println(toString() + "\n");
                }                
                
                for(int q = 0; q < Math.abs(dy); q++) {
                    if (dy > 0) {
                        if (!(map[b.x][a.y + q].isWalkable) && !doorStart) {
                            map[b.x][a.y + q] = tileTheme.getRandomDoor();
                            doorStart = !doorStart;
                        } else if (!doorEnd && doorStart && map[b.x][a.y + q + 1].isWalkable) {
                            map[b.x][a.y + q] = tileTheme.getRandomDoor();
                            doorEnd = !doorEnd;
                            doorStart = !doorStart;
                        } else {
                            map[b.x][a.y + q] = tileTheme.getRandomWalkableTile();
                        }
                    } else {
                        if (!(map[b.x][a.y - q].isWalkable) && !doorStart) {
                            map[b.x][a.y - q] = tileTheme.getRandomDoor();
                            doorStart = !doorStart;
                        } else if (!doorEnd && doorStart && map[b.x][a.y - q - 1].isWalkable) {
                            map[b.x][a.y - q] = tileTheme.getRandomDoor();
                            doorEnd = !doorEnd;
                            doorStart = !doorStart;
                        } else {
                            map[b.x][a.y - q] = tileTheme.getRandomWalkableTile();
                        }
                    }
                }
                
                a = b;
            }     
                
            
            for (int i=1; i<w-1; i++) {
                for (int k=1; k<h-1; k++) {
                    if (map[i][k].type.contains("Door")) {
                        
                        if ((map[i-1][k].isSolid && map[i+1][k].isSolid) && (map[i][k-1].isWalkable && map[i][k+1].isWalkable)) {
                            
                        } else if ((map[i-1][k].isWalkable && map[i+1][k].isWalkable) && (map[i][k-1].isSolid && map[i][k+1].isSolid)) {
                            
                        } else {
                            map[i][k] = tileTheme.getRandomWalkableTile();
                        }
                        
                    }
                }
                
            }
            
            
            for (int i=0; i<w; i++) {
                for (int k=0; k<h; k++) {
                    if (i==0||k==0||i==w-1||k==h-1) {
                       
                    } else {
                        if (map[i][k].isWalkable) {
                            s = sTheme.getRandomEnemy(i, k);
                            if (s != null) {
                                enemies.add(s);
                            }
                            item = iTheme.getRandomItem(threat);
                            if (item != null) {
                                map[i][k].addItem(item);
                            }
                        }
                    }
                }
            }
            
        } else if (generate.contains("maze")) {
            map = new Tile[w][h];
            createPrimMaze();
            populateItems();
            populateEnemies();
            
        } else { //Just take what's in generate and make a map out of it.
            map = new Tile[w][h];
            enemies = new ArrayList(200);
            String[] mapByLine = generate.split("\n");
            
            for (int i=0; i<mapByLine.length; i++) {
                for(int k=0;k<mapByLine[i].trim().length();k++) {
                    map[k][i] = tileTheme.getTile(mapByLine[i].trim().charAt(k));
                    if (map[k][i].isWalkable) {
                        item = iTheme.getRandomItem(threat);
                        if (item != null) {
                            map[k][i].addItem(item);
                        }
                    }
                }
            }
        }
        
        populateUniques();
        //System.err.println(toString());
    }
    
    public void connect(ArrayList<Map> allMaps) {
        boolean done = false;
        boolean stop = false;
        
        for (int a = 0; a < allMaps.size(); a++) {
            stop = false;
            done = false;
            if (allMaps.get(a).getName() != name && connections.contains(allMaps.get(a).getName())) {
                
                //if the map already contains the tile symbol, then make the connection there.
                for (int i = 0; i <width; i++) {
                    for (int k = 0; k< height; k++) {
                        
                        if (map[i][k].type.contains("connection") && map[i][k].symbol.equals(allMaps.get(a).getSymbol())) {
                            map[i][k] = new Tile(allMaps.get(a).getName(), map[i][k].symbol, allMaps.get(a).getIcon(), "connection:" + allMaps.get(a).getName() + ":" + i +":" +k, "A place to go!", null);
                            stop = true;
                            //System.err.println("Solid Connect:"+allMaps.get(a).getName()+" and " + name+"("+i+","+k+")");
                        }
                        
                    }
                }
                //Otherwise make a connection at a random point                
                while (!done && !stop) {
                    int randa = (int)(Math.random() * width);
                    int randb = (int)(Math.random() * height);
                    if (isWalkable(randa, randb) && !map[randa][randb].type.contains("connection")) {
                        map[randa][randb] = new Tile(allMaps.get(a).getName(), "%", allMaps.get(a).getIcon(), "connection:" + allMaps.get(a).getName() + ":" + randa +":" +randb, /*allMaps.get(a).getMapDescription()*/"A new place to go!", null);
                        done = true;
                        //System.err.println("Random Connect:"+allMaps.get(a).getName()+" and " + name +"("+randa+","+randb+")");
                    }
                }
            }
        }
    }
    
    private void populateItems() {
        //int width = map.length;
        //int height = map[0].length;
        Item item;
        for (int i=0; i<width; i++) {
            for (int k=0; k<height; k++) {
                if (map[i][k].isWalkable) {
                    item = itemTheme.getRandomItem(threat);
                    if (item != null) {
                        map[i][k].addItem(item);
                    }
                }
            }
        }
    }
    private void populateEnemies() {
        Sprite enemy;
        for (int i=0; i<width; i++) {
            for (int k=0; k<height; k++) {
                if (map[i][k].isWalkable) {
                    enemy = spriteTheme.getRandomEnemy(i, k);
                    if (enemy != null) {
                        enemies.add(enemy);
                    }
                }
            }
        }
    }
    private void populateUniques() {
        ArrayList<Sprite> uniques = spriteTheme.getUniques();
        for(int g = 0; g < uniques.size(); g++) {
            boolean done = false; 
            while (!done) {
                int randx = (int)(Math.random() * width);
                int randy = (int)(Math.random() * height);
                if (map[randx][randy].isWalkable) {
                    enemies.add(new Sprite(uniques.get(g), randx, randy));
                    done = true;
                }
            }
        }
    }
    
    private void createAllSolid() {
        for (int i = 0; i < map.length ; i++ ) {
            for (int k = 0; k < map[0].length; k++) {
                map[i][k] = tileTheme.getRandomSolidTile();
            }
        }
    }
    
    private Place getRandomPlace(ArrayList<Place> list) {
        if (list.size() != 0) {
            int random = (int)(Math.random() * list.size());
            return list.get(random);
        } else {
            return null;
        }
    }
    private Room getRandomRoom(ArrayList<Room> list) {
        if (list.size() != 0) {
            int random = (int)(Math.random() * list.size());
            return list.get(random);
        } else {
            return null;
        }
    }
    private int[][] createTopology(int width, int height) {
        int w = width;
        int h = height;
        int top[][] = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int k = 0; k < h; k++) {
                top[i][k] = 1;
            }
        }
        
        for (int m = 0; m < ((w*h/90)+1); m++) {
            int randX = (int)(Math.random()*w);
            int randY = (int)(Math.random()*h);
            Place place = new Place(randX, randY);
            for (int i = 0; i < w; i++) {
                for (int k = 0; k < h; k ++) {
                    if (place.distance(i, k) < 7) {                        
                        top[i][k]++;                        
                    }
                }
            }
        }
        return top;
    }
    
    private int[][] createHalls(int w, int h) {
        int halls[][] = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int k = 0; k < h; k++) {
                if (i == 0 || k == 0 || i == w-1 || k == h-1) {
                    halls[i][k] = 8;
                } else {
                    halls[i][k] = 1;
                }
            }
        }
        ArrayList<Room> rooms = new ArrayList<Room>(w*h/25);
        ArrayList<Room> toDraw = new ArrayList<Room>(w*h/25);
        rooms.add(new Room(new Place(0, 0), new Place(w-1, h-1)));
        int rand;
        int third;
        Room a;
        Room b;
        while (rooms.size() > 0) {
            Room current = rooms.remove(0);
            if ((int)(Math.random() * 2) == 0) {
                rand = (int)(Math.random() * 3);
                third = current.getWidth()/2 -1;
                a = new Room(current.topLeft, new Place(current.topLeft.x + third+rand, current.bottomRight.y));
                b = new Room(new Place(current.topLeft.x + third+rand, current.topLeft.y), current.bottomRight);
            } else {
                rand = (int)(Math.random() * 3);
                third = current.getHeight()/2 -1;
                a = new Room(current.topLeft, new Place(current.bottomRight.x, current.topLeft.y + third+rand));
                b = new Room(new Place(current.topLeft.x, current.topLeft.y + third+rand), current.bottomRight);
            }
            int size = 5;
            if (a.getWidth() > size && b.getWidth() > size && a.getHeight() > size && b.getHeight() > size) {
                rooms.add(a);
                rooms.add(b);
            } else {
                if (!toDraw.contains(current)) {
                    toDraw.add(current);
                }
            }
        }
        
        for (int m = 0; m<toDraw.size(); m++) {
            Room current = toDraw.get(m);
            
            for (int i = 0; i < current.getWidth(); i++) {
                for (int k = 0; k < current.getHeight(); k++) {
                    if (i == 0 || k == 0 || i == current.getWidth()-1 || k == current.getHeight()-1)
                    halls[current.topLeft.x + i][current.topLeft.y + k] = 8;
                }
            }
        }
        
        ArrayList<Room> connected = new ArrayList<Room>(toDraw.size() + 2);
        //  This is if you don't want doors to every place.
        ArrayList<Room> toCheck = new ArrayList<Room>(toDraw.size());
        ArrayList<Room> possibles = new ArrayList<Room>(6);
        Room current = toDraw.remove(0);
        connected.add(current);
        while (toDraw.size() > 0  || toCheck.size() > 0) {
            possibles.clear();
            for (int m = 0; m<toDraw.size(); m++) {
                if (toDraw.get(m).sharesBorder(current)) {
                    if (!connected.contains(toDraw.get(m))) {
                        possibles.add(toDraw.get(m));
                        if (!toCheck.contains(toDraw.get(m))) {
                            toCheck.add(toDraw.get(m));
                        }
                    }
                }
            }
            if (possibles.size() > 0) {
                Room toConnect = getRandomRoom(possibles);
                toConnect.drawDoorBetween(current);
                current = toConnect;
            } else {
                current = getRandomRoom(toCheck);
                for (int m = 0; m<connected.size(); m++) {
                    if (connected.get(m).sharesBorder(current)) {
                        possibles.add(connected.get(m));
                    }
                }
                Room toConnect = getRandomRoom(possibles);
                toConnect.drawDoorBetween(current);
            }
            connected.add(current);
            toDraw.remove(current);
            toCheck.remove(current);
        }
        
        for (int m = 0; m < connected.size(); m++) {
            ArrayList<Place> doorsToDraw = connected.get(m).doors;
            for (int s = 0; s < doorsToDraw.size(); s++) {
                halls[doorsToDraw.get(s).x][doorsToDraw.get(s).y] = 0;
            }
        }
        /*
        for (int i = 0; i < h; i++) {
            for (int k = 0; k < w; k++) {
                System.err.print(halls[k][i]);
            }
            System.err.println("");
        } 
         */
        return halls;
    }
        
    private void createPrimMaze() {
        createAllSolid();
        int placeNums = map.length*map[0].length/4 + 10;
        ArrayList<Place> nodes = new ArrayList<Place>(placeNums);
        ArrayList<Place> inMaze = new ArrayList<Place>(placeNums);
        ArrayList<Place> neighbors = new ArrayList<Place>(placeNums);
        ArrayList<Place> possibleChoices = new ArrayList<Place>(4);
        int w = map.length - 1;
        int h = map[0].length - 1;
        int n = 1;
        int m = 1;
        while (n < w) {
            while (m < h) {
                nodes.add(new Place(n, m));
                m++;
                m++;
            }
            n++;
            n++;
            m = 1;
        }
        Place currentPlace;
        Place futurePlace;
        
        currentPlace = getRandomPlace(nodes);
        makeWalkable(currentPlace);
        inMaze.add(currentPlace);
        nodes.remove(currentPlace);
        
        while (nodes.size() > 0) {
            //System.err.println(toString());
            for (int a = 0; a<nodes.size(); a++) {
                if (nodes.get(a).x == currentPlace.x - 2) {
                    if (nodes.get(a).y == currentPlace.y) {
                        if (!inMaze.contains(nodes.get(a))) {
                            possibleChoices.add(nodes.get(a));
                        }
                        if (!neighbors.contains(nodes.get(a))) {
                            neighbors.add(nodes.get(a));
                        }
                    }
                } else if (nodes.get(a).x == currentPlace.x) {
                    if (nodes.get(a).y == currentPlace.y - 2) {
                        if (!inMaze.contains(nodes.get(a))) {
                            possibleChoices.add(nodes.get(a));
                        }
                        if (!neighbors.contains(nodes.get(a))) {
                            neighbors.add(nodes.get(a));
                        }
                    } else if (nodes.get(a).y == currentPlace.y + 2) {
                        if (!inMaze.contains(nodes.get(a))) {
                            possibleChoices.add(nodes.get(a));
                        }
                        if (!neighbors.contains(nodes.get(a))) {
                            neighbors.add(nodes.get(a));
                        }
                    }
                } else if (nodes.get(a).x == currentPlace.x + 2) {
                    if (nodes.get(a).y == currentPlace.y) {
                        if (!inMaze.contains(nodes.get(a))) {
                            possibleChoices.add(nodes.get(a));
                        }
                        if (!neighbors.contains(nodes.get(a))) {
                            neighbors.add(nodes.get(a));
                        }
                    }
                }
            }
            if (possibleChoices.size() != 0) {
                futurePlace = getRandomPlace(possibleChoices);
                tunnel(currentPlace, futurePlace);
                currentPlace = futurePlace;
            } else {
                currentPlace = getRandomPlace(neighbors);
                //get neighboring inMaze pieces
                for (int a = 0; a<inMaze.size(); a++) {
                    if (inMaze.get(a).x == currentPlace.x - 2) {
                        if (inMaze.get(a).y == currentPlace.y) {
                            possibleChoices.add(inMaze.get(a));
                        }
                    } else if (inMaze.get(a).x == currentPlace.x) {
                        if (inMaze.get(a).y == currentPlace.y - 2) {
                            possibleChoices.add(inMaze.get(a));
                        } else if (inMaze.get(a).y == currentPlace.y + 2) {
                            possibleChoices.add(inMaze.get(a));
                        }
                    } else if (inMaze.get(a).x == currentPlace.x + 2) {
                        if (inMaze.get(a).y == currentPlace.y) {
                            possibleChoices.add(inMaze.get(a));
                        }
                    }
                }
                //Tunnel into a random one.
                futurePlace = getRandomPlace(possibleChoices);
                tunnel(currentPlace, futurePlace);
            }
            makeWalkable(currentPlace);
            inMaze.add(currentPlace);
            nodes.remove(currentPlace);
            neighbors.remove(currentPlace);
            
            possibleChoices.clear();
        }
    }
    
    private void makeWalkable(Place p) {
        map[p.x][p.y] = tileTheme.getRandomWalkableTile();
    }
    
    private void tunnel(Place start, Place end) {
        int difX = (end.x - start.x)/2;
        int difY = (end.y - start.y)/2;
        //System.err.println("Start:" + start + " Mid: " + new Place(start.x + difX, start.y + difY) + " End: " + end);
        makeWalkable(new Place(start.x + difX, start.y + difY));
        //makeWalkable(end);
    }
    
    private void drawRoom(Place p) {
        int x = p.x;
        int y = p.y;
        int lx = random(5) + 2;
        int dy = random(5) + 2;
        int rx = random(5) + 2;
        int uy = random(5) + 2;
        int startx = x-lx;
        int starty = y-uy;
        int endx = x+rx;
        int endy = y+dy;
        Place a = new Place(startx, starty);
        Place b = new Place(endx, endy);
        a = putInBounds(a);
        b = putInBounds(b);
        for (int i = 0; i < b.x-a.x; i++) {
            for (int k = 0; k < b.y - a.y; k++) {
                map[a.x + i][a.y + k] = tileTheme.getRandomWalkableTile();
            }
        }
    }
    
    private Place putInBounds(Place p) {
        if (p.x < 0) {
            p.x = 0;
        }
        if (p.y < 0) {
            p.y = 0;
        }
        if (p.x>width) {
            p.x = width;
        }
        if (p.y > height) {
            p.y = height;
        }
        return p;
    }
    
    private int random(int a) {
        return (int)(Math.random() * a);
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public Image getIcon() {
        return myPic;
    }
    
    public String getStoryFile() {
        if (storyFile.equals("none")) {
            return null;
        }
        if (isStory) {
            isStory = false;
            return storyFile;
        }
        return null;
    }
    
    public String getName() {
        return name;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String getDescription(int x, int y) {
        String ans = ""; 
        for (int s = 0; s<enemies.size(); s++) {
            if (enemies.get(s).getX() == x && enemies.get(s).getY() == y) {
                String name = enemies.get(s).getName();
                
                ans += " " + enemies.get(s).getName() + " is standing there.  ";
            }
        }        
        
        ans += map[x][y].description;
        if (map[x][y].getItems().size() > 1) {
            ans += " You see several items!";
        } else if (map[x][y].getItems().size() == 1) {
            ans += " You see a";
            String name = map[x][y].getItems().get(0).getName();
            if (name.contains("Boots") || name.contains("boots")) {
                ans+= " pair of ";
            } else if (name.startsWith("A") ||name.startsWith("E") ||name.startsWith("I") ||name.startsWith("O") ||name.startsWith("U")) {
                ans+= "n";
            }
            ans += " " + name + "!";
        }
        return ans;
    }
    public Image getImage(int x, int y) {
        if (inBounds(x, y)) {
            return map[x][y].i;
        }
        return map[0][0].i;
    }
    public Tile getTile(int x, int y) {
        if (inBounds(x, y)) {
            return map[x][y];
        }
        return map[0][0];
        
    }   
    public Sprite getEnemy(int x, int y) {
        int size = enemies.size();
        for (int m =0; m<size;m++) {
            if ((enemies.get(m).getX() == x) && (enemies.get(m).getY() == y)) {
                return enemies.get(m);
            }
        }
        return null;
    }
    public ArrayList getItems(int x, int y) {
        if (x<width && y <height && x>-1 && y>-1) {
            return map[x][y].getItems();
        } else {
            System.err.println("Cannot get an Item out of these bounds!");
            //TODO figure out if this answer it ok.
            return map[0][0].getItems();
        }
    }
    public ArrayList<Sprite> getEnemies() {
        return enemies;
    }
    
    public int getActionCode(int x, int y) {
        if (!inBounds(x, y)) {
            return NONE;
        }
        int size = enemies.size();
        for (int m =0; m<size;m++) {
            if ((enemies.get(m).getX() == x) && (enemies.get(m).getY() == y)) {
                if (enemies.get(m).isFriendly()) {
                    return FRIEND;
                } else {
                    return ENEMY;
                }
            }
        }
        if (map[x][y].type.contains("Door")) {
            return DOOR;
        }
        //return codes for destructable, openable, etc...
        return NONE;
    }
    
    public void hit(Item item, int x, int y, MessageHolder m) {
        //System.err.println(item + ": " + x + ", " + y);
        Sprite s = getEnemy(x, y);
        if (s != null) {
            int damage = s.hit(item);
            m.message("You hit " + s.getName() + " for " + damage + " damage with a " + item.getName(), Color.GREEN);
        } else {
        //If the wall or whatever should take damamge!
        map[x][y].hit(item, m);
        
        }
    }
    public void kick(Player p, Item boot, int x, int y, MessageHolder m) {
        Sprite s = getEnemy(x, y);
        int damage;
        if (s != null) {
            if (boot != null) {
                damage = p.kick(s);
                m.message("You kicked " + s.getName() + " for " + damage + " damage with a " + boot.getName(), Color.GREEN);
            } else {
                damage = p.kick(s);
                m.message("You kicked " + s.getName() + " for " + damage + " damage with your bare feet!", Color.GREEN);
            }
        } else {
            map[x][y].kick(p, m);
        }
    }
    public void attackTile(Player p, int x, int y, MessageHolder m) {
        map[x][y].attack(p, m);
    }
    
    public boolean isWalkable(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        int size = enemies.size();
        for (int m =0; m<enemies.size();m++) {
            if ((enemies.get(m).getX() == x) && (enemies.get(m).getY() == y)) {
                return false;
            }
        }
        return map[x][y].isWalkable;
    }
    public boolean isThrowable(int x ,int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        int size = enemies.size();
        for (int m =0; m<enemies.size();m++) {
            if ((enemies.get(m).getX() == x) && (enemies.get(m).getY() == y)) {
                return false;
            }
        }
        return map[x][y].isThrowable();
    }
    public boolean isLandable(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        return !map[x][y].isSolid;
    }
    
    public boolean isTransparent(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        return map[x][y].isTransparent;
    }
    
    public void addItem(Item item, int x, int y) {
        if (x<width && y <height && x>-1 && y>-1) {
            map[x][y].addItem(item);
        } else {
            System.err.println("Cannot add an Item out of these bounds!");
        }
    }   
    
    public void addItemInMotion(Item item, double startX, double startY, double endX, double endY) {
        itemsInMotion.add(new ItemInMotion(item, startX, startY, endX, endY));
    }
    
    public ArrayList<ItemInMotion> getItemsInMotion() {
        return itemsInMotion;
    }
    
    public boolean inBounds(int x, int y) {
        boolean answer;
        answer = true;
        if (x>=width || y>=height || x<0 || y<0) {
            answer = false;
        }
        return answer;
    }
    
    public String toString() {
        String ans = "";
        for (int i=0; i<width; i++) {
            for (int k=0; k<height; k++) {
                ans += map[i][k].symbol;
            }
            ans += "\n";
        }
        return ans;
    }
    
    public String getPlace(int x, int y) {
        if (map[x][y].type.contains("connection")) {
            return map[x][y].type.split(":")[1].trim();
        }
        return null;
    }
    
    public Tile getConnectTile(String connectName) {
        for (int i=0; i<width; i++) {
            for (int k=0; k<height; k++) {
                if (map[i][k].type.contains(connectName) && map[i][k].type.contains("connect")) {
                    return map[i][k];
                }
            }
        }
        return null;
    }
    public Image getBackgroundImage() {
        return backgroundImage;
    }
    
    public String openDoor(Player p, int x, int y) {
        String response = map[x][y].open(p);
        return response;
    }
    
    public Item getNewItem(String s, Material m) {
        return itemTheme.getNewItem(s, m);
    }
    
    public ArrayList<String> getActionOptions(Player p, Sprite target) {
        ArrayList<String> answer = new ArrayList<String>(10);
        if (target != null) {
            int distanceX = Math.abs(p.getX()-target.getX());
            int distanceY = Math.abs(p.getY()-target.getY());
            if (distanceX <= 1 && distanceY <=1) {
                answer.add("Attack");
                answer.add("Kick");
            }
            answer.add("Throw");
            answer.add("Chat");
            answer.add("Deselect");
        } else {
            if (getPlace(p.getX(), p.getY()) != null) {
                answer.add("Travel");
            }
            if (getItems(p.getX(), p.getY()).size() > 0) {
                answer.add("Pickup");
            }
            answer.add("Inventory");
            answer.add("Rest");
        }
        
        answer.add("Do nothing (z)");
        return answer;
    }
    
    public ArrayList<String> getActionOptions(Player p, int x, int y) {
        ArrayList<String> answer = new ArrayList<String>(10);
        int distanceX = Math.abs(p.getX()-x);
        int distanceY = Math.abs(p.getY()-y);
        if (distanceX <= 1 && distanceY <=1) {
            answer.add("Attack");
            answer.add("Kick");
        }
        answer.add("Throw");
        answer.add("Do nothing (z)");
        return answer;
    }
    
    private Tile[][] map;
    private int width;
    private int height;
    private int threat;
    private String name;
    private Image myPic;
    private String connections;
    private String symbol;
    private Image backgroundImage;
    
    private boolean isStory;
    private String storyFile;
    
    private TileTheme tileTheme;
    private SpriteTheme spriteTheme;
    private ItemTheme itemTheme;       
    
    private ArrayList<Sprite> enemies;
    
    private ArrayList<ItemInMotion> itemsInMotion;
    
    
}
