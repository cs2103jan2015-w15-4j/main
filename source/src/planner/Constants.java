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
        ADD, UPDATE, DELETE, SHOW, SHOW_ALL, SHOW_ONE, DONE, SETNOTDONE, UNDO, SEARCH,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_SHOW, HELP_DONE,
        HELP_UNDO, HELP_SEARCH, JUMP, CONVERT, CONVERT_FLOATING, 
        CONVERT_DEADLINE, CONVERT_TIMED, SAVEWHERE, SAVEHERE, INVALID
    };
    
    public enum ErrorType {
        INVALID_COMMAND, INVALID_TASK_ID, BLANK_TASK_NAME, INVALID_DATE,
        INVALID_ARGUMENTS, INVALID_TIME, DATE1_NOT_SMALLER_THAN_DATE2,
    };
    
    public enum TipType{
        UP_TIP, DOWN_TIP, UPDOWN_TIP
    }
    
    public enum DisplayStateFlag {
        ALL, TODAY, TENTATIVE, OVERDUE, RECURRING, DONE, WORD_SEARCH, DATE_SEARCH, INVALID, SETTINGS
    }
    
    public static final String[] COMMAND_KEYWORDS = {"add", "new", "update", "edit", 
        "change", "del", "delete", "trash", "remove", "show", "display", "done", 
        "completed", "finished", "setnotdone", "undo", "revert", "search", 
        "find", "help", "sos", "jump", "convert", "savewhere", "savehere" };
    
    public static final String[] NONCOMMAND_KEYWORDS = { "at",
        "on", "date", "from", "by", "due", "until", "to", "jump", "every", "in",
        "priority", "desc", "description", "remind", "tag" };
    
    public static String[] NAVIGATION_BAR_STRING_CONTENTS = { "More info for task #",           // F1
                                                              "Previous View",                  // F2
                                                              "Tutorial",                       // F3
                                                              " Quick key used",                // F4
                                                              " Task due today",                // F5
                                                              " Tasks due today",
                                                              " Task in total",                 // F6
                                                              " Tasks in total",
                                                              " Tentative Task",                // F7
                                                              " Tentative Tasks",
                                                              " Overdue Task",                  // F8
                                                              " Overdue Tasks",
                                                              " Task Done",                     // F9
                                                              " Tasks Done",
                                                              "YOPO's Settings"};               // F10
    
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
    public static final int NAVIGATION_BAR_STRING_CONTENTS_SIZE = 16;
    public static final int NAVIGATION_BARS_MAX_NUM = 10;
    
    EnumMap<ErrorType, String> errorMessages = new EnumMap<ErrorType, String>(ErrorType.class);
    
    public void initializeErrorMessages() {
        errorMessages.put(ErrorType.INVALID_COMMAND, "invalid command type");
        errorMessages.put(ErrorType.INVALID_TASK_ID, "a number must be entered for the task id");
        errorMessages.put(ErrorType.BLANK_TASK_NAME, "the name of the task added cannot be blank");
        errorMessages.put(ErrorType.INVALID_DATE, "Unable to parse date");
        errorMessages.put(ErrorType.INVALID_TIME, "Unable to parse time");
    }
}
