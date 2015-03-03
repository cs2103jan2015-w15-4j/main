package planner;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Storage {
    
    public static void saveConfiguration(Configuration newConfig) throws IOException {
        JSONObject configObject = new JSONObject();
        configObject.put("storagePath", newConfig.getStoragePath());
        ArrayList<String> config = new ArrayList<String>();
        config.add(configObject.toJSONString());
        writeToFile(Constants.CONFIG_FILE_LOCATION, config);
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
    
    private static String convertTaskToJsonString(Task task) {
        JSONObject taskObject = new JSONObject();
        taskObject.put("name", task.getName());
        taskObject.put("description", task.getDescription());
        taskObject.put("tag", task.getTag());
        taskObject.put("priority", String.valueOf(task.getPriority()));
        taskObject.put("due", task.getDueDate().getTime());
        taskObject.put("created", task.getCreatedDate().getTime());
        taskObject.put("done", task.isDone());
        return taskObject.toJSONString();
    }
    
    private static Task convertTaskFromJsonString(String taskString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject taskJson = (JSONObject) parser.parse(taskString);
            String name = (String) taskJson.get("name");
            String description = (String) taskJson.get("description");
            String tag = (String) taskJson.get("tag");
            int priority = Integer.valueOf((String)taskJson.get("priority"));
            Timestamp dueDate = new Timestamp((Long) taskJson.get("due"));
            
            Timestamp createdDate = new Timestamp((Long) taskJson.get("created"));
            boolean done = (boolean) taskJson.get("done");
            
            Task result = new Task(name, description, dueDate, priority, tag);
            
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
