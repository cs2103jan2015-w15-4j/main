//@author A0111333B

package planner;

import java.util.LinkedList;

public class DisplayStateStack {

    private int maxNumOfStates;
    private LinkedList<DisplayState> stack;
    
    public DisplayStateStack( int maxNumOfDisplayStates ){
        
        maxNumOfStates = Math.max( 1, maxNumOfDisplayStates );
        
        stack = new LinkedList<DisplayState>();
    }
    
    public DisplayState push( DisplayState displayState ){
        
        if( displayState != null && (displayState.getKeyEvent() != null || displayState.getCommand() != null) ){
            
            while( stack.size() >= maxNumOfStates ){
                
                stack.removeLast();
            }
            
            if( stack.size() + 1 <= maxNumOfStates ){
                
                stack.push(displayState);
            }
        }
        
        return displayState;
    }
    
    public DisplayState pop(){
        
        if( !stack.isEmpty() ){
            
            return stack.pop();
            
        } else{
            
            return null;
        }
    }
    
    public boolean isEmpty(){
        
        return stack.isEmpty();
    }
    
    public DisplayState peek(){
        
        return stack.peek();
    }
    
    public int size(){
        
        return stack.size();
    }
}
