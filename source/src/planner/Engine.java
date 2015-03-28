package planner;

import java.io.File;
import java.util.Stack;

/**
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
    
    public static boolean isFirstRun() {
        return config.isNew();
    }
    
    public static long lastModifiedTask() {
        return lastModifiedTask;
    }
    
    //Not tested yet
    public static boolean init() {
        try {
            storage = new Storage();
            config = storage.readConfig();
            allTasks = storage.readTaskStorage(config.getStoragePath());
            doneTasks = new TaskList();
            undoneTasks = new TaskList();
            tentativeTasks = new TaskList();
            normalTasks = new TaskList();
            previousStates = new Stack<TaskList>();
            
            refreshLists();
            System.out.println(allTasks.size());
            return true;
        } catch(Exception e) {
            System.out.println("read error");
            return false;
        }
    }
    
    //Not tested yet
    public static boolean exit() {
        try {
            storage.saveConfiguration(config);
            storage.saveTaskList(config.getStoragePath(), allTasks);
            System.out.println(allTasks.size());
            return true;
        } catch(Exception e) {
            System.out.println("write error");
            return false;
        }
    }
    
    //Not tested yet
    private static void refreshLists() {
        
        //Logic.sortTaskListByPriority(allTasks);
        //Logic.sortTaskListByDate(allTasks);
        
        doneTasks.clear();
        undoneTasks.clear();
        normalTasks.clear();
        tentativeTasks.clear();
        
        doneTasks = Logic.searchDone(allTasks);
        undoneTasks = Logic.searchNotDone(allTasks);
        normalTasks = Logic.searchConfirmed(undoneTasks);
        tentativeTasks = Logic.searchTentative(undoneTasks);
        
    }
    
    private static void pushState() {
        previousStates.push(new TaskList(allTasks));
    }
    
    private static Constants.CommandType addTask (ParseResult result) {
        pushState();
        
        boolean[] flags = result.getCommandFlags();
        Task newTask;
        if(!flags[0] && flags[7]) {
            newTask = new Task(result.getName(), result.getDescription(), result.getSecondDate(), result.getPriorityLevel(), result.getTag(), config.getNewTaskNumber());
        } else {
            newTask = new Task(result.getName(), result.getDescription(), result.getDate(), result.getPriorityLevel(), result.getTag(), config.getNewTaskNumber());
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
                //TO BE DONE
                return Constants.CommandType.HELP;
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
