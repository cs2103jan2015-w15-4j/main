//@author A0111333B

package planner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
* The CommandTextbox class is YOPO's command text field component in which users enter their commands. This command text field also has
* a pop up box embedded within it. The pop up box will show the relevant commands depending on the text typed within the command 
* text field
*
* @author A0111333B
*/
public class CommandTextbox extends JScrollPane{

    private JTextPane inputCommandBox;
    
    private JComboBox<String> popUpBox;
    
    private DefaultComboBoxModel<String> popUpList;
    
    private String []m_commandKeywords;
    private String []m_nonCommandKeywords;
    
    private Style originalTextStyle;
    
    private ArrayList<String> possibleCommands;
    
    private int currentPopupListIndex;
    
    private CustomTextFieldDocumentFilter commandPanelDocumentFilter;
    
    private boolean isCurrentlyHandlingKeyEvent;
    
    private final int SELF_DEFINED_MINIMUM_IDX_OF_POPUP_LIST = -2;
    private final int MINIMUM_IDX_OF_POPUP_LIST = -1;
    private final int FIRST_ITEM_IDX_IN_POPUP_LIST = 0;
    private final int SIZE_OF_ONE_ITEM_IN_POPUP_LIST = 1;
    private final int SIZE_OF_EMPTY_POPUP_LIST = 0;
    private final int MINIMUM_POPUP_BOX_HEIGHT = 0;
    private final int DEFAULT_POPUP_BOX_STRING_FONT_SIZE = 14;
    private final int DEFAULT_TEXTPANE_STRING_FONT_SIZE = 20;
    private final int MINIMUM_POPUP_BOX_RELATIVE_YCOORDINATE = 0;
    private final int DEFAULT_POPUP_BOX_RELATIVE_YCOORDINATE = 30;
    private final int NUM_WORDS_TO_SPLIT_INTO = 2;
    private final int MIMIMUM_STRING_LENGTH = 0;
    private final int FIRST_WORD_INDEX = 0;
    
    private final String DEFAULT_DELIMITER = " ";
    private final String DEFAULT_POPUP_BOX_STRING_FONT_FAMILY = "Arial";
    private final String DEFAULT_TEXTPANE_STRING_FONT_FAMILY = "Arial";
    private final String DEFAULT_TEXT_STYLE_NAME = "original";
    
    private final Color DEFAULT_TEXTPANE_FONT_COLOR = new Color(128,128,128);
    
    private final int DEFAULT_NUMBER_OF_PIXELS_TO_SHOW_BEYOND_CARET = 2;
    /**
     * Constructs a command text field component with a pop up box embedded within it. The pop up box will show
     * the relevant commands based on the list of command Keywords passed in. Any keyword found within commandKeywords or
     * nonCommandKeywords will be syntax colored when entered in this command text field component
     *
     * @param commandKeywords       The list of command keywords that will be used by YOPO to perform operations
     * @param nonCommandKeywords    The list of non-command keywords that will be used by YOPO to customize the behavior of an operations
     * @param listOfCommands        Several lists of example commands that can be entered into YOPO to perform operations categorized by
     *                              the command keyword used
     */
    public CommandTextbox( String []commandKeywords, String []nonCommandKeywords, 
                           ArrayList<Map.Entry<String, ArrayList<String>>> listOfCommands ){
        
        m_commandKeywords = commandKeywords;
        m_nonCommandKeywords = nonCommandKeywords;
        inputCommandBox = new JTextPane();
        prepareTextPane(inputCommandBox);

        setFocusable(false);
        setBorder(null);
        setOpaque(false);
        
        setViewportView(inputCommandBox);
        if( inputCommandBox != null ){
            getViewport().setOpaque(false);
        }
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        
        if( horizontalScrollBar != null ){
            horizontalScrollBar.setUI(new InvisibleScrollBarUI());
            horizontalScrollBar.setFocusable(false);
            horizontalScrollBar.setPreferredSize(new Dimension(0,0));
        }
        prepareComboBox( inputCommandBox, listOfCommands );
        isCurrentlyHandlingKeyEvent = false;
    }
    
