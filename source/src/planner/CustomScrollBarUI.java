//@author A0111333B

package planner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
* The CustomScrollBarUI class implements a custom scroll bar with customized thumb and buttons
*
* @author A0111333B
*/
public class CustomScrollBarUI extends BasicScrollBarUI{

    private final Color SCROLLBAR_THUMB_COLOR = new Color(0,0,0);
            
    private final int SCROLLBAR_THUMB_BORDER_SIZE = 3;
    private final int SCROLLBAR_THUMB_SIZE = 12;
    private final int SCROLLBAR_ALPHA_ROLLBACK_VALUE = 150;
    private final int SCROLLBAR_ALPHA_VALUE = 100;
    private final int SCROLLBAR_THUMB_BORDER_SCALE_FACTOR = 2;
    
    private InvisibleButton downButton = null;
    private InvisibleButton upButton = null;
    
    private final String UP_ARROW_IMAGE_RESOURCE_LINK = "/planner/arrowUp.png";
    private final String DOWN_ARROW_IMAGE_RESOURCE_LINK = "/planner/arrowDown.png";
    
    /**
    * The CustomScrollBarUI class constructs a custom scroll bar with customized thumb and buttons
    */
    public CustomScrollBarUI(){
        super();
        downButton = new InvisibleButton(); 
        prepareDownButton(downButton);
        upButton = new InvisibleButton();
        prepareUpButton(upButton);
    }
    
    /**
     * Initializes the down arrow button component of the scrollbar with its corresponding image resource
     * 
     * @param currentDownButton     The down arrow button component of the scrollbar to be initialized with its corresponding 
     *                              image resource
     */
    private void prepareDownButton( InvisibleButton currentDownButton ){
        if( currentDownButton != null ){
            currentDownButton.setIcon( new ImageIcon( UserInterface.class.getResource(DOWN_ARROW_IMAGE_RESOURCE_LINK) ) );
        }
    }
    
    /**
     * Initializes the up arrow button component of the scrollbar with its corresponding image resource
     * 
     * @param currentUpButton     The up arrow button component of the scrollbar to be initialized with its corresponding 
     *                            image resource
     */
    private void prepareUpButton( InvisibleButton currentUpButton ){
        if( currentUpButton != null ){
            currentUpButton.setIcon( new ImageIcon( UserInterface.class.getResource(UP_ARROW_IMAGE_RESOURCE_LINK) ) );
        }
    }
    
    /**
     * Returns the button that represent the decrease view 
     * 
     * @param orientation     The orientation of the scrollbar that will be using this button
     */
    @Override
    protected JButton createIncreaseButton( int orientation ){
        return downButton;
    }
    
    /**
     * Returns the button that represent the increase view 
     * 
     * @param orientation     The orientation of the scrollbar that will be using this button
     */
    @Override
    protected JButton createDecreaseButton( int orientation ){
        return upButton;
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
        int scrollbarOrientation = scrollbar.getOrientation();
        int xCoordinates = thumbRect.x + SCROLLBAR_THUMB_BORDER_SIZE;
        int yCoordinates = thumbRect.y + SCROLLBAR_THUMB_BORDER_SIZE;
        int width;
        int height;
        
        if( scrollbarOrientation == JScrollBar.HORIZONTAL ){
            width = thumbRect.width - (SCROLLBAR_THUMB_BORDER_SIZE*SCROLLBAR_THUMB_BORDER_SCALE_FACTOR);
        } else{
            width = SCROLLBAR_THUMB_SIZE;
        }
        width = Math.max(width, SCROLLBAR_THUMB_SIZE);
        
        if( scrollbarOrientation == JScrollBar.HORIZONTAL ){
            height = SCROLLBAR_THUMB_SIZE;
        } else{
            height = thumbRect.height - (SCROLLBAR_THUMB_BORDER_SIZE*SCROLLBAR_THUMB_BORDER_SCALE_FACTOR);
        }
        height = Math.max(height, SCROLLBAR_THUMB_SIZE);
        
        int alphaColourValue = (isThumbRollover() ? SCROLLBAR_ALPHA_ROLLBACK_VALUE : SCROLLBAR_ALPHA_VALUE);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor( new Color(SCROLLBAR_THUMB_COLOR.getRed(), SCROLLBAR_THUMB_COLOR.getGreen(), SCROLLBAR_THUMB_COLOR.getBlue(), alphaColourValue) );
        graphics2D.fillRoundRect(xCoordinates, yCoordinates, width, height, SCROLLBAR_THUMB_SIZE, SCROLLBAR_THUMB_SIZE);
        graphics2D.dispose();
    }
    
    /**
     * Returns the custom down button component used for the custom scrollbar.
     * 
     * @return The down button component used for the custom scrollbar
     */
    public InvisibleButton getDownButtonComponent(){
        return downButton;
    }
    
    /**
     * Returns the custom up button component used for the custom scrollbar.
     * 
     * @return The up button component used for the custom scrollbar
     */
    public InvisibleButton getUpButtonComponent(){
        return upButton;
    }
}
    
    

