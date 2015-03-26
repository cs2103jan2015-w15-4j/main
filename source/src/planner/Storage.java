package planner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.security.CodeSource;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class handles the external storage of the program. The storage format
 * of the program is in JSON using the json.simple library. This class handles
 * the saving and loading of TaskList objects and Configuration objects.
 * 
 * @author kohwaikit
 *
 */
public class Storage {
    
    //Not tested yet
    public Configuration readConfig() {
        Configuration result = new Configuration("data");
        try {
            CodeSource cs = getClass().getProtectionDomain().getCodeSource();
            String sourcePath = cs.getLocation().getPath();
            String filePath = sourcePath + Constants.CONFIG_FILE_LOCATION;
            
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            JSONParser parser = new JSONParser();
            JSONObject taskJson = (JSONObject) parser.parse(br.readLine());
            String path = (String) taskJson.get("storagePath");
            long curTaskNum = Long.valueOf((String) taskJson.get("numTasks"));
            
            result = new Configuration(path, curTaskNum);
            return result;
        } catch (Exception e) {
            return result;
        }
    }
    
    
    /**
     * Converts a Configuration object into a JSONObject, then into a string
     * and put into an ArrayList then passed to writeToFile to write into the
     * config file.
     * 
     * @param newConfig The Configuration object to be saved.
     * @throws IOException If there is a problem writing to file.
     */
    public void saveConfiguration(Configuration newConfig) throws IOException {
        
        JSONObject configObject = new JSONObject();
        configObject.put("storagePath", newConfig.getStoragePath());
        configObject.put("numTasks", 
                String.valueOf(newConfig.getCurTaskNum()));
        
        ArrayList<String> config = new ArrayList<String>();
        config.add(configObject.toJSONString());
        
        CodeSource cs = getClass().getProtectionDomain().getCodeSource();
        writeToFile(cs.getLocation().getPath() + 
                Constants.CONFIG_FILE_LOCATION, config);
        
    }
    
    /**
     * Takes in the location to write, and an ArrayList of Strings as content
     * to be written. Creates the file if it does not exist.Writes each string
     * as a line in the new file.
     * 
     * @param fileName The location of the file to write to.
     * @param content An ArrayList of Strings to be written to file.
     * @throws IOException If there is a problem writing to file.
     */
    private static void writeToFile(String fileName, ArrayList<String> content)
            throws IOException {
        
        File writeTarget = new File(fileName);
        
        if(!writeTarget.exists()) {
            writeTarget.createNewFile();
        }
        
        FileWriter fw = new FileWriter(writeTarget);
        
        for(String s:content) {
            fw.write(s);
            fw.write(System.lineSeparator());
        }
        
        fw.flush();
        fw.close();
    }
    
    //Not tested yet
    private ArrayList<String> convertTaskListToJsonStringList(TaskList input) {
        ArrayList<String> results = new ArrayList<String>();
        for(Task t:input) {
            results.add(convertTaskToJsonString(t));
        }
        return results;
    }
    
    //Not tested yet
    public void saveTaskList(String fileName, TaskList tasks) {
        ArrayList<String> taskJsonStrings = convertTaskListToJsonStringList(tasks);
        try {
            writeToFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + fileName, taskJsonStrings);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //Not tested yet
    public TaskList readTaskStorage(String fileName) {
        TaskList tasks = new TaskList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + fileName));
            String input;
            while((input = br.readLine()) != null) {
                tasks.add(convertTaskFromJsonString(input));
            }
            
            br.close();
            return tasks;
        } catch (Exception e){
            return tasks;
        }
    }
    
    private String convertTaskToJsonString(Task task) {
        JSONObject taskObject = new JSONObject();
        taskObject.put("name", task.getName());
        taskObject.put("description", task.getDescription());
        taskObject.put("tag", task.getTag());
        taskObject.put("priority", String.valueOf(task.getPriority()));
        taskObject.put("due", task.getDueDate().getTime());
        taskObject.put("created", task.getCreatedDate().getTime());
        taskObject.put("done", task.isDone());
        taskObject.put("id", String.valueOf(task.getID()));
        return taskObject.toJSONString();
    }
    
    private Task convertTaskFromJsonString(String taskString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject taskJson = (JSONObject) parser.parse(taskString);
            String name = (String) taskJson.get("name");
            String description = (String) taskJson.get("description");
            String tag = (String) taskJson.get("tag");
            int priority = Integer.valueOf((String)taskJson.get("priority"));
            Date dueDate = new Date((Long) taskJson.get("due"));
            long ID = Long.valueOf((String)taskJson.get("id"));
            
            Date createdDate = new Date((Long) taskJson.get("created"));
            boolean done = (boolean) taskJson.get("done");
            
            Task result = new Task(name, description, dueDate, priority, tag, ID);
            
            if(done) {
                result.setDone();
            } else {
                result.setUndone();
            }
            result.configureCreatedDate(createdDate);
            
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
