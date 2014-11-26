package tilegame.sprites;

import java.lang.reflect.Constructor;
import graphics.*;

/**
 * VALKYRIE
 * ANDREA JAQUELINE BOONE MARTINEZ A01139540
 * JESUS ALEJANDRO VALDES VALDES A00999044
 * JORGE ALFONSO GONZALEZ HERRERA A00999088
 * LUIS ALBERTO LAMADRID TAFICH A01191158
 */

/**
    A PowerUp class is a Sprite that the player can pick up.
    * Here we'll have our ingredients for chilaquiles.
*/
public abstract class PowerUp extends Sprite {

    public PowerUp(Animation anim) {
        super(anim);
    }


    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Animation)animAnimation.clone()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


    /**
        PLayer needs ingredients. Gives the player the oportunity to advance.
    */
    public static class Frijol extends PowerUp {
        public Frijol(Animation anim) {
            super(anim);
        }
    }
    
        /**
        PLayer needs ingredients. Gives the player the oportunity to advance.
    */
    public static class Salsa extends PowerUp {
        public Salsa(Animation anim) {
            super(anim);
        }
    }
    
        /**
        PLayer needs ingredients. Gives the player the oportunity to advance.
    */
    public static class Queso extends PowerUp {
        public Queso(Animation anim) {
            super(anim);
        }
    }
    
        /**
        PLayer needs ingredients. Gives the player the oportunity to advance.
    */
    public static class Tortilla extends PowerUp {
        public Tortilla(Animation anim) {
            super(anim);
        }
    }


    /**
     *  A Coing powerUp. Increases score
     */ 
    public static class Coin extends PowerUp {
        public Coin(Animation anim) {
            super(anim);
        }
    }


    /**
        A Goal PowerUp. Advances to the next map.
    */
    public static class Goal extends PowerUp {
        public Goal(Animation anim) {
            super(anim);
        }
    }

}
