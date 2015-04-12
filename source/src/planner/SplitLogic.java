//@author A0110797B

package planner;

import java.util.Date;
import java.util.Calendar;

/**
 * SPLITLOGIC CLASS SPLITS TASKLIST INTO DISPLAYTASKLIST FORMAT
 */
public class SplitLogic {
    
    private static int ID;
    private static int INCREMENT_DATE_BY_ONE = 1;
    
    public static DisplayTaskList splitAllTaskList(TaskList input) {
        ID = 0;
        
        DisplayTaskList outputList = new DisplayTaskList();
        
        for (int i = 0; i < input.size(); i++) {           
            addNewDisplayTask(outputList, input.get(i));
        }   
        return outputList;
    }
    
    private static void addNewDisplayTask(DisplayTaskList outputList, 
                                          Task inputTask) {
        if (!inputTask.isDone()) {
            if (inputTask.isFloating()) {
                createNewDisplayTask(outputList, null, null, null, inputTask);
                
            } else if (inputTask.getEndDate() == null) {  
                Date shownDate = getShownDate(inputTask.getDueDate());
                
                createNewDisplayTask(outputList, shownDate, 
                                     inputTask.getDueDate(), null, inputTask);
            } else {
                long DateDifference = calculateDifferenceInDate(inputTask);
                
                if (DateDifference == 0) {
                    Date shownDate = getShownDate(inputTask.getDueDate());
                    
                    createNewDisplayTask(outputList, shownDate, 
                                         inputTask.getDueDate(), 
                                         inputTask.getEndDate(), inputTask);
                } else {
                
                    Date tempDate = getShownDate(inputTask.getDueDate());
                    Calendar cal = Calendar.getInstance();
                    
                    for (int j = 0; j <= DateDifference; j++) {
                        if (j == 0) {
                            createNewDisplayTask(outputList, tempDate, 
                                                 inputTask.getDueDate(), null, 
                                                 inputTask);       
                        } else {
                            cal.setTime(tempDate);
                            cal.add(Calendar.DATE, INCREMENT_DATE_BY_ONE);
                            tempDate = cal.getTime();
                            
                            createNewDisplayTask(outputList, tempDate, null, 
                                                 inputTask.getEndDate(), 
                                                 inputTask);
                        }    
                    } 
                    createLastDisplayTaskIfNeeded(outputList, 
                                                  inputTask, tempDate);
                }
            }  
        } else {
            Date doneDate = getShownDate(inputTask.getDateCompleted());
            
            createNewDisplayTask(outputList, doneDate, null, 
                                 inputTask.getDateCompleted(), inputTask);
        }
    }
    
    private static void createNewDisplayTask(DisplayTaskList outputList, 
                                             Date shownDate, Date from, 
                                             Date to, Task inputTask) {
        
        DisplayTask newTask = new DisplayTask(ID, shownDate, from, to, inputTask);
        
        outputList.add(newTask);
        
        ID++;
    }
    
    private static long calculateDifferenceInDate(Task inputTask) {
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(inputTask.getDueDate());
        long dueDateDay = dueDate.getTimeInMillis();
        
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(inputTask.getEndDate());
        long endDateDay = endDate.getTimeInMillis();
        
        long DateDifference = (endDateDay - dueDateDay) / (24 * 60 * 60 * 1000);
        return DateDifference;
    }
    
    private static void createLastDisplayTaskIfNeeded(
                                            DisplayTaskList outputList, 
                                            Task inputTask, Date tempDate) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(tempDate);
        cal.add(Calendar.DATE, INCREMENT_DATE_BY_ONE);
        tempDate = cal.getTime();
        int finalDate = cal.get(Calendar.DATE);
        cal.setTime(inputTask.getEndDate());
        int endTime = cal.get(Calendar.DATE);

        if (finalDate == endTime) {
            createNewDisplayTask(outputList, tempDate, null, 
                                 inputTask.getEndDate(), inputTask);
        }
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
    
    public static DisplayTaskList splitAllPriorityTask(TaskList input) {      
        ID = 0;
        
        DisplayTaskList outputList = new DisplayTaskList();
        
        for (int i = 0; i < input.size(); i++) {
            Task temp = input.get(i);
            createNewDisplayTask(outputList, null, temp.getDueDate(), 
                                 temp.getEndDate(), input.get(i));
        }      
        return outputList;
    }
   
}
