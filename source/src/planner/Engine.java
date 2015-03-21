package planner;

import java.io.File;

public class Engine {
    private static Configuration config;
    private static TaskList allTasks;
    private static TaskList doneTasks;
    private static TaskList undoneTasks;
    private static TaskList tentativeTasks;
    private static TaskList normalTasks;
    private static TaskList searchResults;
    private static long lastModifiedTask;
    
    public static boolean isFirstRun() {
        return config.isNew();
    }
    
    public static long lastModifiedTask() {
        return lastModifiedTask;
    }
    
    //Not tested yet
    public static boolean init() {
        try {
        	
            config = Storage.readConfig();
            allTasks = Storage.readTaskStorage(config.getStoragePath());
        	doneTasks = new TaskList();
        	undoneTasks = new TaskList();
        	tentativeTasks = new TaskList();
        	normalTasks = new TaskList();
        	
            refreshLists();
            System.out.println(allTasks.size());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    //Not tested yet
    public static boolean exit() {
        try {
            Storage.saveConfiguration(config);
            Storage.saveTaskList(config.getStoragePath(), allTasks);
            System.out.println(allTasks.size());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    //Not tested yet
    private static void refreshLists() {
    	
    	
        doneTasks.clear();
        undoneTasks.clear();
        normalTasks.clear();
        tentativeTasks.clear();
        
        //Logic.splitTaskByDone(allTasks, doneTasks, undoneTasks);
        //Logic.splitTasksByTentative(undoneTasks, normalTasks, tentativeTasks);
        
        doneTasks = Logic.searchDone(allTasks);
        undoneTasks = Logic.searchNotDone(allTasks);
        normalTasks = Logic.searchConfirmed(undoneTasks);
        tentativeTasks = Logic.searchTentative(undoneTasks);
        
    }
    
    private static Constants.COMMAND_TYPE addTask (ParseResult result) {
        Task newTask = new Task(result.getName(), result.getDescription(), result.getDate(), result.getPriorityLevel(), result.getTag(), config.newTaskNumber());
        allTasks.add(newTask);
        refreshLists();
        lastModifiedTask = newTask.getID();
        return Constants.COMMAND_TYPE.ADD;
    }
    
    private static Constants.COMMAND_TYPE updateTask (ParseResult result) {
        boolean[] flags = result.getCommandFlags();
        boolean nothing = true;
        long ID = result.getId();
        Task toBeUpdated = allTasks.getTaskByID(ID);
        if(toBeUpdated == null) {
            return Constants.COMMAND_TYPE.INVALID;
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
            return Constants.COMMAND_TYPE.INVALID;
        }
        refreshLists();
        lastModifiedTask = toBeUpdated.getID();
        return Constants.COMMAND_TYPE.UPDATE;
    }
    
    private static Constants.COMMAND_TYPE deleteTask(ParseResult result) {
        long ID = result.getId();
        allTasks.remove(allTasks.getTaskByID(ID));
        refreshLists();
        return Constants.COMMAND_TYPE.DELETE;
    }
    
    private static Constants.COMMAND_TYPE setDoneTask(ParseResult result) {
        if(!result.getCommandFlags()[3]) {
            return Constants.COMMAND_TYPE.INVALID;
        } else {
            long ID = result.getId();
            Task toBeDone = allTasks.getTaskByID(ID);
            if(toBeDone == null) {
                return Constants.COMMAND_TYPE.INVALID;
            } else {
                toBeDone.setDone();
                lastModifiedTask = toBeDone.getID();
                return Constants.COMMAND_TYPE.DONE;
            }
            
        }
    }
    
    private static Constants.COMMAND_TYPE searchTask(ParseResult result) {
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
            searchResults = Logic.searchAll(searchResults, result.getName());
        }
        if(flags[6]) {
            searchResults = Logic.searchTaskByTags(searchResults, result.getTag());
        }
        return Constants.COMMAND_TYPE.SEARCH;
    }
    
    //Not tested yet
    public static Constants.COMMAND_TYPE process(String userInput) {
    	
    	if(userInput == null){
    		
    		return Constants.COMMAND_TYPE.INVALID;
    	}
    	
        ParseResult result = Parser.parse(userInput);
        Constants.COMMAND_TYPE command = result.getCommandType();
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
                return Constants.COMMAND_TYPE.SHOW;
            case DONE:
                return setDoneTask(result);
            case UNDO:
                //TO BE DONE
                return Constants.COMMAND_TYPE.UNDO;
            case SEARCH:
                return searchTask(result);
            case HELP:
                //TO BE DONE
                return Constants.COMMAND_TYPE.HELP;
            default:
                return Constants.COMMAND_TYPE.INVALID;
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
