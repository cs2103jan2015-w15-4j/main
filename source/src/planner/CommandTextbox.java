package planner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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

public class CommandTextbox extends JScrollPane{

    private JTextPane inputCommandBox;
    
    private JComboBox<String> popUpBox;
    
    private DefaultComboBoxModel<String> popUpList;
    
    private String []m_commandKeywords;
    private String []m_nonCommandKeywords;
    
    private Style originalTextStyle;
    
    private ArrayList<String> possibleCommands;
    
    private int currentPopupListIndex;
    
    private CommandPanelDocumentFilter commandPanelDocumentFilter;
    
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
    }
    
    public boolean handleKeyEvent( KeyEvent keyEvent ){
        
        if( keyEvent != null && popUpBox != null && popUpList != null ){
            
            if( popUpBox != null && popUpBox.isPopupVisible() && inputCommandBox.isFocusOwner() ){
                
                restrictPopupBoxDimensions( inputCommandBox, popUpBox, 30 );
                
                keyEvent.setSource( popUpBox );
                popUpBox.dispatchEvent(keyEvent);
                
                if( keyEvent.getKeyCode() == KeyEvent.VK_UP ){
                    
                    currentPopupListIndex = Math.max( currentPopupListIndex - 1, -2 );
                    popUpBox.setSelectedIndex(getPopupListIdx(currentPopupListIndex, popUpList.getSize() ));
                    currentPopupListIndex = (currentPopupListIndex > -2 ? currentPopupListIndex : popUpList.getSize() - 1);
                    
                    if( popUpBox.getSelectedIndex() < 0 ){
                        popUpBox.hidePopup();
                        popUpBox.setPopupVisible(true);
                    }
                    
                } else if( keyEvent.getKeyCode() == KeyEvent.VK_DOWN ){
                    
                    
                    currentPopupListIndex = Math.min( currentPopupListIndex + 1, popUpList.getSize() + 1 );
                    popUpBox.setSelectedIndex(getPopupListIdx(currentPopupListIndex , popUpList.getSize() ));
                    
                    currentPopupListIndex = ( currentPopupListIndex<= popUpList.getSize() ? currentPopupListIndex  : (popUpList.getSize() > 0 ? 0 : -1) );
                    
                    if( popUpBox.getSelectedIndex() < 0 ){
                        popUpBox.hidePopup();
                        popUpBox.setPopupVisible(true);
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
                        return true;
                        
                    } else{
                        
                        popUpBox.hidePopup();
                        popUpBox.setPopupVisible(false);
                        return false;
                    }
                }
                
                return true;
                
            } else{
                
                return false;
            }
            
        } else{
            
            return false;
        }
    }
    
    private int getPopupListIdx( int idx, int listSize ){
        
        if( idx >= 0 && idx < listSize ){
            
            return idx;
            
        } else if( idx == -2 ){
            
            return listSize-1;
            
        } else if( idx == listSize + 1 ){
            
            return (listSize > 0 ? 0 : -1);
            
        } else{
            
            return -1;
        }
    }
    
    private void prepareComboBox( final JTextPane textPane, 
                                  ArrayList<Map.Entry<String, ArrayList<String>>> listOfCommands){
        
        if( textPane != null && listOfCommands != null ){
        
            popUpList = new DefaultComboBoxModel<String>();
            
            popUpBox = new JComboBox<String>(popUpList){
                
                @Override
                public Dimension getPreferredSize(){
                    
                    return new Dimension( textPane.getVisibleRect().width, 0 );
                }
            };
            
            popUpBox.setOpaque(false);
            popUpBox.setFocusable(false);
            
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
    
    public void setSyntaxFilterOn(){
        
        commandPanelDocumentFilter.setFilterOn();
    }
    
    public void setSyntaxFilterOff(){
        
        commandPanelDocumentFilter.setFilterOff();
    }
    
    public void hidePopupBox(){
        
        if( popUpBox != null ){
            
            popUpBox.removeAllItems();
            popUpBox.setPopupVisible(false);
            popUpBox.hidePopup();
        }
    }
    
    public void showPopupBox(){
        
        if( popUpBox != null ){
            
            popUpBox.setPopupVisible(true);
            popUpBox.showPopup();
        }
    }
    
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
                        
                            restrictPopupBoxDimensions( textPane, popupBox, 30 );
                            
                            popupBoxList.removeAllElements();
                            
                            String userInput = textPane.getText().trim();
                            
                            if( !userInput.isEmpty() ){
                                
                                String []userInputWords = userInput.split( " ", 2 );
                                
                                if( userInputWords.length > 0 ){
                                
                                    String firstWordInUserInput = userInputWords[0].toLowerCase();
                                    String []wordsInCommand;
                                    
                                    for( String currentCommand : possibleCommands ){
                                        
                                        wordsInCommand = currentCommand.split( " ", 2 );
                                        
                                        if( wordsInCommand.length > 0 && firstWordInUserInput.startsWith(wordsInCommand[0]) ){
                                            
                                            popupBoxList.addElement(currentCommand);
                                        }
                                    }
                                }
                            }
                            
                            currentPopupListIndex = -1;
                            popupBox.setSelectedIndex(currentPopupListIndex);
                            popupBox.hidePopup();
                            popupBox.setPopupVisible(popupBoxList.getSize() > 0);
                        }
                    }
                    
                    
                });
            }
        }
    }
    
    private void restrictPopupBoxDimensions( JTextPane textPane, JComboBox<String> popupBox, int yCoordinate ){
        
        Rectangle tempRectangle = textPane.getVisibleRect();
        
        if( tempRectangle != null && popupBox != null && yCoordinate >= 0 ){
            
            tempRectangle.height = 0;
            tempRectangle.y = yCoordinate;
            popupBox.setBounds(tempRectangle);
        }
    }
    
    private void prepareTextPane( JTextPane textPane ){
        
        if( textPane != null ){
            
            inputCommandBox.setEditorKit(new CustomNoWrapKit());
            inputCommandBox.addCaretListener(new CustomCaretListener(2));
            inputCommandBox.setOpaque(false);
            inputCommandBox.setFont( new Font( "Arial", Font.BOLD, 20 ) );
            inputCommandBox.setForeground(new Color(128,128,128));
            
            originalTextStyle = getStyleOfTextPane(textPane);
            
            AbstractDocument abstractDocument = (AbstractDocument)inputCommandBox.getDocument();
            commandPanelDocumentFilter = new CommandPanelDocumentFilter( m_commandKeywords, m_nonCommandKeywords, originalTextStyle);
            abstractDocument.setDocumentFilter(commandPanelDocumentFilter);
        }
    }
    
    public void setText( String text, boolean turnOffFilter ){
        
        if( text != null ){
            
            if( turnOffFilter ){
                
                commandPanelDocumentFilter.setFilterOff();
            }
            
            inputCommandBox.setText(text);
        }
    }
    
    public void setForegroundColor( Color colour ){
        
        if( colour != null ){
            
            inputCommandBox.setForeground(colour);
            
            Style tempStyle = getStyleOfTextPane(inputCommandBox);
            StyleConstants.setForeground(tempStyle, colour);
            commandPanelDocumentFilter.setTextStyle(tempStyle);
            commandPanelDocumentFilter.resetTextColor(inputCommandBox.getStyledDocument());
        }
    }
    
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
    
    public JTextPane getTextDisplay(){
        
        return inputCommandBox;
    }
    
    private Style getStyleOfTextPane( JTextPane textPane ){
        
        if( textPane != null ){
            
            Style tempStyle = textPane.getStyle("original");
            
            if( tempStyle == null ){
                
                tempStyle = textPane.addStyle("original", null);
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
