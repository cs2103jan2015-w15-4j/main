package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import planner.Constants.COMMAND_TYPE;
import planner.Constants.ERROR_TYPE;
import planner.Constants.RESULT_TYPE;

/**
 * This class contains all the logic for parsing
 * user commands into useful information for
 * the engine.
 * 
 * @author Tham Zheng Yi
 */
public class Parser {

    private static Logger logger = Logger.getLogger("Parser");    
    
    // stores the arguments for each keyword
    private static String keywordArgs = "";

    private static String[] commandWords = null;
    private static String[] keywordsArray = {"at", "on", "by", "tomorrow",
        "every", "in", "priority", "desc", "description", "date", "due",
        "remind", "tag", "until"
    };
    private static ArrayList<String> keywords =
            new ArrayList<String>(Arrays.asList(keywordsArray));
    private static String[] monthsArray = {"jan", "january", "feb", "february",
        "mar", "march", "apr", "april", "may", "may", "jun", "june", "jul",
        "july", "aug", "august", "sep", "september", "oct", "october", "nov",
        "november", "dec", "december"
    };
    private static ArrayList<String> months =
            new ArrayList<String>(Arrays.asList(monthsArray));

    // these fields will be used to construct the parseResult
    private static RESULT_TYPE resultType = RESULT_TYPE.VALID;
    private static ERROR_TYPE errorType = null;
    private static COMMAND_TYPE commandType = null;    
    private static Date date = null;
    private static Date date2 = null;
    private static Date dateToRemind = null;
    private static int priorityLevel = 0;
    private static long id = 0;
    private static String name = "";
    private static String description = "";
    private static String tag = "";
    
    private static boolean[] flags = new boolean[8];
    private static Calendar calendar = null;

    private static final int FIRST_AFTER_COMMAND_TYPE = 1;

