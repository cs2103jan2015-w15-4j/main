package planner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * @author Ke Jing
 * SORTLOGIC IS USED TO SORT DISPLAYTASKLIST OR TREEMAPS
 */
public class SortLogic {
    
    /**
     * Comparators for DisplayTaskList
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
        
        private int getTime (DisplayTask task) {
            if (task.getDueDate() == null) {
                
                SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
                
                String endTime = dateFormatter.format(task.getEndDate());
                String today = dateFormatter.format(task.getShownDate());
                
                if (endTime.equals(today)) {
                    //System.out.println("Task ID: " + task.getParent().getID() + " Time: " + getTimeFromDate(task.getEndDate()));
                    return getTimeFromDate(task.getEndDate());
                    
                } else {   
                    //System.out.println("Task ID: " + task.getParent().getID() + " Time: " + 2400);
                    return 2400;                 
                }    
                
            } else {
                //System.out.println("Task ID: " + task.getParent().getID() + " Time: " + getTimeFromDate(task.getDueDate()));
                return getTimeFromDate(task.getDueDate());
            }
        }
        
        private int getTimeFromDate (Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int totalTime = (hours * 100) + minutes;
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
     * Helper Methods
     * @param displayTask1
     * @param displayTask2
     * @return
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
    
    public static DisplayTaskList sortByDate(DisplayTaskList tasks) {
        //ADD LOG
        DisplayTaskList newTasks = new DisplayTaskList(tasks);
        Collections.sort(newTasks, dateComparator);
        return newTasks;
    }
    
    public static DisplayTaskList sortByPriority(DisplayTaskList tasks) {
        //ADD LOG
        DisplayTaskList newTasks = new DisplayTaskList(tasks);
        Collections.sort(newTasks, priorityComparator);
        return newTasks;
    }
    
    public static DisplayTaskList sortByName(DisplayTaskList tasks) {
        
        DisplayTaskList newTasks = new DisplayTaskList(tasks);
        Collections.sort(newTasks, nameComparator);
        return newTasks;
    }
    
    public static TreeMap <Date, DisplayTaskList> sortTreeMapIntoSetMapByDate(TreeMap <Date, DisplayTaskList> map) {
        TreeMap<Date, DisplayTaskList> sortedTree = 
                new TreeMap<Date, DisplayTaskList>(dateComparatorForMap);
        
        for (Map.Entry<Date, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();     
            Date key = entry.getKey();
            
            if (key == null) {          
                DisplayTaskList sortedDisplayList = sortByPriority(unsortedList);     
                sortedTree.put(key, sortedDisplayList);           
            } else {            
                DisplayTaskList sortedDisplayList = sortByDate(unsortedList);       
                sortedTree.put(key, sortedDisplayList);
            }        
        }
        return sortedTree;
    }
    
    public static TreeMap <Integer, DisplayTaskList> sortTreeMapIntoSetMapByPriority(TreeMap <Integer, DisplayTaskList> map) {
        TreeMap <Integer, DisplayTaskList> displayMap = new TreeMap 
                <Integer, DisplayTaskList> (priorityComparatorForMap);
        
        for (Map.Entry<Integer, DisplayTaskList> entry : map.entrySet()) {
            
            DisplayTaskList unsortedList = entry.getValue();
            
            Integer key = entry.getKey();
            
            DisplayTaskList sortedDisplayList = sortByName(unsortedList);
                
            displayMap.put(key, sortedDisplayList);
        }
        
        return displayMap;
    }
    
    public static TreeMap <Integer, DisplayTaskList> sortListToMapByPriority (DisplayTaskList input) {
        
        TreeMap <Integer, DisplayTaskList> displayMap = new TreeMap 
                <Integer, DisplayTaskList> (priorityComparatorForMap);
        
        for (int i = 0; i < input.size(); i++) {
        
            DisplayTask temp = input.get(i);
            
            addDisplayTaskToMapByPriority(displayMap, temp);
        }
        
        return displayMap;
    }
    
    private static void addDisplayTaskToMapByPriority(TreeMap < Integer, DisplayTaskList> displayMap, 
            DisplayTask inputTask) {
        
        if (displayMap.containsKey(inputTask.getParent().getPriority())) {
            
            displayMap.get(inputTask.getParent().getPriority()).add(inputTask);
            
        } else {
            
            displayMap.put(inputTask.getParent().getPriority(), new DisplayTaskList());
            
            displayMap.get(inputTask.getParent().getPriority()).add(inputTask);
        }
    }
    
    public static TreeMap <Date, DisplayTaskList> sortListToMapByDate (DisplayTaskList input) {
    
        TreeMap < Date, DisplayTaskList > displayMap = 
                new TreeMap < Date, DisplayTaskList>(dateComparatorForMap);
        
        for (int i = 0; i < input.size(); i++) {   
            
            DisplayTask temp = input.get(i);
            addDisplayTaskToMapByDate(displayMap, temp);
        }
        return displayMap;
    }
    
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
