package planner;

public class Configuration {
    private String storagePath;
    
    public Configuration(String path) {
        storagePath = path;
    }
    
    public String getStoragePath() {
        return storagePath;
    }
    
    public void setStoragePath(String newPath) {
        storagePath = newPath;
    }
}
