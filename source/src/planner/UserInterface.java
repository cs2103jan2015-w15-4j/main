package planner;

import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import org.

/**
 *
 * @author jia jun
 */
public class UserInterface extends javax.swing.JFrame {

    // J-components
    private JPanel mainFrame;
    private JLabel Background;
    private JLabel closeButton;
    private JLabel dragPanel;
    private JLabel minimiseButton;
    
    private JTextField command;
    
    private JPanel commandPanel;
    
    private JTextPane display;
    private JScrollPane displayScroll;
    
    private JTextPane tentative;
    private JScrollPane tentativeScroll;
    
    public UserInterface(){
        
        setVisible(true);
        
        minimiseButton = new JLabel();
        closeButton = new JLabel();
        dragPanel = new JLabel();
        Background = new JLabel();
        
        commandPanel = new JPanel();
        command = new JTextField();
        
        tentativeScroll = new JScrollPane();
        tentative = new JTextPane();
        
        displayScroll = new JScrollPane();
        display = new JTextPane();
        
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);
        
        pack();
        setLocationRelativeTo(null);
    }
}
