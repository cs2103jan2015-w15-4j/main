//@author A0111333B

package planner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
* The DisplayPane class manages the display of all of YOPO's task information and messages.
*
* @author A0111333B
*/
public class DisplayPane extends JScrollPane{

	private JTextPane display;
	
	private TaskBar currentTaskBar;
	
	private long currentTaskBarID;
	
	private TreeMap<Long, TaskBar> listOfTasks;
	private TreeMap<Long, DisplayTask> tasksList;
	private TreeMap<Integer, Long> taskNumberList;
	private TreeMap<Integer, Long> jumpList;
	
	private CustomScrollBarUI scrollBarSkin;
	
	private final long DEFAULT_INVALID_TASKBAR_ID = -1;
	private final long MINIMUM_VALID_TASKBAR_ID = 0;
	private final long MINIMUM_VALID_TASK_ID = 0;
	private final long MINIMUM_VALID_JUMP_ID = 0;
	private final long DEFAULT_INVALID_LINENUMBER = 0;
	private final long MINIMUM_LINENUMBER = 1;
	private final long SIZE_OF_ONE_LINE = 1;
	private final long SIZE_OF_ONE_TASK = 1;
	private final int DEFAULT_DISPLAY_FONT_SIZE = 2;
    private final int DEFAULT_FONT_SIZE_OF_MESSAGEPANE = 20;
    private final int DEFAULT_FONT_SPACE_SIZE_OF_MESSAGEPANE = 2;
    private final int DEFAULT_RELATIVE_XCOORDINATE_OF_MESSAGEPANE = 0;
    private final int DEFAULT_RELATIVE_YCOORDINATE_OF_MESSAGEPANE = 0;
    private final int DEFAULT_HEIGHT_OF_MESSAGEPANE = 100;
    private final int DEFAULT_HIGHLIGHTED_WORDS_FONT_SIZE = 14;
    private final int DEFAULT_HEADERLABEL_FONT_SIZE = 16;
    private final int MINIMUM_SIZE_OF_ARRAY_TO_SHOW_MESSAGES = 2;
    private final int MINIMUM_SIZE_OF_ARRAY_TO_HIGHTLIGHT_KEYWORDS = 3;
    private final int START_INDEX_OF_HIGHTLIGHT_KEYWORDS_IN_ARRAY = 2;
    private final int INDEX_OF_HEADER_IN_ARRAY = 0;
    private final int INDEX_OF_MESSAGE_IN_ARRAY = 1;
    private final int MINIMUM_ARRAY_INDEX = 0;
    private final int MINIMUM_DOCUMENT_INDEX = 0;
    private final int MINIMUM_STRING_LENGTH = 0;
    private final int DEFAULT_DATEHEADER_FONT_SIZE = 16;
    private final int MINIMUM_LIST_SIZE = 0;
    private final int MAXIMUM_PRIORITY = 5;
    private final int MINIMUM_PRIOIRTY = 1;
    private final int DEFAULT_DATEHEADER_WIDTH = 50;
    private final int DEFAULT_DATEHEADER_HEIGHT = 100;
    private final int MINIMUM_LIST_INDEX = 0;
    
	private final String DEFAULT_DISPLAY_FONT_FAMILY = "Arial";
	private final String EMPTY_STRING = ""; 
	private final String BORDER_SPACING = "               ";
	private final String NEW_LINE = "\n";
	private final String DEFAULT_FONT_FAMILY_OF_MESSAGEPANE = "Arial";
	private final String TOP_BORDER_SPACING = "\n\n";
	private final String BOTTOM_BORDER_SPACING = "\n\n\n";
	private final String BOTTOM_BORDER_SPACING_II = "\n\n\n\n\n\n";
	private final String DEFAULT_HIGHLIGHTED_WORDS_FONT_FAMILY = "Arial";
    private final String DEFAULT_HEADERLABEL_FONT_FAMILY = "Arial";
    private final String SPACING = " ";
    private final String DEFAULT_DATEHEADER_FONT_FAMILY = "Arial";
    private final String PRIORITY_STRING_LABEL = "Priority ";
    private final String NO_PRIORITY_STRING_LABEL = "No Priority";
    private final String NO_TASKS_MESSAGE = " You have no tasks in this section :)";
    private final String DEFAULT_DATEFORMAT = "EEE, d MMM yyyy";
    private final String TODAY_HEADER = "Today";
    private final String FLOATING_TASK_HEADER = "Floating Tasks";
    
	private final Color DEFAULT_COLOR_OF_MESSAGEPANE = new Color( 0, 0, 0, 100 );
    
