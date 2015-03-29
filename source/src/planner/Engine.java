package planner;

import java.io.IOException;
import java.util.Stack;

/**
 * This class handles the main flow of the program. It provides an interface
 * for the UI to interact with the other components. Engine stores the state
 * of the program, controls the flow of data and commands, and executes the
 * changes to the data. Provides accessors for GUI to access the different
 * types of tasks.
 * 
 * @author kohwaikit
 *
 */
public class Engine {
    
    private static Configuration config;
    
    private static TaskList allTasks;
    private static TaskList doneTasks;
    private static TaskList undoneTasks;
    private static TaskList tentativeTasks;
    private static TaskList normalTasks;
    private static TaskList searchResults;
    
    private static long lastModifiedTask;
    
    private static Storage storage;
    private static Stack<TaskList> previousStates;
    
    /**
     * Returns true if the config was new, i.e. no older config was read or 
     * found.
     * 
     * @return
     */
    public static boolean isFirstRun() {
        return config.isNew();
    }
    
    /**
     * Returns the ID of the last modified task.
     * 
     * @return
     */
    public static long lastModifiedTask() {
        return lastModifiedTask;
    }
    
    /**
     * Routine to initialise engine. Reads the storage and prepares the 
     * TaskLists to be used.
     * 
     * @return success of the process
     */
    //Not tested yet
    public static boolean init() {
        try {
            //Initiates the storage and read the config and storage files
            storage = new Storage();
            config = storage.readConfig();
            allTasks = storage.readTaskStorage(config.getStoragePath());
            
            doneTasks = new TaskList();
            undoneTasks = new TaskList();
            tentativeTasks = new TaskList();
            normalTasks = new TaskList();
            
            //Initiates stack to be used for undo
            previousStates = new Stack<TaskList>();
            
            //Updates the list for display
            refreshLists();
            
            System.out.println(allTasks.size());
            return true;
        } catch(NullPointerException e) {
            
            System.out.println("read error");
            return false;
            
        }
    }
    
    /**
     * Routine to be executed before quitting the program. This method saves
     * the configuration and all the tasks and returns status of these save
     * operations
     * @return
     */
    //Not tested yet
    public static boolean exit() {
        try {
            
            storage.saveConfiguration(config);
            storage.saveTaskList(config.getStoragePath(), allTasks);
            
            System.out.println(allTasks.size());
            return true;
            
        } catch(IOException e) {
            
            System.out.println("write error");
            return false;
            
        }
    }
    
    /**
     * Rebuilds the TaskLists with the latest allTasks such that
     * the TaskLists that GUI requests will be up to date.
     */
    //Not tested yet
    private static void refreshLists() {
        
        //Logic.sortTaskListByPriority(allTasks);
        //Logic.sortTaskListByDate(allTasks);
        
        //Clears TaskLists
        doneTasks.clear();
        undoneTasks.clear();
        normalTasks.clear();
        tentativeTasks.clear();
        
        //Rebuilds TaskLists
        doneTasks = Logic.searchDone(allTasks);
        undoneTasks = Logic.searchNotDone(allTasks);
        normalTasks = Logic.searchConfirmed(undoneTasks);
        tentativeTasks = Logic.searchTentative(undoneTasks);
        
    }
    
    /**
     * Copies the current allTasks and pushes the copy into the
     * previousStates, such that older states of the program is stored and 
     * can be popped to be used for undo.
     */
    private static void pushState() {
        previousStates.push(new TaskList(allTasks));
    }
    
