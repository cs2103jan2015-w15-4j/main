//@author A0110797B

package planner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import planner.Constants.SearchType;

/**
 * THIS LOGIC CLASS HAS YET TO BE TESTED AS OF NOW
 * WILL DEFINITELY BE REFACTORED SINCE MOST OF THE METHODS ARE VERY SIMILAR IN STRUCTURE 
 */

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    /**
     * 
     * @param input 
     * @param start
     * @param end
     * @return
     */
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        return SearchLogic.searchPeriod(input, start, end);
    }
    
    public static TaskList searchToday(TaskList input) {
        Date today = new Date();
        return SearchLogic.searchDay(input, today);
    }
    
    public static TaskList searchDay(TaskList input, Date day) {
        return SearchLogic.searchDay(input, day);
    }
    
    public static TaskList searchPriority(TaskList input , int priority) {
        return SearchLogic.searchPriorityGreaterThan(input, priority);
    }
    
    //All to be the same method
    public static TaskList searchName(TaskList input, String name) {
        return SearchLogic.searchString(input, name, SearchType.SEARCH_NAME);
    }
    
    public static TaskList searchDescription(TaskList input, String desc) {
        return SearchLogic.searchString(input, desc, SearchType.SEARCH_DESC);
    }
    
    public static TaskList searchTag(TaskList input, String tags) {
        return SearchLogic.searchString(input, tags, SearchType.SEARCH_TAG);
    }
    
    public static TaskList searchOverDuedTasks (TaskList input) {
        return SearchLogic.searchTaskDue(input, SearchType.SEARCH_OVERDUE);
    }
    
    public static TaskList searchUpcomingTasks (TaskList input) {
        return SearchLogic.searchTaskDue(input, SearchType.SEARCH_UPCOMING);
    }
    
    public static TaskList searchTimedTask (TaskList input) {
        return SearchLogic.searchTaskProperties(input, SearchType.SEARCH_TIMED);
    }
    
    public static TaskList searchDone (TaskList input) {
        return SearchLogic.searchTaskProperties(input, SearchType.SEARCH_DONE);
    }
    
    public static TaskList searchNotDone (TaskList input) {
        return SearchLogic.searchTaskProperties(input, SearchType.SEARCH_NOTDONE);
    }
    
    public static TaskList searchFloating (TaskList input) {
        return SearchLogic.searchTaskProperties(input, SearchType.SEARCH_FLOATING);
    }
    
    public static TaskList searchConfirmed (TaskList input) {
        return SearchLogic.searchTaskProperties(input, SearchType.SEARCH_CONFIRMED);
    }
    
    /**
     * 
     * @param taskInput
     * @param start
     * @param end
     * @return
     */
    public static int findClash(TaskList taskInput, Date start, Date end) {
        return SearchLogic.searchForClash(taskInput, start, end);
    }
    
    /**
     * Purpose is for testing
     * 
     * @param input
     * @return
     */
    public static DisplayTaskList splitAllTask (TaskList input) {
        return SplitLogic.splitAllTaskList(input);
    }
    
    public static DisplayTaskList splitAllPriorityTask(TaskList input) {
        return SplitLogic.splitAllPriorityTask(input);
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
    
    public static Set<Map.Entry<Integer, DisplayTaskList>> displaySearchedTaskByPriority (TaskList taskInput) {
        
        DisplayTaskList searchedList = splitAllPriorityTask(taskInput);
        
        TreeMap<Integer, DisplayTaskList> searchedMap = SortLogic.sortListToMapByPriority(searchedList);
        
        return convertTreeMapToSetMapByName(searchedMap);
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