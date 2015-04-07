//@author A0110767B

package planner;

import java.util.Calendar;
import java.util.Date;

/**
 * SEARCH LOGIC TAKES IN TASKLIST AND OUTPUTS ANOTHER TASKLIST TO 
 * BE CONVERTED BY SPLITLOGIC INTO DISPLAYTASKLIST
 */
public class SearchLogic {
    
    public static TaskList searchName(TaskList input, String wordToSearch) {
    
        TaskList searchList = new TaskList();
     
        try {
            for (int i = 0; i < input.size(); i++) {
                String name = input.get(i).getName().toLowerCase();
                if (name.contains(wordToSearch.toLowerCase())) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
 
        return searchList;
    }
    
    public static TaskList searchDesc(TaskList input, String wordToSearch) {
     
        TaskList searchList = new TaskList();
    
        try {
            for (int i = 0; i < input.size(); i++) {
                String desc = input.get(i).getDescription().toLowerCase();
                if (desc.contains(wordToSearch.toLowerCase())) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    
        return searchList;
    }
    
    public static TaskList searchTags(TaskList input, String tagToLookFor){
        
        TaskList searchList = new TaskList();
        
        try { 
            for (int i = 0; i < input.size(); i++) {
                String tags = input.get(i).getTag().toLowerCase();
                if (tags.contains(tagToLookFor.toLowerCase())) {
                    searchList.add(input.get(i));
                }
            } 
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
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
    
    public static TaskList searchTimedTask (TaskList input) {
        
        TaskList searchList = new TaskList();
     
        for (int i = 0; i < input.size(); i++) {
         
            if (input.get(i).isTimed()) {
          
                searchList.add(input.get(i));
            }
        }
      
        return searchList;
    }
    
    public static TaskList searchOverDuedTask (TaskList input) {
        
        TaskList searchList = new TaskList();
        
        Date now = new Date();
        
        long current = now.getTime();
        
        for (int i = 0; i < input.size(); i++) {
        
            if (!(input.get(i).isFloating()) && !(input.get(i).isDone())) {
            
                if (input.get(i).getEndDate() == null) {
                
                    long taskDate = input.get(i).getDueDate().getTime();
                
                    if (current > taskDate) {
                
                        searchList.add(input.get(i));
                   
                    }
               
                } else {
 
                    long taskDate = input.get(i).getEndDate().getTime();
                    
                    if (current > taskDate) {
    
                        searchList.add(input.get(i));
                    }
                }
            }
         }
    
        return searchList;
    }
    
    public static TaskList searchUpcomingTask (TaskList input) {
   
        TaskList searchList = new TaskList();
   
        Date now = new Date();
  
        long current = now.getTime();
  
        for (int i = 0; i < input.size(); i++) {
     
            if (!(input.get(i).isFloating()) && !(input.get(i).isDone())) {
    
                if (input.get(i).getEndDate() == null) {
    
                    long taskDate = input.get(i).getDueDate().getTime();
         
                    if (current <= taskDate) {
      
                        searchList.add(input.get(i));
                    }
       
                } else {
         
                    long taskDate = input.get(i).getEndDate().getTime();
   
                    if (current <= taskDate) {
          
                        searchList.add(input.get(i));
                    }
                }
            } 
        }
 
        return searchList;
    }
    
    public static TaskList searchFloating(TaskList input) {
        TaskList searchList = new TaskList();
        
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isFloating()) {
                searchList.add(input.get(i));
            }
        }
        return searchList;
    }
    
    public static TaskList searchConfirmed(TaskList input) {
      
        TaskList searchList = new TaskList();
  
        for (int i = 0; i < input.size(); i++) {
   
            if (!input.get(i).isFloating()) {
   
                searchList.add(input.get(i));
            }
        }
    
        return searchList;
    }
    
    public static TaskList searchDone(TaskList input) {
    
        TaskList searchList = new TaskList();
   
        for (int i = 0; i < input.size(); i++) {
  
            if (input.get(i).isDone()) {
        
                searchList.add(input.get(i));
            }
        }
    
        return searchList;
    }
    
    public static TaskList searchNotDone(TaskList input) {
   
        TaskList searchList = new TaskList();
   
        for (int i = 0; i < input.size(); i++) {
   
            if (!input.get(i).isDone()) {
  
                searchList.add(input.get(i));
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
  
                    if (!((inputStartTime > endTime) && (inputStartTime > endTime))
                            && !((inputEndTime < startTime) && (inputEndTime < endTime))) {
    
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
    private static boolean containsSearchedWord (String description, String wordToLookFor) throws Exception{
    
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
