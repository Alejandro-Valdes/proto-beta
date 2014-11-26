package tilegame;
import graphics.*;
import input.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.Iterator;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.swing.ImageIcon;
import test.GameCore;
import tilegame.sprites.*;
import sound.*;

/**
 * VALKYRIE
 * ANDREA JAQUELINE BOONE MARTINEZ A01139540
 * JESUS ALEJANDRO VALDES VALDES A00999044
 * JORGE ALFONSO GONZALEZ HERRERA A00999088
 * LUIS ALBERTO LAMADRID TAFICH A01191158
 */

/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {

    

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;

    private Point pointCache = new Point();
    private TileMap map;  //map we'll use
    private ResourceManager resourceManager;
    private InputManager inputManager; 
    private TileMapRenderer renderer;

    private static SoundClip scMusic;
    private SoundClip scCheer;
    private SoundClip scBoo;
    private SoundClip scZombie;
    private SoundClip scNotYet;
    private SoundClip scCoin;
    private SoundClip scGoal;
    
    private GameAction moveLeft;  //GmeAct to move
    private GameAction moveRight;

    private GameAction jump;    //GmeAct to jump
    private GameAction exit;    //GmeAct to ext
    private GameAction pause;   //GmeAct to pause
    private GameAction next;    //GmeAct to begin
    private GameAction attack;  //GmeAct to attack
    private GameAction restart; //GameAct to restart
    private Image imaPausa; //imagen para pausar

    private boolean bCanMoveR;  //can i move right
    private boolean bCanMoveL;  //can i move left
    private boolean bPause;     //am i paused
    private boolean bImPlaying; //am i playing
    private boolean bTutLabel;  // player requests tutorial
    private boolean bLost;
    private int iLevel;     //which level am i in
    private int iNumLevels;  //how many levels do we have
    private int iContTime;      //timer for loading
    private int iLife;      //life percentage
    private int iContVida;      //timer to know when to substract life
    private int iContAttack;    //each attack lasts 1 secnod
    private boolean bAttack;    //is player attacking
    private int iIngredientes;      //numbre of ingredients needed
    private int iScore;     //score of the game incrmeneted by coins
    
    /**
     * init initializes all my variables
     */
    public void init() {
        super.init();

        // set up input manager
        initInput();
        
        scMusic = new SoundClip("/sounds/music.wav");
        scMusic.setLooping(true);
        
        scCheer = new SoundClip("/sounds/cheer.wav");
        scBoo = new SoundClip("/sounds/boo.wav");
        scCoin = new SoundClip("/sounds/coin.wav");
        scNotYet = new SoundClip("/sounds/notYet.wav");
        scZombie = new SoundClip("/sounds/zombie.wav");
        scGoal = new SoundClip("/sounds/goal.wav");
                  
        
        // start resource manager
        resourceManager = new ResourceManager(
        screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        //load pause image
        imaPausa = resourceManager.loadImage("extras/PAUSA_white.png");
        //Level manegement
        iIngredientes = 4;
        iNumLevels = 6;
        iLevel = 0;
        iContTime = 0;
        bTutLabel = false;
        iLife = 100;
        iContVida = 0;
        iContAttack = 0;
        bAttack = false;
        bLost = false;
        iScore = 0;
        
        //Adds all my backgrounds
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/valkyrie.png")); //0
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/PROJECTA00.jpg")); //1
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/tutorial.png"));  //2
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level1.png")); //3
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level2.jpg")); //4
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level3.jpg")); //5
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level4.png"));//6 last level
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/YOULOSE.jpg")); //7 
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/YOUWIN.jpg")); //8
        
        renderer.setBackground(iLevel);

        // load first map
        map = resourceManager.loadNextMap();
    }


    /**
        Closes any resurces used by the GameManager.
    */
    public void stop() {
        super.stop();
    }


    /**
     * initializes the input that'll be used
     */
    private void initInput() {
        //i can move
        bCanMoveR = false;
        bCanMoveL = false;
        //not pause
        bPause = false;
        bImPlaying = false;
        //Creation of all the game actions
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump",
                GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        exit = new GameAction("exit",
                GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        pause = new GameAction("pause", 
                GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        next = new GameAction("next");
        attack = new GameAction("attack",
                GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        restart = new GameAction("restart", 
                GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        
        
        inputManager = new InputManager(
            screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        //Mapping of gameActions to keys
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(next, KeyEvent.VK_S);
        inputManager.mapToKey(attack, KeyEvent.VK_A);
        inputManager.mapToKey(restart, KeyEvent.VK_R);
        
    }

    /**
     * Checks for pause and exit
     * can  be used even out of pause
     * @param elapsedTime 
     */
    private void checkSystemInput(long elapsedTime) {
        
        if(pause.isPressed()){
            bPause = false; 
            pause.reset();
        }
        
        if (exit.isPressed()) {
                stop();
        }
    }

    /**
     * Checks all the input of the keyboard
     * @param elapsedTime 
     */
    private void checkInput(long elapsedTime) {
            
        if (exit.isPressed()) {
            stop();
        }
        
        if(restart.isPressed() && bLost) {
            //UN TIPO DE INIT()
            //Level manegement
            scMusic.play();
            iIngredientes = 4;
            iLevel = 3;
            iContTime = 0;
            iLife = 100;
            iContVida = 0;
            iContAttack = 0;
            iScore = 0;
            bAttack = false;
            bLost = false;
            //i can move
            bCanMoveR = true;
            bCanMoveL = true;
            //not pause
            bPause = false;
            renderer.setBackground(iLevel);
            resourceManager.setiCurrentMap(1); 
            //cargo el uno y cargo el siguiente
            map = resourceManager.loadNextMap();
            bImPlaying = true;
            restart.reset();            
        }

        if(next.isPressed() && iLevel == 1) {
            bCanMoveL =true;
            bCanMoveR = true;
            iLevel++;
            bImPlaying = true;
            scMusic.play();
            renderer.setBackground(iLevel);     //tutorial background   
        }

        //Moves the player around
        Player player = (Player)map.getPlayer();
        if (player.isAlive()) {
            float velocityX = 0;
            
            if(bCanMoveL){
                if (moveLeft.isPressed()) {
                    velocityX-=player.getMaxSpeed();
                }
            }

            if(bCanMoveR){
                if (moveRight.isPressed()) {
                    velocityX+=player.getMaxSpeed();
                }
            }

            if (pause.isPressed() && bImPlaying) {
                bPause = true;
                pause.reset();
            }

            if (jump.isPressed()) {
                player.jump(false);
                bCanMoveR = true;
                bCanMoveL = true;
            }

            if(attack.isPressed()) {
                player.setAttack();
                bAttack = true;
                iContAttack++;
               
            }
            else {
                if(iContAttack > 15) {
                    player.stopAttack();
                    iContAttack = 0;
                    attack.release();
                    bAttack = false;
                    
                }
                else if (iContAttack > 0) {
                    iContAttack++;
                }
            }

            player.setVelocityX(velocityX);
            }
    }

    

    /**
     * draws basic info on the screen
     * @param g 
     */
    public void draw(Graphics2D g) {        
        renderer.draw(g, map,
            screen.getWidth(), screen.getHeight());

        if(iLevel > 1 && !bLost) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 40));
            g.drawString("LIFE: " + iLife +"%", screen.getWidth() - 250,
                     50);
            g.drawString("SCORE: " + iScore, 50 , 50);
            
            if(iIngredientes > 0){
                g.setFont(new Font("Verdana",Font.PLAIN, 20));
                g.drawString("INGREDIENTS 2 GO: " + iIngredientes, 
                        screen.getWidth() - 240, 100);
            }
            else {
                g.setFont(new Font("Verdana",Font.PLAIN, 20));
                g.drawString("GET TO THE CHILAQUILES", 
                        screen.getWidth() - 265, 100);
            }
            
        }
        
        // si el jugador esta pidiendo tutorial
        if (bTutLabel) {
            // Obteniendo el jugador
            Player player = (Player)map.getPlayer();
            
            // declarando imagen inicial
            Image image = resourceManager.loadImage("extras/pato_agarrachilaquiles.png");
            //g.drawString("X: " + player.getX(), 300, 400);
            // checando en que posicion del mapa tutorial esta el personaje para saber
            // que consejo del pato desplegar
            if (player.getX() > 3400) {
                image = resourceManager.loadImage("extras/pato_agarrachilaquiles.png");
            } else if (player.getX() > 2500) {
                image = resourceManager.loadImage("Props/pato_juntaingredientes.png");
            } else if (player.getX() > 1400) {
                image = resourceManager.loadImage("Props/pato_enemigosmasgrandes.png");
            } else if (player.getX() > 1000) {
                image = resourceManager.loadImage("extras/pato_instrucciones_02.png");
            }  else if (player.getX() > 770) {
                image = resourceManager.loadImage("extras/pato_instrucciones_01.png");
            } else if (player.getX() > 550) {
                image = resourceManager.loadImage("Props/pato_historia03.png");
            } else if (player.getX() > 350) {
                image = resourceManager.loadImage("Props/pato_historia02.png");
            } else if (player.getX() > 0) {
                image = resourceManager.loadImage("Props/pato_historia01.png");
            }
            
            // pinta la imagen en un rectangulo de su tamaño
            g.fillRect(0, 0, 800, 180);
            g.drawImage(image, 0, 0, null);
        }   
        
        
        if(bPause) {
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawImage(imaPausa , screen.getWidth() / 2 - 
                    imaPausa.getWidth(null)/2,200, null);
        }
        
        if(bLost) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 40));
            g.drawString("R to RESTART", screen.getWidth()/ 2 - 150,
                     250);      
        }
    }


    /**
        Gets the current map.
    */
    public TileMap getMap() {
        return map;
    }


    /**
        Gets the tile that a Sprites collides with. Only the
        Sprite's X or Y should be changed, not both. Returns null
        if no collision is detected.
    */
    public Point getTileCollision(Sprite sprite,
        float newX, float newY)
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(
            toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                    map.getTile(x, y) != null)
                {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }


    /**
        Checks if two Sprites collide with one another. Returns
        false if the two Sprites are the same. Returns false if
        one of the Sprites is a Creature that is not alive.
    */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }


    /**
        Gets the Sprite that collides with the specified Sprite,
        or null if no Sprite collides with the specified Sprite.
    */
    public Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }
   

    /**
        Updates Animation, position, and velocity of all Sprites
        in the current map.
    */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        
        //for our loading screen
        if(iLevel == 0 && iContTime < 50) {
            iContTime++;
        }
        else if(iLevel == 0){
            iContTime = 0;
            iLevel++;
            renderer.setBackground(iLevel);
        }

        // player is dead! start map over
        if (player.getState() == Creature.I_STATE_DEAD) {
            //map = resourceManager.reloadMap();
            iLevel = 7;  //El You lose
            renderer.setBackground(iLevel);
            bLost = true;
            bImPlaying = false;
            bCanMoveL = false;
            bCanMoveR = false;
            // get keyboard/mouse input
            checkInput(elapsedTime);
            //return;
        }

        if(bPause) {
            checkSystemInput(elapsedTime);
        }

        else if(!bPause){      
            
            // get keyboard/mouse input
            checkInput(elapsedTime);
            
            // update player
            updateCreature(player, elapsedTime);

            player.update(elapsedTime);

            // update other sprites
            Iterator i = map.getSprites();
            while (i.hasNext()) {
                Sprite sprite = (Sprite)i.next();
                if (sprite instanceof Creature) {
                    Creature creature = (Creature)sprite;
                    if (creature.getState() == Creature.I_STATE_DEAD) {
                        i.remove();
                    }
                    else {
                        updateCreature(creature, elapsedTime);
                    }
                }
                // normal update
                sprite.update(elapsedTime);
            }
        }
    }


    /**
        Updates the creature, applying gravity for creatures that
        aren't flying, and checks collisions.
    */
    private void updateCreature(Creature creature,
        long elapsedTime)
    {

        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                GRAVITY * elapsedTime);
        }

        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
            getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        }
        else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x) -
                    creature.getWidth());
            }
            else if (dx < 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }
        

        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        }
        else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    creature.getHeight());
            }
            else if (dy < 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }

    }


    /**
        Checks for Player collision with other Sprites. If
        canKill is true, collisions with Creatures will kill
        them.
    */
    public void checkPlayerCollision(Player player, boolean canKill)
    {
        if (!player.isAlive()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        }
        else if (collisionSprite instanceof Minion) {
            Minion badguy = (Minion)collisionSprite;
            if (canKill || bAttack) {
                // kill the badguy and make player bounce
                if(canKill) {
                    player.setY(badguy.getY() - player.getHeight());
                    player.jump(true);

                }
                badguy.setState(Minion.I_STATE_DYING);
                scZombie.play();
                bCanMoveR = true;
                bCanMoveL = true;
                iScore+=5;
                
            }
                        
            //if a second has gone by decrease life
            if(iContVida<30){
                iContVida++;
            }
            else{
                iContVida = 0;
                iLife -= 20;
            }
            //im dead
            if(iLife <= 0) {
                player.setState(Minion.I_STATE_DYING);
                scMusic.stop();
                scBoo.play();
            }
            
            //if player can attack;
            /*if (bAttack) {
                badguy.setState(Minion.I_STATE_DYING);

                //I can move again
                bCanMoveR = true;
                bCanMoveL = true;
                iContVida = 0;    
                
            }*/
           
            /*if the player is on the ground he can only move the oposite 
            direction*/
            
            if (player.bOnGround && !bAttack){
                
                player.setVelocityX(0);
                if(badguy.getX() > player.getX()) {
                    badguy.setX(player.getX() + player.getWidth());
                    bCanMoveR = false;
                }
                
                else {
                    badguy.setX(player.getX() - badguy.getWidth());
                    bCanMoveL = false;

                }
               
                badguy.setAttack();
                
               
            }
            
            else {
                badguy.stopAttack();
            }
            

            
        }
        // checa si el jugador pide el tutorial al colisionar con un pato
        else if (collisionSprite instanceof Duck) {
            bTutLabel = true;  // prende la boleana
        } else {
            bTutLabel = false; // apaga la boleana
        }
        
        //si choco con la lava me muero obviamente
        if(collisionSprite instanceof Lava){
            iLife = 0;
            player.setState(Minion.I_STATE_DYING);
        }

    }


    /**
        Gives the player the speicifed power up and removes it
        from the map.
    */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map

        if (powerUp instanceof PowerUp.Goal) {
            // advance to next map
            if(iIngredientes ==0) {
                if(iLevel < iNumLevels){
                    scNotYet.play();
                    iLevel++;
                    iIngredientes = 4;  //necesito otros 4 ingredientes otra vez
                }
                else {
                    scCheer.play();
                    iLevel = 8;  //El You win
                    renderer.setBackground(iLevel);
                    bLost = true;
                    bImPlaying = false;
                    bCanMoveL = false;
                    bCanMoveR = false;
                }
                map = resourceManager.loadNextMap();
                renderer.setBackground(iLevel);
            }
        }
        else if (powerUp instanceof PowerUp.Coin) {
            iScore+=10;
            scCoin.play();
            map.removeSprite(powerUp);
        }
        else { 
            iIngredientes--;
            map.removeSprite(powerUp);
            scGoal.play();
        }
            
    }
}

