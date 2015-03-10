package planner;

import java.io.File;

public class Engine {
    private static Configuration config;
    private static TaskList allTasks;
    private static TaskList doneTasks;
    private static TaskList undoneTasks;
    private static TaskList tentativeTasks;
    private static TaskList normalTasks;
    
    //Not tested yet
    public static boolean init() {
        try {
        	
            config = Storage.readConfig();
            allTasks = Storage.readTaskStorage(config.getStoragePath());
        	allTasks = new TaskList();
        	doneTasks = new TaskList();
        	undoneTasks = new TaskList();
        	tentativeTasks = new TaskList();
        	normalTasks = new TaskList();
        	
            refreshLists();
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
        
        
        
        Logic.splitTaskByDone(allTasks, doneTasks, undoneTasks);
        Logic.splitTasksByTentative(undoneTasks, normalTasks, tentativeTasks);
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
                Task newTask = new Task(result.getName(), result.getDescription(), result.getDate(), result.getPriorityLevel(), result.getTag(), config.newTaskNumber());
                allTasks.add(newTask);
                refreshLists();
                return Constants.COMMAND_TYPE.ADD;
            case UPDATE:
                boolean[] flags = result.getCommandFlags();
                ID = result.getId();
                Task toBeUpdated = allTasks.getTaskByID(ID);
                if(flags[0]) {
                	toBeUpdated.setDueDate(result.getDate());
                }
                if(flags[2]) {
                    toBeUpdated.setPriority(result.getPriorityLevel());
                }
                if(flags[4]) {
                    toBeUpdated.setName(result.getName());
                }
                if(flags[5]) {
                    toBeUpdated.setDescription(result.getDescription());
                }
                if(flags[6]) {
                    toBeUpdated.setTag(result.getTag());
                }
                refreshLists();
                return Constants.COMMAND_TYPE.UPDATE;
            case DELETE:
                ID = result.getId();
                allTasks.remove(allTasks.getTaskByID(ID));
                refreshLists();
                return Constants.COMMAND_TYPE.DELETE;
            case SHOW:
                //TO BE DONE
                return Constants.COMMAND_TYPE.SHOW;
            case DONE:
                //TO BE DONE
                return Constants.COMMAND_TYPE.DONE;
            case UNDO:
                //TO BE DONE
                return Constants.COMMAND_TYPE.UNDO;
            case SEARCH:
                //TO BE DONE
                return Constants.COMMAND_TYPE.SEARCH;
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
    
    //
}
