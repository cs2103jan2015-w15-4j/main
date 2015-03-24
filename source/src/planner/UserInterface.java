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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.*;

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
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import sun.util.logging.PlatformLogger.Level;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS START HERE
    ///////////////////////////////////////////////////////////////////// 
    public void processCommand( String input ){
        
        planner.Constants.CommandType commandType = Engine.process(input);
        
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
    private JPanel slidePanelFrame;
    
    private SliderPanel slidePanel;
    
    private JTextField command;
    
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
    private JTextPane navigationPanel;
    
    // Used for drag logic
    private int mouseXCoordinate;
    private int mouseYCoordinate;
    
    private TaskList currentList;
    
    private boolean isMessageDisplayed;
    
    private DisplayState currentDisplayState;
    
    private ArrayList<NavigationBar> currentNavigationBars;
    
    private final static Logger userInterfaceLogger = Logger.getLogger(UserInterface.class.getName());
    
    private char characterToTransfer;
    private boolean isBackspacePressed;
    
    public static void main(String[] args) {
        
        userInterfaceLogger.setLevel(java.util.logging.Level.SEVERE);
        
        EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                
                try {
                    
                    UserInterface frame = new UserInterface();
                    frame.setVisible(true);
                    
                } catch (Exception e) {
                    
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
    }

    public UserInterface() {
        
    	// Initialise engine
    	if( !Engine.init() ){
    		
    		JOptionPane.showMessageDialog(null, "Engine failed to initialise :(", "Error Message", JOptionPane.ERROR_MESSAGE);
    		
    		userInterfaceLogger.severe("Fail to intialise engine");
    		
    		System.exit(1);
    	}
    	
    	isMessageDisplayed = true;
    	
        // Main frame
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Adding paintable component to main frame
        setBounds(100, 100, 867, 587);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        prepareCommandTextField();
        prepareDisplay();
        prepareCloseButton();
        prepareMinimiseButton();
        prepareSlidePanel();
        prepareNavigationPanel();
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
            
            userInterfaceLogger.info( "Task display panel set up successfully" );
            
        } else{
            
            userInterfaceLogger.severe("Task display panel fail to initialise");
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
    
    
    private char filterKeys( char keyCode ){
        
        if( (keyCode >= 32 && keyCode <= 126) || keyCode == 8 ){
            
            return keyCode;
            
        } else{
            
            return '\0';
        }
    }
    
    private void handleKeys( char keyTyped ){
        
        if( keyTyped >= 32 && keyTyped <= 126){
            
            characterToTransfer = keyTyped;
            isBackspacePressed = false;
            
        } else if( keyTyped == 8 ){
            
            characterToTransfer = '\0';
            isBackspacePressed = true;
            
        } else{
            
            characterToTransfer = '\0';
            isBackspacePressed = false;
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
                
            } else if( event.getKeyCode() == KeyEvent.VK_ESCAPE ){
                
                if( slidePanel.isVisible()){
                    
                    slidePanel.slideIn();
                    
                    event.consume();
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_UP ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( slidePanel.isVisible() && event.isControlDown() ){
                    
                    JScrollPane tempScrollPane = slidePanel.getDisplayScrollComponent();
                    JScrollBar verticalScrollBar = tempScrollPane != null ? tempScrollPane.getVerticalScrollBar() : null;
                    
                    if( verticalScrollBar != null && verticalScrollBar.isVisible() ){
                        
                        int currentScrollValue = verticalScrollBar.getValue();
                        int tempScrollUnitDifference = -verticalScrollBar.getUnitIncrement(-1);
                        verticalScrollBar.setValue(currentScrollValue + tempScrollUnitDifference);
                        
                        event.consume();
                        
                        return;
                    }
                }
                
                displayPane.selectPreviousTask();
                
                if( currentNavigationBars != null && 
                    currentNavigationBars.size() > 0 && 
                    currentNavigationBars.get(0).isVisible() ){
                    
                    long taskID = displayPane.getCurrentSelectedTaskID();
                    
                    if( taskID >= 0 ){
                        
                        long tempTaskID = currentList.get((int)displayPane.getCurrentSelectedTaskID()).getID();
                        
                        currentNavigationBars.get(0).setMessage(planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + tempTaskID, "F1");
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    int selectedTaskID = (int)displayPane.getCurrentSelectedTaskID();   //Possible loss of data
                    
                    if( selectedTaskID >= 0 && selectedTaskID < currentList.size() ){
                        
                        slidePanel.populateDisplay(currentList.get(selectedTaskID));
                    }
                }
                
                event.consume();
                
            } else if( event.getKeyCode() == KeyEvent.VK_DOWN ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( slidePanel.isVisible() && event.isControlDown() ){
                    
                    JScrollPane tempScrollPane = slidePanel.getDisplayScrollComponent();
                    JScrollBar verticalScrollBar = tempScrollPane != null ? tempScrollPane.getVerticalScrollBar() : null;
                    
                    if( verticalScrollBar != null && verticalScrollBar.isVisible() ){
                    
                        int currentScrollValue = verticalScrollBar.getValue();
                        int tempScrollUnitDifference = verticalScrollBar.getUnitIncrement(1);
                        verticalScrollBar.setValue(currentScrollValue + tempScrollUnitDifference);
                        
                        event.consume();
                        
                        return;
                    }
                }
                
                displayPane.selectNextTask();
                
                if( currentNavigationBars != null && 
                        currentNavigationBars.size() > 0 && 
                        currentNavigationBars.get(0).isVisible() ){
                        
                    long taskID = displayPane.getCurrentSelectedTaskID();
                    
                    if( taskID >= 0 ){
                        
                        long tempTaskID = currentList.get((int)displayPane.getCurrentSelectedTaskID()).getID();
                        
                        currentNavigationBars.get(0).setMessage(planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + tempTaskID, "F1");
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    int selectedTaskID = (int)displayPane.getCurrentSelectedTaskID();   //Possible loss of data
                    
                    if( selectedTaskID >= 0 && selectedTaskID < currentList.size() ){
                        
                        slidePanel.populateDisplay(currentList.get(selectedTaskID));
                    }
                }
                
                event.consume();
                
            } else{
                
                if( !command.isFocusOwner() ){
                    
                    if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                        
                        int selectedTaskID = (int)displayPane.getCurrentSelectedTaskID();   //Possible loss of data
                        
                        if( selectedTaskID >= 0 && selectedTaskID < currentList.size() ){
                            
                            slidePanel.slideOut(currentList.get(selectedTaskID));
                        }
                        
                        event.consume();
                        
                        return;
                        
                    } else{
                        
                        characterToTransfer = '\0';
                        isBackspacePressed = false;
                        
                        handleKeys( filterKeys(event.getKeyChar()) );
                        
                        command.requestFocusInWindow();
                        
                        event.consume();
                    }
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
    	sectionTitle.setBounds(6, 30, 497, 33);
    	sectionTitle.setFont( new Font( "Arial", Font.BOLD, 24 ) );
    	sectionTitle.setForeground( new Color( 255,255,255,200 ) );
    	sectionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(sectionTitle);
        sectionTitle.setText("All Tasks");
    }
    
    private void prepareSectionTitleLine(){
    	
    	sectionTitleLine = new JLabel();
    	sectionTitleLine.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/titleLine.png")));
    	sectionTitleLine.setBounds(-142, 28, 643, 33);
    	contentPane.add(sectionTitleLine);
    }
    
    private void prepareSlidePanel(){
        
        slidePanelFrame = new JPanel();
        slidePanelFrame.setBounds(670, 45, 396, 520);
        contentPane.add(slidePanelFrame);
        slidePanelFrame.setLayout(null);
        slidePanelFrame.setOpaque(false);
        //slidePanelFrame.setFocusable(false);
        
        slidePanel = new SliderPanel(0, 198);
        slidePanel.setBounds(198, 0, 202, 520);
        slidePanelFrame.add(slidePanel);
    }
    
    private void prepareNavigationPanel(){
        
        navigationPanel = new JTextPane();
        navigationPanel.setBounds(10, 0, 188, 519);
        slidePanelFrame.add(navigationPanel);
        
        navigationPanel.setEditable(false);
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(null);
        navigationPanel.setHighlighter(null);
        navigationPanel.setFocusable(false);
        navigationPanel.setFont( new Font( "Arial", Font.PLAIN, 2 ) );
        
        navigationPanel.setText( "\n\n\n\n\n\n\n\n\n\n\n\n\n" );
        
        currentNavigationBars = generateContentForNavigationBars( currentDisplayState );
        addNavigationBarsToPanel(currentNavigationBars);
    }
    
    private ArrayList<NavigationBar> generateContentForNavigationBars( DisplayState displayState ){
        
        ArrayList<NavigationBar> tempList = new ArrayList<NavigationBar>();
        
        if( displayState != null && 
            planner.Constants.NAVIGATION_BAR_STRING_CONTENTS_SIZE == planner.Constants.NAVIGATION_BAR_STRING_CONTENTS.length ){
            
            int key = 1;
            
            TaskList tempTaskList;
            
            // More info on current task
            if( currentList.size() > 0 ){
                
                long taskID = displayPane.getCurrentSelectedTaskID();
                
                if( taskID >= 0 ){
                    
                    tempList.add( new NavigationBar( 
                            planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + currentList.get((int)displayPane.getCurrentSelectedTaskID()).getID(), 
                            "F" + key ) );
                    
                } else{
                    
                    tempList.add( new NavigationBar(null,null) );
                }
                
            } else{
                
                tempList.add( new NavigationBar(null,null) );
            }
            ++key;
            
            // tutorial
            tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[1], "F" + key ));
            ++key;
            
            // Quick keys
            tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[2], "F" + key ));
            ++key;
            
            // Today tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TODAY ){
                
                tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[4], "F" + key ));
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
            
            // all tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.ALL ){
                
                tempTaskList = Engine.getAllTasks();
                
                if( tempTaskList.size() <= 1 ){
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[6], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[7], "F" + key ));
                }
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
            
            // Tentative tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TENTATIVE ){
                
                tempTaskList = Engine.getTentativeTasks();
                
                if( tempTaskList.size() <= 1 ){
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[8], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[9], "F" + key ));
                }
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
            
            // Overdue tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.OVERDUE ){
                
                tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[10], "F" + key ));
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
            
            // Recurring tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.RECURRING ){
                
                tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[12], "F" + key ));
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
            
            // Done tasks
            if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.DONE ){
                
                tempTaskList = Engine.getTentativeTasks();
                
                if( tempTaskList.size() <= 1 ){
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[14], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[15], "F" + key ));
                }
                
            } else{
                
                tempList.add( new NavigationBar(null,null ) );
            }
            ++key;
        }
        
        return tempList;
    }
    
    private void addNavigationBarsToPanel( ArrayList<NavigationBar> listOfNavigationBarComponents ){
        
        try{
            
            if( listOfNavigationBarComponents != null && listOfNavigationBarComponents.size() > 0 ){

                StyledDocument styledDocument = navigationPanel.getStyledDocument();
                
                Iterator<NavigationBar> iterator = listOfNavigationBarComponents.iterator();
                
                NavigationBar tempNavigationBar;
                
                while( iterator.hasNext() ){
                    
                    tempNavigationBar = iterator.next();
                    
                    if( tempNavigationBar.isVisible() ){
                        
                        Style style = styledDocument.addStyle("component", null);
                        StyleConstants.setComponent(style, tempNavigationBar);
                        
                        styledDocument.insertString( styledDocument.getLength(), "component", style );
                    }
                }
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    private void prepareBackground(){
        
        // Adding UI background
        background = new JLabel();
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 867, 587);
        contentPane.add(background);
    }
    
	private void prepareDisplay(){
        
    	displayPane = new DisplayPane();
        displayPane.setBounds(25, 83, 630, 430);
        
        contentPane.add(displayPane);
        
        // Copying of all tasks
        currentList = new TaskList( Engine.getAllTasks() );
        
        // Display all tasks as default screen for now
        displayPane.addTasksToDisplay(currentList);
        
        displayPane.requestFocusInWindow();
        
        addKeyBindingsToDisplay(displayPane);
        
        currentDisplayState = new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, KeyEvent.VK_F4 );
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
        command.setBounds(34, 530, 610, 33);
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
        
        characterToTransfer = '\0';
        isBackspacePressed = false;
    }
    
    private void addKeyBindingsToCommandTextField( JTextField currentCommand ){
        
        if( currentCommand != null ){
            
            currentCommand.addKeyListener(new KeyListener(){
                
                @Override
                public void keyPressed( KeyEvent e ){
                        
                    handleKeyEvent(e);
                    
                    characterToTransfer = '\0';
                    isBackspacePressed = false;
                    
                    return;
                }
        
                @Override
                public void keyTyped(KeyEvent e) {
                    
                    if( characterToTransfer != '\0' ){
                        
                        e.consume();
                    }
                    
                    return;
                }
        
                @Override
                public void keyReleased(KeyEvent e) {
                    
                    e.consume();
                    
                    return;
                }
            });
        }
    }
    
    private void addFocusListenerToCommandTextField( JTextField currentCommand ){
        
        if(currentCommand != null){
            
            currentCommand.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    
                    command.setForeground( new Color( 0,0,0 ) );
                    
                    String additionalCharacterToAdd = (characterToTransfer != '\0' ? characterToTransfer + "" : "");
                    
                    if( isMessageDisplayed ){
                        
                        command.setText(additionalCharacterToAdd);
                        
                        isMessageDisplayed = false;
                        
                    } else{
                        
                        String finalString = command.getText() + additionalCharacterToAdd;
                        
                        if( isBackspacePressed && finalString.length() > 0 ){
                            
                            finalString = finalString.substring(0, finalString.length()-1);
                        }
                        
                        command.setText(finalString);
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
                    
                    characterToTransfer = '\0';
                    isBackspacePressed = false;
                }
            });
        }
    }

    private void prepareCloseButton(){
        
        closeButton = new JLabel();
        closeButton.setBounds(819, 13, 27, 27);
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
        minimiseButton.setBounds(782, 12, 28, 28);
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
        dragPanel.setBounds(0, 0, 867, 587);
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
