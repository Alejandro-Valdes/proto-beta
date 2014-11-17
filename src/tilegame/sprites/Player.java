package tilegame.sprites;

import graphics.Animation;

/**
 * 
 * The Player.
*/
public class Player extends Creature {

    private static final float F_JUMP_SPEED = -.95F;

    public boolean bOnGround;
    

    public Player(Animation animLeft, Animation animRight,
        Animation animDeadLeft, Animation animDeadRight,
            Animation animAttackLeft, Animation animAttackRight)
    {
        
        super(animLeft, animRight, animDeadLeft, animDeadRight, 
                animAttackLeft, animAttackRight);
    }


    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            bOnGround = true;
        }
        setVelocityY(0);
    }


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
    */
    public void jump(boolean bForceJump) {
        if (bOnGround || bForceJump) {
            bOnGround = false;
            setVelocityY(F_JUMP_SPEED);
        }
    }


    public float getMaxSpeed() {
        return 0.25f;
    }
    
    public void update(long lElapsedTime){   // select the correct Animation
        Animation newAnim = animAnimation;
        if (getVelocityX() < 0) {
            newAnim = animLeft;
        }
        else if (getVelocityX() > 0) {
            newAnim = animRight;
        }
        if (iState == I_STATE_DYING && newAnim == animLeft) {
            newAnim = animDeadLeft;
        }
        else if (iState == I_STATE_DYING && newAnim == animRight) {
            newAnim = animDeadRight;
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
    }

}
