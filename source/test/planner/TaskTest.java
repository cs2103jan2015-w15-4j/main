package planner;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
/**
 * 
 * @author kohwaikit
 *
 */
public class TaskTest {

    @Test
    public void createTest() {
        String taskName = "entry1";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 1;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, ID);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfTaskNameIsEmpty() {
        String emptyTask = "";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 3;
        
        Task empty = new Task(emptyTask, taskDescription, taskDueDate, taskPriority, taskTag, ID);
    }
    
    @Test
    public void testGetName() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        long ID = 4;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, 4);
        assertEquals("Task name should be "+taskName, taskName, entry.getName());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        long ID2 = 400;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, 400);
        assertEquals("Task name should be "+taskName2, taskName2, another.getName());
    }
    
    @Test
    public void testGetDescription() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 4;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        assertEquals("Task description should be "+taskDescription, taskDescription, entry.getDescription());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        int ID2 = 5;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, ID2);
        assertEquals("Task description should be "+taskDescription2, taskDescription2, another.getDescription());
    }
    
    @Test
    public void testGetTag() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 4;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, 4);
        assertEquals("Task tag should be "+taskTag, taskTag, entry.getTag());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        int ID2 = 41;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, ID2);
        assertEquals("Task tag should be "+taskTag2, taskTag2, another.getTag());
    }
    
    @Test
    public void testGetPriority() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 8;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        assertEquals("Task priority should be "+taskPriority, taskPriority, entry.getPriority());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        int ID2 = 4103;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, ID2);
        assertEquals("Task priority should be "+taskPriority2, taskPriority2, another.getPriority());
    }
    
    @Test
    public void testGetDueDate() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 4;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        assertEquals("Task due date should be "+taskDueDate, taskDueDate.toString(), entry.getDueDate().toString());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        int ID2 = 42;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, ID2);
        assertEquals("Task due date should be "+taskDueDate2, taskDueDate2, another.getDueDate());
    }
    
    /*This is a typical case for setting name*/
    @Test
    public void testSetName() {
        String taskName = "getTask";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 4;
        
        Task entry = new Task(taskName, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        
        String newName = "newTask";
        entry.setName(newName);
        assertEquals("New task name should be "+newName, newName, entry.getName());
        
        String taskName2 = "anotherTask";
        String taskDescription2 = "other";
        Date taskDueDate2 = new Date(System.currentTimeMillis());
        String taskTag2 = "others";
        int taskPriority2 = 3;
        int ID2 = 24;
        
        Task another = new Task(taskName2, taskDescription2, taskDueDate2, taskPriority2, taskTag2, ID2);
        
        String newName2 = "modifiedTask";
        another.setName(newName2);
        assertEquals("New task name should be "+newName2, newName2, another.getName());
    }
    
    /*This is a boundary case where name is null*/
    @Test(expected=IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfNameIsSetNull() {
        String emptyTask = "name";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 3;
        
        Task empty = new Task(emptyTask, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        
        empty.setName(null);
    }
    
    /*This is a boundary case where name is an empty string*/
    @Test(expected=IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfNameIsSetEmpty() {
        String emptyTask = "name";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 3;
        
        Task empty = new Task(emptyTask, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        
        empty.setName("");
    }
    
    /*This is a boundary case where name is a string with only 1 character*/
    @Test
    public void testSetNameToShortString() {
        String emptyTask = "name";
        String taskDescription = "testing";
        Date taskDueDate = new Date(System.currentTimeMillis());
        String taskTag = "nothing";
        int taskPriority = 5;
        int ID = 3;
        
        Task empty = new Task(emptyTask, taskDescription, taskDueDate, taskPriority, taskTag, ID);
        
        String shortName = "a";
        empty.setName(shortName);
        assertEquals("New task name should be "+shortName, shortName, empty.getName());
    }
}
