package planner;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS START HERE
    ///////////////////////////////////////////////////////////////////// 
    public void processCommand( String input ){
        
        planner.Constants.COMMAND_TYPE commandType = Engine.process(input);
        
        TaskList tempTaskList = Engine.getAllTasks();
        
        switch( commandType ){
        
            case ADD:
                
                handleAddOperation(tempTaskList);
                
                break;
                
            case UPDATE:
                
                handleUpdateOperation(tempTaskList);
                
                break;
                
            case DELETE:
                
                handleDeleteOperation(tempTaskList);
                
                break;
                
            case DONE:
                    
                handleDoneOperation(tempTaskList);
                
                break;
                
            case INVALID:
                
                handleInvalidOperation();
                
                break;
                
            default:
                
                handleUnexpectedOperation();
            
                break;
        }
    }
    
    private void handleAddOperation( TaskList newTaskList ){
        
        long newTaskNumber;
        
        if( newTaskList!= null && 
            newTaskList.size() > currentList.size() &&
            (newTaskNumber = compareList( currentList, newTaskList )) > 0 ){
                
                command.setText( "Task added successfully" );
                
                currentList.copyTaskList(newTaskList);
                
                displayPane.clearDisplay();
                
                displayPane.addTasksToDisplay(currentList);
                
                displayPane.selectTask(newTaskNumber);
                
                System.out.println( "line added = " + newTaskNumber );
                
        } else{
            
            command.setText( "Failed to add task" );
        }
    }
    
    private void handleUpdateOperation( TaskList newTaskList ){
        
        long newTaskNumber = compareList( currentList, newTaskList );
        
        // changed back to newTaskNumber > 0 after fixing bug that caused data of a task in both taskList 
        // (currentList and tempTaskList) to change even though the program was only changing data of the task 
        // in only one taskList (tempTaskList)
        if( newTaskNumber > 0 ){
            
            command.setText( "Task updated successfully" );
            
            currentList.copyTaskList(newTaskList);
            
            displayPane.clearDisplay();
            
            displayPane.addTasksToDisplay(currentList);
            
            displayPane.selectTask(newTaskNumber);
            
        } else{
            
            command.setText( "Failed to update task" );
        }
    }
    
    private void handleDoneOperation( TaskList newTaskList ){
        
        long newTaskNumber = compareList( currentList, newTaskList );
        
        if( newTaskNumber > 0 ){
            
            command.setText( "Task marked done successfully" );
            
            currentList.copyTaskList(newTaskList);
                
            displayPane.clearDisplay();
                
            displayPane.addTasksToDisplay(currentList);
            
            displayPane.selectTask( newTaskNumber );
            
        } else{
            
            command.setText( "Fail to mark task as done" );
        }
    }
    
    private void handleDeleteOperation( TaskList newTaskList ){
        
        long newTaskNumber;
        
        if( newTaskList != null && 
            newTaskList.size() < currentList.size() &&
            (newTaskNumber = compareList( currentList, newTaskList )) > 0 ){
            
            command.setText( "Task deleted successfully" );
            
            currentList.copyTaskList(newTaskList);
            
            displayPane.clearDisplay();
            
            displayPane.addTasksToDisplay(currentList);
            
            displayPane.selectTask( newTaskNumber - 1 );
            
        } else{
            
            command.setText( "Failed to delete task" );
        }
    }
    
    private void handleInvalidOperation(){
        
        command.setText( "Invalid Command" );
    }
    
    public void handleUnexpectedOperation(){
        
        command.setText( "Feature not supported in V0.1" );
    }
    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS END HERE
    /////////////////////////////////////////////////////////////////////
    
    private JPanel contentPane;
    
    private JTextField command;
    
    private JTextPane tentativeDisplay;
    private JScrollPane tentativeDisplayScrollPane;
    
    private DisplayPane displayPane;
    
    private JLabel closeButton;
    private JLabel minimiseButton;
    private JLabel dragPanel;
    private JLabel sectionTitle;
    private JLabel sectionTitleLine;
    private JLabel background;
    
    private InvisibleButton upArrowButton;
    private InvisibleButton downArrowButton;
    
    private JTextPane taskDisplayPanel;
    
    // Used for drag logic
    private int mouseXCoordinate;
    private int mouseYCoordinate;
    
    private TaskList currentList;
    
    private boolean isMessageDisplayed;
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                
                try {
                    
                    UserInterface frame = new UserInterface();
                    frame.setVisible(true);
                    
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
                
            }
        });
    }

    public UserInterface() {
        
    	// Initialise engine
    	if( !Engine.init() ){
    		
    		JOptionPane.showMessageDialog(null, "Engine failed to initialise :(", "Error Message", JOptionPane.ERROR_MESSAGE);
    		System.exit(1);
    	}
    	
    	isMessageDisplayed = true;
    	
        // Main frame
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Adding paintable component to main frame
        setBounds(100, 100, 781, 493);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        prepareCommandTextField();
        prepareDisplay();
        prepareTentativeDisplay();
        prepareCloseButton();
        prepareMinimiseButton();
        prepareDragPanel();
        prepareSectionTitle();
        prepareSectionTitleLine();
        prepareBackground();
        
        if( displayPane != null ){
            
            prepareUpArrowButton(displayPane);
            
            prepareDownArrowButton(displayPane);
            
            prepareTaskDisplayPanel(displayPane);
        }
        
        setUndecorated(true);
        setLocationRelativeTo(null);
    }
    
    private void prepareTaskDisplayPanel( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            taskDisplayPanel = displayPanel.getDisplayComponent();
            
            addKeyBindingsToTextView(taskDisplayPanel);
        }
    }
    
    private void prepareDownArrowButton( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            downArrowButton = displayPanel.getDownButtonComponent();
            
            addKeyBindingsToButton(downArrowButton);
        }
    }
    
    private void prepareUpArrowButton( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            upArrowButton = displayPanel.getUpButtonComponent();
            
            addKeyBindingsToButton(upArrowButton);
        }
    }
    
    private void addKeyBindingsToTextView( JTextPane currentTextView ){
        
        if( currentTextView != null ){
            
            currentTextView.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {

                    handleKeyEvent(event);
                }
            });
        }
    }
    
    private void addKeyBindingsToButton( InvisibleButton currentArrowButton ){
        
        if( currentArrowButton != null ){
            
            currentArrowButton.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {

                    handleKeyEvent(event);
                }
            });
        }
    }
    
    private void handleKeyEvent(KeyEvent event){
        
        if( event != null ){
            
            if( event.getKeyCode() == KeyEvent.VK_PAGE_UP || 
                event.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                JScrollBar verticalScrollBar = displayPane.getVerticalScrollBar();
                int currentScrollValue = verticalScrollBar.getValue();
             
                int tempScrollUnitDifference = (event.getKeyCode() == KeyEvent.VK_PAGE_UP ? -verticalScrollBar.getBlockIncrement(-1) : verticalScrollBar.getBlockIncrement(1));
                verticalScrollBar.setValue( currentScrollValue + tempScrollUnitDifference );
                
            } else if( event.getKeyCode() == KeyEvent.VK_UP ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                displayPane.selectPreviousTask();
                
                event.consume();
                
            } else if( event.getKeyCode() == KeyEvent.VK_DOWN ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                displayPane.selectNextTask();
                
                event.consume();
                
            } else{
                
                if( !command.isFocusOwner() ){
                    
                    /*
                    if( isMessageDisplayed ){
                        
                        Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
                        
                        command.setText("" + ((char)event.getKeyCode()) );
                        
                        isMessageDisplayed = false;
                        
                    } else{
                        
                        command.setText(command.getText() + ((char)event.getKeyCode()));
                    }*/
                    
                    command.requestFocusInWindow();   
                }
                
                if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
                    
                    if( command.getText().length() <= 0 ){
                        
                        event.consume();
                    } 
                    
                } else if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                       
                    String input = command.getText();
                    
                    if( input.length() > 0 ){
                        
                        processCommand(input);
                        
                        displayPane.requestFocusInWindow();
                        
                        isMessageDisplayed = true;
                        
                    } else{
                        
                        event.consume();
                    }
                    
                }
            }
        }
    }
    
    private void prepareSectionTitle(){
    	
    	sectionTitle = new JLabel();
    	sectionTitle.setBounds(6, 36, 381, 33);
    	sectionTitle.setFont( new Font( "Arial", Font.BOLD, 24 ) );
    	sectionTitle.setForeground( new Color( 255,255,255,200 ) );
    	sectionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(sectionTitle);
        sectionTitle.setText("All Tasks");
    }
    
    private void prepareSectionTitleLine(){
    	
    	sectionTitleLine = new JLabel();
    	sectionTitleLine.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/titleLine.png")));
    	sectionTitleLine.setBounds(-142, 35, 539, 33);
    	contentPane.add(sectionTitleLine);
    }
    
    private void prepareBackground(){
        
        // Adding UI background
        background = new JLabel();
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, -1, 781, 494);
        contentPane.add(background);
    }
    
	private void prepareDisplay(){
        
    	displayPane = new DisplayPane();
        displayPane.setBounds(25, 83, 548, 334);
        
        contentPane.add(displayPane);
        
        // Copying of all tasks
        currentList = new TaskList( Engine.getAllTasks() );
        
        // Display all tasks as default screen for now
        displayPane.addTasksToDisplay(currentList);
        
        displayPane.requestFocusInWindow();
        
        addKeyBindingsToDisplay(displayPane);
    }
    
	private void addKeyBindingsToDisplay( DisplayPane currentDisplayPane ){
	    
	    if( currentDisplayPane != null ){
	        
	        currentDisplayPane.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {
                    
                    handleKeyEvent(event);
                }            
	        });
	    }
	}
	
	private long compareList( TaskList originalList, TaskList modifiedList ){
		
		if( originalList == null || modifiedList == null ){
			
			return -1;
		}
		
		Iterator<Task> iteratorOriginal = originalList.iterator();
		Iterator<Task> iteratorModified = modifiedList.iterator();
		
		Task originalTask;
		Task modifiedTask;
		
		long lineNumber = 1L;
		while( iteratorOriginal.hasNext() && iteratorModified.hasNext() ){
			
			originalTask = iteratorOriginal.next();
			modifiedTask = iteratorModified.next();
			
			if( !originalTask.equals(modifiedTask) ){

				return lineNumber;
			}
			
			++lineNumber;
		}
		
		if( iteratorModified.hasNext() || iteratorOriginal.hasNext() ){
			
			return lineNumber;
			
		} else {
			
			return 0L;
		}
	}
	
    private void prepareCommandTextField(){
        
        // Adding command text field
        command = new JTextField();
        command.setBounds(34, 432, 531, 33);
        contentPane.add(command);
        command.setColumns(10);
        
        // Setting command text field attributes
        command.setBorder(null);
        command.setOpaque(false);
        command.setFont( new Font( "Arial", Font.BOLD, 20 ));
        command.setForeground(new Color( 128,128,128 ));
        command.setText("Enter commands here");
        
        addFocusListenerToCommandTextField(command);
        addKeyBindingsToCommandTextField(command);
    }
    
    private void addKeyBindingsToCommandTextField( JTextField currentCommand ){
        
        if( currentCommand != null ){
            
            currentCommand.addKeyListener(new KeyListener(){
                
                
                @Override
                public void keyPressed( KeyEvent e ){
                    
                    handleKeyEvent(e);
                }
        
                @Override
                public void keyTyped(KeyEvent e) {}
        
                @Override
                public void keyReleased(KeyEvent e) {}
                
            });
        }
    }
    
    private void addFocusListenerToCommandTextField( JTextField currentCommand ){
        
        if(currentCommand != null){
            
            currentCommand.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    
                    command.setForeground( new Color( 0,0,0 ) );
                    
                    if( isMessageDisplayed ){
                        
                        command.setText("");
                        
                        isMessageDisplayed = false;
                        
                    } else{
                        
                        command.setText(command.getText());
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    
                    command.setForeground( new Color( 128,128,128 ) );
                    
                    String input = command.getText();
                    
                    if( !isMessageDisplayed && input.length() <= 0 ){
                        
                        command.setText("Enter commands here");
                        
                        isMessageDisplayed = true;
                        
                    } else{
                        
                        command.setText(input);
                    }
                }
            });
        }
    }

    private void prepareTentativeDisplay(){
        
        tentativeDisplayScrollPane = new JScrollPane();
        tentativeDisplayScrollPane.setBounds(601, 83, 179, 330);
        contentPane.add(tentativeDisplayScrollPane);
        
        tentativeDisplay = new JTextPane();
        tentativeDisplayScrollPane.setViewportView(tentativeDisplay);
        
        tentativeDisplayScrollPane.setBorder(null);
        tentativeDisplayScrollPane.setOpaque(false);

        tentativeDisplay.setBorder(null);
        tentativeDisplay.setOpaque(false);
        tentativeDisplay.setFocusable(false);
        
        tentativeDisplay.setEditable(false);
        
        tentativeDisplay.setFont( new Font( "Arial", Font.BOLD, 16 ) );
        tentativeDisplayScrollPane.setForeground(new Color(255,255,255));
        
        tentativeDisplayScrollPane.getViewport().setOpaque(false);
    }

    private void prepareCloseButton(){
        
        closeButton = new JLabel();
        closeButton.setBounds(744, 13, 27, 27);
        contentPane.add(closeButton);
        
        closeButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        addMouseActionListenersToCloseButton(closeButton);
    }

    private void addMouseActionListenersToCloseButton( JLabel currentCloseButton ){
        
        if( currentCloseButton != null ){
            
            currentCloseButton.addMouseListener( new MouseInputAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                        
                        Engine.exit();
                        System.exit(0);
                    }
                }
            });
        }
    }
    
    private void prepareMinimiseButton(){
        
        minimiseButton = new JLabel();
        minimiseButton.setBounds(707, 12, 28, 28);
        contentPane.add(minimiseButton);
        
        minimiseButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        addMouseActionListenersToMinimiseButton(minimiseButton);
    }

    private void addMouseActionListenersToMinimiseButton( JLabel currentMinimiseButton ){
        
        if( currentMinimiseButton != null ){
            
            currentMinimiseButton.addMouseListener( new MouseInputAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                        
                        setState( UserInterface.ICONIFIED );
                    }
                }
                
            } );
        }
    }
    
    private void prepareDragPanel(){
        
        dragPanel = new JLabel();
        dragPanel.setBounds(0, 0, 781, 493);
        contentPane.add(dragPanel);
        
        addMouseMovementBindingsToDragPanel(dragPanel);
        addMouseActionListenersToDragPanel(dragPanel);
    }
    
    private void addMouseMovementBindingsToDragPanel( JLabel currentDragPanel ){
        
        if( currentDragPanel != null ){
            
            currentDragPanel.addMouseMotionListener( new MouseMotionAdapter(){ 
    
                @Override
                public void mouseDragged(MouseEvent e) {
                    
                    int mouseDragXCoordinate = e.getXOnScreen();
                    int mouseDragYCoordinate = e.getYOnScreen();
                    
                    setLocation( mouseDragXCoordinate - mouseXCoordinate,
                                 mouseDragYCoordinate - mouseYCoordinate);
                }                
            });
        }
    }
    
    private void addMouseActionListenersToDragPanel( JLabel currentDragPanel ){
        
        if( currentDragPanel != null ){
            
            currentDragPanel.addMouseListener( new MouseInputAdapter(){ 

                @Override
                public void mousePressed(MouseEvent e) {
                    
                    mouseXCoordinate = e.getX();
                    mouseYCoordinate = e.getY();
                }
            });
        }
    }
}
