//@author A0111333B

package planner;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TaskBar extends JComponent {

    private Insets componentCoordinates;
	
	private final int m_width = 597;
	private final int m_height = 50;
	
	private int heightOfLabel;
	
	private JLabel taskBarBackground;
	private JLabel taskCheckBox;
	private JLabel lineNumberLabel;
	private JLabel timeDisplayLabel;
	private JLabel timeDisplayLabelEx;
	private JLabel dateHeader;
	
	private JTextPane tagsPane;
	private JTextPane infoPane;
	
	private FadedTextField taskTitleLabel;
	
	private int position;
	
	private boolean hasMovedUp;
	private boolean hasMovedDown;
	
	// Helper functions
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(m_width, m_height+heightOfLabel);
	}
	
	private void removeMouseListener( Component component ){
        
        if( component != null ){
            
            component.addMouseListener( new MouseAdapter(){

                @Override
                public void mouseClicked(MouseEvent arg0) {}
            });
        }
    }
	// End of helper functions
	
	public TaskBar() {
	    
		this(0);
	}
	
	public TaskBar( int position ){
		
	    this(null, position);
	}
	
	public TaskBar( JLabel newDateHeader, int position ){
	    
	    this.position = position >= 0 ? position : 0;
	    
	    dateHeader = newDateHeader;
	    componentCoordinates = getInsets();
	    
        if( dateHeader != null ){
            
            //heightOfLabel = newDateHeader.getHeight();
            
            heightOfLabel = 20;
            
            //dateHeader.getText();
            dateHeader.setBounds(componentCoordinates.left, componentCoordinates.top, m_width, heightOfLabel);
            dateHeader.setPreferredSize(new Dimension(m_width, heightOfLabel));
            add(dateHeader);
            
        } else{
            
            System.out.println("went here");
            
            heightOfLabel = 0;
        }
        
        setSize(m_width, m_height+heightOfLabel);
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setLayout(null);
        
        prepareTaskCheckBox();
        prepareTaskTitleLabel();
        prepareLineNumberLabel();
        prepareTagsPane();
        prepareTimeDisplayLabel();
        prepareTaskBarBackground();
	}
	
	public TaskBar( JLabel newTitleHeader, String info ){
	    
	    this.position = position >= 0 ? position : 0;
        
        dateHeader = newTitleHeader;
        componentCoordinates = getInsets();
        
        if( dateHeader != null ){
            
            //heightOfLabel = newDateHeader.getHeight();
            
            heightOfLabel = 20;
            
            //dateHeader.getText();
            dateHeader.setBounds(componentCoordinates.left, componentCoordinates.top, m_width, heightOfLabel);
            dateHeader.setPreferredSize(new Dimension(m_width, heightOfLabel));
            add(dateHeader);
            
        } else{
            
            System.out.println("went here");
            
            heightOfLabel = 0;
        }
        
        setSize(m_width, m_height+heightOfLabel);
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setLayout(null);
        
        prepareInfoPane(info);
        prepareTaskBarBackground();
	}
	
	private void prepareInfoPane( String info ){
        
        infoPane = new JTextPane();
        
        infoPane.setBounds(40, 13+heightOfLabel, 330, 20);
        infoPane.setFocusable(false);
        infoPane.setEditable(false);
        infoPane.setHighlighter(null);
        infoPane.setOpaque(false);
        infoPane.setFont(new Font( "Arial", Font.BOLD, 14));
        infoPane.setForeground( new Color( 255,255,255 ) );
        add(infoPane);
        
        Style tempStyle = infoPane.addStyle("Original", null);
        StyleConstants.setFontFamily(tempStyle, "Arial");
        StyleConstants.setFontSize(tempStyle, 14);
        StyleConstants.setBold(tempStyle, true);
        StyleConstants.setForeground(tempStyle, new Color( 255,255,255 ));
        
        infoPane.setText(info);
    }
	
	public TaskBar( JLabel newDateHeader ){
	   
	    this( newDateHeader, 0 );
	}

	public int getPosition(){
		
		return position;
	}
	
	public void setPosition( int position ){
		
		this.position = position;
	}

	// Check box functions
	private void prepareTaskCheckBox(){
		
		taskCheckBox = new JLabel();
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/NotDoneCheckBox.png")));
		taskCheckBox.setFocusable(false);
		taskCheckBox.setBounds(562, 12+heightOfLabel, 26, 26);
		add(taskCheckBox);
		removeMouseListener(taskCheckBox);
	}
	
	public void setTaskDone(){
		
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/DoneCheckBox.png")));
	}
	
	public void setTaskNotDone(){
		
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/NotDoneCheckBox.png")));
	}
	
	public void setTaskHalfDone(){
		
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/HalfDoneCheckBox.png")));
	}
	
	// Title label functions
	private void prepareTaskTitleLabel(){
		
		taskTitleLabel = new FadedTextField(new Color(255,255,255), new Color(0,0,0,0), new Color(0,0,0,0), 45, 45);
		taskTitleLabel.setBounds(60, 17+heightOfLabel, 330, 20);
		taskTitleLabel.setFocusable(false);
		taskTitleLabel.setFont(new Font( "Arial", Font.BOLD, 14));
		add(taskTitleLabel);
		taskTitleLabel.setText("This is a sample text testing one two three four five");
		
		hasMovedDown = true;
		hasMovedUp = false;
	}
	
	public void setTaskTitle( String taskTitle ){
		
		if( taskTitle != null ){
			
			taskTitleLabel.setText(taskTitle);
		}
	}
	
	private void moveTitleLabelVertically( int relativeIncrements ){
	    
	    Point currentLocation = taskTitleLabel.getLocation();
	    
	    if( currentLocation != null ){
	        
	        taskTitleLabel.setLocation(currentLocation.x, currentLocation.y + relativeIncrements);
	    }
	}
	
	// Line number label functions
	private void prepareLineNumberLabel(){
		
		lineNumberLabel = new JLabel("#11111");
		lineNumberLabel.setBounds(5, 3+heightOfLabel, 50, 45);
		lineNumberLabel.setForeground(new Color( 255,255,255 ));
		lineNumberLabel.setFont(new Font( "Arial", Font.BOLD, 12 ));
		lineNumberLabel.setFocusable(false);
		lineNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lineNumberLabel);
		removeMouseListener(lineNumberLabel);
	}
	
	public void setLineNumber( long lineNumber ){
		
		if( lineNumber > 0  ){
			
			lineNumberLabel.setText("#"+lineNumber);
		}
	}
	
	private void prepareTagsPane(){
	    
	    tagsPane = new JTextPane();
        tagsPane.setBounds(56, 25+heightOfLabel, 335, 17);
        add(tagsPane);
        tagsPane.setEditable(false);
        tagsPane.setHighlighter(null);
        tagsPane.setFocusable(false);
        tagsPane.setOpaque(false);
        tagsPane.setFont( new Font( "Arial", Font.BOLD, 12 ) );
	}
	
	private Color getPriorityColor( Color []colors, int priority, boolean []colorsToExclude ){
	    
	    if( colors != null && 
	        colors.length >= 5 &&
	        colorsToExclude != null &&
	        colorsToExclude.length >= 5){
	        
	        switch( priority ){
	        
	            case 5:
	            case 4:
	            case 3:
	            case 2:
	                
	                if( colors[5-priority] != null ){
	                    
	                    colorsToExclude[5-priority] = true;
	                    
	                    return colors[5-priority];
	                    
	                } else{
	                    
	                    return new Color( 255, 255, 255 );
	                }
	                
	            default:

	                if( colors[4] != null ){
                        
	                    colorsToExclude[4] = true;
	                    
                        return colors[4];
                        
                    } else{
                        
                        return new Color( 255, 255, 255 );
                    }
	        }
	        
	    } else{
	        
	        return new Color( 255, 255, 255 );
	    }
	}
	
	private void appendColoredStringsToDisplay( StyledDocument doc, Style style, Color color, String str ){
	    
	    if( doc != null && style != null && color != null && str != null){
	        
	        try{
	            
	            StyleConstants.setForeground(style, color);
                doc.insertString(doc.getLength(), str, style);
                
	        } catch( BadLocationException badLocationException ){}
	    }
	}
	
	private Color getRandomColor( Color []colors, boolean []colorsToExclude ){
	    
	    if( colors != null && 
	        colorsToExclude != null && 
	        colors.length == colorsToExclude.length ){
	        
	        Random random = new Random( System.nanoTime() );
	        
	        int numOfColors = colors.length;
	        
	        int selectedColorIdx;
	        
	        boolean allColorsUnavaliable = true;
	        for( int i = 0, size = colorsToExclude.length; i < size; ++i ){
	            
	            if( !colorsToExclude[i] ){
	                
	                allColorsUnavaliable = false;
	                break;
	            }
	        }
	        
	        if( allColorsUnavaliable ){
	            
	            return new Color( 255, 255, 255 );
	        }
	        
	        while( colorsToExclude[selectedColorIdx = random.nextInt(numOfColors)] );
	        
	        if( colors[selectedColorIdx] != null ){
	            
	            return colors[selectedColorIdx];
	            
	        } else{
	            
	            return new Color( 255, 255, 255 );
	        }
	        
	    } else{
	        
	        return new Color( 255, 255, 255 );
	    }
	}
	
	public void setTags( Task task ){
	    
	    if( task != null && planner.Constants.COLOR_SERIES != null ){
	        
	        boolean hasTags = false;
	        boolean previousTextExist = false;
	        
	        StyledDocument doc = tagsPane.getStyledDocument();
	        Style colorStyle = tagsPane.addStyle("color", null);
	        StyleConstants.setItalic(colorStyle, true);
	        
	        boolean []colorsToExclude = new boolean[planner.Constants.COLOR_SERIES.length];
	        
	        if( task.getPriority() > 0 ){
	            
	            int priority = task.getPriority();
	            priority = (priority <= 5 ? priority : 5);
	            
	            appendColoredStringsToDisplay( doc, 
	                                           colorStyle, 
	                                           getPriorityColor(planner.Constants.COLOR_SERIES, priority, colorsToExclude), 
	                                           "priority " + priority + " " );
	            
	            previousTextExist = true;
	            
	            hasTags = true;
	        }
	        
	        if( task.getTag() != null && task.getTag().length() > 0 ){
	            
	            String divider = (previousTextExist ? "/ " : "");
	            
	            appendColoredStringsToDisplay( doc, 
	                                           colorStyle, 
	                                           getRandomColor(planner.Constants.COLOR_SERIES, colorsToExclude), 
	                                           divider + task.getTag() );
	            
	            hasTags = true;
	        }
	        
	        if( hasTags ){
	            
	            if( !hasMovedUp ){
	                
	                System.out.println( "moved up" );
	                
	                moveTitleLabelVertically( -8 );
	                
	                hasMovedUp = true;
	                hasMovedDown = false;
	            }
	            
	        } else{
	            
	            if( !hasMovedDown ){
                    
	                System.out.println( "moved down" );
	                
                    moveTitleLabelVertically( 8 );
                    
                    hasMovedDown = true;
                    hasMovedUp = false;
                }
	        }
	    }
	}
	
	private void prepareTimeDisplayLabel(){
	    
	    timeDisplayLabel = new JLabel();
	    setNotificationImageOn();
        timeDisplayLabel.setBounds(395, 18+heightOfLabel, 164, 14);
        add(timeDisplayLabel);
        setTimeDisplayLabelAttributes(timeDisplayLabel);
        timeDisplayLabel.setText("By 11:59PM");
        timeDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        timeDisplayLabelEx = null;
	}
	
	private void setTimeDisplayLabelAttributes( JLabel targetTimeDisplayLabel ){
	    
	    if( targetTimeDisplayLabel != null ){
	        
	        targetTimeDisplayLabel.setForeground(new Color(255,255,255));
	        targetTimeDisplayLabel.setFont( new Font( "Arial", Font.BOLD, 11 ) );
	    }
	}
	
	public static boolean compareByDateOnly( Date dateOne, Date dateTwo ){
	    
	    if( dateOne != null && dateTwo != null ){
	        
	        Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
	        
            c1.setTime(dateOne);
            c2.setTime(dateTwo);
            
            return (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) &&
                   (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) ) &&
                   (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR));
            
	    } else if( dateOne != null || dateTwo != null ){
	        
	        return false;
	        
	    } else{
	        
	        return true;
	    }
	}
	
	public void setTimeDisplayLabel( Date currentDate, DisplayTask task ){
	    
	    if( task != null && task.getParent() != null ){
	        
	        timeDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        
	        if( task.getParent().isFloating() ){
	            
	            setNotificationImageOff();
	            timeDisplayLabel.setText( "No Due Date Set" );
	            
	            if( timeDisplayLabelEx != null ){
                    
                    Point currentLocation = timeDisplayLabel.getLocation();
                    
                    if( currentLocation != null ){
                        
                        timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+8);
                    }
                    
                    remove(timeDisplayLabelEx);
                    
                    timeDisplayLabelEx = null;
                }
	            
	        } else if( task.getParent().isDone() ){
	            
	            SimpleDateFormat dateFormatter = new SimpleDateFormat( "h:mma" );
	            
	            setNotificationImageOff();
	            
	            String time = (task.getParent().getDateCompleted() != null ? ("Completed at " + dateFormatter.format(task.getParent().getDateCompleted())) : "");
	            
                timeDisplayLabel.setText( time );
	            
	            if( timeDisplayLabelEx != null ){
                    
                    Point currentLocation = timeDisplayLabel.getLocation();
                    
                    if( currentLocation != null ){
                        
                        timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+8);
                    }
                    
                    remove(timeDisplayLabelEx);
                    
                    timeDisplayLabelEx = null;
                }
	            
	        } else{
	            
	            SimpleDateFormat dateFormatter = new SimpleDateFormat( "h:mma" );
                SimpleDateFormat dateFormatterWithDay = new SimpleDateFormat( "d MMM yyyy 'at' h:mma" );
	            
	            if( currentDate != null ){
	                
	                setNotificationImageOn();
	                
    	            Date tempDate;
    	            
    	            if( task.getDueDate() != null && task.getEndDate() != null ){
    	                
    	                timeDisplayLabel.setText( "From " + dateFormatter.format(task.getDueDate()) + " to " + dateFormatter.format(task.getEndDate()) );
    	                
    	            } else if( task.getEndDate() != null ){
    	                
    	                tempDate = task.getEndDate();
    	                
    	                if( !compareByDateOnly( tempDate, currentDate ) ){
    	                    
    	                    timeDisplayLabel.setText( "By " + dateFormatterWithDay.format(task.getEndDate()) );
    	                    
    	                } else{
    	                    
    	                    timeDisplayLabel.setText( "By " + dateFormatter.format(task.getEndDate()) );
    	                }
    	               
    	            } else if( task.getDueDate() != null ){
    	                
    	                tempDate = task.getDueDate();
    	                
    	                if( !compareByDateOnly( tempDate, currentDate ) ){
                            
                            timeDisplayLabel.setText( "From " + dateFormatterWithDay.format(task.getDueDate()) );
                            
                        } else{
                            
                            if( task.getParent().getEndDate() != null ){
                                
                                timeDisplayLabel.setText( "From " + dateFormatter.format(task.getDueDate()) );
                                
                            } else{
                                
                                timeDisplayLabel.setText( "By " + dateFormatter.format(task.getDueDate()) );
                            }
                        }
    	                
    	            } else{
    	                
    	                setNotificationImageOff();
    	                timeDisplayLabel.setText( "No Due Date Set" );
    	            }
    	            
    	            if( timeDisplayLabelEx != null ){
                        
                        Point currentLocation = timeDisplayLabel.getLocation();
                        
                        if( currentLocation != null ){
                            
                            timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+8);
                        }
                        
                        remove(timeDisplayLabelEx);
                        
                        timeDisplayLabelEx = null;
                    }
    	            
	            } else{
	                
	                timeDisplayLabel.setHorizontalAlignment(SwingConstants.LEFT);
	                setNotificationImageOff();
	                
	                if( task.getDueDate() != null && task.getEndDate() != null ){
                        
                        timeDisplayLabel.setText( "From " + dateFormatterWithDay.format(task.getDueDate()) );
                        
                        Point currentLocation = timeDisplayLabel.getLocation();
                        
                        if( timeDisplayLabelEx == null ){
                            
                            if( currentLocation != null ){
                                
                                timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y-8);
                            }
                            
                        } else{
                            
                            remove(timeDisplayLabelEx);
                            
                            timeDisplayLabelEx = null;
                        }
                        
                        timeDisplayLabelEx = new JLabel();
                        setTimeDisplayLabelAttributes(timeDisplayLabelEx);
                        timeDisplayLabelEx.setHorizontalAlignment(SwingConstants.LEFT);
                        timeDisplayLabelEx.setText("To " + dateFormatterWithDay.format(task.getEndDate()) );
                        timeDisplayLabelEx.setBounds(currentLocation.x, currentLocation.y + 8, timeDisplayLabel.getWidth(), timeDisplayLabel.getHeight());
                        add(timeDisplayLabelEx);
                        
                        if( timeDisplayLabelEx.getParent() != null ){
                            
                            timeDisplayLabelEx.getParent().setComponentZOrder(timeDisplayLabelEx, 0);
                        }
                        
                        timeDisplayLabelEx.setForeground(new Color(255,255,255));
                        
                    } else if( task.getEndDate() != null ){
                        
                        timeDisplayLabel.setText( "By " + dateFormatterWithDay.format(task.getEndDate()) );
                        
                        if( timeDisplayLabelEx != null ){
                            
                            Point currentLocation = timeDisplayLabel.getLocation();
                            
                            if( currentLocation != null ){
                                
                                timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+8);
                            }
                            
                            remove(timeDisplayLabelEx);
                            
                            timeDisplayLabelEx = null;
                        }
                        
                    } else if( task.getDueDate() != null ){
                        
                        if( task.getParent() != null && task.getParent().getEndDate() != null ){
                            
                            timeDisplayLabel.setText( "From " + dateFormatterWithDay.format(task.getDueDate()) );
                            
                        } else{
                            
                            timeDisplayLabel.setText( "By " + dateFormatterWithDay.format(task.getDueDate()) );
                        }
                        
                        if( timeDisplayLabelEx != null ){
                            
                            Point currentLocation = timeDisplayLabel.getLocation();
                            
                            if( currentLocation != null ){
                                
                                timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+8);
                            }
                            
                            remove(timeDisplayLabelEx);
                            
                            timeDisplayLabelEx = null;
                        }
                        
                    } else{
                        
                        setNotificationImageOff();
                        timeDisplayLabel.setText( "No Due Date Set" );
                    }
	            }
	        }
	    }
	}
	
	// task bar functions
	private void prepareTaskBarBackground(){
		
		taskBarBackground = new JLabel();
		taskBarBackground.setBackground(Color.WHITE);
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/TaskBar.png")));
		taskBarBackground.setFocusable(false);
		taskBarBackground.setBounds( componentCoordinates.left, componentCoordinates.top+heightOfLabel, m_width, m_height );
		add(taskBarBackground);
		removeMouseListener(taskBarBackground);
	}
	
	public void setFocusedTaskBar(){
		
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/TaskBarActivated.png")));
	}
	
	public void setUnfocusedTaskBar(){
		
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/TaskBar.png")));
	}
	
	public void setNotificationImageOff(){
	    
	    timeDisplayLabel.setIcon(null);
	}
	
	public void setNotificationImageOn(){
	    
	    timeDisplayLabel.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/Alarm.png")));
	}
}
