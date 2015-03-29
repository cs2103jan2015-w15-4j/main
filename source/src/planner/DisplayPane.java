package planner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

public class DisplayPane extends JScrollPane{

	private JTextPane display;
	
	private TaskBar currentTaskBar;
	
	private long currentTaskBarID;
	
	private TreeMap<Long, TaskBar> listOfTasks;
	private TreeMap<Long, Task> tasksList;
	private TreeMap<Long, Long> taskNumberList;
	
	private CustomScrollBarUI scrollBarSkin;
	
	public DisplayPane(){
		
		listOfTasks = new TreeMap<Long, TaskBar>();
		tasksList = new TreeMap<Long, Task>();
		taskNumberList = new TreeMap<Long, Long>();
		
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
			
			// Set tags
			taskBar.setTags(task);
			
			// Set due date label
			taskBar.setTimeDisplayLabel(task);
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
	
	public boolean addTaskToDisplay( Task task ){
		
		if( task == null ){
			
			return false;
		}
		
		long taskBarID = listOfTasks.size();
		
		appendString("               ", null);
		
		TaskBar taskBar = new TaskBar();
		
		//setTaskBarParameters( taskBar, task, taskBarID + 1 );
		
		setTaskBarParameters( taskBar, task, task.getID() );
		
		taskBar.setPosition( display.getCaretPosition() );
		
		addComponentToDisplay( taskBar );
		
		listOfTasks.put( taskBarID, taskBar );
		
		tasksList.put(taskBarID, task);
		
		taskNumberList.put(task.getID(), taskBarID);
		
		appendString("\n", null);
		
		selectTask( taskBar, taskBarID );
		
		return true;
	}
	
	public Task getCurrentSelectedTask(){
	    
	    return tasksList.get(currentTaskBarID);
	}
	
	public boolean selectGivenTask( Task task ){
	    
	    if( task != null ){
	        
	        Long internalID = taskNumberList.get(task.getID());
	        
	        if( internalID != null ){
	            
	            TaskBar tempTaskBar = listOfTasks.get(internalID);
	            
	            if(tempTaskBar != null){
	                
	                selectTask( tempTaskBar, internalID );
	                
	                return true;
	                
	            } else{
	                
	                return false;
	            }
	            
	        } else{
	            
	            return false;
	        }
	        
	    } else{
	        
	        return false;
	    }
	}
	
	public boolean addTasksToDisplay( TaskList taskList ){
		
		if( !addTasksToDisplayWithoutSelection(taskList) ){
		    
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
	
	private boolean addTasksToDisplayWithoutSelection( TaskList taskList ){
	    
	    if( taskList == null ){
            
            return false;
        }
        
        ListIterator<Task> iterator = taskList.listIterator();
        Task currentTask;
        
        display.setCaretPosition(display.getStyledDocument().getLength());
        
        long idxForCurrentTaskBar;
        while( iterator.hasNext() ){
            
            appendString("               ", null);
            
            currentTask = iterator.next();
            
            //System.out.println(currentTask.getID());
            
            currentTaskBar = new TaskBar();
            
            //setTaskBarParameters( currentTaskBar, currentTask, count + 1 );
            
            setTaskBarParameters( currentTaskBar, currentTask, currentTask.getID() );

            currentTaskBar.setPosition( display.getCaretPosition() );
            
            //System.out.println( "Position of " + currentTask.getName() + " = " + display.getCaretPosition() );
            
            addComponentToDisplay( currentTaskBar );
            
            idxForCurrentTaskBar = listOfTasks.size();
            
            listOfTasks.put( idxForCurrentTaskBar, currentTaskBar );
            
            tasksList.put(idxForCurrentTaskBar, currentTask);
            
            taskNumberList.put( currentTask.getID(), idxForCurrentTaskBar );
            
            appendString("\n", null);
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
    
    public void displayByDate( Set<Map.Entry<Date, TaskList>> displayList ){
        
        if( displayList != null ){
            
            Style biggerTextStyle = display.addStyle("biggerText", null);
            StyleConstants.setFontFamily(biggerTextStyle, "Arial");
            StyleConstants.setBold(biggerTextStyle, true);
            StyleConstants.setFontSize(biggerTextStyle, 16);
            StyleConstants.setForeground(biggerTextStyle, new Color(255,255,255));
            
            Date tempDate;
            TaskList tempTaskList = null;
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
            
            String todayDateStr = dateFormatter.format(new Date(System.currentTimeMillis()));
            
            // Iterate through treemap
            for( Map.Entry<Date, TaskList> entry : displayList ){
                
                tempTaskList = entry.getValue();
                
                if( tempTaskList == null ){
                    
                    continue;
                }
                
                Collections.sort( tempTaskList, new Comparator<Task>(){

                    @Override
                    public int compare(Task taskOne, Task taskTwo) {
                        
                        Date firstDate = taskOne.getDueDate();
                        Date secondDate = taskTwo.getDueDate();
                        Date firstEndDate = taskOne.getEndDate();
                        Date secondEndDate = taskTwo.getEndDate();
                        
                        if( firstDate != null && secondDate != null ){
                            
                            return firstDate.compareTo(secondDate);
                            
                        } else if( firstDate != null && secondEndDate != null ){
                            
                            return firstDate.compareTo(secondEndDate);
                            
                        } else if( firstDate != null && secondEndDate == null ){
                            
                            return 1;
                            
                        } else if( secondDate != null && firstEndDate != null ){
                            
                            return firstEndDate.compareTo(secondDate);
                            
                        } else if( secondDate != null && firstEndDate == null ){
                            
                            return -1;
                            
                        } else if( firstEndDate != null && secondEndDate != null ){
                            
                            return firstEndDate.compareTo(secondEndDate);
                            
                        } else if( firstEndDate != null ){
                            
                            return 1;
                            
                        } else if( secondEndDate != null ){
                            
                            return -1;
                            
                        } else{
                            
                            return 0;
                        }
                    }
                    
                });
                
                tempDate = entry.getKey();
                
                SimpleDateFormat dateFormatterForDisplay = new SimpleDateFormat( "EEE, d MMM yyyy" );
                
                int positionOfDateHeader = display.getCaretPosition();
                
                appendString("                  ", null);
                
                if( tempDate != null ){
                    
                    String dateStr = dateFormatter.format( tempDate );
                    
                    if( !dateStr.equals(todayDateStr) ){
                        
                        dateStr = dateFormatterForDisplay.format(tempDate);
                        
                        appendString(dateStr, biggerTextStyle);
                        
                    } else{
                        
                        appendString("Today", biggerTextStyle);
                    }
                    
                } else{
                    
                    appendString("Floating tasks", biggerTextStyle);
                }
                
                appendString("\n", null);
                
                long idxOfFirstTaskUnderNewDateHeader = listOfTasks.size();
                
                addTasksToDisplayWithoutSelection(tempTaskList);
              
                TaskBar tempTaskBar = listOfTasks.get(idxOfFirstTaskUnderNewDateHeader);

                if( tempTaskBar != null ){
                    
                    tempTaskBar.setPosition(positionOfDateHeader);
                }
                
                appendString( "\n\n\n\n\n\n", null );
            }
            
            if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
        }
    }
    
    private Set<Map.Entry<Date, TaskList>> outdatedTasks;
    private Set<Map.Entry<Date, TaskList>> upcomingTasks;
    
    public void getSpecialTasks( TaskList allTaskList ){
        
        if( allTaskList != null ){
            
            outdatedTasks = null;
            upcomingTasks = null;
            
            TreeMap<Date, TaskList> displayList = new TreeMap<Date, TaskList>( new Comparator<Date>(){

                @Override
                public int compare(Date firstDate, Date secondDate) {
                    
                    if( firstDate != null && secondDate != null ){
                        
                        SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
                        
                        String dateOneString = dateFormatter.format(firstDate);
                        String dateTwoString = dateFormatter.format(secondDate);
                        
                        if( dateOneString.equals(dateTwoString) ){
                            
                            return 0;
                            
                        } else{
                            
                            return firstDate.compareTo(secondDate);
                        }
                        
                    } else if( firstDate != null ){
                        
                        return -1;
                        
                    } else if( secondDate != null ){
                        
                        return 1;
                        
                    } else{
                        
                        return 0;
                    }
                }
            });
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
            
            Iterator<Task> iterator = allTaskList.listIterator();
            
            Task tempTask;
            Date tempDate;
            Date endDate;
            TaskList tempTaskList = null;
            
            Calendar calendar = Calendar.getInstance();
            
            while( iterator.hasNext() ){
                
                tempTask = iterator.next();
                
                if( !tempTask.isFloating() ){
                    
                    tempDate = tempTask.getDueDate();
                    endDate = tempTask.getEndDate();
                    
                    if( tempDate != null ){
                        
                        if( endDate != null ){
                            
                            if( endDate.compareTo(tempDate) >= 0 ){
                                
                                String startDateString = dateFormatter.format(tempDate);
                                String endDateString = dateFormatter.format(endDate);
                                
                                long count = 0;
                                while( !startDateString.equals(endDateString) ){
                                    
                                    tempTaskList = displayList.get( tempDate );
                                    
                                    if( count > 0 ){
                                        
                                        tempTask = new Task(tempTask);
                                        tempTask.setDueDate(null);
                                        tempTask.setEndDate(tempDate);
                                    }
                                    
                                    if( tempTaskList != null ){
                                        
                                        tempTaskList.add(tempTask);
                                     
                                    } else{
                                        
                                        tempTaskList = new TaskList();
                                        tempTaskList.add(tempTask);
                                        displayList.put( tempDate, tempTaskList );
                                    }
                                    
                                    calendar.setTime(tempDate);
                                    calendar.add(Calendar.DATE, 1);
                                    tempDate = new Date(calendar.getTime().getTime());
                                    startDateString = dateFormatter.format(tempDate);
                                    
                                    ++count;
                                }
                            }
                            
                        } else{
                            
                            tempTaskList = displayList.get( tempDate );
                            
                            if( tempTaskList != null ){
                                
                                tempTaskList.add(tempTask);
                             
                            } else{
                                
                                tempTaskList = new TaskList();
                                tempTaskList.add(tempTask);
                                displayList.put( tempDate, tempTaskList );
                            }
                        }
                    }
                    
                }
            }
            
            // Iterate through treemap
            for( Map.Entry<Date, TaskList> entry : displayList.entrySet() ){
                
                tempTaskList = entry.getValue();
                
                if( tempTaskList == null ){
                    
                    continue;
                }
                
                Collections.sort( tempTaskList, new Comparator<Task>(){

                    @Override
                    public int compare(Task taskOne, Task taskTwo) {
                        
                        Date firstDate = taskOne.getDueDate();
                        Date secondDate = taskTwo.getDueDate();
                        Date firstEndDate = taskOne.getEndDate();
                        Date secondEndDate = taskTwo.getEndDate();
                        
                        if( firstDate != null && secondDate != null ){
                            
                            return firstDate.compareTo(secondDate);
                            
                        } else if( firstDate != null && secondEndDate != null ){
                            
                            return firstDate.compareTo(secondEndDate);
                            
                        } else if( firstDate != null && secondEndDate == null ){
                            
                            return 1;
                            
                        } else if( secondDate != null && firstEndDate != null ){
                            
                            return firstEndDate.compareTo(secondDate);
                            
                        } else if( secondDate != null && firstEndDate == null ){
                            
                            return -1;
                            
                        } else if( firstEndDate != null && secondEndDate != null ){
                            
                            return firstEndDate.compareTo(secondEndDate);
                            
                        } else if( firstEndDate != null ){
                            
                            return 1;
                            
                        } else if( secondEndDate != null ){
                            
                            return -1;
                            
                        } else{
                            
                            return 0;
                        }
                    }
                    
                });
            }
        }
    }
    
    public void displayByPriority( TaskList taskList ){
        
        if( taskList != null ){
           
            ArrayList<TaskList> displayList = new ArrayList<TaskList>();
            
            for( int i = 0; i < 6; ++i ){
                
                displayList.add(new TaskList());
            }
            
            Iterator<Task> iterator = taskList.listIterator();
            
            Task tempTask;
            TaskList tempTaskList;
            int currentTaskPriority;
            
            while( iterator.hasNext() ){
                
                tempTask = iterator.next();
                
                if( tempTask != null ){
                    
                    currentTaskPriority = ((tempTask.getPriority() >= 1 && tempTask.getPriority() <= 5) ? tempTask.getPriority() : 0);
                    
                    tempTaskList = displayList.get(currentTaskPriority);
                        
                    tempTaskList.add(tempTask);
                }
            }
            
            Style biggerTextStyle = display.addStyle("biggerText", null);
            StyleConstants.setFontFamily(biggerTextStyle, "Arial");
            StyleConstants.setBold(biggerTextStyle, true);
            StyleConstants.setFontSize(biggerTextStyle, 16);
            StyleConstants.setForeground(biggerTextStyle, new Color(255,255,255));
            
            for( int i = 5; i >= 0; --i ){
                
                tempTaskList = displayList.get(i);
                
                if( tempTaskList == null || tempTaskList.size() <= 0 ){
                    
                    continue;
                }
                
                Collections.sort( tempTaskList, new Comparator<Task>(){

                    @Override
                    public int compare(Task taskOne, Task taskTwo) {
                        
                        Date createDateForTaskOne = taskOne.getCreatedDate();
                        Date createDateForTaskTwo = taskTwo.getCreatedDate();
                        
                        if( createDateForTaskOne != null && createDateForTaskTwo != null ){
                            
                            return createDateForTaskOne.compareTo(createDateForTaskTwo );
                            
                        } else if( createDateForTaskOne != null ){
                            
                            return -1;
                            
                        } else if( createDateForTaskTwo != null ){
                            
                            return 1;
                            
                        } else{
                            
                            return 0;
                        }
                    }
                    
                });
                
                int positionOfPriorityHeader = display.getCaretPosition();
                
                appendString("                  ", null);
                
                if( i > 0 ){
                    
                    appendString("Priority " + i, biggerTextStyle);
                    
                } else{
                    
                    appendString("No Priority", biggerTextStyle);
                }
                
                appendString("\n", null);
                
                long idxOfFirstTaskUnderNewDateHeader = listOfTasks.size();
                
                addTasksToDisplayWithoutSelection(tempTaskList);
              
                TaskBar tempTaskBar = listOfTasks.get(idxOfFirstTaskUnderNewDateHeader);

                if( tempTaskBar != null ){
                    
                    tempTaskBar.setPosition(positionOfPriorityHeader);
                }
                
                appendString( "\n\n\n\n\n\n", null );
            }
            
            if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
        }
    }
    
    public void displayByDate( TaskList taskList ){
        
        if( taskList != null ){
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
            
            String todayDateStr = dateFormatter.format(new Date(System.currentTimeMillis()));
            
            TreeMap<Date, TaskList> displayList = new TreeMap<Date, TaskList>( new Comparator<Date>(){

                @Override
                public int compare(Date firstDate, Date secondDate) {
                    
                    if( firstDate != null && secondDate != null ){
                        
                        SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
                        
                        String dateOneString = dateFormatter.format(firstDate);
                        String dateTwoString = dateFormatter.format(secondDate);
                        
                        if( dateOneString.equals(dateTwoString) ){
                            
                            return 0;
                            
                        } else{
                            
                            return firstDate.compareTo(secondDate);
                        }
                        
                    } else if( firstDate != null ){
                        
                        return -1;
                        
                    } else if( secondDate != null ){
                        
                        return 1;
                        
                    } else{
                        
                        return 0;
                    }
                }
            });
            
            Iterator<Task> iterator = taskList.listIterator();
            
            Task tempTask;
            Date tempDate;
            Date endDate;
            TaskList tempTaskList = null;
            
            Calendar calendar = Calendar.getInstance();
            
            while( iterator.hasNext() ){
                
                tempTask = iterator.next();
                
                if( !tempTask.isFloating() ){
                    
                    tempDate = tempTask.getDueDate();
                    endDate = tempTask.getEndDate();
                    
                    if( tempDate != null ){
                        
                        if( endDate != null ){
                            
                            if( endDate.compareTo(tempDate) >= 0 ){
                                
                                String startDateString = dateFormatter.format(tempDate);
                                String endDateString = dateFormatter.format(endDate);
                                
                                long count = 0;
                                while( !startDateString.equals(endDateString) ){
                                    
                                    tempTaskList = displayList.get( tempDate );
                                    
                                    if( count > 0 ){
                                        
                                        tempTask = new Task(tempTask);
                                        tempTask.setDueDate(null);
                                        tempTask.setEndDate(tempDate);
                                    }
                                    
                                    if( tempTaskList != null ){
                                        
                                        tempTaskList.add(tempTask);
                                     
                                    } else{
                                        
                                        tempTaskList = new TaskList();
                                        tempTaskList.add(tempTask);
                                        displayList.put( tempDate, tempTaskList );
                                    }
                                    
                                    calendar.setTime(tempDate);
                                    calendar.add(Calendar.DATE, 1);
                                    tempDate = new Date(calendar.getTime().getTime());
                                    startDateString = dateFormatter.format(tempDate);
                                    
                                    ++count;
                                }
                            }
                            
                        } else{
                            
                            tempTaskList = displayList.get( tempDate );
                            
                            if( tempTaskList != null ){
                                
                                tempTaskList.add(tempTask);
                             
                            } else{
                                
                                tempTaskList = new TaskList();
                                tempTaskList.add(tempTask);
                                displayList.put( tempDate, tempTaskList );
                            }
                        }
                    }
                    
                } else{
                    
                    tempTaskList = displayList.get(null);
                    
                    if( tempTaskList != null ){
                        
                        tempTaskList.add(tempTask);
                        
                    } else{
                        
                        tempTaskList = new TaskList();
                        tempTaskList.add(tempTask);
                        displayList.put(null, tempTaskList);
                    }
                }
            }
            
            Style biggerTextStyle = display.addStyle("biggerText", null);
            StyleConstants.setFontFamily(biggerTextStyle, "Arial");
            StyleConstants.setBold(biggerTextStyle, true);
            StyleConstants.setFontSize(biggerTextStyle, 16);
            StyleConstants.setForeground(biggerTextStyle, new Color(255,255,255));
            
            // Iterate through treemap
            for( Map.Entry<Date, TaskList> entry : displayList.entrySet() ){
                
                tempTaskList = entry.getValue();
                
                if( tempTaskList == null ){
                    
                    continue;
                }
                
                Collections.sort( tempTaskList, new Comparator<Task>(){

                    @Override
                    public int compare(Task taskOne, Task taskTwo) {
                        
                        Date firstDate = taskOne.getDueDate();
                        Date secondDate = taskTwo.getDueDate();
                        Date firstEndDate = taskOne.getEndDate();
                        Date secondEndDate = taskTwo.getEndDate();
                        
                        if( firstDate != null && secondDate != null ){
                            
                            return firstDate.compareTo(secondDate);
                            
                        } else if( firstDate != null && secondEndDate != null ){
                            
                            return firstDate.compareTo(secondEndDate);
                            
                        } else if( firstDate != null && secondEndDate == null ){
                            
                            return 1;
                            
                        } else if( secondDate != null && firstEndDate != null ){
                            
                            return firstEndDate.compareTo(secondDate);
                            
                        } else if( secondDate != null && firstEndDate == null ){
                            
                            return -1;
                            
                        } else if( firstEndDate != null && secondEndDate != null ){
                            
                            return firstEndDate.compareTo(secondEndDate);
                            
                        } else if( firstEndDate != null ){
                            
                            return 1;
                            
                        } else if( secondEndDate != null ){
                            
                            return -1;
                            
                        } else{
                            
                            return 0;
                        }
                    }
                    
                });
                
                tempDate = entry.getKey();
                
                SimpleDateFormat dateFormatterForDisplay = new SimpleDateFormat( "EEE, d MMM yyyy" );
                
                int positionOfDateHeader = display.getCaretPosition();
                
                appendString("                  ", null);
                
                if( tempDate != null ){
                    
                    String dateStr = dateFormatter.format( tempDate );
                    
                    if( !dateStr.equals(todayDateStr) ){
                        
                        dateStr = dateFormatterForDisplay.format(tempDate);
                        
                        appendString(dateStr, biggerTextStyle);
                        
                    } else{
                        
                        appendString("Today", biggerTextStyle);
                    }
                    
                } else{
                    
                    appendString("Floating tasks", biggerTextStyle);
                }
                
                appendString("\n", null);
                
                long idxOfFirstTaskUnderNewDateHeader = listOfTasks.size();
                
                addTasksToDisplayWithoutSelection(tempTaskList);
              
                TaskBar tempTaskBar = listOfTasks.get(idxOfFirstTaskUnderNewDateHeader);

                if( tempTaskBar != null ){
                    
                    tempTaskBar.setPosition(positionOfDateHeader);
                }
                
                appendString( "\n\n\n\n\n\n", null );
            }
            
            if( !listOfTasks.isEmpty() && currentTaskBarID != 0L ){
                
                selectTask( listOfTasks.get(0L), 0L );
            }
        }
    }
}
