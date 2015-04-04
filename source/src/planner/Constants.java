package planner;

import java.awt.Color;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * This class contains all the constants to be used across the planner.
 * 
 * @author kohwaikit
 *
 */
public class Constants {
    
    public enum CommandType {
        ADD, ADD_CLASH, UPDATE, DELETE, SHOW, SHOW_ALL, SHOW_ONE, DONE, SETNOTDONE, UNDO, SEARCH,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_SHOW, HELP_DONE,
        HELP_UNDO, HELP_SEARCH, JUMP, CONVERT, CONVERT_FLOATING, 
        CONVERT_DEADLINE, CONVERT_TIMED, SAVEWHERE, SAVEHERE, INVALID
    };
    
    public enum ErrorType {
        INVALID_COMMAND, INVALID_TASK_ID, BLANK_TASK_NAME, INVALID_DATE,
        INVALID_ARGUMENTS, INVALID_TIME, DATE1_NOT_SMALLER_THAN_DATE2,
        INVALID_PRIORITY_LEVEL, TASK_NOT_FOUND, CONVERT_TASK_TYPE_IN_UPDATE,
        UPDATE_NO_CHANGES, NOTHING_TO_UNDO, NO_INPUT
    };
    
    public enum TipType{
        UP_TIP, DOWN_TIP, UPDOWN_TIP
    }
    
    public enum DisplayStateFlag {
        ALL, TODAY, TENTATIVE, OVERDUE, RECURRING, DONE, WORD_SEARCH, PRIORITY_SEARCH, INVALID, SETTINGS
    }
    
    public static final String[] COMMAND_KEYWORDS = {"add", "new", "update", "edit", 
        "change", "del", "delete", "trash", "remove", "show", "display", "done", 
        "completed", "finished", "setnotdone", "undo", "revert", "search", 
        "find", "help", "sos", "jump", "convert", "savewhere", "savehere" };
    
    public static final String[] NONCOMMAND_KEYWORDS = { "at",
        "on", "date", "from", "by", "due", "until", "to", "jump", "every", "in",
        "priority", "desc", "description", "remind", "tag" };
    
    public static final String [][] POSSIBLE_COMMANDS = { {"Add tasks", "add taskname at date", "new taskname at date"},
                                                          { "Delete tasks", "delete taskid", "del taskid"},
                                                          { "Update tasks", "update taskid date" }
                                                        };
    
    public static String[] NAVIGATION_BAR_STRING_CONTENTS = { "More info for task #",           // F1
                                                              "Previous View",                  // F2
                                                              "Tutorial",                       // F3
                                                              " Task due today",                // F4
                                                              " Tasks due today",
                                                              " Task in total",                 // F5
                                                              " Tasks in total",
                                                              " Tentative Task",                // F6
                                                              " Tentative Tasks",
                                                              " Overdue Task",                  // F7
                                                              " Overdue Tasks",
                                                              " Task Done",                     // F8
                                                              " Tasks Done"};                   // F9
    
    public static Color[] COLOR_SERIES = { new Color( 239, 52, 65 ),
                                           new Color( 244, 132, 57 ),
                                           new Color( 224, 226, 54 ),
                                           new Color( 47, 244, 237 ),
                                           new Color( 74, 243, 110 ),
                                           new Color( 242, 141, 236 ),
                                           new Color( 245, 142, 142 ) };
    
    public static final String CONFIG_FILE_NAME = "config";
    public static final String DEFAULT_STORAGE_NAME = "data";

    public static final int NO_PRIORITY_LEVEL = 0;
    public static final int NO_ID_SET = 0;
    public static final int NAVIGATION_BAR_STRING_CONTENTS_SIZE = 13;
    public static final int NAVIGATION_BARS_MAX_NUM = 10;
    
    EnumMap<ErrorType, String> errorMessages = new EnumMap<ErrorType, String>(ErrorType.class);
    
    public void initializeErrorMessages() {
        errorMessages.put(ErrorType.INVALID_COMMAND, "invalid command type");
        errorMessages.put(ErrorType.INVALID_TASK_ID, "a number must be entered for the task id");
        errorMessages.put(ErrorType.BLANK_TASK_NAME, "the name of the task added cannot be blank");
        errorMessages.put(ErrorType.INVALID_DATE, "Unable to parse date");
        errorMessages.put(ErrorType.INVALID_TIME, "Unable to parse time");
        errorMessages.put(ErrorType.INVALID_PRIORITY_LEVEL, "Please input a priority level from 1 to 5");
        
        //Errors from Engine
        errorMessages.put(ErrorType.TASK_NOT_FOUND, "Task not found!");
        errorMessages.put(ErrorType.CONVERT_TASK_TYPE_IN_UPDATE, "Conversion of"
                + " Task type not allowed in update. Use the convert command");
        errorMessages.put(ErrorType.UPDATE_NO_CHANGES, "No changes in"
                + " update command!");
        errorMessages.put(ErrorType.NOTHING_TO_UNDO, "No actions to undo!");
        errorMessages.put(ErrorType.NO_INPUT, "Please enter a command.");
    }
}
