package planner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    //Sorts according to due date
    //Assumption: TaskList passed to this method MUST have a non-null due date (Non-tentative tasks).
    //Sort in this order, Due Date > Priority > Name >
    public static void sortTaskListByDate(TaskList tasks){
        logger.log(Level.INFO, "pre-sort by date: Starting...");
        try {
            SortLogic.sortByDate(tasks);
        } catch (Exception e) {
            logger.log(Level.WARNING, "sort error encountered", e);
        }
        logger.log(Level.INFO, "post-sort by date: No problems encountered");
    }
    
    //Sorts according to priority
    public static void sortTaskListByPriority(TaskList tasks){
        logger.log(Level.INFO, "pre-sort bypPriority: Starting...");
        try{
            SortLogic.sortByPriority(tasks);
        } catch (Exception e) {
            logger.log(Level.WARNING, "sort error encountered", e);
        }
        logger.log(Level.INFO, "post-sort by priority: No problems encountered");
    }

    //Copies confirmed or tentative tasks into their respective TaskLists
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
    
    //Copies searched results into the searchList 
    public static void searchTaskByTags(TaskList input, TaskList searchList, String tagToLookFor) {
        logger.log(Level.INFO, "search by tags: Starting...");
        try {
            SearchLogic.searchByTags(input, searchList, tagToLookFor);
        } catch (Exception e) {
            logger.log(Level.WARNING, "search error encountered", e);
        }
        logger.log(Level.INFO, "search by tags: No problems encountered");  
    }
    
    public static void searchAll(TaskList input, TaskList searchList, String wordToLookFor) {
        logger.log(Level.INFO, "search all: Starting...");
        try {
            SearchLogic.searchAll(input,searchList, wordToLookFor);
        } catch (Exception e) {
            logger.log(Level.WARNING, "search error encountered", e);
        }
        logger.log(Level.INFO, "search all: No problems encountered");  
    }
}