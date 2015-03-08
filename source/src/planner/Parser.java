package planner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    // stores the arguments for each keyword
    private static String keywordArgs = "";
    private static String[] commandWords = null;
    private static String[] keywordsArray = {"at", "on", "by", "tomorrow",
        "every", "in", "priority", "desc", "description", "date", "due",
        "remind"
    };
    private static ArrayList<String> keywords =
            new ArrayList<String>(Arrays.asList(keywordsArray));

    // these fields will be used to construct the parseResult
    private static Date date = null;
    private static Date dateToRemind = null;
    private static int priorityLevel = 0;
    private static long id = 0;
    private static String name = "";
    private static String description = "";
    private static String tag = "";
    private static ArrayList<Boolean> flags = new ArrayList<Boolean>();

    public static ParseResult parse(String command) {
        resetFields();
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {
        commandWords = command.split(" ");
        COMMAND_TYPE commandType = extractCommandType(commandWords[0]);
        int indexBeingProcessed = 1; // next word after command type
        String wordBeingProcessed = "";
        String keywordBeingProcessed = ""; // to decide what to do with args
        switch(commandType) {
            case ADD:
                keywordBeingProcessed = "add";
                while(indexBeingProcessed < commandWords.length) {
                    wordBeingProcessed = commandWords[indexBeingProcessed];
                    if (isKeyword(wordBeingProcessed)) {
                        // process arguments of the previous command
                        processArgs(keywordBeingProcessed);
                        keywordArgs = "";
                    } else {
                        // add to arguments of previous command
                        keywordArgs += wordBeingProcessed;
                    }
                }
            case UPDATE:

            case DELETE:

            case SHOW:

            case DONE:

            case UNDO:

            case SEARCH:

            case HELP:

            default:

        }

        return new ParseResult(commandType, date, dateToRemind,
                               priorityLevel, id, name,
                               description, tag, flags);
    }

    private static COMMAND_TYPE extractCommandType(String commandWord) {
        switch(commandWord.toLowerCase()) {
            case "add":
            case "new":
                return COMMAND_TYPE.ADD;

            case "update":
            case "edit":
            case "change":
                return COMMAND_TYPE.UPDATE;

            case "delete":
            case "trash":
            case "remove":
            case "del":
                return COMMAND_TYPE.DELETE;

            case "show":
            case "display":
                return COMMAND_TYPE.SHOW;

            case "done":
            case "completed":
            case "finished":
                return COMMAND_TYPE.DONE;

            case "undo":
            case "revert":
                return COMMAND_TYPE.UNDO;

            case "search":
            case "find":
                return COMMAND_TYPE.SEARCH;

            case "help":
            case "sos":
                return COMMAND_TYPE.HELP;

            default:
                return COMMAND_TYPE.INVALID;

        }
    }

    private static void resetFields() {
        keywordArgs = "";
        commandWords = null;
        date = null;
        dateToRemind = null;
        priorityLevel = 0;
        id = 0;
        name = "";
        description = "";
        tag = "";
        flags = new ArrayList<Boolean>();
    }

    private static Boolean isKeyword(String word) {
        return keywords.contains(word);
    }

    private static void processArgs(String keyword) {
        switch(keyword) {
            // command keywords start here
            case "add":
            case "new":
                name = keywordArgs;
                break;

            case "update":
            case "edit":
            case "change":
                break;

            case "delete":
            case "trash":
            case "remove":
            case "del":
                break;

            case "show":
            case "display":
                break;

            case "done":
            case "completed":
            case "finished":
                break;

            case "undo":
            case "revert":
                break;

            case "search":
            case "find":
                break;

            case "help":
            case "sos":
                break;

            // non command keywords start here
            case "at":
            case "by":
            case "on":
            case "date":
            case "due":
                DateFormat format = new SimpleDateFormat("dd.MMMM.yyyy");
                try {
                    date = format.parse(keywordArgs);
                } catch (Exception e) {
                    date = new Date();
                }
                break;

            case "every":
                break;

            case "in":
                break;

            case "priority":
                priorityLevel = Integer.parseInt(keywordArgs);
                break;

            case "desc":
            case "description":
                description = keywordArgs;
                break;

            case "remind":
                DateFormat format2 = new SimpleDateFormat("dd.MMMM.yyyy");
                try {
                    dateToRemind = format2.parse(keywordArgs);
                } catch (Exception e) {
                    dateToRemind = new Date();
                }
                break;

            default:
                break;
        }
    }

    private static ArrayList<Boolean> checkFlagValues(String command) {
        ArrayList<Boolean> flagValues = new ArrayList<Boolean>();
        return flagValues;
    }

}
