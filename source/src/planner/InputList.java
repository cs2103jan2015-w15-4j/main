package planner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class InputList {

    private LinkedList<String> list;
    private ListIterator<String> currentInput;
    private int maxListSize;
    
    private String recentlyTypedString;
    private String currentString;
    
    private boolean hasReturnedRecentlyTypedString;
    private boolean isNextIteratorCalled;
    private boolean isPrevIteratorCalled;
    
    public InputList( int maxListSize ){
        
        list = new LinkedList<String>();
        
        currentInput = null;
             
        this.maxListSize = Math.max( 1, maxListSize ); 
        
        hasReturnedRecentlyTypedString = false;
        isNextIteratorCalled = false;
        isPrevIteratorCalled = false;
    }
    
    public String addWordToList( String input ){
        
        if( input != null ){
            if( input.equals(currentString) ){
                return input;
            }
            while( list.size() >= maxListSize ){
                list.removeLast();
            }
            if( list.size() + 1 <= maxListSize ){
                list.addFirst(input);
            }
        }
        return input;
    }
    
    public void resetGetWordPosition(){
        
        recentlyTypedString = null;
        currentInput = list.listIterator();
        hasReturnedRecentlyTypedString = false;
        isNextIteratorCalled = false;
        isPrevIteratorCalled = false;
        if( !list.isEmpty() ){
            currentString = list.getFirst();
        } else{
            currentString = null;
        }
    }
    
    public String getNextInputString(){
        
        if( currentInput != null && currentInput.hasPrevious() ){
            
            if( isNextIteratorCalled ){
                isNextIteratorCalled = false;
                currentInput.previous();
            }
            String tempString = null;
            if( currentInput.hasPrevious() ){
                currentString = currentInput.previous();
                tempString = currentString;
                isPrevIteratorCalled = true;
            }
            return tempString;
        } else{
            isPrevIteratorCalled = false;
            if( !hasReturnedRecentlyTypedString ){  
                hasReturnedRecentlyTypedString = true;
                return currentString = recentlyTypedString;
            } else{
                return null;
            }
        }
    }
    
    public void setRecentlyTypedString( String input ){
        if( input != null && !input.equals(currentString) ){
            recentlyTypedString = input;
        }
    }
    
    public String getPrevInputString(){
        
        if( currentInput != null && currentInput.hasNext() ){
            
            hasReturnedRecentlyTypedString = false;
            if( isPrevIteratorCalled ){
                isPrevIteratorCalled = false;
                currentInput.next();
            }
            String tempString = null;
            if( currentInput.hasNext() ){
                currentString = currentInput.next();
                tempString = currentString;
                isNextIteratorCalled = true;
            }
            return tempString;
        } else{
            return null;
        }
    }
}
