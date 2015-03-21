package planner;

import java.awt.event.KeyEvent;

import planner.Constants.DISPLAY_STATE_FLAG;

public class DisplayState {

    private DISPLAY_STATE_FLAG displayStateFlag;
    
    private String title;
    private String command;
    
    private int keyCode;
    
    public DisplayState( DISPLAY_STATE_FLAG displayStateFlag, String title, String command, int keyCode ){
        
        this.displayStateFlag = displayStateFlag;
        
        if( title == null ){
            
            this.title = "";
            
        } else{
            
            this.title = title;
        }
        
        if( command == null ){
            
            this.command = "";
            
        } else{
            
            this.command = command;
        }
        
        this.keyCode = keyCode;
    }
    
    public DISPLAY_STATE_FLAG getdisplayStateFlag(){
        
        return displayStateFlag;
    }
    
    public String getTitle(){
        
        return title;
    }
    
    public String getCommand(){
        
        return command;
    }
    
    public int getKeyCode(){
        
        return keyCode;
    }
}
