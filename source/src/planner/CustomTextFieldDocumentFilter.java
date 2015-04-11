//@author A0111333B

package planner;

import java.awt.Color;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
* The CustomTextFieldDocumentFilter class handles syntax colouring of keywords, filters new line characters and
* imposes a character limit for strings entered in text fields using this filter
*
* @author A0111333B
*/

public class CustomTextFieldDocumentFilter extends DocumentFilter{

    private String []commandKeywords_;
    private String []nonCommandKeywords_;
    private String commandKeywordRegexPattern_;
    private String nonCommandKeywordRegexPattern_;
    
    private boolean isFilterTurnedOff_;
    
    private Style originalFontStyle_;
    
    private final Color COLOUR_COMMAND_KEYWORD = new Color( 0, 0, 192 );
    private final Color COLOUR_NONCOMMAND_KEYWORD = new Color( 127, 0, 85 );
    
    private final int CHARACTER_LIMIT = 2000;
    
    private final static Logger customTextFieldDocumentFilterLogger_ = Logger.getLogger(CustomTextFieldDocumentFilter.class.getName());
    
    private final char NONCOMMAND_KEYWORD_NULLIFIER = '/';
    
    private final String NEW_LINE = "\n";
    private final String EMPTY_STRING = "";
    private final String REGEX_NEW_LINE = "\\n";
    private final String REGEX_WORD_BOUNDARY = "\\b";
    private final String REGEX_SEPARATOR = "|";
    private final String REGEX_GROUP_START = "(";
    private final String REGEX_GROUP_END = ")";
    
    private final int MINIMUM_STRING_OFFSET = 0;
    private final int EMPTY_STRING_LENGTH = 0;
    private final int MINIMUM_NON_EMPTY_STRING_LENGTH = 1;
    private final int CHARACTER_LENGTH_IN_STRING = 1;
    
    /**
     * Constructs a filter to filter out new line characters from the document and syntax colours keywords within the document
     *
     * @param commandkeywords    String array containing all command key words (words used to perform an operation).
     * @param nonCommandKeywords String array containing all non-command key words (words used to customize the behavior of an operation).
     * @param originalStyle      The original font style of the document without syntax colouring.
     */
    public CustomTextFieldDocumentFilter( String []commandKeywords, String []nonCommandKeywords, Style originalFontStyle ){
        super();

        customTextFieldDocumentFilterLogger_.setLevel(java.util.logging.Level.SEVERE);
        
        if( commandKeywords != null && nonCommandKeywords != null && originalFontStyle != null ){
            commandKeywords_ = commandKeywords;
            nonCommandKeywords_ = nonCommandKeywords;
            originalFontStyle_ = originalFontStyle;
            commandKeywordRegexPattern_ = generateRegex(commandKeywords_);
            nonCommandKeywordRegexPattern_ = generateRegex(nonCommandKeywords_);
        }
        setFilterOn();
    }
    
    /**
     * Constructs a filter to filter out new line characters from the document but prevents keywords from being syntax coloured 
     * within the document.
     */
    public CustomTextFieldDocumentFilter(){
        this( null, null, null );
        setFilterOff();
    }
    
    /**
     * Turns on syntax colouring filter.
     */
    public void setFilterOn(){
        if( commandKeywords_ != null ){
            isFilterTurnedOff_ = false;
        }
    }
    
    /**
     * Turns off syntax colouring filter.
     */
    public void setFilterOff(){
        isFilterTurnedOff_ = true;
    }
    
    /**
     * Records the original font style used in the document that is to be syntax coloured.
     *
     * @param currentTextStyle  The original font style of the document without syntax colouring.
     */
    public void setTextStyle( Style currentTextStyle ){
        if( currentTextStyle != null ){
            originalFontStyle_ = currentTextStyle;
        }
    }
    
    /**
     * Listens to string insertions in the document currently being filtered
     *
     * @param filterBypass       An object that allows access to the document without any call backs to the document.
     * @param offset             Offset in the document where the string was inserted.
     * @param str                The string that was inserted into the document.
     * @param attribute          The document attributes.
     */
    @Override
    public void insertString( DocumentFilter.FilterBypass filterBypass, int offset, String str, AttributeSet attribute ){
        try{
            StyledDocument doc = null;
            
            if( str != null && filterBypass != null ){
                if( str.contains(NEW_LINE) ){
                    str.replaceAll(REGEX_NEW_LINE, EMPTY_STRING);
                }
                
                doc = (StyledDocument)filterBypass.getDocument();
                
                if( doc.getLength() + str.length() >= CHARACTER_LIMIT ){
                    return; 
                }
            }

            super.insertString(filterBypass, offset, str, originalFontStyle_);
            
            if( doc != null && commandKeywords_ != null ){
                syntaxHighlightingListener( doc );
            }
        } catch( BadLocationException badLocationException ){
            customTextFieldDocumentFilterLogger_.severe(badLocationException.getMessage());
        }
    }
    
    /**
     * Listens to string replacements in the document currently being filtered.
     *
     * @param filterBypass       An object that allows access to the document without any call backs to the document.
     * @param offset             Offset in the document where replacement of string occurs.
     * @param str                The string replacement.
     * @param attribute          The document attributes.
     */
    @Override
    public void replace( DocumentFilter.FilterBypass filterBypass, int offset, int strLength, String str, AttributeSet attribute ){
        try{
            StyledDocument doc = null;
            
            if( str != null && filterBypass != null ){
                
                if( str.contains(NEW_LINE) ){
                    str.replaceAll(REGEX_NEW_LINE, EMPTY_STRING);
                }
                
                doc = (StyledDocument)filterBypass.getDocument();
                
                if( doc.getLength() + str.length() >= CHARACTER_LIMIT ){
                    return; 
                }
            }
            
            super.replace(filterBypass, offset, strLength, str, originalFontStyle_);
            
            if( doc != null && commandKeywords_ != null ){
                syntaxHighlightingListener( doc );
            }
        } catch( BadLocationException badLocationException ){
            customTextFieldDocumentFilterLogger_.severe(badLocationException.getMessage());
        }
    }
    