    // TODO Documentation
    public boolean handleKeyEvent( KeyEvent keyEvent ){
        
        if( !isCurrentlyHandlingKeyEvent && keyEvent != null && popUpBox != null && popUpList != null ){
            
            if( popUpBox != null && popUpBox.isPopupVisible() && inputCommandBox.isFocusOwner() ){
                
                isCurrentlyHandlingKeyEvent = true;
                
                restrictPopupBoxDimensions( inputCommandBox, popUpBox, DEFAULT_POPUP_BOX_RELATIVE_YCOORDINATE );
                
                keyEvent.setSource( popUpBox );
                popUpBox.dispatchEvent(keyEvent);
                
                if( keyEvent.getKeyCode() == KeyEvent.VK_UP ){
                    
                    if( !keyEvent.isShiftDown() && !keyEvent.isControlDown() ){
                        
                        currentPopupListIndex = Math.max( currentPopupListIndex - 1, -2 );
                        popUpBox.setSelectedIndex(getPopupListIdx(currentPopupListIndex, popUpList.getSize() ));
                        currentPopupListIndex = (currentPopupListIndex > -2 ? currentPopupListIndex : popUpList.getSize() - 1);
                        
                        if( popUpBox.getSelectedIndex() < 0 ){
                            popUpBox.hidePopup();
                            popUpBox.setPopupVisible(true);
                        }
                        
                    } else{
                        
                        isCurrentlyHandlingKeyEvent = false;
                        return false;
                    }
                    
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_DOWN ){
                    
                    if( !keyEvent.isShiftDown() && !keyEvent.isControlDown() ){
                        
                        currentPopupListIndex = Math.min( currentPopupListIndex + 1, popUpList.getSize() + 1 );
                        popUpBox.setSelectedIndex(getPopupListIdx(currentPopupListIndex , popUpList.getSize() ));
                        
                        currentPopupListIndex = ( currentPopupListIndex<= popUpList.getSize() ? currentPopupListIndex  : (popUpList.getSize() > 0 ? 0 : -1) );
                        
                        if( popUpBox.getSelectedIndex() < 0 ){
                            popUpBox.hidePopup();
                            popUpBox.setPopupVisible(true);
                        }
                        
                    } else{
                        
                        isCurrentlyHandlingKeyEvent = false;
                        return false;
                    }
                    
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_LEFT ){
                    
                    int pos = Math.max(inputCommandBox.getCaretPosition() - 1, 0 );
                    inputCommandBox.setCaretPosition(pos);
                    popUpBox.hidePopup();
                    popUpBox.setPopupVisible(true);
                    
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_RIGHT ){
                    
                    int pos = Math.min(inputCommandBox.getText().length(), inputCommandBox.getCaretPosition() + 1 );
                    inputCommandBox.setCaretPosition(pos);
                    popUpBox.hidePopup();
                    popUpBox.setPopupVisible(true);
                    
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_ENTER ){
                    
                    if( popUpBox.getSelectedItem() != null ){
                        
                        inputCommandBox.setText(popUpBox.getSelectedItem().toString());
                        popUpBox.hidePopup();
                        popUpBox.setPopupVisible(false);
                        inputCommandBox.setCaretPosition(inputCommandBox.getText().length());
                        
                    } else{
                        
                        popUpBox.hidePopup();
                        popUpBox.setPopupVisible(false);
                        isCurrentlyHandlingKeyEvent = false;
                        return false;
                    }
                }
                
                isCurrentlyHandlingKeyEvent = false;
                return true;
            }
        }
            
        return false;
    }
    
