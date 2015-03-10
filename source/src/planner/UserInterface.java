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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    private JPanel contentPane;
    
    private JTextField command;
    
    private JScrollPane displayScrollPane;
    private JTextPane display;
    
    private JTextPane tentativeDisplay;
    private JScrollPane tentativeDisplayScrollPane;
    
    private JLabel closeButton;
    private JLabel minimiseButton;
    private JLabel dragPanel;
    
    // Used for drag logic
    private int mouseXCoordinate;
    private int mouseYCoordinate;
    
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
        prepareBackground();
        
        
        setUndecorated(true);
        
        setLocationRelativeTo(null);
    }
    
    private void prepareBackground(){
        
        // Adding UI background
        JLabel background = new JLabel("");
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 781, 494);
        contentPane.add(background);
    }
    
    private void prepareDisplay(){
        
        displayScrollPane = new JScrollPane();
        displayScrollPane.setBounds(37, 107, 545, 300);
        contentPane.add(displayScrollPane);
        
        display = new JTextPane();
        displayScrollPane.setViewportView(display);
        
        displayScrollPane.setBorder(null);
        displayScrollPane.setOpaque(false);

        display.setBorder(null);
        display.setOpaque(false);
        
        display.setEditable(false);
        display.setFont( new Font( "Arial", Font.BOLD, 16 ));
        
        display.setText( "This is a sample display box\n" );
        display.setForeground(new Color( 255, 255, 255 ));
        
        displayScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        displayScrollPane.getViewport().setOpaque(false);
    }
    
    private void prepareCommandTextField(){
        
        // Adding command text field
        command = new JTextField();
        command.setBounds(40, 433, 539, 33);
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
                command.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                
                command.setForeground(new Color( 128,128,128 ));
                
                if( !command.getText().equals("Invalid Command") ){
                    
                    command.setText("Enter commands here");
                }
            }
        });
        
        command.addKeyListener(new KeyListener(){
                    
            public void keyPressed( KeyEvent e ){
                
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
                    
                    if( command.getText().length() <= 0 ){
                        
                        e.consume();
                    } 
                    
                } else if( e.getKeyCode() == KeyEvent.VK_ENTER ){
                        
                    try{
                        
                        String input = command.getText();
                        
                        if( input.equalsIgnoreCase("clear") ){
                            
                            display.setCaretPosition(0);
                            
                            display.setText(null);
                            
                            command.setCaretPosition(0);

                            command.setText(null);
                            
                        } else if( input.length() > 0 ){
                        
                            StyledDocument doc = display.getStyledDocument();

                            doc.insertString(doc.getLength(), input + "\n", null );

                            command.setCaretPosition(0);

                            command.setText("");
                            
                        } else{
                            
                            command.setText( "Invalid Command" );
                            
                            display.requestFocus();
                        }
                        
                    } catch( BadLocationException badLocationException ){
                        
                        command.setText( "Bad insert position :(" );
                    }
                }
            }
    
            public void keyTyped(KeyEvent e) {}
    
            public void keyReleased(KeyEvent e) {}
            
        });
    }

    private void prepareTentativeDisplay(){
        
        tentativeDisplayScrollPane = new JScrollPane();
        tentativeDisplayScrollPane.setBounds(601, 99, 152, 364);
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
        
        closeButton = new JLabel("");
        closeButton.setBounds(744, 13, 27, 27);
        contentPane.add(closeButton);
        
        closeButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        closeButton.addMouseListener( new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                    
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
        
        minimiseButton = new JLabel("");
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
        
        dragPanel = new JLabel("");
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
