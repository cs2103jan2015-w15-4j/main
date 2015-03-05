package planner;

import java.util.ArrayList;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;

/**
 * This class is used to deliver the results of  
 * parsing a user command to the engine.
 * 
 * @author Tham Zheng Yi
 */
public class ParseResult {
    private COMMAND_TYPE commandType = null;
    private Date dateGiven = new Date();
    private int priorityLevel = 0;
    ArrayList<Boolean> commandFlags = new ArrayList<Boolean>();
    
    /**
     * This method is the ParseResult constructor
     * 
     * @param commandType Type of the command e.g. search
     * @param time        Time/date parsed from command               
     * @param flags       Booleans that indicate presence of properties (e.g. time)
     */
    public ParseResult(COMMAND_TYPE commandType, Date time, ArrayList<Boolean> flags) {
        this.commandType = commandType;
        this.dateGiven = time;
        this.commandFlags = flags;
    }
    
    public void setCommandType(COMMAND_TYPE desiredType) {
        commandType = desiredType;
    }
    
    public COMMAND_TYPE getCommandType() {
        return commandType;
    }
}