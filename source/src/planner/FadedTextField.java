//@author A0111333B

package planner;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.logging.Logger;

import javax.swing.JTextField;

/**
* The FadedTextField class is a text field component that truncates very long strings and applies a faded effect to a few characters towards the
* end of such strings. If the text fits within the width of the FadedTextField, the string will be displayed normally.
*
* @author A0111333B
*/
public class FadedTextField extends JTextField{

	private Color fromColor_;
	private Color toColor_;
	private Color backgroundColor_;
	
	private Insets componentCoordinates_;
	
	private int maxCharacters_;
	private int numCharactersBeforeFade_;
	
	private final Color DEFAULT_TO_COLOUR = new Color( 192,192,192 );
	
	private final int MIN_NUM_OF_CHARACTERS = 0;
	private final int MAX_NUM_OF_CHARACTERS = 1000;
	private final int MAX_NUM_OF_NONFADED_CHARACTERS = 999;
	private final int DEFAULT_MAX_NUM_OF_CHARACTERS = 15;
	private final int DEFAULT_MAX_NUM_OF_NONFADED_CHARACTERS = 9;
	private final int MINIMUM_STRING_LENGTH = 0;
	private final int LENGTH_OF_ONE_CHARACTER = 1;
	private final int MINIMUM_START_INDEX_OF_STRING = 0;
	private final int NUM_OF_CHARACTERS_TO_APPEND_TO_TRUNCATED_STRING = 3;
	private final int MINIMUM_Y_COORDINATE = 0;
	private final int MINIMUM_TEXT_WIDTH_USED_FOR_RENDER = 30;
	private final int NUM_OF_SUB_INTERVALS = 2;
	
	private final static Logger fadedTextFieldLogger_ = Logger.getLogger(FadedTextField.class.getName());
	
	private final String CHARACTERS_TO_APPEND_TO_TRUNCATED_STRING = "...";
	private final String STRING_ONE_USED_FOR_TEXT_RENDERING_CALCULATION = "ZZZZ";
	private final String STRING_TWO_USED_FOR_TEXT_RENDERING_CALCULATION = "ZZZ";
	
	/**
     * Constructs a text field component that truncates very long strings and applies a fade out effect to a few characters towards the
     * end of such strings. If the text fits within the width of the FadedTextField, the string will be displayed normally.
     *
     * @param fromColor             The colour for the portion of the text that will be shown
     * @param toColor               The colour that the text will faded into in the event that the text string exceed the width of the 
     *                              text field component
     * @param backgroundColor       The colour of the background that the text is rendered on
     * @param maxCharacters         The maximum number of characters that can be typed into this text field component
     * @param numCharactersBefore   The number of characters to be rendered in fromColor before applying the faded out effect on the remaining
     *                              characters
     */
	public FadedTextField( Color fromColor, Color toColor, Color backgroundColor, int maxCharacters, int numCharactersBefore ){
		
	    fadedTextFieldLogger_.setLevel(java.util.logging.Level.SEVERE);
	    
		fromColor_ = fromColor != null ? fromColor : Color.BLACK;
		toColor_ = toColor != null ? toColor : DEFAULT_TO_COLOUR;
		backgroundColor_ = backgroundColor != null ? backgroundColor : Color.BLACK;
		
		if( maxCharacters <= MIN_NUM_OF_CHARACTERS || maxCharacters > MAX_NUM_OF_CHARACTERS ){
		    maxCharacters_ = DEFAULT_MAX_NUM_OF_CHARACTERS;
		} else{
		    maxCharacters_ = maxCharacters;
		}
		
		if( numCharactersBefore <= MIN_NUM_OF_CHARACTERS || numCharactersBefore > MAX_NUM_OF_NONFADED_CHARACTERS ){
		    numCharactersBeforeFade_ = DEFAULT_MAX_NUM_OF_NONFADED_CHARACTERS;
		} else{
		    numCharactersBeforeFade_ = numCharactersBefore;
		}
		componentCoordinates_ = getInsets();
		setBorder(null);
		setHighlighter(null);
		setEditable(false);
		setOpaque( false );
	}
	
