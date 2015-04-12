//@author A0110767B

package planner;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import planner.Constants.ErrorType;
import planner.Constants.SearchType;

/**
 * SEARCH LOGIC TAKES IN TASKLIST AND OUTPUTS ANOTHER TASKLIST TO 
 * BE CONVERTED BY SPLITLOGIC INTO DISPLAYTASKLIST
 */
public class SearchLogic {
    
    public static TaskList searchString(TaskList input, String wordToSearch, 
                                        SearchType searchMode) throws IllegalArgumentException {
        TaskList searchList = new TaskList();
        
        for (int i = 0; i < input.size(); i++) {
            switch (searchMode) {
                case SEARCH_NAME : 
                        String name = input.get(i).getName().toLowerCase();
                        if (name.contains(wordToSearch.toLowerCase())) {
                            searchList.add(input.get(i));
                        }
                    break;
                    
                case SEARCH_DESC :
                        String desc = input.get(i).getDescription().toLowerCase();
                        if (desc.contains(wordToSearch.toLowerCase())) {
                            searchList.add(input.get(i));
                        }
                    break;
                    
                case SEARCH_TAG :
                        String tags = input.get(i).getTag().toLowerCase();
                        if (tags.contains(wordToSearch.toLowerCase())) {
                            searchList.add(input.get(i));
                        }
                    break;
                    
                default :
                    throw new IllegalArgumentException
                    ("SearchType given does not belong to this method.");
            }
        }
        return searchList;
    }
  
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        
        TaskList searchList = new TaskList();
        
        for (int i = 0; i < input.size(); i++) {
            if (!(input.get(i).isFloating())) {       
                if (input.get(i).getEndDate() == null) {
                    
                    long inputTime = input.get(i).getDueDate().getTime();   
                    
                    if ((startTime <= inputTime) && (endTime > inputTime)) {
                        searchList.add(input.get(i));
                    }
                } else {
    
                    long inputStartTime = input.get(i).getDueDate().getTime();
                    long inputEndTime = input.get(i).getEndDate().getTime();
  
                    if (!((inputStartTime > endTime) && (inputEndTime > endTime))
                            && !((inputEndTime < startTime) && (inputStartTime < startTime))) {
    
                        searchList.add(input.get(i));
                    }
                }
            }
        }
        return searchList;
    }
    
    public static TaskList searchDay(TaskList input, Date day) {
        Calendar date = Calendar.getInstance();
        date.setTime(day);
        Calendar start = Calendar.getInstance();
        start.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 23, 59, 59);
        
        TaskList searchList = searchPeriod(input, start.getTime(), end.getTime());
        return searchList;
    }
    
    public static TaskList searchPriorityGreaterThan(TaskList input, int priority) {
        TaskList searchList = new TaskList();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).getPriority() >= priority) {
                searchList.add(input.get(i));
            }
        }
        return searchList;
    }
    
    
    public static TaskList searchTaskDue (TaskList input, SearchType searchMode) {
        TaskList searchList = new TaskList();
        
        Date now = new Date();
        
        long current = now.getTime();
        
        for (int i = 0; i < input.size(); i++) {
            if (!(input.get(i).isFloating()) && !(input.get(i).isDone())) {
                if (input.get(i).getEndDate() == null) {
                    long taskDate = input.get(i).getDueDate().getTime();
                    
                    switch (searchMode) {
                        case SEARCH_OVERDUE :
                            if (current > taskDate) {
                                searchList.add(input.get(i));
                            }
                            break;
                            
                        case SEARCH_UPCOMING :
                            if (current <= taskDate) {
                                searchList.add(input.get(i));
                            }
                            break;
                            
                        default :
                            break;
                    }
                } else {
                    long taskDate = input.get(i).getEndDate().getTime();
                    
                    switch (searchMode) {
                        case SEARCH_OVERDUE :
                            if (current > taskDate) {
                                searchList.add(input.get(i));
                            }
                            break;
                            
                        case SEARCH_UPCOMING :
                            if (current <= taskDate) {         
                                searchList.add(input.get(i));
                            }
                            break;
                            
                        default :
                            break;
                    }
                }
            }
        }
        return searchList;
    }
  
    public static TaskList searchTaskProperties(TaskList input, SearchType searchMode) {
        TaskList searchList = new TaskList();
        
        for (int i = 0; i < input.size(); i++) {
            switch (searchMode) {
            
                case SEARCH_FLOATING :
                    if (input.get(i).isFloating()) {
                        searchList.add(input.get(i));
                    }
                    break;
                    
                case SEARCH_CONFIRMED :      
                    if (!input.get(i).isFloating()) {
                        searchList.add(input.get(i));
                    }
                    break;
                    
                case SEARCH_DONE :
                    if (input.get(i).isDone()) {   
                        searchList.add(input.get(i));
                    }
                    break;
                    
                case SEARCH_NOTDONE :
                    if (!input.get(i).isDone()) {   
                        searchList.add(input.get(i));
                    }
                    break;
                    
                case SEARCH_TIMED :
                    if (input.get(i).isTimed()) {                
                        searchList.add(input.get(i));
                    }
                    break;
                    
                default :
                    break;
            }
        }
        return searchList;
    }
   
    public static int searchForClash(TaskList input, Date start, Date end) {
     
        long startTime = start.getTime();
   
        long endTime = end.getTime();
    
        for (int i = 0; i < input.size(); i++) {
            if (!(input.get(i).isFloating())) {
                if (input.get(i).getEndDate() == null) {
   
                    long inputTime = input.get(i).getDueDate().getTime();
    
                    if ((startTime <= inputTime) && (endTime > inputTime)) {
                        return input.get(i).getID();
                    }
                } else {
    
                    long inputStartTime = input.get(i).getDueDate().getTime();
   
                    long inputEndTime = input.get(i).getDueDate().getTime();
  
                    if (!((inputStartTime > endTime) && 
                            (inputStartTime > endTime)) && 
                            !((inputEndTime < startTime) && 
                                    (inputEndTime < endTime))) {
    
                        return input.get(i).getID();
                    }
                }
            }
        }
        return -1;
    }
    
    
    /**
     * MIGHT NOT BE USED ANYMORE
     */
    private static boolean containsSearchedWord (String description, 
                                String wordToLookFor) throws Exception{
        if (wordToLookFor.equals("")) {
            throw new Exception("Cannot search for empty string");
        }
  
        if (description.equals("")) {
            return false;
        }
 
        String[] token = description.split(" ");
        String[] words = wordToLookFor.split(" ");
     
        for (int i = 0; i < token.length; i++) {
     
            int j = 0;
            int temp = i;
        
            if (words[j].toUpperCase().equals(token[i].toUpperCase().trim())) {
            
                while (words[j].toUpperCase().equals(token[temp].toUpperCase().trim())) {
                    if (j + 1 == words.length) {
                        return true;
                    }
                    j++;
                 
                    if (temp + 1 < token.length) {
                        temp++;
                        
                    } else {
                        return false;
                    }
                }
            }
        }
      
        return false;
    }
}
