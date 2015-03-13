package planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * TaskList class is a container class for Tasks. It extends ArrayList.
 * @author kohwaikit
 *
 */
public class TaskList implements List<Task>{
	
    private ArrayList<Task> tasks;
    
    public TaskList() {
        tasks = new ArrayList<Task>();
    }
    
    public TaskList(ArrayList<Task> input) {
        
        tasks = new ArrayList<Task>();
        
        // Changed to defensive copy
    	if( input != null ){
    		
    	    Iterator<Task> iterator = input.iterator();
    	    Task currentTask;
    	    
    	    while( iterator.hasNext() ){
    	        
    	        currentTask = iterator.next();
    	        
    	        if( currentTask != null ){
    	            
    	            input.add( new Task( currentTask ) );
    	        }
    	    }
    	}
    }
    
    public TaskList( TaskList anotherTaskList ){
    	
    	tasks = new ArrayList<Task>();
    	
    	copyTaskList( anotherTaskList );
    }
    
    public boolean copyTaskList( TaskList anotherTaskList ){
    	
    	if( anotherTaskList != null ){
    		
    		tasks.clear();
    		
    		Iterator<Task> iterator = anotherTaskList.iterator();
    		
    		Task tempTask;
    		
    		while( iterator.hasNext() ){
    			
    		    tempTask = iterator.next();
    		    
    		    // Corner case: NULL is inserted into anotherTaskList
    		    // Changed adding tasks to defensive copy
    		    if( tempTask != null ){
    		        
    		        tasks.add(new Task(tempTask));
    		    }
    		}
    		
    		return true;
    		
    	} else{
    		
    		return false;
    	}
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getTaskByID(long ID) {
        for(Task t:tasks) {
            if(t.getID() == ID) {
                return t;
            }
        }
        return null;
    }
    
    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return tasks.contains(o);
    }

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }

    @Override
    public Object[] toArray() {
        return tasks.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return tasks.toArray(a);
    }

    @Override
    public boolean add(Task e) {
        return tasks.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return tasks.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return tasks.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Task> c) {
        return tasks.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Task> c) {
        return tasks.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return tasks.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return tasks.retainAll(c);
    }

    @Override
    public void clear() {
        tasks.clear();
        
    }

    @Override
    public Task get(int index) {
        return tasks.get(index);
    }

    @Override
    public Task set(int index, Task element) {
        return tasks.set(index, element);
    }

    @Override
    public void add(int index, Task element) {
        tasks.add(index, element);
        
    }

    @Override
    public Task remove(int index) {
        return tasks.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return tasks.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return tasks.lastIndexOf(o);
    }

    @Override
    public ListIterator<Task> listIterator() {
        return tasks.listIterator();
    }

    @Override
    public ListIterator<Task> listIterator(int index) {
        return tasks.listIterator(index);
    }

    @Override
    public List<Task> subList(int fromIndex, int toIndex) {
        return tasks.subList(fromIndex, toIndex);
    }
}
