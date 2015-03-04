package planner;

import java.util.ArrayList;

/**
 * TaskList class is a container class for Tasks. It extends ArrayList.
 * @author kohwaikit
 *
 */
public class TaskList{
    private ArrayList<Task> tasks;
    public TaskList(ArrayList<Task> input) {
        tasks = input;
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
