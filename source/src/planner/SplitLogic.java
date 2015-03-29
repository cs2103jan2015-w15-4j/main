package planner;

import java.util.Date;
import java.util.Calendar;

public class SplitLogic {
    private static long ID;
    private static final int MILLISECONDS = 1000;
    private static final int SECONDS = 60;
    private static final int MINUTES = 60;
    private static final int HOURS = 24;
    
    
    
    public static DisplayTaskList splitTaskListToBeDisplayed(TaskList input) {
        ID = 0;
        DisplayTaskList outputList = new DisplayTaskList();
        for (int i = 0; i < input.size(); i++) {
            addNewDisplayTask(outputList, input.get(i));
        }
        return outputList;
    }
    
    public static void addNewDisplayTask(DisplayTaskList outputList, Task inputTask) {
        if (inputTask.isFloating()) {
            DisplayTask newTask = new DisplayTask(ID, null, null, inputTask);
            outputList.add(newTask);
            ID++;
        } else if (inputTask.getEndDate() == null) {
            DisplayTask newTask = new DisplayTask(ID, inputTask.getDueDate(), null, inputTask);
            outputList.add(newTask);
            ID++;
        } else {
            int dayDifference = (int) (inputTask.getEndDate().getTime() - 
                    inputTask.getDueDate().getTime() / 
                    (MILLISECONDS * SECONDS * MINUTES * HOURS));
            Calendar dueDate = Calendar.getInstance();
            int dueDateDay = dueDate.get(Calendar.DATE);
            Calendar endDate = Calendar.getInstance();
            int endDateDay = endDate.get(Calendar.DATE);
            
            if ((dueDateDay != endDateDay) && (dayDifference == 0)) {
                
            }
        }
        
    }
}
