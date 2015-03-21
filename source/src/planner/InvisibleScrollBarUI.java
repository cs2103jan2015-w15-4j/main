package planner;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class InvisibleScrollBarUI extends BasicScrollBarUI{

    private InvisibleButton downButton = null;
    private InvisibleButton upButton = null;
    
    public InvisibleScrollBarUI(){
        
        super();
        
        downButton = new InvisibleButton(); 
        prepareDownButton(downButton);
        
        upButton = new InvisibleButton();
        prepareUpButton(upButton);
    }
    
    private void prepareDownButton( InvisibleButton currentDownButton ){
        
        if( currentDownButton != null ){
            
            currentDownButton.setFocusable(false);
        }
    }
    
    private void prepareUpButton( InvisibleButton currentUpButton ){
        
        if( currentUpButton != null ){
            
            currentUpButton.setFocusable(false);
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
        
    }
    
    public InvisibleButton getDownButtonComponent(){
        
        return downButton;
    }
    
    public InvisibleButton getUpButtonComponent(){
        
        return upButton;
    }
}
