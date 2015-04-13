//@author A0110797B

package planner;

import java.util.Date;

/**
 * 
 * displayDate is used for sorting
 */
public class DisplayTask {
    private int ID;
    private Date displayDate;
    private Date dueDate, endDate;
    private Task parentTask;
    
    /**
     * Constructor for DisplayTask
     * 
     * @param id            Display ID of DisplayTask
     * @param shownDate     Display date of DisplayTask
     * @param from          Start date of DisplayTask
     * @param to            End date of DisplayTask
     * @param parent        Parent Task of DisplayTask, this cannot be null
     * @throws IllegalArgumentException
     */
    public DisplayTask(int id, Date shownDate, Date from, Date to, Task parent) throws IllegalArgumentException {
        ID = id;
        displayDate = shownDate;
        dueDate = from;
        endDate = to;
        if (parent == null) {
            throw new IllegalArgumentException("Parent Task cannot be null!");
        } else {
            parentTask = parent;
        }
    }
    
    /**
     * Generates a copy of another DisplayTask
     * 
     * @param anotherTask       Another DisplayTask used as reference for copy
     * @throws IllegalArgumentException     Thrown when parent task is null
     */
    public DisplayTask(DisplayTask anotherTask) throws IllegalArgumentException {
        ID = anotherTask.getID();
        displayDate = anotherTask.getShownDate();
        dueDate = anotherTask.getDueDate();
        endDate = anotherTask.getEndDate();
        if (anotherTask.getParent() == null) {
            throw new IllegalArgumentException("Parent Task cannot be null!");
        } else {
            parentTask = anotherTask.getParent();
        }
    }
    
    /**
     * Set new DisplayTask ID
     * 
     * @param id    New ID of DisplayTask
     */
    public void setID(int id) {
        ID = id;
    }
    
    /**
     * Set new Display date 
     * @param shownDate     New Display date
     */
    public void setShownDate(Date shownDate) {
        displayDate = shownDate;
    }
    
    /**
     * Set new Due date
     * 
     * @param date      New Due date
     */
    public void setDueDate(Date date) {
        dueDate = date;
    }
    
    /**
     * Set new End date
     * 
     * @param date      New End date
     */
    public void setEndDate(Date date) {
        endDate = date;
    }
    
    /**
     * Link parent Task
     * 
     * @param parent   Task that will be linked 
     * @throws IllegalArgumentException     Thrown when Task is null
     */
    public void setParent(Task parent) throws IllegalArgumentException {
        if (parent == null) {
            throw new IllegalArgumentException("Parent Task cannot be null!");
        } else {
            parentTask = parent;
        }
    }
    
    /**
     * @return      DisplayTask ID
     */
    public int getID() {
        return this.ID;
    }
    
    /**
     * @return      DisplayTask display date
     */
    public Date getShownDate() {
        return this.displayDate;
    }
    
    /**
     * @return      DisplayTask due date
     */
    public Date getDueDate() {
        return this.dueDate;
    }
    
    /**
     * @return      DisplayTask end date
     */
    public Date getEndDate() {
        return this.endDate;
    }
    
    /**
     * @return      DisplayTask parent Task
     */
    public Task getParent() {
        return this.parentTask;
    }
    
    @Override
    public boolean equals(Object obj) {
        if( obj instanceof DisplayTask ){
            
            DisplayTask anotherTask = (DisplayTask)obj;
            
            boolean dueDateCheck, endDateCheck, displayDateCheck;
            
            if (dueDate == null || anotherTask.getDueDate() == null) {
                dueDateCheck = (dueDate == anotherTask.getDueDate());       
            } else {               
                dueDateCheck = dueDate.equals(anotherTask.getDueDate());
            }
            
            if (endDate == null || anotherTask.getEndDate() == null) {   
                endDateCheck = (endDate == anotherTask.getEndDate());    
            } else {  
                endDateCheck = endDate.equals(anotherTask.getEndDate());
            }
            
            if (displayDate == null || anotherTask.getShownDate() == null) {
                displayDateCheck = (displayDate == anotherTask.getShownDate());
            } else {
                
                displayDateCheck = displayDate.equals(anotherTask.getShownDate());
            }
            
            if (dueDateCheck == false) {
                System.out.println("false dueDatecheck");
            }
            if (endDateCheck == false) {
                System.out.println("false endDatecheck");
            }
            if (displayDateCheck == false) {
                System.out.println("false displayDatecheck");
                System.out.println(displayDate + " " + anotherTask.getShownDate());
            }
            
            return ((ID == anotherTask.getID()) &&
                   dueDateCheck &&
                   endDateCheck &&
                   displayDateCheck &&
                   (parentTask.equals(anotherTask.getParent())));
            
        } else{
            
            return false;
        }
    }
}