    /**
     * Returns a self defined index of the currently selected item in the pop-up box based on the current index of the item in the pop up box list
     * and the size of the pop up box list.
     *
     * @param idx              Current index of the item in the pop up box list
     * @param listSize         The size of the pop up box list.
     * @param listOfCommands   Several lists of example commands that can be entered into YOPO to perform operations categorized by
     *                         the command keyword used
     */
    private int getPopupListIdx( int idx, int listSize ){
        if( idx >= FIRST_ITEM_IDX_IN_POPUP_LIST && idx < listSize ){
            return idx;
        } else if( idx == SELF_DEFINED_MINIMUM_IDX_OF_POPUP_LIST ){
            return listSize-SIZE_OF_ONE_ITEM_IN_POPUP_LIST;
        } else if( idx == listSize + SIZE_OF_ONE_ITEM_IN_POPUP_LIST ){
            return (listSize > SIZE_OF_EMPTY_POPUP_LIST ? FIRST_ITEM_IDX_IN_POPUP_LIST : MINIMUM_IDX_OF_POPUP_LIST);
        } else{
            return MINIMUM_IDX_OF_POPUP_LIST;
        }
    }
    

    
    /**
     * Initializes and set the attributes of the popup box and its list.
     *
     * @param textPane         The text component that the pop up box will be embedded within
     * @param listOfCommands   Several lists of example commands that can be entered into YOPO to perform operations categorized by
     *                         the command keyword used     
     */
    private void prepareComboBox( final JTextPane textPane, 
                                  ArrayList<Map.Entry<String, ArrayList<String>>> listOfCommands){
        
        if( textPane != null && listOfCommands != null ){
            popUpList = new DefaultComboBoxModel<String>();
            popUpBox = new JComboBox<String>(popUpList){
                @Override
                public Dimension getPreferredSize(){
                    return new Dimension( textPane.getVisibleRect().width, MINIMUM_POPUP_BOX_HEIGHT );
                }
            };
           
            popUpBox.setOpaque(false);
            popUpBox.setFocusable(false);
            popUpBox.setFont(new Font(DEFAULT_POPUP_BOX_STRING_FONT_FAMILY, Font.PLAIN, DEFAULT_POPUP_BOX_STRING_FONT_SIZE));
            possibleCommands = new ArrayList<String>();
            
            ArrayList<String> tempStringList;
            for( Map.Entry<String, ArrayList<String>> tempEntry : listOfCommands ){
                tempStringList = tempEntry.getValue();
                if( tempStringList != null ){
                    for( String tempString : tempStringList ){
                        if( tempString != null ){
                            possibleCommands.add(tempString);
                        }
                    }
                }
            }
            currentPopupListIndex = -1;
            popUpBox.setSelectedIndex(currentPopupListIndex);
            bindDocumentListener( textPane, popUpBox, popUpList );
            textPane.setLayout(new BorderLayout());
            textPane.add( popUpBox, BorderLayout.SOUTH );
        }
    }
    
    /**
     * Turns syntax text coloring on within the text component
     */
    public void setSyntaxFilterOn(){
        commandPanelDocumentFilter.setFilterOn();
    }
    
    /**
     * Turns syntax text coloring off within the text component
     */
    public void setSyntaxFilterOff(){
        commandPanelDocumentFilter.setFilterOff();
    }
    
    /**
     * Hides the popup box used to display the relevant example commands from view  
     */
    public void hidePopupBox(){
        if( popUpBox != null ){
            popUpBox.removeAllItems();
            popUpBox.setPopupVisible(false);
            popUpBox.hidePopup();
        }
    }
    
    /**
     * Displays the popup box used to show the relevant example commands
     */
    public void showPopupBox(){
        if( popUpBox != null ){
            popUpBox.setPopupVisible(true);
            popUpBox.showPopup();
        }
    }
    
