package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

import planner.Constants.CommandType;
import planner.Constants.ErrorType;

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
    private static String[] keywordsArray = {"at", "on", "from", "by",
        "every", "in", "priority", "desc", "description", "date", "due",
        "remind", "tag", "until", "to"
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
    private static String[] cmdsWithoutFollowingKeywords = {"help", 
        "delete", "done", "setnotdone", "savewhere", "savehere", "show"
    };
    private static ArrayList<String> commandsWithoutFollowingKeywords =
            new ArrayList<String>(Arrays.asList(cmdsWithoutFollowingKeywords));
    

    // these fields will be used to construct the parseResult
    private static ErrorType errorType = null;
    private static CommandType commandType = null;    
    private static Date date = null;
    private static Date date2 = null;
    private static Date dateToRemind = null;
    private static int priorityLevel = 0;
    private static long id = 0;
    private static String name = "";
    private static String description = "";
    private static String tag = "";
    private static boolean isTimeSetByUser = false;
    private static boolean isTime2SetByUser = false;
    
    private static boolean[] flags = new boolean[8];
    private static Calendar calendar = null;
    
    private static final int COMMAND_WORD_INDEX = 0;
    private static final int FIRST_AFTER_COMMAND_TYPE = 1;
    private static final int HALF_DAY_IN_HOURS = 12;

    public static ParseResult parse(String command) {
        logger.setLevel(Level.WARNING);
        resetFields();
        ParseResult result = process(command);
        return result;
    }    
    
    private static ParseResult process(String command) {        
        logger.log(Level.INFO, "going to begin processing");
        commandWords = splitBySpaceDelimiter(command);
        assert(commandWords.length > 0);
        commandType = extractCommandType(commandWords[COMMAND_WORD_INDEX]);
        processDependingOnCommandType(commandType);
        
        logger.log(Level.INFO, "processing ended. returning result.");
        ParseResult parseResult = createParseResult(commandType);        
        return parseResult;
    }
    
    private static String[] splitBySpaceDelimiter(String input) {
        return input.split(" ");
    }

    private static CommandType extractCommandType(String commandWord) {
        switch(commandWord.toLowerCase()) {
            case "add":
            case "new":
                return CommandType.ADD;

            case "update":
            case "edit":
            case "change":
                return CommandType.UPDATE;

            case "delete":
            case "trash":
            case "remove":
            case "del":
                return CommandType.DELETE;

            case "show":
            case "display":
                // at this point, undetermined if showing one task or all
                return CommandType.SHOW;

            case "done":
            case "completed":
            case "finished":
                return CommandType.DONE;
                
            case "setnotdone":
                return CommandType.SETNOTDONE;

            case "undo":
            case "revert":
                return CommandType.UNDO;

            case "search":
            case "find":
                return CommandType.SEARCH;

            case "help":
            case "sos":
                return CommandType.HELP;
                
            case "jump":
                return CommandType.JUMP;
                
            case "convert":
                return CommandType.CONVERT;
                
            case "savewhere":
                return CommandType.SAVEWHERE;
                
            case "savehere":
                return CommandType.SAVEHERE;

            default:
                return CommandType.INVALID;
        }
    }
    
    private static void processDependingOnCommandType(CommandType commandType) {
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
                // no need to process command
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
                
            case SAVEWHERE:
                processCommand("savewhere");
                break;
            
            case SAVEHERE:
                processCommand("savehere");
                break;
                
            default:
                setErrorType(ErrorType.INVALID_COMMAND);
                logger.log(Level.WARNING, "command " + commandType.toString() + " not recognized");
                break;
        }
    }
    
    private static void setErrorType(ErrorType desiredErrorType) {
        errorType = desiredErrorType;
    }
    
    private static void setCommandType(CommandType desiredCommandType) {
        commandType = desiredCommandType;
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
        isTimeSetByUser = false;
        isTime2SetByUser = false;        
    }

    private static Boolean isKeyword(String word) {
        return keywords.contains(word);
    }

    private static void processCommand(String commandWord) {        
        processKeywordsAndArgs(commandWord);        
        checkAddConvertValidFields();
        setDefaultDatesForAdd();
        checkValidDates();
        flags = updateResultFlags(flags);
    }
    
    /**
     * Searches the user input for keywords and processes each keyword and its 
     * arguments in succession.
     * 
     * @param commandWord The initial keyword representing the command type
     */
    private static void processKeywordsAndArgs(String commandWord) {
        int indexBeingProcessed = FIRST_AFTER_COMMAND_TYPE;
        String wordBeingProcessed = "";        
        String previousKeywordProcessed = "";
        // to decide what to do with args
        String keywordBeingProcessed = commandWord;
        
        // continue looking for keywords until the end of the command        
        while (indexBeingProcessed < commandWords.length) {
            wordBeingProcessed = commandWords[indexBeingProcessed];
            if (isKeyword(wordBeingProcessed)) {
                /* all text after the help, delete, done, setnotdone, savewhere,
                   savehere and show commands and their arguments is ignored */
                if (commandsWithoutFollowingKeywords.contains(keywordBeingProcessed)) {
                    break;
                } else if (keywordBeingProcessed.equals("jump")) {
                    // allow users to say use "jump date <date>" as well as "jump <date>"
                    if (wordBeingProcessed.equals("date")) {
                        // do nothing
                    } else {
                        break;
                    }
                // no special case
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
    }

    private static void processArgs(String keyword) {  
        // remove escape character from arguments since now unneeded
        keywordArgs = removeEscapeCharacterInstances(keywordArgs);
        // split for later use in cases
        String[] keywordArgsArray = splitBySpaceDelimiter(keywordArgs);
        
        switch(keyword) {
            // command keywords start here
            case "add":
                name = keywordArgs.trim();
                break;

            case "update":
            case "delete":
            case "setnotdone":
            case "done":
                try {
                    id = Long.parseLong(keywordArgsArray[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id in " + keyword);
                    setCommandType(CommandType.INVALID);
                    setErrorType(ErrorType.INVALID_TASK_ID);
                }
                break;

            case "show":
                try {
                    // check whether next token is an id of the task to show
                    id = Long.parseLong(keywordArgsArray[0]);
                    logger.log(Level.INFO, "successfully parsed id, show" + 
                               "specific task");
                    setCommandType(CommandType.SHOW_ONE);
                } catch (NumberFormatException e) {
                    logger.log(Level.INFO, "no id parsed, show all tasks");
                    setCommandType(CommandType.SHOW_ALL);
                }
                break;
            
            case "undo":
                // no arguments, all other text ignored
                break;

            case "search":
                name = keywordArgs.trim();
                break;

            case "help":
                // check whether the user needs help with specific command
                String cmdToHelpWith = keywordArgsArray[0];
                CommandType cmdToHelpWithType = extractCommandType(cmdToHelpWith);
                if (cmdToHelpWithType.equals(Constants.CommandType.INVALID)) {
                    logger.log(Level.INFO, "show general help");
                } else {
                    logger.log(Level.INFO, "show help for specific command");
                    setCommandType(determineHelpCommandType(cmdToHelpWithType));
                }
                break;
            
            case "convert":
                String[] convertArgs = keywordArgsArray;
                // get id of task to convert
                try {
                    id = Long.parseLong(convertArgs[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id for convert");
                    setCommandType(CommandType.INVALID);
                    setErrorType(ErrorType.INVALID_TASK_ID);
                }
                
                // determine type to convert to
                setCommandType(determineConvertType(convertArgs[1]));             
                break;
                
            case "savewhere":
                // no arguments, all other text ignored
                break;
                
            case "savehere":
                // desired file path for data storage will be put in name field
                name = keywordArgs.trim();
                break;

            // non command keywords start here
            case "at":            
            case "on":
            case "date":            
            case "from":
            case "by":
            case "due":
                calendar = parseDate(keywordArgs, "date1");
                if (calendar != null) {
                    date = calendar.getTime();
                }                
                break;
            
            // end date (for timed tasks)
            case "until":
            case "to":
                calendar = parseDate(keywordArgs, "date2");
                if (calendar != null) {
                    date2 = calendar.getTime();
                }
                break;
            
            // arguments for jump are expected to be date info
            case "jump":
                calendar = parseDate(keywordArgs, "jumpdate");
                if (calendar != null) {
                    date = calendar.getTime();
                }
                break;

            // not yet implemented
            case "every":
                break;
            
            // not yet implemented
            case "in":
                break;

            case "priority":
                priorityLevel = Integer.parseInt(keywordArgsArray[0]);
                break;

            case "desc":
            case "description":
                description = keywordArgs.trim();
                break;

            case "remind":
                calendar = parseDate(keywordArgs, "dateRemind");
                if (calendar != null) {
                    dateToRemind = calendar.getTime();
                }
                break;
                
            case "tag":
                tag = keywordArgs.trim();
                break;

            default:
                break;
        }
    }
    
    /**
     * Remove all instances of the escape character '/' from the given input 
     * string, which is expected to be the arguments of a keyword.
     * 
     * @param  inputString Arguments of the keyword being processed
     * @return             The input string with the character removed
     */
    private static String removeEscapeCharacterInstances(String inputString) {     
        String[] keywordArgsArray = splitBySpaceDelimiter(inputString);
        StringBuilder sb = new StringBuilder(inputString.length());
        for (String s: keywordArgsArray) {
            sb.append(s.replaceFirst("/", "") + " ");
        }
        String keywordArgs = sb.toString().trim();
        return keywordArgs;
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
    private static ParseResult createParseResult(CommandType commandType) {
        return new ParseResult(commandType, date, date2, dateToRemind, 
                               priorityLevel, id, name, description, tag, 
                               errorType, flags);
    }
    
    // returns a command type for the result based on what user wants help with
    private static CommandType determineHelpCommandType(CommandType commandType) {
        switch(commandType) {
            case ADD:
                return CommandType.HELP_ADD;
                
            case UPDATE:
                return CommandType.HELP_UPDATE;
                
            case DELETE:
                return CommandType.HELP_DELETE;
                
            case SHOW:
                return CommandType.HELP_SHOW;

                
            case DONE:
                return CommandType.HELP_DONE;
                
            case UNDO:                
                return CommandType.UNDO;
                
            case SEARCH:
                return CommandType.HELP_SEARCH;
                
            default:
                setErrorType(ErrorType.INVALID_COMMAND);
                return CommandType.INVALID;

        }
    }
    
    private static CommandType determineConvertType(String convertTypeString) {
        switch (convertTypeString.trim()) {
            case "deadline":
                return CommandType.CONVERT_DEADLINE;
                
            case "floating":
                return CommandType.CONVERT_FLOATING;
                
            case "timed":
                return CommandType.CONVERT_TIMED;
               
            default:
                setErrorType(ErrorType.INVALID_COMMAND);
                logger.log(Level.WARNING, "unable to determine convert type");
                return CommandType.INVALID;
        }
    }

    private static Calendar parseDate(String arguments, String parseTarget) {
        logger.log(Level.INFO, "beginning date parsing");
        Calendar currentTime = Calendar.getInstance();
        int day = currentTime.get(Calendar.DATE);
        int month = currentTime.get(Calendar.MONTH);
        int year = currentTime.get(Calendar.YEAR);
        int tokenBeingParsedIndex = 0;
        
        // the tokens that will individually represent day, month etc
        String[] dateParts = splitBySpaceDelimiter(arguments);
        assert(dateParts.length > 0);
        // may be a representation of the day, or a time keyword, or the next keyword
        String firstArg = dateParts[tokenBeingParsedIndex].toLowerCase();
        
        // check whether the current argument is a keyword for time
        if (firstArg.equals("pm") || firstArg.equals("am")) {
            setTimeSetByUserToTrue(parseTarget);
            return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                       firstArg, year, month, day); 
        
        } else {
            // parse as date without regards to time
            logger.log(Level.INFO, "value expected to be day or next keyword: " + firstArg);
            try {
                day = Integer.parseInt(firstArg);
                
            } catch (NumberFormatException e) {
                
                //Goes into parseNext when "next" is the first argument
                if (firstArg.toLowerCase().trim().equals("next")) {
                    return parseNext(arguments);
                } else { 
                    setCommandType(CommandType.INVALID);
                    setErrorType(ErrorType.INVALID_DATE);
                    logger.log(Level.WARNING, "unable to parse day");
                    return null;
                }
            }
        }
        
        
        //Checks whether date information is incomplete, if so checks whether date has passed
        //If so push to next month, else keep the current month
        if (dateParts.length == 1) {
            if (day < currentTime.get(Calendar.DATE)) {
                return createCalendar(year, month + 1, day, 0, 0); 
            } else {
                return createCalendar(year, month, day, 0, 0);
            }
        } else {
            // now parsing the second token
            tokenBeingParsedIndex++;
            // may be a representation of the month, or a time keyword
            String secondArg = dateParts[tokenBeingParsedIndex].toLowerCase();
            
            // check whether the current argument is a keyword for time
            if (secondArg.equals("pm") || secondArg.equals("am")) {
                setTimeSetByUserToTrue(parseTarget);
                return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           secondArg, year, month, day); 
                
            } else {
                // parse as date without regards to time
                logger.log(Level.INFO, "value expected to be month: " + secondArg);
                try {
                    month = Integer.parseInt(secondArg);
                } catch (NumberFormatException e) {
                    int monthIndex = months.indexOf(secondArg.toLowerCase());
                    // check whether it is found in the list of English month strings
                    if (monthIndex == -1) {
                        setCommandType(CommandType.INVALID);
                        setErrorType(ErrorType.INVALID_DATE);
                        logger.log(Level.WARNING, "unable to parse month");
                        return null;
                    } else {
                        month = (monthIndex / 2) + 1;
                        logger.log(Level.INFO, "month of parsed date: " + month);
                    }
                }
            }            
        }     
        
        // Similar check as above, push to next year if month had passed
        if (dateParts.length == 2) {
            if (month < currentTime.get(Calendar.MONTH)) {
                return createCalendar(year + 1, month - 1, day, 0, 0);
            } else {
                return createCalendar(year, month - 1, day, 0, 0);
            }
        } else {
            // now parsing the third token
            tokenBeingParsedIndex++;
            // may be a representation of the year, or a time keyword
            String thirdArg = dateParts[2].toLowerCase();

            // check whether the current argument is a keyword for time
            if (thirdArg.equals("pm") || thirdArg.equals("am")) {
                setTimeSetByUserToTrue(parseTarget);
                return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           thirdArg, year, month, day); 

            } else {
                logger.log(Level.INFO, "value expected to be year: " + thirdArg);
                try {
                    year = Integer.parseInt(thirdArg);
                } catch (NumberFormatException e) {
                    setCommandType(CommandType.INVALID);
                    setErrorType(ErrorType.INVALID_DATE);
                    logger.log(Level.WARNING, "unable to parse year");
                    return null;
                }
            }            
        }
        
        // user has input year, month, day, as well as time
        if (dateParts.length == 5) {
            tokenBeingParsedIndex++;
            // expected to be a time keyword
            String fourthArg = dateParts[3].toLowerCase();
            if (fourthArg.equals("pm") || fourthArg.equals("am")) {
                setTimeSetByUserToTrue(parseTarget);
                return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           fourthArg, year, month, day); 
            }
        }
        
        return createCalendar(year, month - 1, day, 0, 0);
    }
    
    
    //Parses whatever that comes after "next" is typed
    //Will delete/change bad comments before refactoring the code
    private static Calendar parseNext(String arguments) {
        String[] dateParts = splitBySpaceDelimiter(arguments);
        String secondArg = dateParts[1].toLowerCase().trim();
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH);
        int date = currentTime.get(Calendar.DATE);
        int day = currentTime.get(Calendar.DAY_OF_WEEK);
        try {
            switch(secondArg) {
                case "day":
                    return createCalendar(year, month, date + 1, 0, 0);
                
                case "month":
                    return createCalendar(year, month + 1, 1, 0 ,0);
                    
                case "year":
                    return createCalendar(year + 1, 0, 1, 0 ,0);
                    
                case "week": 
                    
                    //Calendar forces monday = 2, sunday = 1 for some reason and hence if current date is monday
                    //the difference calculated after %7 is 0 so i must change it to 7
                    int daysDifference = (8 - day + 1) % 7;
                    if (daysDifference == 0) {
                        daysDifference = 7;
                    }
                    return createCalendar(year, month, date + daysDifference, 0 ,0);
                    
                default:
                    
                    //Checks the months arraylist to find matches
                    int index = months.indexOf(secondArg.toLowerCase().trim());
                    if (index == -1) {
                        
                        //If it is not in months, check newly created daysInWeek arraylist
                        index = days.indexOf(secondArg.toLowerCase().trim());
                        if (index == -1) {
                            
                            //report error and return junk calendar values to avoid exception errors 
                            setCommandType(CommandType.INVALID);
                            setErrorType(ErrorType.INVALID_DATE);
                            break;
                        } else {
                            
                            //If found calculate differences, magic numbers again since mon = 2 for calendar 
                            //while mon = 1 from arraylist index
                            int dayDifference = 7 + (index / 2) + 1 - (day - 1);
                            System.out.println(day);
                            return createCalendar(year, month, date + dayDifference , 0, 0);
                        }
                    } else {
                        int monthRequested = (index / 2) + 1;
                        return createCalendar(year + 1, monthRequested - 1, 1, 0, 0);                    
                    }
            }
        } catch (NumberFormatException e) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_DATE);
            logger.log(Level.WARNING, "unable to parse date");
        }
        return createCalendar(year, month, date, 0, 0);        
    }
    
    /**
     *  Takes in an array of strings containing date info and the expected 
     *  index containing the time string, and returns a Calendar constructed 
     *  with all the date info.
     *  
     *  @param dateParts    string tokens with date info
     *  @param indexToCheck index of array expected to contain time string
     *  @param year         year of desired date
     *  @param month        month of desired date
     *  @param day          day of desired date
     *  @return             representation of full date
     */    
    private static Calendar calcDateGivenTime(String[] dateParts, 
                                                int indexBeingParsed, 
                                                String pmOrAm, int year, 
                                                int month, int day) {
        int indexToCheck = indexBeingParsed + 1;
        // check for a valid argument to be parsed as the time
        if (indexToCheck > dateParts.length) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to no token after time keyword");
            return createCalendar(year, month - 1, day, 0, 0);
        } 
        String timeString = dateParts[indexToCheck];
        String[] timeParts = timeString.split("\\.");
        
        // check for appropriate format (##.##)
        if (timeParts.length != 2) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to incorrect format");
            return createCalendar(year, month - 1, day, 0, 0);
        } else {
            try {
                int hour = Integer.parseInt(timeParts[0]);
                int min = Integer.parseInt(timeParts[1]);
                if (hour < 1 || hour > 12 || min < 0 || min > 59) {
                    setCommandType(CommandType.INVALID);
                    setErrorType(ErrorType.INVALID_TIME);
                    logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to invalid hour/minute given");
                    return createCalendar(year, month, day, 0, 0);
                } else if (pmOrAm.equals("pm")) {
                    return createCalendar(year, month - 1, day, hour + HALF_DAY_IN_HOURS, min); 
                } else {
                    return createCalendar(year, month - 1, day, hour, min); 
                }
                
            } catch (NumberFormatException e) {
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.INVALID_TIME);
                logger.log(Level.WARNING, "error parsing time on argument number " + indexBeingParsed);
                return createCalendar(year, month - 1, day, 0, 0);
            }
            
        }
    }
    
    //Creates calendar 
    private static Calendar createCalendar(int year, int month, int date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        // first set the second to 0
        calendar.set(0, 0, 0, 0, 0, 0);
        calendar.set(year, month, date, hour, minute);
        return calendar;
    }
    
    private static void checkAddConvertValidFields() {
        // check for valid name in the case of the add command
        if (commandType.equals(CommandType.ADD)) {
            if (name.equals("")) {
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.BLANK_TASK_NAME);
            }
            
        // check for two valid dates in the case of the convert timed 
        } else if (commandType.equals(CommandType.CONVERT_TIMED)) {
            if (date == null || date2 == null) {
                logger.log(Level.WARNING, "Less than two valid dates for Convert Timed");
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.INVALID_ARGUMENTS);
            }
         // check for at least one valid date in the case of convert deadline
        } else if (commandType.equals(CommandType.CONVERT_DEADLINE)) {
            logger.log(Level.WARNING, "no valid dates for Convert Deadline");
            if (date == null && date2 == null) {
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.INVALID_ARGUMENTS);
            }
        }
    }
    
    private static void setTimeSetByUserToTrue(String targetTime) {
        if (targetTime.equals("date1")) {
            isTimeSetByUser = true;
        } else if (targetTime.equals("date2")) {
            isTime2SetByUser = true;
        }
    }
    
    private static void setDefaultDatesForAdd() {
        if (commandType.equals(CommandType.ADD)) {
            Calendar calendar = Calendar.getInstance();
            // case of timed task
            if (date != null && date2 != null) {
                if (!isTimeSetByUser) {
                    // set the default time for first date to start of the day
                    calendar.setTime(date);
                    int existingYear = calendar.get(Calendar.YEAR);
                    int existingMonth = calendar.get(Calendar.MONTH);
                    int existingDay = calendar.get(Calendar.DATE);
                    calendar.set(existingYear, existingMonth, existingDay, 0, 0);
                    date = calendar.getTime();
                }
                if (!isTime2SetByUser) {
                    // set the default time for second date to end of the day
                    calendar.setTime(date2);
                    int existingYear = calendar.get(Calendar.YEAR);
                    int existingMonth = calendar.get(Calendar.MONTH);
                    int existingDay = calendar.get(Calendar.DATE);
                    calendar.set(existingYear, existingMonth, existingDay, 23, 59);
                    date2 = calendar.getTime();
                }
            // case of deadline task
            } else if (date != null || date2 != null) {
                if (!isTimeSetByUser && date != null) {
                    // set the default time for the date to end of the day
                    calendar.setTime(date);
                    int existingYear = calendar.get(Calendar.YEAR);
                    int existingMonth = calendar.get(Calendar.MONTH);
                    int existingDay = calendar.get(Calendar.DATE);
                    calendar.set(existingYear, existingMonth, existingDay, 23, 59);
                    date = calendar.getTime();
                }
                
                if (!isTime2SetByUser && date2 != null) {
                    // set the default time for the date to end of the day
                    calendar.setTime(date2);
                    int existingYear = calendar.get(Calendar.YEAR);
                    int existingMonth = calendar.get(Calendar.MONTH);
                    int existingDay = calendar.get(Calendar.DATE);
                    calendar.set(existingYear, existingMonth, existingDay, 23, 59);
                    date2 = calendar.getTime();
                }
            }
        }
    }
    
    /**
     * Checks that the date represented by date1 is before the date represented
     * by date2.
     */
    private static void checkValidDates() {
        if (date != null && date2 != null) {
            if (!(date.compareTo(date2) < 0)) {
                logger.log(Level.WARNING, "Date 1 not smaller than Date 2");
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.DATE1_NOT_SMALLER_THAN_DATE2);
            }
        }
    }
}
