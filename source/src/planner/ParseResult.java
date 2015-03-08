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

    private final int NO_PRIORITY_LEVEL = 0;
    private final int NO_ID_SET = 0;

    private COMMAND_TYPE commandType = null;
    private Date parsedDate = new Date();
    private Date dateToRemind = new Date();
    private int priorityLevel = NO_PRIORITY_LEVEL;
    private long taskId = NO_ID_SET;
    private String taskName = "";
    private String taskDescription = "";
    private String taskTag = "";
    private ArrayList<Boolean> commandFlags = new ArrayList<Boolean>();
    
    /**
     * This method is the ParseResult constructor
     * 
     * @param commandType Type of the command e.g. search
     * @param time        Time/date parsed from command               
     * @param flags       Indicate presence of properties (e.g. time)
     */
    public ParseResult(COMMAND_TYPE commandType, Date date, Date dateToRemind,
                       int priorityLevel, long id, String name,
                       String description, String tag,
                       ArrayList<Boolean> flags) {
        this.commandType = commandType;
        this.parsedDate = date;
        this.dateToRemind = dateToRemind;
        this.priorityLevel = priorityLevel;
        this.taskId = id;
        this.taskName = name;
        this.taskDescription = description;
        this.taskTag = tag;
        this.commandFlags = flags;
    }

    public COMMAND_TYPE getCommandType() {
        return commandType;
    }
    
    public Date getDate() {
        return parsedDate;
    }
    
    public Date getDateToRemind() {
        return dateToRemind;
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

    public ArrayList<Boolean> getCommandFlags() {
        return commandFlags;
    }
}