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
    public ParseResult parse(String command) {
        ParseResult result = process(command);
        return result;
    }    
    
    private ParseResult process(String command) {
        COMMAND_TYPE commandType = extractCommandType(command);
        ArrayList<Boolean> flagValues = checkFlagValues(command);
        Date currentTime = new Date();
        return new ParseResult(commandType, currentTime, flagValues);
    }
    
    private ArrayList<Boolean> checkFlagValues(String command) {
        ArrayList<Boolean> flagValues = new ArrayList<Boolean>();
        return flagValues;
    }
    
    private COMMAND_TYPE extractCommandType(String command) {
        return null;
    }
}
