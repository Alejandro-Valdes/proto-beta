/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tilegame.sprites;

import graphics.Animation;

/**
 *
 * @author Alejandro Valdes
 */
public class Lava extends Creature {

    public Lava(Animation animLeft, Animation animRight, Animation animDeadLeft, Animation animDeadRight, Animation animAttackLeft, Animation animAttackRight, Animation animStand) {
        super(animLeft, animRight, animDeadLeft, animDeadRight, animAttackLeft, animAttackRight, animStand);
    }

    @Override
    public void update(long lElapsedTime) {
        animAnimation.update(lElapsedTime);
    }

    @Override
    public void attack() {
        bAttack = false;
    }
    
}
