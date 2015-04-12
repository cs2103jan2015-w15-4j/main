//@author A0111333B

package planner;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
* The InvisibleScrollBarUI class is a customised scroll bar that is invisible
*
* @author A0111333B
*/
public class InvisibleScrollBarUI extends BasicScrollBarUI{

    private InvisibleButton downButton_ = null;
    private InvisibleButton upButton_ = null;
    
    /**
     * Constructs a scroll bar that is invisible
     */
    public InvisibleScrollBarUI(){
        super();
        downButton_ = new InvisibleButton(); 
        prepareButton(downButton_);
        upButton_ = new InvisibleButton();
        prepareButton(upButton_);
    }
    
    /**
     * Set attributes of the buttons of the scroll bar so that it will not be focusable
     * 
     * @param currentButton The button of the scroll bar that is going to be set to be unfocusable
     */
    private void prepareButton( InvisibleButton currentButton ){
        if( currentButton != null ){
            currentButton.setFocusable(false);
        }
    }
    
    /**
     * Returns the button that represent the decrease view 
     * 
     * @param orientation     The orientation of the scrollbar that will be using this button
     */
    @Override
    protected JButton createIncreaseButton( int orientation ){
        return downButton_;
    }
    
    /**
     * Returns the button that represent the increase view 
     * 
     * @param orientation     The orientation of the scrollbar that will be using this button
     */
    @Override
    protected JButton createDecreaseButton( int orientation ){
        return upButton_;
    }
    
    /**
     * Handles the rendering of the scrollbar track
     * 
     * @param graphics     An object that provides access to scrollbar track component rendering
     * @param component    The scrollbar component
     * @param thumbRect    The x coordinates, y coordinates, width and height of the scrollbar thumb
     */
    @Override
    protected void paintTrack( Graphics graphics, JComponent component, Rectangle thumbRect ){
    }
    
    /**
     * Handles the rendering of the scrollbar thumb
     * 
     * @param graphics     An object that provides access to scrollbar thumb component rendering
     * @param component    The scrollbar component
     * @param thumbRect    The x coordinates, y coordinates, width and height of the scrollbar thumb
     */
    @Override
    protected void paintThumb( Graphics graphics, JComponent component, Rectangle thumbRect ){
    }
    
    /**
     * Returns the custom down button component used for the invisible scrollbar.
     * 
     * @return The down button component used for the invisible scrollbar
     */
    public InvisibleButton getDownButtonComponent(){
        return downButton_;
    }
    
    /**
     * Returns the custom up button component used for the invisible scrollbar.
     * 
     * @return The up button component used for the invisible scrollbar.
     */
    public InvisibleButton getUpButtonComponent(){
        return upButton_;
    }
}
