package planner;

import java.util.Calendar;
import java.util.Date;

public class SearchLogic {
    
    public static TaskList searchName(TaskList input, String wordToSearch) {
        TaskList searchList = new TaskList();
        try {
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i).getName().contains(wordToSearch)) {
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
                if (input.get(i).getDescription().contains(wordToSearch)) {
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
                if (input.get(i).getTag().contains(tagToLookFor)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
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
            if (!(input.get(i).isFloating()) {
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
    
    
    //Scans for words that matches parts or the whole string, not substring
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
