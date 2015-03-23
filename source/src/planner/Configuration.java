package planner;

/**
 * This class holds the configurations and settings of the program.
 * It is meant to keep track of where the program data is stored and
 * the number of tasks that have been created in the program. 
 * 
 * @author kohwaikit
 *
 */

public class Configuration {
    
    //Class variables
    private String storagePath;
    private Long curTaskNum;
    private boolean isNew;
    
    /**
     * Constructor for storing an existing configuration after reading
     * from the config file.
     * 
     * @param path The path of the data storage file
     * @param taskNum The number of tasks tracked by the program
     */
    public Configuration(String path, Long taskNum) {
        storagePath = path;
        curTaskNum = taskNum;
        isNew = false;
    }
    
    /**
     * Constructor for creating a new configuration, when old config file
     * is not found or cannot be read. Implies first run of the program.
     * 
     * @param path The path of the data storage file
     */
    public Configuration(String path) {
        storagePath = path;
        curTaskNum = 1l;
        isNew = true;
    }
    
    /**
     * 
     * @return Returns the path of the data storage file.
     */
    public String getStoragePath() {
        return storagePath;
    }
    
    /**
     * 
     * @return Returns the number of tasks created by the program.
     */
    public Long getCurTaskNum() {
        return curTaskNum;
    }
    
    /**
     * Returns a number to be assigned to a new Task and increases the internal
     * Task count.
     * 
     * @return Returns a number to be assigned to a new Task to be created.
     */
    public Long getNewTaskNumber() {
        curTaskNum++;
        return curTaskNum - 1;
    }
    
    /**
     * Takes in a String that is a path to be used to store the storage file.
     * 
     * @param newPath The path to be used for the data storage file.
     */
    public void setStoragePath(String newPath) {
        storagePath = newPath;
    }
    
    /**
     * Returns true if config was newly created, false otherwise. Returning
     * false implies that a config file was read.
     * 
     * @return Returns true if the config was newly created.
     */
    public boolean isNew() {
        return isNew;
    }
}
