//@author A0111333B

package planner;

import java.awt.Component;
import java.awt.event.KeyEvent;

import planner.Constants.DisplayStateFlag;

/**
* The DisplayState class is a container that stores command or key event used to invoke the display state
*
* @author A0111333B
*/
public class DisplayState {

    private DisplayStateFlag displayStateFlag;
    
    private String title;
    private String inputCommand;
    
    private KeyEvent keyEvent;
    
    /**
     * Constructs a DisplayState that stores the command or key event used to invoke the display state, the displayStateFlag and 
     * title related to the displayState. If userCommand and event are both not null values at the same time, only userCommand will
     * be recorded for this displayState. Although both userCommand and event can be null at the same time, it is generally discouraged because
     * this displayState will be regarded as an invalid state.
     *
     * @param currentDisplayStateFlag  An ENUM flag that describes the displayState
     * @param title                    The title shown for this displayState
     * @param userCommand              The user input string used to invoke this displayState
     * @param event                    The keyevent used to invoke this displayState
     */
    public DisplayState( DisplayStateFlag currentDisplayStateFlag, String title, String userCommand, KeyEvent event ){
        
        displayStateFlag = currentDisplayStateFlag;
        if( title == null ){
            this.title = "";
        } else{
            this.title = title;
        }
        
        inputCommand = null;
        keyEvent = null;
        
        if( userCommand!= null && userCommand.length() > 0 ){
            inputCommand = userCommand;
        } else if( event != null ){
            keyEvent = new KeyEvent( (Component)event.getSource(), event.getID(), 
                                       event.getWhen(), event.getModifiersEx(), 
                                       event.getKeyCode(), event.getKeyChar(), event.getKeyLocation() );
        }
    }
    
    /**
     * Returns the ENUM flag associated with this displayState
     *
     * @return The ENUM flag associated with this displayState  
     */
    public DisplayStateFlag getdisplayStateFlag(){
        return displayStateFlag;
    }
    
    /**
     * Returns the title shown during this displayState
     *
     * @return The title shown during this displayState  
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * Returns the user input string used to invoke this displayState
     *
     * @return The user input string used to invoke this displayState
     */
    public String getCommand(){
        return inputCommand;
    }
    
    /**
     * Returns the key event used to invoke this displayState
     *
     * @return The key event used to invoke this displayState
     */
    public KeyEvent getKeyEvent(){
        return keyEvent;
    }
    
    /**
     * Returns true if the title of the given anotherDisplayState equals to the title of this displayState, false otherwise.
     *
     * @param anotherDisplayState   The displayState in which its title field is to be compared with this displayState's title field
     * @return True if the title of the given anotherDisplayState equals to the title of this displayState, false otherwise.
     */
    public boolean compareTitle( DisplayState anotherDisplayState ){
        if( anotherDisplayState != null ){
            boolean isTitleEqual;
            if( anotherDisplayState.getTitle() != null && title != null ){
                isTitleEqual = title.equals(anotherDisplayState.getTitle());
            } else if( anotherDisplayState.getTitle() != null || title != null ){
                isTitleEqual = false;
            } else{
                isTitleEqual = true;
            }
            return isTitleEqual;
        }
        return false;
    }
    
    /**
     * Returns true if the user input string used to invoke the given anotherDisplayState equals to the user input string used to 
     * invoke this displayState, false otherwise.
     *
     * @param anotherDisplayState   The displayState in which its user input string used is to be compared with this displayState's user input string
     * @return True if the user input string used to invoke the given anotherDisplayState equals to the user input string used to 
     *         invoke this displayState, false otherwise.
     */
    public boolean compareCommand( DisplayState anotherDisplayState ){
        if( anotherDisplayState != null ){
            boolean isCommandEqual;
            if( anotherDisplayState.getCommand() != null && inputCommand != null ){
                isCommandEqual = inputCommand.equals(anotherDisplayState.getCommand());
            } else if( anotherDisplayState.getCommand() != null || inputCommand != null ){
                isCommandEqual = false;
            } else{
                isCommandEqual = true;
            }
            return isCommandEqual;
        }
        return false;
    }
    
    
    
    @Override
    public boolean equals( Object obj ){
        
        if( obj instanceof DisplayState ){
            
            DisplayState displayState = (DisplayState)obj;
            
            // Compare title
            boolean isTitleEqual;
            if( displayState.getTitle() != null && title != null ){
                
                isTitleEqual = title.equals(displayState.getTitle());
                
            } else if( displayState.getTitle() != null || title != null ){
                
                isTitleEqual = false;
                
            } else{
                
                isTitleEqual = true;
            }
            
            // Compare command
            boolean isCommandEqual;
            if( displayState.getCommand() != null && inputCommand != null ){
                
                isCommandEqual = inputCommand.equals(displayState.getCommand());
                
            } else if( displayState.getCommand() != null || inputCommand != null ){
                
                isCommandEqual = false;
                
            } else{
                
                isCommandEqual = true;
            }
            
            // Compare keyevent
            boolean isKeyEventEqual;
            if( displayState.getKeyEvent() != null && keyEvent != null ){
                
                isKeyEventEqual = keyEvent.equals(displayState.getKeyEvent());
                
            } else if( displayState.getKeyEvent() != null || keyEvent != null ){
                
                isKeyEventEqual = false;
                
            } else{
                
                isKeyEventEqual = true;
            }
            
            return ((displayState.getdisplayStateFlag() == displayStateFlag) &&
                    isTitleEqual && isCommandEqual && isKeyEventEqual);
            
        } else{
            
            return false;
        }
    }
}
