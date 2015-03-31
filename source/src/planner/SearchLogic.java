package planner;

import java.util.Calendar;
import java.util.Date;

public class SearchLogic {
    
    public static DisplayTaskList searchName(DisplayTaskList input, String wordToSearch) {
        DisplayTaskList searchList = new DisplayTaskList();
        try {
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i).getParent().getName().contains(wordToSearch)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
        return searchList;
    }
    
    public static DisplayTaskList searchDesc(DisplayTaskList input, String wordToSearch) {
        DisplayTaskList searchList = new DisplayTaskList();
        try {
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i).getParent().getDescription().contains(wordToSearch)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
        return searchList;
    }
    
    public static DisplayTaskList searchTags(DisplayTaskList input, String tagToLookFor){
        DisplayTaskList searchList = new DisplayTaskList();
        try { 
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i).getParent().getTag().contains(tagToLookFor)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
        return searchList;
    }
    
    public static TaskList searchPeriod(TaskList input, Date start, Date end) {
        TaskList searchList = new TaskList();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).getDueDate().compareTo(start) > 0 && 
                    end.compareTo(input.get(i).getDueDate()) > 0) {
                searchList.add(input.get(i));
            }
        }
        return searchList;
    }
    
    public static DisplayTaskList searchOutDated (DisplayTaskList input) {
        Calendar now = Calendar.getInstance();
        //WORK ON THIS LATER
    }
    
    public static DisplayTaskList searchFloating(DisplayTaskList input) {
        DisplayTaskList searchList = new DisplayTaskList();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).getParent().isFloating()) {
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
