package planner;

public class Configuration {
    private String storagePath;
    private Long curTaskNum;
    
    public Configuration(String path, Long taskNum) {
        storagePath = path;
        curTaskNum = taskNum;
    }
    
    public Configuration(String path) {
        storagePath = path;
        curTaskNum = 1l;
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
}