	/**
     * Calculates the maximum string length required to fit within the width of the text field component
     *
     * @param str           The string being queried for its maximum string length required to fit within the width of 
     *                      this text field component
     * @param fontMetrics   The font attributes currently being used by the text displayed within this text field component
     * @return The maximum string length required to fit within the width of the text field component given the string and font attributes
     */
	private int locateSuitableMaxStringLength( String str, FontMetrics fontMetrics ){
	    
	    if( str == null || fontMetrics == null ){
	        return MINIMUM_STRING_LENGTH;
	    }
	    
	    String currString = str;
	    int currStringWidth = fontMetrics.stringWidth(currString);
	    int fieldWidth = getWidth();
	    
	    if( currStringWidth <= fieldWidth ){
	        return maxCharacters_;
	    }
	    
	    int startIdx = MINIMUM_START_INDEX_OF_STRING;
	    int endIdx = str.length()-LENGTH_OF_ONE_CHARACTER;
	    int midIdx;
	    fieldWidth -= fontMetrics.stringWidth(CHARACTERS_TO_APPEND_TO_TRUNCATED_STRING);
	    while( startIdx <= endIdx ){
	        
	        midIdx = (startIdx + endIdx)/NUM_OF_SUB_INTERVALS;
	        currString = str.substring(MINIMUM_START_INDEX_OF_STRING, midIdx+LENGTH_OF_ONE_CHARACTER);
	        currStringWidth = fontMetrics.stringWidth(currString);
	        
	        if( currStringWidth == fieldWidth ){
	            return currString.length();
	        } else if( currStringWidth < fieldWidth ){
	            startIdx = midIdx + LENGTH_OF_ONE_CHARACTER;
	        } else{
	            endIdx = midIdx - LENGTH_OF_ONE_CHARACTER;
	        }
	    }
	    return endIdx + LENGTH_OF_ONE_CHARACTER;
	}
	
	/**
     * Renders the appearance of the text field component and text within it;
     *
     * @param graphics  Component that provide access to rendering information and the rendering of the text field component
     */
	@Override
	public void paintComponent( Graphics graphics ) {
		
		setForeground( backgroundColor_ );
		super.paintComponent(graphics);
		getInsets( componentCoordinates_ );
		String inputString = getText();
		int stringLength = inputString.length();
		try{
		    FontMetrics fontMetrics = graphics.getFontMetrics();
		    int tempMaxCharacters = Math.min( maxCharacters_, locateSuitableMaxStringLength( inputString, fontMetrics ) );
		    int tempNumCharactersBeforeFade = Math.max( MINIMUM_STRING_LENGTH, numCharactersBeforeFade_-(maxCharacters_-tempMaxCharacters) );
			int offsetForText = viewToModel( new Point( componentCoordinates_.left, componentCoordinates_.top ) );
			inputString = inputString.substring(offsetForText, Math.min(stringLength, tempMaxCharacters));
			
			if( stringLength > tempMaxCharacters ){
				
				inputString += CHARACTERS_TO_APPEND_TO_TRUNCATED_STRING;
				stringLength = inputString.length();
				tempMaxCharacters += NUM_OF_CHARACTERS_TO_APPEND_TO_TRUNCATED_STRING;
			}
			int textWidth = fontMetrics.stringWidth(STRING_ONE_USED_FOR_TEXT_RENDERING_CALCULATION) - 
			                fontMetrics.stringWidth(STRING_TWO_USED_FOR_TEXT_RENDERING_CALCULATION) + MINIMUM_TEXT_WIDTH_USED_FOR_RENDER ;
			int offsetToPaint = componentCoordinates_.left;
			int heightOfText = fontMetrics.getAscent() + componentCoordinates_.top;
			
			GradientPaint paintForText = new GradientPaint( offsetToPaint, MINIMUM_Y_COORDINATE, 
			                                                toColor_, offsetToPaint + textWidth, MINIMUM_Y_COORDINATE, fromColor_, true );
			Graphics2D textGraphics = (Graphics2D) graphics;
			String nStr = inputString.substring( offsetToPaint, Math.min( stringLength, tempNumCharactersBeforeFade ) );
			textGraphics.setPaint( fromColor_ );
			textGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			textGraphics.drawString( nStr, offsetToPaint, heightOfText );
			
			if( stringLength > tempNumCharactersBeforeFade ){
				String sStr = inputString.substring( offsetForText + tempNumCharactersBeforeFade, Math.min(tempMaxCharacters, stringLength) );
				textGraphics.setPaint(paintForText);
				textGraphics.drawString( sStr, offsetToPaint+fontMetrics.stringWidth(nStr), heightOfText );
			}
			textGraphics.dispose();
		} catch( StringIndexOutOfBoundsException stringIndexOutOfBoundsException ){
		    fadedTextFieldLogger_.severe(stringIndexOutOfBoundsException.getMessage());
		}
	}
}