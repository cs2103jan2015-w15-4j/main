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
 * This class contains the main methods that are needed by the engine such as
 * searching and returning sorted Set Maps
 */
public class Logic {
    private static Logger logger = Logger.getLogger("Logic");
    
    /**
     * Returns a tasklist of searched results found within the time period
     * Assumption: start and end date must not be null
     *             start date must be before end date
     * 
     * @param input     Input tasklist to perform time period search on 
     * @param start     Starting time
     * @param end       Ending time
     * @return          A search list containing all tasks found within the time 
     *                  period
     */
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search of time period");
        try {
            searchedList = SearchLogic.searchPeriod(input, start, end);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching time period");
        }
        logger.log(Level.INFO, "End of search of time period");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results found within today
     * 
     * @param input     Input tasklist to perform time period search for today
     * @return          A search list containing all tasks found within today
     */
    public static TaskList searchToday(TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for task today");
        try {
            Date today = new Date();
            searchedList = SearchLogic.searchDay(input, today);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching today");
        }
        logger.log(Level.INFO, "End of search within today");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results found within the specified day
     * Assumptions: day cannot be null
     * 
     * @param input     Input tasklist to perform time period search within specified day
     * @param day       Specific day to be searched upon
     * @return          A search list containing all tasks found within the specified day
     */
    public static TaskList searchDay(TaskList input, Date day) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for task on a set date");
        try {
            searchedList = SearchLogic.searchDay(input, day);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching on set date");
        }
        logger.log(Level.INFO, "End of search for the set date");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for tasks with priority 
     * greater or above the given priority
     * Assumption: priority must be an integer from 0 to 5 (inclusive)
     * 
     * @param input     Input tasklist to perform priority search upon
     * @param priority  The priority number that is used to benchmark 
     * @return          A search list of tasks with greater or equal to 
     *                  the given priority integer
     */
    public static TaskList searchPriority(TaskList input , int priority) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for priority");
        try {
            searchedList = SearchLogic.searchPriorityGreaterThan(input, priority);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for priority");
        }
        logger.log(Level.INFO, "End of search for priority");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched result for tasks containing substring of the name
     * 
     * @param input     Input tasklist to perform the search upon
     * @param name      Task name will be checked to determine whether it 
     *                  contains this substring
     * @return          A search list of tasks with names containing this substring
     */
    public static TaskList searchName(TaskList input, String name) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search within name field");
        try {
            searchedList = SearchLogic.searchString(input, name, SearchType.SEARCH_NAME);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for name");
        }
        logger.log(Level.INFO, "End of search within name field");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched result for tasks containing substring of the description
     * 
     * @param input     Input tasklist to perform the search upon
     * @param name      Task description will be checked to determine whether it 
     *                  contains this substring
     * @return          A search list of tasks with description containing this substring
     */
    public static TaskList searchDescription(TaskList input, String desc) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search within description field");
        try {
            searchedList = SearchLogic.searchString(input, desc, SearchType.SEARCH_DESC);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for description");
        }
        logger.log(Level.INFO, "End of search within description field");
        return searchedList;
        
    }
    
