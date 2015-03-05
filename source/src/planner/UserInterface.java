package planner;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class UserInterface extends JFrame {

    private JPanel contentPane;
    private JTextField command;
    
    private JScrollPane displayScrollPane;
    private JTextPane display;
    private JTextPane tentativeDisplay;
    private JScrollPane tentativeDisplayScrollPane;
    
    /**
     * Launch the application.
     */
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

    /**
     * Create the frame.
     */
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
        displayScrollPane.setBounds(40, 93, 526, 313);
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
        command.setBounds(40, 433, 521, 33);
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
        tentativeDisplayScrollPane.setBounds(588, 93, 148, 373);
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
}
