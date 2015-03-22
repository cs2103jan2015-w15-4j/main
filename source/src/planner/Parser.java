package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import planner.Constants.COMMAND_TYPE;
import planner.Constants.ERROR_TYPE;

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
    private static String[] daysInWeek = {"mon", "monday", "tue", "tuesday", 
        "wed", "wednesday", "thu", "thursday", "fri", "friday", "sat", "saturday",
        "sun", "sunday"
    };
    private static ArrayList<String> days =
            new ArrayList<String>(Arrays.asList(daysInWeek));

    // these fields will be used to construct the parseResult
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
            
            case SETNOTDONE:
                processCommand("setnotdone");
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
                break;
                
            default:
                errorType = Constants.ERROR_TYPE.INVALID_COMMAND;
                logger.log(Level.WARNING, "command " + commandType.toString() + " not recognized");
                break;
        }
        logger.log(Level.INFO, "processing ended. returning result.");
        ParseResult parseResult = createParseResult(commandType);        
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
                
            case "setnotdone":
                return COMMAND_TYPE.SETNOTDONE;

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
        commandType = null;
        keywordArgs = "";
        commandWords = null;
        date = null;
        date2 = null;
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
                    logger.log(Level.WARNING, "error parsing id in delete");
                    commandType = Constants.COMMAND_TYPE.INVALID;
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
                
            case "setnotdone":
            case "done":
                try {
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id in done");
                    commandType = Constants.COMMAND_TYPE.INVALID;
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
                    logger.log(Level.WARNING, "error parsing id for convert");
                    commandType = Constants.COMMAND_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_TASK_ID;
                }
                
                // determine type to convert to
                commandType = determineConvertType(convertArgs[1]);
                
                break;

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
            case "to":
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
                 // all text after the id is ignored for setnotdone   
                } else if (keywordBeingProcessed.equals("setnotdone")) {
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
        if (commandType.equals(COMMAND_TYPE.ADD)) {
            if (name.equals("")) {
                commandType = Constants.COMMAND_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.BLANK_TASK_NAME;
            }
            
        // check for two valid dates in the case of the convert timed 
        } else if (commandType.equals(COMMAND_TYPE.CONVERT_TIMED)) {
            if (date == null || date2 == null) {
                logger.log(Level.WARNING, "Less than two valid dates for Convert Timed");
                commandType = Constants.COMMAND_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.INVALID_ARGUMENTS;
            }
         // check for at least one valid date in the case of convert deadline
        } else if (commandType.equals(COMMAND_TYPE.CONVERT_DEADLINE)) {
            logger.log(Level.WARNING, "no valid dates for Convert Deadline");
            if (date == null && date2 == null) {
                commandType = Constants.COMMAND_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.INVALID_ARGUMENTS;
            }
        }

        flags = updateResultFlags(flags);

    }

    private static boolean[] updateResultFlags(boolean[] flags) {
        // flags order: date, dateToRemind, priorityLevel, id, name,
        //              description, tag
        assert(flags.length == 8);
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
    private static ParseResult createParseResult(COMMAND_TYPE commandType) {
        return new ParseResult(commandType, date, date2, dateToRemind, 
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
        switch (convertTypeString.trim()) {
            case "deadline":
                return COMMAND_TYPE.CONVERT_DEADLINE;
                
            case "floating":
                return COMMAND_TYPE.CONVERT_FLOATING;
                
            case "timed":
                return COMMAND_TYPE.CONVERT_TIMED;
               
            default:
                errorType = Constants.ERROR_TYPE.INVALID_COMMAND;
                logger.log(Level.WARNING, "unable to determine convert type");
                return COMMAND_TYPE.INVALID;
        }
    }

    private static Calendar parseDate(String arguments) {
        logger.log(Level.INFO, "beginning date parsing");
        Calendar currentTime = Calendar.getInstance();
        int day = currentTime.get(Calendar.DATE);
        int month = currentTime.get(Calendar.MONTH);
        int year = currentTime.get(Calendar.YEAR);
        String[] dateParts = arguments.split(" ");
        String firstArg = dateParts[0];
        try {
            day = Integer.parseInt(firstArg);
        } catch (NumberFormatException e) {
            if (firstArg.toLowerCase().trim().equals("next")) {
                return parseNext(arguments);
            } else { 
                commandType = Constants.COMMAND_TYPE.INVALID;
                errorType = Constants.ERROR_TYPE.INVALID_DATE;
                logger.log(Level.WARNING, "unable to parse day");
                return createCalendar(year, month, day, 0, 0, 0);
            }
        }
        if (dateParts.length == 1) {
            if (day < currentTime.get(Calendar.DATE)) {
                return createCalendar(year, month + 1, day, 0, 0, 0); 
            } else {
                return createCalendar(year, month, day, 0, 0, 0);
            }
        } else {
            String expectedMonth = dateParts[1];
            try {
                month = Integer.parseInt(expectedMonth);
            } catch (NumberFormatException e) {
                int monthIndex = months.indexOf(expectedMonth.toLowerCase());
                // check whether it is found in the list of month strings
                if (monthIndex == -1) {
                    commandType = Constants.COMMAND_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_DATE;
                    logger.log(Level.WARNING, "unable to parse month");
                    return createCalendar(year, month, day, 0, 0, 0);
                } else {
                    month = (monthIndex / 2) + 1;
                    logger.log(Level.INFO, "month of parsed date: " + month);
                }
            }
            if (dateParts.length == 2) {
                if (month < currentTime.get(Calendar.MONTH)) {
                    return createCalendar(year + 1, month - 1, day, 0, 0, 0);
                } else {
                    return createCalendar(year, month - 1, day, 0, 0, 0);
                }
            } else {
                String expectedYear = dateParts[2];
                logger.log(Level.INFO, "value expected to be day: " + day);
                logger.log(Level.INFO, "value expected to be month: " + expectedMonth);
                logger.log(Level.INFO, "value expected to be year: " + expectedYear);
                try {
                    year = Integer.parseInt(expectedYear);
                } catch (NumberFormatException e) {
                    commandType = Constants.COMMAND_TYPE.INVALID;
                    errorType = Constants.ERROR_TYPE.INVALID_DATE;
                    logger.log(Level.WARNING, "unable to parse year");
                }
                
                return createCalendar(year, month - 1, day, 0, 0, 0);
            }
        }
    }
    
    private static Calendar parseNext(String arguments) {
        String[] dateParts = arguments.split(" ");
        String secondArg = dateParts[1].toLowerCase().trim();
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH);
        int date = currentTime.get(Calendar.DATE);
        int day = currentTime.get(Calendar.DAY_OF_WEEK);
        try {
            switch(secondArg) {
                case "day":
                    return createCalendar(year, month, date + 1, 0, 0, 0);
                
                case "month":
                    return createCalendar(year, month + 1, 1, 0 ,0 ,0);
                    
                case "year":
                    return createCalendar(year + 1, 0, 1, 0 ,0 ,0);
                    
                case "week":
                    int daysDifference = (8 - day + 1) % 7;
                    if (daysDifference == 0) {
                        daysDifference = 7;
                    }
                    return createCalendar(year, month, date + daysDifference, 0 ,0, 0);
                    
                default:
                    int index = months.indexOf(secondArg.toLowerCase().trim());
                    if (index == -1) {
                        index = days.indexOf(secondArg.toLowerCase().trim());
                        if (index == -1) {
                            commandType = Constants.COMMAND_TYPE.INVALID;
                            errorType = Constants.ERROR_TYPE.INVALID_DATE;
                            break;
                        } else {
                            int dayDifference = 7 + (index / 2) + 1 - (day - 1);
                            System.out.println(day);
                            return createCalendar(year, month, date + dayDifference , 0, 0, 0);
                        }
                    } else {
                        int monthRequested = (index / 2) + 1;
                        return createCalendar(year + 1, monthRequested - 1, 1, 0, 0, 0);                    
                    }
            }
        } catch (NumberFormatException e) {
            commandType = Constants.COMMAND_TYPE.INVALID;
            errorType = Constants.ERROR_TYPE.INVALID_DATE;
        }
        return createCalendar(year, month, date, 0, 0, 0);
        
    }
    
    private static Calendar createCalendar(int year, int month, int date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, second);
        return calendar;
    }
}