	/**
    * Constructs a display component that manages the display of all of YOPO's task information and messages and sets up 
    * the scroll functionalities for this display component.
    */
	public DisplayPane(){
		
		listOfTasks = new TreeMap<Long, TaskBar>();
		tasksList = new TreeMap<Long, DisplayTask>();
		taskNumberList = new TreeMap<Integer, Long>();
		jumpList = new TreeMap<Integer, Long>();
		currentTaskBarID = DEFAULT_INVALID_TASKBAR_ID;
		
		// prepareDisplay() must be called first because it is added as viewport to scrollpane
		prepareDisplay();
		setViewportView(display);
		setBorder(null);
		setOpaque(false);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		getViewport().setOpaque(false);
		
		JScrollBar verticalScrollBar = getVerticalScrollBar();
		verticalScrollBar.setOpaque(false);
		scrollBarSkin = new CustomScrollBarUI();
		verticalScrollBar.setUI( scrollBarSkin );
	}
	
    /**
     * Return true if there are tasks being displayed currently; false otherwise
     * 
     * @return True if there are tasks being displayed currently; false otherwise
     */
	public boolean hasTasksDisplayed(){
	    return (listOfTasks != null && !listOfTasks.isEmpty());
	}
	
	/**
     * Initializes and sets the attributes for the display component of this DisplayPane.
     */
	private void prepareDisplay(){
		display = new JTextPane();
		display.setHighlighter(null);
		display.setEditable(false);
		display.setOpaque(false);
		display.setBorder(null);
		display.setFont( new Font( DEFAULT_DISPLAY_FONT_FAMILY, Font.PLAIN, DEFAULT_DISPLAY_FONT_SIZE ) );
	}
	
	/**
     * Clears all content currently being displayed on the display component of this DisplayPane.
     */
	public void clearDisplay(){
		display.setText(EMPTY_STRING);
		currentTaskBar = null;
		currentTaskBarID = DEFAULT_INVALID_TASKBAR_ID;
		listOfTasks.clear();
		tasksList.clear();
		taskNumberList.clear();
	}
	
	/**
     * Returns task id of the currently highlighted task in the display component of this DisplayPane.
     * 
     * @return The task id of the currently highlighted task in the display component of this DisplayPane.
     */
	public long getCurrentSelectedTaskID(){
	    return currentTaskBarID;
	}

	/**
     * Returns task id of the currently highlighted task in the display component of this DisplayPane.
     * 
     * @return The task id of the currently highlighted task in the display component of this DisplayPane.
     */
	public boolean selectTask( long lineNumber ){
		if( lineNumber >= MINIMUM_LINENUMBER && lineNumber <= listOfTasks.size() ){
			TaskBar tempTaskBar = listOfTasks.get(lineNumber-SIZE_OF_ONE_LINE);
			if( tempTaskBar != null ){
				selectTask( tempTaskBar, lineNumber-SIZE_OF_ONE_LINE );             // Changed this function
				return true;
			}
		} 
		return false;
	}
	
	/**
     * Highlights the task at the given distance away from the currently highlighted task 
     * 
     * @return True if there is no currently highlighted task or the task at the given distance away from 
     *         the currently highlighted task does not exists; false otherwise.
     */
	public boolean selectTaskRelativeToCurrentSelectedTask( int distance ){
	    if( !listOfTasks.isEmpty() ){
	        long newCurrentTaskBarID = currentTaskBarID + distance;
	        newCurrentTaskBarID = Math.max( listOfTasks.firstKey(), newCurrentTaskBarID ); 
	        newCurrentTaskBarID = Math.min( listOfTasks.lastKey(),  newCurrentTaskBarID );
	        
	        if( newCurrentTaskBarID == currentTaskBarID ){
	            return true;
	        }
	        TaskBar tempTaskBar = listOfTasks.get( newCurrentTaskBarID );
	        if( tempTaskBar != null ){
	            selectTask( tempTaskBar, newCurrentTaskBarID );
                return true;
	        }
        } 
        return false;
	}
	
	/**
     * Highlights the previous task relative to the currently highlighted task 
     * 
     * @return True if there is no currently highlighted task or the previous task relative to the currently highlighted task 
     *              does not exists; false otherwise.
     */
	public boolean selectPreviousTask(){
		if( !listOfTasks.isEmpty() && (currentTaskBarID - SIZE_OF_ONE_TASK < listOfTasks.size()) ){
		    
		    long newCurrentTaskBarID;
		    if( currentTaskBarID - SIZE_OF_ONE_TASK <= DEFAULT_INVALID_TASKBAR_ID ){
		        newCurrentTaskBarID = listOfTasks.size() - SIZE_OF_ONE_TASK;
		    } else{
		        newCurrentTaskBarID = currentTaskBarID - SIZE_OF_ONE_TASK;
		    }
			TaskBar tempTaskBar = listOfTasks.get( newCurrentTaskBarID );
			if( tempTaskBar != null ){
				selectTask( tempTaskBar, newCurrentTaskBarID );
				return true;
			}
		}
		return false;         // cHANGED THIS FUNCTION
	}
	
