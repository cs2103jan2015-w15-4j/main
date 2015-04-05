package planner;

import java.awt.Color;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * This class contains all the constants to be used across the planner.
 * 
 * @author kohwaikit
 *
 */
public class Constants {
    
    public enum CommandType {
        ADD, ADD_CLASH, UPDATE, DELETE, SHOW, SHOW_ALL, SHOW_ONE, DONE, SETNOTDONE, UNDO, SEARCH,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_SHOW, HELP_DONE,
        HELP_UNDO, HELP_SEARCH, JUMP, CONVERT, CONVERT_FLOATING, 
        CONVERT_DEADLINE, CONVERT_TIMED, SAVEWHERE, SAVEHERE, INVALID, EXIT
    };
    
    public enum ErrorType {
        INVALID_COMMAND, INVALID_TASK_ID, BLANK_TASK_NAME, INVALID_DATE,
        INVALID_ARGUMENTS, INVALID_TIME, DATE1_NOT_SMALLER_THAN_DATE2,
        INVALID_PRIORITY_LEVEL, TASK_NOT_FOUND, CONVERT_TASK_TYPE_IN_UPDATE,
        UPDATE_NO_CHANGES, NOTHING_TO_UNDO, NO_INPUT
    };
    
    public enum TipType{
        UP_TIP, DOWN_TIP, UPDOWN_TIP
    }
    
    public enum DisplayStateFlag {
        ALL, TODAY, TENTATIVE, OVERDUE, UPCOMING, DONE, NOTDONE, WORD_SEARCH, PRIORITY_SEARCH, INVALID,
        HELP, HELP_ADD, HELP_UPDATE, HELP_DELETE, HELP_DONE, HELP_UNDO, HELP_SEARCH, HELP_PRIORITY_SEARCH
    }
    
    public static final String[] COMMAND_KEYWORDS = {"add", "new", "update", 
        "edit", "change", "del", "delete", "trash", "remove", "show", "display", 
        "done", "completed", "finished", "setnotdone", "undo", "revert", 
        "search", "find", "help", "sos", "jump", "convert", "savewhere", 
        "savehere", "exit" 
    };
    
    public static final String[] NONCOMMAND_KEYWORDS = { "at",
        "on", "date", "from", "by", "due", "until", "to", "jump", "every", "in",
        "priority", "desc", "description", "remind", "tag" };
    

    public static final int ADD_TUTORIAL = 0;
    public static final int DELETE_TUTORIAL = 1;
    public static final int UPDATE_TUTORIAL = 2;
    public static final int DONE_TUTORIAL = 3;
    public static final int SETNOTDONE_TUTORIAL = 4;
    public static final int UNDO_TUTORIAL = 5;
    public static final int SEARCH_TUTORIAL = 6;
    public static final int HELP_TUTORIAL =7;
    public static final int SAVEHERE_TUTORIAL = 8;
    public static final int SAVEWHERE_TUTORIAL = 9;
    
    /**
     * Contains the information for the dropdown box at the GUI input bar.
     */
    public static final String [][] POSSIBLE_COMMANDS = { 
        {"Add tasks (fields besides name optional)", 
         "add <taskname>",
         "add <taskname> by <date>", 
         "new <taskname> date <start date> date2 <end date>",
         "new <taskname> at <date> priority <priority level> desc <description info> tag <tagname>"
        },
        {"Delete tasks", 
         "delete <task id>",
         "trash <task id>",
         "remove <task id>",
         "del <task id>"
        },
        {"Update tasks (number of fields to update optional)", 
         "update <task id> on <date> priority <priority level> desc <description info> tag <tagname>",
         "edit <task id> on <date> priority <priority level> desc <description info> tag <tagname>",
         "change <task id> on <date> priority <priority level> desc <description info> tag <tagname>"
        },
        {"Done",
         "done <task id>",
         "completed <task id>",
         "finished <task id>"
        },
        {"Setnotdone",
         "setnotdone <task id>"
        },
        {"Undo previous modification",
         "undo",
         "revert",              
        },
        {"Search tasks (number of fields to search by optional)",
         "search <task id> on <date> priority <priority level> desc <description info> tag <tagname>",
         "find <task id> on <date> priority <priority level> desc <description info> tag <tagname>"   
        },
        {"Help",
         "help",
         "sos"
        },
        {"Savehere",
         "savehere <full file path>"   
        },
        {"Savewhere",
         "savewhere"
        },
        {"Exit",
         "exit",
         "quit",
         "bye"
        }
    };
    
