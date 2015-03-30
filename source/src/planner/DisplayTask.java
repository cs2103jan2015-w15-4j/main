package planner;

import java.util.Date;

public class DisplayTask {
    private long ID;
    private Date displayDate;
    private Date dueDate, endDate;
    private Task parentTask;
    
    public DisplayTask(long id, Date shownDate, Date from, Date to, Task parent) {
        ID = id;
        displayDate = shownDate;
        dueDate = from;
        endDate = to;
        parentTask = parent;
    }
    
    public DisplayTask(DisplayTask anotherTask) {
        ID = anotherTask.getID();
        displayDate = anotherTask.getShownDate();
        dueDate = anotherTask.getDueDate();
        endDate = anotherTask.getEndDate();
        parentTask = anotherTask.getParent();
    }
    
    public void setID(long id) {
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
    
    public void setParent(Task parent) {
        parentTask = parent;
    }
    
    public long getID() {
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
            boolean dateCheck;
            if (dueDate == null || anotherTask.getDueDate() == null) {
                dateCheck = (dueDate == anotherTask.getDueDate());
            } else {
                dateCheck = dueDate.equals(anotherTask.getDueDate());
            }
            
            return (ID == anotherTask.getID()) &&
                   dateCheck &&
                   (parentTask.equals(anotherTask.getParent()));
            
        } else{
            
            return false;
        }
    }
}
