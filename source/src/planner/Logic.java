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
 */

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
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
    
    private static DisplayTaskList splitAllTask (TaskList input) {
        return SplitLogic.splitAllTaskList(input);
    }
    
    public static Set<Map.Entry<Date, DisplayTaskList>> splitDisplayAllTask (TaskList taskInput) {
        
        DisplayTaskList input = splitAllTask(taskInput);
        
        TreeMap<Date, DisplayTaskList> allTaskMap = SortLogic.sortListToMapByDate(input);
        
        return convertTreeMapToSetMapByDate(allTaskMap);
        
    }
    
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
    
    private static Set<Map.Entry<Integer, DisplayTaskList>> convertTreeMapToSetMapByName (TreeMap <Integer, DisplayTaskList> map) {
        
        for (Map.Entry<Integer, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();
            
            Integer key = entry.getKey();
            
            DisplayTaskList sortedDisplayList = SortLogic.sortByName(unsortedList);
            
            map.remove(key);
                
            map.put(key, sortedDisplayList);
        }
        
        return map.entrySet();
    }
    
    private static Set<Map.Entry<Date, DisplayTaskList>> convertTreeMapToSetMapByDate (TreeMap <Date, DisplayTaskList> map) {
        
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