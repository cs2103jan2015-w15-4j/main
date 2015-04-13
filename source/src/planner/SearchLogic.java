//@author A0110767B

package planner;

import java.util.Calendar;
import java.util.Date;

import planner.Constants.SearchType;

/**
 * SearchLogic is a class used for taking in a tasklist and outputting a 
 * searched tasklist to be used for further conversion, splitting and sorting
 */
public class SearchLogic {
    
    /**
     * Searches to see if name, description or tag field contains the substring
     * of given wordToSearch
     * 
     * @param input             Input tasklist to be searched
     * @param wordToSearch      String that is used for searching
     * @param searchMode        Constants used to determine search type
     * @return                  Tasklist containing the search results
     * @throws IllegalArgumentException     Thrown when search type is invalid
     */
    public static TaskList searchString(TaskList input, String wordToSearch, 
                                        SearchType searchMode) throws 
                                        IllegalArgumentException {
        
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
                        String desc = 
                            input.get(i).getDescription().toLowerCase();
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
    
    /**
     * Uses search period method to find tasks due on a specific date
     * 
     * @param input     Input tasklist
     * @param day       Specific date to be searched
     * @return          Tasklist containing the search results
     */
    public static TaskList searchDay(TaskList input, Date day) {
        Calendar date = Calendar.getInstance();
        date.setTime(day);
        Calendar start = Calendar.getInstance();
        start.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 
                  date.get(Calendar.DATE), 0, 0, 0);
        Calendar end = Calendar.getInstance();
        end.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 
                date.get(Calendar.DATE), 23, 59, 59);
        
        TaskList searchList = searchPeriod(input, start.getTime(), 
                                           end.getTime());
        return searchList;
    }
    
    /**
     * Searches a tasklist to find tasks that fall within the given time period
     * 
     * @param input     Input tasklist
     * @param start     Start of time period
     * @param end       End of time period
     * @return          Tasklist containing the search results
     */
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        
        assert(startTime < endTime);
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
  
                    if (!((inputStartTime > endTime) && 
                            (inputEndTime > endTime)) && 
                            !((inputEndTime < startTime) && 
                                    (inputStartTime < startTime))) {
    
                        searchList.add(input.get(i));
                    }
                }
            }
        }
        return searchList;
    }
    
    /**
     * Searches the tasklist for tasks with priority above or equal to the 
     * given priority
     * 
     * @param input         Input tasklist
     * @param priority      Given priority level to search the tasklist for
     * @return              Tasklist containing the search results
     */
    public static TaskList searchPriorityGreaterThan(TaskList input, int priority) {
        assert(priority < 6);
        assert(priority >= 0);
        TaskList searchList = new TaskList();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).getPriority() >= priority) {
                searchList.add(input.get(i));
            }
        }
        return searchList;
    }
    
    /**
     * Searches whether tasks in the given tasklist is overdue or upcoming
     * 
     * @param input         Input tasklist
     * @param searchMode    Constants used to determine search type
     * @return              Tasklist containing the search results
     * @throws IllegalArgumentException     Thrown when search type is invalid
     */
    public static TaskList searchTaskDue (TaskList input, SearchType searchMode) throws IllegalArgumentException {
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
                            throw new IllegalArgumentException("SearchType given does not belong to this method.");
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
                            throw new IllegalArgumentException("SearchType given does not belong to this method.");
                    }
                }
            }
        }
        return searchList;
    }
  
    /**
     * Searches and returns a tasklist containing tasks of given properties
     * 
     * @param input             Input tasklist
     * @param searchMode        Constants used to determine search type
     * @return                  Tasklist containing the search results
     * @throws IllegalArgumentException         Thrown when search type is invalid
     */
    public static TaskList searchTaskProperties(TaskList input, SearchType searchMode) throws IllegalArgumentException{
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
                    throw new IllegalArgumentException("SearchType given does not belong to this method.");
            }
        }
        return searchList;
    }
   
    /**
     * Searches whether a task clashes with any other tasks that are found 
     * within the same time period
     * 
     * @param input     Input tasklist
     * @param start     Start time period
     * @param end       End time period
     * @return          An Integer that either contains -1 if no clashes were found
     *                  or the task ID at the first occurrence of clash
     */
    public static int searchForClash(TaskList input, Date start, Date end) {
     
        long startTime = start.getTime();
        long endTime = end.getTime();
    
        for (int i = 0; i < input.size(); i++) {
            if (!(input.get(i).isDone())) {
                if (!(input.get(i).isFloating())) {
                    if (input.get(i).getEndDate() == null) {
       
                        long inputTime = input.get(i).getDueDate().getTime();
        
                        if ((startTime <= inputTime) && (endTime > inputTime)) {
                            return input.get(i).getID();
                        }
                    } else {
                        long inputStartTime = input.get(i).getDueDate().getTime();
                        long inputEndTime = input.get(i).getEndDate().getTime();
      
                        if (!((inputStartTime > endTime) && 
                                (inputEndTime > endTime)) && 
                                !((inputEndTime < startTime) && 
                                        (inputStartTime < startTime))) {
        
                            return input.get(i).getID();
                        } else if ((inputStartTime < startTime) && (inputEndTime > endTime)) {
                            return input.get(i).getID();                        
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Determines whether a String contains the given word(s) (not substring)
     * This method is no longer used 
     * Reason: The group came to the conclusion that no user will perfectly 
     * remember every name, description or tag he typed
     * 
     * @param description       String to be checked
     * @param wordToLookFor     word used for checking
     * @return                  True if word is found, else false
     * @throws Exception        If user tries searching for empty string
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
