package planner;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class IntegrationTest {

    @Test
    public void testAddTask() {
        String addTaskInput = "add junittest";
        
        Engine.init();
        Storage store = new Storage();
        Engine.process(addTaskInput);
        
        String storagePath = Engine.getStoragePath();
        
        Configuration beforeSaveConfig = store.readConfig();
        TaskList afterAddList = Engine.getAllTasks();
        Task at = afterAddList.get(afterAddList.size()-1);
        
        //After add
        assertEquals("Newly added task should have the right name", "junittest", at.getName());

        
        Engine.exit();
        
        TaskList afterSaveList = store.readTaskStorage(storagePath);
        Task st = afterAddList.get(afterAddList.size()-1);
        Configuration afterSaveConfig = store.readConfig();
        
        //After save
        assertEquals("Newly added task should have the right name", "junittest", st.getName());
        assertEquals("Task number should increase by 1 after add", 
                (int)beforeSaveConfig.getCurTaskNum(), afterSaveConfig.getCurTaskNum() -1);
        
    }

    @Test
    public void testUndoTask() {
        String addTaskInput = "add junittest";
        
        Engine.init();
        Storage store = new Storage();
        Engine.process(addTaskInput);
        
        String storagePath = Engine.getStoragePath();
        
        Configuration beforeSaveConfig = store.readConfig();
        TaskList afterAddList = Engine.getAllTasks();
        
        //After add
        int sizeBeforeUndo = afterAddList.size();
        
        //Undo
        Engine.process("undo");
        
        //After undo
        TaskList afterUndoList = Engine.getAllTasks();
        int sizeAfterUndo = afterUndoList.size();
        assertEquals("TaskList should be smaller by 1 after undoing add", 
                sizeBeforeUndo, sizeAfterUndo+1);

        
        Engine.exit();
        
        TaskList afterSaveList = store.readTaskStorage(storagePath);
        int sizeAfterSave = afterSaveList.size();
        
        //After save
        assertEquals("TaskList size should be the same after save", 
                sizeAfterUndo, sizeAfterSave);
        
    }
    
    @Test
    public void testUpdateTaskDate() {
        String addTaskInput = "add junittest";
        
        Engine.init();
        Storage store = new Storage();
        Engine.process(addTaskInput);
        
        String storagePath = Engine.getStoragePath();
        
        Configuration beforeSaveConfig = store.readConfig();
        TaskList afterAddList = Engine.getAllTasks();
        Task at = afterAddList.get(afterAddList.size()-1);
        int taskID = at.getID();
        
        //After add
        assertEquals("Newly added task should have the right name", "junittest", at.getName());
        
        Engine.process("update "+taskID+" date 12 may 2015");
        String dateString = "Tue May 12 00:00:00 SGT 2015";
        
        //After update
        assertEquals("Updated task should have the right name", "junittest", at.getName());
        assertEquals("Updated task should have the right date", dateString, at.getDueDate().toString());
        
        Engine.exit();
        
        TaskList afterSaveList = store.readTaskStorage(storagePath);
        Task st = afterAddList.get(afterAddList.size()-1);
        Configuration afterSaveConfig = store.readConfig();
        
        //After save
        assertEquals("Saved task should have the right name", "junittest", st.getName());
        assertEquals("Saved task should have the right date", dateString, st.getDueDate().toString());
                
    }
}