    /**
     * Sets up a listener for the text component to listen for text updates and update the list of relevant example commands to show within
     * the pop up box
     *
     * @param textPane         The text component that will display the text typed in
     * @param popupBox         The pop up box that will show the relevant example commands fetched from the popupBoxList
     * @param popupBoxList     The list that the pop up box will refer to for relevant example commands to display
     */
    private void bindDocumentListener( final JTextPane textPane, final JComboBox<String> popupBox, 
                                       final DefaultComboBoxModel<String> popupBoxList ){
        
        if( textPane != null && popupBox != null && popupBoxList != null ){
            Document doc = textPane.getDocument();
            if( doc != null ){
                doc.addDocumentListener(new DocumentListener(){
                    @Override
                    public void insertUpdate( DocumentEvent documentEvent ){
                        updatePopBoxList();
                    }
                    @Override
                    public void removeUpdate( DocumentEvent documentEvent ){
                        updatePopBoxList();
                    }
                    @Override
                    public void changedUpdate( DocumentEvent documentEvent ){
                        updatePopBoxList();
                    }
                    public void updatePopBoxList(){
                        if( textPane.isFocusOwner() ){
                            restrictPopupBoxDimensions( textPane, popupBox, DEFAULT_POPUP_BOX_RELATIVE_YCOORDINATE );
                            popupBoxList.removeAllElements();
                            String userInput = textPane.getText().trim();
                            if( !userInput.isEmpty() ){
                                String []userInputWords = userInput.split( DEFAULT_DELIMITER, NUM_WORDS_TO_SPLIT_INTO );
                                if( userInputWords.length > MIMIMUM_STRING_LENGTH ){
                                    String firstWordInUserInput = userInputWords[FIRST_WORD_INDEX].toLowerCase();
                                    String []wordsInCommand;
                                    for( String currentCommand : possibleCommands ){
                                        wordsInCommand = currentCommand.split( DEFAULT_DELIMITER, NUM_WORDS_TO_SPLIT_INTO );
                                        if( wordsInCommand.length > MIMIMUM_STRING_LENGTH && 
                                            firstWordInUserInput.startsWith(wordsInCommand[FIRST_WORD_INDEX]) ){
                                            popupBoxList.addElement(currentCommand);
                                        }
                                    }
                                }
                            }
                            currentPopupListIndex = MINIMUM_IDX_OF_POPUP_LIST;
                            popupBox.setSelectedIndex(currentPopupListIndex);
                            popupBox.hidePopup();
                            popupBox.setPopupVisible(popupBoxList.getSize() > SIZE_OF_EMPTY_POPUP_LIST);
                        }
                    }
                });
            }
        }
    }
    
    /**
     * Sets the size of the pop up box used to display the relevant example commands
     *
     * @param textPane         The text component that will display the text typed in
     * @param popupBox         The pop up box that will show the relevant example commands fetched from the popupBoxList
     * @param yCoordinate      The y-coordinate of the top left corner of the pop up box that will be displayed at relative to 
     *                         the text component
     */
    private void restrictPopupBoxDimensions( JTextPane textPane, JComboBox<String> popupBox, int yCoordinate ){
        Rectangle tempRectangle = textPane.getVisibleRect();
        if( tempRectangle != null && popupBox != null && yCoordinate >= MINIMUM_POPUP_BOX_RELATIVE_YCOORDINATE ){
            tempRectangle.height = MINIMUM_POPUP_BOX_HEIGHT;
            tempRectangle.y = yCoordinate;
            popupBox.setBounds(tempRectangle);
        }
    }
    
    /**
     * Sets the attributes of the text component
     *
     * @param textPane         The text component that will display the text typed in
     */
    private void prepareTextPane( JTextPane textPane ){
        if( textPane != null ){
            inputCommandBox.setEditorKit(new CustomNoWrapKit());
            inputCommandBox.addCaretListener(new CustomCaretListener(DEFAULT_NUMBER_OF_PIXELS_TO_SHOW_BEYOND_CARET));
            inputCommandBox.setOpaque(false);
            inputCommandBox.setFont( new Font( DEFAULT_TEXTPANE_STRING_FONT_FAMILY, Font.BOLD, DEFAULT_TEXTPANE_STRING_FONT_SIZE ) );
            inputCommandBox.setForeground(DEFAULT_TEXTPANE_FONT_COLOR);
            originalTextStyle = getStyleOfTextPane(textPane);
            AbstractDocument abstractDocument = (AbstractDocument)inputCommandBox.getDocument();
            commandPanelDocumentFilter = new CustomTextFieldDocumentFilter( m_commandKeywords, m_nonCommandKeywords, originalTextStyle);
            abstractDocument.setDocumentFilter(commandPanelDocumentFilter);
        }
    }
    
