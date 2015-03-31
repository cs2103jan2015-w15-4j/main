package planner;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    private String []m_commandKeywords;
    private String []m_nonCommandKeywords;
    private String commandKeywordRegexPattern;
    private String nonCommandKeywordRegexPattern;
    
    private Style m_originalStyle;
    
    private Color COMMAND_KEYWORD_COLOUR;
    private Color NONCOMMAND_KEYWORD_COLOUR;
    
    public CommandPanelDocumentFilter( String []commandkeywords, String []nonCommandKeywords, Style originalStyle ){
        
        super();
        
        if( commandkeywords != null && nonCommandKeywords != null && originalStyle != null ){
            
            m_commandKeywords = commandkeywords;
            m_nonCommandKeywords = nonCommandKeywords;
            m_originalStyle = originalStyle;
            
            commandKeywordRegexPattern = generateRegex(m_commandKeywords);
            nonCommandKeywordRegexPattern = generateRegex(m_nonCommandKeywords);
        }
        
        COMMAND_KEYWORD_COLOUR = new Color( 0, 0, 192 );
        NONCOMMAND_KEYWORD_COLOUR = new Color( 127, 0, 85 );
    }
    
    public CommandPanelDocumentFilter(){
        
        this( null, null, null );
    }
    
    @Override
    public void insertString( DocumentFilter.FilterBypass filterBypass, int offset, String str, AttributeSet attribute ){
        
        try{
            
            if( str != null && str.contains("\n") ){
                
                str.replaceAll("\\n", "");
            }
            
            super.insertString(filterBypass, offset, str, attribute);
            
            if( filterBypass != null && m_commandKeywords != null ){
                
                syntaxHighlightingListener( (StyledDocument)filterBypass.getDocument() );
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    @Override
    public void replace( DocumentFilter.FilterBypass filterBypass, int offset, int strLength, String str, AttributeSet attribute ){
        
        try{
            
            if( str != null && str.contains("\n") ){
                
                str.replaceAll("\\n", "");
            }
            
            super.replace(filterBypass, offset, strLength, str, attribute);
            
            if( filterBypass != null && m_commandKeywords != null ){
                
                syntaxHighlightingListener( (StyledDocument)filterBypass.getDocument() );
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    @Override
    public void remove( DocumentFilter.FilterBypass filterBypass, int offset, int strLength ){
        
        try {
            
            super.remove( filterBypass, offset, strLength );
            
            if( filterBypass != null && m_commandKeywords != null ){
                
                syntaxHighlightingListener( (StyledDocument)filterBypass.getDocument() );
            }
            
        } catch (BadLocationException e) {}
    }
    
    private void changeTextColor( StyledDocument doc, int offset, int stringLength, Color color ){
        
        if( doc != null && m_originalStyle != null && color != null && offset >= 0 && offset < stringLength && stringLength > 0 ){
            
            Color tempColor = StyleConstants.getForeground(m_originalStyle);
            StyleConstants.setForeground(m_originalStyle, color);
            
            doc.setCharacterAttributes(offset, stringLength, m_originalStyle, true);
            StyleConstants.setForeground(m_originalStyle, tempColor);
        }
    }
    
    private void resetTextColor( StyledDocument doc ){
        
        if( doc != null && m_originalStyle != null ){
            
            changeTextColor( doc, 0, doc.getLength(), StyleConstants.getForeground(m_originalStyle) );
        }
    }
    
    private void syntaxHighlightingListener( final StyledDocument doc ){
        
        if( doc != null && m_commandKeywords != null && m_nonCommandKeywords != null ){
            
            SwingUtilities.invokeLater(new Runnable(){
    
                @Override
                public void run() {
                    
                    resetTextColor( doc );
                        
                    Pattern commandPattern = Pattern.compile(commandKeywordRegexPattern);
                    Pattern nonCommandPattern = Pattern.compile(nonCommandKeywordRegexPattern);
                    
                    try{
                        
                        if( doc.getLength() > 0 ){
                            
                            String text = doc.getText( 0, doc.getLength() );
                            
                            Matcher commandStrMatcher = commandPattern.matcher(text);
                            Matcher nonCommandStrMatcher = nonCommandPattern.matcher(text);
                            
                            int startIdx;
                            int endIdx;
                            while( commandStrMatcher.find() ){
                                
                                startIdx = commandStrMatcher.start();
                                endIdx = nonCommandStrMatcher.end();
                                
                                if( startIdx == 0 ){
                                    
                                    changeTextColor( doc, startIdx, endIdx-startIdx, COMMAND_KEYWORD_COLOUR );
                                }
                            }
                            
                            while( nonCommandStrMatcher.find() ){
                                
                                startIdx = nonCommandStrMatcher.start();
                                endIdx = nonCommandStrMatcher.end();
                                
                                changeTextColor( doc, startIdx, endIdx-startIdx, NONCOMMAND_KEYWORD_COLOUR );
                            }
                        }
                        
                    } catch( BadLocationException badLocationException ){}
                }
                
            });
        }
    }
    
    private String generateRegex( String []keywords ){
        
        StringBuilder regex = new StringBuilder("");
        
        regex.append("(");
        
        if( keywords != null ){
            
            for( int i = 0, size = keywords.length; i < size; ++i ){
                
                regex.append("\\b").append(keywords[i]).append("\\b").append("|");
            }
            
            if( regex.length() > 1 ){
                
                regex.deleteCharAt(regex.length()-1);
            }
        }
        
        regex.append(")");
        
        return regex.toString();
    }
}
