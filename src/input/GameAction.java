package input;


/**
 * VALKYRIE
 * ANDREA JAQUELINE BOONE MARTINEZ A01139540
 * JESUS ALEJANDRO VALDES VALDES A00999044
 * JORGE ALFONSO GONZALEZ HERRERA A00999088
 * LUIS ALBERTO LAMADRID TAFICH A01191158
 */

/**
 *The GameAction class is an abstract to a user-initiated
 *action, like jumping or moving. GameActions can be mapped
 *to keys 
 * 
 */
public class GameAction {
    /**
     * Normal behavior. The isPressed() method returns true as long
     * as the key is held down
     */
    
    public static final int I_NORMAL = 0;
    
    /**
     * Initial press behavior. The isPressed() method returns
     * true only after the key is first pressed, and not again 
     * until the key is released and pressed again.
     */
    public static final int I_DETECT_INITIAL_PRESS_ONLY = 1;
    
    private static final int I_STATE_RELEASED = 0;
    private static final int I_STATE_PRESSED = 1;
    private static final int I_STATE_WAITING_FOR_RELEASE = 2;
    
    private String sName;
    private int iBehavior;
    private int iAmount;
    private int iState;
    
    /**
     * Create a new GameAction with the I_NORMAL behavior.
     */
    public GameAction(String sName) {
        this(sName, I_NORMAL);
    }
    
    /**
     * Create a new GameAction with the specified behavior.
     */  
    public GameAction(String name, int behavior) {
        this.sName = sName;
        this.iBehavior = iBehavior;
        reset();
    }
    
    /**
     * Gets the name of this GameAction
     */
    public String getName() {
        return sName;
    }
    
    /**
     * Resets this GameAction so that it appears like it hasn't
     * been pressed.
     */
    public void reset() {
        iState = I_STATE_RELEASED;
        iAmount = 0;
    }
    
    /**
     * Taps this GameAction. Same as calling press() followed 
     * by release().
     */
    public synchronized void tap() {
        press();
        release();
    }
    
    /**
     * signals that the key was pressed.
     */
    public synchronized void press() {
        press(1);
    }
    
    /**
        Signals that the key was pressed a specified number of
        times, or that the mouse move a spcified distance.
    */
    public synchronized void press(int iAmount) {
        if (iState != I_STATE_WAITING_FOR_RELEASE) {
            this.iAmount+=iAmount;
            iState = I_STATE_PRESSED;
        }

    }

    /**
        Signals that the key was released
    */
    public synchronized void release() {
        iState = I_STATE_RELEASED;
    }


    /**
        Returns whether the key was pressed or not since last
        checked.
    */
    public synchronized boolean isPressed() {
        return (getAmount() != 0);
    }


    /**
        For keys, this is the number of times the key was
        pressed since it was last checked.
        For mouse movement, this is the distance moved.
    */
    public synchronized int getAmount() {
        int iRetVal = iAmount;
        if (iRetVal != 0) {
            if (iState == I_STATE_RELEASED) {
                iAmount = 0;
            }
            else if (iBehavior == I_DETECT_INITIAL_PRESS_ONLY) {
                iState = I_STATE_WAITING_FOR_RELEASE;
                iAmount = 0;
            }
        }
        return iRetVal;
    }
   
}
