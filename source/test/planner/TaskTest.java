package planner;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

public class TaskTest {

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
    
    @Test
    public void testGetDescription() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        assertEquals("Task description should be "+taskDescription, taskDescription, entry.getDescription());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        assertEquals("Task description should be "+taskDescription2, taskDescription2, another.getDescription());
    }
    
    @Test
    public void testGetTag() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        assertEquals("Task tag should be "+taskTag, taskTag, entry.getTag());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        assertEquals("Task tag should be "+taskTag2, taskTag2, another.getTag());
    }
    
    @Test
    public void testGetPriority() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        assertEquals("Task priority should be "+taskPriority, taskPriority, entry.getPriority());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        assertEquals("Task priority should be "+taskPriority2, taskPriority2, another.getPriority());
    }
    
    @Test
    public void testGetDueDate() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        assertEquals("Task due date should be "+taskDueDate, taskDueDate, entry.getDueDate());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        assertEquals("Task due date should be "+taskDueDate2, taskDueDate2, another.getDueDate());
    }
    
    @Test
    public void testSetName() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Timestamp taskDueDate = new Timestamp(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag);
        
        String newName = "newTask";
        entry.setName(newName);
        assertEquals("New task name should be "+newName, newName, entry.getName());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Timestamp taskDueDate2 = new Timestamp(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2);
        
        String newName2 = "modifiedTask";
        another.setName(newName2);
        assertEquals("New task name should be "+newName2, newName2, another.getName());
    }
}
