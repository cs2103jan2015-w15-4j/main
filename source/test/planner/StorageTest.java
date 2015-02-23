package planner;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

public class StorageTest {

    @Test
    public void testSaveConfiguration() {
        try {
            File configFile = new File(Constants.CONFIG_FILE_LOCATION);
            configFile.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            
            Configuration config1 = new Configuration("path1");
            Storage.saveConfiguration(config1);

            
            String fileContents = br.readLine();
            
            assertEquals("File should contain configuration", "{\"storagePath\":\""+config1.getStoragePath()+"\"}", fileContents);
            
            br.close();
            configFile.delete();
            
            Configuration config2 = new Configuration("another.path");
            Storage.saveConfiguration(config2);
            
            File configFile2 = new File(Constants.CONFIG_FILE_LOCATION);
            BufferedReader br2 = new BufferedReader(new FileReader(configFile2));
            
            String fileContents2 = br2.readLine();
            
            assertEquals("File should contain configuration", "{\"storagePath\":\""+config2.getStoragePath()+"\"}", fileContents2);
            
            br2.close();
            configFile.delete();
            
            
        } catch (Exception e) {
            fail(e.toString());
        }
    }

}
