package planner;

import java.util.Collections;
import java.util.Comparator;

public class SortLogic {
    
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
    
    public static void sortByDate(TaskList tasks) {
        Collections.sort(tasks, dueDateComparator);
    }
    
    public static void sortByPriority(TaskList tasks) {
        Collections.sort(tasks, priorityComparator);
    }
}
