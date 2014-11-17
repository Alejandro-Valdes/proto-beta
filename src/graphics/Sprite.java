package graphics;

import java.awt.Image;

/**
 *
 * @author Alejandro Valdes
 */
public class Sprite {
    protected Animation animAnimation;
    //position (pixels)
    private float fX;
    private float fY;
    //velocity (pixels per millisecond)
    private float fDx;
    private float fDy;
    
    /**
     * Create a new sprite object with the specified animation
     */
    
    public Sprite(Animation animAnimation) {
        this.animAnimation = animAnimation;
    }
    
    /**
     * Updates this sprites Animation and its position based 
     * on the velocity
     */
    
    public void update(long lElapsedTime) {
        fX += fDx * lElapsedTime;
        fY += fDy * lElapsedTime;
        animAnimation.update(lElapsedTime);
    }
    
    /**
     * Gets this sprites current x position
     */
    
    public float getX(){
        return fX;
    }
    
    /**
     * Gets this sprites current y position
     */
    
    public float getY(){
        return fY;
    }
    
    /**
     * Sets this sprites x position
     */
    
    public void setX(float fX){
        this.fX = fX;
    }
    
    /**
     * Sets this sprites y position
     */
    
    public void setY(float fY){
        this.fY = fY;
    }
    
     /**
        Gets this Sprite's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return animAnimation.getImage().getWidth(null);
    }

    /**
        Gets this Sprite's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return animAnimation.getImage().getHeight(null);
    }
    
    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return fDx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return fDy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float fDx) {
        this.fDx = fDx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float fDy) {
        this.fDy = fDy;
    }

    /**
        Gets this Sprite's current image.
    */
    public Image getImage() {
        return animAnimation.getImage();
    }

    /**
        Clones this Sprite. Does not clone position or velocity
        info.
    */
    public Object clone() {
        return new Sprite(animAnimation);
    }
}
