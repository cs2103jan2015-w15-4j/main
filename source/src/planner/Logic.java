package planner;

import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Logic {
    
    //Comparator for sorting tasks according to due date
    private static Comparator<Task> dueDateComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            long t1 = task1.getDueDate().getTime();
            long t2 = task2.getDueDate().getTime();
            if (t2>t1) {
                return 1;
            }
            else if (t1>t2) {
                return -1;
            }
            else {
                return 0;
            }
        }
    };
    
    //Comparator for sorting tasks according to priority
    private static Comparator<Task> priorityComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            int t1 = task1.getPriority();
            int t2 = task2.getPriority();
            return t1 - t2;
        }
    };
    
    //Sorts according to due date
    //Assumption: TaskList passed to this method MUST have a non-null due date (Non-tentative tasks).
    private static void sortTaskListByDate(TaskList tasks){
        Collections.sort(tasks, dueDateComparator);
    }
    
    //Sorts according to priority
    private static void sortTaskListByPriority(TaskList tasks){
        Collections.sort(tasks, priorityComparator);
    }

    //Copies confirmed or tentative tasks into their respective TaskLists
    private static void splitTasksByTentative(TaskList input, TaskList confirmed, TaskList tentative) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isFloating()) {
                tentative.add(input.get(i));
            }
            else {
                confirmed.add(input.get(i));
            }
        }
    }
    
    //Copies done and incomplete tasks into their respective TaskLists
    private static void splitTaskByDone(TaskList input, TaskList done, TaskList notDone) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isDone()) {
                done.add(input.get(i));
            }
            else {
                notDone.add(input.get(i));
            }
        }
    }
    
    //Copies searched results into the searchList 
    private static void searchTaskByTags(TaskList input, TaskList searchList, String tagToLookFor) {
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
