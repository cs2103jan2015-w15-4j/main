package planner;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    private JPanel contentPane;
    
    private JTextField command;
    
    private JTextPane tentativeDisplay;
    private JScrollPane tentativeDisplayScrollPane;
    
    private JLabel closeButton;
    private JLabel minimiseButton;
    private JLabel dragPanel;
    private JLabel sectionTitle;
    private JLabel sectionTitleLine;
    
    private DisplayPane displayPane;
    
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
        setBounds(100, 100, 781, 494);
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
        
        setUndecorated(true);
        
        setLocationRelativeTo(null);
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
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 781, 494);
        contentPane.add(background);
    }
    
	private void prepareDisplay(){
        
    	displayPane = new DisplayPane();
        displayPane.setBounds(25, 83, 548, 337);
        contentPane.add(displayPane);
        
        // Copying of all tasks
        currentList = new TaskList( Engine.getAllTasks() );
        
        // Display all tasks as default screen for now
        displayPane.addTasksToDisplay(currentList);
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
        command.setBounds(30, 433, 539, 33);
        contentPane.add(command);
        command.setColumns(10);
        
        // Setting command text field attributes
        command.setBorder(null);
        command.setOpaque(false);
        command.setFont( new Font( "Arial", Font.BOLD, 20 ));
        command.setForeground(new Color( 128,128,128 ));
        command.setText("Enter commands here");
        
        // Set up listeners for command text field
        command.addFocusListener(new FocusListener(){

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
                
                if( !isMessageDisplayed || input.length() <= 0 ){
                    
                    command.setText("Enter commands here");
                    
                    isMessageDisplayed = true;
                    
                } else{
                	
                	command.setText(input);
                }
            }
        });
        
        command.addKeyListener(new KeyListener(){
                    
        	@Override
            public void keyPressed( KeyEvent e ){
                
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
                    
                    if( command.getText().length() <= 0 ){
                        
                        e.consume();
                    } 
                    
                } else if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                       
                    String input = command.getText();
                    
                    TaskList tempTaskList;
                    
                    long newTaskNumber;
                    
                    if( input.length() > 0 ){
                        
                    	planner.Constants.COMMAND_TYPE commandType = Engine.process(input);
                    	
                    	tempTaskList = Engine.getAllTasks();
                    	
                    	switch( commandType ){
                    	
                    		case ADD:
                    			
                    			if( tempTaskList.size() > currentList.size() &&
                    			    (newTaskNumber = compareList( currentList, tempTaskList )) > 0 ){
                    					
                    					command.setText( "Task added successfully" );
                    					
                    					currentList.copyTaskList(tempTaskList);
                        				
                    					displayPane.clearDisplay();
                    					
                        				displayPane.addTasksToDisplay(currentList);
                        				
                        				displayPane.selectTask(newTaskNumber);
                        				
                				} else{
                					
                				    command.setText( "Failed to add task" );
                				}
                    			
                    			break;
                    			
                    		case UPDATE:
                    		    
                    			newTaskNumber = compareList( currentList, tempTaskList );
                    			
                    			// changed back to newTaskNumber > 0 after fixing bug that caused data of a task in both taskList 
                    			// (currentList and tempTaskList) to change even though the program was only changing data of the task 
                    			// in only one taskList (tempTaskList)
                    			if( newTaskNumber > 0 ){
                    				
                    				command.setText( "Task updated successfully" );
                    				
                    				currentList.copyTaskList(tempTaskList);
                    				
                    				displayPane.clearDisplay();
                    				
                    				displayPane.addTasksToDisplay(currentList);
                    				
                    				displayPane.selectTask(newTaskNumber);
                    				
                    			} else{
                    			    
                    			    command.setText( "Failed to update task" );
                    			}
                    			
                    			break;
                    			
                    		case DELETE:
                    			
                    			if( tempTaskList.size() < currentList.size() &&
                    			    (newTaskNumber = compareList( currentList, tempTaskList )) > 0 ){
                    				
                    				command.setText( "Task deleted successfully" );
                    				
                    				currentList.copyTaskList(tempTaskList);
                    				
                    				displayPane.clearDisplay();
                    				
                    				displayPane.addTasksToDisplay(currentList);
                    				
                    				displayPane.selectTask( newTaskNumber - 1 );
                    				
                    			} else{
                    				
                    				command.setText( "Failed to delete task" );
                    			}
                    			
                    			break;
                    			
                    	    //ADDED CODE HERE
                            case DONE:
                                    
                                command.setText( "Task completed successfully" );
                                    
                                currentList.copyTaskList(tempTaskList);
                                    
                                displayPane.clearDisplay();
                                    
                                displayPane.addTasksToDisplay(currentList);
                                
                                break;
                            //END OF ADDED CODE
                                
                    		case INVALID:
                    			
                    			command.setText( "Invalid Command" );
                    			
                    			break;
                    			
                    		default:
                    			
                    			command.setText( "Feature not supported in V0.1" );
                    			
                    			break;
                    	}
                    	
                    	displayPane.requestFocus();
                    	
                    	isMessageDisplayed = true;
                    	
                    } else{
                        
                    	e.consume();
                    }
                }
            }
    
        	@Override
            public void keyTyped(KeyEvent e) {}
    
        	@Override
            public void keyReleased(KeyEvent e) {}
            
        });
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
        
        closeButton.addMouseListener( new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                    
                	Engine.exit();
                	
                    System.exit(0);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void prepareMinimiseButton(){
        
        minimiseButton = new JLabel();
        minimiseButton.setBounds(707, 12, 28, 28);
        contentPane.add(minimiseButton);
        
        minimiseButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        minimiseButton.addMouseListener( new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                    
                    setState( UserInterface.ICONIFIED );
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
            
        } );
    }

    private void prepareDragPanel(){
        
        dragPanel = new JLabel();
        dragPanel.setBounds(0, 0, 780, 490);
        contentPane.add(dragPanel);
        
        dragPanel.addMouseMotionListener( new MouseMotionListener(){ 

            @Override
            public void mouseDragged(MouseEvent e) {
                
                int mouseDragXCoordinate = e.getXOnScreen();
                int mouseDragYCoordinate = e.getYOnScreen();
                
                setLocation( mouseDragXCoordinate - mouseXCoordinate,
                             mouseDragYCoordinate - mouseYCoordinate);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        });
        
        dragPanel.addMouseListener( new MouseListener(){ 

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                mouseXCoordinate = e.getX();
                mouseYCoordinate = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
