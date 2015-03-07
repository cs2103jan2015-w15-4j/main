package planner;

public class Logic {
    
    //Sorts according to due date
    //Assumption: TaskList passed to this method MUST have a non-null due date (Non-tentative tasks).
    public static void sortTaskListByDate(TaskList tasks){
        SortLogic.sortByDate(tasks);
    }
    
    //Sorts according to priority
    public static void sortTaskListByPriority(TaskList tasks){
        SortLogic.sortByPriority(tasks);
    }

    //Copies confirmed or tentative tasks into their respective TaskLists
    public static void splitTasksByTentative(TaskList input, TaskList confirmed, TaskList tentative) {
        SplitLogic.splitByTentative(input, confirmed, tentative);
    }
    
    //Copies done and incomplete tasks into their respective TaskLists
    public static void splitTaskByDone(TaskList input, TaskList done, TaskList notDone) {
        SplitLogic.splitByDone(input, done, notDone);
    }
    
    //Copies searched results into the searchList 
    public static void searchTaskByTags(TaskList input, TaskList searchList, String tagToLookFor) {
        SearchLogic.searchByTags(input, searchList, tagToLookFor);
    }
    

}