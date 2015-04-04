package planner;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Ke Jing
 * displayDate is used for sorting
 */
public class DisplayTask {
    private int ID;
    private Date displayDate;
    private Date dueDate, endDate;
    private Task parentTask;
    
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
    
    public void setID(int id) {
        ID = id;
    }
    
    public void setShownDate(Date shownDate) {
        displayDate = shownDate;
    }
    
    public void setDueDate(Date date) {
        dueDate = date;
    }
    
    public void setEndDate(Date date) {
        endDate = date;
    }
    
    public void setParent(Task parent) throws IllegalArgumentException {
        if (parent == null) {
            throw new IllegalArgumentException("Parent Task cannot be null!");
        } else {
            parentTask = parent;
        }
    }
    
    public int getID() {
        return this.ID;
    }
    
    public Date getShownDate() {
        return this.displayDate;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
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
           /* 
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
          */  
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
