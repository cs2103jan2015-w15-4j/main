package planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DisplayTaskList implements List<DisplayTask>{
    
    private ArrayList<DisplayTask> displayTasks;
    
    public DisplayTaskList() {
        displayTasks = new ArrayList<DisplayTask>();
    }
    
    public DisplayTaskList (ArrayList<DisplayTask> input) {
        
        displayTasks = new ArrayList<DisplayTask>();
        
        // Changed to defensive copy
        if( input != null ){
            
            Iterator<DisplayTask> iterator = input.iterator();
            DisplayTask currentTask;
            
            while( iterator.hasNext() ){
                
                currentTask = iterator.next();
                
                if( currentTask != null ){
                    
                    input.add( new DisplayTask( currentTask ) );
                }
            }
        }
    }
    
    public DisplayTaskList( DisplayTaskList anotherTaskList ){
        
        displayTasks = new ArrayList<DisplayTask>();
        
        copyTaskList( anotherTaskList );
    }
    
    public boolean copyTaskList( DisplayTaskList anotherTaskList ){
        
        if( anotherTaskList != null ){
            
            displayTasks.clear();
            
            Iterator<DisplayTask> iterator = anotherTaskList.iterator();
            
            DisplayTask tempTask;
            
            while( iterator.hasNext() ){
                
                tempTask = iterator.next();
                
                // Corner case: NULL is inserted into anotherTaskList
                // Changed adding tasks to defensive copy
                if( tempTask != null ){
                    
                    displayTasks.add(new DisplayTask(tempTask));
                }
            }
            
            return true;
            
        } else{
            
            return false;
        }
    }
    
    public ArrayList<DisplayTask> getTasks() {
        return displayTasks;
    }

    public DisplayTask getTaskByID(long ID) {
        for(DisplayTask t:displayTasks) {
            if(t.getID() == ID) {
                return t;
            }
        }
        return null;
    }
    
    @Override
    public int size() {
        return displayTasks.size();
    }

    @Override
    public boolean isEmpty() {
        return displayTasks.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return displayTasks.contains(o);
    }

    @Override
    public Iterator<DisplayTask> iterator() {
        return displayTasks.iterator();
    }

    @Override
    public Object[] toArray() {
        return displayTasks.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return displayTasks.toArray(a);
    }

    @Override
    public boolean add(DisplayTask e) {
        return displayTasks.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return displayTasks.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return displayTasks.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DisplayTask> c) {
        return displayTasks.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends DisplayTask> c) {
        return displayTasks.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return displayTasks.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return displayTasks.retainAll(c);
    }

    @Override
    public void clear() {
        displayTasks.clear();
        
    }

    @Override
    public DisplayTask get(int index) {
        return displayTasks.get(index);
    }

    @Override
    public DisplayTask set(int index, DisplayTask element) {
        return displayTasks.set(index, element);
    }

    @Override
    public void add(int index, DisplayTask element) {
        displayTasks.add(index, element);
        
    }

    @Override
    public DisplayTask remove(int index) {
        return displayTasks.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return displayTasks.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return displayTasks.lastIndexOf(o);
    }

    @Override
    public ListIterator<DisplayTask> listIterator() {
        return displayTasks.listIterator();
    }

    @Override
    public ListIterator<DisplayTask> listIterator(int index) {
        return displayTasks.listIterator(index);
    }

    @Override
    public List<DisplayTask> subList(int fromIndex, int toIndex) {
        return displayTasks.subList(fromIndex, toIndex);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DisplayTaskList) {
            
            DisplayTaskList tasks = (DisplayTaskList) obj;
            if (this.size() == tasks.size()) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (!(this.get(i).equals(tasks.get(i)))) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
