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
        
        graphics.fillRoundRect(xCoordinate, yCoordinate, componentWidth, componentHeight, 10, 10);
        
        super.paintComponent(graphics);
    }
    
    public void adjustComponentSizeToFitText(){
        
        int newHeightOfComponent = (getPreferredSize() != null ? getPreferredSize().height : getHeight());
        
        super.setSize(getWidth(), newHeightOfComponent);
    }
    
    @Override
    public void setText( String text ){
        
        setSize(getWidth(), Short.MAX_VALUE);
        
        super.setText(text);
        
        adjustComponentSizeToFitText();
    }
    
    public void initialiseForResize(){
        
        setSize(getWidth(), Short.MAX_VALUE);
    }
    
    public void appendText( String text, AttributeSet style ){
        
        try {
        
            if( text != null ){
                
                StyledDocument doc = getStyledDocument();
                
                doc.insertString(doc.getLength(), text, style);
                
                doc.setCharacterAttributes(doc.getLength(), doc.getLength()+1, style, false);
            }
            
        } catch (BadLocationException e) {}
    }
    
    public void highlightWords( String []list, SimpleAttributeSet style ){
        
         try {
             
            if( list != null && list.length > 0 ){
                
                StyledDocument doc = getStyledDocument();
                
                if( doc != null && doc.getLength() > 0 ){
                
                    String regex = CommandPanelDocumentFilter.generateRegex(list);
                    
                    Pattern pattern = Pattern.compile(regex);
                    
                    String currentString = doc.getText(0, doc.getLength()); 
                    
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
