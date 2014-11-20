package tilegame.sprites;

import graphics.Animation;

/**
 * VALKYRIE
 * ANDREA JAQUELINE BOONE MARTINEZ A01139540
 * JESUS ALEJANDRO VALDES VALDES A00999044
 * JORGE ALFONSO GONZALEZ HERRERA A00999088
 * LUIS ALBERTO LAMADRID TAFICH A01191158
 */

/**
 * 
 * The Player, extends Creature
*/
public class Player extends Creature {

    private static final float F_JUMP_SPEED = -.95F;

    public boolean bOnGround;
    

    public Player(Animation animLeft, Animation animRight,
        Animation animDeadLeft, Animation animDeadRight,
            Animation animAttackLeft, Animation animAttackRight, 
                Animation animStand)
    {
        
        super(animLeft, animRight, animDeadLeft, animDeadRight, 
                animAttackLeft, animAttackRight, animStand);
    }

    public void collideHorizontal() {
        setVelocityX(0);
    }


    /**
     * Checks if collides with ground
     */
    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            bOnGround = true;
        }
        setVelocityY(0);
    }


    /**
     * Sets the y parameter
     * @param y 
     */
    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            bOnGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }
    
    /**
     * Allows the player to stand in one place
     */
    public void stand(){
        if(getVelocityX()== 0){
        bMove = false;
        }
        else{
        bMove = false;
        }
    }


    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
     * @param bForceJump makes the player jump
    */
    public void jump(boolean bForceJump) {
        if (bOnGround || bForceJump) {
            bOnGround = false;
            setVelocityY(F_JUMP_SPEED);
        }
    }


    /**
     * returns the max speed of the player
     * @return 
     */
    public float getMaxSpeed() {
        return 0.25f;
    }
    
    /**
     * updates to selece the correct animation
     * @param lElapsedTime 
     */
    public void update(long lElapsedTime){   // select the correct Animation
        Animation newAnim = animAnimation;
        if (getVelocityX() < 0) {
            newAnim = animLeft;
        }
        else if (getVelocityX() > 0) {
            newAnim = animRight;
        }
        
        else {
            newAnim = animStand;
        }
        
        if (getVelocityX() < 0&& bAttack) {
            newAnim = animAttackLeft;
        }
        else if (getVelocityX() >= 0 && bAttack) {
            newAnim = animAttackRight;
        } 
        
        if (iState == I_STATE_DYING && newAnim == animLeft) {
            newAnim = animDeadLeft;
        }
        else if (iState == I_STATE_DYING && newAnim == animRight) {
            newAnim = animDeadRight;
        }
        
        if(getVelocityY() != 0) {
            newAnim = animRight;
        }
              
        // update the Animation
        if(bMove == true){
            if (animAnimation != newAnim ) {
                animAnimation = newAnim;
                animAnimation.start();
            }
            else {
                animAnimation.update(lElapsedTime);
            }
        
        }

        // update to "dead" state
        lStateTime += lElapsedTime;
        if (iState == I_STATE_DYING && lStateTime >= I_DIE_TIME) {
            setState(I_STATE_DEAD);
        }
    }

    @Override
    public void attack() {
        bAttack = true;
    }

}
