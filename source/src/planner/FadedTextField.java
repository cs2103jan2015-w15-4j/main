package planner;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

// Might have to implement actionlisters
public class FadedTextField extends JTextField{

	private Color m_FromColor;
	private Color m_ToColor;
	private Color m_BackgroundColor;
	
	private Insets componentCoordinates;
	
	private int m_MaxCharacters;
	private int m_NumCharactersBeforeFade;
	
	public FadedTextField( Color fromColor, Color toColor, Color backgroundColor, int maxCharacters, int numCharactersBefore ){
		
		m_FromColor = fromColor != null ? fromColor : new Color( 0,0,0 );
		m_ToColor = toColor != null ? toColor : new Color( 192,192,192 );
		m_BackgroundColor = backgroundColor != null ? backgroundColor : new Color( 0,0,0 );
		
		m_MaxCharacters = (maxCharacters <= 0 || maxCharacters > 1000) ? 15 : maxCharacters;
		m_NumCharactersBeforeFade = (numCharactersBefore <= 0 || numCharactersBefore > 999) ? 9 : numCharactersBefore;
		
		componentCoordinates = getInsets();
		
		setBorder(null);
		setHighlighter(null);
		setEditable(false);
		
		setOpaque( false );
	}
	
	private int locateSuitableMaxStringLength( String str, FontMetrics fontMetrics ){
	    
	    if( str == null || fontMetrics == null ){
	        
	        return 0;
	    }
	    
	    String currString = str;
	    
	    int currStringWidth = fontMetrics.stringWidth(currString);
	    int fieldWidth = getWidth();
	    
	    if( currStringWidth <= fieldWidth ){
	        
	        return m_MaxCharacters;
	    }
	    
	    int startIdx = 0;
	    int endIdx = str.length()-1;
	    int midIdx;
	    fieldWidth -= fontMetrics.stringWidth("...");
	    
	    while( startIdx <= endIdx ){
	        
	        midIdx = (startIdx + endIdx)/2;
	        
	        currString = str.substring(0, midIdx+1);
	        
	        currStringWidth = fontMetrics.stringWidth(currString);
	        
	        if( currStringWidth == fieldWidth ){
	            
	            return currString.length();
	            
	        } else if( currStringWidth < fieldWidth ){
	            
	            startIdx = midIdx + 1;
	            
	        } else{
	            
	            endIdx = midIdx - 1;
	        }
	    }
	    
	    return endIdx + 1;
	}
	
	@Override
	public void paintComponent( Graphics graphics ) {
		
		setForeground( m_BackgroundColor );
		super.paintComponent(graphics);
		
		getInsets( componentCoordinates );
		
		String inputString = getText();
		
		int stringLength = inputString.length();
		
		try{
		
		    FontMetrics fontMetrics = graphics.getFontMetrics();
		    
		    int tempMaxCharacters = Math.min( m_MaxCharacters, locateSuitableMaxStringLength( inputString, fontMetrics ) );
		    int tempNumCharactersBeforeFade = Math.max( 0, m_NumCharactersBeforeFade-(m_MaxCharacters-tempMaxCharacters) );
		    
			int offsetForText = viewToModel( new Point( componentCoordinates.left, componentCoordinates.top ) );
			inputString = inputString.substring(offsetForText, Math.min(stringLength, tempMaxCharacters));
			
			if( stringLength > tempMaxCharacters ){
				
				inputString += "...";
				stringLength = inputString.length();
				tempMaxCharacters += 3;
			}
			
			// Values obtained in this line is via testing
			//int textWidth = fontMetrics.stringWidth(inputString)+30;
			int textWidth = fontMetrics.stringWidth("ZZZZ") - fontMetrics.stringWidth("ZZZ") + 30 ;
			
			int offsetToPaint = componentCoordinates.left;
			int heightOfText = fontMetrics.getAscent() + componentCoordinates.top;
			
			//GradientPaint paintForText = new GradientPaint( offsetToPaint, 0, m_FromColor, offsetToPaint + textWidth, 0, m_ToColor, true );
			GradientPaint paintForText = new GradientPaint( offsetToPaint, 0, m_ToColor, offsetToPaint + textWidth, 0, m_FromColor, true );
			
			Graphics2D textGraphics = (Graphics2D) graphics;
		
			String nStr = inputString.substring( offsetToPaint, Math.min( stringLength, tempNumCharactersBeforeFade ) );
			
			textGraphics.setPaint( m_FromColor );
			
			textGraphics.drawString( nStr, offsetToPaint, heightOfText );
			
			if( stringLength > tempNumCharactersBeforeFade ){
				
				String sStr = inputString.substring( offsetForText + tempNumCharactersBeforeFade, Math.min(tempMaxCharacters, stringLength) );
				
				textGraphics.setPaint(paintForText);
				
				textGraphics.drawString( sStr, offsetToPaint+fontMetrics.stringWidth(nStr), heightOfText );
			}
			
		} catch( StringIndexOutOfBoundsException stringIndexOutOfBoundsException ){}
	}
}