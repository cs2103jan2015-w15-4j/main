//@author A0108232U
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
    
    // stores the tokens of the user input delimited by spaces
    private static String[] inputTokens = null;
    
    // all keywords that are not a command
    private static String[] nonCommandKeywordsArray = {"at", "on", "from", "by",
        "priority", "desc", "description", "date", "due",
        "remind", "tag", "until", "to"  
    };
    private static ArrayList<String> nonCommandKeywords =
            new ArrayList<String>(Arrays.asList(nonCommandKeywordsArray));
    
    // common ways to type months
    private static String[] monthsArray = {"jan", "january", "feb", "february",
        "mar", "march", "apr", "april", "may", "may", "jun", "june", "jul",
        "july", "aug", "august", "sep", "september", "oct", "october", "nov",
        "november", "dec", "december"
    };
    private static ArrayList<String> months =
            new ArrayList<String>(Arrays.asList(monthsArray));
    
    // common ways to type days
    private static String[] daysInWeek = {"mon", "monday", "tue", "tuesday", 
        "wed", "wednesday", "thu", "thursday", "fri", "friday", "sat", 
        "saturday", "sun", "sunday"
    };
    private static ArrayList<String> days =
            new ArrayList<String>(Arrays.asList(daysInWeek));
    
    // command types that will not take into account any keywords besides the
    // command itself
    private static String[] cmdsWithoutFollowingKeywords = {"help", "undo",
        "delete", "done", "setnotdone", "savewhere", "savehere", "show", "exit"
    };
    private static ArrayList<String> monoKeywordCommands =
            new ArrayList<String>(Arrays.asList(cmdsWithoutFollowingKeywords));
    
    private static final int COMMAND_WORD_INDEX = 0;
    private static final int FIRST_ARG = 0;
    private static final int SECOND_ARG = 1;
    private static final int THIRD_ARG = 2;
    private static final int FOURTH_ARG = 3;
    private static final int NEXT_ARG = 1;
    private static final int FIRST_AFTER_COMMAND_TYPE = 1;
    private static final int HALF_DAY_IN_HOURS = 12;
    private static final int DATE_1_INDEX = 1;
    private static final int DATE_2_INDEX = 2;
    private static final int NO_PRIORITY_LVL = 0;
    private static final int LOWEST_PRIORITY_LVL = 1;
    private static final int HIGHEST_PRIORITY_LVL = 5;
    private static final int DATE_TO_REMIND_INDEX = 3;
    private static final int ONE_WORD = 1;
    private static final int FLAGS_SIZE = 8;
    private static final int DATE_FLAG = 0;
    private static final int DATE_TO_REMIND_FLAG = 1;
    private static final int PRIORITY_LVL_FLAG = 2;
    private static final int ID_FLAG = 3;
    private static final int NAME_FLAG = 4;
    private static final int DESC_FLAG = 5;
    private static final int TAG_FLAG = 6;
    private static final int DATE_2_FLAG = 7;
    private static final int FIRST_TOKEN = 0;
    private static final int ALL_DATE_ARGS = 5;
    private static final int CORRECT_NO_TIME_ARGS = 2;
    private static final int ONLY_DAY_ARG = 1;
    private static final int DAY_AND_MONTH_ARGS = 2;
    private static final int ZERO_HOURS = 0;
    private static final int ZERO_MINUTES = 0;
    private static final int ZERO_SECONDS = 0;
    private static final int ONE_DAY = 1;
    private static final int ONE_MONTH = 1;
    private static final int FIRST_MONTH = 0;
    private static final int ONE_YEAR = 1;
    private static final int DAYS_IN_WEEK = 7;
    private static final int WEEK_DIFFERENCE_IN_DAYS = 0;
    private static final int MONTH_ENGLISH_VARIATIONS = 2;
    private static final int DAY_ENGLISH_VARIATIONS = 2;
    private static final int SMALLEST_HOUR = 0;
    private static final int BIGGEST_HOUR = 12;
    private static final int BIGGEST_24_HOUR_HOUR = 23;
    private static final int SMALLEST_MIN = 0;
    private static final int BIGGEST_MIN = 59;
    private static final int NOT_FOUND = -1;
    private static final String ADD_COMMAND_STR = "add";
    private static final String UPDATE_COMMAND_STR = "update";
    private static final String DEL_COMMAND_STR = "delete";
    private static final String SHOW_COMMAND_STR = "show";
    private static final String DONE_COMMAND_STR = "done";
    private static final String SETNOTDONE_COMMAND_STR = "setnotdone";
    private static final String UNDO_COMMAND_STR = "undo";
    private static final String SEARCH_COMMAND_STR = "search";
    private static final String HELP_COMMAND_STR = "help";
    private static final String JUMP_COMMAND_STR = "jump";
    private static final String CONVERT_COMMAND_STR = "convert";
    private static final String SAVEWHERE_COMMAND_STR = "savewhere";
    private static final String SAVEHERE_COMMAND_STR = "savehere";
    private static final String EXIT_COMMAND_STR = "exit";    
    private static final String DATE_KEYWORD = "date";
    private static final String DEADLINE_TYPE = "deadline";
    private static final String TIMED_TYPE = "timed";
    private static final String FLOATING_TYPE = "floating";
    private static final String PM = "pm";
    private static final String AM = "am";
    private static final String NEXT_KEYWORD = "next";
    private static final String TIME_DELIMITER = "\\.";
    
    // these fields will be used to construct the parseResult
    private static ErrorType errorType = null;
    private static CommandType commandType = null;    
    private static Date date = null;
    private static Date date2 = null;
    private static Date dateToRemind = null;
    private static int priorityLevel = NO_PRIORITY_LVL;
    private static long id = 0;
    private static String name = "";
    private static String description = "";
    private static String tag = "";
    private static boolean isTimeSetByUser = false;
    private static boolean isTime2SetByUser = false;
    private static boolean isParseDateComplete = false;
    private static boolean[] flags = new boolean[FLAGS_SIZE];
    private static Calendar desiredTime = null;
    
    // fields used for parsing date
    private static int year = 0;
    private static int month = 0;
    private static int day = 0;
    
    /**
     * Processes a user input string and returns a result object containing
     * information such as the user's desired command type and the relevant
     * fields to update.
     * 
     * @param inputString The user input.
     * @return            A result containing information such as command type.
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
     * @param inputString The user input.
     * @return            A result containing information such as command type.
     */
    private static void process(String inputString) {
        inputTokens = splitBySpaceDelimiter(inputString);
        assert(inputTokens.length > 0);
        commandType = determineCommandType(inputTokens[COMMAND_WORD_INDEX]);
        processDependingOnCommandType(commandType);
    }
    
    /**
     * Tokenizes a string input.
     * 
     * @param input Input string.
     * @return      Array of tokens.
     */
    private static String[] splitBySpaceDelimiter(String input) {
        return input.split(" ");
    }

    /**
     * Determines the type of command a user wants to execute given the input
     * token expected to be a valid command keyword.
     * 
     * @param commandWord Command keyword.
     * @return            Type of command.
     */
    private static CommandType determineCommandType(String commandWord) {
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
                
            case "exit":
                return CommandType.EXIT;

            default:
                return CommandType.INVALID;
        }
    }
    
    /**
     * Given the command type, processes the rest of the input depending on 
     * what the command is. The command type is converted to a string for 
     * convenience in processing.
     * 
     * @param commandType Type of command being parsed.
     */
    private static void processDependingOnCommandType(CommandType commandType) {
        switch(commandType) {
            case ADD:
                processCommand(ADD_COMMAND_STR);
                break;
                
            case UPDATE:
                processCommand(UPDATE_COMMAND_STR);
                break;
                
            case DELETE:
                processCommand(DEL_COMMAND_STR);
                break;
                
            case SHOW:
                processCommand(SHOW_COMMAND_STR);
                break;
                
            case DONE:
                processCommand(DONE_COMMAND_STR);
                break;
            
            case SETNOTDONE:
                processCommand(SETNOTDONE_COMMAND_STR);
                break;
                
            case UNDO:
                processCommand(UNDO_COMMAND_STR);
                break;
                
            case SEARCH:
                processCommand(SEARCH_COMMAND_STR);
                break;
                
            case HELP:
                processCommand(HELP_COMMAND_STR);
                break;
            
            case JUMP:
                processCommand(JUMP_COMMAND_STR);
                break;
                
            case CONVERT:
                processCommand(CONVERT_COMMAND_STR);
                break;
                
            case SAVEWHERE:
                processCommand(SAVEWHERE_COMMAND_STR);
                break;
            
            case SAVEHERE:
                processCommand(SAVEHERE_COMMAND_STR);
                break;
            
            case EXIT:
                processCommand(EXIT_COMMAND_STR);
                break;
                
            default:
                setErrorType(ErrorType.INVALID_COMMAND);
                logger.log(Level.WARNING, "command " + commandType.toString() + 
                           " not recognized");
                break;
        }
    }
    
    /**
     * Sets the error type result field to the given input.
     * 
     * @param desiredErrorType Error that arose from parsing.
     */
    private static void setErrorType(ErrorType desiredErrorType) {
        errorType = desiredErrorType;
    }
    
    /**
     * Sets the command type result field to the given input.
     * 
     * @param desiredCommandType Command type determined from parsing.
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
        priorityLevel = NO_PRIORITY_LVL;
        id = 0;
        name = "";
        description = "";
        tag = "";
        errorType = null;
        flags = new boolean[FLAGS_SIZE];
        desiredTime = null;
        isTimeSetByUser = false;
        isTime2SetByUser = false;    
        isParseDateComplete = false;
    }

    /**
     * Checks whether input word is a non command keyword.
     * 
     * @param word Keyword.
     * @return     Whether the keyword is a non command keyword.
     */
    private static Boolean isNonCmdKeyword(String word) {
        return nonCommandKeywords.contains(word);
    }
    
    /**
     * Processes the rest of the input given the command word the user used.
     * 
     * @param commandWord User's desired command type.
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
     * @param commandWord The initial keyword representing the command type.
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
                if (monoKeywordCommands.contains(keywordBeingProcessed)) {
                    break;
                } else if (keywordBeingProcessed.equals(JUMP_COMMAND_STR)) {
                    /* allow users to say use "jump date <date>" as well as 
                     * "jump <date>" */
                    if (wordBeingProcessed.equals(DATE_KEYWORD)) {
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
     * @param keyword The keyword for which the arguments will be processed.
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
     *                         processed.
     * @param keywordArgs      The arguments of the keyword with escape 
     *                         characters removed.
     * @param keywordArgsArray The tokenized arguments of the keyword with 
     *                         escape characters removed.
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
                updatePriorityLevel(keywordArgsArray[FIRST_ARG]);                
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
     *                         processed.
     * @param keywordArgs      The arguments of the keyword with escape 
     *                         characters removed.
     * @param keywordArgsArray The tokenized arguments of the keyword with 
     *                         escape characters removed.
     */
    private static void processCmdKeywordArgs(String keyword, 
                                              String keywordArgs,
                                              String[] keywordArgsArray) {
        switch(keyword) {            
            case "add":
                name = keywordArgs.trim();
                break;
    
            case "update":
                updateId(keywordArgsArray[FIRST_ARG], keyword);
                updateName(keywordArgsArray);
                
            case "delete":
            case "setnotdone":
            case "done":
                updateId(keywordArgsArray[FIRST_ARG], keyword);                
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
                determineWhatUserNeedsHelpWith(keywordArgsArray[FIRST_ARG]);
                break;
            
            case "convert":
                // get id of task to convert
                updateId(keywordArgsArray[FIRST_ARG], keyword); 
                
                // determine type to convert to
                setCommandType(determineConvertType(
                               keywordArgsArray[SECOND_ARG]));             
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
     * @param desiredLevel Priority level.
     */
    private static void updatePriorityLevel(String desiredLevel) {
        try {
            priorityLevel = Integer.parseInt(desiredLevel);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "error parsing priority level");
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_PRIORITY_LEVEL);
        }
        
        if (priorityLevel < LOWEST_PRIORITY_LVL || 
            priorityLevel > HIGHEST_PRIORITY_LVL) {
            
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_PRIORITY_LEVEL);
        }
    }
    
    /**
     * Updates the ID field after processing a string representing the target
     * id of the command. Keyword is input for logging purposes.
     * 
     * @param targetIdString     Target task of command.
     * @param keywordBeingParsed Keyword being parsed.
     */
    private static void updateId(String targetIdString, 
                                 String keywordBeingParsed) {
        try {
            id = Long.parseLong(targetIdString);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "error parsing id in " + 
                       keywordBeingParsed);
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TASK_ID);
        }
    }
    
    private static void updateName(String[] keywordArgs) {
        if (keywordArgs.length > ONE_WORD) {
            String nameToUpdateTo = "";
            for (int i = 1; i < keywordArgs.length; i++) {
                nameToUpdateTo += keywordArgs[i] + " ";
            }
            name = nameToUpdateTo.trim();
        }        
    }
    
    /**
     * Updates the selected date result field based on the keyword arguments.
     * 
     * @param keywordArgs       The arguments of the keyword, expected to be 
     *                          date data.
     * @param dateFieldToUpdate Index representing which date field to update.
     */
    private static void updateDate(String keywordArgs, int dateFieldToUpdate) {
        desiredTime = parseDate(keywordArgs, dateFieldToUpdate);
        if (desiredTime != null) {
            if (dateFieldToUpdate == DATE_1_INDEX) {
                date = desiredTime.getTime();
            } else if (dateFieldToUpdate == DATE_2_INDEX) {
                date2 = desiredTime.getTime();
            } else {
                dateToRemind = desiredTime.getTime();
            }
        }      
    }
    
    /**
     * Updates the command type to a help-related command type if the user has
     * specified a valid command type as an argument.
     * 
     * @param commandType Type of command user needs help with.
     */
    private static void determineWhatUserNeedsHelpWith(String commandType) {
        // check whether the user needs help with specific command
        String cmdToHelpWith = commandType;
        
        // determine the type of command the user wants help with
        CommandType cmdToHelpWithType = determineCommandType(cmdToHelpWith);
        
        // user did not ask for help for a valid command type
        if (cmdToHelpWithType.equals(Constants.CommandType.INVALID)) {
            logger.log(Level.INFO, "show general help");
        } else {
            logger.log(Level.INFO, "show help for specific command");
            setCommandType(determineHelpCmdType(cmdToHelpWithType));
        }
    }
    
    /**
     * Remove all instances of the escape character '/' from the given input 
     * string, which is expected to be the arguments of a keyword.
     * 
     * @param  inputString Arguments of the keyword being processed.
     * @return             The input string with the character removed.
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
     * @param date          First date field.
     * @param dateToRemind  Date at which to remind user to do task.
     * @param priorityLevel Priority level.
     * @param id            Task ID.
     * @param name          Task name.
     * @param description   Task description.
     * @param tag           Task tag.
     * @param date2         Second date field, used for timed tasks.
     * @return              Flags showing presence/absence of useful info.
     */
    private static boolean[] updateResultFlags(Date date, Date dateToRemind, 
                                               int priorityLevel, long id, 
                                               String name, String description, 
                                               String tag, Date date2) {
        /* flags order: date, dateToRemind, priorityLevel, id, name,
                        description, tag */
        boolean[] resultFlags = new boolean[FLAGS_SIZE];

        if (date != null) {
            resultFlags[DATE_FLAG] = true;
        }
        if (dateToRemind != null) {
            resultFlags[DATE_TO_REMIND_FLAG] = true;
        }
        if (priorityLevel != 0) {
            resultFlags[PRIORITY_LVL_FLAG] = true;
        }
        if (id != 0) {
            resultFlags[ID_FLAG] = true;
        }
        if (!name.equals("")) {
            resultFlags[NAME_FLAG] = true;
        }
        if (!description.equals("")) {
            resultFlags[DESC_FLAG] = true;
        }
        if (!tag.equals("")) {
            resultFlags[TAG_FLAG] = true;
        }
        if (date2 != null) {
            resultFlags[DATE_2_FLAG] = true;
        }
        return resultFlags;
    }

    /**
     * Constructs and returns a result object based on the current result 
     * fields and input command type.
     * 
     * @param commandType Type of command the user input.
     * @return            Result of parsing user input string.
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
     * @param commandType Command that user wants help with.
     * @return            Help-related command type.
     */
    private static CommandType determineHelpCmdType(CommandType commandType) {
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
     * @param convertTypeString Desired type of task to convert to.
     * @return                  Convert-related command type.
     */
    private static CommandType determineConvertType(String convertTypeString) {
        switch (convertTypeString.trim()) {
            case DEADLINE_TYPE:
                return CommandType.CONVERT_DEADLINE;
                
            case FLOATING_TYPE:
                return CommandType.CONVERT_FLOATING;
                
            case TIMED_TYPE:
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
     * @param arguments            String representation of a date.
     * @param dateBeingParsedIndex Index of the date field being updated (e.g. 
     *                             date, date2).
     * @return                     Parsed date.
     */
    private static Calendar parseDate(String arguments, 
                                      int dateBeingParsedIndex) {
        logger.log(Level.INFO, "beginning date parsing");
        desiredTime = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        updateDateFields(desiredTime);
        int tokenBeingParsedIndex = FIRST_TOKEN;
        // the tokens that will individually represent day, month etc
        String[] dateParts = splitBySpaceDelimiter(arguments);
        assert(dateParts.length > 0);
        
        parseFirstDateArg(dateParts, arguments, tokenBeingParsedIndex, 
                          dateBeingParsedIndex, currentTime);
        if (isParseDateComplete) {
            return desiredTime;
        }
        
        // now parsing second token
        tokenBeingParsedIndex++;
        parseSecondDateArg(dateParts, arguments, tokenBeingParsedIndex, 
                           dateBeingParsedIndex, currentTime);
        if (isParseDateComplete) {
            return desiredTime;
        }
        
        // now parsing third token
        tokenBeingParsedIndex++;
        parseThirdDateArg(dateParts, arguments, tokenBeingParsedIndex, 
                          dateBeingParsedIndex);
        if (isParseDateComplete) {
            return desiredTime;
        }        
        
        tokenBeingParsedIndex++;     
        
        if (dateParts.length == ALL_DATE_ARGS) {
            // parsing 4th/5th token, user has input year, month, day, and time
            parseFourthAndFithDateArg(dateParts, arguments, 
                                      tokenBeingParsedIndex, 
                                      dateBeingParsedIndex);         
            return desiredTime;
        }
        
        logger.log(Level.INFO, "ending date parsing");
        
        // date was parsed with a simple day month year format.
        return createCalendar(year, month - ONE_MONTH, day, 0, 0);
    }
    
    /**
     * Updates the individual date related fields with the given calendar.
     * @param desiredTime Time to update to.
     */
    private static void updateDateFields(Calendar desiredTime) {
        day = desiredTime.get(Calendar.DATE);
        month = desiredTime.get(Calendar.MONTH);
        year = desiredTime.get(Calendar.YEAR);
    }
    
    /**
     * Processes the first date argument.
     * 
     * @param dateParts             Tokens that represent day, month, etc.
     * @param arguments             All the date arguments.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     * @param existingTime          Existing time.
     */
    private static void parseFirstDateArg(String[] dateParts, String arguments, 
                                          int tokenBeingParsedIndex, 
                                          int dateBeingParsedIndex, 
                                          Calendar existingTime) {
        // may be the day, a time keyword, or the next keyword
        String firstArg = dateParts[tokenBeingParsedIndex].toLowerCase();
        
        try {
            day = Integer.parseInt(firstArg);
            
            // check whether there are no more args to be processed
            if (dateParts.length == ONLY_DAY_ARG) {
                /* handle whether the next occurrence of the specified day 
                 * occurs this month or next month */
                if (day < existingTime.get(Calendar.DATE)) {
                    desiredTime = createCalendar(year, month + ONE_MONTH, day, 
                                                 ZERO_HOURS, ZERO_MINUTES); 
                } else {
                    desiredTime = createCalendar(year, month, day, ZERO_HOURS, 
                                                 ZERO_MINUTES);
                }
            }
        } catch (NumberFormatException e) {
            // try parsing as a non standard format (e.g. time field, next day)
            calcDiffFormatDateOnFirstDateArg(arguments, dateParts, 
                                             tokenBeingParsedIndex, 
                                             dateBeingParsedIndex,
                                             firstArg);
        }
    }
    
    /**
     * Processes the second date argument.
     * 
     * @param dateParts             Tokens that represent day, month, etc.
     * @param arguments             All the date arguments.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     * @param existingTime          Existing time.
     */
    private static void parseSecondDateArg(String[] dateParts, String arguments, 
                                           int tokenBeingParsedIndex, 
                                           int dateBeingParsedIndex, 
                                           Calendar existingTime) {
        // may be a representation of the month, or a time keyword
        String secondArg = dateParts[tokenBeingParsedIndex].toLowerCase();
            
        try {
            month = Integer.parseInt(secondArg);
            
            // check whether there are no more args to be processed
            if (dateParts.length == DAY_AND_MONTH_ARGS) {
                /* handle whether the next occurrence of the specified month 
                 * occurs this year or next year */
                if (month < existingTime.get(Calendar.MONTH)) {
                    desiredTime = createCalendar(year + ONE_YEAR, 
                                                 month - ONE_MONTH, day, 
                                                 ZERO_HOURS, ZERO_MINUTES);
                } else {
                    desiredTime = createCalendar(year, month - ONE_MONTH, day, 
                                                 ZERO_HOURS, ZERO_MINUTES);
                }
                isParseDateComplete = true;
            }
        } catch (NumberFormatException e) {            
            int monthIndex = months.indexOf(secondArg.toLowerCase());
            // check whether it is an English month string
            if (monthIndex != NOT_FOUND) {
                month = (monthIndex / MONTH_ENGLISH_VARIATIONS) + ONE_MONTH;
            } else {
                desiredTime = calcTimeOnDateArg(arguments, dateParts, 
                                         tokenBeingParsedIndex, 
                                         dateBeingParsedIndex,
                                         secondArg);                
            }
        }
    }
    
    /**
     * Processes the third date argument.
     * 
     * @param dateParts             Tokens that represent day, month, etc.
     * @param arguments             All the date arguments.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     */
    private static void parseThirdDateArg(String[] dateParts, String arguments, 
                                          int tokenBeingParsedIndex, 
                                          int dateBeingParsedIndex) {
        // may be a representation of the year, or a time keyword
        String thirdArg = dateParts[THIRD_ARG].toLowerCase();
        
        try {
            year = Integer.parseInt(thirdArg);
        } catch (NumberFormatException e) {
            desiredTime = calcTimeOnDateArg(arguments, dateParts, 
                                            tokenBeingParsedIndex, 
                                            dateBeingParsedIndex,
                                            thirdArg);            
        }
    }
    
    /**
     * Processes the fourth and fifth date arguments, expected to be for time.
     * 
     * @param dateParts             Tokens that represent day, month, etc.
     * @param arguments             All the date arguments.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     */
    private static void parseFourthAndFithDateArg(String[] dateParts, 
                                                  String arguments, 
                                                  int tokenBeingParsedIndex, 
                                                  int dateBeingParsedIndex) {
        // expected to be a time keyword
        String fourthArg = dateParts[FOURTH_ARG].toLowerCase();
        
        desiredTime = calcTimeOnDateArg(arguments, dateParts, 
                                        tokenBeingParsedIndex, 
                                        dateBeingParsedIndex,
                                        fourthArg);
    }
    
    /**
     * Processes the first argument of the arguments for date. Expected to 
     * be either a time keyword (pm/am) or 'next' to signify a command similar
     * to 'next day'.
     * 
     * @param dateParts             Tokens that represent day, month, etc.
     * @param arguments             All the date arguments.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     * @param dateArg               First argument of the date.
     */
    private static void calcDiffFormatDateOnFirstDateArg(String arguments,
            String[] dateParts, int tokenBeingParsedIndex, 
            int dateBeingParsedIndex, String firstArg) {
        
        // check whether the current argument is a keyword for time
        if (firstArg.equals(PM) || firstArg.equals(AM)) {
            setTimeSetByUserToTrue(dateBeingParsedIndex);
            desiredTime = calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                            firstArg);            
        
        // use parseNext when "next" is the first argument
        } else if (firstArg.toLowerCase().trim().equals(NEXT_KEYWORD)) {
            desiredTime = parseNext(arguments);
        } else { 
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_DATE);
            logger.log(Level.WARNING, "unable to parse day");
            desiredTime = null;
        }
        isParseDateComplete = true;
    }
    
    /**
     * Calculate the time given an argument that is expected to be a time 
     * keyword (am/pm).
     * 
     * @param arguments             All the date arguments.
     * @param dateParts             Tokens that represent day, month, etc.
     * @param tokenBeingParsedIndex Index of the token being parsed.
     * @param dateBeingParsedIndex  Either date1 or date2.
     * @param dateArg               Expected to be am/pm.
     * @return                      Result date/time.
     */
    private static Calendar calcTimeOnDateArg(String arguments,
            String[] dateParts, int tokenBeingParsedIndex, 
            int dateBeingParsedIndex, String dateArg) {
        // check whether the current argument is a keyword for time
        if (dateArg.equals(PM) || dateArg.equals(AM)) {
            setTimeSetByUserToTrue(dateBeingParsedIndex);
            return calcDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                       dateArg); 
            
        } else {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_DATE);
            logger.log(Level.WARNING, "unable to parse date");
            return null;                
         }
    }   
    
    /**
     * Converts arguments in the form of 'next x time period' (e.g. next 3 
     * days) into a proper date based on the current time and date.
     * 
     * @param arguments String representation of date in form "next ...".
     * @return          Parsed date.
     */
    private static Calendar parseNext(String arguments) {
        String[] dateParts = splitBySpaceDelimiter(arguments);
        String secondArg = dateParts[SECOND_ARG].toLowerCase().trim();
        Calendar existingTime = Calendar.getInstance();
        int year = existingTime.get(Calendar.YEAR);
        int month = existingTime.get(Calendar.MONTH);
        int date = existingTime.get(Calendar.DATE);
        int day = existingTime.get(Calendar.DAY_OF_WEEK);
        try {
            switch(secondArg) {
                case "day":
                    return createCalendar(year, month, date + ONE_DAY, 
                                          ZERO_HOURS, ZERO_MINUTES);
                
                case "month":
                    return createCalendar(year, month + ONE_MONTH, ONE_DAY, 
                                          ZERO_HOURS, ZERO_MINUTES);
                    
                case "year":
                    return createCalendar(year + ONE_YEAR, FIRST_MONTH, ONE_DAY, 
                                          ZERO_HOURS, ZERO_MINUTES);
                    
                case "week":                     
                    return createCalendarForNextWeek(year, month, date, day);
                    
                default:                    
                    return createCalendarForNextNamedTime(secondArg, date, day);
            }
        } catch (NumberFormatException e) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_DATE);
            logger.log(Level.WARNING, "unable to parse date");
        }
        return createCalendar(year, month, date, ZERO_HOURS, ZERO_MINUTES);        
    }
    
    /**
     * Handles the special case of 'next week' when parsing a date.
     * 
     * @param existingYear      Existing year.
     * @param existingMonth     Existing month.
     * @param existingDate      Existing day of the month.
     * @param existingDayOfWeek Existing day of the week.
     * @return                  Parsed date.
     */
    private static Calendar createCalendarForNextWeek(int existingYear, 
                                                      int existingMonth, 
                                                      int existingDate, 
                                                      int existingDayOfWeek) {
        /* Calendar forces monday = 2, sunday = 1. If current day is monday,
         * difference calculated after %7 is 0 so needs to be changed back to 7
         */
        int daysDifference = (DAYS_IN_WEEK - existingDayOfWeek) % DAYS_IN_WEEK;
        if (daysDifference == WEEK_DIFFERENCE_IN_DAYS) {
            daysDifference = DAYS_IN_WEEK;
        }
        return createCalendar(existingYear, existingMonth, 
                              existingDate + daysDifference, ZERO_HOURS, 
                              ZERO_MINUTES);
    }
    
    /**
     * Handles the special case of parsing a named time such as 'next Friday'
     * or 'next October'.
     * 
     * @param dateArg           Expected to be the name (e.g. Friday, Oct).
     * @param existingDate      Existing day of the month.
     * @param existingDayOfWeek Existing day of the week.
     * @return                  Parsed date.
     */
    private static Calendar createCalendarForNextNamedTime(String dateArg, 
            int existingDate, int existingDayOfWeek) {
        // check if argument is a month in English
        int index = months.indexOf(dateArg.toLowerCase().trim());
        if (index == NOT_FOUND) {
            
            // check if argument is a day of the week
            index = days.indexOf(dateArg.toLowerCase().trim());
            if (index == NOT_FOUND) {                
                // invalid argument, set error
                setCommandType(CommandType.INVALID);
                setErrorType(ErrorType.INVALID_DATE);
                return null;
            } else {
                
                /* calculate differences since mon = 2 for calendar while 
                   mon = 1 from arraylist index */
                int dayDiff = DAYS_IN_WEEK + (index / DAY_ENGLISH_VARIATIONS) + 
                              ONE_DAY - (existingDayOfWeek - ONE_DAY);
                System.out.println(existingDayOfWeek);
                return createCalendar(year, month, existingDate + dayDiff, 
                                      ZERO_HOURS, ZERO_MINUTES);
            }
        } else {
            int monthRequested = (index / MONTH_ENGLISH_VARIATIONS) + ONE_MONTH;
            return createCalendar(year + ONE_YEAR, monthRequested - ONE_MONTH, 
                                  ONE_DAY, ZERO_HOURS, ZERO_MINUTES);                    
        }
    }
    
    /**
     *  Takes in an array of strings containing date info and the expected 
     *  index containing the time string, and returns a Calendar constructed 
     *  with all the date info.
     *  
     *  @param dateParts    String tokens with date info.
     *  @param indexToCheck Index expected to contain time string.
     *  @param pmOrAm       Differentiates which half of the day it is.
     *  @return             Parsed date.
     */    
    private static Calendar calcDateGivenTime(String[] dateParts, 
                                              int indexBeingParsed, 
                                              String pmOrAm) {
        // check for a valid argument to be parsed as the time
        if (!isTimeArgExistent(indexBeingParsed, dateParts)) {
            // does not exist, return default date value
            return createCalendar(year, month - ONE_MONTH, day, ZERO_HOURS, 
                                  ZERO_MINUTES);
        }
        
        String timeString = dateParts[indexBeingParsed + NEXT_ARG];
        String[] timeParts = timeString.split(TIME_DELIMITER);  
        
        // check for appropriate format (##.##) 
        if (!isValidTimeFormat(indexBeingParsed, timeParts)) {
            // invalid date format, return default date value
            return createCalendar(year, month - ONE_MONTH, day, ZERO_HOURS, 
                                  ZERO_MINUTES);
        }       

        try {
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            if (!isValidHour(hour) || !isValidMin(min)) {
                // hour and/or minute not valid, return default date value
                return createCalendar(year, month, day, ZERO_HOURS, 
                                      ZERO_MINUTES);
            }
            
            return calcDateConsideringAmOrPm(pmOrAm, hour, min);            
            
        } catch (NumberFormatException e) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "error parsing time on argument number " + 
                       indexBeingParsed);
            return createCalendar(year, month - ONE_MONTH, day, ZERO_HOURS, 
                                  ZERO_MINUTES);
        }
        
    }
    
    /**
     * Helper method for calcDateGivenTime that validates the presence of an 
     * argument for the time keyword.
     * 
     * @param indexBeingParsed Index of time keyword.
     * @param dateParts        String tokens with date info.
     * @return                 Whether the argument exists.
     */
    private static boolean isTimeArgExistent(int indexBeingParsed, 
                                             String[] dateParts) {
        if (indexBeingParsed + NEXT_ARG > dateParts.length) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument " + 
                       "number " + indexBeingParsed + 
                       " due to no token after time keyword");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Helper method for calcDateGivenTime that validates that the time has 
     * two parts (expected to be hour and minute). Index of time keyword is 
     * input for logging purposes.
     * 
     * @param indexBeingParsed Index of time keyword.
     * @param timeParts        Hour and minute.
     * @return                 Whether there are two parts.
     */
    private static boolean isValidTimeFormat(int indexBeingParsed, String[] timeParts) {  
        if (timeParts.length != CORRECT_NO_TIME_ARGS) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument " + 
                       "number " + indexBeingParsed + 
                       " due to incorrect format");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Checks whether the input hour is between 0 and 12 inclusive.
     * 
     * @param hour Hour of the day.
     * @return     Whether the hour is valid.
     */
    private static boolean isValidHour(int hour) {
        if (hour < SMALLEST_HOUR || hour > BIGGEST_HOUR) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument " +
                                      "due to invalid hour given");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Checks whether the input minute is between 0 and 59 inclusive.
     * 
     * @param min Minute of the day.
     * @return    Whether the minute is valid.
     */
    private static boolean isValidMin(int min) {
        if (min < SMALLEST_MIN || min > BIGGEST_MIN) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_TIME);
            logger.log(Level.WARNING, "unable to parse time on argument " + 
                       "due to invalid minute given");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Helper method for calcDateGivenTime that finishes calculating the result
     * based on the fields calculated so far as well as whether the time given 
     * is in PM or AM.
     * 
     * @param pmOrAm Differentiates which half of the day it is.
     * @param hour   Desired hour.
     * @param min    Desired min.
     * @return       Resulting date.
     */
    private static Calendar calcDateConsideringAmOrPm(String pmOrAm, int hour, 
                                                      int min) {
        assert(pmOrAm.equals(PM) || pmOrAm.equals(AM));
        if (pmOrAm.equals(PM)) {
            // special case of 12pm
            if (hour == BIGGEST_HOUR) {
                return createCalendar(year, month - ONE_MONTH, day, hour, min); 
            } else {
                return createCalendar(year, month - ONE_MONTH, day, 
                                      hour + HALF_DAY_IN_HOURS, min); 
            }
        // am case
        } else {
            // special case for 12am
            if (hour == BIGGEST_HOUR) {
                return createCalendar(year, month - ONE_MONTH, day,
                                      hour - HALF_DAY_IN_HOURS, min); 
            } else {
                return createCalendar(year, month - ONE_MONTH, day, hour, min);  
            }                    
        }
    }
    
    /**
     * Creates a calendar with a time given by the input values. The value of the
     * second is always set to 0.
     * 
     * @param year   Desired year.
     * @param month  Desired month.
     * @param day    Desired day.
     * @param hour   Desired hour.
     * @param minute Desired minute.
     * @return       Resulting date.
     */
    private static Calendar createCalendar(int desiredYear, int desiredMonth, 
                                           int desiredDay, int hour, 
                                           int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(desiredYear, desiredMonth, desiredDay, hour, minute, 
                     ZERO_SECONDS);
        return calendar;
    }
    
    /**
     * Verifies that a command with the type Add or Convert has valid fields.
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
            logger.log(Level.WARNING, "Less than two valid dates for " + 
                       "Convert Timed");
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_NUMBER_OF_DATES);
        }
    }
    /**
     * Check that there is at least one valid date out of date1 and date2.
     */
    private static void checkAtLeastOneValidDate() {
        logger.log(Level.WARNING, "no valid dates for Convert Deadline");
        if (date == null && date2 == null) {
            setCommandType(CommandType.INVALID);
            setErrorType(ErrorType.INVALID_NUMBER_OF_DATES);
        }
    }
    
    /**
     * Sets true the flag showing whether the user has set a particular time 
     * field.
     * 
     * @param targetDateIndex Index of date field the user has set.
     */
    private static void setTimeSetByUserToTrue(int targetDateIndex) {
        if (targetDateIndex == DATE_1_INDEX) {
            isTimeSetByUser = true;
        } else if (targetDateIndex == DATE_2_INDEX) {
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
            desiredTime.setTime(date);
            int existingYear = desiredTime.get(Calendar.YEAR);
            int existingMonth = desiredTime.get(Calendar.MONTH);
            int existingDay = desiredTime.get(Calendar.DATE);
            desiredTime.set(existingYear, existingMonth, existingDay, 
                            ZERO_HOURS, ZERO_MINUTES);
            date = desiredTime.getTime();
        }
        if (!isTime2SetByUser) {
            // set the default time for second date to end of the day
            desiredTime.setTime(date2);
            int existingYear = desiredTime.get(Calendar.YEAR);
            int existingMonth = desiredTime.get(Calendar.MONTH);
            int existingDay = desiredTime.get(Calendar.DATE);
            desiredTime.set(existingYear, existingMonth, existingDay, 
                            BIGGEST_24_HOUR_HOUR, BIGGEST_MIN);
            date2 = desiredTime.getTime();
        }
    }
    
    /**
     * Sets default time for any of the fields date and date2 to the end of the
     * day if they were not set by the user.
     */
    private static void setDefaultTimeForDeadlineAdd() {
        if (!isTimeSetByUser && date != null) {
            // set the default time for the date to end of the day
            desiredTime.setTime(date);
            int existingYear = desiredTime.get(Calendar.YEAR);
            int existingMonth = desiredTime.get(Calendar.MONTH);
            int existingDay = desiredTime.get(Calendar.DATE);
            desiredTime.set(existingYear, existingMonth, existingDay, 
                            BIGGEST_24_HOUR_HOUR, BIGGEST_MIN);
            date = desiredTime.getTime();
        }
        
        if (!isTime2SetByUser && date2 != null) {
            // set the default time for the date to end of the day
            desiredTime.setTime(date2);
            int existingYear = desiredTime.get(Calendar.YEAR);
            int existingMonth = desiredTime.get(Calendar.MONTH);
            int existingDay = desiredTime.get(Calendar.DATE);
            desiredTime.set(existingYear, existingMonth, existingDay, 
                            BIGGEST_24_HOUR_HOUR, BIGGEST_MIN);
            date2 = desiredTime.getTime();
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
