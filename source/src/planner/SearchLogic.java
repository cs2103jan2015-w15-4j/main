package planner;

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
