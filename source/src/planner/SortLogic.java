package planner;

import java.util.Collections;
import java.util.Comparator;

public class SortLogic {
    
    //Comparator for sorting tasks according to due date
    //Sort in this order, Due Date > Priority > Name > ID
    private static Comparator<Task> dueDateComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            Long t1 = task1.getDueDate().getTime();
            Long t2 = task2.getDueDate().getTime();
            assert (t1 != null);    //Assertion that date cannot be null
            assert (t2 != null);
            if (t1>t2) {
                return 1;
            }
            else if (t2>t1) {
                return -1;
            }
            else {
                int priority = task1.getPriority() - task2.getPriority();
                if (priority > 0) {
                    return 1;
                }
                else if (priority < 0) {
                    return -1;
                }
                else {
                    String name1 = task1.getName();
                    String name2 = task2.getName();
                    int name = name1.compareTo(name2);
                    if (name > 0) {
                        return 1;
                    }
                    else if (name < 0) {
                        return -1;
                    }
                    else {
                        long ID1 = task1.getID();
                        long ID2 = task2.getID();
                        if (ID1 > ID2) {
                            return 1;
                        }
                        else if (ID1 < ID2) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                }
            }
        }
    };
    
    //Comparator for sorting tasks according to priority
    private static Comparator<Task> priorityComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            int result = task1.getPriority() - task2.getPriority();
            if (result > 0) {
                return 1;
            }
            else if (result < 0) {
                return -1;
            }
            else {
                Long t1 = task1.getDueDate().getTime();
                Long t2 = task2.getDueDate().getTime();
                if ((t1 == null && t2 == null) || (t1 != null && t2 != null)) {
                    String name1 = task1.getName();
                    String name2 = task2.getName();
                    int name = name1.compareTo(name2);
                    if (name > 0) {
                        return 1;
                    }
                    else if (name < 0) {
                        return -1;
                    } else {
                        long ID1 = task1.getID();
                        long ID2 = task2.getID();
                        if (ID1 > ID2) {
                            return 1;
                        }
                        else if (ID1 < ID2) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                }
                else if (t1 == null) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        }
    };
    
    public static void sortByDate(TaskList tasks) {
        Collections.sort(tasks, dueDateComparator);
    }
    
    public static void sortByPriority(TaskList tasks) {
        Collections.sort(tasks, priorityComparator);
    }
}
