//@author A0111333B

package planner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

/**
* The TranslucentTextPane class is a text component that can render a translucent background color for itself and automatically
* adjust its size to fit the text displayed within it.
*
* @author A0111333B
*/
public class TranslucentTextPane extends JTextPane{

    private Color backgroundColor_;
    
    private final String EMPTY_STRING = "";
    private final String REGEX_KEYWORD_SEPERATOR = "|";
    
    private final int MINIMUM_NONEMPTY_STRING_LENGTH = 1;
    private final int LENGTH_OF_ONE_CHARACTER_IN_STRING = 1;
    private final int MINIMUM_STRING_LENGTH = 0;
    
    /**
    * Constructs a text component that can render a translucent background color for itself and automatically
    * adjust its size to fit the text displayed within it. For a translucent background, the alpha value
    * of the backgroundColor should be set as well.
    *
    * @param backgroundColor    The background color that is going to be set for the text component.
    */
    public TranslucentTextPane( Color backgroundColor ){
        setOpaque(false);
        if( backgroundColor != null ){
            backgroundColor_ = backgroundColor;
        } else{
            backgroundColor_ = Color.BLACK;
        }
        setEditorKit(new CustomWrapKit());
    }
    
    /**
     * Sets the text component to be opaque or not. In this case, this method will prevent the text
     * component to be set as opaque.
     *
     * @param isOpaque    Flag to indicate if text component is to be set to be opaque or not
     */
    @Override
    public void setOpaque( boolean isOpaque ){
        super.setOpaque(false);
    }
    
    /**
     * Renders the appearance of the text field component and text within it;
     *
     * @param graphics  Component that provide access to rendering information and the rendering of the text field component
     */
    @Override
    public void paintComponent( Graphics graphics ){
        
        graphics.setColor(backgroundColor_);
        Insets posAttribute = getInsets();
        int yCoordinate = posAttribute.top;
        int xCoordinate = posAttribute.left;
        int componentWidth = getWidth() - posAttribute.right - posAttribute.left;
        int componentHeight = getHeight() - posAttribute.bottom - posAttribute.top;
        graphics.fillRoundRect(xCoordinate, yCoordinate, componentWidth, componentHeight, 10, 10);
        super.paintComponent(graphics);
    }
    
    /**
     * Adjusts the text component's size to fit the text displayed within it
     */
    public void adjustComponentSizeToFitText(){
        int newHeightOfComponent = (getPreferredSize() != null ? getPreferredSize().height : getHeight());
        super.setSize(getWidth(), newHeightOfComponent);
    }
    
    /**
     * Sets the text to be displayed in this text component. The text component will then adjust its size to fit the text. This
     * method will overwrite any text previously displayed within this text component.
     * 
     * @param text  The text to be displayed in this text component
     */
    @Override
    public void setText( String text ){
        setSize(getWidth(), Short.MAX_VALUE);
        super.setText(text);
        adjustComponentSizeToFitText();
    }
    
    /**
     * Sets the appropriate attributes of the text component so that the appropriate text component's size can be accurately 
     * calculated by adjustComponentSizeToFitText() method to fit the text displayed within it. NOTE: This method should be 
     * called before calling the adjustComponentSizeToFitText() method.
     */
    public void initialiseForResize(){
        setSize(getWidth(), Short.MAX_VALUE);
    }
    
    /**
     * Inserts the text at the end of the document displayed in this text component. This method will not resize the text component
     * to fit the text. If resizing text component is necessary, the user should call the initialiseForResize() method -> this method ->
     * adjustComponentSizeToFitText() method to adjust its size to fit the text displayed within it.
     * 
     * @param text      The text to be appended to the document displayed in this text component
     * @param style     The font style of the appended text to be rendered in
     */
    public void appendText( String text, AttributeSet style ){
        try {
            if( text != null ){
                StyledDocument doc = getStyledDocument();
                doc.insertString(doc.getLength(), text, style);
                doc.setCharacterAttributes(doc.getLength(), doc.getLength()+1, style, false);
            }
        } catch (BadLocationException e) {}
    }
    
    
    /**
     * Generates a regex pattern without word boundaries from the list of keywords given. Null keywords will not be included
     * in the generated regex pattern.
     * 
     * @param keywords      List of keywords to be compiled into a regex pattern without word boundaries
     * @return              The regex pattern generated from the list of keywords given
     */
    private String generateRegexWithoutWordBoundary( String []keywords ){
        StringBuilder regex = new StringBuilder(EMPTY_STRING);
        if( keywords != null ){
            for( int i = 0, size = keywords.length; i < size; ++i ){
                if( keywords[i] != null ){
                    regex.append(keywords[i]).append(REGEX_KEYWORD_SEPERATOR);
                }
            }
            if( regex.length() > MINIMUM_NONEMPTY_STRING_LENGTH ){
                regex.deleteCharAt(regex.length()-LENGTH_OF_ONE_CHARACTER_IN_STRING);
            }
        }
        return regex.toString();
    }
    
    /**
     * Renders the keywords that are found in the list with the given style
     * 
     * @param list     List of keywords to be syntax colour with the given style
     * @param style    The style that the keywords will be rendered in
     */
    public void highlightWords( String []list, SimpleAttributeSet style ){
         try {
            if( list != null && list.length > MINIMUM_STRING_LENGTH ){
                StyledDocument doc = getStyledDocument();
                if( doc != null && doc.getLength() > MINIMUM_STRING_LENGTH ){
                    String regex = generateRegexWithoutWordBoundary(list);
                    Pattern pattern = Pattern.compile(regex);
                    String currentString = doc.getText(MINIMUM_STRING_LENGTH, doc.getLength()); 
                    Matcher matcher = pattern.matcher(currentString);
                    
                    int startIdx;
                    int endIdx;
                    while( matcher.find() ){
                        startIdx = matcher.start();
                        endIdx = matcher.end();
                        doc.setCharacterAttributes(startIdx, endIdx-startIdx, style, true);
                    }
                }
            }
        } catch (BadLocationException e) {}
    }
}
