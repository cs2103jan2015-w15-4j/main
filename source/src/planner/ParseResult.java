package planner;

import java.util.Calendar;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;
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
    private String errorMessage = "";
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
                       String description, String tag, String errorMessage, 
                       boolean[] flags) {
        this.resultType = resultType;
        this.commandType = commandType;
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
        this.errorMessage = errorMessage;
        this.commandFlags = flags;
    }

    public RESULT_TYPE getResultType() {
        return resultType;
    }

    public COMMAND_TYPE getCommandType() {
        return commandType;
    }

    public Date getDate() {
        return parsedDate;
    }
    
    public Date getSecondDate() {
        return parsedDate2;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean[] getCommandFlags() {
        return commandFlags;
    }
}