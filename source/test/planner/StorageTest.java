package planner;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

}
