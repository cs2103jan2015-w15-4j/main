package planner;

import java.util.Date;

public class Task {
	
	private final long ID;
	private String taskName, taskDescription, taskTag;
	private Date dateCreated, dateDue, dateEnd;
	private int taskPriority;
	private boolean taskCompleted, taskFloating, isTimedTask;
	
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
	public Task(String name, String description, Date dueDate, int priority, String tag, long id) throws IllegalArgumentException {
		
		ID = id;
		
		if(name.equals("")) {
			
			throw new IllegalArgumentException("Task name cannot be empty!");
		}
		
		taskName = name;
		taskDescription = description;
		taskTag = tag;
		dateCreated = new Date(System.currentTimeMillis());
		
		//Tests needed for null date
		if(dueDate != null) {
			dateDue = dueDate;
			taskFloating = false;
			
		} else {
			
			dateDue = null;
			taskFloating = true;
		}
		
		taskPriority = priority;
		taskCompleted = false;
		isTimedTask = false;
	}
	
	public Task(String name, String description, Date startDate, Date endDate, int priority, String tag, long id) throws IllegalArgumentException {
        
        ID = id;
        
        if(name.equals("")) {
            
            throw new IllegalArgumentException("Task name cannot be empty!");
        }
        
        taskName = name;
        taskDescription = description;
        taskTag = tag;
        dateCreated = new Date(System.currentTimeMillis());
        
        //Tests needed for null date
        dateDue = startDate;
        taskFloating = false;
        dateEnd = endDate;
        
        taskPriority = priority;
        taskCompleted = false;
        isTimedTask = true;
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
	
	public Date getCreatedDate() {
		return dateCreated;
	}
	
	//Not tested yet
	public long getID() {
		return ID;
	}
	
	//Not covered by tests yet
	public boolean isDone() {
		
		return taskCompleted;
	}
	
	//Not covered by tests yet
	public boolean isFloating() {
		
		return taskFloating;
	}
	
	public void setName(String newName) throws IllegalArgumentException {
		
		if(newName.equals("")) {
			throw new IllegalArgumentException("Task name cannot be empty!");
		}
		
		taskName = newName;
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
			taskFloating = false;
			
		} else {
			dateDue = null;
			taskFloating = true;
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
	}
	//Not covered by tests yet
	public void setUndone() {
		
		taskCompleted = false;
	}
	
	@Override
	public boolean equals( Object obj ){
		
		if( obj instanceof Task ){
			
			Task anotherTask = (Task)obj;
			
			return (ID == anotherTask.getID()) &&
				   taskName.equals( anotherTask.getName() ) &&
				   taskDescription.equals( anotherTask.getDescription() ) &&
				   taskTag.equals( anotherTask.getTag() ) &&
				   dateCreated.equals(anotherTask.getCreatedDate()) &&
				   dateDue.equals( anotherTask.getDueDate() ) &&
				   (taskPriority == anotherTask.getPriority()) &&
				   (taskCompleted == anotherTask.isDone()) &&
				   (taskFloating == anotherTask.isFloating());
			
		} else{
			
			return false;
		}
	}
}
