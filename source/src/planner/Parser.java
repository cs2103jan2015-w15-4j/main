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

    private static String partlyParsedCmd = "";

    public static ParseResult parse(String command) {
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {
        partlyParsedCmd = command;
        COMMAND_TYPE commandType = extractCommandType(partlyParsedCmd);
        Date parsedDate = extractDate(partlyParsedCmd);
        Date dateToRemind = extractDateToRemind(partlyParsedCmd);
        int priorityLevel = extractPriorityLevel(partlyParsedCmd);
        long taskId = extractTaskId(partlyParsedCmd);
        String taskName = extractTaskName(partlyParsedCmd);
        String taskDescription = extractTaskDescription(partlyParsedCmd);
        String taskTag = extractTaskTag(partlyParsedCmd);
        ArrayList<Boolean> flagValues = checkFlagValues(partlyParsedCmd);

        return new ParseResult(commandType, parsedDate, dateToRemind,
                               priorityLevel, taskId, taskName,
                               taskDescription, taskTag, flagValues);
    }

    private static COMMAND_TYPE extractCommandType(String command) {
        return null;
    }

    private static Date extractDate(String command) {
        return new Date();
    }

    private static Date extractDateToRemind(String command) {
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
