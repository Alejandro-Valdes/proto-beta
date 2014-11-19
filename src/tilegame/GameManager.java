package tilegame;

import graphics.*;
import input.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import test.GameCore;
import tilegame.sprites.*;

/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {

    public static void main(String[] args) {
        new GameManager().run();
    }

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;

    private Point pointCache = new Point();
    private TileMap map;
    private ResourceManager resourceManager;;
    private InputManager inputManager;
    private TileMapRenderer renderer;

    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction pause;
    private GameAction next;

    private boolean bCanMoveR;
    private boolean bCanMoveL;
    private boolean bPause;
    private int iLevel;
    private int iContTime;
    private int iVida;
    private int iContVida;

    public void init() {
        super.init();

        // set up input manager
        initInput();

        // start resource manager
        resourceManager = new ResourceManager(
        screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        iLevel = 0;
        iContTime = 0;
        iVida = 100;
        iContVida = 0;
        
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/valkyrie.png"));
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/Menu.png"));
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/tutorial.png"));
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level1.png"));
        
        renderer.addBackground(
            resourceManager.loadImage("backgrounds/level2.png"));
        
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


    private void initInput() {
        bCanMoveR = true;
        bCanMoveL = true;
        bPause = false;
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump",
            GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        exit = new GameAction("exit",
            GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        pause = new GameAction("pause", 
            GameAction.I_DETECT_INITIAL_PRESS_ONLY);
        next = new GameAction("next");
        
        inputManager = new InputManager(
            screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(pause, KeyEvent.VK_P);
        inputManager.mapToKey(next, KeyEvent.VK_S);
        
    }

    private void checkSystemInput(long elapsedTime) {
        
        if(pause.isPressed()){
            bPause = false; 
            pause.reset();
        }
    }

    private void checkInput(long elapsedTime) {
            
            if (exit.isPressed()) {
                stop();
            }

            if(next.isPressed() && iLevel == 1) {
                iLevel++;
                renderer.setBackground(iLevel);        
            }

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
                
                if (pause.isPressed()) {
                    bPause = true;
                    pause.reset();
                }

                if (jump.isPressed()) {
                    player.jump(false);
                    bCanMoveR = true;
                    bCanMoveL = true;
                }

                player.setVelocityX(velocityX);
            }
    }


    public void draw(Graphics2D g) {
        renderer.draw(g, map,
            screen.getWidth(), screen.getHeight());
        
        if(iLevel == 1) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawString("PROYECT A00" , screen.getWidth() / 2 - 200,
                     200);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("PRESS S TO START" , screen.getWidth() / 2 - 110,
                     230);
            g.drawString("PRESS P TO PAUSE" , screen.getWidth() / 2 - 110,
                     250);
            g.drawString("PRESS ESC TO EXIT" , screen.getWidth() / 2 - 110,
                     270);
            
        }
        
        else if(iLevel > 1) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 40));
            g.drawString("LIFE: " + iVida , screen.getWidth() - 200,
                     50);
        }
        
        if(bPause) {
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawString("PAUSE" , screen.getWidth() / 2 - 200,
                     200);
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
        
        if(iLevel == 0 && iContTime < 120) {
            iContTime++;
        }
        else if(iLevel == 0){
            iContTime = 0;
            iLevel++;
            renderer.setBackground(iLevel);
        }

        // player is dead! start map over
        if (player.getState() == Creature.I_STATE_DEAD) {
            map = resourceManager.reloadMap();
            return;
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
    public void checkPlayerCollision(Player player,
        boolean canKill)
    {
        if (!player.isAlive()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        }
        else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            
            if(iContVida<30){
                iContVida++;
            }
            else{
                iContVida = 0;
                iVida -= 5;
            }
            if(iVida < 0) {
                player.setState(Creature.I_STATE_DYING);
            }
            if (canKill) {
                // kill the badguy and make player bounce
                badguy.setState(Creature.I_STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                bCanMoveR = true;
                bCanMoveL = true;
                player.jump(true);
                
            }
            else if (player.bOnGround){
                // player dies!
                
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
    }


    /**
        Gives the player the speicifed power up and removes it
        from the map.
    */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        if (powerUp instanceof PowerUp.Goal) {
            // advance to next map
            if(iLevel < 4){
                iLevel++;
            }
            else {
                iLevel = 1;
                resourceManager.setiCurrentMap(2);
            }
            map = resourceManager.loadNextMap();
            renderer.setBackground(iLevel);
        }
    }

}

