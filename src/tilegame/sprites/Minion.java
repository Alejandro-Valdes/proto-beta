/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * Minion class for all the bad guys
 */
public class Minion extends Creature {
        
    public Minion(Animation left, Animation right, 
            Animation deadLeft, Animation deadRight, Animation animAttackLeft,
                Animation animAttackRight, Animation animStand)
    {
        super(left, right, deadLeft, deadRight, animAttackLeft,
                animAttackRight, animStand);
    }
    
    public float getMaxSpeed() {
        return 0.2f;
    }
    
    public void attack() {
        bAttack = true;
    }
    
    /**
     * Selects the correct animation
     * @param lElapsedTime 
     */
    public void update(long lElapsedTime){   // select the correct Animation
        Animation newAnim = animAnimation;
        
        if (bAttack && getVelocityX() < 0) {
            newAnim = animAttackLeft;
        }
        
        else if(bAttack && getVelocityX() > 0) {
            newAnim = animAttackRight;
        }
        
        else if (getVelocityX() < 0) {
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
}
