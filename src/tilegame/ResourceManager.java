package tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import graphics.*;
import tilegame.sprites.*;

/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class ResourceManager {
    private ArrayList arrLTiles;
    private int iCurrentMap;
    private GraphicsConfiguration gc;
    
    //Sprites used for cloning
    private Sprite sprPlayer;
    private Sprite sprGoal;
    private Sprite sprChicken;
    private Sprite sprBorrego;
    private Sprite sprGota;
    
    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public ResourceManager(GraphicsConfiguration gc) {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }

    /**
            Gets an image from the images/ directory.
     */
    public Image loadImage(String name) {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }
    
    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
    
    public void setiCurrentMap(int iI) {
        iCurrentMap = iI;
    }
    
    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            iCurrentMap ++ ;
            try {
                map = loadMap(
                    "maps/map" + iCurrentMap + ".txt");
            }
            catch (IOException ex) {
                if (iCurrentMap == 1) {
                    // no maps to load!
                    return null;
                }
                iCurrentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() {
        try {
            return loadMap(
                "maps/map" + iCurrentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < arrLTiles.size()) {
                    newMap.setTile(x, y, (Image)arrLTiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, sprGota, x, y);
                }

                else if (ch == '*') {
                    addSprite(newMap, sprGoal, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, sprChicken, x, y);
                }
                
                else if (ch == '2') {
                    addSprite(newMap, sprBorrego, x, y);
                }
            }
        }

        // add the player to the map
        Sprite player = (Sprite)sprPlayer.clone();
        player.setX(TileMapRenderer.tilesToPixels(3));
        player.setY(0);
        newMap.setPlayer(player);

        return newMap;
    }


    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }
    
    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        arrLTiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tiles/tile_" + ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) {
                break;
            }
            arrLTiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadCreatureSprites() {

        Image[][] images = new Image[6][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("player/Hero Sprite Running_01.png"),
            loadImage("player/Hero Sprite Running_02.png"),
            loadImage("player/Hero Sprite Running_03.png"),
            loadImage("player/Hero Sprite Running_04.png"),
            loadImage("player/Hero Sprite Running_05.png"),
            loadImage("player/Hero Sprite Running_06.png"),
            loadImage("gallinas/gallina_zombie_run_01.png"),
            loadImage("gallinas/gallina_zombie_run_02.png"),
            loadImage("gallinas/gallina_zombie_run_03.png"),
            loadImage("gallinas/gallina_zombie_run_04.png"),
            loadImage("gallinas/gallina_zombie_run_05.png"),
            loadImage("gallinas/gallina_zombie_run_06.png"),
            loadImage("borrego/borrego_run01.png"),
            loadImage("borrego/borrego_run02.png"),
            loadImage("borrego/borrego_run03.png"),
            loadImage("borrego/borrego_run04.png"),
            loadImage("borrego/borrego_run05.png"),
            loadImage("borrego/borrego_run06.png"),
            loadImage("borrego/borrego_run07.png"),
            loadImage("borrego/borrego_run08.png")
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        images[4] = new Image[images[0].length];
        images[5] = new Image[images[0].length];
        
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
            
        }

        // create creature animations
        Animation[] animPlayer = new Animation[6];
        Animation[] animChicken = new Animation[5];
        Animation[] animBorrego = new Animation[8];
        
        for (int i=0; i<5; i++) {
            animPlayer[i] = createPlayerAnim(
                images[i][0], images[i][1], images[i][2],images[i][3], 
                    images[i][4],images[i][5]);
            
            animChicken[i] = createChickenAnim(
               images[i][6], images[i][7], images[i][8],images[i][9],
                    images[i][10],images[i][11]);
            
            
            //las otras animaciones las hago por fuera 
            animBorrego[i] = createBorregoAnim(
                images[i][12], images[i][13], images[i][14],images[i][15],
                    images[i][16],images[i][17],images[i][18],images[i][19]);
            
        }
        
        //Heroe Parado
        Animation animPlayerStand = new Animation();
        animPlayerStand.addFrame(
                loadImage("player/Hero Sprite Idle_00.png"), 150);
        animPlayerStand.addFrame(
                loadImage("player/Hero Sprite Idle_01.png"), 150);
        
        //Ataque gallina
        Animation animChickenAttackLeft = new Animation();
        animChickenAttackLeft.addFrame(
                loadImage("gallinas/gallina_zombie_attack2_01.png"), 150);
        animChickenAttackLeft.addFrame(
                loadImage("gallinas/gallina_zombie_attack2_02.png"), 150);
        animChickenAttackLeft.addFrame(
                loadImage("gallinas/gallina_zombie_attack2_03.png"), 150);
        animChickenAttackLeft.addFrame(
                loadImage("gallinas/gallina_zombie_attack2_04.png"), 150);
        
        Animation animChickenAttackRight = new Animation();
        animChickenAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_attack2_01.png")), 150);
        animChickenAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_attack2_02.png")), 150);
        animChickenAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_attack2_03.png")), 150);
        animChickenAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_attack2_04.png")), 150);
                
        
        //ATAQUE DE BORREGOS
        Animation animBorregoAttackLeft = new Animation();
        animBorregoAttackLeft.addFrame(
                loadImage("borrego/borrego_attack01.png"), 150);
        animBorregoAttackLeft.addFrame(
                loadImage("borrego/borrego_attack02.png"), 150);
        animBorregoAttackLeft.addFrame(
                loadImage("borrego/borrego_attack03.png"), 150);
        animBorregoAttackLeft.addFrame(
                loadImage("borrego/borrego_attack04.png"), 150);
        
        Animation animBorregoAttackRight = new Animation();
        animBorregoAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_attack01.png")), 150);
        animBorregoAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_attack02.png")), 150);
        animBorregoAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_attack03.png")), 150);
        animBorregoAttackRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_attack04.png")), 150);
        
        //MUERTE BORREGO
        Animation animBorregoDieLeft = new Animation();
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies01.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies02.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies03.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies04.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies05.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies06.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies07.png"), 150);
        animBorregoDieLeft.addFrame(
                loadImage("borrego/borrego_dies08.png"), 150);
        
        Animation animBorregoDieRight = new Animation();
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies01.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies02.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies03.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies04.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies05.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies06.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies07.png")), 150);
        animBorregoDieRight.addFrame(
                getMirrorImage(loadImage
                    ("borrego/borrego_dies08.png")), 150);
       
        
        //MUERTE GALLINA
        Animation animGallinaDieLeft = new Animation();
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies01.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies02.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies03.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies04.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies05.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies06.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies07.png"), 150);
        animGallinaDieLeft.addFrame(
                loadImage("gallinas/gallina_zombie_dies08.png"), 150);
        
        Animation animGallinaDieRight = new Animation();
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies01.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies02.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies03.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies04.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies05.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies06.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies07.png")), 150);
        animGallinaDieRight.addFrame(
                getMirrorImage(loadImage
                    ("gallinas/gallina_zombie_dies08.png")), 150);
       

        // create creature sprites
        //en player es 1, 0 para que esten iguales.
        sprPlayer = new Player(animPlayer[1], animPlayer[0],
            animPlayer[2], animPlayer[3], null, null, animPlayerStand);
        sprChicken = new Minion(animChicken[0], animChicken[1],
            animGallinaDieLeft, animGallinaDieRight, animChickenAttackLeft,
                animChickenAttackRight, null);
        sprBorrego = new Minion(animBorrego[0], animBorrego[1],
            animBorregoDieLeft, animBorregoDieRight, animBorregoAttackLeft, 
                animBorregoAttackRight, null);
       // sprDuck1 = new Duck();
    }


    private Animation createPlayerAnim(Image player1,
        Image player2, Image player3, Image player4,
            Image player5, Image player6)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 150);
        anim.addFrame(player4, 150);
        anim.addFrame(player5, 150);
        anim.addFrame(player6, 150);
        //anim.addFrame(player1, 150);
        
        return anim;
    }


    //Se cargan las animaciones de las mugres (enemigos)
    private Animation createChickenAnim(Image img1, Image img2,
        Image img3, Image img4, Image img5, Image img6)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 100);
        anim.addFrame(img2, 100);
        anim.addFrame(img3, 100);
        anim.addFrame(img4, 100);
        anim.addFrame(img5, 100);
        anim.addFrame(img6, 100);
        return anim;
    }
    
    //Se cargan las animaciones de las mugres (enemigos)
    private Animation createBorregoAnim(Image img1, Image img2,
        Image img3, Image img4, Image img5, Image img6, Image img7, Image img8)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 100);
        anim.addFrame(img2, 100);
        anim.addFrame(img3, 100);
        anim.addFrame(img4, 100);
        anim.addFrame(img5, 100);
        anim.addFrame(img6, 100);
        anim.addFrame(img7, 100);
        anim.addFrame(img8, 100);
        return anim;
    }


    private void loadPowerUpSprites() {//Se cargan las animaciones de los objetos a agarrar
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("extras/Chilaquiles_puntos01.png"), 150);
        anim.addFrame(loadImage("extras/Chilaquiles_puntos02.png"), 150);
        anim.addFrame(loadImage("extras/Chilaquiles_puntos03.png"), 150);
        anim.addFrame(loadImage("extras/Chilaquiles_puntos02.png"), 150);
        sprGoal = new PowerUp.Goal(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("extras/gota1.png"), 100);
        anim.addFrame(loadImage("extras/gota2.png"), 100);
        anim.addFrame(loadImage("extras/gota3.png"), 100);
        anim.addFrame(loadImage("extras/gota4.png"), 100);
        sprGota = new PowerUp.Gota(anim);

    }
}