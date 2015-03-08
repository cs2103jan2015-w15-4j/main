package planner;

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
            Logic.splitTaskByDone(allTasks, doneTasks, undoneTasks);
            Logic.splitTasksByTentative(undoneTasks, normalTasks, tentativeTasks);
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
    public static Constants.COMMAND_TYPE process(String userInput) {
        ParseResult result = Parser.parse(userInput);
        Constants.COMMAND_TYPE command = result.getCommandType();
        switch (command) {
            case ADD:
                //TO BE DONE
                break;
            case UPDATE:
                //TO BE DONE
                break;
            case DELETE:
                //TO BE DONE
                break;
            case SHOW:
                //TO BE DONE
                break;
            case DONE:
                //TO BE DONE
                break;
            case UNDO:
                //TO BE DONE
                break;
            case SEARCH:
                //TO BE DONE
                break;
            case HELP:
                //TO BE DONE
                break;
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
