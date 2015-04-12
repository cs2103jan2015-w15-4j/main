package planner;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class testt extends JFrame {

    private JPanel contentPane;
    private SliderPanel sliderPanel;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    testt frame = new testt();
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
    private int count = 0;
    private Task tempTask = new Task( "good day", "th\n\n\n\n\n\n\n\n\n\n\nis is a sa\n\n\n\n\n\n\n\n\n\n\nmple desc\n\n\n\n\n\nription", new Date(), 5, "sample tag", 14 );
    
    public testt() {
        
        sliderPanel = new SliderPanel( (2*(699/3))-15, 699 );
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 669, 450);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        sliderPanel.setBounds(660, 11, 202, 391);
        contentPane.add(sliderPanel);
        
        System.out.println( "width = " + getWidth());
        
        sliderPanel.slideOut(tempTask, true);
        
        System.out.println( "x = " + sliderPanel.getBounds().x );
        
        JLabel lblJgnmjfnmhm = new JLabel("jgnmjfnmhm");
        lblJgnmjfnmhm.setBounds(37, 52, 147, 68);
        contentPane.add(lblJgnmjfnmhm);
        
        textField = new JTextField();
        textField.setBounds(10, 202, 153, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        
        textField.addKeyListener( new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent event) {

                if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                    
                    if( count == 0 ){
                        
                        sliderPanel.slideIn();
                    }
                    
                    else{
                        
                        sliderPanel.slideOut(tempTask, true);
                    }
                    
                    count ^= 1;
                }
                
                
                else if( event.getKeyCode() == KeyEvent.VK_S ){
                    
                    sliderPanel.showScrollBar();
                }
                
                
                else if( event.getKeyCode() == KeyEvent.VK_H ){
                    
                    sliderPanel.hideScrollBar();
                }
            }
            
        });
    }
}
