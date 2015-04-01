package planner;

import java.util.Date;
import java.util.Calendar;

/**
 * @author Ke Jing
 * SPLITLOGIC CLASS SPLITS TASKLIST INTO DISPLAYTASKLIST FORMAT
 */
public class SplitLogic {
    
    private static int ID;
    
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
            
            createNewDisplayTask(outputList, null, null, null, inputTask);
            
        } else if (inputTask.getEndDate() == null) {
            
            Date shownDate = getShownDate(inputTask.getDueDate());
            
            createNewDisplayTask(outputList, shownDate, inputTask.getDueDate(), null, inputTask);
            
        } else {
            
            Calendar dueDate = Calendar.getInstance();
            dueDate.setTime(inputTask.getDueDate());
            int dueDateDay = dueDate.get(Calendar.DATE);
            
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(inputTask.getEndDate());
            int endDateDay = endDate.get(Calendar.DATE);
            
            int DateDifference = endDateDay - dueDateDay;
            
            if (DateDifference == 0) {
                
                Date shownDate = getShownDate(inputTask.getDueDate());
                
                createNewDisplayTask(outputList, shownDate, inputTask.getDueDate(), 
                        inputTask.getEndDate(), inputTask);
                
            } else {
            
                for (int j = 0; j <= DateDifference; j++) {
                    
                    Date tempDate = null;
                
                    if (j == 0) {
                        
                        tempDate = getShownDate(inputTask.getDueDate());
                    
                        createNewDisplayTask(outputList, tempDate, inputTask.getDueDate(), null, inputTask);
                    
                    } else {
                        
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(tempDate);
                        cal.add(Calendar.DATE, 1);
                        tempDate = cal.getTime();
                        
                        createNewDisplayTask(outputList, tempDate, null, inputTask.getEndDate(), inputTask);
                    }
                    
                }
            }
        }     
    }
    
    private static void createNewDisplayTask(DisplayTaskList outputList,Date shownDate, Date from, Date to, Task inputTask) {
        
        DisplayTask newTask = new DisplayTask(ID, shownDate, from, to, inputTask);
        
        outputList.add(newTask);
        
        ID++;
    }
    
    /**
     * Converts specific time into a date that contains only day, month and year
     * Purpose: Used as keys to sort them into TreeMap<Date, DisplayTaskList>
     */
    private static Date getShownDate(Date day) {
        
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(day);
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        
        cal.set(year, month, date);
        
        return cal.getTime();
    }
    
}
