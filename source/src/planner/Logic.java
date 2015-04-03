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
/*
    public static Set<Map.Entry<Date, DisplayTaskList>> searchInName (TaskList taskInput, String wordToSearch) {
        
        TaskList input = SearchLogic.searchName(taskInput, wordToSearch);
        
        DisplayTaskList wordList =  splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> searchMap = SortLogic.sortListToMapByDate(wordList);
        
        return convertTreeMapToSetMapByDate(searchMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchInDescription (TaskList taskInput, String wordToSearch) {
        
        TaskList input =  SearchLogic.searchDesc(taskInput, wordToSearch);
        
        DisplayTaskList wordList = splitAllTask(input); 
        
        TreeMap<Date, DisplayTaskList> searchMap = SortLogic.sortListToMapByDate(wordList);
        
        return convertTreeMapToSetMapByDate(searchMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchInTag (TaskList taskInput, String wordToSearch) {
        
        TaskList input = SearchLogic.searchTags(taskInput, wordToSearch);
        
        DisplayTaskList wordList = splitAllTask(input); 
        
        TreeMap<Date, DisplayTaskList> searchMap = SortLogic.sortListToMapByDate(wordList);
        
        return convertTreeMapToSetMapByDate(searchMap);
    }
  
    public static Set<Map.Entry<Date, DisplayTaskList>> searchOverDuedTasks (TaskList taskInput) {
        
        TaskList input =  SearchLogic.searchOverDuedTask(taskInput);
        
        DisplayTaskList outDatedList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> outDatedMap = SortLogic.sortListToMapByDate(outDatedList);
        
        return convertTreeMapToSetMapByDate(outDatedMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchUpcomingTasks (TaskList taskInput) {
        
        TaskList input = SearchLogic.searchUpcomingTask(taskInput);
        
        DisplayTaskList upcomingList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> upcomingMap = SortLogic.sortListToMapByDate(upcomingList);
        
        return convertTreeMapToSetMapByDate(upcomingMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchTimedTask (TaskList taskInput) {
        
        TaskList input = SearchLogic.searchTimedTask(taskInput);
        
        DisplayTaskList timedList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> timedMap = SortLogic.sortListToMapByDate(timedList);
        
        return convertTreeMapToSetMapByDate(timedMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchDone(TaskList taskInput) {
        
        TaskList input = SearchLogic.searchDone(taskInput);
        
        DisplayTaskList doneList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> doneMap = SortLogic.sortListToMapByDate(doneList);
        
        return convertTreeMapToSetMapByDate(doneMap);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchNotDone (TaskList taskInput) {
        
        TaskList input = SearchLogic.searchNotDone(taskInput);
        
        DisplayTaskList notDoneList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> notDoneMap = SortLogic.sortListToMapByDate(notDoneList);
        
        return convertTreeMapToSetMapByDate(notDoneMap);
    }

    public static Set<Map.Entry<Integer, DisplayTaskList>> searchFloatingTasks (TaskList taskInput) {
        
        TaskList input = SearchLogic.searchFloating(taskInput);
        
        DisplayTaskList floatingList = splitAllTask(input); 
        
        TreeMap<Integer, DisplayTaskList> floatingMap = SortLogic.sortListToMapByPriority(floatingList);
        
        return convertTreeMapToSetMapByName(floatingMap);
    }

    public static Set<Map.Entry<Date, DisplayTaskList>> searchConfirmed(TaskList taskInput) {
        
        TaskList input = SearchLogic.searchConfirmed(taskInput);
                
        DisplayTaskList confirmedList = splitAllTask(input); 
        
        TreeMap<Date, DisplayTaskList> confirmedMap = SortLogic.sortListToMapByDate(confirmedList);

        return convertTreeMapToSetMapByDate(confirmedMap);
    }
    */
    
    public static Set<Map.Entry<Integer, DisplayTaskList>> convertTreeMapToSetMapByName (TreeMap <Integer, DisplayTaskList> map) {
        
        for (Map.Entry<Integer, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();
            
            Integer key = entry.getKey();
            
            DisplayTaskList sortedDisplayList = SortLogic.sortByName(unsortedList);
            
            map.remove(key);
                
            map.put(key, sortedDisplayList);
        }
        
        return map.entrySet();
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> convertTreeMapToSetMapByDate (TreeMap <Date, DisplayTaskList> map) {
        
        for (Map.Entry<Date, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();
            
            Date key = entry.getKey();
            
            if (key == null) {
            
                DisplayTaskList sortedDisplayList = SortLogic.sortByPriority(unsortedList);
                
                map.remove(key);
                
                map.put(key, sortedDisplayList);
            
            } else {
            
                DisplayTaskList sortedDisplayList = SortLogic.sortByDate(unsortedList);
                
                map.remove(key);
                
                map.put(key, sortedDisplayList);
            }
        }
        
        return map.entrySet();
    }
}