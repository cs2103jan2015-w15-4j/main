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
}
