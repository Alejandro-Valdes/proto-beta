package tilegame.sprites;

import java.lang.reflect.Constructor;
import graphics.*;

/**
 *
 * @author Alejandro Valdes
 */
public abstract class Creature extends Sprite {
    
    
    /**
     * Amount of time to go from STATE_FYING to STATE_DEAD
     */
    
    protected static final int I_DIE_TIME = 1000;
    
    public static final int I_STATE_NORMAL = 0;
    public static final int I_STATE_DYING = 1;
    public static final int I_STATE_DEAD = 2;
    public boolean bMove;
    public boolean bAttack;
    
    protected Animation animLeft;
    protected Animation animRight;
    protected Animation animDeadLeft;
    protected Animation animDeadRight;
    protected Animation animAttackLeft;
    protected Animation animAttackRight;
    protected Animation animStand;
    protected int iState;
    protected long lStateTime;
    
    /**
     * Create a new creature with the specified Animation
     */
    
    public Creature(Animation animLeft, Animation animRight,
            Animation animDeadLeft, Animation animDeadRight, 
                Animation animAttackLeft, Animation animAttackRight,
                    Animation animStand) {
        super(animRight);
        
        this.animLeft = animLeft;
        this.animRight = animRight;
        this.animDeadLeft = animDeadLeft;
        this.animDeadRight = animDeadRight;
        
        if(animAttackLeft == null) {
            this.animAttackLeft = animLeft;
        }
        else {
            this.animAttackLeft = animAttackLeft;
        }
        
        if(animAttackRight == null) {
            this.animAttackRight = animRight;
        }
        else {
            this.animAttackRight = animAttackRight;
        }
        
        if(animStand == null) {
            this.animStand = animLeft;
        }
        else {
            this.animStand = animStand;
        }
        
        iState = I_STATE_NORMAL;
        bMove = true;
        bAttack = false;
                
    }
    
    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(new Object[] {
                (Animation)animLeft.clone(),
                (Animation)animRight.clone(),
                (Animation)animDeadLeft.clone(),
                (Animation)animDeadRight.clone(),
                (Animation)animAttackLeft.clone(),
                (Animation)animAttackRight.clone(),
                (Animation)animStand.clone()
            });
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }

    public void setAttack() {
        bAttack = true;
    }
    
    public void stopAttack() {
        bAttack = false;
    }
    /**
        Gets the maximum speed of this Creature.
    */
    public float getMaxSpeed() {
        return 0;
    }


    /**
        Wakes up the creature when the Creature first appears
        on screen. Normally, the creature starts moving left.
    */
    public void wakeUp() {
        if (getState() == I_STATE_NORMAL && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
    }


    /**
        Gets the state of this Creature. The state is either
        STATE_NORMAL, STATE_DYING, or STATE_DEAD.
    */
    public int getState() {
        return iState;
    }


    /**
        Sets the state of this Creature to STATE_NORMAL,
        STATE_DYING, or STATE_DEAD.
    */
    public void setState(int state) {
        if (this.iState != state) {
            this.iState = state;
            lStateTime = 0;
            if (state == I_STATE_DYING) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }


    /**
        Checks if this creature is alive.
    */
    public boolean isAlive() {
        return (iState == I_STATE_NORMAL);
    }


    /**
        Checks if this creature is flying.
    */
    public boolean isFlying() {
        return false;
    }


    /**
        Called before update() if the creature collided with a
        tile horizontally.
    */
    public void collideHorizontal() {
        setVelocityX(-getVelocityX());
    }


    /**
        Called before update() if the creature collided with a
        tile vertically.
    */
    public void collideVertical() {
        setVelocityY(0);
    }


    /**
        Updates the animaton for this creature.
    */
    public abstract void update(long lElapsedTime);
    
    public abstract void attack();

}