	/**
     * Highlights the next task relative to the currently highlighted task 
     * 
     * @return True if there is no currently highlighted task or the next task relative to the currently highlighted task 
     *              does not exists; false otherwise.
     */
	public boolean selectNextTask(){
	    
		if( !listOfTasks.isEmpty() && currentTaskBarID + SIZE_OF_ONE_TASK >= MINIMUM_VALID_TASKBAR_ID ){
			
		    long newCurrentTaskBarID;
		    if( currentTaskBarID + SIZE_OF_ONE_TASK >= listOfTasks.size() ){
		        newCurrentTaskBarID = MINIMUM_VALID_TASKBAR_ID;
		    } else{
		        newCurrentTaskBarID = currentTaskBarID + SIZE_OF_ONE_TASK;
		    }
			TaskBar tempTaskBar = listOfTasks.get( newCurrentTaskBarID );
			if( tempTaskBar != null ){
				selectTask( tempTaskBar, newCurrentTaskBarID );
				return true;
			}
		} 
		return false;         // cHANGED THIS FUNCTION
	}
	
	/**
     * Highlights the given taskBar, sets the currentTaskBarID as the given id and currentTaskBar as the given taskBar
     * only if both taskBar and id are valid; Otherwise nothing will take place. 
     * 
     * @param taskBar   The taskBar to be highlighted
     * @param id        The id of the taskBar to be highlighted
     */
	private void selectTask( TaskBar taskBar, long id ){
		if( taskBar != null && id >= MINIMUM_VALID_TASKBAR_ID && id < listOfTasks.size()  ){
			deselectTask( currentTaskBar );
			taskBar.setFocusedTaskBar();
			display.setCaretPosition(taskBar.getPosition());
			currentTaskBarID = id;
			currentTaskBar = taskBar;
		}
	}
	
	/**
     * Remove the highlights effect on the given taskBar, sets the currentTaskBarID as -1 and currentTaskBar as null if 
     * the given taskBar is valid; Otherwise nothing will take place.
     * 
     * @param taskBar   The taskBar to have its highlight effect removed
     */
	private void deselectTask( TaskBar taskBar ){
		if( taskBar != null ){
			currentTaskBarID = DEFAULT_INVALID_TASKBAR_ID;
			taskBar.setUnfocusedTaskBar();
			currentTaskBar = null;
		}
	}
	
	/**
     * Adds the given component to the display which is rendered by the display component of this DisplayPane
     * 
     * @param component   The component to add to the display which is rendered by the display component of this DisplayPane
     */
	private void addComponentToDisplay( JComponent component ){
		if( component != null ){
			try{
				StyledDocument styledDocument = (StyledDocument) display.getDocument();
				Style style = styledDocument.addStyle("component", null);
				StyleConstants.setComponent(style, component);
				styledDocument.insertString( styledDocument.getLength(), "component", style );
			} catch( BadLocationException badLocationException ){}
		}
	}
	
	/**
     * Sets the display contents of the taskBar
     * 
     * @param currentDate   The date which this task is categorized under (This date object only contains
     *                      day of the month, the month and the year)
     * @param taskBar       The taskBar object in which the given task information will be displayed on
     * @param task          The Task object in which its information will be extracted out to fill the display 
     *                      contents of the taskBar
     * @param lineNumber    The id that will be displayed for the taskBar
     */
	private void setTaskBarParameters( Date currentDate, TaskBar taskBar, DisplayTask task, long lineNumber ){
	    Task parentTask;
		if( taskBar != null && task != null ){
		    parentTask = task.getParent();
		    if(parentTask != null){
    			if( lineNumber > DEFAULT_INVALID_LINENUMBER ){
    				taskBar.setLineNumber(lineNumber);
    			}
    			if( parentTask.isDone() ){
    				taskBar.setTaskDone();
    			}	
    			taskBar.setTaskTitle(parentTask);
    			taskBar.setTags(parentTask);
    			taskBar.setTimeDisplayLabel(currentDate, task);
		    }
		}
	}
	
	/**
     * Appends the given strings to the display component rendered using the given style
     * 
     * @param string    String to be appended to display component
     * @param style     The style in which the string will be rendered in
     */
	private void appendString( String string, Style style ){
		if( string != null ){
			try{
				StyledDocument doc = display.getStyledDocument();
				doc.insertString(doc.getLength(), string, style );
			} catch( BadLocationException badLocationException ){}
		}
	}

