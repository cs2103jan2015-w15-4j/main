package planner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collection;

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    
    //Copies searched results into the searchList 
    public static TaskList searchTaskByTags(TaskList input, String tagToLookFor) {
        logger.log(Level.INFO, "search by tags: Starting...");
        TaskList searchList = new TaskList();
        try {
             searchList = SearchLogic.searchByTags(input, tagToLookFor);
        } catch (Exception e) {
            logger.log(Level.WARNING, "search error encountered", e);
        }
        logger.log(Level.INFO, "search by tags: No problems encountered");  
        return searchList;
    }
    
    public static TaskList searchAll(TaskList input, String wordToLookFor) {
        TaskList searchList = new TaskList();
        logger.log(Level.INFO, "search all: Starting...");
        try {
            searchList = SearchLogic.searchAll(input, wordToLookFor);
        } catch (Exception e) {
            logger.log(Level.WARNING, "search error encountered", e);
        }
        logger.log(Level.INFO, "search all: No problems encountered");  
        return searchList;
    }
    
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        return SearchLogic.searchPeriod(input, start, end);
    }
    
    public static TaskList searchPriority(TaskList input , int priority) {
        return SearchLogic.searchPriorityGreaterThan(input, priority);
    }
    
    public static TaskList searchTentative(TaskList input) {
        return SearchLogic.searchTentative(input);
    }
    
    public static TaskList searchConfirmed(TaskList input) {
        return SearchLogic.searchConfirmed(input);
    }
    
    public static TaskList searchDone(TaskList input) {
        return SearchLogic.searchDone(input);
    }
    
    public static TaskList searchNotDone(TaskList input) {
        return SearchLogic.searchNotDone(input);
    }
    
    public static DisplayTaskList splitAllTask (TaskList input) {
        return SplitLogic.splitAllTaskList(input);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> splitDisplayAllTask (DisplayTaskList input) {
        
        TreeMap<Date, DisplayTaskList> allTaskMap = SortLogic.sortListToMapByDate(input);
        
        for (Map.Entry<Date, DisplayTaskList> entry : allTaskMap.entrySet()) {
            DisplayTaskList unsortedList = entry.getValue();
            Date key = entry.getKey();
            if (key == null) {
                DisplayTaskList sortedDisplayList = SortLogic.sortByPriority(unsortedList);
                allTaskMap.remove(key);
                allTaskMap.put(key, sortedDisplayList);
            } else {
                DisplayTaskList sortedDisplayList = SortLogic.sortByDate(unsortedList);
                allTaskMap.remove(key);
                allTaskMap.put(key, sortedDisplayList);
            }
        }
        
        return allTaskMap.entrySet();
        
    }
}