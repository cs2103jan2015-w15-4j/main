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
    private Date parsedDate = new Date();
    private int priorityLevel = Constants.NO_PRIORITY_LEVEL;
    private long taskId = Constants.NO_ID_SET;
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
    public ParseResult(COMMAND_TYPE commandType, Date date, int priorityLevel,
                       long id, String name, String description, String tag,
                       ArrayList<Boolean> flags) {
        this.commandType = commandType;
        this.parsedDate = date;
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
    
    public Date getTime() {
        return parsedDate;
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