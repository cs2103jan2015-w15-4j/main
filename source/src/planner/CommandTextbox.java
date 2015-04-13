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

    private JTextPane inputCommandBox_;
    
    private JComboBox<String> popUpBox_;
    
    private DefaultComboBoxModel<String> popUpList_;
    
    private String []commandKeywords_;
    private String []nonCommandKeywords_;
    
    private Style originalTextStyle_;
    
    private ArrayList<String> possibleCommands_;
    
    private int currentPopupListIndex_;
    
    private CustomTextFieldDocumentFilter commandPanelDocumentFilter_;
    
    private boolean isCurrentlyHandlingKeyEvent_;
    
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
    private final int MINIMUM_SCROLLBAR_WIDTH = 0;
    private final int MINIMUM_SCROLLBAR_HEIGHT = 0;
    private final int ONE_CARET_INCREMENT_LENGTH = 1;
    private final int MINIMUM_CARET_POSITION = 0;
    
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
        
        commandKeywords_ = commandKeywords;
        nonCommandKeywords_ = nonCommandKeywords;
        inputCommandBox_ = new JTextPane();
        prepareTextPane(inputCommandBox_);

        setFocusable(false);
        setBorder(null);
        setOpaque(false);
        
        setViewportView(inputCommandBox_);
        if( inputCommandBox_ != null ){
            getViewport().setOpaque(false);
        }
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        
        if( horizontalScrollBar != null ){
            horizontalScrollBar.setUI(new InvisibleScrollBarUI());
            horizontalScrollBar.setFocusable(false);
            horizontalScrollBar.setPreferredSize(new Dimension(MINIMUM_SCROLLBAR_WIDTH,MINIMUM_SCROLLBAR_HEIGHT));
        }
        prepareComboBox( inputCommandBox_, listOfCommands );
        isCurrentlyHandlingKeyEvent_ = false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when the up arrow key is registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    private boolean handleUpKeyEvent( KeyEvent keyEvent ){
        if( keyEvent != null ){
            if( !keyEvent.isShiftDown() && !keyEvent.isControlDown() ){
                currentPopupListIndex_ = Math.max( currentPopupListIndex_ - SIZE_OF_ONE_ITEM_IN_POPUP_LIST, 
                                                  SELF_DEFINED_MINIMUM_IDX_OF_POPUP_LIST );
                popUpBox_.setSelectedIndex(getPopupListIdx(currentPopupListIndex_, popUpList_.getSize() ));
                if( currentPopupListIndex_ <= SELF_DEFINED_MINIMUM_IDX_OF_POPUP_LIST ){
                    currentPopupListIndex_ = popUpList_.getSize() - SIZE_OF_ONE_ITEM_IN_POPUP_LIST;
                }
                if( popUpBox_.getSelectedIndex() < SIZE_OF_EMPTY_POPUP_LIST ){
                    popUpBox_.hidePopup();
                    popUpBox_.setPopupVisible(true);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when the down arrow key is registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    private boolean handleDownKeyEvent( KeyEvent keyEvent ){
        if( keyEvent != null ){
            if( !keyEvent.isShiftDown() && !keyEvent.isControlDown() ){
                currentPopupListIndex_ = Math.min( currentPopupListIndex_ + SIZE_OF_ONE_ITEM_IN_POPUP_LIST, 
                                                  popUpList_.getSize() + SIZE_OF_ONE_ITEM_IN_POPUP_LIST );
                popUpBox_.setSelectedIndex(getPopupListIdx(currentPopupListIndex_ , popUpList_.getSize() ));
                if( currentPopupListIndex_ > popUpList_.getSize() ){
                    currentPopupListIndex_ = (popUpList_.getSize() > SIZE_OF_EMPTY_POPUP_LIST ? FIRST_ITEM_IDX_IN_POPUP_LIST : MINIMUM_IDX_OF_POPUP_LIST);
                }
                if( popUpBox_.getSelectedIndex() < SIZE_OF_EMPTY_POPUP_LIST ){
                    popUpBox_.hidePopup();
                    popUpBox_.setPopupVisible(true);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when the left arrow key is registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    private boolean handleLeftKeyEvent( KeyEvent keyEvent ){
        if( keyEvent != null ){
            int pos = Math.max(inputCommandBox_.getCaretPosition() - ONE_CARET_INCREMENT_LENGTH, MINIMUM_CARET_POSITION );
            inputCommandBox_.setCaretPosition(pos);
            popUpBox_.hidePopup();
            popUpBox_.setPopupVisible(true);
            return true;
        }
        return false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when the right arrow key is registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    private boolean handleRightKeyEvent( KeyEvent keyEvent ){
        if( keyEvent != null ){
            int pos = Math.min(inputCommandBox_.getText().length(), inputCommandBox_.getCaretPosition() + ONE_CARET_INCREMENT_LENGTH );
            inputCommandBox_.setCaretPosition(pos);
            popUpBox_.hidePopup();
            popUpBox_.setPopupVisible(true);
            return true;
        }
        return false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when the enter key is registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    private boolean handleEnterKeyEvent( KeyEvent keyEvent ){
        if( keyEvent != null ){
            if( popUpBox_.getSelectedItem() != null ){
                inputCommandBox_.setText(popUpBox_.getSelectedItem().toString());
                popUpBox_.hidePopup();
                popUpBox_.setPopupVisible(false);
                inputCommandBox_.setCaretPosition(inputCommandBox_.getText().length());
                return true;
            } else{
                popUpBox_.hidePopup();
                popUpBox_.setPopupVisible(false);
            }
        }
        return false;
    }
    
    /**
     * This method will invoke commandTextBox events that should be called when specific keys are registered by the keyboard
     *
     * @param keyEvent   The key event that will be processed
     * @return TRUE if the events are invoked successfully; false otherwise.
     */
    public boolean handleKeyEvent( KeyEvent keyEvent ){
        if( !isCurrentlyHandlingKeyEvent_ && keyEvent != null && popUpBox_ != null && popUpList_ != null ){
            if( popUpBox_ != null && popUpBox_.isPopupVisible() && inputCommandBox_.isFocusOwner() ){
                isCurrentlyHandlingKeyEvent_ = true;
                restrictPopupBoxDimensions( inputCommandBox_, popUpBox_, DEFAULT_POPUP_BOX_RELATIVE_YCOORDINATE );
                keyEvent.setSource( popUpBox_ );
                popUpBox_.dispatchEvent(keyEvent);
                if( keyEvent.getKeyCode() == KeyEvent.VK_UP ){
                    if( !handleUpKeyEvent(keyEvent) ){
                        isCurrentlyHandlingKeyEvent_ = false;
                        return false;
                    }
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_DOWN ){
                    if( !handleDownKeyEvent(keyEvent) ){
                        isCurrentlyHandlingKeyEvent_ = false;
                        return false;
                    }
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_LEFT ){
                    if( !handleLeftKeyEvent(keyEvent) ){
                        isCurrentlyHandlingKeyEvent_ = false;
                        return false;
                    }
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_RIGHT ){
                    if( !handleRightKeyEvent(keyEvent) ){
                        isCurrentlyHandlingKeyEvent_ = false;
                        return false;
                    }
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_ENTER ){
                    if( !handleEnterKeyEvent(keyEvent) ){
                        isCurrentlyHandlingKeyEvent_ = false;
                        return false;
                    }
                }
                isCurrentlyHandlingKeyEvent_ = false;
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
            popUpList_ = new DefaultComboBoxModel<String>();
            popUpBox_ = new JComboBox<String>(popUpList_){
                @Override
                public Dimension getPreferredSize(){
                    return new Dimension( textPane.getVisibleRect().width, MINIMUM_POPUP_BOX_HEIGHT );
                }
            };
           
            popUpBox_.setOpaque(false);
            popUpBox_.setFocusable(false);
            popUpBox_.setFont(new Font(DEFAULT_POPUP_BOX_STRING_FONT_FAMILY, Font.PLAIN, DEFAULT_POPUP_BOX_STRING_FONT_SIZE));
            possibleCommands_ = new ArrayList<String>();
            
            ArrayList<String> tempStringList;
            for( Map.Entry<String, ArrayList<String>> tempEntry : listOfCommands ){
                tempStringList = tempEntry.getValue();
                if( tempStringList != null ){
                    for( String tempString : tempStringList ){
                        if( tempString != null ){
                            possibleCommands_.add(tempString);
                        }
                    }
                }
            }
            currentPopupListIndex_ = MINIMUM_IDX_OF_POPUP_LIST;
            popUpBox_.setSelectedIndex(currentPopupListIndex_);
            bindDocumentListener( textPane, popUpBox_, popUpList_ );
            textPane.setLayout(new BorderLayout());
            textPane.add( popUpBox_, BorderLayout.SOUTH );
        }
    }
    
    /**
     * Turns syntax text coloring on within the text component
     */
    public void setSyntaxFilterOn(){
        commandPanelDocumentFilter_.setFilterOn();
    }
    
    /**
     * Turns syntax text coloring off within the text component
     */
    public void setSyntaxFilterOff(){
        commandPanelDocumentFilter_.setFilterOff();
    }
    
    /**
     * Hides the popup box used to display the relevant example commands from view  
     */
    public void hidePopupBox(){
        if( popUpBox_ != null ){
            popUpBox_.removeAllItems();
            popUpBox_.setPopupVisible(false);
            popUpBox_.hidePopup();
        }
    }
    
    /**
     * Displays the popup box used to show the relevant example commands
     */
    public void showPopupBox(){
        if( popUpBox_ != null ){
            popUpBox_.setPopupVisible(true);
            popUpBox_.showPopup();
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
                                    for( String currentCommand : possibleCommands_ ){
                                        wordsInCommand = currentCommand.split( DEFAULT_DELIMITER, NUM_WORDS_TO_SPLIT_INTO );
                                        if( wordsInCommand.length > MIMIMUM_STRING_LENGTH && 
                                            firstWordInUserInput.startsWith(wordsInCommand[FIRST_WORD_INDEX]) ){
                                            popupBoxList.addElement(currentCommand);
                                        }
                                    }
                                }
                            }
                            currentPopupListIndex_ = MINIMUM_IDX_OF_POPUP_LIST;
                            popupBox.setSelectedIndex(currentPopupListIndex_);
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
            inputCommandBox_.setEditorKit(new CustomNoWrapKit());
            inputCommandBox_.addCaretListener(new CustomCaretListener(DEFAULT_NUMBER_OF_PIXELS_TO_SHOW_BEYOND_CARET));
            inputCommandBox_.setOpaque(false);
            inputCommandBox_.setFont( new Font( DEFAULT_TEXTPANE_STRING_FONT_FAMILY, Font.BOLD, DEFAULT_TEXTPANE_STRING_FONT_SIZE ) );
            inputCommandBox_.setForeground(DEFAULT_TEXTPANE_FONT_COLOR);
            originalTextStyle_ = getStyleOfTextPane(textPane);
            AbstractDocument abstractDocument = (AbstractDocument)inputCommandBox_.getDocument();
            commandPanelDocumentFilter_ = new CustomTextFieldDocumentFilter( commandKeywords_, nonCommandKeywords_, originalTextStyle_);
            abstractDocument.setDocumentFilter(commandPanelDocumentFilter_);
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
                commandPanelDocumentFilter_.setFilterOff();
            }
            inputCommandBox_.setText(text);
        }
    }
    
    /**
     * Sets the default colour of the text to be displayed within the text component.
     *
     * @param colour    The default colour of the text to be displayed within the text component.
     */
    public void setForegroundColor( Color colour ){
        if( colour != null ){
            inputCommandBox_.setForeground(colour);
            Style tempStyle = getStyleOfTextPane(inputCommandBox_);
            StyleConstants.setForeground(tempStyle, colour);
            commandPanelDocumentFilter_.setTextStyle(tempStyle);
            commandPanelDocumentFilter_.resetTextColor(inputCommandBox_.getStyledDocument());
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
            inputCommandBox_.setFont(newFont);
            Style tempStyle = getStyleOfTextPane(inputCommandBox_);
            StyleConstants.setFontFamily(tempStyle, newFont.getFamily());
            StyleConstants.setFontSize(tempStyle, newFont.getSize());
            StyleConstants.setBold(tempStyle, newFont.isBold());
            StyleConstants.setItalic(tempStyle, newFont.isItalic());
            commandPanelDocumentFilter_.setTextStyle(tempStyle);
        }
    }
    
    /**
     * Returns the text component responsible for displaying text within the commandTextBox class
     *
     * @return  The text component responsible for displaying text within the commandTextBox class
     */
    public JTextPane getTextDisplay(){
        return inputCommandBox_;
    }
    
    /**
     * Returns the pop up box responsible for showing the example commands.
     *
     * @return  The pop up box responsible for showing the example commands
     */
    public JComboBox<String> getPopupBox(){
        return popUpBox_;
    }
    
    /**
     * Returns true if there is a currently selected item in the pop up box; false otherwise.
     *
     * @return  True if there is a currently selected item in the pop up box; false otherwise.
     */
    public boolean hasSelectedItem(){
        if( popUpList_ != null ){
            return (popUpList_.getSelectedItem() != null);
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
