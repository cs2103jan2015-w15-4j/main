package planner;

public class Engine {
    private static Configuration config;
    private static TaskList allTasks;
    private static TaskList doneTasks;
    private static TaskList undoneTasks;
    private static TaskList tentativeTasks;
    private static TaskList normalTasks;
    
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
