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

public class DisplayPane extends JScrollPane{

	private JTextPane display;
	
	private TaskBar currentTaskBar;
	
	private long currentTaskBarID;
	
	private TreeMap<Long, TaskBar> listOfTasks;
	private TreeMap<Long, DisplayTask> tasksList;
	private TreeMap<Integer, Long> taskNumberList;
	private TreeMap<Integer, Long> jumpList;
	
	private CustomScrollBarUI scrollBarSkin;
	
	public DisplayPane(){
		
		listOfTasks = new TreeMap<Long, TaskBar>();
		tasksList = new TreeMap<Long, DisplayTask>();
		taskNumberList = new TreeMap<Integer, Long>();
		jumpList = new TreeMap<Integer, Long>();
		
		currentTaskBarID = -1;
		
		prepareDisplay();  // must be first because it is added as viewport to scrollpane
		
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
	
	public boolean hasTasksDisplayed(){
	    
	    return (listOfTasks != null && !listOfTasks.isEmpty());
	}
	
	private void prepareDisplay(){
		
		display = new JTextPane();
		
		display.setHighlighter(null);
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
		tasksList.clear();
		taskNumberList.clear();
	}
	
	public long getCurrentSelectedTaskID(){
	    
	    return currentTaskBarID;
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
	
	public boolean selectTaskRelativeToCurrentSelectedTask( int increments ){
	    
	    if( !listOfTasks.isEmpty() ){
	        
	        long newCurrentTaskBarID = currentTaskBarID + increments;
	        
	        // TODO: May have to check for overflow here
	        
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
	
	public boolean selectPreviousTask(){
		
		if( !listOfTasks.isEmpty() && currentTaskBarID - 1 < listOfTasks.size() ){
		
		    long newCurrentTaskBarID = (currentTaskBarID - 1 < 0 ? listOfTasks.size() - 1 : currentTaskBarID - 1);
		    
			TaskBar tempTaskBar = listOfTasks.get( newCurrentTaskBarID );
			
			if( tempTaskBar != null ){
				
				selectTask( tempTaskBar, newCurrentTaskBarID );
                
				return true;
				
			} else{
				
				return false;
			}
			
		} else{
			
			return false;
		}
	}
	
	public boolean selectNextTask(){
	    
		if( !listOfTasks.isEmpty() && currentTaskBarID + 1 >= 0 ){
			
		    long newCurrentTaskBarID = (currentTaskBarID + 1 >= listOfTasks.size() ? 0 : currentTaskBarID + 1 );
		    
			TaskBar tempTaskBar = listOfTasks.get( newCurrentTaskBarID );
			
			if( tempTaskBar != null ){
				
				selectTask( tempTaskBar, newCurrentTaskBarID );
	            
				return true;
				
			} else{
				
				return false;
			}
			
		} else{
			
			return false;
		}
	}
	
	private void selectTask( TaskBar taskBar, long id ){
		
		if( taskBar != null && id >= 0 && id < listOfTasks.size()  ){
		    
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
				
			} catch( BadLocationException badLocationException ){}
		}
	}
	
	private void setTaskBarParameters( Date currentDate, TaskBar taskBar, DisplayTask task, long lineNumber ){
		
	    Task parentTask;
	    
		if( taskBar != null && task != null ){
		    
		    parentTask = task.getParent();
		    
		    if(parentTask != null){
		        
    			// Set Line number
    			if( lineNumber > 0 ){
    				
    				taskBar.setLineNumber(lineNumber);
    			}
    			
    			// Check if task is done
    			if( parentTask.isDone() ){
    				
    				taskBar.setTaskDone();
    			}
    			
    			// set task name	
    			taskBar.setTaskTitle(parentTask.getName());
    			
    			// Set tags
    			taskBar.setTags(parentTask);
    			
    			// Set due date label
    			taskBar.setTimeDisplayLabel(currentDate, task);
		    }
		}
	}
	
	private void appendString( String string, Style style ){
		
		if( string != null ){
			
			try{
				
				StyledDocument doc = display.getStyledDocument();
				
				doc.insertString(doc.getLength(), string, style );
				
			} catch( BadLocationException badLocationException ){
				
				
			}
		}
	}
	
	public boolean addTaskToDisplay( Date currentDate, DisplayTask task ){
		
		if( task == null || task.getParent() == null ){
			
			return false;
		}
		
		long taskBarID = listOfTasks.size();
		
		TaskBar taskBar = new TaskBar();
		
		setTaskBarParameters( currentDate, taskBar, task, task.getParent().getID() );
		
		taskBar.setPosition( display.getCaretPosition() );
		
		appendString("               ", null);
		
		addComponentToDisplay( taskBar );
		
		listOfTasks.put( taskBarID, taskBar );
		
		tasksList.put(taskBarID, task);
		
		taskNumberList.put(task.getParent().getID(), taskBarID);
		
		jumpList.put(task.getID(), taskBarID);
		
		appendString("\n", null);
		
		selectTask( taskBar, taskBarID );
		
		return true;
	}
	
	public DisplayTask getCurrentSelectedTask(){
	    
	    return tasksList.get(currentTaskBarID);
	}
	
	public boolean selectGivenJumpID( int jumpID ){
	    
	    if( jumpID >= 0 ){
            
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
	
	public boolean selectGivenTaskID( int taskID ){
	    
	    if( taskID >= 0 ){
	        
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
	
	@SuppressWarnings("unused")
    private boolean addTasksToDisplay( Date currentDate, DisplayTaskList taskList ){
		
		if( !addTasksToDisplayWithoutSelection(currentDate, taskList) ){
		    
		    return false;
		}
		
		if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
		    
			selectTask( listOfTasks.get(0L), 0L );
		}
		
		return true;
	}
	
	public int getNumberOfTasksDisplayed(){
	    
	    return listOfTasks.size();
	}
	
	public void showMessageOnDisplay( String msg ){
	    
	    if( msg != null ){
	        
	        clearDisplay();
	        
	        TranslucentTextPane messagePane = new TranslucentTextPane( new Color( 0, 0, 0, 100 ) );
	        messagePane.setEditable(false);
	        messagePane.setHighlighter(null);
	        messagePane.setFocusable(false);
	        messagePane.setFont(new Font( "Arial", Font.BOLD, 20 ));
	        messagePane.setForeground(new Color(255,255,255));
	        
	        messagePane.setBounds(0, 0, display.getWidth(), 100);
	        
	        SimpleAttributeSet newLineStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(newLineStyle, "Arial");
	        StyleConstants.setFontSize(newLineStyle, 2);
	        StyleConstants.setBold(newLineStyle, false);
	        
	        SimpleAttributeSet TextStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(TextStyle, "Arial");
	        StyleConstants.setFontSize(TextStyle, 20);
	        StyleConstants.setBold(TextStyle, true);
	        
	        messagePane.initialiseForResize();
	        messagePane.appendText("\n\n", newLineStyle);
	        messagePane.appendText(msg, TextStyle);
	        messagePane.appendText("\n\n\n", newLineStyle);
	        messagePane.adjustComponentSizeToFitText();
	        
	        addComponentToDisplay(messagePane);
	    }
	}
	
	public boolean addInfoToDisplay( String [][]mappings, int idx ){
	    
	    if( mappings != null ){
	        
	        clearDisplay();
	        
	        SimpleAttributeSet hightlightStyle = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(hightlightStyle, "Arial");
	        StyleConstants.setFontSize(hightlightStyle, 14);
	        StyleConstants.setBold(hightlightStyle, true);
	        StyleConstants.setItalic(hightlightStyle, true);
	        StyleConstants.setForeground(hightlightStyle, Color.GREEN);
	        
	        if( idx >= 0 && idx < mappings.length && mappings[idx].length > 1 ){
                
	            String []wordsToHighlight = null;
	            
	            if( mappings[idx].length > 2 ){
	                
	                wordsToHighlight = Arrays.copyOfRange(mappings[idx], 2, mappings[idx].length);
	            }
	            
	            addInfoToDisplayWithoutSelection( mappings[idx][1], wordsToHighlight, hightlightStyle );
                
	        } else{
	            
	            String []wordsToHighlight;
	            
	            for( int i = 0, size = mappings.length; i < size; ++i ){
	                
	                if( mappings[i].length > 0 ){
	                    
    	                JLabel headerLabel = new JLabel();
    	                headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
    	                headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    	                headerLabel.setForeground(new Color(255,255,255));
    	                headerLabel.setText(" " + mappings[i][0]);
    	                
    	                addComponentToDisplay(headerLabel);
    	                
    	                appendString("\n", null);
    	                
    	                wordsToHighlight = null;
    	                
    	                if( mappings[i].length > 2 ){
    	                    
    	                    wordsToHighlight = Arrays.copyOfRange(mappings[i], 2, mappings[i].length);
    	                }
    	                
    	                addInfoToDisplayWithoutSelection( mappings[i][1], wordsToHighlight, hightlightStyle );
    	                
    	                appendString( "\n\n\n\n\n\n", null );
	                }
	            }
	        }
	        
	        if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
	        
	        display.setCaretPosition(0);
	        
	        return true;
	    }   
	        
	    return false;
	}
	
	private boolean addInfoToDisplayWithoutSelection( String info, String []wordsToHighlight, SimpleAttributeSet style ){
	    
	    if( info == null ){
	        
	        return false;
	    }
	    
	    SimpleAttributeSet newLineStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(newLineStyle, "Arial");
        StyleConstants.setFontSize(newLineStyle, 2);
        StyleConstants.setBold(newLineStyle, false);
        
        SimpleAttributeSet TextStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(TextStyle, "Arial");
        StyleConstants.setFontSize(TextStyle, 14);
        StyleConstants.setBold(TextStyle, true);
	    
        TranslucentTextPane messagePane = new TranslucentTextPane( new Color( 0, 0, 0, 100 ) );
        messagePane.setEditable(false);
        messagePane.setHighlighter(null);
        messagePane.setFocusable(false);
        messagePane.setFont(new Font( "Arial", Font.BOLD, 20 ));
        messagePane.setForeground(new Color(255,255,255));
        
        messagePane.setBounds(0, 0, display.getWidth(), 100);
        
        messagePane.initialiseForResize();
        
        messagePane.appendText("\n\n", newLineStyle);
        messagePane.appendText(info, TextStyle);
        messagePane.appendText("\n\n\n", newLineStyle);
        messagePane.adjustComponentSizeToFitText();
        
        addComponentToDisplay(messagePane);
        
        if( wordsToHighlight != null && wordsToHighlight.length > 0 ){
            
            messagePane.highlightWords(wordsToHighlight, style);
        }
        
        return true;
	}
	
	private boolean addTaskWithHeaderToDisplayWithoutSelection( Date currentDate, DisplayTask task, JLabel header ){
	    
	    if( task == null || task.getParent() == null ){
            
            return false;
        }
	    
	    long taskBarID = listOfTasks.size();
	    
        TaskBar taskBar = new TaskBar(header);
        
        setTaskBarParameters( currentDate, taskBar, task, task.getParent().getID() );
        
        taskBar.setPosition( display.getCaretPosition() );
        
        appendString("               ", null);
        
        addComponentToDisplay( taskBar );
        
        listOfTasks.put( taskBarID, taskBar );
        
        tasksList.put(taskBarID, task);
        
        taskNumberList.put(task.getParent().getID(), taskBarID);
        
        jumpList.put(task.getID(), taskBarID);
        
        appendString("\n", null);
        
        return true;
	}
	
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
            
                appendString("               ", null);
                
                tempTaskBar = new TaskBar();
                
                setTaskBarParameters( currentDate, tempTaskBar, currentTask, currentTask.getParent().getID() );
    
                tempTaskBar.setPosition( display.getCaretPosition() );
                
                addComponentToDisplay( tempTaskBar );
                
                idxForCurrentTaskBar = listOfTasks.size();
                
                listOfTasks.put( idxForCurrentTaskBar, tempTaskBar );
                
                tasksList.put(idxForCurrentTaskBar, currentTask);
                
                taskNumberList.put( currentTask.getParent().getID(), idxForCurrentTaskBar );
                
                jumpList.put(currentTask.getID(), idxForCurrentTaskBar);
                
                appendString("\n", null);
            }
        }
        
        return true;
	}
	
	public InvisibleButton getUpButtonComponent(){
        
        return (scrollBarSkin != null ? scrollBarSkin.getUpButtonComponent() : null);
    }
    
    public InvisibleButton getDownButtonComponent(){
        
        return (scrollBarSkin != null ? scrollBarSkin.getDownButtonComponent() : null);
    }
    
    public JTextPane getDisplayComponent(){
        
        return display;
    }
    
    public void displayByPriority( Set<Map.Entry<Integer, DisplayTaskList>> displayList ){
        
        if( displayList != null ){
            
            Integer priority;
            DisplayTaskList tempTaskList = null;
            
            JLabel dateHeaderLabel;
            
            DisplayTask tempDisplayTask;
            
            // Iterate through treemap
            for( Map.Entry<Integer, DisplayTaskList> entry : displayList ){
                
                tempTaskList = entry.getValue();
                
                if( tempTaskList != null && tempTaskList.size() > 0 ){
                    
                    priority = entry.getKey();
                    
                    dateHeaderLabel = new JLabel();
                    dateHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    dateHeaderLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    dateHeaderLabel.setForeground(new Color(255,255,255));
                    
                    if( priority != null && priority >= 1 && priority <= 5 ){
                        
                        dateHeaderLabel.setText( "Priority " + priority );
                        
                    } else{
                        
                        dateHeaderLabel.setText( "No Priority" );
                    }
                    
                    dateHeaderLabel.setPreferredSize(new Dimension( 50, 100 ));
                    dateHeaderLabel.setMaximumSize(new Dimension( 50, 100 ));
                    dateHeaderLabel.setMinimumSize(new Dimension( 50, 100 ));
                    dateHeaderLabel.setSize(50, 100);
                    
                    tempDisplayTask = tempTaskList.get(0);
                    
                    addTaskWithHeaderToDisplayWithoutSelection( null, tempDisplayTask, dateHeaderLabel );
                    
                    tempTaskList.set(0, null);
                    
                    addTasksToDisplayWithoutSelection( null, tempTaskList );
                    
                    tempTaskList.set(0, tempDisplayTask);
                }
                
                appendString( "\n\n\n\n\n\n", null );
            }
            
            if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
        }
        
        if( !hasTasksDisplayed() ){
            
            showMessageOnDisplay( " You have no tasks for this section :)" );
        }
    }
    
    public void displayByDate( Set<Map.Entry<Date, DisplayTaskList>> displayList ){
        
        if( displayList != null ){
            
            Date tempDate;
            DisplayTaskList tempTaskList = null;
            
            Date todayDate = new Date(System.currentTimeMillis());
            
            JLabel dateHeaderLabel;
            
            DisplayTask tempDisplayTask;
            
            SimpleDateFormat dateFormatterForDisplay = new SimpleDateFormat( "EEE, d MMM yyyy" );
            
            // Iterate through treemap
            for( Map.Entry<Date, DisplayTaskList> entry : displayList ){
                
                tempTaskList = entry.getValue();
                
                if( tempTaskList != null && tempTaskList.size() > 0 ){
                    
                    tempDate = entry.getKey();
                    
                    dateHeaderLabel = new JLabel();
                    dateHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    dateHeaderLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    dateHeaderLabel.setForeground(new Color(255,255,255));
                    
                    if( tempDate != null ){
                        
                        if( !TaskBar.compareByDateOnly(tempDate, todayDate) ){
                            
                            dateHeaderLabel.setText(dateFormatterForDisplay.format(tempDate));
                            
                        } else{
                            
                            dateHeaderLabel.setText("Today");
                        }
                        
                    } else{
                        
                        dateHeaderLabel.setText( "Floating Tasks" );
                    }
                    
                    tempDisplayTask = tempTaskList.get(0);
                    
                    addTaskWithHeaderToDisplayWithoutSelection( tempDate, tempDisplayTask, dateHeaderLabel );
                    
                    tempTaskList.set(0, null);
                    
                    addTasksToDisplayWithoutSelection( tempDate, tempTaskList );
                    
                    tempTaskList.set(0, tempDisplayTask);
                }
                
                appendString( "\n\n\n\n\n\n", null );
            }
            
            if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
        }
        
        if( !hasTasksDisplayed() ){
            
            showMessageOnDisplay( " You have no tasks for this section :)" );
        }
    }
}
