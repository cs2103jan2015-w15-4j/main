package planner;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ListIterator;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DisplayPane extends JScrollPane{

	private JTextPane display;
	
	private TaskBar currentTaskBar;
	
	private long currentTaskBarID;
	
	private TreeMap<Long, TaskBar> listOfTasks;
	
	public DisplayPane(){
		
		listOfTasks = new TreeMap<Long, TaskBar>();
		
		currentTaskBarID = -1;
		
		prepareDisplay();  // must be first because it is added as viewport to scrollpane
		
		setViewportView(display);
		setBorder(null);
		setOpaque(false);
		
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		getViewport().setOpaque(false);
	}
	
	private void prepareDisplay(){
		
		display = new JTextPane();
		
		display.setEditable(false);
		display.setOpaque(false);
		display.setBorder(null);
		
		display.setFont( new Font( "Arial", Font.PLAIN, 2 ) );
	}
	
	public void clearDisplay(){
		
		display.setText("");
		
		currentTaskBar = null;
		
		currentTaskBarID = -1;
		
		listOfTasks.clear();
	}
	
	public boolean selectTask( long lineNumber ){
		
		if( lineNumber > 0 && lineNumber <= listOfTasks.size() ){
			
			TaskBar tempTaskBar = listOfTasks.get(lineNumber-1);
			
			if( tempTaskBar != null ){
				
				selectTask( tempTaskBar, lineNumber-1 );
				
				return true;
				
			} else{
				
				return false;
			}
			
		} else{
			
			return false;
		}
	}
	
	public boolean selectPreviousTask(){
		
		if( currentTaskBarID - 1 >= 0 ){
		
			TaskBar tempTaskBar = listOfTasks.get( currentTaskBarID + 1 );
			
			if( tempTaskBar != null ){
				
				selectTask( tempTaskBar, currentTaskBarID + 1 );
				
				return true;
				
			} else{
				
				return false;
			}
			
		} else{
			
			return false;
		}
	}
	
	public boolean selectNextTask(){
		
		if( currentTaskBarID + 1 < listOfTasks.size() ){
			
			TaskBar tempTaskBar = listOfTasks.get( currentTaskBarID + 1 );
			
			if( tempTaskBar != null ){
				
				selectTask( tempTaskBar, currentTaskBarID + 1 );
				
				return true;
				
			} else{
				
				return false;
			}
			
		} else{
			
			return false;
		}
	}
	
	private void selectTask( TaskBar taskBar, long id ){
		
		if( taskBar != null && id >= 0 && id < listOfTasks.size() ){
		
			deselectTask( currentTaskBar );
			
			taskBar.setFocusedTaskBar();
			display.setCaretPosition(taskBar.getPosition());
			
			currentTaskBarID = id;
			currentTaskBar = taskBar;
		}
	}
	
	private void deselectTask( TaskBar taskBar ){
		
		if( taskBar != null ){
			
			currentTaskBarID = -1;
			
			taskBar.setUnfocusedTaskBar();
			
			currentTaskBar = null;
		}
	}
	
	private void addComponentToDisplay( JComponent component ){
		
		if( component != null ){
			
			try{
				StyledDocument styledDocument = (StyledDocument) display.getDocument();
				Style style = styledDocument.addStyle("component", null);
				StyleConstants.setComponent(style, component);
				styledDocument.insertString( styledDocument.getLength(), "component", style );
				
			} catch( BadLocationException badLocationException ){
				
				
			}
		}
	}
	
	private void setTaskBarParameters( TaskBar taskBar, Task task, long lineNumber ){
		
		if( taskBar != null && task != null ){
			
			// Set Line number
			if( lineNumber > 0 ){
				
				taskBar.setLineNumber(lineNumber);
			}
			
			// Check if task is done
			if( task.isDone() ){
				
				taskBar.setTaskDone();
			}
			
			// set task name	
			taskBar.setTaskTitle(task.getName());
			
			// Set due date
			if( task.getDueDate() != null ){
				
				taskBar.setTaskTimeDataLabel( task.getDueDate(), "30 days");
			}
			
			// Set tags
			taskBar.setTaskTags("#DisabledForV0.1");
		}
	}
	
	private void appendString( String string ){
		
		if( string != null ){
			
			try{
				
				StyledDocument doc = display.getStyledDocument();
				
				doc.insertString(doc.getLength(), string, null );
				
			} catch( BadLocationException badLocationException ){
				
				
			}
		}
	}
	
	public boolean addTaskToDisplay( Task task  ){
		
		if( task == null ){
			
			return false;
		}
		
		long taskBarID = listOfTasks.size();
		
		appendString(" ");
		
		TaskBar taskBar = new TaskBar();
		
		setTaskBarParameters( taskBar, task, taskBarID + 1 );
		
		taskBar.setPosition( display.getCaretPosition() );
		
		addComponentToDisplay( taskBar );
		
		listOfTasks.put( taskBarID, taskBar );
		
		appendString("\n");
		
		selectTask( taskBar, taskBarID );
		
		return true;
	}
	
	public boolean addTasksToDisplay( TaskList taskList ){
		
		if( taskList == null ){
			
			return false;
		}
		
		ListIterator<Task> iterator = taskList.listIterator();
		Task currentTask;
		
		long count = 0;
		while( iterator.hasNext() ){
			
			appendString(" ");
			
			currentTask = iterator.next();
			
			currentTaskBar = new TaskBar();
			
			setTaskBarParameters( currentTaskBar, currentTask, count + 1 );
			
			currentTaskBar.setPosition( display.getCaretPosition() );
			
			addComponentToDisplay( currentTaskBar );
			
			listOfTasks.put( count, currentTaskBar );
			
			appendString("\n");
			
			++count;
		}
		
		if( !listOfTasks.isEmpty() ){
			
			selectTask( listOfTasks.get(0L), 0L );
		}
		
		return true;
	}
}
