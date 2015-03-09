package planner;

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
        ADD, UPDATE, DELETE, SHOW, SHOW_ALL, SHOW_ONE, DONE, UNDO, SEARCH,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_SHOW, HELP_DONE,
        HELP_UNDO, HELP_SEARCH, INVALID
    };
    
    public static String CONFIG_FILE_LOCATION = "config";

    public static final int NO_PRIORITY_LEVEL = 0;
    public static final int NO_ID_SET = 0;
}
