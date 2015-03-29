package planner;

import java.util.Date;
import java.util.Calendar;

public class SplitLogic {
    
    private static long ID;
  
    public static DisplayTaskList splitAllTaskList(TaskList input) {
        
        ID = 0;
        
        DisplayTaskList outputList = new DisplayTaskList();
        
        for (int i = 0; i < input.size(); i++) {           
            addNewDisplayTask(outputList, input.get(i));
        }
        
        return outputList;
    }
    
    private static void addNewDisplayTask(DisplayTaskList outputList, Task inputTask) {
        
        if (inputTask.isFloating()) {
            
            createNewDisplayTask(outputList, null , null, inputTask);
            
        } else if (inputTask.getEndDate() == null) {
            
            createNewDisplayTask(outputList, inputTask.getDueDate(), null, inputTask);
            
        } else {
            
            Calendar dueDate = Calendar.getInstance();
            dueDate.setTime(inputTask.getDueDate());
            int dueDateDay = dueDate.get(Calendar.DATE);
            
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(inputTask.getEndDate());
            int endDateDay = endDate.get(Calendar.DATE);
            
            int DateDifference = endDateDay - dueDateDay;
            
            if (DateDifference == 0) {
                
                createNewDisplayTask(outputList, inputTask.getDueDate(), 
                        inputTask.getEndDate(), inputTask);
                
            } else {
            
                for (int j = 0; j <= DateDifference; j++) {
                
                    if (j == 0) {
                    
                        createNewDisplayTask(outputList, inputTask.getDueDate(), null, inputTask);
                    
                    } else {
                    
                        createNewDisplayTask(outputList, null, inputTask.getEndDate(), inputTask);
                    }
                    
                }
            }
        }     
    }
    
    private static void createNewDisplayTask(DisplayTaskList outputList, Date from, Date to, Task inputTask) {
        
        DisplayTask newTask = new DisplayTask(ID, from, to, inputTask);
        
        outputList.add(newTask);
        
        ID++;
    }
}
