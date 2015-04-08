//@author A0114156N

package planner;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.ArrayList;

import org.junit.Test;

public class StorageTest {

    @Test
    public void testSaveConfiguration() {
        try {

            Storage storage = new Storage();
            File configFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONFIG_FILE_NAME);
            configFile.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            Configuration config1 = new Configuration("path1");

            storage.saveConfiguration(config1);
            
            String fileContents = br.readLine();
            
            assertEquals("File should contain configuration", "{\"storagePath\":\""+config1.getStoragePath()+"\",\"numTasks\":\"1\"}", fileContents);
            
            br.close();
            configFile.delete();
            
            Configuration config2 = new Configuration("another.path");
            storage.saveConfiguration(config2);
            
            File configFile2 = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONFIG_FILE_NAME);
            BufferedReader br2 = new BufferedReader(new FileReader(configFile2));
            
            String fileContents2 = br2.readLine();
            
            assertEquals("File should contain configuration", "{\"storagePath\":\""+config2.getStoragePath()+"\",\"numTasks\":\"1\"}", fileContents2);
            
            br2.close();
            configFile.delete();
            
            
        } catch (Exception e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testWriteToFile() {
        try {
            Method method = Storage.class.getDeclaredMethod("writeToFile", String.class, ArrayList.class);
            method.setAccessible(true);
            
            String testPath = "testPath";
            String testContentLine1 = "testContent1";
            String testContentLine2 = "testContent2";
            String testContentLine3 = "testContent3";
            ArrayList<String> testContent = new ArrayList<String>();
            testContent.add(testContentLine1);
            testContent.add(testContentLine2);
            File tester = new File(testPath);
            tester.createNewFile();
            
            method.invoke(null, testPath, testContent);
            BufferedReader br = new BufferedReader(new FileReader(tester));
            for(int i=0; i<testContent.size();i++) {
                assertEquals("content in file should be same as content to be written", testContent.get(i), br.readLine());
            }
            br.close();
            
            testContent.add(testContentLine3);
            method.invoke(null, testPath, testContent);
            br = new BufferedReader(new FileReader(tester));
            for(int i=0; i<testContent.size();i++) {
                assertEquals("content in file should be same as content to be written", testContent.get(i), br.readLine());
            }
            br.close();
            
            
            
            tester.delete();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void testConvertTaskBetweenJsonString() {
        try {
            Storage storage = new Storage();
            Method convertToJson = Storage.class.getDeclaredMethod("convertTaskToJsonString", Task.class);
            convertToJson.setAccessible(true);
            Method convertFromJson = Storage.class.getDeclaredMethod("convertTaskFromJsonString", String.class);
            convertFromJson.setAccessible(true);
            
            String result;
            
            String name = "testTask";
            String description = "toTestConversionIntoJson";
            Date dueDate = new Date(System.currentTimeMillis());
            int priority = 3;
            String tag = "testing";
            int ID = 400;
            
            Task testTask = new Task(name, description, dueDate, priority, tag, ID);
            
            Date createdDate = testTask.getCreatedDate();
            boolean floating = testTask.isFloating();
            boolean done = testTask.isDone();
            
            result = (String) convertToJson.invoke(storage, testTask);
            Task postConversionTask = (Task) convertFromJson.invoke(storage, result);
            
            
            assertEquals("name after converting back and forth should be equal", testTask.getName(), postConversionTask.getName());
            assertEquals("description after converting back and forth should be equal", testTask.getDescription(), postConversionTask.getDescription());
            assertEquals("tag after converting back and forth should be equal", testTask.getTag(), postConversionTask.getTag());
            assertEquals("priority after converting back and forth should be equal", testTask.getPriority(), postConversionTask.getPriority());
            assertEquals("due date after converting back and forth should be equal", testTask.getDueDate(), postConversionTask.getDueDate());
            assertEquals("created date after converting back and forth should be equal", testTask.getCreatedDate(), postConversionTask.getCreatedDate());
            assertEquals("done status after converting back and forth should be equal", testTask.isDone(), postConversionTask.isDone());
            assertEquals("floating status after converting back and forth should be equal", testTask.isFloating(), postConversionTask.isFloating());
            assertEquals("ID after converting back and forth should be equal", testTask.getID(), postConversionTask.getID());
            
            
        } catch (Exception e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testSaveAndReadTaskList() {
        try{
            Storage storage = new Storage();
            Method save = Storage.class.getDeclaredMethod("saveTaskList", String.class, TaskList.class);
            save.setAccessible(true);
            Method read = Storage.class.getDeclaredMethod("readTaskStorage", String.class);
            read.setAccessible(true);
            
            String fileName = "testFile.txt";
            TaskList testList = new TaskList();
            TaskList result;
            Task saveTask;
            Task readTask;
            Task task1 = new Task("task1", "description1", new Date(System.currentTimeMillis()), 3, "first tag", 1);
            Task task2 = new Task("task2", "description2", new Date(System.currentTimeMillis()), 2, "second tag", 2);
            testList.add(task1);
            testList.add(task2);
            
            save.invoke(storage, fileName, testList);
            result = (TaskList) read.invoke(storage, fileName);
            
            for(int i=0; i<testList.size();i++) {
                saveTask = testList.get(i);
                readTask = result.get(i);
                assertEquals("name after converting back and forth should be equal", saveTask.getName(), readTask.getName());
                assertEquals("description after converting back and forth should be equal", saveTask.getDescription(), readTask.getDescription());
                assertEquals("tag after converting back and forth should be equal", saveTask.getTag(), readTask.getTag());
                assertEquals("priority after converting back and forth should be equal", saveTask.getPriority(), readTask.getPriority());
                assertEquals("due date after converting back and forth should be equal", saveTask.getDueDate(), readTask.getDueDate());
                assertEquals("created date after converting back and forth should be equal", saveTask.getCreatedDate(), readTask.getCreatedDate());
                assertEquals("done status after converting back and forth should be equal", saveTask.isDone(), readTask.isDone());
                assertEquals("floating status after converting back and forth should be equal", saveTask.isFloating(), readTask.isFloating());
                assertEquals("ID after converting back and forth should be equal", saveTask.getID(), readTask.getID());
            }
            
            Task task3 = new Task("task3", "description3", new Date(System.currentTimeMillis()), 1, "third tag", 3);
            Task task4 = new Task("task4", "description4", null, 8, "fourth tag", 4);
            
            save.invoke(storage, fileName, testList);
            result = (TaskList) read.invoke(storage, fileName);
            
            for(int i=0; i<testList.size();i++) {
                saveTask = testList.get(i);
                readTask = result.get(i);
                assertEquals("name after converting back and forth should be equal", saveTask.getName(), readTask.getName());
                assertEquals("description after converting back and forth should be equal", saveTask.getDescription(), readTask.getDescription());
                assertEquals("tag after converting back and forth should be equal", saveTask.getTag(), readTask.getTag());
                assertEquals("priority after converting back and forth should be equal", saveTask.getPriority(), readTask.getPriority());
                assertEquals("due date after converting back and forth should be equal", saveTask.getDueDate(), readTask.getDueDate());
                assertEquals("created date after converting back and forth should be equal", saveTask.getCreatedDate(), readTask.getCreatedDate());
                assertEquals("done status after converting back and forth should be equal", saveTask.isDone(), readTask.isDone());
                assertEquals("floating status after converting back and forth should be equal", saveTask.isFloating(), readTask.isFloating());
                assertEquals("ID after converting back and forth should be equal", saveTask.getID(), readTask.getID());
            }
            
            File configFile = new File(fileName);
            configFile.delete();
        } catch(Exception e) {
            fail(e.toString());
        }
    }
}
