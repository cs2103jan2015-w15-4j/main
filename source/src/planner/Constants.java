package planner;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * This class contains all the constants to be used across the planner.
 * 
 * @author kohwaikit
 *
 */
public class Constants {
    public enum RESULT_TYPE {
        VALID, INVALID
    };
    
    public enum COMMAND_TYPE {
        ADD, UPDATE, DELETE, SHOW, SHOW_ALL, SHOW_ONE, DONE, SETNOTDONE, UNDO, SEARCH,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_SHOW, HELP_DONE,
        HELP_UNDO, HELP_SEARCH, JUMP, CONVERT, CONVERT_FLOATING, 
        CONVERT_DEADLINE, CONVERT_TIMED, INVALID
    };
    
    public enum ERROR_TYPE {
        INVALID_COMMAND, INVALID_TASK_ID, BLANK_TASK_NAME, INVALID_DATE,
        INVALID_ARGUMENTS
    };
    
    public enum TIP_TYPE{
        UP_TIP, DOWN_TIP, UPDOWN_TIP
    }
    
    public enum DISPLAY_STATE_FLAG{
        ALL, TODAY, TENTATIVE, OVERDUE, RECURRING, DONE, WORD_SEARCH, DATE_SEARCH, INVALID
    }
    
    public static String[] NAVIGATION_BAR_STRING_CONTENTS = { "More info for task #",
                                                              "Tutorial",
                                                              " Quick key used",
                                                              " Quick keys used",
                                                              " Task due today",
                                                              " Tasks due today",
                                                              " Task in total",
                                                              " Tasks in total",
                                                              " Tentative Task",
                                                              " Tentative Tasks",
                                                              " Overdue Task",
                                                              " Overdue Tasks",
                                                              " Recurring Task",
                                                              " Recurring Tasks",
                                                              " Task Done",
                                                              " Tasks Done"};
    
    public static String CONFIG_FILE_LOCATION = "config";

    public static final int NO_PRIORITY_LEVEL = 0;
    public static final int NO_ID_SET = 0;
    public static final int NAVIGATION_BAR_STRING_CONTENTS_SIZE = 16;
    public static final int NAVIGATION_BARS_MAX_NUM = 10;
    
    EnumMap<ERROR_TYPE, String> errorMessages = new EnumMap<ERROR_TYPE, String>(ERROR_TYPE.class);
    
    public void initializeErrorMessages() {
        errorMessages.put(ERROR_TYPE.INVALID_COMMAND, "invalid command type");
        errorMessages.put(ERROR_TYPE.INVALID_TASK_ID, "a number must be entered for the task id");
        errorMessages.put(ERROR_TYPE.BLANK_TASK_NAME, "the name of the task added cannot be blank");
        errorMessages.put(ERROR_TYPE.INVALID_DATE, "Unable to parse date");
    }
}
