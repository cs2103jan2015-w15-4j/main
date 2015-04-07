package planner;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class NavigationBar extends JComponent{

    private JLabel background;
    
    private JTextPane infoPanel;
    
    private final int width = 253;
    private final int height = 47;
    
    private Style boldText;
    private Style smallerText;
    
    private boolean m_isVisible;
    
    public NavigationBar(){
        
        this( null, null );
    }
    
    public NavigationBar( String message, String navigationKey ){
        
        prepareInfoPanel();
        prepareBackground();
        
        boldText = infoPanel.addStyle( "BigBoldText", null );
        StyleConstants.setBold(boldText, true);
        StyleConstants.setFontSize(boldText, 13);
        StyleConstants.setFontFamily(boldText, "Arial");
        StyleConstants.setForeground(boldText, new Color( 255,255,255 ));
        
        smallerText = infoPanel.addStyle( "SmallerText", null );
        StyleConstants.setBold(smallerText, true);
        StyleConstants.setItalic(smallerText, true);
        StyleConstants.setFontSize(smallerText, 12);
        StyleConstants.setFontFamily(smallerText, "Calibri");
        StyleConstants.setForeground(smallerText, new Color( 255,255,255 ));
        
        setMessageToView( message, navigationKey );
    }
    
    public NavigationBar( String message ){
        
        prepareInfoPanel();
        prepareBackground();
        
        boldText = infoPanel.addStyle( "BigBoldText", null );
        StyleConstants.setBold(boldText, true);
        StyleConstants.setFontSize(boldText, 13);
        StyleConstants.setFontFamily(boldText, "Arial");
        StyleConstants.setForeground(boldText, new Color( 255,255,255 ));
        
        smallerText = infoPanel.addStyle( "SmallerText", null );
        StyleConstants.setBold(smallerText, true);
        StyleConstants.setItalic(smallerText, true);
        StyleConstants.setFontSize(smallerText, 12);
        StyleConstants.setFontFamily(smallerText, "Calibri");
        StyleConstants.setForeground(smallerText, new Color( 255,255,255 ));
        
        setMessageToCurrentView(message);
    }
    
    @Override
    public Dimension getPreferredSize(){
        
        return new Dimension( width, height );
    }
    
    private void prepareInfoPanel(){
        
        infoPanel = new JTextPane();
        infoPanel.setBounds(10, 7, 180, 38);
        infoPanel.setOpaque(false);
        infoPanel.setFocusable(false);
        infoPanel.setEditable(false);
        infoPanel.setHighlighter(null);
        infoPanel.setBorder(null);
        add(infoPanel);
    }
    
    public void setMessageToCurrentView( String message ){
        
        StyledDocument doc = infoPanel.getStyledDocument();
        
        try{
            
            m_isVisible = true;
            
            infoPanel.setText("");
            
            if(message == null || message.length() <= 0 ){
                
                message = "<Insert navigation screen type here>";
                
                m_isVisible = false;
            }
            
            doc.insertString(doc.getLength(), message, boldText);
            
            doc.insertString(doc.getLength(), "\nCurrently viewing", smallerText );
            
            SimpleAttributeSet centerStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLineSpacing(centerStyle, -0.2f);
            doc.setParagraphAttributes(0, doc.getLength(), centerStyle, true);
            
        } catch( BadLocationException badLocationException ){}
    }
    
    public void setMessageToView( String message, String navigationKey ){
        
        StyledDocument doc = infoPanel.getStyledDocument();
        
        try{
            
            m_isVisible = true;
            
            infoPanel.setText("");
            
            if(message == null || message.length() <= 0 ){
                
                message = "<Insert navigation screen type here>";
                
                m_isVisible = false;
            }
            
            doc.insertString(doc.getLength(), message, boldText);
            
            if( navigationKey == null || navigationKey.length() <= 0 ){
                
                navigationKey = "<Insert key here>";
                
                m_isVisible = false;
            }
            
            doc.insertString(doc.getLength(), "\nPress " + navigationKey + " to view", smallerText );
            
            SimpleAttributeSet centerStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLineSpacing(centerStyle, -0.2f);
            doc.setParagraphAttributes(0, doc.getLength(), centerStyle, true);
            
        } catch( BadLocationException badLocationException ){}
    }
    
    private void prepareBackground(){
        
        background = new JLabel();
        background.setIcon(new ImageIcon(NavigationBar.class.getResource("/planner/NavigationBar.png")));
        background.setBounds(0, 0, width, height);
        add(background);
    }
    
    public boolean isVisible(){
        
        return m_isVisible;
    }
}
