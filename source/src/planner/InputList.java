package planner;

import java.util.LinkedList;
import java.util.ListIterator;

/**
* The InputList class handles the storage of previous commands and the logic of cycling through previous commands
*
* @author A0111333B
*/
public class InputList {

    private LinkedList<String> commandList_;
    private ListIterator<String> currentStringInput_;
    
    private int maxCommandListSize_;
    
    private String recentlyTypedCommand_;
    private String recentCommandReturned_;
    
    private boolean hasReturnedRecentlyTypedString_;
    private boolean isNextIteratorCalled_;
    private boolean isPrevIteratorCalled_;
    
    /**
     * Constructs a command input list that will handle the storage of previous commands and the logic of cycling through
     * previous commands. If maxListSize is < 1, the maximum number of previous commands strings that this list will store
     * will automatically be set to 1.
     *
     * @param maxListSize   The maximum number of previous commands strings that this list will store.
     */
    public InputList( int maxListSize ){
        
        commandList_ = new LinkedList<String>();
        currentStringInput_ = null;
        maxCommandListSize_ = Math.max( 1, maxListSize ); 
        hasReturnedRecentlyTypedString_ = false;
        isNextIteratorCalled_ = false;
        isPrevIteratorCalled_ = false;
    }
    
    /**
     * Adds a command string to the list. Null strings passed in will be ignored.
     *
     * @param input   The command input string to be added to the list.
     */
    public String addWordToList( String input ){
        
        if( input != null ){
            if( input.equals(recentCommandReturned_) ){
                return input;
            }
            while( commandList_.size() >= maxCommandListSize_ ){
                commandList_.removeLast();
            }
            if( commandList_.size() + 1 <= maxCommandListSize_ ){
                commandList_.addFirst(input);
            }
        }
        return input;
    }
    
    /**
     * Resets the position of the list pointer used to decide which command string is to be returned 
     * at the next getNextInputString() or getPrevInputString() call.
     */
    public void resetGetWordPosition(){
        
        recentlyTypedCommand_ = null;
        currentStringInput_ = commandList_.listIterator();
        hasReturnedRecentlyTypedString_ = false;
        isNextIteratorCalled_ = false;
        isPrevIteratorCalled_ = false;
        if( !commandList_.isEmpty() ){
            recentCommandReturned_ = commandList_.getFirst();
        } else{
            recentCommandReturned_ = null;
        }
    }
    
    /**
     * Returns the next command input string stored in the list. If there no more next command input strings to be returned,
     * the current user typed command string will be returned instead.
     *
     * @return The next command input string stored in the list.
     */
    public String getNextInputString(){
        
        if( currentStringInput_ != null && currentStringInput_.hasPrevious() ){
            
            if( isNextIteratorCalled_ ){
                isNextIteratorCalled_ = false;
                currentStringInput_.previous();
            }
            String tempString = null;
            if( currentStringInput_.hasPrevious() ){
                recentCommandReturned_ = currentStringInput_.previous();
                tempString = recentCommandReturned_;
                isPrevIteratorCalled_ = true;
            }
            return tempString;
        } else{
            isPrevIteratorCalled_ = false;
            if( !hasReturnedRecentlyTypedString_ ){  
                hasReturnedRecentlyTypedString_ = true;
                return recentCommandReturned_ = recentlyTypedCommand_;
            } else{
                return null;
            }
        }
    }
    
    /**
     * Records the current user typed command string that is not yet processed by YOPO. 
     *
     * @param The current user typed command string that is not yet processed by YOPO.
     */
    public void setRecentlyTypedString( String input ){
        if( input != null && !input.equals(recentCommandReturned_) ){
            recentlyTypedCommand_ = input;
        }
    }
    
    /**
     * Returns the previous command input string stored in the list. If there no more previous command input strings 
     * to be returned, a null string will be returned
     *
     * @return The previous command input string stored in the list.
     */
    public String getPrevInputString(){
        
        if( currentStringInput_ != null && currentStringInput_.hasNext() ){
            
            hasReturnedRecentlyTypedString_ = false;
            if( isPrevIteratorCalled_ ){
                isPrevIteratorCalled_ = false;
                currentStringInput_.next();
            }
            String tempString = null;
            if( currentStringInput_.hasNext() ){
                recentCommandReturned_ = currentStringInput_.next();
                tempString = recentCommandReturned_;
                isNextIteratorCalled_ = true;
            }
            return tempString;
        } else{
            return null;
        }
    }
}
