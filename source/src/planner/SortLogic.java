//@author A0110797B

package planner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import planner.Constants.SortType;

/**
 * This class is used to sort displayTaskList as well as how tasks are ordered 
 * before inserting into TreeMaps. 
 * Contains multiple ways to sort such as sorting by date, priority, ID 
 * and name
 */
public class SortLogic {
    
    private static final int TIME_GREATER_THAN_2359 = 2400;
    private static final int HOUR_MULTIPLIER = 100;
    
    /**
     * Comparators for DisplayTaskList comparison
     * Order: Time -> priority -> Name -> Task ID
     */
    private static Comparator<DisplayTask> dateComparator = new Comparator<DisplayTask>() {
        
        @Override
        public int compare(DisplayTask task1, DisplayTask task2) {
            
            int time1 = getTime(task1);
            int time2 = getTime(task2);
            
            if (time1 < time2) {        
                return -1;     
                
            } else if (time1 > time2) { 
                return 1;  
                
            } else {
                return comparePriority(task1, task2);
            }
        }
        
        /**
         * Determines whether the task can be completed in a specific date
         * Otherwise return 2400 which is a greater number than 2359
         * such that the task will always appear at the bottom 
         * 
         * @param task      DisplayTask that will be checked whether it is 
         *                  a due date or timed task
         * @return          An integer format of time in a day 
         */
        private int getTime (DisplayTask task) {
            if (task.getDueDate() == null) {
                
                SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
                
                String endTime = dateFormatter.format(task.getEndDate());
                String today = dateFormatter.format(task.getShownDate());
                
                if (endTime.equals(today)) {
                    return getTimeFromDate(task.getEndDate());
                    
                } else {   
                    return TIME_GREATER_THAN_2359;                 
                }    
                
            } else {
                return getTimeFromDate(task.getDueDate());
            }
        }
        
        /**
         * Converts time for comparison into a 4 digit Integer
         * 0 is the smallest indicating 12.00 AM
         * 2359 is the biggest indicatin 11.59 PM
         * 
         * @param date      The date to be used for comparison
         * @return          An integer that is used for time comparison
         */
        private int getTimeFromDate (Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int totalTime = (hours * HOUR_MULTIPLIER) + minutes;
            
            return totalTime;
        }
    };
    
    //Comparator for sorting tasks according to priority
    private static Comparator<DisplayTask> priorityComparator = new Comparator<DisplayTask>() {
        
        @Override
        public int compare(DisplayTask displayTask1, DisplayTask displayTask2) {  
            return comparePriority(displayTask1, displayTask2);
        }
    };
    
    private static Comparator<DisplayTask> nameComparator = new Comparator<DisplayTask>() {
        
        @Override
        public int compare(DisplayTask displayTask1, DisplayTask displayTask2) { 
            return compareName(displayTask1, displayTask2);
        }
    };
    
    /**
     * Comparators for Maps
     */
    
    private static Comparator<Integer> priorityComparatorForMap = new Comparator<Integer>() {
        
        @Override
        public int compare(Integer priority1, Integer priority2) {   
            if (priority1 > priority2) {
                return -1;
     
            } else if (priority1 < priority2) {
                return 1;
         
            } else {
                return 0;
            }
        }
    };
    
    
    private static Comparator<Date> dateComparatorForMap = new Comparator<Date>() {
        
        @Override
        public int compare(Date date1, Date date2) {
            if( date1 != null && date2 != null ){ 
                SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
                
                String dateOneString = dateFormatter.format(date1);
                String dateTwoString = dateFormatter.format(date2);
                
                if( dateOneString.equals(dateTwoString) ){       
                    return 0;
                    
                } else{         
                    return date1.compareTo(date2);        
                }            
            } else if( date1 != null ){   
                return -1;
                
            } else if( date2 != null ){     
                return 1;
                
            } else{   
                return 0;      
            }
        }   
    };
    
