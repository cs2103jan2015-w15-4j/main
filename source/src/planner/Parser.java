package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import planner.Constants.COMMAND_TYPE;
import planner.Constants.RESULT_TYPE;

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
    private static String[] monthsArray = {"Jan", "January", "Feb", "February",
        "Mar", "March", "Apr", "April", "May", "May", "Jun", "June", "Jul",
        "July", "Aug", "August", "Sep", "September", "Oct", "October", "Nov",
        "November", "Dec", "December"
    };
    private static ArrayList<String> months =
            new ArrayList<String>(Arrays.asList(monthsArray));

    // these fields will be used to construct the parseResult
    private static RESULT_TYPE resultType = RESULT_TYPE.VALID;
    private static COMMAND_TYPE commandType = null;
    private static Date date = null;
    private static Date dateToRemind = null;
    private static int priorityLevel = 0;
    private static long id = 0;
    private static String name = "";
    private static String description = "";
    private static String tag = "";
    private static String errorMessage = "";
    private static boolean[] flags = new boolean[7];
    private static Calendar calendar = null;

    public static ParseResult parse(String command) {
        resetFields();
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {
        commandWords = command.split(" ");
        commandType = extractCommandType(commandWords[0]);
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
                        keywordBeingProcessed = wordBeingProcessed;
                    } else {
                        // add to arguments of previous command
                        keywordArgs += wordBeingProcessed + " ";
                    }
                    indexBeingProcessed++;

                }
                processArgs(keywordBeingProcessed);
                if (name.equals("")) {
                    resultType = RESULT_TYPE.INVALID;
                    errorMessage = "the name of the task added cannot be blank";
                }
                flags = updateResultFlags(flags);

            case UPDATE:

            case DELETE:

            case SHOW:

            case DONE:

            case UNDO:

            case SEARCH:

            case HELP:

            default:

        }
        return parseResult(resultType, commandType);
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
        resultType = RESULT_TYPE.VALID;
        commandType = null;
        keywordArgs = "";
        commandWords = null;
        date = null;
        dateToRemind = null;
        priorityLevel = 0;
        id = 0;
        name = "";
        description = "";
        tag = "";
        errorMessage = "";
        flags = new boolean[7];
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
                calendar = parseDate(keywordArgs);
                date = calendar.getTime();
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
                calendar = parseDate(keywordArgs);
                dateToRemind = calendar.getTime();
                break;

            default:
                break;
        }
    }

    private static boolean[] updateResultFlags(boolean[] flags) {
        // flags order: date, dateToRemind, priorityLevel, id, name,
        // description, tag
        if (date != null) {
            flags[0] = true;
        }
        if (dateToRemind != null) {
            flags[1] = true;
        }
        if (priorityLevel != 0) {
            flags[2] = true;
        }
        if (id != 0) {
            flags[3] = true;
        }
        if (!name.equals("")) {
            flags[4] = true;
        }
        if (!description.equals("")) {
            flags[5] = true;
        }
        if (!tag.equals("")) {
            flags[6] = true;
        }
        return flags;
    }

    // constructs and returns result based on existing fields
    private static ParseResult parseResult(RESULT_TYPE resultType,
                                           COMMAND_TYPE commandType) {
        return new ParseResult(resultType, commandType, date, dateToRemind,
                               priorityLevel, id, name, description, tag,
                               errorMessage, flags);
    }

    private static Calendar parseDate(String arguments) {
        int day = 0;
        int month = 0;
        int year = 0;
        String[] dateParts = arguments.split(" ");
        String expectedDay = dateParts[0];
        String expectedMonth = dateParts[0];
        String expectedYear = dateParts[0];

        try {
            day = Integer.parseInt(expectedDay);
        } catch (NumberFormatException e) {
            resultType = Constants.RESULT_TYPE.INVALID;
            errorMessage = "Unable to parse date";
        }

        try {
            month = Integer.parseInt(expectedMonth);
        } catch (NumberFormatException e) {
            int monthIndex = months.indexOf(expectedMonth);
            // check whether it is found in the list of month strings
            if (monthIndex == -1) {
                resultType = Constants.RESULT_TYPE.INVALID;
                errorMessage = "Unable to parse date";
            } else {
                month = (monthIndex / 2) + 1;
            }

        }

        try {
            year = Integer.parseInt(expectedYear);
        } catch (NumberFormatException e) {
            resultType = Constants.RESULT_TYPE.INVALID;
            errorMessage = "Unable to parse date";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar;
    }

}
