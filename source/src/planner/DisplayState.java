package planner;

import java.awt.event.KeyEvent;

import planner.Constants.DisplayStateFlag;

public class DisplayState {

    private DisplayStateFlag displayStateFlag;
    
    private String title;
    private String command;
    
    private int keyCode;
    
    public DisplayState( DisplayStateFlag displayStateFlag, String title, String command, int keyCode ){
        
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
    
    public DisplayStateFlag getdisplayStateFlag(){
        
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
