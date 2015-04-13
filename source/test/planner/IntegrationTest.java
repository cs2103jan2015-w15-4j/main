//@author A0114156N

package planner;

import static org.junit.Assert.assertEquals;

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

        Engine.exit();

        TaskList afterSaveList = store.readTaskStorage(storagePath);
        Configuration afterSaveConfig = store.readConfig();
        Task st = afterSaveList.get(afterSaveList.size() - 1);

        // After save
        assertEquals("Newly added task should have the right name",
                "junittest", st.getName());
        assertEquals("Task number should increase by 1 after add",
                (long) beforeSaveConfig.getCurTaskNum(),
                afterSaveConfig.getCurTaskNum() - 1);

    }

    @Test
    public void testUndoTask() {

        String addTaskInput = "add junittest";

        Engine.init();
        Storage store = new Storage();
        String storagePath = Engine.getStoragePath();
        TaskList beforeAddList = store.readTaskStorage(storagePath);
        int sizeBeforeAdd = beforeAddList.size();
        Engine.process(addTaskInput);

        // Undo
        Engine.process("undo");

        Engine.exit();

        TaskList afterSaveList = store.readTaskStorage(storagePath);
        int sizeAfterSave = afterSaveList.size();

        // After save
        assertEquals("TaskList size should be the same after save",
                sizeBeforeAdd, sizeAfterSave);

    }

    @Test
    public void testUpdateTaskPriority() {

        String addTaskInput = "add junittest";

        Engine.init();
        Storage store = new Storage();
        Engine.process(addTaskInput);

        String storagePath = Engine.getStoragePath();

        Engine.exit();

        TaskList afterAddList = store.readTaskStorage(storagePath);
        Task taskAdded = afterAddList.get(afterAddList.size() - 1);
        int taskID = taskAdded.getID();
        System.out.println("Taskid " + taskID);

        Engine.process("update " + taskID + " priority 5");

        Engine.exit();

        Engine.init();
        TaskList afterSaveList = store.readTaskStorage(storagePath);
        Task st = afterSaveList.getTaskByID(taskID);
        System.out.println(st.getDueDate() == null);

        // After save
        assertEquals("Saved task should have the right name", "junittest",
                st.getName());
        assertEquals("Saved task should have the right priority", 5,
                st.getPriority());

    }
}
