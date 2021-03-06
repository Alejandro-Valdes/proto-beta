/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tilegame.sprites;

import graphics.Animation;
import static tilegame.sprites.Creature.I_DIE_TIME;
import static tilegame.sprites.Creature.I_STATE_DYING;

/**
 *
 * @author Alejandro Valdes
 */
public class Duck extends Creature {
        
    public Duck(Animation a, Animation b, Animation c,
                Animation d, Animation e, Animation f,
                Animation animStand)
    {
        super(animStand, animStand, animStand, animStand, animStand,
                animStand, animStand);
    }
    
    public float getMaxSpeed() {
        return 0.0f;
    }
    
    public void attack() {
        bAttack = false;
    }
    
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
//        if(bMove == true){
//        if (animAnimation != newAnim ) {
//            animAnimation = newAnim;
//            animAnimation.start();
//        }
//        else {
            animAnimation.update(lElapsedTime);
//        }
        
//        }

        // update to "dead" state
        lStateTime += lElapsedTime;
        if (iState == I_STATE_DYING && lStateTime >= I_DIE_TIME) {
            setState(I_STATE_DEAD);
        }
    }
}
