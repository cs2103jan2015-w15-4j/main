package planner;

import java.util.Date;

/**
 * 
 * @author kohwaikit
 *
 */
public class Task {
	
	private final int ID;
	private String taskName, taskDescription, taskTag;
	private Date dateCreated, dateDue, dateEnd, dateCompleted;
	private int taskPriority;
	private boolean taskCompleted, isFloatingTask, isTimedTask;
	
	/**
	* This method is the Task constructor
	* @param name
	* Name of the task. This cannot be an empty string. Throws IllegalArgumentException when invalid.
	* @param description
	* Description of task.
	* @param dueDate
	* Due date of task. This is represented by the Date class.
	* @param priority
	* Priority of task defined by an integer.
	* @param tag
	* A string tag on a task.
	* @throws IllegalArgumentException
	*/
	public Task(String name, String description, Date dueDate, int priority, String tag, int id) throws IllegalArgumentException {
		
	    ID = id;
        
	    if (name == null) {
            throw new IllegalArgumentException("Task name cannot be null!");
        } else if (name.equals("")) {
            throw new IllegalArgumentException("Task name cannot be empty!");
        } else {
            taskName = name;
        }
        
        taskDescription = description;
        taskTag = tag;
        dateCreated = new Date(System.currentTimeMillis());
        
        //Tests needed for null date
        if(dueDate != null) {
            
            dateDue = new Date(dueDate.getTime());     // Changed to defensive copy
            isFloatingTask = false;
            
        } else {
            
            dateDue = null;
            isFloatingTask = true;
        }
		
		taskPriority = priority;
		taskCompleted = false;
		isTimedTask = false;
	}
	
    public Task( Task anotherTask ) throws IllegalArgumentException {
        
        if( anotherTask != null ){
            ID = anotherTask.getID();
            
            if(anotherTask.getName().equals("") || anotherTask.getName() == null) {
                throw new IllegalArgumentException("Task name cannot be empty!");
            }
            
            taskName = anotherTask.getName();
            taskDescription  = anotherTask.getDescription();
            taskTag = anotherTask.getTag();
            if(anotherTask.getCreatedDate() != null) {
                dateCreated = new Date(anotherTask.getCreatedDate().getTime());
            }
            if(anotherTask.getDueDate() != null) {
                dateDue = new Date(anotherTask.getDueDate().getTime());
            }
            if(anotherTask.getEndDate() != null) {
                dateEnd = new Date(anotherTask.getEndDate().getTime()); 
            }
            if(anotherTask.getDateCompleted() != null) {
                dateCompleted = new Date(anotherTask.getDateCompleted().getTime());
            }
            taskPriority = anotherTask.getPriority();
            taskCompleted = anotherTask.isDone();
            isFloatingTask = anotherTask.isFloating();
            isTimedTask = anotherTask.isTimed();
            
        } else {
            
            ID = 0;
            taskName = "Insert task Name here";
            taskDescription = "Insert task description here";
            taskTag = "";
            dateCreated = new Date( System.currentTimeMillis() );
            dateDue = null;
            isFloatingTask = true;
            taskPriority = 0;
            taskCompleted = false;
        }
    }
	
	public String getName() {
		return taskName;
	}
	
	public String getDescription() {
		return taskDescription;
	}
	
	public String getTag() {
		return taskTag;
	}
	
	public int getPriority() {
		return taskPriority;
	}
	
	public Date getDueDate() {
		return dateDue;
	}
	
	public Date getStartDate() {
	    
	    return dateDue;

	}
	
	public Date getEndDate() {
	    return dateEnd;
	}
	
	public Date getCreatedDate() {
		return dateCreated;
	}
	
	//Not tested yet
	public int getID() {
		return ID;
	}
	
	//Not covered by tests yet
	public boolean isDone() {
		
		return taskCompleted;
	}
	
	//Not covered by tests yet
	public boolean isFloating() {
		
		return isFloatingTask;
	}
	
	public boolean isTimed() {
	    
	    return isTimedTask;
	    
	}
	
	public void setName(String newName) throws IllegalArgumentException {
		
	    if (newName == null) {
            throw new IllegalArgumentException("Task name cannot be null!");
        } else if (newName.equals("")) {
			throw new IllegalArgumentException("Task name cannot be empty!");
		} else {
		    taskName = newName;
		}
	}
	//Not covered by tests yet
	public void setDescription(String newDescription) {
	
		taskDescription = newDescription;
	}
	//Not covered by tests yet
	public void setTag(String newTag) {
		
		taskTag = newTag;
	}
	//Not covered by tests yet
	public void setPriority(int newPriority) {
		
		taskPriority = newPriority;
	}
	//Not covered by tests yet
	public void setDueDate(Date newDueDate) {
		
	//Tests needed for null date
		if(newDueDate != null) {
			dateDue = newDueDate;
			isFloatingTask = false;
			
		} else {
			dateDue = null;
			isFloatingTask = true;
		}
	}
	
	public void setStartDate(Date newStartDate) {

	    //Tests needed for null date
	    if(newStartDate != null) {
	        dateDue = newStartDate;
	        isFloatingTask = false;

	    } else {
	        dateDue = null;
	        isFloatingTask = true;
	    }
	}
	
	public void setEndDate(Date newEndDate) {
	    if(newEndDate != null) {
            dateEnd = newEndDate;
            isTimedTask = true;
            
        } else {
            dateEnd = null;
            isTimedTask = false;
        }
	}
	
	//Not covered by tests yet
	public void configureCreatedDate(Date newCreatedDate) {
		
		//Tests needed for null date
		dateCreated = newCreatedDate;
	}
	
	//Not covered by tests yet
	public void setDone() {
		
		taskCompleted = true;
		dateCompleted = new Date(System.currentTimeMillis());
		
	}
	//Not covered by tests yet
	public void setUndone() {
		
		taskCompleted = false;
		dateCompleted = null;
		
	}
	
	//Not covered by tests yet
	public Date getDateCompleted() {
	    
	    return dateCompleted;
	}
	

	public void setDateCompleted(Date date) {
	    dateCompleted = date;
	}

	@Override
	public boolean equals( Object obj ){
		
		if( obj instanceof Task ){
			
			Task anotherTask = (Task)obj;
			boolean dateCheck;
			if (dateDue == null || anotherTask.getDueDate() == null) {
			    dateCheck = (dateDue == anotherTask.getDueDate());
			} else {
			    dateCheck = dateDue.equals(anotherTask.getDueDate());
			}
			
			return (ID == anotherTask.getID()) &&
				   taskName.equals( anotherTask.getName() ) &&
				   taskDescription.equals( anotherTask.getDescription() ) &&
				   taskTag.equals( anotherTask.getTag() ) &&
				   dateCreated.equals(anotherTask.getCreatedDate()) &&
				   dateCheck &&
				   (taskPriority == anotherTask.getPriority()) &&
				   (taskCompleted == anotherTask.isDone()) &&
				   (isFloatingTask == anotherTask.isFloating());
			
		} else{
			
			return false;
		}
	}
}