	/**
     * Adds the given task to the display component in the form of a TaskBar object
     * 
     * @param currentDate    The date which this task is categorized under (This date object only contains
     *                       day of the month, the month and the year)
     * @param task           The Task object in which its information will be extracted out to fill the display 
     *                       contents of the taskBar
     */
	public boolean addTaskToDisplay( Date currentDate, DisplayTask task ){
		
		if( task == null || task.getParent() == null ){
			return false;
		}
		long taskBarID = listOfTasks.size();
		TaskBar taskBar = new TaskBar();
		setTaskBarParameters( currentDate, taskBar, task, task.getParent().getID() );
		taskBar.setPosition( display.getCaretPosition() );
		appendString(BORDER_SPACING, null);
		addComponentToDisplay( taskBar );
		listOfTasks.put( taskBarID, taskBar );
		tasksList.put(taskBarID, task);
		taskNumberList.put(task.getParent().getID(), taskBarID);
		jumpList.put(task.getID(), taskBarID);
		appendString(NEW_LINE, null);
		selectTask( taskBar, taskBarID );
		return true;
	}
	
	/**
     * Returns the DisplayTask Object representing the currently highlighted taskBar
     */
	public DisplayTask getCurrentSelectedTask(){
	    return tasksList.get(currentTaskBarID);
	}
	
	/**
     * Highlights the taskBar corresponding to its jump id specified in its corresponding DisplayTask Object.
     * 
     * @param jumpID    The jump id associated with the taskBar to be highlighted
     * @return TRUE if the taskBar corresponding to the given jump id exists; false otherwise.
     */
	public boolean selectGivenJumpID( int jumpID ){
	    if( jumpID >= MINIMUM_VALID_JUMP_ID ){
            Long internalID = jumpList.get(jumpID);
            if( internalID != null ){     
                TaskBar tempTaskBar = listOfTasks.get(internalID);  
                if(tempTaskBar != null){   
                    selectTask( tempTaskBar, internalID );
                    return true;  
                }
            }
	    }
        return false;
	}
	
	/**
     * Highlights the taskBar corresponding to its taskid specified in its corresponding Task Object.
     * 
     * @param taskID    The task id associated with the taskBar to be highlighted
     * @return TRUE if the taskBar corresponding to the given task id exists; false otherwise.
     */
	public boolean selectGivenTaskID( int taskID ){
	    if( taskID >= MINIMUM_VALID_TASK_ID ){
	        Long internalID = taskNumberList.get(taskID);
	        if( internalID != null ){
                TaskBar tempTaskBar = listOfTasks.get(internalID);
                if(tempTaskBar != null){
                    selectTask( tempTaskBar, internalID );
                    return true;
                } 
	        }
	    }
	    return false;
	}
	
	/**
     * Highlights the taskBar corresponding to the given Task object.
     * 
     * @param task    The Task object associated with the taskBar to be highlighted
     * @return TRUE if the taskBar corresponding to the given task exists; false otherwise.
     */
	public boolean selectGivenTask( Task task ){
	    if( task != null ){
	        Long internalID = taskNumberList.get(task.getID());
	        if( internalID != null ){
	            TaskBar tempTaskBar = listOfTasks.get(internalID);
	            if(tempTaskBar != null){
	                selectTask( tempTaskBar, internalID );
	                return true;
	            } 
	        }
	    }
	    return false;
	}
	
	/**
     * Adds tasks found within taskList to the display component in the form of a TaskBar object
     * 
     * @param currentDate    The current date that the tasks found within taskList are categorized under
     * @param taskList       The list containing the tasks that is going to be added to the display component in 
     *                       the form of a TaskBar object
     * 
     * @return TRUE if all tasks contained within taskList is added successfully to the display component in the 
     *         form of a TaskBar object; false otherwise.
     */
    private boolean addTasksToDisplay( Date currentDate, DisplayTaskList taskList ){
		if( !addTasksToDisplayWithoutSelection(currentDate, taskList) ){
		    return false;
		}
		if( !listOfTasks.isEmpty() && currentTaskBarID != MINIMUM_VALID_TASKBAR_ID ){
			selectTask( listOfTasks.get(MINIMUM_VALID_TASKBAR_ID), MINIMUM_VALID_TASKBAR_ID );
		}
		return true;
	}
	
    /**
     * Returns the number of tasks currently being displayed on the display component in the form of a TaskBar object
     * 
     * @return The number of tasks currently being displayed on the display component in the form of a TaskBar object
     */
	public int getNumberOfTasksDisplayed(){
	    return listOfTasks.size();
	}
	
