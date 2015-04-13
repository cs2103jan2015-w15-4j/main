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

/**
 * The DisplayPane class is a visual container that stores and displays information about a task
 */
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
	
	private final static int DEFAULT_DISPLAY_POSITION = 0;
	private final int DEFAULT_LABEL_HEIGHT = 20;
    private final int MINIMUM_HEIGHT_OF_LABEL = 0;
    private final int MINIMUM_POSITION_IN_DISPLAY = 0;
    private final int DEFAULT_INFOPANEL_RELATIVE_XCOORDINATES = 40;
    private final int DEFAULT_INFOPANEL_RELATIVE_YCOORDINATES = 13;
    private final int DEFAULT_INFOPANEL_WIDTH = 330;
    private final int DEFAULT_INFOPANEL_HEIGHT = 20;
    private final int DEFAULT_INFOPANEL_FONT_SIZE = 14;
    private final int DEFAULT_TASKCHECKBOX_RELATIVE_XCOORDINATES = 562;
    private final int DEFAULT_TASKCHECKBOX_RELATIVE_YCOORDINATES = 12;
    private final int DEFAULT_TASKCHECKBOX_WIDTH = 26;
    private final int DEFAULT_TASKCHECKBOX_HEIGHT = 26;
    private final int DEFAULT_TASKTITLELABEL_RELATIVE_XCOORDINATES = 60;
    private final int DEFAULT_TASKTITLELABEL_RELATIVE_YCOORDINATES = 17;
    private final int DEFAULT_TASKTITLELABEL_WIDTH = 330;
    private final int DEFAULT_TASKTITLELABEL_HEIGHT = 20;
    private final int DEFAULT_TASKTITLELABEL_FONT_SIZE = 14;
    private final int DEFAULT_TASKTITLELABEL_MAX_CHARACTERS = 45;
    private final int DEFAULT_LINENUMBERLABEL_FONT_SIZE = 12;
    private final int DEFAULT_LINENUMBERLABEL_RELATIVE_XCOORDINATES = 5;
    private final int DEFAULT_LINENUMBERLABEL_RELATIVE_YCOORDINATES = 3;
    private final int DEFAULT_LINENUMBERLABEL_WIDTH = 50;
    private final int DEFAULT_LINENUMBERLABEL_HEIGHT = 45;
    private final int MINIMUM_LINENUMBER = 0;
    private final int NUMBER_OF_PRIORIIES = 5;
    private final int INDEX_OF_COLOUR_FOR_PRIORITY_ONE = 4;
    private final int PRIORITY_FIVE = 5;
    private final int PRIORITY_FOUR = 4;
    private final int PRIORITY_THREE = 3;
    private final int PRIORITY_TWO = 2;
    private final int PRIORITY_ONE = 1;
    private final int DEFAULT_TAGSPANE_RELATIVE_XCOORDINATES = 56;
    private final int DEFAULT_TAGSPANE_RELATIVE_YCOORDINATES = 25;
    private final int DEFAULT_TAGSPANE_WIDTH = 335;
    private final int DEFAULT_TAGSPANE_HEIGHT = 17;
    private final int DEFAULT_TAGSPANE_FONT_SIZE = 12;
    private final int MINIMUM_STRING_LENGTH = 0;
    private final int DEFAULT_MOVE_OFFSET = 8;
    private final int DEFAULT_TIMEDISPLAYLABEL_RELATIVE_XCOORDINATES = 395;
    private final int DEFAULT_TIMEDISPLAYLABEL_RELATIVE_YCOORDINATES = 18;
    private final int DEFAULT_TIMEDISPLAYLABEL_WIDTH = 164;
    private final int DEFAULT_TIMEDISPLAYLABEL_HEIGHT = 14;
    private final int DEFAULT_TIMEDISPLAYLABEL_FONT_SIZE = 11;
    private final int FIRST_COMPONENT_ORDER_INDEX = 0;
    private final int MOVE_OFFSET = 8;
    private final int MAX_TAG_CHARACTERS = 30;
    private final int MAX_TAG_INDEX = 29;
    private final int MIN_STRING_OFFSET = 0;
    
    private final String STRING_BREAK = "...";
    private final String FLOATING_TASK_DATE_STRING = "No Due Date Set";
    private final String DEFAULT_COMPARE_DATE_FORMAT = "h:mma";
    private final String DONE_TASK_DATE_STRING = "Completed at ";
    private final String FROM_DATE_STRING = "From ";
    private final String TO_DATE_STRING = " to ";
    private final String TO_DATE_STRING_II = "To";
    private final String BY_DATE_STRING = "By ";
    private final String DISPLAY_DATE_FORMAT = "d MMM yyyy 'at' h:mma";
    private final String BACKGROUND_IMAGE_RESOURCE_LINK = "/planner/TaskBar.png";
    private final String DEFAULT_TIMEDISPLAYLABEL_FONT_FAMILY = "Arial";
    private final String DEFAULT_DATE_STRING = "By 11:59PM";
    private final String DEFAULT_TAGSPANE_FONT_STYLE_NAME = "color";
    private final String PRIORITY_STRING = "priority ";
    private final String SPACING = " ";
    private final String TAGS_DIVIDER = "/ ";
    private final String EMPTY_STRING = "";
    private final String DEFAULT_TAGSPANE_FONT_FAMILY = "Arial";
    private final String DEFAULT_LINENUMBERLABEL_FONT_FAMILY = "Arial";
    private final String DEFAULT_LINENUMBER_STRING = "#11111";
    private final String NUMBER_SYMBOL = "#";
    private final String TASKCHECKBOX_IMAGE_RESOURCE_LINK = "/planner/NotDoneCheckBox.png";
    private final String FLOATING_TASK_STATUS_IMAGE_RESOURCE_LINK = "/planner/FloatingTaskStatus.png";
    private final String TIMED_TASK_STATUS_IMAGE_RESOURCE_LINK = "/planner/TimedTaskStatus.png";
    private final String DEADLINE_TASK_STATUS_IMAGE_RESOURCE_LINK = "/planner/DeadlineTaskStatus.png";
    private final String ACTIVATED_TASKBAR_IMAGE_RESOURCE_LINK = "/planner/TaskBarActivated.png";
    private final String TASKBAR_IMAGE_RESOURCE_LINK = "/planner/TaskBar.png";
    private final String TASK_WITH_DATE_IMAGE_RESOURCE_LINK = "/planner/Alarm.png";
    private final String DEFAULT_INFOPANEL_FONT_STYLE_NAME = "Original";
    private final String DEFAULT_INFOPANEL_FONT_FAMILY = "Arial";
    private final String DEFAULT_TASKTITLELABEL_FONT_FAMILY = "Arial";
    private final String DEFAULT_TASKTITLELABEL_TEXT = "This is a sample text testing one two three four five";
    
	/**
     * Constructs a TaskBar object without a header on the top of it and sets the default display position of 0
     */
	public TaskBar() {
		this(DEFAULT_DISPLAY_POSITION);
	}
	
	/**
     * Constructs a TaskBar object without a header on the top of it
     * 
     * @param  position         The caret position of the taskBar object within the displayPane
     */
	public TaskBar( int position ){
	    this(null, position);
	}
	
	/**
	 * Constructs a TaskBar object with a newDateHeader header on the top of it
	 * 
	 * @param  newDateHeader    The header to be placed above the TaskBar object
	 * @param  position         The caret position of the taskBar object within the displayPane
	 */
	public TaskBar( JLabel newDateHeader, int position ){
	    
	    this.position = position >= MINIMUM_POSITION_IN_DISPLAY ? position : MINIMUM_POSITION_IN_DISPLAY;
	    dateHeader = newDateHeader;
	    componentCoordinates = getInsets();
        if( dateHeader != null ){
            heightOfLabel = DEFAULT_LABEL_HEIGHT;
            dateHeader.setBounds(componentCoordinates.left, componentCoordinates.top, m_width, heightOfLabel);
            dateHeader.setPreferredSize(new Dimension(m_width, heightOfLabel));
            add(dateHeader);
        } else{
            heightOfLabel = MINIMUM_HEIGHT_OF_LABEL;
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
	
	/**
     * Constructs a TaskBar object with a newDateHeader header on the top of it and displays the given info string
     * 
     * @param  newDateHeader    The header to be placed above the TaskBar object
     * @param  info             The string to be displayed on the TaskBar object
     */
	public TaskBar( JLabel newTitleHeader, String info ){
	    this.position = position >= MINIMUM_POSITION_IN_DISPLAY ? position : MINIMUM_POSITION_IN_DISPLAY;
        dateHeader = newTitleHeader;
        componentCoordinates = getInsets();
        if( dateHeader != null ){
            heightOfLabel = DEFAULT_LABEL_HEIGHT;
            dateHeader.setBounds(componentCoordinates.left, componentCoordinates.top, m_width, heightOfLabel);
            dateHeader.setPreferredSize(new Dimension(m_width, heightOfLabel));
            add(dateHeader);
        } else{
            heightOfLabel = MINIMUM_HEIGHT_OF_LABEL;
        }
        setSize(m_width, m_height+heightOfLabel);
        setOpaque(false);
        setBorder(null);
        setFocusable(false);
        setLayout(null);
        prepareInfoPane(info);
        prepareTaskBarBackground();
	}
	
	/**
     * Initializes and set the attributes of the InfoPane text component
     * 
     * @param  info             The string to be displayed in the InfoPane text component
     */
	private void prepareInfoPane( String info ){
        infoPane = new JTextPane();
        infoPane.setBounds(DEFAULT_INFOPANEL_RELATIVE_XCOORDINATES, 
                           DEFAULT_INFOPANEL_RELATIVE_YCOORDINATES+heightOfLabel, 
                           DEFAULT_INFOPANEL_WIDTH, DEFAULT_INFOPANEL_HEIGHT);
        infoPane.setFocusable(false);
        infoPane.setEditable(false);
        infoPane.setHighlighter(null);
        infoPane.setOpaque(false);
        infoPane.setFont(new Font( DEFAULT_INFOPANEL_FONT_FAMILY, Font.BOLD, DEFAULT_INFOPANEL_FONT_SIZE));
        infoPane.setForeground( Color.WHITE );
        add(infoPane);
        Style tempStyle = infoPane.addStyle(DEFAULT_INFOPANEL_FONT_STYLE_NAME, null);
        StyleConstants.setFontFamily(tempStyle, DEFAULT_INFOPANEL_FONT_FAMILY);
        StyleConstants.setFontSize(tempStyle, DEFAULT_INFOPANEL_FONT_SIZE);
        StyleConstants.setBold(tempStyle, true);
        StyleConstants.setForeground(tempStyle, Color.WHITE);
        infoPane.setText(info);
        
        assert infoPane.isFocusable() == false;
        assert infoPane.isEditable() == false;
        assert infoPane.isOpaque() == false;
    }
	
	/**
     * Constructs a TaskBar object with a newDateHeader header on the top of it and sets the default display position of 0
     * 
     * @param  newDateHeader    The header to be placed above the TaskBar object
     */
	public TaskBar( JLabel newDateHeader ){
	    this( newDateHeader, DEFAULT_DISPLAY_POSITION );
	}

	/**
     * Returns the stored display position of this taskBar object
     * 
     * @return  The stored display position of this taskBar object
     */
	public int getPosition(){
		return position;
	}
	
	/**
     * Sets display position of this taskBar object
     * 
     * @param position  The display position of this taskBar object
     */
	public void setPosition( int position ){
		this.position = position;
	}

	/**
     * Initializes and set the attributes of the Checkbox label component
     */
	private void prepareTaskCheckBox(){
		
		taskCheckBox = new JLabel();
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource(TASKCHECKBOX_IMAGE_RESOURCE_LINK)));
		taskCheckBox.setFocusable(false);
		taskCheckBox.setBounds(DEFAULT_TASKCHECKBOX_RELATIVE_XCOORDINATES, 
		                       DEFAULT_TASKCHECKBOX_RELATIVE_YCOORDINATES+heightOfLabel, 
		                       DEFAULT_TASKCHECKBOX_WIDTH, DEFAULT_TASKCHECKBOX_HEIGHT);
		add(taskCheckBox);
		removeMouseListener(taskCheckBox);
	}
	
	/**
     * Changes the Checkbox label component's image to a done check box image.
     */
	public void setTaskDone(){
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/DoneCheckBox.png")));
	}
	
	/**
     * Changes the Checkbox label component's image to a not done check box image.
     */
	public void setTaskNotDone(){
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/NotDoneCheckBox.png")));
	}
	
	/**
     * Changes the Checkbox label component's image to a half done check box image.
     */
	public void setTaskHalfDone(){
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/HalfDoneCheckBox.png")));
	}

	/**
     * Initializes and set the attributes of the TaskTitleLabel component
     */
	private void prepareTaskTitleLabel(){
		taskTitleLabel = new FadedTextField(Color.WHITE, Color.BLACK, Color.BLACK, 
		                                    DEFAULT_TASKTITLELABEL_MAX_CHARACTERS, DEFAULT_TASKTITLELABEL_MAX_CHARACTERS);
		taskTitleLabel.setBounds(DEFAULT_TASKTITLELABEL_RELATIVE_XCOORDINATES, 
		                         DEFAULT_TASKTITLELABEL_RELATIVE_YCOORDINATES+heightOfLabel, 
		                         DEFAULT_TASKTITLELABEL_WIDTH, DEFAULT_TASKTITLELABEL_HEIGHT);
		taskTitleLabel.setFocusable(false);
		taskTitleLabel.setFont(new Font( DEFAULT_TASKTITLELABEL_FONT_FAMILY, Font.BOLD, 
		                                 DEFAULT_TASKTITLELABEL_FONT_SIZE));
		add(taskTitleLabel);
		taskTitleLabel.setText(DEFAULT_TASKTITLELABEL_TEXT);
		hasMovedDown = true;
		hasMovedUp = false;
	}
	
	/**
     * Sets the task title of the TaskBar with the title string found within the given task
     * 
     * @param task  The Task object in which its title is extracted to set the task title of this TaskBar
     */
	public void setTaskTitle( Task task ){
	    if( task != null ){
	        String taskName = task.getName();
	        if( taskName != null ){
	            if( !task.isDone() ){
        	        if( task.isFloating() ){
        	            taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource(FLOATING_TASK_STATUS_IMAGE_RESOURCE_LINK)));
        	        } else if( task.isTimed() ){
        	            taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource(TIMED_TASK_STATUS_IMAGE_RESOURCE_LINK)));
        	        } else{
        	            taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource(DEADLINE_TASK_STATUS_IMAGE_RESOURCE_LINK)));
        	        }
	            }
    	        taskTitleLabel.setText( taskName );
	        }
	    }
	}
	
	/**
     * Move the title of this TaskBar given the distance offset relative to its current position
     * 
     * @param relativeIncrements  The distance offset to move the title of this TaskBar relative to its current position
     */
	private void moveTitleLabelVertically( int relativeIncrements ){
	    Point currentLocationOfTitle = taskTitleLabel.getLocation();
	    if( currentLocationOfTitle != null ){
	        taskTitleLabel.setLocation(currentLocationOfTitle.x, currentLocationOfTitle.y + relativeIncrements);
	    }
	}
	
	/**
     * Initializes and set the attributes of the LineNumberLabel component
     */
	private void prepareLineNumberLabel(){
		lineNumberLabel = new JLabel(DEFAULT_LINENUMBER_STRING);
		lineNumberLabel.setBounds(DEFAULT_LINENUMBERLABEL_RELATIVE_XCOORDINATES, 
		                          DEFAULT_LINENUMBERLABEL_RELATIVE_YCOORDINATES+heightOfLabel, 
		                          DEFAULT_LINENUMBERLABEL_WIDTH, DEFAULT_LINENUMBERLABEL_HEIGHT);
		lineNumberLabel.setForeground(Color.WHITE);
		lineNumberLabel.setFont(new Font( DEFAULT_LINENUMBERLABEL_FONT_FAMILY, Font.BOLD, DEFAULT_LINENUMBERLABEL_FONT_SIZE ));
		lineNumberLabel.setFocusable(false);
		lineNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lineNumberLabel);
		removeMouseListener(lineNumberLabel);
	}
	
	/**
     * Sets the id of the TaskBar to be displayed
     * 
     * @param lineNumber  The id of the TaskBar to be displayed
     */
	public void setLineNumber( long lineNumber ){
		if( lineNumber > MINIMUM_LINENUMBER ){
			lineNumberLabel.setText(NUMBER_SYMBOL+lineNumber);
		}
	}
	
	/**
     * Initializes and set the attributes of the TagsPane component
     */
	private void prepareTagsPane(){
	    tagsPane = new JTextPane();
        tagsPane.setBounds(DEFAULT_TAGSPANE_RELATIVE_XCOORDINATES, DEFAULT_TAGSPANE_RELATIVE_YCOORDINATES+heightOfLabel, 
                           DEFAULT_TAGSPANE_WIDTH, DEFAULT_TAGSPANE_HEIGHT);
        add(tagsPane);
        tagsPane.setEditable(false);
        tagsPane.setHighlighter(null);
        tagsPane.setFocusable(false);
        tagsPane.setOpaque(false);
        tagsPane.setFont( new Font( DEFAULT_TAGSPANE_FONT_FAMILY, Font.BOLD, DEFAULT_TAGSPANE_FONT_SIZE ) );
	}
	
	/**
     * Returns the color corresponding to the given priority. If a priority < 1 or > 5 is given, it will return priority one's
     * color. It will also mark the color returned in the colorsToExclude array as used.
     * 
     * @param colors            The array of colors to find a corresponding color for the given priority
     * @param priority          The priority of a task
     * @param colorsToExclude   An array that records the colours that have already been used
     * 
     * @return The color corresponding to the given priority
     */
	private Color getPriorityColor( Color []colors, int priority, boolean []colorsToExclude ){
	    if( colors != null && 
	        colors.length >= NUMBER_OF_PRIORIIES &&
	        colorsToExclude != null &&
	        colorsToExclude.length >= PRIORITY_FIVE){
	        switch( priority ){
	            case PRIORITY_FIVE:
	            case PRIORITY_FOUR:
	            case PRIORITY_THREE:
	            case PRIORITY_TWO:
	                if( colors[PRIORITY_FIVE-priority] != null ){
	                    colorsToExclude[PRIORITY_FIVE-priority] = true;
	                    return colors[PRIORITY_FIVE-priority];
	                } else{
	                    return Color.WHITE;
	                }
	                
	            default:
	                if( colors[INDEX_OF_COLOUR_FOR_PRIORITY_ONE] != null ){
	                    colorsToExclude[INDEX_OF_COLOUR_FOR_PRIORITY_ONE] = true;
                        return colors[INDEX_OF_COLOUR_FOR_PRIORITY_ONE];
                    } else{
                        return Color.WHITE;
                    }
	        }
	    } else{
	        return Color.WHITE;
	    }
	}
	
	/**
     * This method will append the given string at the back of the given document and will be rendered with 
     * the given color and style.
     * 
     * @param doc               The document in which the string will be appended to
     * @param style             The style to render the appended string in
     * @param color             The color to set for the appended string
     * @param str               The string to be appended
     */
	private void appendColoredStringsToDisplay( StyledDocument doc, Style style, Color color, String str ){
	    if( doc != null && style != null && color != null && str != null){
	        try{
	            StyleConstants.setForeground(style, color);
                doc.insertString(doc.getLength(), str, style);
	        } catch( BadLocationException badLocationException ){}
	    }
	}
	
	/**
     * This method will return a random color that has not been used based on the colorsToExclude boolean array from 
     * the colors array
     * 
     * @param colors            The array of colors to find a random color from
     * @param colorsToExclude   The array of flags to indicate which colors have already been used
     */
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
	            return Color.WHITE;
	        }
	        while( colorsToExclude[selectedColorIdx = random.nextInt(numOfColors)] );
	        if( colors[selectedColorIdx] != null ){
	            return colors[selectedColorIdx];
	        } else{
	            return Color.WHITE;
	        }
	    } else{
	        return Color.WHITE;
	    }
	}
	
	/**
     * This method will set tags on the TaskBar Object, retrieved from the given task
     * 
     * @param task     The Task object which will have its tags retrieved to set tags on the TaskBar Object
     */
	public void setTags( Task task ){
	    if( task != null && planner.Constants.COLOR_SERIES != null ){
	        boolean hasTags = false;
	        boolean previousTextExist = false;
	        StyledDocument doc = tagsPane.getStyledDocument();
	        Style colorStyle = tagsPane.addStyle(DEFAULT_TAGSPANE_FONT_STYLE_NAME, null);
	        StyleConstants.setItalic(colorStyle, true);
	        boolean []colorsToExclude = new boolean[planner.Constants.COLOR_SERIES.length];
	        if( task.getPriority() >= PRIORITY_ONE ){
	            int priority = task.getPriority();
	            priority = (priority <= PRIORITY_FIVE ? priority : PRIORITY_FIVE);
	            appendColoredStringsToDisplay( doc, 
	                                           colorStyle, 
	                                           getPriorityColor(planner.Constants.COLOR_SERIES, priority, colorsToExclude), 
	                                           PRIORITY_STRING + priority + SPACING );
	            previousTextExist = true;
	            hasTags = true;
	        }
	        if( task.getTag() != null && task.getTag().length() > MINIMUM_STRING_LENGTH ){
	            String divider = (previousTextExist ? TAGS_DIVIDER : EMPTY_STRING);
	            String tag = divider + task.getTag();
	            if(tag.length() > MAX_TAG_CHARACTERS){
	                tag = tag.substring(MIN_STRING_OFFSET,MAX_TAG_INDEX);
	                tag += STRING_BREAK;
	            }
	            appendColoredStringsToDisplay( doc, 
	                                           colorStyle, 
	                                           getRandomColor(planner.Constants.COLOR_SERIES, colorsToExclude), 
	                                           tag );
	            hasTags = true;
	        }
	        if( hasTags ){
	            if( !hasMovedUp ){
	                moveTitleLabelVertically( -DEFAULT_MOVE_OFFSET );
	                hasMovedUp = true;
	                hasMovedDown = false;
	            }
	        } else{
	            if( !hasMovedDown ){    
                    moveTitleLabelVertically( DEFAULT_MOVE_OFFSET ); 
                    hasMovedDown = true;
                    hasMovedUp = false;
                }
	        }
	    }
	}
    
	/**
     * Initializes and set the attributes of the TimeDisplayLabel component
     */
	private void prepareTimeDisplayLabel(){
	    timeDisplayLabel = new JLabel();
	    setNotificationImageOn();
        timeDisplayLabel.setBounds(DEFAULT_TIMEDISPLAYLABEL_RELATIVE_XCOORDINATES, 
                                   DEFAULT_TIMEDISPLAYLABEL_RELATIVE_YCOORDINATES+heightOfLabel, 
                                   DEFAULT_TIMEDISPLAYLABEL_WIDTH, DEFAULT_TIMEDISPLAYLABEL_HEIGHT);
        add(timeDisplayLabel);
        setTimeDisplayLabelAttributes(timeDisplayLabel);
        timeDisplayLabel.setText(DEFAULT_DATE_STRING);
        timeDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeDisplayLabelEx = null;
	}
	
	/**
     * Set the attributes of the TimeDisplayLabel component
     * 
     * @param targetTimeDisplayLabel    The TimeDisplayLabel component in which its attributes are to be set
     */
	private void setTimeDisplayLabelAttributes( JLabel targetTimeDisplayLabel ){
	    if( targetTimeDisplayLabel != null ){
	        targetTimeDisplayLabel.setForeground(Color.WHITE);
	        targetTimeDisplayLabel.setFont( new Font( DEFAULT_TIMEDISPLAYLABEL_FONT_FAMILY, Font.BOLD, 
	                                                  DEFAULT_TIMEDISPLAYLABEL_FONT_SIZE ) );
	    }
	}
	
	/**
     * This method compares two dates only based on their day of the month, month and year.
     * 
     * @param dateOne    The first date object to be compared with the second date object
     * @param dateTwo    The second date object to be compared with the first date object
     */
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
	
	/**
     * This method display the time label for floating tasks
     */
	private void displayFloatingTimeLabel(){
	    setNotificationImageOff();
        timeDisplayLabel.setText( FLOATING_TASK_DATE_STRING );
        if( timeDisplayLabelEx != null ){
            Point currentLocation = timeDisplayLabel.getLocation();
            if( currentLocation != null ){
                timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+MOVE_OFFSET);
            }
            remove(timeDisplayLabelEx);
            timeDisplayLabelEx = null;
        }
	}
	
	/**
     * This method display the time label for done tasks
     * 
     * @param task     Task object to extract due date from to fill in the contents of the time label
     */
	private void displayDoneTimeLabel( DisplayTask task ){
	    
	    if( task != null && task.getParent() != null ){
    	    SimpleDateFormat dateFormatter = new SimpleDateFormat( DEFAULT_COMPARE_DATE_FORMAT );
            setNotificationImageOff();
            
            String time = EMPTY_STRING;
            if( task.getParent().getDateCompleted() != null ){
                time = DONE_TASK_DATE_STRING + dateFormatter.format(task.getParent().getDateCompleted());
            }
            timeDisplayLabel.setText( time );
            if( timeDisplayLabelEx != null ){
                Point currentLocation = timeDisplayLabel.getLocation();
                if( currentLocation != null ){
                    timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+MOVE_OFFSET);
                }
                remove(timeDisplayLabelEx);
                timeDisplayLabelEx = null;
            }
	    }
	}
	
	/**
     * This method display the time label for timed and deadline tasks they are displayed in chronological order
     * 
     * @param currentDate    The date (Only day of the month, month and year) that the given is classified under
     * @param task           The DisplayTask object for which dues dates of a task can be extracted from
     */
	private void displayTimeLabelByDateFormat( Date currentDate, DisplayTask task ){
	    if( currentDate != null && task != null && task.getParent() != null ){
	        SimpleDateFormat dateFormatter = new SimpleDateFormat( DEFAULT_COMPARE_DATE_FORMAT );
            SimpleDateFormat dateFormatterWithDay = new SimpleDateFormat( DISPLAY_DATE_FORMAT );
	        setNotificationImageOn();
            Date tempDate;
            if( task.getDueDate() != null && task.getEndDate() != null ){
                timeDisplayLabel.setText( FROM_DATE_STRING + dateFormatter.format(task.getDueDate()) + 
                                          TO_DATE_STRING + dateFormatter.format(task.getEndDate()) );
            } else if( task.getEndDate() != null ){
                tempDate = task.getEndDate();
                if( !compareByDateOnly( tempDate, currentDate ) ){
                    timeDisplayLabel.setText( BY_DATE_STRING + dateFormatterWithDay.format(task.getEndDate()) );
                } else{
                    timeDisplayLabel.setText( BY_DATE_STRING + dateFormatter.format(task.getEndDate()) );
                }
            } else if( task.getDueDate() != null ){
                tempDate = task.getDueDate();
                if( !compareByDateOnly( tempDate, currentDate ) ){
                    timeDisplayLabel.setText( FROM_DATE_STRING + dateFormatterWithDay.format(task.getDueDate()) );
                } else{
                    if( task.getParent().getEndDate() != null ){
                        timeDisplayLabel.setText( FROM_DATE_STRING + dateFormatter.format(task.getDueDate()) );
                    } else{
                        timeDisplayLabel.setText( BY_DATE_STRING + dateFormatter.format(task.getDueDate()) );
                    }
                }
            } else{
                setNotificationImageOff();
                timeDisplayLabel.setText( FLOATING_TASK_DATE_STRING );
            }
            if( timeDisplayLabelEx != null ){
                Point currentLocation = timeDisplayLabel.getLocation();
                if( currentLocation != null ){
                    timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+MOVE_OFFSET);
                }
                remove(timeDisplayLabelEx);
                timeDisplayLabelEx = null;
            }
	    }
	}
	
	/**
     * This method display the time label for timed and deadline tasks they are displayed br priority
     * 
     * @param task           The DisplayTask object for which dues dates of a task can be extracted from
     */
	private void displayTimeLabelByPriorityFormat( DisplayTask task ){
	    if( task != null && task.getParent() != null  ){
	        SimpleDateFormat dateFormatterWithDay = new SimpleDateFormat( DISPLAY_DATE_FORMAT );
    	    timeDisplayLabel.setHorizontalAlignment(SwingConstants.LEFT);
            setNotificationImageOff();
            if( task.getDueDate() != null && task.getEndDate() != null ){
                timeDisplayLabel.setText( FROM_DATE_STRING + dateFormatterWithDay.format(task.getDueDate()) );
                Point currentLocation = timeDisplayLabel.getLocation();
                if( timeDisplayLabelEx == null ){
                    if( currentLocation != null ){
                        timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y-MOVE_OFFSET);
                    }
                } else{
                    remove(timeDisplayLabelEx);
                    timeDisplayLabelEx = null;
                }
                timeDisplayLabelEx = new JLabel();
                setTimeDisplayLabelAttributes(timeDisplayLabelEx);
                timeDisplayLabelEx.setHorizontalAlignment(SwingConstants.LEFT);
                timeDisplayLabelEx.setText(TO_DATE_STRING_II + dateFormatterWithDay.format(task.getEndDate()) );
                timeDisplayLabelEx.setBounds(currentLocation.x, currentLocation.y + MOVE_OFFSET, 
                                             timeDisplayLabel.getWidth(), timeDisplayLabel.getHeight());
                add(timeDisplayLabelEx);
                if( timeDisplayLabelEx.getParent() != null ){
                    timeDisplayLabelEx.getParent().setComponentZOrder(timeDisplayLabelEx, FIRST_COMPONENT_ORDER_INDEX);
                }
                timeDisplayLabelEx.setForeground(Color.WHITE);
            } else if( task.getEndDate() != null ){
                timeDisplayLabel.setText( BY_DATE_STRING + dateFormatterWithDay.format(task.getEndDate()) );
                if( timeDisplayLabelEx != null ){
                    Point currentLocation = timeDisplayLabel.getLocation();
                    if( currentLocation != null ){
                        timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+MOVE_OFFSET);
                    }
                    remove(timeDisplayLabelEx);
                    timeDisplayLabelEx = null;
                }
            } else if( task.getDueDate() != null ){
                if( task.getParent() != null && task.getParent().getEndDate() != null ){
                    timeDisplayLabel.setText( FROM_DATE_STRING + dateFormatterWithDay.format(task.getDueDate()) );
                } else{
                    timeDisplayLabel.setText( BY_DATE_STRING + dateFormatterWithDay.format(task.getDueDate()) );
                }
                if( timeDisplayLabelEx != null ){
                    Point currentLocation = timeDisplayLabel.getLocation();
                    if( currentLocation != null ){
                        timeDisplayLabel.setLocation(currentLocation.x, currentLocation.y+MOVE_OFFSET);
                    }
                    remove(timeDisplayLabelEx);
                    timeDisplayLabelEx = null;
                }
            } else{
                setNotificationImageOff();
                timeDisplayLabel.setText( FLOATING_TASK_DATE_STRING );
            }
	    }
	}
	
	/**
     * This method controls the way how due dates of tasks are displayed
     * 
     * @param currentDate    The date (Only day of the month, month and year) that the given is classified under
     * @param task           The DisplayTask object for which dues dates of a task can be extracted from
     */
	public void setTimeDisplayLabel( Date currentDate, DisplayTask task ){
	    if( task != null && task.getParent() != null ){
	        timeDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        if( task.getParent().isFloating() ){
	            displayFloatingTimeLabel();
	        } else if( task.getParent().isDone() ){
	            displayDoneTimeLabel(task);
	        } else{
               
	            if( currentDate != null ){
	                displayTimeLabelByDateFormat(currentDate, task);
	            } else{
	                displayTimeLabelByPriorityFormat(task);
	            }
	        }
	    }
	}
	
	/**
     * Returns the preferred size of this TaskBar component
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(m_width, m_height+heightOfLabel);
    }
    
    /**
     * Prevents the given component from receiving inputs from mouse clicks
     * 
     * @param component     The component fow which mouse clicks events must be prevented from 
     *                      reaching it
     */
    private void removeMouseListener( Component component ){
        if( component != null ){
            component.addMouseListener( new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {}
            });
        }
    }
	
	/**
     * Initializes and set the attributes of the background component of the TaskBar
     */
	private void prepareTaskBarBackground(){
		taskBarBackground = new JLabel();
		taskBarBackground.setBackground(Color.WHITE);
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource(BACKGROUND_IMAGE_RESOURCE_LINK)));
		taskBarBackground.setFocusable(false);
		taskBarBackground.setBounds( componentCoordinates.left, componentCoordinates.top+heightOfLabel, m_width, m_height );
		add(taskBarBackground);
		removeMouseListener(taskBarBackground);
	}
	
	/**
     * This method changes the image of the taskbar to a activated task bar image
     */
	public void setFocusedTaskBar(){
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource(ACTIVATED_TASKBAR_IMAGE_RESOURCE_LINK)));
	}
	

	/**
     * This method changes the image of the taskbar to a non-activated task bar image
     */
	public void setUnfocusedTaskBar(){
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource(TASKBAR_IMAGE_RESOURCE_LINK)));
	}
	
	/**
     * This method removes the image used to indicate tasks with a date set from the timeDisplayLabel component
     */
	public void setNotificationImageOff(){
	    timeDisplayLabel.setIcon(null);
	}
	
	/**
     * This method adds the image used to indicate tasks with a date set from the timeDisplayLabel component
     */
	public void setNotificationImageOn(){
	    timeDisplayLabel.setIcon(new ImageIcon(TaskBar.class.getResource(TASK_WITH_DATE_IMAGE_RESOURCE_LINK)));
	}
}
