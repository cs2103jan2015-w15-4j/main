package planner;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Storage {
    
    //Not tested yet
    public Configuration readConfig() {
        Configuration result = new Configuration("data");
        try {
            BufferedReader br = new BufferedReader(new FileReader(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONFIG_FILE_LOCATION));
            
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
    
    
    //Need to update tests
    public void saveConfiguration(Configuration newConfig) throws IOException {
        JSONObject configObject = new JSONObject();
        configObject.put("storagePath", newConfig.getStoragePath());
        configObject.put("numTasks", String.valueOf(newConfig.getCurTaskNum()));
        ArrayList<String> config = new ArrayList<String>();
        config.add(configObject.toJSONString());
        writeToFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONFIG_FILE_LOCATION, config);
        System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + Constants.CONFIG_FILE_LOCATION);
        System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    }
    
    private static void writeToFile(String fileName, ArrayList<String> content) throws IOException {
        File writeTarget = new File(fileName);
        if(!writeTarget.exists()) {
            writeTarget.createNewFile();
        }
        FileWriter fw = new FileWriter(writeTarget);
        for(String s:content) {
            fw.write(s+System.lineSeparator());
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
            System.out.println(e);
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
