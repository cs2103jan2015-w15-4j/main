//@author A0111333B

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

/**
* The NavigationBar class is a component that stores and displays navigation information for YOPO
*
* @author A0111333B
*/
public class NavigationBar extends JComponent{

    private JLabel background_;
    
    private JTextPane infoPanel_;
    
    private Style boldText_;
    private Style smallerText_;
    
    private boolean isVisible_;
    
    private final int DEFAULT_WIDTH = 253;
    private final int DEFAULT_HEIGHT = 47;
    private final int MINIMUM_STRING_LENGTH = 0;
    private final int MINIMUM_STRING_INDEX = 0;
    private final int FONT_SIZE_FOR_BOLDTEXTSTYLE = 13;
    private final int FONT_SIZE_FOR_SMALLTEXTSTYLE = 12;
    private final int MINIMUM_RELATIVE_XCOORDINATE = 0;
    private final int MIMIMUM_RELATIVE_YCOORDINATE = 0;
    private final int DEFAULT_RELATIVE_XCOORDINATE_OF_INFOPANEL = 10;
    private final int DEFAULT_RELATIVE_YCOORDINATE_OF_INFOPANEL = 7;
    private final int DEFAULT_WIDTH_OF_INFOPANEL = 180;
    private final int DEFAULT_HEIGHT_OF_INFOPANEL = 38;
    
    private final String NAME_FOR_BOLDTEXTSTYLE = "BigBoldText";
    private final String FONT_FAMILY_FOR_BOLDTEXTSTYLE = "Arial";
    private final String NAME_FOR_SMALLTEXTSTYLE = "SmallerText";
    private final String FONT_FAMILY_FOR_SMALLTEXTSTYLE = "Calibri";
    private final String EMPTY_STRING = "";
    private final String DEFAULT_NAVIGATION_MESSAGE = "<Insert navigation screen type here>";
    private final String DEFAULT_NAVIGATION_STATUS = "\nCurrently viewing";
    private final String DEFAULT_NAVIGATION_KEY = "<Insert key here>";
    private final String NAVIGATION_KEY_MSG_I = "\nPress ";
    private final String NAVIGATION_KEY_MSG_II = " to view";
    private final String NAVIGATION_BAR_IMAGE_RESOURCE_LINK = "/planner/NavigationBar.png";
    
    private final float DEFAULT_LINE_SPACING = -0.2f;
    
    /**
     * Constructs a NavigationBar component that displays its default navigation messages
     */
    public NavigationBar(){
        this( null, null );
    }
    
    /**
     * Constructs a NavigationBar component that displays the provided navigation information in message and
     * navigation key involved for this navigation
     * 
     * @param message       The navigation information to be displayed
     * @param navigationKey The navigation key involved for this navigation
     */
    public NavigationBar( String message, String navigationKey ){
        prepareInfoPanel();
        prepareBackground();
        intialiseTextStyles();
        setMessageToView( message, navigationKey );
    }
    
    /**
     * Constructs a NavigationBar component that displays the provided navigation information in message and
     * will be set to display a "currently viewing message"
     * 
     * @param message       The navigation information to be displayed
     */
    public NavigationBar( String message ){
        prepareInfoPanel();
        prepareBackground();
        intialiseTextStyles();
        setMessageToCurrentView(message);
    }
    