	/**
     * Displays the given message encapsulated in a translucent box onto the display component of this DisplayPane
     * 
     * @param msg   The message to be encapsulated in a translucent box and display onto the display component of this DisplayPane
     */
	public void showMessageOnDisplay( String msg ){
	    if( msg != null ){
	        clearDisplay();
	        TranslucentTextPane messagePane = new TranslucentTextPane( DEFAULT_COLOR_OF_MESSAGEPANE );
	        messagePane.setEditable(false);
	        messagePane.setHighlighter(null);
	        messagePane.setFocusable(false);
	        messagePane.setFont(new Font( DEFAULT_FONT_FAMILY_OF_MESSAGEPANE, Font.BOLD, DEFAULT_FONT_SIZE_OF_MESSAGEPANE ));
	        messagePane.setForeground(Color.WHITE);
	        messagePane.setBounds(DEFAULT_RELATIVE_XCOORDINATE_OF_MESSAGEPANE, 
	                              DEFAULT_RELATIVE_YCOORDINATE_OF_MESSAGEPANE, 
	                              display.getWidth(), DEFAULT_HEIGHT_OF_MESSAGEPANE);
	        SimpleAttributeSet newLineStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(newLineStyle, DEFAULT_FONT_FAMILY_OF_MESSAGEPANE);
	        StyleConstants.setFontSize(newLineStyle, DEFAULT_FONT_SPACE_SIZE_OF_MESSAGEPANE);
	        StyleConstants.setBold(newLineStyle, false);
	        SimpleAttributeSet TextStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(TextStyle, DEFAULT_FONT_FAMILY_OF_MESSAGEPANE);
	        StyleConstants.setFontSize(TextStyle, DEFAULT_FONT_SIZE_OF_MESSAGEPANE);
	        StyleConstants.setBold(TextStyle, true);
	        messagePane.initialiseForResize();
	        messagePane.appendText(TOP_BORDER_SPACING, newLineStyle);
	        messagePane.appendText(msg, TextStyle);
	        messagePane.appendText(BOTTOM_BORDER_SPACING, newLineStyle);
	        messagePane.adjustComponentSizeToFitText();
	        addComponentToDisplay(messagePane);
	    }
	}
	
	/**
     * Displays messages contained within the 2D String array mappings onto the display component of this DisplayPane and encapsulates
     * each message in a translucent box
     * 
     * @param mappings   The String 2D array containing the messages to be displayed.
     * @param idx        If valid, it will only display messages contained within mappings[idx]; otherwise it will display all messages
     *                   contained within mappings.
     *                   
     * @return True if all messages within mappings are added to the display component of this DisplayPane successfully; false otherwise.
     */
	public boolean addInfoToDisplay( String [][]mappings, int idx ){
	    if( mappings != null ){
	        clearDisplay();
	        SimpleAttributeSet hightlightStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(hightlightStyle, DEFAULT_HIGHLIGHTED_WORDS_FONT_FAMILY);
	        StyleConstants.setFontSize(hightlightStyle, DEFAULT_HIGHLIGHTED_WORDS_FONT_SIZE);
	        StyleConstants.setBold(hightlightStyle, true);
	        StyleConstants.setItalic(hightlightStyle, true);
	        StyleConstants.setForeground(hightlightStyle, Color.GREEN);
	        if( idx >= MINIMUM_ARRAY_INDEX && idx < mappings.length 
	            && mappings[idx].length >= MINIMUM_SIZE_OF_ARRAY_TO_SHOW_MESSAGES ){
	            String []wordsToHighlight = null;
	            if( mappings[idx].length >= MINIMUM_SIZE_OF_ARRAY_TO_HIGHTLIGHT_KEYWORDS ){
	                wordsToHighlight = Arrays.copyOfRange(mappings[idx], 
	                                                      START_INDEX_OF_HIGHTLIGHT_KEYWORDS_IN_ARRAY, mappings[idx].length);
	            }
	            addInfoToDisplayWithoutSelection( mappings[idx][INDEX_OF_MESSAGE_IN_ARRAY], wordsToHighlight, hightlightStyle );
	        } else{
	            String []wordsToHighlight;
	            for( int i = MINIMUM_ARRAY_INDEX, size = mappings.length; i < size; ++i ){
	                if( mappings[i].length > MINIMUM_ARRAY_INDEX ){
	                    
    	                JLabel headerLabel = new JLabel();
    	                headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
    	                headerLabel.setFont(new Font(DEFAULT_HEADERLABEL_FONT_FAMILY, Font.BOLD, DEFAULT_HEADERLABEL_FONT_SIZE));
    	                headerLabel.setForeground(Color.WHITE);
    	                headerLabel.setText(SPACING + mappings[i][INDEX_OF_HEADER_IN_ARRAY]);
    	                addComponentToDisplay(headerLabel);
    	                appendString(NEW_LINE, null);
    	                wordsToHighlight = null;
    	                if( mappings[i].length >= MINIMUM_SIZE_OF_ARRAY_TO_HIGHTLIGHT_KEYWORDS ){
    	                    
    	                    wordsToHighlight = Arrays.copyOfRange(mappings[i], 
    	                                                          START_INDEX_OF_HIGHTLIGHT_KEYWORDS_IN_ARRAY, 
    	                                                          mappings[i].length);
    	                }
    	                addInfoToDisplayWithoutSelection( mappings[i][INDEX_OF_MESSAGE_IN_ARRAY], wordsToHighlight, hightlightStyle );
    	                appendString( BOTTOM_BORDER_SPACING_II, null );
	                }
	            }
	        }
	        if( !listOfTasks.isEmpty() && currentTaskBarID != MINIMUM_VALID_TASKBAR_ID ){
                selectTask( listOfTasks.get(MINIMUM_VALID_TASKBAR_ID), MINIMUM_VALID_TASKBAR_ID );
            }
	        display.setCaretPosition(MINIMUM_DOCUMENT_INDEX);
	        return true;
	    }   
	    return false;
	}
	
