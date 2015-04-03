package planner;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * 
 * @author kohwaikit
 *
 */
public class ConfigurationTest {

    @Test
    public void createTest() {
        Configuration oldConfig = new Configuration("C:/Test/", 35);
        Configuration newConfig = new Configuration("/User/Documents/");
    }

    @Test
    public void configShouldReturnCorrectPath() {
        String oldConfigPath = "C:/Test/";
        int oldConfigId = 35;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigId);
        assertEquals("Config path should be " + oldConfigPath, oldConfigPath, oldConfig.getStoragePath());
        
        String newConfigPath = "/User/Documents/";
        Configuration newConfig = new Configuration(newConfigPath);
        assertEquals("Config path should be " + newConfigPath, newConfigPath, newConfig.getStoragePath());
    }
    
    @Test
    public void configShouldReturnCorrectID() {
        String oldConfigPath = "C:/Test/";
        int oldConfigID = 35;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        assertEquals("Config ID should be " + oldConfigID, oldConfigID, oldConfig.getCurTaskNum());
        
        String newConfigPath = "/User/Documents/";
        Configuration newConfig = new Configuration(newConfigPath);
        assertEquals("Config ID should be " + newConfigPath, 1, newConfig.getCurTaskNum());
    }
    
    @Test
    public void newConfigShouldReturnTrueIsNew() {
        String oldConfigPath = "C:/Test/";
        int oldConfigID = 35;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        assertEquals("isNew on an old config should return false", false, oldConfig.isNew());
        
        String newConfigPath = "/User/Documents/";
        Configuration newConfig = new Configuration(newConfigPath);
        assertEquals("isNew on an new config should return false", true, newConfig.isNew());
    }
    
    @Test
    public void setStoragePathShouldUpdateStoragePath() {
        String oldConfigPath = "C:/Test/";
        String oldConfigPathUpdate = "C:/Another/Place";
        int oldConfigID = 35;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        oldConfig.setStoragePath(oldConfigPathUpdate);
        assertEquals("Config path should be " + oldConfigPathUpdate, oldConfigPathUpdate, oldConfig.getStoragePath());
        
        String newConfigPath = "/User/Documents/";
        String newConfigPathUpdate = "/User/Downloads/";
        Configuration newConfig = new Configuration(newConfigPath);
        newConfig.setStoragePath(newConfigPathUpdate);
        assertEquals("Config path should be " + newConfigPathUpdate, newConfigPathUpdate, newConfig.getStoragePath());
    }
    
    @Test
    public void newTaskNumberShouldIncreaseCurTaskNum() {
        String oldConfigPath = "C:/Test/";
        String oldConfigPathUpdate = "C:/Another/Place";
        int oldConfigID = 35;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        for(int i = 0 ; i < 100000 ; i++) {
            assertEquals("ID returned should be " + (oldConfigID + i), oldConfigID + i, oldConfig.getNewTaskNumber());
            assertEquals("New num should be " + (oldConfigID + i + 1), oldConfigID + i + 1, oldConfig.getCurTaskNum());

        }
        
        String newConfigPath = "/User/Documents/";
        String newConfigPathUpdate = "/User/Downloads/";
        Configuration newConfig = new Configuration(newConfigPath);
        for(int i = 0 ; i < 100000 ; i++) {
            assertEquals("ID returned should be " + (1 + i), 1 + i, newConfig.getNewTaskNumber());
            assertEquals("New num should be " + (1 + i + 1), 1 + i + 1, newConfig.getCurTaskNum());

        }
    }
}
