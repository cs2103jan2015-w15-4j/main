//@author A0110797B

package planner;

import java.util.Date;
import java.util.Calendar;

/**
 * A class used to convert TaskList into DisplayTaskList
 */
public class SplitLogic {
    
    private static int ID;
    private static final int INITIALIZE_ID = 0;
    private static final int INCREMENT_DATE_BY_ONE = 1;
    private static final int ZERO_HOUR = 0;
    private static final int ZERO_MINUTE = 0;
    private static final int ZERO_SECOND = 0;
    private static final int SECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    
    /**
     * Splits the input tasklist into an output displayTaskList
     * This method will split tasks that are more than 1 day into multiple
     * displayTasks each representing 1 day
     * 
     * @param input     Input tasklist to be split and converted
     * @return          Output displayTaskList that is properly split
     */
    public static DisplayTaskList splitAllTaskList(TaskList input) {
        ID = INITIALIZE_ID;
        
        DisplayTaskList outputList = new DisplayTaskList();
        
        for (int i = 0; i < input.size(); i++) {           
            addNewDisplayTask(outputList, input.get(i));
        }   
        return outputList;
    }
 
    /**
     * Another method used to split and convert every task into 1 respective displayTask
     * Used by engine to show Today date
     * 
     * @param input         Input tasklist 
     * @return              The converted displayTaskList
     */
    public static DisplayTaskList splitAllPriorityTask(TaskList input) {      
        ID = INITIALIZE_ID;
        
        DisplayTaskList outputList = new DisplayTaskList();
        
        for (int i = 0; i < input.size(); i++) {
            Task temp = input.get(i);
            createNewDisplayTask(outputList, null, temp.getDueDate(), 
                                 temp.getEndDate(), input.get(i));
        }      
        return outputList;
    }
    
    /**
     * Used only by splitAllTaskList method
     * Creates and adds a newly converted displayTask into the output 
     * displayTaskList
     * 
     * Done tasks are converted into 1 displayTask containing date completed
     * Floating tasks are converted into 1 displayTask containing null dates
     * Due date tasks are converted into 1 displayTask containing its single
     * due date
     * Timed tasks can be converted into 1 or more displayTasks depending 
     * on its time span
     * 
     * @param outputList        Output displayTaskList containing all 
     *                          displayTask
     * @param inputTask         Input task to be checked by its type before 
     *                          creating the necessary amount of displayTasks
     */
    private static void addNewDisplayTask(DisplayTaskList outputList, 
                                          Task inputTask) {
        if (!inputTask.isDone()) {
            if (inputTask.isFloating()) {
                createNewDisplayTask(outputList, null, null, null, inputTask);
                
            } else if (inputTask.getEndDate() == null) {  
                assert(inputTask.getDueDate() != null);
                
                Date shownDate = getShownDate(inputTask.getDueDate());
                
                createNewDisplayTask(outputList, shownDate, 
                                     inputTask.getDueDate(), null, inputTask);
            } else {
                assert(inputTask.getEndDate() != null);
                assert(inputTask.getDueDate() != null);
                
                long DateDifference = calculateDifferenceInDate(inputTask);
                
                // Creates a single displayTask if the task is all 
                // within a single day
                if (DateDifference == 0) {
                    Date shownDate = getShownDate(inputTask.getDueDate());
                    
                    createNewDisplayTask(outputList, shownDate, 
                                         inputTask.getDueDate(), 
                                         inputTask.getEndDate(), inputTask);
                } else {
                    // Otherwise display tasks will have to be created for 
                    // every single day till difference in days is 0
                    
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
                    // There might be a case where date difference is X.X
                    // where int rounds it down to X.0
                    // This method captures such cases and creates the final
                    // displaytask on the final date
                    createLastDisplayTaskIfNeeded(outputList, 
                                                  inputTask, tempDate);
                }
            }  
        } else {
            assert(inputTask.getDateCompleted() != null);
            
            Date doneDate = getShownDate(inputTask.getDateCompleted());
            
            createNewDisplayTask(outputList, doneDate, null, 
                                 inputTask.getDateCompleted(), inputTask);
        }
    }
    
    /**
     * Creates and add a new displayTask into the output displayTaskList 
     * 
     * @param outputList        Output displayTaskList for displayTasks to be
     *                          added into
     * @param shownDate         The date that is used by Sort logic to 
     *                          insert into a TreeMap of date keys
     * @param from              Time of the start of task
     * @param to                Time of the end of task
     * @param inputTask         The parent task that will be directed to when 
     *                          more information such as ID needs to be found
     */
    private static void createNewDisplayTask(DisplayTaskList outputList, 
                                             Date shownDate, Date from, 
                                             Date to, Task inputTask) {
        assert(inputTask != null);
        
        DisplayTask newTask = new DisplayTask(ID, shownDate, from, to, inputTask);
        
        outputList.add(newTask);
        
        ID++;
    }
    
    /**
     * Calculates the difference in the number of days so that multiple days
     * can be created for timed task splitting
     * 
     * @param inputTask     Input timed task
     * @return              The time span of the timed task
     */
    private static long calculateDifferenceInDate(Task inputTask) {
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(inputTask.getDueDate());
        long dueDateDay = dueDate.getTimeInMillis();
        
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(inputTask.getEndDate());
        long endDateDay = endDate.getTimeInMillis();
        
        assert(endDateDay >= dueDateDay);
        long DateDifference = (endDateDay - dueDateDay) / SECONDS_PER_DAY;
        return DateDifference;
    }
    
    /**
     * Determines whether the final date has been reached, if not create the 
     * final displayTask
     * Used to catch cases where difference in days are rounded down and the 
     * final day is left out
     * 
     * @param outputList        Output displayTaskList
     * @param inputTask         Input tasklist 
     * @param tempDate          Date that is used to check if the final date
     *                          has been reached
     */
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
     * 
     * @param day       Day that is referenced to create the displayDate 
     * @return          The displayDate
     */
    private static Date getShownDate(Date day) {  
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(day);
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        
        cal.set(year, month, date, ZERO_HOUR, ZERO_MINUTE, ZERO_SECOND);
        
        return cal.getTime();
    } 
}
