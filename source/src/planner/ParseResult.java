package planner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;
import planner.Constants.ERROR_TYPE;

/**
 * This class is used to deliver the results of  
 * parsing a user command to the engine.
 * 
 * @author Tham Zheng Yi
 */
public class ParseResult {

    private final int NO_PRIORITY_LEVEL = 0;
    private final int NO_ID_SET = 0;
    private final int COMMAND_FLAGS_MAX_SIZE = 8;    

    private COMMAND_TYPE commandType = null;
    private Date parsedDate = null;
    private Date parsedDate2 = null;
    private Date dateToRemind = null;
    private int priorityLevel = NO_PRIORITY_LEVEL;
    private long taskId = NO_ID_SET;
    private String taskName = "";
    private String taskDescription = "";
    private String taskTag = "";
    private ERROR_TYPE errorType = null;
    // flags order: date, dateToRemind, priorityLevel, id, name,
    //              description, tag, date2
    private boolean[] commandFlags;
    
    /**
     * This method is the ParseResult constructor
     * 
     * @param commandType Type of the command e.g. search
     * @param time        Time/date parsed from command               
     * @param flags       Indicate presence of properties (e.g. time)
     */
	public ParseResult(COMMAND_TYPE commandType,
                       Date date, Date date2, Date dateToRemind, 
                       int priorityLevel, long id, String name, 
                       String description, String tag, ERROR_TYPE errorType,
                       boolean[] flags) {

        this.commandType = commandType;
        
        if (date != null) {            
            this.parsedDate = new Date(date.getTime());            // Changed to defensive copy            
        } else {            
            this.parsedDate = null;
        }
        
        if (date2 != null) {            
            this.parsedDate = new Date(date.getTime());            // Changed to defensive copy            
        } else {            
            this.parsedDate = null;
        }
        
        if (dateToRemind != null) {            
            this.dateToRemind = new Date(dateToRemind.getTime());  // Changed to defensive copy            
        } else {            
            this.dateToRemind = null;
        }       
        
        this.parsedDate2 = date2;
        this.dateToRemind = dateToRemind;
        this.priorityLevel = priorityLevel;
        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskTag = tag;
        this.errorType = errorType;
        this.commandFlags = flags;
        
        if(flags != null) {            
            this.commandFlags = Arrays.copyOf( flags, flags.length );   // changed to defensive copy            
        } else {            
            this.commandFlags = new boolean[COMMAND_FLAGS_MAX_SIZE];
        }
    }

    public COMMAND_TYPE getCommandType() {
        return commandType;
    }

    public Date getDate() {        
        return parsedDate != null ? new Date(parsedDate.getTime()) : null;    // Changed to defensive copy
    }
    
    public Date getSecondDate() {
        return parsedDate2;
    }
    
    public Date getDateToRemind() {        
        return dateToRemind != null ? new Date(dateToRemind.getTime()) : null;    // Changed to defensive copy
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public long getId() {
        return taskId;
    }

    public String getName() {
        return taskName;
    }

    public String getDescription() {
        return taskDescription;
    }

    public String getTag() {
        return taskTag;
    }

    public ERROR_TYPE getErrorType() {
        return errorType;
    }

    public boolean[] getCommandFlags() {        
        return commandFlags != null ? Arrays.copyOf( commandFlags, commandFlags.length ) : new boolean[COMMAND_FLAGS_MAX_SIZE];    // Changed to defensive copy
    }
}
