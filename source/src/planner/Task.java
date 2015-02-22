package planner;

import java.sql.Timestamp;

/**
 * This class is used to represent the entries for storing tasks.
 * @author kohwaikit
 *
 */
public class Task {
    private String taskName, taskDescription, taskTag;
    private Timestamp dateCreated, dateDue;
    private int taskPriority;
    
    public Task(String name, String description, Timestamp dueDate, int priority, String tag) throws IllegalArgumentException{
        if(name.equals("")) {
            throw new IllegalArgumentException("Task name cannot be empty!");
        }
        
        taskName = name;
        taskDescription = description;
        taskTag = tag;
        
        dateCreated = new Timestamp(System.currentTimeMillis());
        dateDue = dueDate;
        
        taskPriority = priority;
        
    }
    
    public String getName() {
        return taskName;
    }
}
