//@author A0111333B

package planner;

import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
* The CustomCaretListener class controls many pixels to be shown beyond the caret
*
* @author A0111333B
*/
public class CustomCaretListener implements CaretListener{

    private int numOfPixelsToShow_;
    
    private final static Logger customCaretListenerLogger_ = Logger.getLogger(CustomCaretListener.class.getName());
    
    private final int MINIMUM_NUMBER_OF_PIXELS_TO_BE_SHOWN = 0;
    
    /**
     * Constructs a listener to handle many pixels to be shown beyond the caret. If negative integers
     * are passed in, it will automatically set itself to show zero pixels beyond the caret.
     *
     * @param numOfPixelsToShow    The number of pixels to be shown beyond the caret.
     */
    public CustomCaretListener( int numOfPixelsToShow ){
        customCaretListenerLogger_.setLevel(java.util.logging.Level.SEVERE);
        setNumOfPixelsToShow(numOfPixelsToShow);
    }
    
    /**
     * Sets the number of pixels to be shown beyond the caret. If negative integers
     * are passed in, it will automatically set itself to show zero pixels beyond the caret.
     *
     * @param numOfPixelsToShow    The number of pixels to be shown beyond the caret.
     */
    public void setNumOfPixelsToShow( int numOfPixelsToShow ){
        numOfPixelsToShow_ = Math.max(numOfPixelsToShow, MINIMUM_NUMBER_OF_PIXELS_TO_BE_SHOWN);
    }
    
    /**
     * Get the number of pixels to be shown beyond the caret.
     *
     * @return  The number of pixels currently set to be shown beyond the caret
     */
    public int getNumOfPixelsShown(){
        return numOfPixelsToShow_;
    }
    
    /**
     * Setup a listener to control the number of pixels to be shown beyond the caret
     *
     * @param caretEvent   The event fired by the caret that is being listened to
     */
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
                        visibleRect.x = visibleRect.x + numOfPixelsToShow_;
                        textComponent.scrollRectToVisible(visibleRect);
                    }
                } catch( BadLocationException badLocationException ){
                    customCaretListenerLogger_.severe(badLocationException.getMessage());
                }
            }
        });
    }
}