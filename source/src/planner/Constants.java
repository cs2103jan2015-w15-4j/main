package planner;

/**
 * This class contains all the constants to be used across the planner.
 * 
 * @author kohwaikit
 *
 */
public class Constants {
    public enum RESULT_TYPE {
        SEARCH, UPDATE, DELETE
    };
    
    public enum COMMAND_TYPE {
        ADD, UPDATE, DELETE, SHOW, DONE, UNDO, SEARCH, HELP
    };
}