    /**
     * Returns a tasklist of searched result for tasks containing substring of the tags
     * 
     * @param input     Input tasklist to perform the search upon
     * @param name      Task tags will be checked to determine whether it 
     *                  contains this substring
     * @return          A search list of tasks with tags containing this substring
     */
    public static TaskList searchTag(TaskList input, String tags) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search within tag field");
        try {
            searchedList = SearchLogic.searchString(input, tags, SearchType.SEARCH_TAG);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for tag");
        }
        logger.log(Level.INFO, "End of search within tag field");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for tasks that are overdue
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing overdued tasks
     */
    public static TaskList searchOverDuedTasks (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for overdue tasks");
        try {
            searchedList = SearchLogic.searchTaskDue(input, SearchType.SEARCH_OVERDUE);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for overdue tasks");
        }
        logger.log(Level.INFO, "End of search for overdue tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for upcoming tasks
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing upcoming tasks
     */
    public static TaskList searchUpcomingTasks (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for upcoming tasks");
        try {
            searchedList = SearchLogic.searchTaskDue(input, SearchType.SEARCH_UPCOMING);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for upcoming tasks");
        }
        logger.log(Level.INFO, "End of search for upcoming tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for timed tasks
     * This old method is never called by engine since the GUI does not need
     * it anymore due to the new displayTask implementation that splits 
     * most tasks to be displayed into singular dates anyway
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing timed tasks
     */
    public static TaskList searchTimedTask (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for timed tasks");
        try {
            searchedList = SearchLogic.searchTaskProperties(input, SearchType.SEARCH_TIMED);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for timed tasks");
        }
        logger.log(Level.INFO, "End of search for timed tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for completed tasks
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing all completed tasks
     */
    public static TaskList searchDone (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for completed tasks");
        try {
            searchedList = SearchLogic.searchTaskProperties(input, SearchType.SEARCH_DONE);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for completed tasks");
        }
        logger.log(Level.INFO, "End of search for completed tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for uncompleted tasks
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing all uncompleted tasks
     */
    public static TaskList searchNotDone (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for uncompleted tasks");
        try {
            searchedList = SearchLogic.searchTaskProperties(input, SearchType.SEARCH_NOTDONE);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for uncompleted tasks");
        }
        logger.log(Level.INFO, "End of search for uncompleted tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for floating tasks
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing all floating tasks
     */
    public static TaskList searchFloating (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for floating tasks");
        try {
            searchedList = SearchLogic.searchTaskProperties(input, SearchType.SEARCH_FLOATING);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for floating tasks");
        }
        logger.log(Level.INFO, "End of search for floating tasks");
        return searchedList;
    }
    
    /**
     * Returns a tasklist of searched results for non-floating tasks
     * 
     * @param input     Input tasklist to perform the search upon
     * @return          A search list containing all non-floating tasks
     */
    public static TaskList searchConfirmed (TaskList input) {
        TaskList searchedList = new TaskList();
        logger.log(Level.INFO, "Starting search for non-floating tasks");
        try {
            searchedList = SearchLogic.searchTaskProperties(input, SearchType.SEARCH_CONFIRMED);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for non-floating tasks");
        }
        logger.log(Level.INFO, "End of search for non-floating tasks");
        return searchedList;
    }
    
    /**
     * Returns an integer result that is passed from the engine to UI to be displayed to the user
     * 
     * @param taskInput     Input tasklist to check the clash upon
     * @param start         The starting time within the time period
     * @param end           The ending time within the time period
     * @return              An integer that returns either -1 when no clash is detected
     *                      or an ID when the first clash occurs
     */
    public static int findClash(TaskList taskInput, Date start, Date end) {
        int clashID = 0;
        logger.log(Level.INFO, "Starting search for clashes");
        try {
            clashID = SearchLogic.searchForClash(taskInput, start, end);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Search error encountered when searching for clashes");
        }
        logger.log(Level.INFO, "End of search for clashes");
        return clashID;
    }
    
    /**
     * Splits and converts the input tasklist into a displayTaskList by date
     * This method was created solely for testing purposes
     * 
     * @param input     Input tasklist to be scanned through and every task to be 
     *                  converted to a displayTask (can be multiple) instead
     * @return          The split and converted DisplayTaskList is returned
     */
    public static DisplayTaskList splitAllTask (TaskList input) {
        return SplitLogic.splitAllTaskList(input);
    }
    
    /**
     * Splits and converts the input tasklist into a displayTaskList by priority
     * 
     * @param input     Input tasklist to be scanned through and every 1 
     *                  task is converted to 1 displayTask
     * @return          The converted DisplayTaskList is returned after 
     */
    public static DisplayTaskList splitAllPriorityTask(TaskList input) {
        return SplitLogic.splitAllPriorityTask(input);
    }
    /**
     * Splits and converts input DisplayTaskLists into TreeMaps by date
     * Purpose: For testing
     * 
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
     * Every date key is the day where the task will be displayed
     * Every value is the displayTaskList that holds the number of tasks to 
     * be displayed by the UI in days
     * 
     * @param taskInput     Input tasklist to be split, inserted into a 
     *                      TreeMap and sorted by date and converted into 
     *                      Set map
     * @return              A set map containing keys in dates and values in 
     *                      displayTaskLists
     */
    public static Set<Map.Entry<Date, DisplayTaskList>> displayAllTaskByDate (TaskList taskInput) {
        
        DisplayTaskList input = splitAllTask(taskInput);
        
        TreeMap<Date, DisplayTaskList> allTaskMap = SortLogic.sortListToMapByDate(input);
        
        return convertTreeMapToSetMapByDate(allTaskMap);
        
    }
    
    /**
     * Every integer key is the priority where the tasks will be displayed 
     * under
     * Every value is the displayTaskList that holds the number of tasks to 
     * be displayed by the UI in days
     * 
     * @param taskInput     Input tasklist to be split, inserted into TreeMap 
     *                      and sorted by priority and converted into Set map
     * @return              A set map containing keys in Integer and values in 
     *                      displayTaskLists
     */
    public static Set<Map.Entry<Integer, DisplayTaskList>> displayAllTaskByPriority (TaskList taskInput) {
        
        DisplayTaskList floatingList = splitAllTask(taskInput); 
        
        TreeMap<Integer, DisplayTaskList> floatingMap = SortLogic.sortListToMapByPriority(floatingList);
        
        return convertTreeMapToSetMapByName(floatingMap);
    }
    
    /**
     * Every integer key is the priority 
     * Every value is the displayTaskList that holds tasks to be displayed
     * by the UI 
     * 
     * This is used by the engine to search and display today tasks
     * Rather than showing multiple displayTasks that are displaying the same 
     * task, this method does not split tasks into multiple displayTasks
     * 
     * Purpose: Clearer display of today's tasks
     * 
     * @param taskInput     Input tasklist to be converted, inserted into a 
     *                      TreeMap and sorted by priority into Set Map
     * @return              A set map containing keys in Integers and values in 
     *                      displayTaskLists
     */
    public static Set<Map.Entry<Integer, DisplayTaskList>> displaySearchedTaskByPriority (TaskList taskInput) {
        
        DisplayTaskList searchedList = splitAllPriorityTask(taskInput);
        
        TreeMap<Integer, DisplayTaskList> searchedMap = SortLogic.sortListToMapByPriority(searchedList);
        
        return convertTreeMapToSetMapByName(searchedMap);
    }
    
    /**
     * Sorts every node on the TreeMap by name before returning a Set Map
     * 
     * @param map       TreeMap that is passed to be iterated through and 
     *                  sorted accordingly
     * @return          A Set Map that is sorted by name
     */
    public static Set<Map.Entry<Integer, DisplayTaskList>> convertTreeMapToSetMapByName (TreeMap <Integer, DisplayTaskList> map) {
        TreeMap <Integer, DisplayTaskList> priorityMap = SortLogic.sortTreeMapIntoSetMapByPriority(map);
        return priorityMap.entrySet();
    }
    
    /**
     * Sorts every node on the TreeMap by Time before returning a Set Map
     * 
     * @param map       TreeMap that is passed to be iterated through and 
     *                  sorted accordingly
     * @return          A Set Map that is sorted by name
     */
    public static Set<Map.Entry<Date, DisplayTaskList>> convertTreeMapToSetMapByDate (TreeMap <Date, DisplayTaskList> map) {
        TreeMap <Date, DisplayTaskList> dateMap = SortLogic.sortTreeMapIntoSetMapByDate(map);
        return dateMap.entrySet();
    }
}