    /**
     * Handles the add command. If the first date is missing but second date
     * is present, the second date will be promoted to the first date, and 
     * the task is treated as a deadline task. Otherwise the first date is
     * is used to create the task object. If date is null then the task is
     * a floating task. Otherwise second date is checked and if present
     * end date of the task is set and the task is a timed task.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType addTask (ParseResult result) {
        //Previous state is saved
        pushState();
        
        
        boolean[] flags = result.getCommandFlags();
        Task newTask;
        
        if(!flags[0] && flags[7]) {
            //If first date is absent but second is present
            newTask = new Task(result.getName(), result.getDescription(), 
                    result.getSecondDate(), result.getPriorityLevel(), 
                    result.getTag(), config.getNewTaskNumber());
        } else {
            newTask = new Task(result.getName(), result.getDescription(), 
                    result.getDate(), result.getPriorityLevel(), 
                    result.getTag(), config.getNewTaskNumber());
            if(flags[7]) {
                //If second date is present
                newTask.setEndDate(result.getSecondDate());
            }
        }
        
        //Add the task
        allTasks.add(newTask);
        //Refresh the lists for display
        refreshLists();
        //Record the last updated task
        lastModifiedTask = newTask.getID();
        
        return Constants.CommandType.ADD;
    }
    
    /**
     * Handles the update command. Update checks for the presences of all
     * updatable fields and updates the specified task accordingly. Returns
     * Invalid if the task to be updated is not found, or nothing was updated.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType updateTask (ParseResult result) {
        //Saves previous state
        pushState();
        
        boolean[] flags = result.getCommandFlags();
        
        //Flag to check if nothing was updated
        boolean nothing = true;
        
        //Finds the task of the right ID
        long ID = result.getId();
        Task toBeUpdated = allTasks.getTaskByID(ID);
        
        if(toBeUpdated == null) {
            //If no such task was found, command was invalid
            return Constants.CommandType.INVALID;
        }
        
        //Checking for fields and setting them if present in command
        if(flags[0]) {
            nothing = false;
            toBeUpdated.setDueDate(result.getDate());
        }
        if(flags[2]) {
            nothing = false;
            toBeUpdated.setPriority(result.getPriorityLevel());
        }
        if(flags[4]) {
            nothing = false;
            toBeUpdated.setName(result.getName());
        }
        if(flags[5]) {
            nothing = false;
            toBeUpdated.setDescription(result.getDescription());
        }
        if(flags[6]) {
            nothing = false;
            toBeUpdated.setTag(result.getTag());
        }
        
        //If nothing was changed, command was invalid
        if(nothing) {
            return Constants.CommandType.INVALID;
        }
        
        //Refreshes lists for display
        refreshLists();
        //Records the last updated task
        lastModifiedTask = toBeUpdated.getID();
        
        return Constants.CommandType.UPDATE;
    }
    
    /**
     * Handles the delete command. Finds the task in all tasks and deletes.
     * Command is invalid if task is not found.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType deleteTask(ParseResult result) {
        //Saves the previous state
        pushState();
        
        //Finds the task of the right ID
        long ID = result.getId();
        Task toBeDeleted = allTasks.getTaskByID(ID);
        
        if(toBeDeleted == null) {
            //If such a task is not found, command was invalid
            return Constants.CommandType.INVALID;
        } else {
            allTasks.remove(allTasks.getTaskByID(ID));
        }
        
        //Refreshes the display lists
        refreshLists();
        
        return Constants.CommandType.DELETE;
    }
    
    /**
     * Handles done commands. Finds the task in all tasks and marks it as done.
     * Command is invalid if task is not found.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType setDoneTask(ParseResult result) {
        //Saves the previous state
        pushState();
        
        //Finds the task of the right ID
        long ID = result.getId();
        Task toBeDone = allTasks.getTaskByID(ID);
        
        if(toBeDone == null) {
            //If Task is not found then command is invalid
            return Constants.CommandType.INVALID;
        } else {
            toBeDone.setDone();
            lastModifiedTask = toBeDone.getID();
            return Constants.CommandType.DONE;
        }

    }
    
    /**
     * Handles set not done commands. Finds the task in all tasks and marks it
     * as not done. Command is invalid if task is not found.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType setUndoneTask(ParseResult result) {
        
        //Saves previous state
        pushState();
        
        //Finds the task of the right ID
        long ID = result.getId();
        Task toBeDone = allTasks.getTaskByID(ID);
        
        if(toBeDone == null) {
            //If Task is not found then command is invalid
            return Constants.CommandType.INVALID;
        } else {
            toBeDone.setUndone();
            lastModifiedTask = toBeDone.getID();
            return Constants.CommandType.SETNOTDONE;
        }
    }
    
    /**
     * Handles search commands. Search results starts with all the tasks being
     * tracked. The presence of each flag from the parse result will indicate
     * the presences of the criteria and the search space will be pruned 
     * accordingly.
     * 
     * @param result
     * @return
     */
    private static Constants.CommandType searchTask(ParseResult result) {
        
        boolean[] flags = result.getCommandFlags();
        searchResults = new TaskList(allTasks);
        
        if(flags[0] && flags[7]) {
            searchResults = Logic.searchPeriod(searchResults, result.getDate(), result.getSecondDate());
        }
        if(flags[2]) {
            searchResults = Logic.searchPriority(searchResults, result.getPriorityLevel());
        }
        if(flags[4]) {
            searchResults = Logic.searchAll(searchResults, result.getName());
        }
        if(flags[5]) {
            searchResults = Logic.searchAll(searchResults, result.getDescription());
        }
        if(flags[6]) {
            searchResults = Logic.searchTaskByTags(searchResults, result.getTag());
        }
        
        return Constants.CommandType.SEARCH;
    }
    
