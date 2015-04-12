//@author A0111333B

package planner;

import java.util.LinkedList;

/**
* The DisplayStateStack class is a customised stack works like a normal stack but ignores invalid DisplayStates and limits the 
* number of DisplayStates that exist within the stack by removing some of the oldest DisplayStates that exist on the stack
*
* @author A0111333B
*/
public class DisplayStateStack {

    private int maxNumOfStates_;
    private LinkedList<DisplayState> stack_;
    
    /**
    * Constructs a DisplayState stack used for storing DisplayStates in chronological order. If maxNumOfDisplayStates is passed
    * as an integer less than 1, the DisplayStateStack automatically sets the maximum number of DisplayStates that can be stored
    * within this stack to be 1
    *
    * @param maxNumOfDisplayStates  The maximum number of DisplayStates that can be stored within this stack
    */
    public DisplayStateStack( int maxNumOfDisplayStates ){
        maxNumOfStates_ = Math.max( 1, maxNumOfDisplayStates );
        stack_ = new LinkedList<DisplayState>();
    }
    
    /**
     * Pushes the provided displayState onto the stack. This method will not add any invalid displayStates and null displayStates.
     *
     * @param displayState  The displayState to be pushed onto the stack
     * @return              The displayState provided
     */
    public DisplayState push( DisplayState displayState ){
        if( displayState != null && (displayState.getKeyEvent() != null || displayState.getCommand() != null) ){
            while( stack_.size() >= maxNumOfStates_ ){
                stack_.removeLast();
            }
            if( stack_.size() + 1 <= maxNumOfStates_ ){
                stack_.push(displayState);
            }
        }
        return displayState;
    }
    
    /**
     * Removes and returns the most recent displayState pushed onto the stack.
     *
     * @return   The most recent displayState pushed onto the stack.
     */
    public DisplayState pop(){
        if( !stack_.isEmpty() ){
            return stack_.pop();
        } else{
            return null;
        }
    }
    
    /**
     * Returns true if the stack currently contains no displayStates, false otherwise
     *
     * @return   True if the stack currently contains no displayStates, false otherwise
     */
    public boolean isEmpty(){
        return stack_.isEmpty();
    }
    
    /**
     * Returns the most recent displayState pushed onto the stack.
     *
     * @return   The most recent displayState pushed onto the stack.
     */
    public DisplayState peek(){
        return stack_.peek();
    }
    
    /**
     * Returns the number of displayStates currently present within the stack.
     *
     * @return   The number of displayStates currently present within the stack.
     */
    public int size(){
        return stack_.size();
    }
}
