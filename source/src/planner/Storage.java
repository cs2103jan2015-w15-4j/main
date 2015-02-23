package planner;
import org.json.simple.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {
    
    public static void saveConfiguration(Configuration newConfig) throws IOException {
        JSONObject configObject = new JSONObject();
        configObject.put("storagePath", newConfig.getStoragePath());
        writeToFile(Constants.CONFIG_FILE_LOCATION, configObject.toJSONString());
    }
    
    private static void writeToFile(String fileName, String content) throws IOException {
        File writeTarget = new File(fileName);
        if(!writeTarget.exists()) {
            writeTarget.createNewFile();
        }
        FileWriter fw = new FileWriter(writeTarget);
        fw.write(content);
        fw.flush();
        fw.close();
    }
}
