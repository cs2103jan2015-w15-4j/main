package planner;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TaskBar extends JComponent {

	private Insets componentCoordinates;
	
	private final int m_width = 515;
	private final int m_height = 69;
	
	private JLabel taskBarBackground;
	private JLabel taskPriorityBar;
	private JLabel taskTimeCounter;
	private JLabel taskTimeCounterLabel;
	private JLabel taskTagLabel;
	private JLabel taskCheckBox;
	private JLabel lineNumberLabel;
	
	private FadedTextField taskTitleLabel;
	
	private JTextArea taskTimeDataLabel;
	
	private DateFormat dateFormatter;
	
	private int position;
	
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(m_width, m_height);
	}
	
	public TaskBar() {
		
		this(0);
	}
	
	public TaskBar( int position ){
		
		this.position = position;
		
		dateFormatter = new SimpleDateFormat( "EEE, d MMM yy" );
		
		setSize(m_width, m_height);
		setOpaque(false);
		setBorder(null);
		setFocusable(false);
		componentCoordinates = getInsets();
		setLayout(null);
		
		prepareTaskTimeDataLabel();
		prepareTaskPriorityBar();
		prepareTaskTimeCounter();
		prepareTaskTimeCounterLabel();
		prepareTaskTagLabel();
		prepareTaskCheckBox();
		prepareTaskTitleLabel();
		prepareLineNumberLabel();
		prepareTaskBarBackground();
	}

	public int getPosition(){
		
		return position;
	}
	
	public void setPosition( int position ){
		
		this.position = position;
	}
	
	// Task time data label functions
	private void prepareTaskTimeDataLabel(){
		
		taskTimeDataLabel = new JTextArea();
		taskTimeDataLabel.setEditable(false);
		taskTimeDataLabel.setBounds(67, 30, 229, 30);
		taskTimeDataLabel.setOpaque(false);
		taskTimeDataLabel.setFocusable(false);
		taskTimeDataLabel.setFont(new Font( "Arial", Font.PLAIN, 11 ));
		taskTimeDataLabel.setForeground(new Color(238, 238, 238));
		add(taskTimeDataLabel);
		taskTimeDataLabel.setText( "Due               :  Sun, 23 May 15 by 03:00:00\nTime needed :  30 days" );
	}
	
	public void setTaskTimeDataLabel( Date dueDate, String timeLeft ){
		
		if( dueDate != null && timeLeft != null ){
			
			String finalString = "Due               :  " + dateFormatter.format(dueDate) + "\nTime needed :  " + timeLeft;
			
			taskTimeDataLabel.setText( finalString );
		}
	}
	
	// Priority bar functions
	private void prepareTaskPriorityBar(){
		
		taskPriorityBar = new JLabel();
		taskPriorityBar.setBounds(58, 14, 5, 45);
		taskPriorityBar.setBackground(new Color( 255, 0, 0 ));
		taskPriorityBar.setFocusable(false);
		taskPriorityBar.setOpaque(true);
		add(taskPriorityBar);
	}
	
	public void setPriorityBarColour( Color colour ){
		
		if( colour != null ){
			
			taskPriorityBar.setBackground(colour);
		}
	}
	
	// Task Time Counter functions
	private void prepareTaskTimeCounter(){
		
		taskTimeCounter = new JLabel();
		taskTimeCounter.setFont(new Font("Arial", Font.BOLD, 20 ));
		taskTimeCounter.setForeground(new Color(255, 102, 0));
		taskTimeCounter.setHorizontalAlignment(SwingConstants.CENTER);
		taskTimeCounter.setFocusable(false);
		taskTimeCounter.setBounds(302, 9, 145, 24);
		add(taskTimeCounter);
		taskTimeCounter.setText("12");
	}
	
	public void setTimeCounter( String time ){
		
		try{
			
			taskTimeCounter.setText( Integer.parseInt(time) + "" );
			
		} catch( NumberFormatException numberFormatException ){
			
			
		}
	}
	
	// Task Time Counter Label functions
	private void prepareTaskTimeCounterLabel(){
		
		taskTimeCounterLabel = new JLabel();
		taskTimeCounterLabel.setFont(new Font("Arial", Font.BOLD, 15 ));
		taskTimeCounterLabel.setForeground(new Color(238, 238, 238));
		taskTimeCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		taskTimeCounterLabel.setFocusable(false);
		taskTimeCounterLabel.setBounds(302, 32, 145, 14);
		add(taskTimeCounterLabel);
		taskTimeCounterLabel.setText("Days Left");
	}
	
	// will change this to Left or Overdue. And time unit will be an enum value
	public void prepareTaskTimeCounterLabel( String timeUnit ){
		
		if( timeUnit != null ){
			
			taskTimeCounterLabel.setText( timeUnit + " Left" );
		}
	}
	
	// Task tag functions
	private void prepareTaskTagLabel(){
		
		taskTagLabel = new JLabel();
		taskTagLabel.setFont(new Font("Arial", Font.BOLD, 12 ));
		taskTagLabel.setForeground(new Color(31, 190, 214));
		taskTagLabel.setHorizontalAlignment(SwingConstants.CENTER);
		taskTagLabel.setFocusable(false);
		taskTagLabel.setBounds(302, 46, 145, 14);
		add(taskTagLabel);
		taskTagLabel.setText("#Work #Important #Final");
	}
	
	public void setTaskTags( String tags ){
		
		if( tags != null ){
			
			taskTagLabel.setText(tags);
		}
	}
	
	// Check box functions
	private void prepareTaskCheckBox(){
		
		taskCheckBox = new JLabel();
		taskCheckBox.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/NotDoneCheckBox.png")));
		taskCheckBox.setFocusable(false);
		taskCheckBox.setBounds(460, 18, 35, 35);
		add(taskCheckBox);
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
		
		taskTitleLabel = new FadedTextField(new Color(255,255,255), new Color(102,102,102), new Color(102,102,102), 25, 22);
		taskTitleLabel.setBounds(67, 10, 228, 24);
		taskTitleLabel.setFocusable(false);
		taskTitleLabel.setFont(new Font( "Arial", Font.BOLD, 18 ));
		add(taskTitleLabel);
		taskTitleLabel.setText("This is a sample text testing one two");
	}
	
	public void setTaskTitle( String taskTitle ){
		
		if( taskTitle != null ){
			
			taskTitleLabel.setText(taskTitle);
		}
	}
	
	// Line number label functions
	private void prepareLineNumberLabel(){
		
		lineNumberLabel = new JLabel("#0");
		lineNumberLabel.setBounds(10, 14, 42, 44);
		lineNumberLabel.setForeground(new Color( 255,255,255,220 ));
		lineNumberLabel.setFont(new Font( "Arial", Font.BOLD, 15 ));
		lineNumberLabel.setFocusable(false);
		lineNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lineNumberLabel);
	}
	
	public void setLineNumber( long lineNumber ){
		
		if( lineNumber > 0  ){
			
			lineNumberLabel.setText("#"+lineNumber);
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
	}
	
	public void setFocusedTaskBar(){
		
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/TaskBarActivated.png")));
	}
	
	public void setUnfocusedTaskBar(){
		
		taskBarBackground.setIcon(new ImageIcon(TaskBar.class.getResource("/planner/TaskBar.png")));
	}
}
