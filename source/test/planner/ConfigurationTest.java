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
        Configuration oldConfig = new Configuration("C:/Test/", 35l);
        Configuration newConfig = new Configuration("/User/Documents/");
    }

    @Test
    public void configShouldReturnCorrectPath() {
        String oldConfigPath = "C:/Test/";
        long oldConfigId = 35l;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigId);
        assertEquals("Config path should be " + oldConfigPath, oldConfigPath, oldConfig.getStoragePath());
        
        String newConfigPath = "/User/Documents/";
        Configuration newConfig = new Configuration(newConfigPath);
        assertEquals("Config path should be " + newConfigPath, newConfigPath, newConfig.getStoragePath());
    }
    
    @Test
    public void configShouldReturnCorrectID() {
        String oldConfigPath = "C:/Test/";
        Long oldConfigID = 35l;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        assertEquals("Config ID should be " + oldConfigID, oldConfigID, oldConfig.getCurTaskNum());
        
        String newConfigPath = "/User/Documents/";
        Configuration newConfig = new Configuration(newConfigPath);
        assertEquals("Config ID should be " + newConfigPath, Long.valueOf(1), newConfig.getCurTaskNum());
    }
    
    @Test
    public void newConfigShouldReturnTrueIsNew() {
        String oldConfigPath = "C:/Test/";
        Long oldConfigID = 35l;
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
        Long oldConfigID = 35l;
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
        Long oldConfigID = 35l;
        Configuration oldConfig = new Configuration(oldConfigPath, oldConfigID);
        for(int i = 0 ; i < 100000 ; i++) {
            assertEquals("ID returned should be " + (oldConfigID + i), Long.valueOf(oldConfigID + i), oldConfig.newTaskNumber());
            assertEquals("New num should be " + (oldConfigID + i + 1), Long.valueOf(oldConfigID + i + 1), oldConfig.getCurTaskNum());

        }
        
        String newConfigPath = "/User/Documents/";
        String newConfigPathUpdate = "/User/Downloads/";
        Configuration newConfig = new Configuration(newConfigPath);
        for(int i = 0 ; i < 100000 ; i++) {
            assertEquals("ID returned should be " + (1 + i), Long.valueOf(1 + i), newConfig.newTaskNumber());
            assertEquals("New num should be " + (1 + i + 1), Long.valueOf(1 + i + 1), newConfig.getCurTaskNum());

        }
    }
}