    /**
     * Compares the priority of 2 DisplayTasks
     * If they are equal, compare by name
     * 
     * @param displayTask1      First DisplayTask to be compared
     * @param displayTask2      Second DisplayTask to be compared
     * @return                  An integer result dictating which DisplayTask
     *                          is greater 
     */
    private static int comparePriority(DisplayTask displayTask1, DisplayTask displayTask2) {
        Task task1 = displayTask1.getParent();
        Task task2 = displayTask2.getParent();
        
        int result = task1.getPriority() - task2.getPriority();
        
        if (result > 0) {    
            return -1;
            
        } else if (result < 0) {
            return 1;
            
        } else {   
            return compareName(displayTask1, displayTask2);
        }
    }
    
    /**
     * Compares the name of 2 DisplayTasks
     * If they are equal, compare by ID
     * Assumption: No 2 Task ID can be the same
     * 
     * @param displayTask1      First DisplayTask to be compared
     * @param displayTask2      Second DisplayTask to be compared
     * @return                  An integer result dictating which DisplayTask
     *                          is greater 
     */
    private static int compareName(DisplayTask displayTask1, DisplayTask displayTask2) {
        
        Task task1 = displayTask1.getParent();
        Task task2 = displayTask1.getParent();
        
        String name1 = task1.getName();
        String name2 = task2.getName();
        
        int name = name1.compareTo(name2);
        
        if (name > 0) {       
            return 1;
            
        } else if (name < 0) {  
            return -1;
            
        } else {  
            long ID1 = task1.getID();
            long ID2 = task2.getID();
            
            if (ID1 > ID2) {    
                return 1;
                
            } else if (ID1 < ID2) {   
               return -1;
               
            } else {
                return 0;
            }
        }
    }
    
    /**
     * Sorts a given displayTaskList in accordance to the sort type provided
     * 
     * @param tasks         Input displayTaskList to be sorted
     * @param sortMode      The type of sorting that the engine demands
     * @return              A sorted DisplayTaskList
     * @throws IllegalArgumentException     Thrown when Sort type given is invalid
     */
    public static DisplayTaskList sortDisplayTaskList(DisplayTaskList tasks, SortType sortMode) throws IllegalArgumentException {
        DisplayTaskList newTasks = new DisplayTaskList(tasks);
        switch (sortMode) {
            case SORT_DATE :
                Collections.sort(newTasks, dateComparator);
                break;
                
            case SORT_PRIORITY :
                Collections.sort(newTasks, priorityComparator);
                break;
                
            case SORT_NAME :
                Collections.sort(newTasks, nameComparator);
                break;
                
            default :
                throw new IllegalArgumentException
                ("SortType given does not belong to this method.");
        }
        return newTasks;
    }
   
    /**
     * Iterated through all the nodes in a Date TreeMap and sorts all 
     * DisplayTaskLists in order of time, priority, name and ID
     * 
     * @param map       The TreeMap to be sorted
     * @return          A sorted TreeMap by order of time
     */
    public static TreeMap <Date, DisplayTaskList> sortTreeMapIntoSetMapByDate(
                                        TreeMap <Date, DisplayTaskList> map) {
        
        TreeMap<Date, DisplayTaskList> sortedTree = 
                new TreeMap<Date, DisplayTaskList>(dateComparatorForMap);
        
        for (Map.Entry<Date, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();     
            Date key = entry.getKey();
            
            if (key == null) {          
                DisplayTaskList sortedDisplayList = 
                        sortDisplayTaskList(unsortedList, 
                                            SortType.SORT_PRIORITY);  
                
                sortedTree.put(key, sortedDisplayList);           
            } else {            
                DisplayTaskList sortedDisplayList = 
                        sortDisplayTaskList(unsortedList, SortType.SORT_DATE);  
                
                sortedTree.put(key, sortedDisplayList);
            }        
        }
        return sortedTree;
    }
    
