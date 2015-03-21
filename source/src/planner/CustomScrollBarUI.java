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

public class CustomScrollBarUI extends BasicScrollBarUI{

    private final Color SCROLLBAR_THUMB_COLOR = new Color(0,0,0);
            
    private final int SCROLLBAR_THUMB_BORDER_SIZE = 3;
    private final int SCROLLBAR_THUMB_SIZE = 12;
    private final int SCROLLBAR_ALPHA_ROLLBACK_VALUE = 150;
    private final int SCROLLBAR_ALPHA_VALUE = 100;
    
    private InvisibleButton downButton = null;
    private InvisibleButton upButton = null;
    
    public CustomScrollBarUI(){
        
        super();
        
        downButton = new InvisibleButton(); 
        prepareDownButton(downButton);
        
        upButton = new InvisibleButton();
        prepareUpButton(upButton);
    }
    
    private void prepareDownButton( InvisibleButton currentDownButton ){
        
        if( currentDownButton != null ){
            
            currentDownButton.setIcon( new ImageIcon( UserInterface.class.getResource("/planner/arrowDown.png") ) );
        }
    }
    
    private void prepareUpButton( InvisibleButton currentUpButton ){
        
        if( currentUpButton != null ){
            
            currentUpButton.setIcon( new ImageIcon( UserInterface.class.getResource("/planner/arrowUp.png") ) );
        }
    }
    
    @Override
    protected JButton createIncreaseButton( int orientation ){
        
        return downButton;
    }
    
    @Override
    protected JButton createDecreaseButton( int orientation ){
        
        return upButton;
    }
    
    @Override
    protected void paintTrack( Graphics graphics, JComponent component, Rectangle thumbRect ){
    }
    
    @Override
    protected void paintThumb( Graphics graphics, JComponent component, Rectangle thumbRect ){
        
        int scrollbarOrientation = scrollbar.getOrientation();
        
        int xCoordinates = thumbRect.x + SCROLLBAR_THUMB_BORDER_SIZE;
        int yCoordinates = thumbRect.y + SCROLLBAR_THUMB_BORDER_SIZE;
        
        int width = (scrollbarOrientation == JScrollBar.HORIZONTAL ? (thumbRect.width - (SCROLLBAR_THUMB_BORDER_SIZE*2)) : SCROLLBAR_THUMB_SIZE);
        width = Math.max(width, SCROLLBAR_THUMB_SIZE);
        
        int height = (scrollbarOrientation == JScrollBar.HORIZONTAL ? SCROLLBAR_THUMB_SIZE : (thumbRect.height - (SCROLLBAR_THUMB_BORDER_SIZE*2)) );
        height = Math.max(height, SCROLLBAR_THUMB_SIZE);
        
        int alphaColourValue = (isThumbRollover() ? SCROLLBAR_ALPHA_ROLLBACK_VALUE : SCROLLBAR_ALPHA_VALUE);
        
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor( new Color(SCROLLBAR_THUMB_COLOR.getRed(), SCROLLBAR_THUMB_COLOR.getGreen(), SCROLLBAR_THUMB_COLOR.getBlue(), alphaColourValue) );
        graphics2D.fillRoundRect(xCoordinates, yCoordinates, width, height, SCROLLBAR_THUMB_SIZE, SCROLLBAR_THUMB_SIZE);
        graphics2D.dispose();
    }
    
    public InvisibleButton getDownButtonComponent(){
        
        return downButton;
    }
    
    public InvisibleButton getUpButtonComponent(){
        
        return upButton;
    }
}
    
    

