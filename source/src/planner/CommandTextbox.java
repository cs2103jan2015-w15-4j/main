package planner;

import java.awt.BorderLayout;
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
    
    private boolean hasPopupBoxChange;
    
    private ArrayList<String> possibleCommands;
    
    private int currentPopupListIndex;
    
    public CommandTextbox( String []commandKeywords, String []nonCommandKeywords, 
                           Set<Map.Entry<String, ArrayList<String>>> listOfCommands ){
        
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
            
            hasPopupBoxChange = true;
            
            if( popUpBox != null && popUpBox.isPopupVisible() ){
                
                restrictPopupBoxDimensions( inputCommandBox, popUpBox, 50 );
                
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
                    }
                }
                    
                hasPopupBoxChange = false;
                
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
                                  Set<Map.Entry<String, ArrayList<String>>> listOfCommands){
        
        if( textPane != null && listOfCommands != null ){
        
            popUpBox = new JComboBox<String>(){
                
                @Override
                public Dimension getPreferredSize(){
                    
                    return new Dimension( textPane.getVisibleRect().width, 0 );
                }
            };
            
            popUpList = new DefaultComboBoxModel<String>();
            
            popUpBox.setOpaque(false);
            
            hasPopupBoxChange = false;
            
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
            
            bindActionListener(textPane, popUpBox);
            
            bindDocumentListener( textPane, popUpBox, popUpList );
            
            textPane.setLayout(new BorderLayout());
            textPane.add( popUpBox, BorderLayout.SOUTH );
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
                        
                        restrictPopupBoxDimensions( textPane, popupBox, 50 );
                        
                        // Might have to remove all action listeners here
                        
                        popupBoxList.removeAllElements();
                        
                        String userInput = textPane.getText().trim();
                        
                        if( !userInput.isEmpty() ){
                            
                            String []userInputWords = userInput.split( " ", 2 );
                            
                            if( userInputWords.length > 0 ){
                            
                                String firstWordInUserInput = userInputWords[0];
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
    
    private void bindActionListener( final JTextPane textPane, final JComboBox<String> popupBox ){
        
        if( textPane != null && popupBox != null ){
            
            popupBox.addActionListener(new ActionListener(){
                
                @Override
                public void actionPerformed( ActionEvent event ){
                    
                    if( !hasPopupBoxChange && popupBox.getSelectedItem() != null ){
                        
                        textPane.setText( popupBox.getSelectedItem().toString() );
                    }
                }
            });
        }
    }
    
    private void prepareTextPane( JTextPane textPane ){
        
        if( textPane != null ){
            
            inputCommandBox.setEditorKit(new CustomWrapKit());
            inputCommandBox.addCaretListener(new CustomCaretListener(2));
            inputCommandBox.setOpaque(false);
            inputCommandBox.setFont( new Font( "Arial", Font.BOLD, 20 ) );
            
            originalTextStyle = getStyleOfTextPane(textPane);
            
            AbstractDocument abstractDocument = (AbstractDocument)inputCommandBox.getDocument();
            abstractDocument.setDocumentFilter(new CommandPanelDocumentFilter( m_commandKeywords, m_nonCommandKeywords, originalTextStyle));
        }
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
