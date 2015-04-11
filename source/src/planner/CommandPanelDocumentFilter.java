//@author A0111333B

package planner;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CommandPanelDocumentFilter extends DocumentFilter{

    private String []commandKeywords_;
    private String []nonCommandKeywords_;
    private String commandKeywordRegexPattern_;
    private String nonCommandKeywordRegexPattern_;
    
    private boolean isFilterTurnedOff_;
    
    private Style originalStyle_;
    
    private final Color COLOUR_COMMAND_KEYWORD = new Color( 0, 0, 192 );
    private final Color COLOUR_NONCOMMAND_KEYWORD = new Color( 127, 0, 85 );
    
    private final int CHARACTER_LIMIT = 2000;
    
    public CommandPanelDocumentFilter( String []commandkeywords, String []nonCommandKeywords, Style originalStyle ){
        
        super();

        if( commandkeywords != null && nonCommandKeywords != null && originalStyle != null ){
            
            commandKeywords_ = commandkeywords;
            nonCommandKeywords_ = nonCommandKeywords;
            originalStyle_ = originalStyle;
            
            commandKeywordRegexPattern_ = generateRegex(commandKeywords_);
            nonCommandKeywordRegexPattern_ = generateRegex(nonCommandKeywords_);
        }
        
        setFilterOn();
    }
    
    public void setFilterOn(){
        
        isFilterTurnedOff_ = false;
    }
    
    public void setFilterOff(){
        
        isFilterTurnedOff_ = true;
    }
    
    public void setTextStyle( Style currentTextStyle ){
        
        if( currentTextStyle != null ){
            
            originalStyle_ = currentTextStyle;
        }
    }
    
    public CommandPanelDocumentFilter(){
        
        this( null, null, null );
    }
    
    @Override
    public void insertString( DocumentFilter.FilterBypass filterBypass, int offset, String str, AttributeSet attribute ){
        
        try{
            
            StyledDocument doc = null;
            
            if( str != null && filterBypass != null ){
                
                if( str.contains("\n") ){
                    
                    str.replaceAll("\\n", "");
                }
                
                doc = (StyledDocument)filterBypass.getDocument();
                
                if( doc.getLength() + str.length() >= CHARACTER_LIMIT ){
                    
                    return; 
                }
            }

            super.insertString(filterBypass, offset, str, originalStyle_);
            
            if( doc != null && commandKeywords_ != null ){
                
                syntaxHighlightingListener( doc );
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    @Override
    public void replace( DocumentFilter.FilterBypass filterBypass, int offset, int strLength, String str, AttributeSet attribute ){
        
        try{
            
            StyledDocument doc = null;
            
            if( str != null && filterBypass != null ){
                
                if( str.contains("\n") ){
                    
                    str.replaceAll("\\n", "");
                }
                
                doc = (StyledDocument)filterBypass.getDocument();
                
                if( doc.getLength() + str.length() >= CHARACTER_LIMIT ){
                    
                    return; 
                }
            }
            
            super.replace(filterBypass, offset, strLength, str, originalStyle_);
            
            if( doc != null && commandKeywords_ != null ){
                
                syntaxHighlightingListener( doc );
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    @Override
    public void remove( DocumentFilter.FilterBypass filterBypass, int offset, int strLength ){
        
        try {
            
            StyledDocument doc = null;
            
            if( filterBypass != null ){
                
                doc = (StyledDocument)filterBypass.getDocument();
            }
            
            super.remove( filterBypass, offset, strLength );
            
            if( doc != null && commandKeywords_ != null ){
                
                syntaxHighlightingListener( doc );
            }
            
        } catch (BadLocationException e) {}
    }
    
    private void changeTextColor( StyledDocument doc, int offset, int stringLength, Color color ){
        
        if( doc != null && originalStyle_ != null && color != null && offset >= 0 && stringLength > 0 ){
            
            Color tempColor = StyleConstants.getForeground(originalStyle_);
            StyleConstants.setForeground(originalStyle_, color);
            
            doc.setCharacterAttributes(offset, stringLength, originalStyle_, true);
            StyleConstants.setForeground(originalStyle_, tempColor);
        }
    }
    
    public void resetTextColor( StyledDocument doc ){
        
        if( doc != null && originalStyle_ != null ){

            changeTextColor( doc, 0, doc.getLength(), StyleConstants.getForeground(originalStyle_) );
        }
    }
    
    private void syntaxHighlightingListener( final StyledDocument doc ){
        
        if( doc != null && commandKeywords_ != null && nonCommandKeywords_ != null ){
            
            SwingUtilities.invokeLater(new Runnable(){
    
                @Override
                public void run() {
                    
                    if( !isFilterTurnedOff_ ){
                        
                        resetTextColor( doc );
                            
                        Pattern commandPattern = Pattern.compile(commandKeywordRegexPattern_);
                        Pattern nonCommandPattern = Pattern.compile(nonCommandKeywordRegexPattern_);
                        
                        try{
                            
                            if( doc.getLength() > 0 ){
                                
                                String text = doc.getText( 0, doc.getLength() ).toLowerCase();
                                
                                Matcher commandStrMatcher = commandPattern.matcher(text);
                                Matcher nonCommandStrMatcher = nonCommandPattern.matcher(text);
                                
                                int startIdx;
                                int endIdx;
                                String stringInFrontOfMatchedWord;
                                while( commandStrMatcher.find() ){
                                    
                                    startIdx = commandStrMatcher.start();
                                    endIdx = commandStrMatcher.end();
                                    
                                    stringInFrontOfMatchedWord = text.substring(0, startIdx).trim();
                                    
                                    if( stringInFrontOfMatchedWord.length() <= 0 ){
      
                                        changeTextColor( doc, startIdx, endIdx-startIdx, COLOUR_COMMAND_KEYWORD );
                                        break;
                                    }
                                }
                                
                                while( nonCommandStrMatcher.find() ){
                                    
                                    startIdx = nonCommandStrMatcher.start();
                                    endIdx = nonCommandStrMatcher.end();
                                    
                                    if( startIdx - 1 < 0 || text.charAt(startIdx - 1) != '/' ){
                                    
                                        changeTextColor( doc, startIdx, endIdx-startIdx, COLOUR_NONCOMMAND_KEYWORD );
                                    }
                                }
                            }
                            
                        } catch( BadLocationException badLocationException ){}
                    }
                }
                
            });
        }
    }
    
    public static String generateRegex( String []keywords ){
        
        StringBuilder regex = new StringBuilder("");
        
        regex.append("(");
        
        if( keywords != null ){
            
            for( int i = 0, size = keywords.length; i < size; ++i ){
                
                if( keywords[i] != null ){
                    regex.append("\\b").append(keywords[i]).append("\\b").append("|");
                }
            }
            
            if( regex.length() > 1 ){
                
                regex.deleteCharAt(regex.length()-1);
            }
        }
        
        regex.append(")");
        
        return regex.toString();
    }
}
