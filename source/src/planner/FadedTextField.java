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