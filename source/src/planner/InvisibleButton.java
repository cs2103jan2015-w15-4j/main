//@author A0111333B

package planner;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
* The InvisibleButton class is a customised button component that is invisible.
*
* @author A0111333B
*/
public class InvisibleButton extends JButton{

    private final Color ZERO_ALPHA_BLACK_COLOR = new Color(0,0,0,0);
    
    private final int DEFAULT_WIDTH = 10;
    private final int DEFAULT_HEIGHT = 15;
    
    /**
     * Constructs a button component that is invisible.
     */
    public InvisibleButton(){
        
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusable(true);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(ZERO_ALPHA_BLACK_COLOR);
    }
    
    /**
     * Returns the preferred size of this button component.
     *
     * @return   The preferred size of this button component.
     */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
}
