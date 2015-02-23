package planner;
import org.json.simple.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {
    
    public static void saveConfiguration(Configuration newConfig) throws IOException {
        File configs = new File("config");
        configs.createNewFile();
        JSONObject configObject = new JSONObject();
        configObject.put("storagePath", newConfig.getStoragePath());
        FileWriter fw = new FileWriter(configs);
        fw.write(configObject.toJSONString());
        fw.flush();
        fw.close();
    }
    
}
