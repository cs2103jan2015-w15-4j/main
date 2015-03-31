package planner;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.text.SimpleDateFormat;

public class TaskBar extends JComponent {

    private Insets componentCoordinates;
	
	private final int m_width = 597;
	private final int m_height = 50;
	
	private JLabel taskBarBackground;
	private JLabel taskCheckBox;
	private JLabel lineNumberLabel;
	private JLabel timeDisplayLabel;
	
	private JTextPane tagsPane;
	
	private FadedTextField taskTitleLabel;
	
	private int position;
	
	private boolean hasMovedUp;
	private boolean hasMovedDown;
	
	// Helper functions
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(m_width, m_height);
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
		
		this.position = position >= 0 ? position : 0;
		
		setSize(597, 50);
		setOpaque(false);
		setBorder(null);
		setFocusable(false);
		componentCoordinates = getInsets();
		setLayout(null);
		
		prepareTaskCheckBox();
		prepareTaskTitleLabel();
		prepareLineNumberLabel();
		prepareTagsPane();
		prepareTimeDisplayLabel();
		prepareTaskBarBackground();
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
		taskCheckBox.setBounds(562, 12, 26, 26);
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
		taskTitleLabel.setBounds(60, 17, 330, 20);
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
		lineNumberLabel.setBounds(5, 3, 50, 45);
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
        tagsPane.setBounds(56, 25, 335, 17);
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
	            
	            System.out.println( "has priority" );
	            
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
        timeDisplayLabel.setBounds(395, 18, 164, 14);
        add(timeDisplayLabel);
        timeDisplayLabel.setForeground(new Color(255,255,255));
        timeDisplayLabel.setFont( new Font( "Arial", Font.BOLD, 11 ) );
        timeDisplayLabel.setText("By 11:59PM");
        timeDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public void setTimeDisplayLabel( Task task ){
	    
	    if( task != null ){
	        
	        if( task.isFloating() ){
	            
	            setNotificationImageOff();
	            timeDisplayLabel.setText( "No Due Date Set" );
	            
	        } else{
	            
	            SimpleDateFormat dateFormatter = new SimpleDateFormat( "h:mma" );
	            
	            if( task.getDueDate() != null && task.getEndDate() != null ){
	                
	                timeDisplayLabel.setText( "From " + dateFormatter.format(task.getDueDate()) + " to " + dateFormatter.format(task.getEndDate()) );
	                
	            } else if( task.getEndDate() != null ){
	                
	                timeDisplayLabel.setText( "By " + dateFormatter.format(task.getEndDate()) );
	                
	            } else if( task.getDueDate() != null ){
	                
	                timeDisplayLabel.setText( "By " + dateFormatter.format(task.getDueDate() ) );
	                
	            } else{
	                
	                setNotificationImageOff();
	                timeDisplayLabel.setText( "No Due Date Set" );
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
		taskBarBackground.setBounds( componentCoordinates.left, componentCoordinates.top, m_width, m_height );
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
