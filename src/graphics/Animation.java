package graphics;

import java.awt.Image;
import java.util.ArrayList;
/**
 *
 * @author Alejandro Valdes
 */
public class Animation {
    private ArrayList arlFrames;
    private int iCurrFrameIndex;
    private long lAnimTime;
    private long lTotalDuration;
    
    /**
     * Creates a new empty Animation.
     */
    public Animation() {
        this(new ArrayList(),0);
    }
    
    /**
     * Constructor Animation
     */
    private Animation(ArrayList arlFrames, long lTotalDuration) {
        this.arlFrames = arlFrames;
        this.lTotalDuration = lTotalDuration;
        start();
    }
    
    /**
     * Creates a duplicate of this animation. The list of frames are shared
     * between the two Animations, but each Animation can be animated 
     * independently.
     */
    
    public Object clone() {
        return new Animation(arlFrames, lTotalDuration);
    }
    
    /**
     * Adds an image to the animation with the specified duration
     * (time to display the image)
     */
    public synchronized void addFrame(Image imaImage, long lDuration) {
        lTotalDuration += lDuration;
        arlFrames.add(new AnimFrame(imaImage, lTotalDuration));
    }
    
    /**
     * starts this animation over from the beginnging.
     */
    public synchronized void start() {
        lAnimTime = 0;
        iCurrFrameIndex = 0;
    }
    
    /**
     * updates this animations current image(frame), if 
     * needed.
     */
    
    public synchronized void update(long lElapsedTime) {
        if(arlFrames.size() > 1) {
            lAnimTime += lElapsedTime;
            
            if(lAnimTime >= lTotalDuration) {
                lAnimTime = lAnimTime % lTotalDuration;
                iCurrFrameIndex = 0;
            }
            
            while(lAnimTime > getFrame(iCurrFrameIndex).lEndTime) {
                iCurrFrameIndex++;
            }
        }
    }
    
    /**
     * Gets this Animations current image, null if no images.
     */
    
    public synchronized Image getImage() {
        if(arlFrames.size()==0) {
            return null;
        }
        else {
            return getFrame(iCurrFrameIndex).imaImage;    
        }
    }
    
    /**
     * returns a certain frame from an animation
     */
    
    private AnimFrame getFrame(int iI) {
        return (AnimFrame)arlFrames.get(iI);
    }
    
    private class AnimFrame {
        Image imaImage;
        long lEndTime;
        
        public AnimFrame(Image imaImage, long lEndTime) {
            this.imaImage = imaImage;
            this.lEndTime = lEndTime;
        }
    }
}
