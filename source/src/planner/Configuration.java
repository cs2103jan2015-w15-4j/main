package planner;

/**
 * 
 * @author kohwaikit
 *
 */
public class Configuration {
    private String storagePath;
    private Long curTaskNum;
    private boolean isNew;
    
    public Configuration(String path, Long taskNum) {
        storagePath = path;
        curTaskNum = taskNum;
        isNew = false;
    }
    
    public Configuration(String path) {
        storagePath = path;
        curTaskNum = 1l;
        isNew = true;
        
    }
    
    public String getStoragePath() {
        return storagePath;
    }
    
    public Long getCurTaskNum() {
        return curTaskNum;
    }
    
    public Long newTaskNumber() {
        curTaskNum++;
        return curTaskNum-1;
    }
    
    public void setStoragePath(String newPath) {
        storagePath = newPath;
    }
    
    public boolean isNew() {
        return isNew;
    }
}