	/**
     * Displays the given info onto the display component of this DisplayPane and encapsulates the message in a translucent box
     * 
     * @param info              The string to be displayed 
     * @param wordsToHighlight  An array of keywords to be highlighted with the given string to be displayed
     * @param style             The style of the displayed string to be rendered in.   
     * 
     * @return True if the info is added to the display component of this DisplayPane successfully; false otherwise.
     */
	private boolean addInfoToDisplayWithoutSelection( String info, String []wordsToHighlight, SimpleAttributeSet style ){
	    if( info == null ){
	        return false;
	    }
	    SimpleAttributeSet newLineStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(newLineStyle, DEFAULT_FONT_FAMILY_OF_MESSAGEPANE);
        StyleConstants.setFontSize(newLineStyle, DEFAULT_FONT_SPACE_SIZE_OF_MESSAGEPANE);
        StyleConstants.setBold(newLineStyle, false);
        SimpleAttributeSet TextStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(TextStyle, DEFAULT_FONT_FAMILY_OF_MESSAGEPANE);
        StyleConstants.setFontSize(TextStyle, DEFAULT_HIGHLIGHTED_WORDS_FONT_SIZE);
        StyleConstants.setBold(TextStyle, true);
        
        TranslucentTextPane messagePane = new TranslucentTextPane( DEFAULT_COLOR_OF_MESSAGEPANE );
        messagePane.setEditable(false);
        messagePane.setHighlighter(null);
        messagePane.setFocusable(false);
        messagePane.setFont(new Font( DEFAULT_FONT_FAMILY_OF_MESSAGEPANE, Font.BOLD, DEFAULT_FONT_SIZE_OF_MESSAGEPANE ));
        messagePane.setForeground(Color.WHITE);
        messagePane.setBounds(DEFAULT_RELATIVE_XCOORDINATE_OF_MESSAGEPANE, DEFAULT_RELATIVE_YCOORDINATE_OF_MESSAGEPANE, 
                              display.getWidth(), DEFAULT_HEIGHT_OF_MESSAGEPANE);
        messagePane.initialiseForResize();
        messagePane.appendText(TOP_BORDER_SPACING, newLineStyle);
        messagePane.appendText(info, TextStyle);
        messagePane.appendText(BOTTOM_BORDER_SPACING, newLineStyle);
        messagePane.adjustComponentSizeToFitText();
        
        addComponentToDisplay(messagePane);
        if( wordsToHighlight != null && wordsToHighlight.length > MINIMUM_STRING_LENGTH ){
            messagePane.highlightWords(wordsToHighlight, style);
        }
        return true;
	}
	
