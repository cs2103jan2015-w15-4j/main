package planner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.Map;
import java.util.Set;


public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    //Sorts according to due date
    //Assumption: TaskList passed to this method MUST have a non-null due date (Non-tentative tasks).
    //Sort in this order, Due Date > Priority > Name >
    public static TaskList sortTaskListByDate(TaskList tasks){
        return SortLogic.sortByDate(tasks);
    }
    
    //Sorts according to priority
    public static TaskList sortTaskListByPriority(TaskList tasks){
        return SortLogic.sortByPriority(tasks);
    }

    //Copies confirmed or tentative tasks into their respective TaskLists
    /*
    public static void splitTasksByTentative(TaskList input, TaskList confirmed, TaskList tentative) {
        logger.log(Level.INFO, "split by tentative: Starting...");
        try {
            SplitLogic.splitByTentative(input, confirmed, tentative);
        } catch (Exception e) {
            logger.log(Level.WARNING, "split error encountered", e);
        }
        logger.log(Level.INFO, "split by tentative: No problems encountered");
    }
    
    //Copies done and incomplete tasks into their respective TaskLists
    public static void splitTaskByDone(TaskList input, TaskList done, TaskList notDone) {
        logger.log(Level.INFO, "split by done: Starting...");
        try {
            SplitLogic.splitByDone(input, done, notDone);
        } catch (Exception e) {
            logger.log(Level.WARNING, "split error encountered", e);
        }
        logger.log(Level.INFO, "split by done: No problems encountered");
    }
    */
    
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
        //ADD MORE STUFF
        
    }
}