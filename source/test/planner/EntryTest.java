package planner;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

public class EntryTest {

    @Test
    public void createTest() {
        String taskName = "entry1";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfTaskNameIsEmpty() {
        String emptyTask = "";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task empty = new Task(emptyTask, taskDescription, taskDueDate, taskPriority, taskTag);
    }
    
    @Test
    public void testGetName() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        assertEquals("Task name should be "+taskName, taskName, entry.getName());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        assertEquals("Task name should be "+taskName2, taskName2, another.getName());
    }
}
