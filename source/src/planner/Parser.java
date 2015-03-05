package planner;

import java.util.ArrayList;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;

/**
 * This class contains all the logic for parsing
 * user commands into useful information for
 * the engine.
 * 
 * @author Tham Zheng Yi
 */
public class Parser {
    public static ParseResult parse(String command) {
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {
        COMMAND_TYPE commandType = extractCommandType(command);
        Date parsedTime = extractDate(command);
        int priorityLevel = extractPriorityLevel(command);
        long taskId = extractTaskId(command);
        String taskName = extractTaskName(command);
        String taskDescription = extractTaskDescription(command);
        String taskTag = extractTaskTag(command);
        ArrayList<Boolean> flagValues = checkFlagValues(command);

        return new ParseResult(commandType, parsedTime, priorityLevel,
                               taskId, taskName, taskDescription, taskTag,
                               flagValues);
    }

    private static COMMAND_TYPE extractCommandType(String command) {
        return null;
    }

    private static Date extractDate(String command) {
        return new Date();
    }

    private static int extractPriorityLevel(String command) {
        return 0;
    }

    private static long extractTaskId(String command) {
        return 0;
    }

    private static String extractTaskName(String command) {
        return "";
    }

    private static String extractTaskDescription(String command) {
        return "";
    }

    private static String extractTaskTag(String command) {
        return "";
    }

    private static ArrayList<Boolean> checkFlagValues(String command) {
        ArrayList<Boolean> flagValues = new ArrayList<Boolean>();
        return flagValues;
    }

}
