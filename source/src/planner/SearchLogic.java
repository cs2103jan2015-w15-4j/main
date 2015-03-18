package planner;

import java.util.StringTokenizer;

public class SearchLogic {
    
    public static void searchAll(TaskList input, TaskList searchList, String wordToLookFor) {
        try {
            for (int i = 0; i < input.size(); i++) {
                if (containsSearchedWord(input.get(i).getName(), wordToLookFor)) {
                    searchList.add(input.get(i));
                }
                else if (containsSearchedWord(input.get(i).getDescription(), wordToLookFor)) {
                    searchList.add(input.get(i));
                }
                else if (containsSearchedWord(input.get(i).getTag(), wordToLookFor)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
    
    public static void searchByTags(TaskList input, TaskList searchList, String tagToLookFor){
        try {
            for (int i = 0; i < input.size(); i++) {
                if (containsSearchedWord(input.get(i).getTag(), tagToLookFor)) {
                    searchList.add(input.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
    
    //Scans for words that matches parts or the whole string, not substring
    private static boolean containsSearchedWord (String description, String wordToLookFor) throws Exception{
        if (wordToLookFor.equals("")) {
            throw new Exception("Cannot search for empty string");
        }
        StringTokenizer token = new StringTokenizer(description);
        while (token.hasMoreTokens()) {
            String word = token.nextToken();
            if (word.toUpperCase().equals(wordToLookFor.toUpperCase().trim())) {
                return true;
            }
        }
        return false;
    }
}
