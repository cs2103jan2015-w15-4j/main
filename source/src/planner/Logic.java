package planner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
/*    
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
*/    
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        return SearchLogic.searchPeriod(input, start, end);
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
    
    
    /**
     * New methods 
     * @param input
     * @return
     */
    
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
    
    
    public static Set<Map.Entry<Date, DisplayTaskList>> searchOutDatedTasks (TaskList taskInput) {
        
        TaskList input =  SearchLogic.searchOutDated(taskInput);
        
        DisplayTaskList outDatedList = splitAllTask(input);
        
        TreeMap<Date, DisplayTaskList> outDatedMap = SortLogic.sortListToMapByDate(outDatedList);
        
        return convertTreeMapToSetMapByDate(outDatedMap);
    }
    
    
    public static Set<Map.Entry<Integer, DisplayTaskList>> searchFloatingTasks (TaskList taskInput) {
        
        TaskList input = SearchLogic.searchFloating(taskInput);
        
        DisplayTaskList floatingList = splitAllTask(input); 
        
        TreeMap<Integer, DisplayTaskList> floatingMap = SortLogic.sortListToMapByPriority(floatingList);
        
        return convertTreeMapToSetMapByName(floatingMap);
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