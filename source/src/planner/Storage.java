package planner;
import org.json.simple.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
}
