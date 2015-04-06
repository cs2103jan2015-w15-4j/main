package planner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextPane;

public class TranslucentTextPane extends JTextPane{

    private Color backgroundColor_;
    
    public TranslucentTextPane( Color backgroundColor ){
        
        setOpaque(false);
        
        if( backgroundColor != null ){
            
            backgroundColor_ = backgroundColor;
            
        } else{
            
            backgroundColor_ = Color.BLACK;
        }
        
        setEditorKit(new CustomWrapKit());
    }
    
    @Override
    public void setOpaque( boolean isOpaque ){
        
        super.setOpaque(false);
    }
    
    @Override
    public void paintComponent( Graphics graphics ){
        
        graphics.setColor(backgroundColor_);
        
        Insets posAttribute = getInsets();
        
        int yCoordinate = posAttribute.top;
        int xCoordinate = posAttribute.left;
        
        int componentWidth = getWidth() - posAttribute.right - posAttribute.left;
        int componentHeight = getHeight() - posAttribute.bottom - posAttribute.top;
        
        graphics.fillRect(xCoordinate, yCoordinate, componentWidth, componentHeight);
        
        super.paintComponent(graphics);
    }
    
    private void adjustComponentSizeToFitText( String text ){
        
        setSize(getWidth(), Short.MAX_VALUE);
        
        super.setText(text);
        
        int newHeightOfComponent = (getPreferredSize() != null ? getPreferredSize().height : getHeight());
        
        super.setSize(getWidth(), newHeightOfComponent);
        
        super.setPreferredSize(new Dimension( getWidth(), newHeightOfComponent ));
    }
    
    @Override
    public void setText( String text ){
        
        adjustComponentSizeToFitText(text);
    }
}