    /**
     * Contains the information for the GUI help window.
     */
    public static final String [][] HELP_CONTENT = {
        {"Add tasks (fields besides name optional)", 
         "Equivalent commands: add/create/new",
         "This command will add the task into YOPO. You can use this command together with description, time, priority and tag.",
         "Example usage: add revise CS2103",
         "[This will add revise CS2103T as one of the task to complete in YOPO]"
        },
        {"Delete tasks",
         "Equivalent commands: delete/del/trash/remove",
         "This command deletes the task with the specific id.",
         "Example usage: delete 143",
         "[This will delete item 143 from the task list]"
        },
        {"Update tasks (number of fields to update optional)", 
         "Equivalent commands: update/edit/change",
         "This command will update the task with the specified id with the information provided.",
         "Example usage: update 143 revise CS2103T",
         "[This will update item 143 to the name revise CS2103T]"
        },
        {"Set tasks as done",
         "Equivalent commands: done/completed/finished",
         "This command marks the task with the specified id as done.",
         "Example usage: done 143",
         "[This will mark item 143 as done]"
        },
        {"Set tasks as not done",
         "This command marks the task with the specified id as not done.",
         "Example usage: setnotdone 271",
         "[This will mark item 271 as not done]"
        },
        {"Undo previous modification",
         "This command undoes the previous command entered that changed the data.",
         "Example usage: undo",
         "[This will undo the previous command]"             
        },
        {"Search tasks (number of fields to search by optional)",
         "Equivalent commands: search/find",
         "This command will search all tasks stored in YOPO and display all tasks matching the criteria the user specified.",
         "Example usage: search priority 4 description important",
         "[This will display all tasks with priority 4 and above and description containing the word important]"
        },
        {"Help",
         "Equivalent commands: help/sos",
         "This command will bring out the help message, listing the available commands.",
         "Example usage: help",
         "[This will show this help window]"
        },
        {"Changing save data location",
         "format: savehere <full file path>",
         "This command causes all program files to be saved in the location specified by <full file path>",
         "Example usage: save here C:\\",
         "[This will cause all program files to be saved in C:\\]"
        },
        {"Finding out save data location",
         "This command will cause the current save location for program files to be displayed in the display box.",
         "Example usage: savewhere",
         "[This causes the current save location for program files to be displayed in the display box]"
        },
        {"Exit",
         "Equivalent commands: exit/quit/bye",
         "This command will cause the program to exit.",
         "Example usage: exit",
         "[This will exit the program]",
        }
    };
    
    public static String[] NAVIGATION_BAR_STRING_CONTENTS = { "More info for task #",           // F1
                                                              "Previous View",                  // F2
                                                              "Tutorial",                       // F3
                                                              " Task due today",                // F4
                                                              " Tasks due today",
                                                              " Task in total",                 // F5
                                                              " Tasks in total",
                                                              " Upcoming Task",                 // F6
                                                              " Upcoming Tasks",
                                                              " Tentative Task",                // F7
                                                              " Tentative Tasks",
                                                              " Overdue Task",                  // F8
                                                              " Overdue Tasks",
                                                              " Task Done",                     // F9
                                                              " Tasks Done",
                                                              " Task not Done",                 // F10
                                                              " Tasks not Done"};                   
    
    public static Color[] COLOR_SERIES = { new Color( 239, 52, 65 ),
                                           new Color( 244, 132, 57 ),
                                           new Color( 224, 226, 54 ),
                                           new Color( 47, 244, 237 ),
                                           new Color( 74, 243, 110 ),
                                           new Color( 242, 141, 236 ),
                                           new Color( 245, 142, 142 ) };
    
    public static final String CONFIG_FILE_NAME = "config";
    public static final String DEFAULT_STORAGE_NAME = "data";

    public static final int NO_PRIORITY_LEVEL = 0;
    public static final int NO_ID_SET = 0;
    public static final int NAVIGATION_BAR_STRING_CONTENTS_SIZE = 17;
    public static final int NAVIGATION_BARS_MAX_NUM = 10;
    
    EnumMap<ErrorType, String> errorMessages = new EnumMap<ErrorType, String>(ErrorType.class);
    
    public void initializeErrorMessages() {
        errorMessages.put(ErrorType.INVALID_COMMAND, "invalid command type");
        errorMessages.put(ErrorType.INVALID_TASK_ID, "a number must be entered for the task id");
        errorMessages.put(ErrorType.BLANK_TASK_NAME, "the name of the task added cannot be blank");
        errorMessages.put(ErrorType.INVALID_DATE, "Unable to parse date");
        errorMessages.put(ErrorType.INVALID_TIME, "Unable to parse time");
        errorMessages.put(ErrorType.INVALID_PRIORITY_LEVEL, "Please input a priority level from 1 to 5");
        
        //Errors from Engine
        errorMessages.put(ErrorType.TASK_NOT_FOUND, "Task not found!");
        errorMessages.put(ErrorType.CONVERT_TASK_TYPE_IN_UPDATE, "Conversion of"
                + " Task type not allowed in update. Use the convert command");
        errorMessages.put(ErrorType.UPDATE_NO_CHANGES, "No changes in"
                + " update command!");
        errorMessages.put(ErrorType.NOTHING_TO_UNDO, "No actions to undo!");
        errorMessages.put(ErrorType.NO_INPUT, "Please enter a command.");
    }
}