	/**
     * Displays the given task onto the display component of this DisplayPane in the form of a TaskBar object attached 
     * the give header label. NOTE: The first taskBar displayed on the display component of this DisplayPane will not be 
     * automatically highlighted.
     * 
     * @param currentDate       The date that task is categorized under.
     * @param task              The task whose information will be displayed on the display component of this DisplayPane in the form of a TaskBar object
     * @param header            The header label which will be attached to the top of the taskBar containing the contents of the given task
     * 
     * @return True if the given task is displayed successfully onto the display component of this DisplayPane; false otherwise.
     */
	private boolean addTaskWithHeaderToDisplayWithoutSelection( Date currentDate, DisplayTask task, JLabel header ){
	    if( task == null || task.getParent() == null ){
            return false;
        }
	    long taskBarID = listOfTasks.size();
        TaskBar taskBar = new TaskBar(header);
        setTaskBarParameters( currentDate, taskBar, task, task.getParent().getID() );
        taskBar.setPosition( display.getCaretPosition() );
        appendString(BORDER_SPACING, null);
        addComponentToDisplay( taskBar );
        listOfTasks.put( taskBarID, taskBar );
        tasksList.put(taskBarID, task);
        taskNumberList.put(task.getParent().getID(), taskBarID);
        jumpList.put(task.getID(), taskBarID);
        appendString(NEW_LINE, null);
        return true;
	}
	
	/**
     * Displays the tasks contained in the taskList onto the display component of this DisplayPane in the form of a TaskBar object with the first
     * task in a date catergory attached with a header label filled with the contents of currentDate
     * NOTE: The first taskBar displayed on the display component of this DisplayPane will not be automatically highlighted.
     * 
     * @param currentDate       The date that all tasks contained in taskList is categorized under.
     * @param taskList          The list containing the tasks that is going to be added to the display component in 
     *                          the form of a TaskBar object
     *                          
     * @return True if all tasks contained within taskList is displayed successfullyonto the display component of this DisplayPane; false otherwise.
     */
	private boolean addTasksToDisplayWithoutSelection( Date currentDate, DisplayTaskList taskList ){
	    if( taskList == null ){
            return false;
        }
        ListIterator<DisplayTask> iterator = taskList.listIterator();
        DisplayTask currentTask;
        display.setCaretPosition(display.getStyledDocument().getLength());
  
        long idxForCurrentTaskBar;
        TaskBar tempTaskBar;
        while( iterator.hasNext() ){
            currentTask = iterator.next();
            if( currentTask != null && currentTask.getParent() != null ){
                appendString(BORDER_SPACING, null);
                tempTaskBar = new TaskBar();
                setTaskBarParameters( currentDate, tempTaskBar, currentTask, currentTask.getParent().getID() );
                tempTaskBar.setPosition( display.getCaretPosition() );
                addComponentToDisplay( tempTaskBar );
                idxForCurrentTaskBar = listOfTasks.size();
                listOfTasks.put( idxForCurrentTaskBar, tempTaskBar );
                tasksList.put(idxForCurrentTaskBar, currentTask);
                taskNumberList.put( currentTask.getParent().getID(), idxForCurrentTaskBar );
                jumpList.put(currentTask.getID(), idxForCurrentTaskBar);
                appendString(NEW_LINE, null);
            }
        }
        return true;
	}
	
	/**
     * Returns the up button component that is used by the scrollbar attached to the display component of this
     * DisplayPanel
     * 
     * @return The up button component that is used by the scrollbar attached to the display component of this
     * DisplayPanel
     */
	public InvisibleButton getUpButtonComponent(){
        return (scrollBarSkin != null ? scrollBarSkin.getUpButtonComponent() : null);
    }
    
	/**
     * Returns the down button component that is used by the scrollbar attached to the display component of this
     * DisplayPanel
     * 
     * @return The down button component that is used by the scrollbar attached to the display component of this
     * DisplayPanel
     */
    public InvisibleButton getDownButtonComponent(){
        return (scrollBarSkin != null ? scrollBarSkin.getDownButtonComponent() : null);
    }
    
    /**
     * Returns the display component of this DisplayPanel
     * 
     * @return The display component of this DisplayPanel
     */
    public JTextPane getDisplayComponent(){
        return display;
    }
    
