package planner;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
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

import planner.Constants.CommandType;
import planner.Constants.DisplayStateFlag;
import sun.util.logging.PlatformLogger.Level;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS START HERE
    ///////////////////////////////////////////////////////////////////// 
    public void processCommand( String input ){
        
        planner.Constants.CommandType commandType = Engine.process(input);
        
        switch( commandType ){
        
            case ADD:
                
                handleAddOperation();
                
                break;
                
            case UPDATE:
                
                handleUpdateOperation();
                
                break;
                
            case DELETE:
                
                handleDeleteOperation();
                
                break;
                
            case DONE:
                    
                handleDoneOperation();
                
                break;
                
            case INVALID:
                
                handleInvalidOperation();
                
                break;
                
            case SEARCH:
                
                handleSearch( input );
                
                break;
            
            case SETNOTDONE:
                
                handleSetNotDone();
                
                break;
                
            case UNDO:
                
                handleUndo();
                
                break;
                
            default:
                
                handleUnexpectedOperation();
            
                break;
        }
    }
    
    private void handleUndo(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
            
            displayStateStack.push(upcomingDisplayState);
        }
        
        updateGUIView( sectionTitleString, currentList, null );
        
        commandPanel.setText( "Previous keyboard command undone successfully", true );
    }
    
    private void handleSetNotDone(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        long modifiedTaskID = Engine.lastModifiedTask();
        Task modifiedTask = currentList.getTaskByID(modifiedTaskID);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( sectionTitleString, currentList, modifiedTask );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                TaskList tempTaskList = Engine.getAllTasks();
                
                modifiedTask = tempTaskList.getTaskByID(modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList.copyTaskList(tempTaskList);
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( "All Tasks", currentList, modifiedTask );
                    
                } else{
                    
                    commandPanel.setText( "Fail to mark task as not done", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Fail to mark task as not done", true );
                
                return;
            }
        }

        commandPanel.setText( "Task marked not done successfully", true );
    }
    
    private void handleSearch( String userInput ){
        
        TaskList tempTaskList = Engine.getSearchResult();
        
        if( tempTaskList != null && tempTaskList.size() > 0 ){
            
            currentList.copyTaskList(tempTaskList);
            
            displayPane.clearDisplay();
            
            displayPane.displayByDate(currentList);
            
            displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.WORD_SEARCH, userInput, userInput, null ));
            
            updateGUIView(userInput, currentList, displayPane.getCurrentSelectedTask());
            
            commandPanel.setText( "Search success", true );
            
        } else{
            
            commandPanel.setText( "We cannot find any task containing the search phrase :/", true );
        }
    }
    
    private void updateGUIView( String sectionTitleString, TaskList taskList, Task task ){
        
        displayPane.clearDisplay();
        
        displayPane.displayByDate(taskList);
        
        displayPane.selectGivenTask(task);
        
        currentNavigationBars = generateContentForNavigationBars( displayStateStack.peek() );
        addNavigationBarsToPanel(currentNavigationBars);
        
        if( task != null ){
            
            slidePanel.populateDisplay(task);
            
        } else{
            
            slidePanel.populateDisplay(displayPane.getCurrentSelectedTask());
        }
        
        if(sectionTitleString != null){
            
            sectionTitle.setText(sectionTitleString);
        }
    }
    
    private String getDisplaySectionTitle( DisplayState currentDisplayState ){
        
        if( currentDisplayState != null ){
        
            DisplayStateFlag currentDisplayStateFlag  = currentDisplayState.getdisplayStateFlag();
            
            switch(currentDisplayStateFlag){
            
                case TENTATIVE:
                    
                    return "Floating Tasks";
                    
                case TODAY:
                    
                    return "All tasks due today";
                    
                case OVERDUE:
                    
                    return "Overdue Tasks";
                    
                case DONE:
                    
                    return "Completed tasks";
                    
                case WORD_SEARCH:
                case DATE_SEARCH:
                    
                    return currentDisplayState.getTitle();
                    
                default:
                    
                    break;
            }
        }
        
        return "All Tasks";
    }
    
    private DisplayState updateCurrentList( DisplayState currentDisplayState ){
        
        if( currentDisplayState != null ){
            
            DisplayStateFlag currentDisplayStateFlag  = currentDisplayState.getdisplayStateFlag();
            
            String searchString;
            
            switch(currentDisplayStateFlag){
            
                case TENTATIVE:
                    
                    currentList.copyTaskList(Engine.getTentativeTasks());
                    
                    return currentDisplayState;
                    
                case DONE:
                    
                    currentList.copyTaskList(Engine.getDoneTasks());
                    
                    return currentDisplayState;
                    
                case WORD_SEARCH:
                case DATE_SEARCH:
                    
                    searchString = currentDisplayState.getCommand();
                    
                    if ( Engine.process(searchString) == CommandType.SEARCH ){
                        
                        currentList.copyTaskList(Engine.getSearchResult());
                        
                        return currentDisplayState;
                    } 

                    break;
                    
                default:
                    
                    break;
            }
        }
            
        currentList.copyTaskList(Engine.getAllTasks());
        
        return new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, 
                                 new KeyEvent( displayPane, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                                 0, KeyEvent.VK_F6, '\0', KeyEvent.KEY_LOCATION_STANDARD) ); 
    }
    
    private void handleAddOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        long modifiedTaskID = Engine.lastModifiedTask();
        Task modifiedTask = currentList.getTaskByID(modifiedTaskID);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( sectionTitleString, currentList, modifiedTask );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                TaskList tempTaskList = Engine.getAllTasks();
                
                modifiedTask = tempTaskList.getTaskByID(modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList.copyTaskList(tempTaskList);
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( "All Tasks", currentList, modifiedTask );
                    
                } else{
                    
                    commandPanel.setText( "Failed to add task", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Failed to add task", true );
                
                return;
            }
        }

        commandPanel.setText( "Task added successfully", true );
    }
    
    private void handleUpdateOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        long modifiedTaskID = Engine.lastModifiedTask();
        Task modifiedTask = currentList.getTaskByID(modifiedTaskID);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( sectionTitleString, currentList, modifiedTask );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                TaskList tempTaskList = Engine.getAllTasks();
                
                modifiedTask = tempTaskList.getTaskByID(modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList.copyTaskList(tempTaskList);
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( "All Tasks", currentList, modifiedTask );
                    
                } else{
                    
                    commandPanel.setText( "Failed to update task", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Failed to update task", true );
                
                return;
            }
        }

        commandPanel.setText( "Task updated successfully", true );
    }
    
    private void handleDoneOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        long modifiedTaskID = Engine.lastModifiedTask();
        Task modifiedTask = currentList.getTaskByID(modifiedTaskID);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( sectionTitleString, currentList, modifiedTask );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                TaskList tempTaskList = Engine.getAllTasks();
                
                modifiedTask = tempTaskList.getTaskByID(modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList.copyTaskList(tempTaskList);
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( "All Tasks", currentList, modifiedTask );
                    
                } else{
                    
                    commandPanel.setText( "Fail to mark task as done", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Fail to mark task as done", true );
                
                return;
            }
        }

        commandPanel.setText( "Task marked done successfully", true );
    }
    
    private void handleDeleteOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = getDisplaySectionTitle( upcomingDisplayState );
        
        if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
            
            displayStateStack.push(upcomingDisplayState);
        }
        
        updateGUIView( sectionTitleString, currentList, null );
        
        commandPanel.setText( "Task deleted successfully", true );
    }
    
    private void handleInvalidOperation(){
        
        commandPanel.setText( "Invalid Command", true );
    }
    
    public void handleUnexpectedOperation(){
        
        commandPanel.setText( "Feature not supported in V0.3", true );
    }
    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS END HERE
    /////////////////////////////////////////////////////////////////////
    
    private JPanel contentPane;
    private JPanel slidePanelFrame;
    
    private SliderPanel slidePanel;
    
    //private JTextField command;
    private CommandTextbox commandPanel;
    private JTextPane commandInputField;
    
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
    
    private ArrayList<NavigationBar> currentNavigationBars;
    
    private final static Logger userInterfaceLogger = Logger.getLogger(UserInterface.class.getName());
    
    private char characterToTransfer;
    private boolean isBackspacePressed;
    
    private DisplayStateStack displayStateStack;
    private final int maxNumOfDisplayStates = 100;
    
    private boolean isWindowCurrentlyActive;
    
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
            
            if( commandPanel != null ){
                
                if( commandPanel.handleKeyEvent(event) ){
                   
                    return;
                }
            }
            
            if( event.getKeyCode() == KeyEvent.VK_PAGE_UP || 
                event.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                JScrollBar verticalScrollBar = displayPane.getVerticalScrollBar();
                
                int currentScrollValue = verticalScrollBar.getValue();
                int tempScrollUnitDifference = (event.getKeyCode() == KeyEvent.VK_PAGE_UP ? -verticalScrollBar.getBlockIncrement(-1) : verticalScrollBar.getBlockIncrement(1));
                verticalScrollBar.setValue( currentScrollValue + tempScrollUnitDifference );
                
            } else if( event.getKeyCode() == KeyEvent.VK_F6 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.ALL ){
                    
                    currentList.copyTaskList(Engine.getAllTasks());
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, event ));
                    updateGUIView( "All Tasks", currentList, null );
                }
                
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
                    
                    Task currentTask = displayPane.getCurrentSelectedTask();
                    
                    if( currentTask != null ){
                        
                        currentNavigationBars.get(0).setMessageToView(planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + currentTask.getID(), "F1");
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    Task tempTask = displayPane.getCurrentSelectedTask();
                    
                    if( tempTask != null ){
                        
                        slidePanel.populateDisplay(tempTask);
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
                    
                    Task currentTask = displayPane.getCurrentSelectedTask();
                    
                    if( currentTask != null ){
                        
                        currentNavigationBars.get(0).setMessageToView(planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + currentTask.getID(), "F1");
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    Task tempTask = displayPane.getCurrentSelectedTask();
                    
                    if( tempTask != null ){
                        
                        slidePanel.populateDisplay(tempTask);
                    }
                }
                
                event.consume();
                
            } else{
                
                if( !commandInputField.isFocusOwner() ){
                    
                    if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                        
                        Task tempTask = displayPane.getCurrentSelectedTask();
                        
                        if( tempTask != null ){
                            
                            slidePanel.slideOut( tempTask );
                        }
                        
                        event.consume();
                        
                        return;
                        
                    } else{
                        
                        characterToTransfer = '\0';
                        isBackspacePressed = false;
                        
                        handleKeys( filterKeys(event.getKeyChar()) );
                        
                        commandInputField.requestFocusInWindow();
                        
                        event.consume();
                    }
                }
                
                if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
                    
                    if( commandInputField.getText().length() <= 0 ){
                        
                        event.consume();
                    } 
                    
                } else if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                       
                    String input = commandInputField.getText();
                    
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
        
        currentNavigationBars = generateContentForNavigationBars( displayStateStack.peek() );
        addNavigationBarsToPanel(currentNavigationBars);
    }
    
    private ArrayList<NavigationBar> generateContentForNavigationBars( DisplayState currentDisplayState ){
        
        ArrayList<NavigationBar> tempList = new ArrayList<NavigationBar>();
        
        if( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS_SIZE == planner.Constants.NAVIGATION_BAR_STRING_CONTENTS.length ){
            
            int key = 1;
            
            TaskList tempTaskList;
            
            // More info on current task
            if( currentList.size() > 0 ){
                
                //long taskID = displayPane.getCurrentSelectedTaskID();
                
                Task tempTask = displayPane.getCurrentSelectedTask();
                
                //if( taskID >= 0 ){
                          
                if( tempTask != null ){
                    
                    tempList.add( new NavigationBar( 
                            planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + tempTask.getID(), 
                            "F" + key ) );
                    
                } else{
                    
                    tempList.add( new NavigationBar(null) );
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // Previous view
            if( displayStateStack.size() > 1 ){
                
                tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[1], "F" + key ) );
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // tutorial
            tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[2], "F" + key ));
            ++key;
            
            // Quick keys
            tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[4], "F" + key ));
            ++key;
            
            // Today tasks
            if( currentDisplayState != null ){
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TODAY ){
                    
                    tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[6], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[6] ));
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // all tasks
            if( currentDisplayState != null ){
                
                tempTaskList = Engine.getAllTasks();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.ALL ){
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[7], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[8], "F" + key ));
                    }
                    
                } else{
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[7] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[8] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // Tentative tasks
            if( currentDisplayState != null ){
                
                tempTaskList = Engine.getTentativeTasks();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TENTATIVE ){
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[9], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[10], "F" + key ));
                    }
                    
                } else{
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[9] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[10]));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Overdue tasks
            if( currentDisplayState != null ){
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.OVERDUE ){
                    
                    tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[12], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( 0 + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[12] ));
                }

            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Done tasks
            if( currentDisplayState != null ){
                
                tempTaskList = Engine.getDoneTasks();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.DONE ){
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[13], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[14], "F" + key ));
                    }
                    
                } else{
                    
                    if( tempTaskList.size() == 1 ){
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[13] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( tempTaskList.size() + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[14] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Settings
            if( currentDisplayState != null ){
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.SETTINGS ){
                    
                    tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[15], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[15] ) );
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
        }
        
        return tempList;
    }
    
    private void addNavigationBarsToPanel( ArrayList<NavigationBar> listOfNavigationBarComponents ){
        
        try{
            
            if( listOfNavigationBarComponents != null && listOfNavigationBarComponents.size() > 0 ){

                navigationPanel.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n");
                
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
        
        displayStateStack = new DisplayStateStack(maxNumOfDisplayStates);
        
        // Copying of all tasks
        currentList = new TaskList( Engine.getAllTasks() );
        
        // Display all tasks as default screen for now
        //displayPane.addTasksToDisplay(currentList);
        displayPane.displayByDate(currentList);
        
        displayPane.requestFocusInWindow();
        
        addKeyBindingsToDisplay(displayPane);
        
        displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, 
                                                new KeyEvent( displayPane, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                                                              0, KeyEvent.VK_F6, '\0', KeyEvent.KEY_LOCATION_STANDARD) ) );
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
    
    private ArrayList<Map.Entry<String, ArrayList<String>>> generateTutorialStringMappings( String [][]possibleCommands ){
        
        ArrayList<Map.Entry<String, ArrayList<String>>> mapping = new ArrayList<Map.Entry<String, ArrayList<String>>>();
        
        if( possibleCommands != null ){
            
            String sectionHeaderString;
            String []tempStringArray;
            ArrayList<String> newList = null;
            for( int i = 0; i < possibleCommands.length; ++i ){
                
                tempStringArray = possibleCommands[i];
                
                for( int j = 0; j < tempStringArray.length; ++j ){
                    
                    if( j > 0 ){
                        
                        newList.add(tempStringArray[j]);
                        
                    } else{
                        
                        newList = new ArrayList<String>();
                        
                        sectionHeaderString = tempStringArray[j];
                        
                        if( sectionHeaderString != null ){
                            
                            mapping.add( new AbstractMap.SimpleEntry<String, ArrayList<String>>( sectionHeaderString, newList ) );
                            
                        } else{
                            
                            break;
                        }
                    }
                }
            }
        }
        
        return mapping;
    }
    
    private void prepareCommandTextField(){
        
        // New command
        ArrayList<Map.Entry<String, ArrayList<String>>> possibleCommands = generateTutorialStringMappings( Constants.POSSIBLE_COMMANDS );
        
        commandPanel = new CommandTextbox( Constants.COMMAND_KEYWORDS, Constants.NONCOMMAND_KEYWORDS, possibleCommands );
        commandPanel.setBounds(34, 530, 610, 60);
        contentPane.add(commandPanel);
        commandInputField = commandPanel.getTextDisplay();
        commandPanel.setFontAttributes(new Font( "Arial", Font.BOLD, 20 ));
        commandPanel.setForegroundColor(new Color( 128,128,128 ));
        commandPanel.setText("Enter commands here", true);
        
        addKeyBindingsToCommandTextField(commandInputField);
        addFocusListenerToCommandTextField( commandPanel, commandInputField );
        
        // End of new Command
        
        /*
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
        addKeyBindingsToCommandTextField(command);*/
        
        characterToTransfer = '\0';
        isBackspacePressed = false;
    }
    
    private void addKeyBindingsToCommandTextField( JTextPane currentCommand ){
        
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
    
    /*
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
    }*/
    
    private void addFocusListenerToCommandTextField( final CommandTextbox commandTextbox, final JTextPane currentCommand ){
        
        if(currentCommand != null && commandTextbox != null){
            
            currentCommand.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    
                    commandTextbox.setSyntaxFilterOn();
                    
                    commandTextbox.setForegroundColor( new Color( 0,0,0 ) );
                    
                    String additionalCharacterToAdd = (characterToTransfer != '\0' ? characterToTransfer + "" : "");
                    
                    if( isMessageDisplayed ){
                        
                        commandTextbox.setText(additionalCharacterToAdd, false);
                        
                        isMessageDisplayed = false;
                        
                    } else{
                        
                        String finalString = currentCommand.getText() + additionalCharacterToAdd;
                        
                        if( isBackspacePressed && finalString.length() > 0 ){
                            
                            finalString = finalString.substring(0, finalString.length()-1);
                        }
                        
                        commandTextbox.setText(finalString, false);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    
                    commandTextbox.setSyntaxFilterOff();
                    
                    commandTextbox.hidePopupBox();
                    
                    commandTextbox.setForegroundColor( new Color( 128,128,128 ) );
                    
                    String input = currentCommand.getText();
                    
            
                    
                    if( !isMessageDisplayed && input.length() <= 0 ){
                        
                        commandTextbox.setText("Enter commands here", true);
                        
                        isMessageDisplayed = true;
                        
                    } else{
                        
                        commandTextbox.setText(input, true);
                    }
                    
                    characterToTransfer = '\0';
                    isBackspacePressed = false;
                }
            });
        }
    }
    
    /*
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
    }*/

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
