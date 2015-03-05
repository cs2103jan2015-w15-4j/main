package planner;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class UserInterface extends JFrame {

    private JPanel contentPane;

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
        
        setResizable(false);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setBounds(100, 100, 781, 494);
        contentPane = new JPanel();
        
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // UI background
        JLabel background = new JLabel("");
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 781, 494);
        
        
        
        contentPane.add(background);
        
        setUndecorated(true);
        
        setLocationRelativeTo(null);
    }
}
