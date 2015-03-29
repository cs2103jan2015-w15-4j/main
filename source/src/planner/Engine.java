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
     * This method rebuilds the TaskLists with the latest allTasks such that
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
     * This method copies the current allTasks and pushes the copy into the
     * previousStates, such that older states of the program is stored and 
     * can be popped to be used for undo.
     */
    private static void pushState() {
        previousStates.push(new TaskList(allTasks));
    }
    
    
    private static Constants.CommandType addTask (ParseResult result) {
        
        pushState();
        
        boolean[] flags = result.getCommandFlags();
        Task newTask;
        if(!flags[0] && flags[7]) {
            newTask = new Task(result.getName(), result.getDescription(), 
                    result.getSecondDate(), result.getPriorityLevel(), 
                    result.getTag(), config.getNewTaskNumber());
        } else {
            newTask = new Task(result.getName(), result.getDescription(), 
                    result.getDate(), result.getPriorityLevel(), 
                    result.getTag(), config.getNewTaskNumber());
            if(flags[7]) {
                newTask.setEndDate(result.getSecondDate());
            }
        }
        
        allTasks.add(newTask);
        refreshLists();
        lastModifiedTask = newTask.getID();
        return Constants.CommandType.ADD;
    }
    
    private static Constants.CommandType updateTask (ParseResult result) {
        pushState();
        
        boolean[] flags = result.getCommandFlags();
        boolean nothing = true;
        long ID = result.getId();
        Task toBeUpdated = allTasks.getTaskByID(ID);
        if(toBeUpdated == null) {
            return Constants.CommandType.INVALID;
        }
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
        if(nothing) {
            return Constants.CommandType.INVALID;
        }
        refreshLists();
        lastModifiedTask = toBeUpdated.getID();
        return Constants.CommandType.UPDATE;
    }
    
    private static Constants.CommandType deleteTask(ParseResult result) {
        pushState();
        
        long ID = result.getId();
        allTasks.remove(allTasks.getTaskByID(ID));
        refreshLists();
        return Constants.CommandType.DELETE;
    }
    
    private static Constants.CommandType setDoneTask(ParseResult result) {
        pushState();
        
        if(!result.getCommandFlags()[3]) {
            return Constants.CommandType.INVALID;
        } else {
            long ID = result.getId();
            Task toBeDone = allTasks.getTaskByID(ID);
            if(toBeDone == null) {
                return Constants.CommandType.INVALID;
            } else {
                toBeDone.setDone();
                lastModifiedTask = toBeDone.getID();
                return Constants.CommandType.DONE;
            }
            
        }
    }
    
    private static Constants.CommandType setUndoneTask(ParseResult result) {
        pushState();
        
        if(!result.getCommandFlags()[3]) {
            return Constants.CommandType.INVALID;
        } else {
            long ID = result.getId();
            Task toBeDone = allTasks.getTaskByID(ID);
            if(toBeDone == null) {
                return Constants.CommandType.INVALID;
            } else {
                toBeDone.setUndone();
                lastModifiedTask = toBeDone.getID();
                return Constants.CommandType.DONE;
            }
            
        }
    }
    
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
    
    private static Constants.CommandType undo() {
        if(previousStates.isEmpty()) {
            return Constants.CommandType.INVALID;
        } else {
            allTasks = previousStates.pop();
            refreshLists();
            return Constants.CommandType.UNDO;
        }
    }
    
    public static String getStoragePath() {
        return config.getStoragePath();
    }
    
    private static Constants.CommandType fetchStoragePath() {
        //System.out.println(config.getStoragePath());
        return Constants.CommandType.SAVEWHERE;
    }
    
    //Not tested yet
    public static Constants.CommandType process(String userInput) {
        
        if(userInput == null){
            
            return Constants.CommandType.INVALID;
        }
        
        ParseResult result = Parser.parse(userInput);
        Constants.CommandType command = result.getCommandType();
        long ID;
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
