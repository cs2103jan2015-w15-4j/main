package planner;

public class Engine {
    private static Configuration config;
    private static TaskList allTasks;
    
    //Not tested yet
    public static void init() {
        config = Storage.readConfig();
        allTasks = Storage.readTaskStorage(config.getStoragePath());
    }
    
    //Not tested yet
    public static TaskList getAllTasks() {
        return allTasks;
    }
    
}
