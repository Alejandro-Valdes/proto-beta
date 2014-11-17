package graphics;

import javax.swing.RepaintManager;
import javax.swing.JComponent;

/**
 *
 * @author Alejandro Valdes
 */
public class NullRepaintManager extends RepaintManager {
    
    /**
     * Installs the NullRepaintManager.
     */
    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }
    
    public void addInvalidComponent(JComponent c) {
        // do nothing
    }

    public void addDirtyRegion(JComponent c, int x, int y,
        int w, int h)
    {
        // do nothing
    }

    public void markCompletelyDirty(JComponent c) {
        // do nothing
    }

    public void paintDirtyRegions() {
        // do nothing
    }   
}
