package planner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author Ke Jing
 * 
 * THIS LOGIC CLASS HAS YET TO BE TESTED AS OF NOW
 * WILL DEFINITELY BE REFACTORED SINCE MOST OF THE METHODS ARE VERY SIMILAR IN STRUCTURE 
 */

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        return SearchLogic.searchPeriod(input, start, end);
    }
    
    public static TaskList searchPriority(TaskList input , int priority) {
        return SearchLogic.searchPriorityGreaterThan(input, priority);
    }
    
    public static TaskList searchName(TaskList input, String name) {
        return SearchLogic.searchName(input, name);
    }
    
    public static TaskList searchDescription(TaskList input, String desc) {
        return SearchLogic.searchDesc(input, desc);
    }
    
    public static TaskList searchTag(TaskList input, String tags) {
        return SearchLogic.searchTags(input, tags);
    }
    
    public static TaskList searchOverDuedTasks (TaskList input) {
        return SearchLogic.searchOverDuedTask(input);
    }
    
    public static TaskList searchUpcomingTasks (TaskList input) {
        return SearchLogic.searchUpcomingTask(input);
    }
    
    public static TaskList searchTimedTask (TaskList input) {
        return SearchLogic.searchTimedTask(input);
    }
    
    public static TaskList searchDone (TaskList input) {
        return SearchLogic.searchDone(input);
    }
    
    public static TaskList searchNotDone (TaskList input) {
        return SearchLogic.searchNotDone(input);
    }
    
    public static TaskList searchFloating (TaskList input) {
        return SearchLogic.searchFloating(input);
    }
    
    public static TaskList searchConfirmed (TaskList input) {
        return SearchLogic.searchConfirmed(input);
    }
    
    /**This method searches for time clashes if found within the TaskList provided
     * Output: returns -1 if no clashes are found 
     *         OR
     *         returns int task ID for tasks that are found to clash
     *         
     * WORD OF CAUTION: Assuming a task is 3 months long, this method would report clash
     *                  for every clash search within this time period
     */
    public static int findClash(TaskList taskInput, Date start, Date end) {
        return SearchLogic.searchForClash(taskInput, start, end);
    }
    
    /**
     * This method splits TaskList into displayTaskList regardless it is floating or null
     * Purpose: For testing
     */
    public static DisplayTaskList splitAllTask (TaskList input) {
        return SplitLogic.splitAllTaskList(input);
    }
    
    /**
     * These methods sorts DisplayTaskLists into TreeMaps
     * Purpose: For testing
     * @param taskInput
     * @return
     */
    public static TreeMap<Date, DisplayTaskList> convertToTreeMapWithDate(DisplayTaskList taskInput) {
        return SortLogic.sortListToMapByDate(taskInput);
    }
    
    public static TreeMap<Integer, DisplayTaskList> convertToTreeMapWithPriority(DisplayTaskList taskInput) {
        return SortLogic.sortListToMapByPriority(taskInput);
    }
    
    /**
     * Creates a sorted Set of map entries using an input TaskList
     */
    public static Set<Map.Entry<Date, DisplayTaskList>> displayAllTaskByDate (TaskList taskInput) {
        
        DisplayTaskList input = splitAllTask(taskInput);
        
        TreeMap<Date, DisplayTaskList> allTaskMap = SortLogic.sortListToMapByDate(input);
        
        return convertTreeMapToSetMapByDate(allTaskMap);
        
    }
    
    public static Set<Map.Entry<Integer, DisplayTaskList>> displayAllTaskByPriority (TaskList taskInput) {
        
        DisplayTaskList floatingList = splitAllTask(taskInput); 
        
        TreeMap<Integer, DisplayTaskList> floatingMap = SortLogic.sortListToMapByPriority(floatingList);
        
        return convertTreeMapToSetMapByName(floatingMap);
    }
    
    public static Set<Map.Entry<Integer, DisplayTaskList>> convertTreeMapToSetMapByName (TreeMap <Integer, DisplayTaskList> map) {
        TreeMap <Integer, DisplayTaskList> priorityMap = SortLogic.sortTreeMapIntoSetMapByPriority(map);
        return priorityMap.entrySet();
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> convertTreeMapToSetMapByDate (TreeMap <Date, DisplayTaskList> map) {
        TreeMap <Date, DisplayTaskList> dateMap = SortLogic.sortTreeMapIntoSetMapByDate(map);
        return dateMap.entrySet();
    }
}