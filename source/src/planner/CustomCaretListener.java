//@author A0111333B

package planner;

import java.awt.Rectangle;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class CustomCaretListener implements CaretListener{

    private int numOfPixelsToShow;
    
    public CustomCaretListener( int numOfPixelsToShow ){
        
        setNumOfPixelsToShow(numOfPixelsToShow);
    }
    
    public void setNumOfPixelsToShow( int numOfPixelsToShow ){
        
        this.numOfPixelsToShow = numOfPixelsToShow;
    }
    
    public int getNumOfPixelsShown(){
        
        return numOfPixelsToShow;
    }
    
    @Override
    public void caretUpdate( final CaretEvent caretEvent ){
        
        SwingUtilities.invokeLater(new Runnable(){
            
            @Override
            public void run(){
                
                try{
                    
                    JTextComponent textComponent = (JTextComponent)caretEvent.getSource();
                    
                    if( textComponent != null ){
                        
                        int caretPosition = textComponent.getCaretPosition();
                        Rectangle visibleRect = textComponent.modelToView(caretPosition);
                        visibleRect.x = visibleRect.x + numOfPixelsToShow;
                        textComponent.scrollRectToVisible(visibleRect);
                    }
        
                } catch( BadLocationException badLocationException ){
                    
                }
            }
        });
    }
}