    /**
     * Listens to string removals in the document currently being filtered
     *
     * @param filterBypass   An object that allows access to the document without any call backs to the document.
     * @param offset         Offset in the document where removal of string occurs.
     * @param strLength      The length of the string removed.
     */
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
        } catch (BadLocationException badLocationException) {
            customTextFieldDocumentFilterLogger_.severe(badLocationException.getMessage());
        }
    }
    
    /**
     * Changes the color of string with given offset in the document and given length to the given color.
     *
     * @param doc          The document in which string color mutation is going to take place.
     * @param offset       Offset in the document where color mutation is going to take place.
     * @param strLength    The color that the string in the document is going to mutate to.
     */
    private void changeTextColor( StyledDocument doc, int offset, int stringLength, Color color ){
        
        if( doc != null && originalFontStyle_ != null && color != null && 
            offset >= MINIMUM_STRING_OFFSET && stringLength > EMPTY_STRING_LENGTH ){
            
            Color tempColor = StyleConstants.getForeground(originalFontStyle_);
            StyleConstants.setForeground(originalFontStyle_, color);
            doc.setCharacterAttributes(offset, stringLength, originalFontStyle_, true);
            StyleConstants.setForeground(originalFontStyle_, tempColor);
        }
    }
    
    /**
     * Resets the color of string with given offset in the document and given length to the document global font color.
     *
     * @param doc   The document in which string color mutation is going to take place.
     */
    public void resetTextColor( StyledDocument doc ){
        if( doc != null && originalFontStyle_ != null ){
            changeTextColor( doc, MINIMUM_STRING_OFFSET, doc.getLength(), StyleConstants.getForeground(originalFontStyle_) );
        }
    }
    
    /**
     * Parses the document for keywords to highlight provided that syntax coloring filter is turned on.
     *
     * @param doc   The document in which string color mutation is going to take place.
     */
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
                            if( doc.getLength() > EMPTY_STRING_LENGTH ){
                                String text = doc.getText( MINIMUM_STRING_OFFSET, doc.getLength() ).toLowerCase();
                                Matcher commandStrMatcher = commandPattern.matcher(text);
                                Matcher nonCommandStrMatcher = nonCommandPattern.matcher(text);
                                
                                int startIdx;
                                int endIdx;
                                String stringInFrontOfMatchedWord;
                                while( commandStrMatcher.find() ){
                                    startIdx = commandStrMatcher.start();
                                    endIdx = commandStrMatcher.end();
                                    
                                    stringInFrontOfMatchedWord = text.substring(MINIMUM_STRING_OFFSET, startIdx).trim();
                                    
                                    if( stringInFrontOfMatchedWord.length() <= EMPTY_STRING_LENGTH ){
                                        changeTextColor( doc, startIdx, endIdx-startIdx, COLOUR_COMMAND_KEYWORD );
                                        break;
                                    }
                                }
                                
                                while( nonCommandStrMatcher.find() ){
                                    startIdx = nonCommandStrMatcher.start();
                                    endIdx = nonCommandStrMatcher.end();
                                    
                                    if( startIdx - CHARACTER_LENGTH_IN_STRING < MINIMUM_STRING_OFFSET || 
                                            text.charAt(startIdx - CHARACTER_LENGTH_IN_STRING) != NONCOMMAND_KEYWORD_NULLIFIER ){
                                        
                                        changeTextColor( doc, startIdx, endIdx-startIdx, COLOUR_NONCOMMAND_KEYWORD );
                                    }
                                }
                            }
                        } catch( BadLocationException badLocationException ){
                            customTextFieldDocumentFilterLogger_.severe(badLocationException.getMessage());
                        } catch( IllegalStateException illegalStateException ){
                            customTextFieldDocumentFilterLogger_.severe(illegalStateException.getMessage());
                        } catch(PatternSyntaxException patternSyntaxException){
                            customTextFieldDocumentFilterLogger_.severe(patternSyntaxException.getMessage());
                        } catch(IndexOutOfBoundsException indexOutOfBoundsException){
                            customTextFieldDocumentFilterLogger_.severe(indexOutOfBoundsException.getMessage());
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Generates the regex pattern using the keywords provided.
     *
     * @param keywords   A string array containing keywords to be compiled into a regex pattern.
     * @return           A string representation of regex pattern complied from the keywords provided.
     */
    public String generateRegex( String []keywords ){
        
        try{
            StringBuilder regex = new StringBuilder(EMPTY_STRING);
            regex.append(REGEX_GROUP_START);
            if( keywords != null ){
                for( int i = 0, size = keywords.length; i < size; ++i ){
                    if( keywords[i] != null ){
                        regex.append(REGEX_WORD_BOUNDARY).append(keywords[i]).append(REGEX_WORD_BOUNDARY).append(REGEX_SEPARATOR);
                    }
                }
                if( regex.length() > MINIMUM_NON_EMPTY_STRING_LENGTH ){
                    regex.deleteCharAt(regex.length()-CHARACTER_LENGTH_IN_STRING);
                }
            }
            regex.append(REGEX_GROUP_END);
            return regex.toString();
        } catch( StringIndexOutOfBoundsException stringIndexOutOfBoundsException){
            customTextFieldDocumentFilterLogger_.severe(stringIndexOutOfBoundsException.getMessage());
        }
        return null;
    }
}
