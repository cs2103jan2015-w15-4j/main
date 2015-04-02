package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import planner.Constants.CommandType;
import planner.Constants.ErrorType;

/**
 * This class contains all the logic for parsing
 * user commands into useful information for
 * the engine. 
 */
public class Parser {

    private static Logger logger = Logger.getLogger("Parser");    
    
    // stores the arguments for each keyword
    private static String keywordArgs = "";

    private static String[] inputTokens = null;
    private static String[] nonCommandKeywordsArray = {"at", "on", "from", "by",
        "every", "in", "priority", "desc", "description", "date", "due",
        "remind", "tag", "until", "to"  
    };
    private static ArrayList<String> nonCommandKeywords =
            new ArrayList<String>(Arrays.asList(nonCommandKeywordsArray));
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
    private static String[] cmdsWithoutFollowingKeywords = {"help", "undo",
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
    private static final int DATE_1_INDEX = 1;
    private static final int DATE_2_INDEX = 2;
    private static final int DATE_TO_REMIND_INDEX = 3;
    
    /**
     * Processes a user input string and returns a result object containing
     * information such as the user's desired command type and the relevant
     * fields to update.
     * 
     * @param inputString The user input
     * @return            A result containing information such as command type
     */
    public static ParseResult parse(String inputString) {
        logger.setLevel(Level.WARNING);
        resetFields();
        process(inputString.trim());
        return createParseResult(commandType); 
    }    
    
    /**
     * Processes the input string by updating the result fields based on what 
     * the command type is.
     * 
     * @param inputString The user input
     * @return            A result containing information such as command type
     */
    private static void process(String inputString) {        
        logger.log(Level.INFO, "going to begin processing");
        inputTokens = splitBySpaceDelimiter(inputString);
        assert(inputTokens.length > 0);
        commandType = extractCommandType(inputTokens[COMMAND_WORD_INDEX]);
        processDependingOnCommandType(commandType);
        
        logger.log(Level.INFO, "processing ended.");
    }
    
    /**
     * Tokenizes a string input.
     * 
     * @param input Input string
     * @return      Array of tokens
     */
    private static String[] splitBySpaceDelimiter(String input) {
        return input.split(" ");
    }

    /**
     * Determines the type of command a user wants to execute given the input
     * token expected to be a valid command keyword.
     * 
     * @param commandWord Command keyword
     * @return            Type of command
     */
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
    
    /**
     * Given the command type, processes the rest of the input depending on 
     * what the command is. The command type is converted to a string for 
     * convenience in processing.
     * 
     * @param commandType Type of command being parsed
     */
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
                processCommand("undo");
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
    
    /**
     * Sets the error type result field to the given input.
     * 
     * @param desiredErrorType Error that arose from parsing
     */
    private static void setErrorType(ErrorType desiredErrorType) {
        errorType = desiredErrorType;
    }
    
    /**
     * Sets the command type result field to the given input.
     * 
     * @param desiredCommandType Command type determined from parsing
     */
    private static void setCommandType(CommandType desiredCommandType) {
        commandType = desiredCommandType;
    }

    /**
     * Resets the result fields used to construct the parse result object.
     */
    private static void resetFields() {
        commandType = null;
        keywordArgs = "";
        inputTokens = null;
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

    /**
     * Checks whether input word is a non command keyword
     * @param word Keyword
     * @return     Whether the keyword is a non command keyword
     */
    private static Boolean isNonCmdKeyword(String word) {
        return nonCommandKeywords.contains(word);
    }
    
    /**
     * Processes the rest of the input given the command word the user used.
     * 
     * @param commandWord User's desired command type
     */
    private static void processCommand(String commandWord) {        
        processKeywordsAndArgs(commandWord);        
        checkAddConvertHaveValidFields();
        setDefaultTimesForAdd();
        checkDate1BeforeDate2();
        flags = updateResultFlags(date, dateToRemind, priorityLevel, id, name, 
                                  description, tag, date2);
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
        while (indexBeingProcessed < inputTokens.length) {
            wordBeingProcessed = inputTokens[indexBeingProcessed];
            if (isNonCmdKeyword(wordBeingProcessed)) {
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
    
    /**
     * Processes the existing keywords in the keyword buffer string based on
     * what the keyword is.
     * 
     * @param keyword The keyword for which the arguments will be processed
     */
    private static void processArgs(String keyword) {  
        // remove escape character from arguments since now unneeded
        keywordArgs = removeEscapeCharacterInstances(keywordArgs);
        // split for later use in cases
        String[] keywordArgsArray = splitBySpaceDelimiter(keywordArgs);
        
        if (isNonCmdKeyword(keyword)) {
            processNonCmdKeywordArgs(keyword, keywordArgs, keywordArgsArray);
        } else {
            processCmdKeywordArgs(keyword, keywordArgs, keywordArgsArray);
        }           
    }
    
    /**
     * Processes the existing keywords in the keyword buffer string based on
     * which of the non command keywords the keyword is.
     * 
     * @param keyword          The keyword for which the arguments will be 
     *                         processed
     * @param keywordArgs      The arguments of the keyword with escape 
     *                         characters removed
     * @param keywordArgsArray The tokenized arguments of the keyword with 
     *                         escape characters removed
     */
    private static void processNonCmdKeywordArgs(String keyword,
                                                 String keywordArgs,
                                                 String[] keywordArgsArray) {
        switch(keyword) {     
            case "at":            
            case "on":
            case "date":            
            case "from":
            case "by":
            case "due":
                updateDate(keywordArgs, DATE_1_INDEX);                          
                break;
            
            // end date (for timed tasks)
            case "until":
            case "to":
                updateDate(keywordArgs, DATE_2_INDEX);                
                break;           
    
            // not yet implemented
            case "every":
                break;
            
            // not yet implemented
            case "in":
                break;
    
            case "priority":
                updatePriorityLevel(keywordArgsArray[0]);                
                break;
    
            case "desc":
            case "description":
                description = keywordArgs.trim();
                break;
    
            case "remind":
                updateDate(keywordArgs, DATE_TO_REMIND_INDEX);  
                break;
                
            case "tag":
                tag = keywordArgs.trim();
                break;
    
            default:
                break;    
        }
    }
    
    /**
     * Processes the existing keywords in the keyword buffer string based on
     * which of the command keywords the keyword is.
     * 
     * @param keyword          The keyword for which the arguments will be 
     *                         processed
     * @param keywordArgs      The arguments of the keyword with escape 
     *                         characters removed
     * @param keywordArgsArray The tokenized arguments of the keyword with 
     *                         escape characters removed
     */
    private static void processCmdKeywordArgs(String keyword, 
                                              String keywordArgs,
                                              String[] keywordArgsArray) {
        switch(keyword) {            
            case "add":
                name = keywordArgs.trim();
                break;
    
            case "update":
            case "delete":
            case "setnotdone":
            case "done":
                updateId(keywordArgsArray[0], keyword);                
                break;
    
            case "show":
                // not in use
                break;
            
            case "undo":
                // no arguments, all other text ignored
                break;
    
            case "search":
                name = keywordArgs.trim();
                break;
    
            case "help":
                determineWhatUserNeedsHelpWith(keywordArgsArray[0]);
                break;
            
            case "convert":
                // get id of task to convert
                updateId(keywordArgsArray[0], keyword); 
                
                // determine type to convert to
                setCommandType(determineConvertType(keywordArgsArray[1]));             
                break;
                
            case "savewhere":
                // no arguments, all other text ignored
                break;
                
            case "savehere":
                // desired file path for data storage will be put in name field
                name = keywordArgs.trim();
                break;
                
             // arguments for jump are expected to be date info
            case "jump":
                updateDate(keywordArgs, DATE_1_INDEX);
                break;
                
            default:
                break;
        }
    }    
    
    /**
     * Updates the priority field after processing a string representing the 
     * desired priority level.
     * 
     * @param desiredLevel Priority level
     */
    private static void updatePriorityLevel(String desiredLevel) {
        try {
            priorityLevel = Integer.parseInt(desiredLevel);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "error parsing priority level");
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_PRIORITY_LEVEL);
        }
        
        if (priorityLevel < 1 || priorityLevel > 5) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_PRIORITY_LEVEL);
        }
    }
    
    /**
     * Updates the ID field after processing a string representing the target
     * id of the command. Keyword is input for logging purposes.
     * 
     * @param targetIdString     Target task of command
     * @param keywordBeingParsed Keyword being parsed
     */
    private static void updateId(String targetIdString, String keywordBeingParsed) {
        try {
            id = Long.parseLong(targetIdString);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "error parsing id in " + keywordBeingParsed);
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TASK_ID);
        }
    }
    
    /**
     * Updates the selected date result field based on the keyword arguments.
     * 
     * @param keywordArgs       The arguments of the keyword, expected to be 
     *                          date data
     * @param dateFieldToUpdate Index representing which date field to update
     */
    private static void updateDate(String keywordArgs, int dateFieldToUpdate) {
        calendar = parseDate(keywordArgs, dateFieldToUpdate);
        if (calendar != null) {
            if (dateFieldToUpdate == 1) {
                date = calendar.getTime();
            } else if (dateFieldToUpdate == 2) {
                date2 = calendar.getTime();
            } else {
                dateToRemind = calendar.getTime();
            }
        }      
    }
    
    /**
     * Updates the command type to a help-related command type if the user has
     * specified a valid command type as an argument.
     * @param commandType
     */
    private static void determineWhatUserNeedsHelpWith(String commandType) {
        // check whether the user needs help with specific command
        String cmdToHelpWith = commandType;
        
        // determine the type of command the user wants help with
        CommandType cmdToHelpWithType = extractCommandType(cmdToHelpWith);
        
        // user did not ask for help for a valid command type
        if (cmdToHelpWithType.equals(Constants.CommandType.INVALID)) {
            logger.log(Level.INFO, "show general help");
        } else {
            logger.log(Level.INFO, "show help for specific command");
            setCommandType(determineHelpCommandType(cmdToHelpWithType));
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

    /**
     * Updates the flags that show what useful information the parse result
     * contains after checking each field.
     * 
     * @param date          First date field
     * @param dateToRemind  Date at which to remind user to do task
     * @param priorityLevel Priority level
     * @param id            Task ID
     * @param name          Task name
     * @param description   Task description
     * @param tag           Task tag
     * @param date2         Second date field, used for timed tasks
     * @return              Collection of flags showing presence of useful info
     */
    private static boolean[] updateResultFlags(Date date, Date dateToRemind, 
                                               int priorityLevel, long id, 
                                               String name, String description, 
                                               String tag, Date date2) {
        // flags order: date, dateToRemind, priorityLevel, id, name,
        //              description, tag
        boolean[] resultFlags = new boolean[8];

        if (date != null) {
            resultFlags[0] = true;
        }
        if (dateToRemind != null) {
            resultFlags[1] = true;
        }
        if (priorityLevel != 0) {
            resultFlags[2] = true;
        }
        if (id != 0) {
            resultFlags[3] = true;
        }
        if (!name.equals("")) {
            resultFlags[4] = true;
        }
        if (!description.equals("")) {
            resultFlags[5] = true;
        }
        if (!tag.equals("")) {
            resultFlags[6] = true;
        }
        if (date2 != null) {
            resultFlags[7] = true;
        }
        return resultFlags;
    }

    /**
     * Constructs and returns a result object based on the current result 
     * fields and input command type.
     * 
     * @param commandType Type of command the user input
     * @return            Result of parsing user input string
     */
    private static ParseResult createParseResult(CommandType commandType) {
        return new ParseResult(commandType, date, date2, dateToRemind, 
                               priorityLevel, id, name, description, tag, 
                               errorType, flags);
    }
    
    /**
     * Returns a help-related command type for the result based on which 
     * command the user wants help with.
     * 
     * @param commandType Command that user wants help with
     * @return            Help-related command type
     */
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
    
    /**
     * Returns a convert-related command type for the result based on which 
     * type of task the user wants to convert his task to.
     * 
     * @param convertTypeString Desired type of task to convert to
     * @return                  Convert-related command type
     */
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
    
    /**
     * Parses the input arguments that are expected to be a date representation
     * into a consistent Calendar format.
     * 
     * @param arguments            String representation of a date
     * @param dateBeingParsedIndex Index of the date field being updated (e.g. 
     *                             date, date2)
     * @return                     Parsed date
     */
    private static Calendar parseDate(String arguments, int dateBeingParsedIndex) {
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
            setTimeSetByUserToTrue(dateBeingParsedIndex);
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
                setTimeSetByUserToTrue(dateBeingParsedIndex);
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
                setTimeSetByUserToTrue(dateBeingParsedIndex);
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
                setTimeSetByUserToTrue(dateBeingParsedIndex);
                return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           fourthArg, year, month, day); 
            }
        }
        
        return createCalendar(year, month - 1, day, 0, 0);
    }
    
    
    /**
     * Converts arguments in the form of 'next x time period' (e.g. next 3 
     * days) into a proper date based on the current time and date.
     * 
     * @param arguments String representation of date in form "next ..."
     * @return          Parsed date
     */
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
     *  @param dateParts    String tokens with date info
     *  @param indexToCheck Index expected to contain time string
     *  @param year         Year of desired date
     *  @param month        Month of desired date
     *  @param day          Day of desired date
     *  @return             Representation of full date
     */    
    private static Calendar calcDateGivenTime(String[] dateParts, 
                                                int indexBeingParsed, 
                                                String pmOrAm, int year, 
                                                int month, int day) {
        // check for a valid argument to be parsed as the time
        if (!isTimeArgExistent(indexBeingParsed, dateParts)) {
            // does not exist, return default date value
            return createCalendar(year, month - 1, day, 0, 0);
        }
        
        String timeString = dateParts[indexBeingParsed + 1];
        String[] timeParts = timeString.split("\\.");  
        
        // check for appropriate format (##.##) 
        if (!isValidTimeFormat(indexBeingParsed, timeParts)) {
            // invalid date format, return default date value
            return createCalendar(year, month - 1, day, 0, 0);
        }       

        try {
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            if (hour < 0 || hour > 12 || min < 0 || min > 59) {
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.INVALID_TIME);
                logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to invalid hour/minute given");
                return createCalendar(year, month, day, 0, 0);
            } else if (pmOrAm.equals("pm")) {
                // special case of 12pm
                if (hour == 12) {
                    return createCalendar(year, month - 1, day, hour, min); 
                } else {
                    return createCalendar(year, month - 1, day, hour + HALF_DAY_IN_HOURS, min); 
                }                    
            } else {
                // special case for 12am
                if (hour == 12) {
                    return createCalendar(year, month - 1, day, hour - HALF_DAY_IN_HOURS, min); 
                } else {
                    return createCalendar(year, month - 1, day, hour, min);  
                }                    
            }
            
        } catch (NumberFormatException e) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "error parsing time on argument number " + indexBeingParsed);
            return createCalendar(year, month - 1, day, 0, 0);
        }
        
    }    

    
    /**
     * Helper method for calcDateGivenTime that validates the presence of an 
     * argument for the time keyword.
     * 
     * @param indexBeingParsed Index of time keyword
     * @param dateParts        String tokens with date info
     * @return                 Whether the argument exists
     */
    private static boolean isTimeArgExistent(int indexBeingParsed, String[] dateParts) {
        if (indexBeingParsed + 1 > dateParts.length) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to no token after time keyword");
            return false;
        } else {
            return true;
        }
    }
    
    private static boolean isValidTimeFormat(int indexBeingParsed, String[] timeParts) {  
        if (timeParts.length != 2) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to incorrect format");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Creates a calendar with a time given by the input values. The value of the
     * second is always set to 0.
     * @param year   Desired year
     * @param month  Desired month
     * @param day    Desired day
     * @param hour   Desired hour
     * @param minute Desired minute
     * @return
     */
    private static Calendar createCalendar(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        // first set the second to 0
        calendar.set(0, 0, 0, 0, 0, 0);
        calendar.set(year, month, day, hour, minute);
        return calendar;
    }
    
    /**
     * Verifies that a command with the type Add or Convert has valid fields:
     */
    private static void checkAddConvertHaveValidFields() {       
        if (commandType.equals(CommandType.ADD)) {
            checkAddHasValidName();         
         
        // check for two valid dates in the case of convert timed
        } else if (commandType.equals(CommandType.CONVERT_TIMED)) {
            checkValidDates();
            
        // check for at least one valid date in the case of convert deadline
        } else if (commandType.equals(CommandType.CONVERT_DEADLINE)) {
            checkAtLeastOneValidDate();
        }
    }
    
    /**
     * Checks that the user provided a valid name.
     */
    private static void checkAddHasValidName() {
        if (name.equals("")) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.BLANK_TASK_NAME);
        }
    }
    
    /**
     * Check that both date1 and date2 fields contain valid dates.
     */
    private static void checkValidDates() {
        if (date == null || date2 == null) {
            logger.log(Level.WARNING, "Less than two valid dates for Convert Timed");
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_ARGUMENTS);
        }
    }
    /**
     * Check that there is at least one valid date out of date1 and date2.
     */
    private static void checkAtLeastOneValidDate() {
        logger.log(Level.WARNING, "no valid dates for Convert Deadline");
        if (date == null && date2 == null) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_ARGUMENTS);
        }
    }
    
    /**
     * Sets true the flag showing whether the user has set a particular time 
     * field.
     * 
     * @param targetTimeIndex Index of time field the user has set
     */
    private static void setTimeSetByUserToTrue(int targetTimeIndex) {
        if (targetTimeIndex == 1) {
            isTimeSetByUser = true;
        } else if (targetTimeIndex == 2) {
            isTime2SetByUser = true;
        }
    }
    
    /**
     * Sets default times for the user in the case that they added a task 
     * without specifying the times on their own.
     */
    private static void setDefaultTimesForAdd() {
        if (commandType.equals(CommandType.ADD)) {
            // case of timed task
            if (date != null && date2 != null) {
                setDefaultTimesForTimedAdd();
            // case of deadline task
            } else if (date != null || date2 != null) {
                setDefaultTimeForDeadlineAdd();
            }
        }
    }
    
    /**
     * Sets default times to the start of the day for the first date and the
     * end of the day for the second date if they were not set by the user.
     */
    private static void setDefaultTimesForTimedAdd() {
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
    }
    
    /**
     * Sets default time for any of the fields date and date2 to the end of the
     * day if they were not set by the user.
     */
    private static void setDefaultTimeForDeadlineAdd() {
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
    
    /**
     * Checks that the date represented by date1 is before the date represented
     * by date2.
     */
    private static void checkDate1BeforeDate2() {
        if (date != null && date2 != null) {
            if (!(date.compareTo(date2) < 0)) {
                logger.log(Level.WARNING, "Date 1 not smaller than Date 2");
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.DATE1_NOT_SMALLER_THAN_DATE2);
            }
        }
    }
}
