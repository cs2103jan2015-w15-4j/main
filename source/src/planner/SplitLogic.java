package planner;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

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
            long dueDateDay = dueDate.getTimeInMillis();
            
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(inputTask.getEndDate());
            long endDateDay = endDate.getTimeInMillis();
            
            long DateDifference = (endDateDay - dueDateDay) / (24 * 60 * 60 * 1000);
       
            if (DateDifference == 0) {
                
                Date shownDate = getShownDate(inputTask.getDueDate());
                
                createNewDisplayTask(outputList, shownDate, inputTask.getDueDate(), 
                        inputTask.getEndDate(), inputTask);
                
            } else {
            
                Date tempDate = getShownDate(inputTask.getDueDate());
                Calendar cal = Calendar.getInstance();
                
                for (int j = 0; j <= DateDifference; j++) {
                
                    if (j == 0) {
                        createNewDisplayTask(outputList, tempDate, inputTask.getDueDate(), null, inputTask);
                        
                    } else {
                        cal.setTime(tempDate);
                        cal.add(Calendar.DATE, 1);
                        tempDate = cal.getTime();
                        
                        createNewDisplayTask(outputList, tempDate, null, inputTask.getEndDate(), inputTask);
                    }    
                }
                
                cal.setTime(tempDate);
                cal.add(Calendar.DATE, 1);
                tempDate = cal.getTime();
                int finalDate = cal.get(Calendar.DATE);
                cal.setTime(inputTask.getEndDate());
                int endTime = cal.get(Calendar.DATE);

                if (finalDate == endTime) {
                    createNewDisplayTask(outputList, tempDate, null, inputTask.getEndDate(), inputTask);
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
        
        cal.set(year, month, date, 0, 0, 0);
        
        return cal.getTime();
    }
}