    public static ParseResult parse(String command) {
        logger.setLevel(Level.WARNING);
        resetFields();
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {
        
        logger.log(Level.INFO, "going to begin processing");
        commandWords = command.split(" ");
        assert(commandWords.length > 0);
        commandType = extractCommandType(commandWords[0]);

        switch(commandType) {
            case ADD:
                processCommand("add");
                break;
                
            case UPDATE:
                processCommand("update");
                break;
                
            case DELETE:
                processCommand("delete");
                break;
                
            case SHOW:
                processCommand("show");
                break;
                
            case DONE:
                processCommand("done");
                break;
                
            case UNDO:
                // not yet implemented
                break;
                
            case SEARCH:
                processCommand("search");
                break;
                
            case HELP:
                processCommand("help");
                break;
            
            case JUMP:
                processCommand("jump");
                break;
                
            case CONVERT:
                processCommand("convert");
                
            default:
                resultType = Constants.RESULT_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.INVALID_COMMAND;
                break;
        }
        logger.log(Level.INFO, "processing ended. returning result.");
        ParseResult parseResult = createParseResult(resultType, commandType);        
        return parseResult;
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
                // at this point, undetermined if showing one task or all
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
                
            case "jump":
                return COMMAND_TYPE.JUMP;
                
            case "convert":
                return COMMAND_TYPE.CONVERT;

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
        errorType = null;
        flags = new boolean[8];
        calendar = null;
    }

    private static Boolean isKeyword(String word) {
        return keywords.contains(word);
    }

    private static void processArgs(String keyword) {
        // remove escape character from arguments since now unneeded
        String[] keywordArgsArray = keywordArgs.split(" ");
        StringBuilder sb = new StringBuilder(keywordArgs.length());
        for (String s: keywordArgsArray) {
            sb.append(s.replaceFirst("/", "") + " ");
        }
        keywordArgs = sb.toString().trim();
        
        switch(keyword) {
            // command keywords start here
            case "add":
                name = keywordArgs.trim();
                break;

            case "update":
            case "delete":
                try {
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id");
                    resultType = Constants.RESULT_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_TASK_ID;
                }
                break;

            case "show":
                try {
                    // check whether next token is an id of the task to show
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                    logger.log(Level.INFO, "successfully parsed id, show" + 
                               "specific task");
                    commandType = Constants.COMMAND_TYPE.SHOW_ONE;
                } catch (NumberFormatException e) {
                    logger.log(Level.INFO, "no id parsed, show all tasks");
                    commandType = Constants.COMMAND_TYPE.SHOW_ALL;
                }
                break;

            case "done":
                try {
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id");
                    resultType = Constants.RESULT_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_TASK_ID;
                }
                break;

            case "undo":
                break;

            case "search":
                name = keywordArgs.trim();
                break;

            case "help":
                // check whether the user needs help with specific command
                String cmdToHelpWith = keywordArgs.split(" ")[0];
                COMMAND_TYPE cmdToHelpWithType = extractCommandType(cmdToHelpWith);
                if (cmdToHelpWithType.equals(Constants.COMMAND_TYPE.INVALID)) {
                    logger.log(Level.INFO, "show general help");
                } else {
                    logger.log(Level.INFO, "show help for specific command");
                    commandType = determineHelpCommandType(cmdToHelpWithType);
                }
                break;
            
            case "convert":
                String[] convertArgs = keywordArgs.split(" ");
                // get id of task to convert
                try {
                    id = Long.parseLong(convertArgs[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id");
                    resultType = Constants.RESULT_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_TASK_ID;
                }
                
                // determine type to convert to
                commandType = determineConvertType(convertArgs[1]);

            // non command keywords start here
            case "at":            
            case "on":
            case "date":            
            case "from":
                calendar = parseDate(keywordArgs);
                date = calendar.getTime();
                break;
            
            // end date (for timed tasks)
            case "by":
            case "due":
            case "until":
                calendar = parseDate(keywordArgs);
                date2 = calendar.getTime();
                break;

            case "every":
                break;

            case "in":
                break;

            case "priority":
                priorityLevel = Integer.parseInt(keywordArgs.split(" ")[0]);
                break;

            case "desc":
            case "description":
                description = keywordArgs.trim();
                break;

            case "remind":
                calendar = parseDate(keywordArgs);
                dateToRemind = calendar.getTime();
                break;
                
            case "tag":
                tag = keywordArgs.trim();
                break;

            default:
                break;
        }
    }

    private static void processCommand(String commandWord) {
        int indexBeingProcessed = FIRST_AFTER_COMMAND_TYPE;
        String wordBeingProcessed = "";
        // store previous keyword that was processed
        String previousKeywordProcessed = "";
        // to decide what to do with args
        String keywordBeingProcessed = commandWord;

        while(indexBeingProcessed < commandWords.length) {
            wordBeingProcessed = commandWords[indexBeingProcessed];
            if (isKeyword(wordBeingProcessed)) {
                // all text after the "help" command is ignored
                if (keywordBeingProcessed.equals("help")) {
                    break;
                 // all text after the id is ignored for delete
                } else if (keywordBeingProcessed.equals("delete")) {
                    break;

                 // all text after the id is ignored for done
                } else if (keywordBeingProcessed.equals("done")) {
                    break;
                    
                // all text after 'show' and its id is ignored for show 
                } else if (keywordBeingProcessed.equals("show")) {
                    break;
                } else {
                    // process arguments of the previous command
                    processArgs(keywordBeingProcessed);
                    keywordArgs = "";
                    previousKeywordProcessed = keywordBeingProcessed;
                    keywordBeingProcessed = wordBeingProcessed;
                }
            } else {
                // add to arguments of previous command
                keywordArgs += wordBeingProcessed + " ";
            }
            indexBeingProcessed++;

        }
        processArgs(keywordBeingProcessed);

        // check for valid name in the case of the add command
        if (commandWord.equals("add")) {
            if (name.equals("")) {
                resultType = RESULT_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.BLANK_TASK_NAME;
            }
        }

        flags = updateResultFlags(flags);

    }

    private static boolean[] updateResultFlags(boolean[] flags) {
        // flags order: date, dateToRemind, priorityLevel, id, name,
        //              description, tag
        assert(flags.length == 7);
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
        if (date2 != null) {
            flags[7] = true;
        }
        return flags;
    }

    // constructs and returns result based on existing fields
    private static ParseResult createParseResult(RESULT_TYPE resultType,
                                           COMMAND_TYPE commandType) {
        return new ParseResult(resultType, commandType, date, date2, dateToRemind, 
                               priorityLevel, id, name, description, tag, 
                               errorType, flags);
    }
    
    // returns a command type for the result based on what user wants help with
    private static COMMAND_TYPE determineHelpCommandType(COMMAND_TYPE commandType) {
        switch(commandType) {
            case ADD:
                return COMMAND_TYPE.HELP_ADD;
                
            case UPDATE:
                return COMMAND_TYPE.HELP_UPDATE;
                
            case DELETE:
                return COMMAND_TYPE.HELP_DELETE;
                
            case SHOW:
                return COMMAND_TYPE.HELP_SHOW;

                
            case DONE:
                return COMMAND_TYPE.HELP_DONE;
                
            case UNDO:
                // not yet implemented
                return COMMAND_TYPE.INVALID;
                
            case SEARCH:
                return COMMAND_TYPE.HELP_SEARCH;
                
            default:
                errorType = Constants.ERROR_TYPE.INVALID_COMMAND;
                return COMMAND_TYPE.INVALID;

        }
    }
    
    private static COMMAND_TYPE determineConvertType(String convertTypeString) {
        switch (convertTypeString) {
            case "deadline":
                return COMMAND_TYPE.CONVERT_DEADLINE;
                
            case "floating":
                return COMMAND_TYPE.CONVERT_FLOATING;
                
            case "timed":
                return COMMAND_TYPE.CONVERT_TIMED;
               
            default:
                errorType = Constants.ERROR_TYPE.INVALID_COMMAND;
                return COMMAND_TYPE.INVALID;
        }
    }

    private static Calendar parseDate(String arguments) {
        logger.log(Level.INFO, "beginning date parsing");
        int day = 0;
        int month = 0;
        int year = 0;
        String[] dateParts = arguments.split(" ");
        assert(dateParts.length == 3);
        String expectedDay = dateParts[0];
        String expectedMonth = dateParts[1];
        String expectedYear = dateParts[2];
        logger.log(Level.INFO, "value expected to be day: " + expectedDay);
        logger.log(Level.INFO, "value expected to be month: " + expectedMonth);
        logger.log(Level.INFO, "value expected to be year: " + expectedYear);

        try {
            day = Integer.parseInt(expectedDay);
        } catch (NumberFormatException e) {
            resultType = Constants.RESULT_TYPE.INVALID;
            errorType = Constants.ERROR_TYPE.INVALID_DATE;
        }

        try {
            month = Integer.parseInt(expectedMonth);
        } catch (NumberFormatException e) {
            int monthIndex = months.indexOf(expectedMonth.toLowerCase());
            // check whether it is found in the list of month strings
            if (monthIndex == -1) {
                resultType = Constants.RESULT_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.INVALID_DATE;
                logger.log(Level.WARNING, "unable to parse date");
            } else {
                month = (monthIndex / 2) + 1;
                logger.log(Level.INFO, "month of parsed date: " + month);
            }

        }

        try {
            year = Integer.parseInt(expectedYear);
        } catch (NumberFormatException e) {
            resultType = Constants.RESULT_TYPE.INVALID;
            errorType = Constants.ERROR_TYPE.INVALID_DATE;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar;
    }

}
