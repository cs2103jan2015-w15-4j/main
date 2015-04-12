//@author A0114156N

package planner;

import java.io.File;
import java.io.IOException;

/**
 * This class holds the configurations and settings of the program. It is meant
 * to keep track of where the program data is stored and the number of tasks
 * that have been created in the program.
 * 
 *
 */

public class Configuration {

    // Class variables
    private String storagePath;
    private int curTaskNum;
    private boolean isNew;

    /**
     * Constructor for storing an existing configuration after reading from the
     * config file.
     * 
     * @param path
     *            The path of the data storage file
     * @param taskNum
     *            The number of tasks tracked by the program
     */
    public Configuration(String path, int taskNum) {
        storagePath = path;
        curTaskNum = taskNum;
        isNew = false;
    }

    /**
     * Constructor for creating a new configuration, when old config file is not
     * found or cannot be read. Implies first run of the program.
     * 
     * @param path
     *            The path of the data storage file
     */
    public Configuration(String path) {
        storagePath = path;
        curTaskNum = 1;
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
    public int getCurTaskNum() {
        return curTaskNum;
    }

    /**
     * Returns a number to be assigned to a new Task and increases the internal
     * Task count.
     * 
     * @return Returns a number to be assigned to a new Task to be created.
     */
    public int getNewTaskNumber() {
        curTaskNum++;
        return curTaskNum - 1;
    }

    /**
     * Returns the validity of the path given. Also sets the config to the new
     * path if it was valid.
     * 
     * @param newPath
     * @return
     */
    public boolean setStoragePath(String newPath) {
        if (isPathValid(newPath)) {
            storagePath = newPath;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks the validity of the path to be used as a new storage.
     * 
     * @param path
     * @return
     */
    private boolean isPathValid(String path) {
        File tester = new File(path);
        if (tester.exists()) {
            return false;
        } else {
            try {
                System.out.println(path);
                tester.createNewFile();
                tester.delete();
                return true;
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }
        }
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