    /**
     * Sets text to be displayed within the text component. The keywords within the text will be syntax coloured if turnOffFilter
     * is false.
     *
     * @param textPane         The text component that will display the text typed in
     * @param turnOffFilter    The flag that determines if keywords within the text will be syntax coloured or not. If turnOffFilter
     *                         is false, keywords within the text will be syntax coloured. On the other hand, keywords within the text 
     *                         will not be syntax coloured if turnOffFilter is true.
     */
    public void setText( String text, boolean turnOffFilter ){
        if( text != null ){
            if( turnOffFilter ){
                commandPanelDocumentFilter.setFilterOff();
            }
            inputCommandBox.setText(text);
        }
    }
    
    /**
     * Sets the default colour of the text to be displayed within the text component.
     *
     * @param colour    The default colour of the text to be displayed within the text component.
     */
    public void setForegroundColor( Color colour ){
        if( colour != null ){
            inputCommandBox.setForeground(colour);
            Style tempStyle = getStyleOfTextPane(inputCommandBox);
            StyleConstants.setForeground(tempStyle, colour);
            commandPanelDocumentFilter.setTextStyle(tempStyle);
            commandPanelDocumentFilter.resetTextColor(inputCommandBox.getStyledDocument());
        }
    }
    
    /**
     * Sets the font attributes of the text to be displayed within the text component.
     *
     * @param newFont    The font whose attributes will be set for the default font attributes of the text to be 
     *                   displayed within the text component.
     */
    public void setFontAttributes( Font newFont ){
        if( newFont != null ){
            inputCommandBox.setFont(newFont);
            Style tempStyle = getStyleOfTextPane(inputCommandBox);
            StyleConstants.setFontFamily(tempStyle, newFont.getFamily());
            StyleConstants.setFontSize(tempStyle, newFont.getSize());
            StyleConstants.setBold(tempStyle, newFont.isBold());
            StyleConstants.setItalic(tempStyle, newFont.isItalic());
            commandPanelDocumentFilter.setTextStyle(tempStyle);
        }
    }
    
    /**
     * Returns the text component responsible for displaying text within the commandTextBox class
     *
     * @return  The text component responsible for displaying text within the commandTextBox class
     */
    public JTextPane getTextDisplay(){
        return inputCommandBox;
    }
    
    /**
     * Returns the pop up box responsible for showing the example commands.
     *
     * @return  The pop up box responsible for showing the example commands
     */
    public JComboBox<String> getPopupBox(){
        return popUpBox;
    }
    
    /**
     * Returns true if there is a currently selected item in the pop up box; false otherwise.
     *
     * @return  True if there is a currently selected item in the pop up box; false otherwise.
     */
    public boolean hasSelectedItem(){
        if( popUpList != null ){
            return (popUpList.getSelectedItem() != null);
        } else {
            return false;
        }
    }
    
    /**
     * Returns the original style of the text displayed in the text component. 
     *
     * @param textPane The text component that will display the text typed in
     * @return  The original style of the text displayed in the text component. If there are no
     *          original text style found, it returns a default text style instead.
     */
    private Style getStyleOfTextPane( JTextPane textPane ){
        if( textPane != null ){
            Style tempStyle = textPane.getStyle(DEFAULT_TEXT_STYLE_NAME);
            if( tempStyle == null ){
                tempStyle = textPane.addStyle(DEFAULT_TEXT_STYLE_NAME, null);
                Font tempFont = textPane.getFont();
                StyleConstants.setFontFamily(tempStyle, tempFont.getFamily());
                StyleConstants.setFontSize(tempStyle, tempFont.getSize());
                StyleConstants.setBold(tempStyle, tempFont.isBold());
                StyleConstants.setItalic(tempStyle, tempFont.isItalic());
                StyleConstants.setForeground(tempStyle, textPane.getForeground());
            }
            return tempStyle;
        } else{
            return null;
        }
    }
}
