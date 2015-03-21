package planner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class InvisibleButton extends JButton{

    public InvisibleButton(){
        
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
        setFocusable(true);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
    }
    
    @Override
    public Dimension getPreferredSize(){
        
        return new Dimension(10,15);
    }
}