    /**
     * Handles undo commands. If there was no previous state that was saved the
     * command is invalid. Otherwise, it will pop the previous state from the
     * stack of stored states and use it as the current one. Each operation
     * that mutates the state of tasks should be preceeded by copying the 
     * TaskList before the change and pushed onto previousStates.
     * 
     * @return
     */
    private static Constants.CommandType undo() {
        
        if(previousStates.isEmpty()) {
            //If there were no previous saved states, command is invalid
            return Constants.CommandType.INVALID;
        } else {
            allTasks = previousStates.pop();
            refreshLists();
            return Constants.CommandType.UNDO;
        }
        
    }
    
    /**
     * Gets the storage path stored in the current configuration.
     * 
     * @return
     */
    public static String getStoragePath() {
        return config.getStoragePath();
    }
    
    /**
     * Handles savewhere commands. Returns the path of the current storage 
     * file.
     * 
     * @return
     */
    private static Constants.CommandType fetchStoragePath() {
        //System.out.println(config.getStoragePath());
        return Constants.CommandType.SAVEWHERE;
    }
    
    /**
     * The function supplied to UI to call upon receiving user input. Takes
     * in a string to be processed. Parses and acts on the command accordingly.
     * 
     * @param userInput
     * @return
     */
    //Not tested yet
    public static Constants.CommandType process(String userInput) {
        
        if(userInput == null){
            
            return Constants.CommandType.INVALID;
            
        }
        
        
        ParseResult result = Parser.parse(userInput);
        Constants.CommandType command = result.getCommandType();
        
        switch (command) {
            case ADD:
                return addTask(result);
                
            case UPDATE:
                return updateTask(result);
                
            case DELETE:
                return deleteTask(result);
                
            case SHOW:
                //TO BE DONE
                return Constants.CommandType.SHOW;
                
            case DONE:
                return setDoneTask(result);
                
            case UNDO:
                return undo();
                
            case SEARCH:
                return searchTask(result);
                
            case SETNOTDONE:
                return setUndoneTask(result);
                
            case SAVEWHERE:
                return fetchStoragePath();
                
            case HELP:
                return Constants.CommandType.HELP;
                
            case HELP_ADD:
                return Constants.CommandType.HELP_ADD;
                
            case HELP_UPDATE:
                return Constants.CommandType.HELP_UPDATE;
                
            case HELP_DELETE:
                return Constants.CommandType.HELP_DELETE;
                
            case HELP_SHOW:
                return Constants.CommandType.HELP_SHOW;
                
            case HELP_DONE:
                return Constants.CommandType.HELP_DONE;
                
            case HELP_UNDO: 
                return Constants.CommandType.HELP_UNDO;
                
            case HELP_SEARCH:
                return Constants.CommandType.HELP_SEARCH;
                
            default:
                return Constants.CommandType.INVALID;
        }
    }
    
    //Not tested yet
    public static TaskList getAllTasks() {
        return allTasks;
    }
    
    //Not tested yet
    public static TaskList getDoneTasks() {
        return doneTasks;
    }
    
    //Not tested yet
    public static TaskList getUndoneTasks() {
        return undoneTasks;
    }
    
    //Not tested yet
    public static TaskList getTentativeTasks() {
        return tentativeTasks;
    }
    
    //Not tested yet
    public static TaskList getConfirmedTasks() {
        return normalTasks;
    }
    
    //Not tested yet
    public static TaskList getSearchResult() {
        return searchResults;
    }
}
