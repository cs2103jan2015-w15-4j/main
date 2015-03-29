package planner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class handles the external storage of the program. The storage format
 * of the program is in JSON using the json.simple library. This class handles
 * the saving and loading of TaskList objects and Configuration objects.
 * 
 * @author kohwaikit
 *
 */
public class Storage {
    
    /**
     * This method reads the config file, of which the location is stored in 
     * Constants.CONFIG_FILE_LOCATION and data stored in JSON String, and 
     * converts the data from the JSON object into a Configuration object.
     * 
     * When the method fails to find the file or fails to read the config file,
     * a new Configuration is new. The program will run as if it is ran for the
     * first time.
     * 
     * @return Configuration object read from the file or a brand new object
     */
    //Not tested yet
    public Configuration readConfig() {
        String defaultPath = getSourcePath() + Constants.DEFAULT_STORAGE_NAME;
        Configuration result = new Configuration(defaultPath);
        BufferedReader br = null;
        try {
            String sourcePath = getSourcePath();
            String filePath = sourcePath + Constants.CONFIG_FILE_NAME;
            
            br = new BufferedReader(new FileReader(filePath));
            
            JSONParser parser = new JSONParser();
            JSONObject taskJson = (JSONObject) parser.parse(br.readLine());
            String path = (String) taskJson.get("storagePath");
            long curTaskNum = Long.valueOf((String) taskJson.get("numTasks"));
            
            result = new Configuration(path, curTaskNum);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IO Error!");
        } catch (ParseException e) {
            System.out.println("Parse error!");
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    
                }
            }
        }
        return result;
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
        
        String sourcePath = getSourcePath();
        writeToFile(sourcePath + 
                Constants.CONFIG_FILE_NAME, config);
        
    }
    
    /**
     * Takes in the location to write, and an ArrayList of Strings as content
     * to be written. Creates the file if it does not exist. Writes each string
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
    
    /**
     * Takes in a TaskList intended to be written to data storage, and converts
     * each task into a String using convertTaskToJsonString. The resultant 
     * ArrayList of Strings will be write ready for the data storage.
     * 
     * @param input the TaskList to be converted
     * @return Strings that are ready to be written to data storage
     */
    //Not tested yet
    private ArrayList<String> convertTaskListToJsonStringList(TaskList input) {
        ArrayList<String> results = new ArrayList<String>();
        for(Task t:input) {
            results.add(convertTaskToJsonString(t));
        }
        return results;
    }
    
    /**
     * Takes in the file name to be used for the storage file and the TaskList
     * to be stored. Saves the taskList in the storage file.
     * 
     * @param fileName The file name to store the list of tasks as
     * @param tasks The TaskList to be stored
     * @throws IOException if writing fails
     */
    //Not tested yet
    public void saveTaskList(String fileName, TaskList tasks) 
            throws IOException {
        ArrayList<String> taskJsonStrings = convertTaskListToJsonStringList(tasks);
        writeToFile(fileName, taskJsonStrings);
    }
    
    //Not tested yet
    public TaskList readTaskStorage(String fileName) {
        TaskList tasks = new TaskList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
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
        if(task.getDueDate() == null) {
            taskObject.put("due", null);
        } else {
            taskObject.put("due", task.getDueDate().getTime());
        }
        if(task.getEndDate() == null) {
            taskObject.put("end", null);
        } else {
            taskObject.put("end", task.getEndDate().getTime());
        }
        if(task.getDateCompleted() == null) {
            taskObject.put("complete", null);
        } else {
            taskObject.put("complete", task.getDateCompleted().getTime());
        }
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
            Object due = taskJson.get("due");
            Object end = taskJson.get("end");
            Object completed = taskJson.get("complete");
            Date dueDate = null;
            Date endDate = null;
            Date completedDate = null;
            if(due != null) {
                dueDate = new Date((Long) due);
            }
            
            if(end != null) {
                endDate = new Date((Long) end);
            }
            if(completed != null) {
                completedDate = new Date((Long) completed);
            }
            
            long ID = Long.valueOf((String)taskJson.get("id"));
            
            
            Date createdDate = new Date((Long) taskJson.get("created"));
            boolean done = (boolean) taskJson.get("done");
            
            Task result = new Task(name, description, dueDate, priority, tag, ID);
            
            if(done) {
                result.setDone();
            } else {
                result.setUndone();
            }
            
            result.setEndDate(endDate);
            result.setDateCompleted(completedDate);
            result.configureCreatedDate(createdDate);
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    private String getSourcePath() {
        CodeSource cs = getClass().getProtectionDomain().getCodeSource();
        return cs.getLocation().getPath();
    }
}