    /**
     * Returns the preferred width and height of the component
     * 
     * @return A dimension container that stores the preferred width and height of the component
     */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );
    }
    
    /**
     * Initializes the text styles required by the text shown within the text component in the NavigationBar component
     */
    private void intialiseTextStyles(){
        boldText_ = infoPanel_.addStyle( NAME_FOR_BOLDTEXTSTYLE, null );
        StyleConstants.setBold(boldText_, true);
        StyleConstants.setFontSize(boldText_, FONT_SIZE_FOR_BOLDTEXTSTYLE);
        StyleConstants.setFontFamily(boldText_, FONT_FAMILY_FOR_BOLDTEXTSTYLE);
        StyleConstants.setForeground(boldText_, Color.WHITE);
        smallerText_ = infoPanel_.addStyle( NAME_FOR_SMALLTEXTSTYLE, null );
        StyleConstants.setBold(smallerText_, true);
        StyleConstants.setItalic(smallerText_, true);
        StyleConstants.setFontSize(smallerText_, FONT_SIZE_FOR_SMALLTEXTSTYLE);
        StyleConstants.setFontFamily(smallerText_, FONT_FAMILY_FOR_SMALLTEXTSTYLE);
        StyleConstants.setForeground(smallerText_, Color.WHITE);
    }
 
    /**
     * Initializes and sets attributes for the text component that will display the messages
     */
    private void prepareInfoPanel(){
        infoPanel_ = new JTextPane();
        infoPanel_.setBounds(DEFAULT_RELATIVE_XCOORDINATE_OF_INFOPANEL, 
                            DEFAULT_RELATIVE_YCOORDINATE_OF_INFOPANEL, 
                            DEFAULT_WIDTH_OF_INFOPANEL, 
                            DEFAULT_HEIGHT_OF_INFOPANEL);
        infoPanel_.setOpaque(false);
        infoPanel_.setFocusable(false);
        infoPanel_.setEditable(false);
        infoPanel_.setHighlighter(null);
        infoPanel_.setBorder(null);
        add(infoPanel_);
    }
    
    /**
     * Sets the navigation information to be displayed in the text component within the NavigationBar component
     * 
     * @param message   The navigation information to be displayed in the text component within the NavigationBar component
     */
    public void setMessageToCurrentView( String message ){
        
        StyledDocument doc = infoPanel_.getStyledDocument();
        try{
            isVisible_ = true;
            infoPanel_.setText(EMPTY_STRING);
            
            if(message == null || message.length() <= MINIMUM_STRING_LENGTH ){
                message = DEFAULT_NAVIGATION_MESSAGE;
                isVisible_ = false;
            }
            
            doc.insertString(doc.getLength(), message, boldText_);
            doc.insertString(doc.getLength(), DEFAULT_NAVIGATION_STATUS, smallerText_ );
            
            SimpleAttributeSet centerStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLineSpacing(centerStyle, DEFAULT_LINE_SPACING);
            doc.setParagraphAttributes(MINIMUM_STRING_INDEX, doc.getLength(), centerStyle, true);
        } catch( BadLocationException badLocationException ){}
    }
    
    /**
     * Sets the provided navigation information in message and navigation key involved for this navigation
     * 
     * @param message       The navigation information to be displayed
     * @param navigationKey The navigation key involved for this navigation
     */
    public void setMessageToView( String message, String navigationKey ){
        
        StyledDocument doc = infoPanel_.getStyledDocument();
        try{
            isVisible_ = true;
            infoPanel_.setText(EMPTY_STRING);
            
            if(message == null || message.length() <= MINIMUM_STRING_LENGTH ){
                message = DEFAULT_NAVIGATION_MESSAGE;
                isVisible_ = false;
            }
            doc.insertString(doc.getLength(), message, boldText_);
            if( navigationKey == null || navigationKey.length() <= MINIMUM_STRING_LENGTH ){
                
                navigationKey = DEFAULT_NAVIGATION_KEY;
                
                isVisible_ = false;
            }
            doc.insertString(doc.getLength(), NAVIGATION_KEY_MSG_I + navigationKey + NAVIGATION_KEY_MSG_II, smallerText_ );
            SimpleAttributeSet centerStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_LEFT);
            StyleConstants.setLineSpacing(centerStyle, DEFAULT_LINE_SPACING);
            doc.setParagraphAttributes(MINIMUM_STRING_INDEX, doc.getLength(), centerStyle, true);
        } catch( BadLocationException badLocationException ){}
    }
    
    /**
     * Initializes and sets attributes for background component of this NavigationBar component
     */
    private void prepareBackground(){
        background_ = new JLabel();
        background_.setIcon(new ImageIcon(NavigationBar.class.getResource(NAVIGATION_BAR_IMAGE_RESOURCE_LINK)));
        background_.setBounds(MINIMUM_RELATIVE_XCOORDINATE, MIMIMUM_RELATIVE_YCOORDINATE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        add(background_);
    }
    
    /**
     * Returns true if navigation bar is shown; false otherwise
     * 
     * @return True if navigation bar is shown; false otherwise
     */
    public boolean isVisible(){
        return isVisible_;
    }
}