    /**
     * Iterated through all the nodes in an Integer TreeMap and sorts all 
     * DisplayTaskLists in order of priority, name and ID
     * 
     * @param map       The TreeMap to be sorted
     * @return          A sorted TreeMap by order of priority
     */
    public static TreeMap <Integer, DisplayTaskList> 
                            sortTreeMapIntoSetMapByPriority(TreeMap <Integer, 
                                                        DisplayTaskList> map) {
        
        TreeMap <Integer, DisplayTaskList> displayMap = new TreeMap 
                <Integer, DisplayTaskList> (priorityComparatorForMap);
        
        for (Map.Entry<Integer, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();
            
            Integer key = entry.getKey();
            
            DisplayTaskList sortedDisplayList = 
                    sortDisplayTaskList(unsortedList, SortType.SORT_NAME);
                
            displayMap.put(key, sortedDisplayList);
        }
        
        return displayMap;
    }
    
    /**
     * Sorts and inserts a displayTask from the list into the corresponding
     * priority key within the TreeMap
     * 
     * @param input     The displayTaskList to be read
     * @return          A TreeMap that contains all displayTasks under their 
     *                  priority header
     */
    public static TreeMap <Integer, DisplayTaskList> sortListToMapByPriority 
                                                    (DisplayTaskList input) {
        
        TreeMap <Integer, DisplayTaskList> displayMap = new TreeMap 
                <Integer, DisplayTaskList> (priorityComparatorForMap);
        
        for (int i = 0; i < input.size(); i++) {
        
            DisplayTask temp = input.get(i);
            
            addDisplayTaskToMapByPriority(displayMap, temp);
        }
        
        return displayMap;
    }
    
    /**
     * Sorts and inserts a displayTask from the list into the corresponding
     * Date key within the TreeMap
     * 
     * @param input     The displayTaskList to be read
     * @return          A TreeMap that contains all displayTasks under their 
     *                  date header
     */
    public static TreeMap <Date, DisplayTaskList> sortListToMapByDate (
                                                    DisplayTaskList input) {
    
        TreeMap < Date, DisplayTaskList > displayMap = 
                new TreeMap < Date, DisplayTaskList>(dateComparatorForMap);
        
        for (int i = 0; i < input.size(); i++) {   
            
            DisplayTask temp = input.get(i);
            addDisplayTaskToMapByDate(displayMap, temp);
        }
        return displayMap;
    }
    
    /**
     * Adding of a the displayTask into the correct priority header of the
     * TreeMap
     * 
     * @param displayMap        TreeMap that is used for containing displayTask
     * @param inputTask         The displayTask that is checked and inserted 
     *                          into the correct key header in the TreeMap
     */
    private static void addDisplayTaskToMapByPriority(
                            TreeMap < Integer, DisplayTaskList> displayMap, 
                            DisplayTask inputTask) {
        
        if (displayMap.containsKey(inputTask.getParent().getPriority())) {
            
            displayMap.get(inputTask.getParent().getPriority()).add(inputTask);
            
        } else {
            
            displayMap.put(inputTask.getParent().getPriority(), 
                                        new DisplayTaskList());
            
            displayMap.get(inputTask.getParent().getPriority()).add(inputTask);
        }
    }

    /**
     * Adding of a the displayTask into the correct Date header of the
     * TreeMap
     * 
     * @param displayMap        TreeMap that is used for containing displayTask
     * @param inputTask         The displayTask that is checked and inserted 
     *                          into the correct key header in the TreeMap
     */
    private static void addDisplayTaskToMapByDate(
            TreeMap <Date, DisplayTaskList> displayMap, 
            DisplayTask inputTask) {
        
        if (displayMap.containsKey(inputTask.getShownDate())) {        
            displayMap.get(inputTask.getShownDate()).add(inputTask);
            
        } else {        
            displayMap.put(inputTask.getShownDate(), new DisplayTaskList());     
            displayMap.get(inputTask.getShownDate()).add(inputTask);
        }
    }
    
}
