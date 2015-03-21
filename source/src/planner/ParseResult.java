package planner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;
import planner.Constants.ERROR_TYPE;
import planner.Constants.RESULT_TYPE;

/**
 * This class is used to deliver the results of  
 * parsing a user command to the engine.
 * 
 * @author Tham Zheng Yi
 */
public class ParseResult {

    private final int NO_PRIORITY_LEVEL = 0;
    private final int NO_ID_SET = 0;
    private final int COMMAND_FLAGS_MAX_SIZE = 7;
    
    private RESULT_TYPE resultType = null;
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
    //              description, tag
    private boolean[] commandFlags;
    
    /**
     * This method is the ParseResult constructor
     * 
     * @param commandType Type of the command e.g. search
     * @param time        Time/date parsed from command               
     * @param flags       Indicate presence of properties (e.g. time)
     */
	public ParseResult(RESULT_TYPE resultType, COMMAND_TYPE commandType,
                       Date date, Date date2, Date dateToRemind, 
                       int priorityLevel, long id, String name, 
                       String description, String tag, ERROR_TYPE errorType,
                       boolean[] flags) {

        this.resultType = resultType;
        this.commandType = commandType;
        
        if( date != null ){
            
            this.parsedDate = new Date(date.getTime());            // Changed to defensive copy
            
        } else{
            
            this.parsedDate = null;
        }
        
        if( dateToRemind != null ){
            
            this.dateToRemind = new Date(dateToRemind.getTime());  // Changed to defensive copy
            
        } else{
            
            this.dateToRemind = null;
        }
       
        if (date == null && commandType == COMMAND_TYPE.ADD) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(9999, 8, 9, 9, 9, 9);
            this.parsedDate = calendar.getTime();
        } else {
            this.parsedDate = date;
        }
        this.parsedDate2 = date2;
        this.dateToRemind = dateToRemind;
        this.priorityLevel = priorityLevel;
        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskTag = tag;
<<<<<<< HEAD
        this.errorType = errorType;
        this.commandFlags = flags;
=======
        this.errorMessage = errorMessage;
        
        if( flags != null ){
            
            this.commandFlags = Arrays.copyOf( flags, flags.length );   // changed to defensive copy
            
        } else{
            
            this.commandFlags = new boolean[COMMAND_FLAGS_MAX_SIZE];
        }
>>>>>>> a4ff94ef9bfebcbe950c1d7291df036a6dbd9b9f
    }

    public RESULT_TYPE getResultType() {
        return resultType;
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
