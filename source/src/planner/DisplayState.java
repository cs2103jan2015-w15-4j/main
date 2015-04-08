//@author A0111333B

package planner;

import java.awt.Component;
import java.awt.event.KeyEvent;

import planner.Constants.DisplayStateFlag;

public class DisplayState {

    private DisplayStateFlag displayStateFlag;
    
    private String title;
    private String inputCommand;
    
    private KeyEvent keyEvent;
    
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
    
    public DisplayStateFlag getdisplayStateFlag(){
        
        return displayStateFlag;
    }
    
    public String getTitle(){
        
        return title;
    }
    
    public String getCommand(){
        
        return inputCommand;
    }
    
    public KeyEvent getKeyEvent(){
        
        return keyEvent;
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
