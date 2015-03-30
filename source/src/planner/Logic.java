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
    
    //Sorts according to due date
    //Assumption: TaskList passed to this method MUST have a non-null due date (Non-tentative tasks).
    //Sort in this order, Due Date > Priority > Name >
    public static TaskList sortTaskListByDate(TaskList tasks){
        TaskList sortList = SortLogic.sortByDate(tasks);
        return sortList;
    }
    
    //Sorts according to priority
    public static TaskList sortTaskListByPriority(TaskList tasks){
        return SortLogic.sortByPriority(tasks);
    }
    
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
    
    public static Set<Map.Entry<Date, DisplayTaskList>> splitDisplayedTask (DisplayTaskList input) {
        TreeMap<Date, DisplayTaskList> allTaskMap = SortLogic.sortListToMapByDate(input);
        
        for (Map.Entry<Date, DisplayTaskList> entry : allTaskMap.entrySet()) {
            DisplayTaskList unsortedList = entry.getValue();
            DisplayTaskList sortedDisplayList = SortLogic.sortByDate(unsortedList);
        }
        
    }
}