    /**
     * Displays the tasks contained in the given displayList by priority from the highest(5) priority to the lowest(1) priority.
     * 
     * @param displayList       A list of DisplayTaskList sorted from the highest(5) priority to the lowest(1) priority.
     */
    public void displayByPriority( Set<Map.Entry<Integer, DisplayTaskList>> displayList ){
        if( displayList != null ){
            Integer priority;
            DisplayTaskList tempTaskList = null;
            JLabel dateHeaderLabel;
            DisplayTask tempDisplayTask;
            for( Map.Entry<Integer, DisplayTaskList> entry : displayList ){
                tempTaskList = entry.getValue();
                if( tempTaskList != null && tempTaskList.size() > MINIMUM_LIST_SIZE ){
                    priority = entry.getKey();
                    dateHeaderLabel = new JLabel();
                    dateHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    dateHeaderLabel.setFont(new Font(DEFAULT_DATEHEADER_FONT_FAMILY, Font.BOLD, DEFAULT_DATEHEADER_FONT_SIZE));
                    dateHeaderLabel.setForeground(Color.WHITE);
                    if( priority != null && priority >= MINIMUM_PRIOIRTY && priority <= MAXIMUM_PRIORITY ){
                        dateHeaderLabel.setText( PRIORITY_STRING_LABEL + priority );
                    } else{
                        dateHeaderLabel.setText( NO_PRIORITY_STRING_LABEL );
                    }
                    dateHeaderLabel.setPreferredSize(new Dimension( DEFAULT_DATEHEADER_WIDTH, DEFAULT_DATEHEADER_HEIGHT ));
                    dateHeaderLabel.setMaximumSize(new Dimension( DEFAULT_DATEHEADER_WIDTH, DEFAULT_DATEHEADER_HEIGHT ));
                    dateHeaderLabel.setMinimumSize(new Dimension( DEFAULT_DATEHEADER_WIDTH, DEFAULT_DATEHEADER_HEIGHT ));
                    dateHeaderLabel.setSize(DEFAULT_DATEHEADER_WIDTH, DEFAULT_DATEHEADER_HEIGHT);
 
                    tempDisplayTask = tempTaskList.get(MINIMUM_LIST_INDEX );
                    addTaskWithHeaderToDisplayWithoutSelection( null, tempDisplayTask, dateHeaderLabel );
                    tempTaskList.set(MINIMUM_LIST_INDEX , null);
                    addTasksToDisplayWithoutSelection( null, tempTaskList );
                    tempTaskList.set(MINIMUM_LIST_INDEX , tempDisplayTask);
                }
                appendString( BOTTOM_BORDER_SPACING_II, null );
            }
            if( !listOfTasks.isEmpty() && currentTaskBarID != MINIMUM_VALID_TASKBAR_ID ){
                selectTask( listOfTasks.get(MINIMUM_VALID_TASKBAR_ID), MINIMUM_VALID_TASKBAR_ID );
            }
        }
        if( !hasTasksDisplayed() ){
            showMessageOnDisplay( NO_TASKS_MESSAGE );
        }
    }
    
    /**
     * Displays the tasks contained in the given displayList by date from earliest date in the displayList to the latest date in the displayList.
     * 
     * @param displayList   A list of DisplayTaskList sorted from the earliest date to the latest date
     */
    public void displayByDate( Set<Map.Entry<Date, DisplayTaskList>> displayList ){
        if( displayList != null ){
            Date tempDate;
            DisplayTaskList tempTaskList = null;
            Date todayDate = new Date(System.currentTimeMillis());
            JLabel dateHeaderLabel;
            DisplayTask tempDisplayTask;
            SimpleDateFormat dateFormatterForDisplay = new SimpleDateFormat( DEFAULT_DATEFORMAT );
            for( Map.Entry<Date, DisplayTaskList> entry : displayList ){
                tempTaskList = entry.getValue();
                if( tempTaskList != null && tempTaskList.size() > MINIMUM_LIST_SIZE ){
                    tempDate = entry.getKey();
                    dateHeaderLabel = new JLabel();
                    dateHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    dateHeaderLabel.setFont(new Font(DEFAULT_DATEHEADER_FONT_FAMILY, Font.BOLD, DEFAULT_DATEHEADER_FONT_SIZE));
                    dateHeaderLabel.setForeground(Color.WHITE);
                    if( tempDate != null ){
                        if( !TaskBar.compareByDateOnly(tempDate, todayDate) ){
                            dateHeaderLabel.setText(dateFormatterForDisplay.format(tempDate));
                        } else{
                            dateHeaderLabel.setText(TODAY_HEADER);
                        }
                    } else{
                        dateHeaderLabel.setText( FLOATING_TASK_HEADER );
                    }
                    tempDisplayTask = tempTaskList.get(MINIMUM_LIST_INDEX);
                    addTaskWithHeaderToDisplayWithoutSelection( tempDate, tempDisplayTask, dateHeaderLabel );
                    tempTaskList.set(MINIMUM_LIST_INDEX, null);
                    addTasksToDisplayWithoutSelection( tempDate, tempTaskList );
                    tempTaskList.set(MINIMUM_LIST_INDEX, tempDisplayTask);
                }
                appendString( BOTTOM_BORDER_SPACING_II, null );
            }
            if( !listOfTasks.isEmpty() && currentTaskBarID != MINIMUM_VALID_TASKBAR_ID ){
                selectTask( listOfTasks.get(MINIMUM_VALID_TASKBAR_ID), MINIMUM_VALID_TASKBAR_ID );
            }
        }
        if( !hasTasksDisplayed() ){
            showMessageOnDisplay( NO_TASKS_MESSAGE );
        }
    }
}
