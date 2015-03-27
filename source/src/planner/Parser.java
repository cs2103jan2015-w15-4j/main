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
    private static String[] keywordsArray = {"at", "on", "by", "tomorrow",
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
    
    private static boolean[] flags = new boolean[8];
    private static Calendar calendar = null;

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
                errorType = Constants.ErrorType.INVALID_COMMAND;
                logger.log(Level.WARNING, "command " + commandType.toString() + " not recognized");
                break;
        }
        logger.log(Level.INFO, "processing ended. returning result.");
        ParseResult parseResult = createParseResult(commandType);        
        return parseResult;
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

            default:
                return CommandType.INVALID;

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
                    logger.log(Level.WARNING, "error parsing id in " + keyword);
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_TASK_ID;
                }
                break;

            case "show":
                try {
                    // check whether next token is an id of the task to show
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                    logger.log(Level.INFO, "successfully parsed id, show" + 
                               "specific task");
                    commandType = Constants.CommandType.SHOW_ONE;
                } catch (NumberFormatException e) {
                    logger.log(Level.INFO, "no id parsed, show all tasks");
                    commandType = Constants.CommandType.SHOW_ALL;
                }
                break;
                
            case "setnotdone":
            case "done":
                try {
                    id = Long.parseLong(keywordArgs.split(" ")[0]);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "error parsing id in done");
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_TASK_ID;
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
                CommandType cmdToHelpWithType = extractCommandType(cmdToHelpWith);
                if (cmdToHelpWithType.equals(Constants.CommandType.INVALID)) {
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
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_TASK_ID;
                }
                
                // determine type to convert to
                commandType = determineConvertType(convertArgs[1]);
                
                break;

            // non command keywords start here
            case "at":            
            case "on":
            case "date":            
            case "from":
            case "by":
            case "due":
                calendar = parseDate(keywordArgs);
                date = calendar.getTime();
                break;
            
            // end date (for timed tasks)
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
        if (commandType.equals(CommandType.ADD)) {
            if (name.equals("")) {
                commandType = Constants.CommandType.INVALID;
                errorType = Constants.ErrorType.BLANK_TASK_NAME;
            }
            
        // check for two valid dates in the case of the convert timed 
        } else if (commandType.equals(CommandType.CONVERT_TIMED)) {
            if (date == null || date2 == null) {
                logger.log(Level.WARNING, "Less than two valid dates for Convert Timed");
                commandType = Constants.CommandType.INVALID;
                errorType = Constants.ErrorType.INVALID_ARGUMENTS;
            }
         // check for at least one valid date in the case of convert deadline
        } else if (commandType.equals(CommandType.CONVERT_DEADLINE)) {
            logger.log(Level.WARNING, "no valid dates for Convert Deadline");
            if (date == null && date2 == null) {
                commandType = Constants.CommandType.INVALID;
                errorType = Constants.ErrorType.INVALID_ARGUMENTS;
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
                // not yet implemented
                return CommandType.INVALID;
                
            case SEARCH:
                return CommandType.HELP_SEARCH;
                
            default:
                errorType = Constants.ErrorType.INVALID_COMMAND;
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
                errorType = Constants.ErrorType.INVALID_COMMAND;
                logger.log(Level.WARNING, "unable to determine convert type");
                return CommandType.INVALID;
        }
    }

    private static Calendar parseDate(String arguments) {
        logger.log(Level.INFO, "beginning date parsing");
        Calendar currentTime = Calendar.getInstance();
        int day = currentTime.get(Calendar.DATE);
        int month = currentTime.get(Calendar.MONTH);
        int year = currentTime.get(Calendar.YEAR);
        int hour = currentTime.get(Calendar.HOUR);
        int minute = currentTime.get(Calendar.MINUTE);
        int tokenBeingParsedIndex = 0;
        
        // the tokens that will individually represent day, month etc
        String[] dateParts = arguments.split(" ");
        assert(dateParts.length > 0);
        // may be a representation of the day, or a time keyword, or the next keyword
        String firstArg = dateParts[tokenBeingParsedIndex].toLowerCase();
        
        // check whether the current argument is a keyword for time
        if (firstArg.equals("pm") || firstArg.equals("am")) {
            return returnDateGivenTime(dateParts, tokenBeingParsedIndex, 
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
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_DATE;
                    logger.log(Level.WARNING, "unable to parse day");
                    return createCalendar(year, month, day, 0, 0);
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
                return returnDateGivenTime(dateParts, tokenBeingParsedIndex, 
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
                        commandType = Constants.CommandType.INVALID;
                        errorType = Constants.ErrorType.INVALID_DATE;
                        logger.log(Level.WARNING, "unable to parse month");
                        return createCalendar(year, month, day, 0, 0);
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
                return returnDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           thirdArg, year, month, day); 

            } else {
                logger.log(Level.INFO, "value expected to be year: " + thirdArg);
                try {
                    year = Integer.parseInt(thirdArg);
                } catch (NumberFormatException e) {
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_DATE;
                    logger.log(Level.WARNING, "unable to parse year");
                }
            }            
        }
        
        // user has input year, month, day, as well as time
        if (dateParts.length == 5) {
            tokenBeingParsedIndex++;
            // expected to be a time keyword
            String fourthArg = dateParts[3].toLowerCase();
            if (fourthArg.equals("pm") || fourthArg.equals("am")) {
                return returnDateGivenTime(dateParts, tokenBeingParsedIndex, 
                                           fourthArg, year, month, day); 
            }
        }
        
        return createCalendar(year, month - 1, day, 0, 0);
    }
    
    
    //Parses whatever that comes after "next" is typed
    //Will delete/change bad comments before refactoring the code
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
                            commandType = Constants.CommandType.INVALID;
                            errorType = Constants.ErrorType.INVALID_DATE;
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

            commandType = Constants.CommandType.INVALID;
            errorType = Constants.ErrorType.INVALID_DATE;
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
    private static Calendar returnDateGivenTime(String[] dateParts, 
                                                int indexBeingParsed, 
                                                String pmOrAm, int year, 
                                                int month, int day) {
        int indexToCheck = indexBeingParsed + 1;
        // check for a valid argument to be parsed as the time
        if (indexToCheck > dateParts.length) {
            commandType = Constants.CommandType.INVALID;
            errorType = Constants.ErrorType.INVALID_TIME;
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to no token after time keyword");
            return createCalendar(year, month, day, 0, 0);
        } 
        String timeString = dateParts[indexToCheck];
        String[] timeParts = timeString.split(".");
        
        // check for appropriate format (##.##)
        if (timeParts.length != 2) {
            commandType = Constants.CommandType.INVALID;
            errorType = Constants.ErrorType.INVALID_TIME;
            logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to incorrect format");
            return createCalendar(year, month, day, 0, 0);
        } else {
            try {
                int hour = Integer.parseInt(timeParts[0]);
                int min = Integer.parseInt(timeParts[1]);
                if (hour < 1 || hour > 12 || min < 0 || min > 59) {
                    commandType = Constants.CommandType.INVALID;
                    errorType = Constants.ErrorType.INVALID_TIME;
                    logger.log(Level.WARNING, "unable to parse time on argument number " + indexBeingParsed + " due to invalid hour/minute given");
                    return createCalendar(year, month, day, 0, 0);
                } else if (pmOrAm.equals("pm")) {
                    return createCalendar(year, month, day, hour + HALF_DAY_IN_HOURS, min); 
                } else {
                    return createCalendar(year, month, day, hour, min); 
                }
                
            } catch (NumberFormatException e) {
                commandType = Constants.CommandType.INVALID;
                errorType = Constants.ErrorType.INVALID_TIME;
                logger.log(Level.WARNING, "error parsing time on argument number " + indexBeingParsed);
                return createCalendar(year, month, day, 0, 0);
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
}
