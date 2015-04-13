//@author A0108232U
package planner;

import java.util.Arrays;
import java.util.Date;

import planner.Constants.CommandType;
import planner.Constants.ErrorType;

/**
 * This class is used to deliver the results of  
 * parsing a user command to the engine. 
 */
public class ParseResult {

    private final int NO_PRIORITY_LEVEL = 0;
    private final int NO_ID_SET = 0;
    private final int COMMAND_FLAGS_MAX_SIZE = 8;    

    private CommandType commandType = null;
    private Date parsedDate = null;
    private Date parsedDate2 = null;
    private Date dateToRemind = null;
    private int priorityLevel = NO_PRIORITY_LEVEL;
    private long taskId = NO_ID_SET;
    private String taskName = "";
    private String taskDescription = "";
    private String taskTag = "";
    private ErrorType errorType = null;
    // flags order: date, dateToRemind, priorityLevel, id, name,
    //              description, tag, date2
    private boolean[] commandFlags;
    
    /**
     * This method is the ParseResult constructor.
     * 
     * @param commandType Type of the command e.g. search.
     * @param time        Time/date parsed from command.               
     * @param flags       Indicate presence of properties (e.g. time).
     */
	public ParseResult(CommandType commandType,
                       Date date, Date date2, Date dateToRemind, 
                       int priorityLevel, long id, String name, 
                       String description, String tag, ErrorType errorType,
                       boolean[] flags) {

        this.commandType = commandType;
        
        if (date != null) {            
            this.parsedDate = new Date(date.getTime());                   
        } else {            
            this.parsedDate = null;
        }
        
        if (dateToRemind != null) {            
            this.dateToRemind = new Date(dateToRemind.getTime());           
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
            this.commandFlags = Arrays.copyOf(flags, flags.length);       
        } else {            
            this.commandFlags = new boolean[COMMAND_FLAGS_MAX_SIZE];
        }
    }
	
	/**
	 * Gets the type of command the user input
	 * 
	 * @return Command type.
	 */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Gets the first date field the user input.
     * 
     * @return First date field.
     */
    public Date getDate() {
        if (parsedDate != null) {
            return new Date(parsedDate.getTime());
        } else {
            return null;
        }
    }
    
    /**
     * Gets the second date field the user input.
     * 
     * @return Second date field.
     */
    public Date getSecondDate() {
        return parsedDate2;
    }
    
    /**
     * Gets the date by which the user wishes to be reminded.
     * 
     * @return Reminder date.
     */
    public Date getDateToRemind() {     
        if (dateToRemind != null) {
            return new Date(dateToRemind.getTime());
        } else {
            return null;
        }
    }

    /**
     * Gets the priority level the user input.
     * 
     * @return Priority level.
     */
    public int getPriorityLevel() {
        return priorityLevel;
    }

    /**
     * Gets the task id the user input.
     * 
     * @return Task id.
     */
    public long getId() {
        return taskId;
    }

    /**
     * Gets the task name the user input.
     * 
     * @return Task name.
     */
    public String getName() {
        return taskName;
    }

    /**
     * Gets the task description the user input.
     * 
     * @return Task description.
     */
    public String getDescription() {
        return taskDescription;
    }

    /**
     * Gets the task tag the user input.
     * 
     * @return Task tag.
     */
    public String getTag() {
        return taskTag;
    }

    /**
     * Gets the error from parsing the user command.
     * 
     * @return Error type.
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Gets the flags that represent which fields in the result contain valid
     * values.
     * 
     * @return Field validity flags.
     */
    public boolean[] getCommandFlags() {
        if (commandFlags != null) {
            return Arrays.copyOf(commandFlags, commandFlags.length);
        } else {
            return new boolean[COMMAND_FLAGS_MAX_SIZE];
        }
    }
}
