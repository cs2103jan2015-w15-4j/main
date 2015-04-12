//@author A0114156N

package planner;

import java.util.Date;

/**
 * This class is the main entity for representing Tasks in the planner
 */
public class Task {

    private final int ID;
    private String taskName, taskDescription, taskTag;
    private Date dateCreated, dateDue, dateEnd, dateCompleted;
    private int taskPriority;
    private boolean taskCompleted, isFloatingTask, isTimedTask;

    /**
     * This method is the Task constructor
     * 
     * @param name
     *            Name of the task. This cannot be an empty string.
     * @param description
     *            Description of task.
     * @param dueDate
     *            Due date of task. This is represented by the Date class.
     * @param priority
     *            Priority of task defined by an integer.
     * @param tag
     *            A string tag on a task.
     * @throws IllegalArgumentException
     */
    public Task(String name, String description, Date dueDate, int priority,
            String tag, int id) throws IllegalArgumentException {

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

        // Tests needed for null date
        if (dueDate != null) {

            dateDue = new Date(dueDate.getTime()); // Changed to defensive copy
            isFloatingTask = false;

        } else {

            dateDue = null;
            isFloatingTask = true;
        }

        taskPriority = priority;
        taskCompleted = false;
        isTimedTask = false;
    }

    /**
     * Generates a deep copy given a Task object
     * 
     * @param anotherTask
     * @throws IllegalArgumentException
     */

    public Task(Task anotherTask) throws IllegalArgumentException {

        if (anotherTask != null) {
            ID = anotherTask.getID();

            if (anotherTask.getName().equals("")
                    || anotherTask.getName() == null) {
                throw new IllegalArgumentException("Task name cannot be empty!");
            }

            taskName = anotherTask.getName();
            taskDescription = anotherTask.getDescription();
            taskTag = anotherTask.getTag();
            if (anotherTask.getCreatedDate() != null) {
                dateCreated = new Date(anotherTask.getCreatedDate().getTime());
            }
            if (anotherTask.getDueDate() != null) {
                dateDue = new Date(anotherTask.getDueDate().getTime());
            }
            if (anotherTask.getEndDate() != null) {
                dateEnd = new Date(anotherTask.getEndDate().getTime());
            }
            if (anotherTask.getDateCompleted() != null) {
                dateCompleted = new Date(anotherTask.getDateCompleted()
                        .getTime());
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
            dateCreated = new Date(System.currentTimeMillis());
            dateDue = null;
            isFloatingTask = true;
            taskPriority = 0;
            taskCompleted = false;
        }
    }

    /**
     * Returns the name of the Task
     * 
     * @return
     */
    public String getName() {
        return taskName;
    }

    /**
     * Returns the description of the Task
     * 
     * @return
     */
    public String getDescription() {
        return taskDescription;
    }

    /**
     * Returns the tag of the Task
     * 
     * @return
     */
    public String getTag() {
        return taskTag;
    }

    /**
     * Returns the priority of the Task
     * 
     * @return
     */
    public int getPriority() {
        return taskPriority;
    }

    /**
     * Returns the due date of the Task
     * 
     * @return
     */
    public Date getDueDate() {
        return dateDue;
    }

    /**
     * Returns the start date of the Task
     * 
     * @return
     */
    public Date getStartDate() {

        return dateDue;

    }

    /**
     * Returns the end date of the Task
     * 
     * @return
     */
    public Date getEndDate() {
        return dateEnd;
    }

    /**
     * Returns the date the Task was created
     * 
     * @return
     */
    public Date getCreatedDate() {
        return dateCreated;
    }

    /**
     * Returns the ID of the Task
     * 
     * @return
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns if the Task is marked as done
     * 
     * @return
     */
    public boolean isDone() {
        return taskCompleted;
    }

    /**
     * Returns if Task is a floating task
     * 
     * @return
     */
    public boolean isFloating() {

        return isFloatingTask;
    }

    /**
     * Returns if Task is a timed task
     * 
     * @return
     */
    public boolean isTimed() {

        return isTimedTask;

    }

    /**
     * Sets the name of the Task. Throws an exception if name is empty string or
     * null
     * 
     * @param newName
     * @throws IllegalArgumentException
     */
    public void setName(String newName) throws IllegalArgumentException {

        if (newName == null) {
            throw new IllegalArgumentException("Task name cannot be null!");
        } else if (newName.equals("")) {
            throw new IllegalArgumentException("Task name cannot be empty!");
        } else {
            taskName = newName;
        }
    }

    /**
     * Sets the description for the Task
     * 
     * @param newDescription
     */
    public void setDescription(String newDescription) {

        taskDescription = newDescription;
    }

    /**
     * Sets the tag for the Task
     * 
     * @param newTag
     */
    public void setTag(String newTag) {

        taskTag = newTag;
    }

    /**
     * Sets the priority for the Task
     * 
     * @param newPriority
     */
    public void setPriority(int newPriority) {

        taskPriority = newPriority;
    }

    /**
     * Sets the due date for the Task. If the due date is set to null, the Task
     * is converted into a floating task. The dateEnd of the task should already
     * be null and task should not be a timed task if we are casting it into a
     * floating task.
     * 
     * @param newDueDate
     */
    public void setDueDate(Date newDueDate) {
        if (newDueDate != null) {
            dateDue = newDueDate;
            isFloatingTask = false;

        } else {
            // Add assert date ends
            dateDue = null;
            isFloatingTask = true;
        }
    }

    /**
     * Sets the start date for the Task. Start is used in the context of a timed
     * task so the dateDue is used as start date.
     * 
     * @param newStartDate
     */
    public void setStartDate(Date newStartDate) {
        setDueDate(newStartDate);
    }

    /**
     * Sets the end date for the Task. If the end date is set to a non null
     * date, the Task is converted into a timed task. If end date is null the
     * Task is not a timed Task. If the task is to be casted into a timed task,
     * 
     * 
     * @param newEndDate
     */
    public void setEndDate(Date newEndDate) {
        if (newEndDate != null) {
            dateEnd = newEndDate;
            isTimedTask = true;

        } else {
            dateEnd = null;
            isTimedTask = false;
        }
    }

    // Not covered by tests yet
    public void configureCreatedDate(Date newCreatedDate) {

        // Tests needed for null date
        dateCreated = newCreatedDate;
    }

    // Not covered by tests yet
    public void setDone() {

        taskCompleted = true;
        dateCompleted = new Date(System.currentTimeMillis());

    }

    // Not covered by tests yet
    public void setUndone() {

        taskCompleted = false;
        dateCompleted = null;

    }

    // Not covered by tests yet
    public Date getDateCompleted() {

        return dateCompleted;
    }

    public void setDateCompleted(Date date) {
        dateCompleted = date;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Task) {

            Task anotherTask = (Task) obj;
            boolean dateCheck;
            if (dateDue == null || anotherTask.getDueDate() == null) {
                dateCheck = (dateDue == anotherTask.getDueDate());
            } else {
                dateCheck = dateDue.equals(anotherTask.getDueDate());
            }

            return (ID == anotherTask.getID())
                    && taskName.equals(anotherTask.getName())
                    && taskDescription.equals(anotherTask.getDescription())
                    && taskTag.equals(anotherTask.getTag())
                    && dateCreated.equals(anotherTask.getCreatedDate())
                    && dateCheck && (taskPriority == anotherTask.getPriority())
                    && (taskCompleted == anotherTask.isDone())
                    && (isFloatingTask == anotherTask.isFloating());

        } else {

            return false;
        }
    }
}
