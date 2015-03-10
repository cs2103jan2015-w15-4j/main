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
	
	@Override
	public void paintComponent( Graphics graphics ) {
		
		setForeground( m_BackgroundColor );
		super.paintComponent(graphics);
		
		getInsets( componentCoordinates );
		
		String inputString = getText();
		
		int stringLength = inputString.length();
		
		try{
		
			int offsetForText = viewToModel( new Point( componentCoordinates.left, componentCoordinates.top ) );
			inputString = inputString.substring(offsetForText, Math.min(stringLength, m_MaxCharacters));
			
			int tempMaxCharacters = m_MaxCharacters;
			
			if( stringLength > m_MaxCharacters ){
				
				inputString += "...";
				stringLength = inputString.length();
				tempMaxCharacters += 3;
			}
			
			FontMetrics fontMetrics = graphics.getFontMetrics();
			
			// Values obtained in this line is via testing
			int textWidth = fontMetrics.stringWidth(inputString)+30;
			
			int offsetToPaint = componentCoordinates.left;
			int heightOfText = fontMetrics.getAscent() + componentCoordinates.top;
			
			GradientPaint paintForText = new GradientPaint( offsetToPaint, 0, m_FromColor, offsetToPaint + textWidth, 0, m_ToColor );
			
			Graphics2D textGraphics = (Graphics2D) graphics;
		
			String nStr = inputString.substring( offsetToPaint, Math.min( stringLength, m_NumCharactersBeforeFade ) );
			
			textGraphics.setPaint( m_FromColor );
			
			textGraphics.drawString( nStr, offsetToPaint, heightOfText );
			
			if( stringLength > m_NumCharactersBeforeFade ){
				
				String sStr = inputString.substring( offsetForText + m_NumCharactersBeforeFade, Math.min(tempMaxCharacters, stringLength) );
				
				textGraphics.setPaint(paintForText);
				
				textGraphics.drawString( sStr, offsetToPaint+fontMetrics.stringWidth(nStr), heightOfText );
			}
			
		} catch( StringIndexOutOfBoundsException stringIndexOutOfBoundsException ){}
	}
}



// Revision Control

/*
@Override
public void append( String text ){
	
	Document document = getDocument();
	
	if( document != null ){
		
		try{
			
			document.insertString( document.getLength(), text, null );
			
		} catch( BadLocationException badLocationException ){
		}
	}
}

public void append( String text, Color colour ){
	
	Document document = getDocument();
	
	if( document != null && colour != null ){
		
		try{
			
			StyleContext cont = StyleContext.getDefaultStyleContext();
			
			AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, colour);
			
			document.insertString( document.getLength(), text, attr );
			
		} catch( BadLocationException badLocationException ){
		}
	}
}

public void setFadedText( String str ){
	
	if( str != null ){
		
		str.replace( "\r", "" );
		str.replace( "\n", "" );
		
		int finalTextLength = str.length();
		int tempMaxCharacters = m_MaxCharacters;
		
		String finalText = str.substring( 0, Math.min(tempMaxCharacters, finalTextLength) );
		
		if( finalTextLength > tempMaxCharacters ){
			
			finalText = finalText.concat("...");
			tempMaxCharacters += 3;
			finalTextLength = finalText.length();
		}
		
		String nStr = finalText.substring(0, Math.min( m_NumCharactersBeforeFade, finalTextLength ));
		
		setText(nStr);
		
		if( finalTextLength > m_NumCharactersBeforeFade ){
			
			int alpha = 30;
			int decrement = 25/(finalTextLength-m_NumCharactersBeforeFade);
			decrement = decrement <= 0 ? 1 : decrement;
			Color currColor = getForeground();
			
			for( int i = m_NumCharactersBeforeFade; i < finalTextLength; ++i ){
				
				append(finalText.charAt(i) + "", new Color( currColor.getRed(), currColor.getGreen(), currColor.getBlue(), alpha ));
				
				alpha = Math.max( alpha - decrement, 5 );
			}
		}
		
		this.setCaretPosition(0);
	}
}*/

/*
public void setFadedText( String str, int idxToStartFading, int charLimitNum ){
	
	int strLength = 0;
	
	if( str != null && (strLength = str.length()) > 0 &&
	    idxToStartFading >= 0 && 
	    charLimitNum <= strLength &&
	    charLimitNum >= 0 ){
		
		String finalString;
		
		if( charLimitNum < strLength ){
			
			finalString = charLimitNum > 0 ? str.substring( 0, charLimitNum ) : "";
			
		} else{
			
			finalString = str;
		}
		
		strLength = finalString.length();
		
		if( idxToStartFading - 1 >= 0 ){
			
			idxToStartFading = Math.min( idxToStartFading, strLength );
			
			String nText = finalString.substring(0, idxToStartFading);
			
			setText( nText );
			
			System.out.println( "Caret Pos: " + getCaretPosition() );
		}
		
		if( idxToStartFading < strLength ){
			
			int alpha = 50;
			//int numCharToPrint = strLength - idxToStartFading;
			int decrements = 10;
			
			Color currColour = getForeground();
			
			for( int i = idxToStartFading; i < strLength; ++i ){
				
				setCaretPosition(i);
				
				this.set
				
				setForeground( new Color( currColour.getRed(), currColour.getGreen(), currColour.getBlue(), alpha ) );
				
				setText( finalString.charAt(i) + "" );
				
				alpha = Math.max( alpha - decrements, 10 );
			}
			
			setForeground( new Color( currColour.getRed(), currColour.getGreen(), currColour.getBlue(), currColour.getAlpha() ) );
		}
	}
}
*/
