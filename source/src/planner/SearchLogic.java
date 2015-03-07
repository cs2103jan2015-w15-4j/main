package planner;

import java.util.StringTokenizer;

public class SearchLogic {
    
    public static void searchByTags(TaskList input, TaskList searchList, String tagToLookFor){
        for (int i = 0; i < input.size(); i++) {
            if (containsSearchedWord(input.get(i).getName(), tagToLookFor)) {
                searchList.add(input.get(i));
            }
        }
    }
    
    //Scans for words that matches parts or the whole string, not substring
    private static boolean containsSearchedWord (String description, String wordToLookFor) {
        StringTokenizer token = new StringTokenizer(description);
        while (token.hasMoreTokens()) {
            String word = token.nextToken();
            if (word.equals(wordToLookFor.trim())) {
                return true;
            }
        }
        return false;
    }
}
