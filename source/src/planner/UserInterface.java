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

public class UserInterface extends JFrame {

    private JPanel contentPane;
    private JTextField command;

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
        prepareBackground();
        
        
        setUndecorated(true);
        
        setLocationRelativeTo(null);
    }
    
    void prepareBackground(){
        
        // Adding UI background
        JLabel background = new JLabel("");
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 781, 494);
        contentPane.add(background);
    }
    
    void prepareCommandTextField(){
        
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
                }
            }
    
            public void keyTyped(KeyEvent e) {}
    
            public void keyReleased(KeyEvent e) {}
            
        });
    }

    
}
