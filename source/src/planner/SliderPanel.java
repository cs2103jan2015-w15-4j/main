package planner;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import planner.Constants.TipType;

public class SliderPanel extends JComponent{
    
    private JLabel background;
    private JLabel downArrow;
    private JLabel upArrow;
    private JLabel tips;
    
    private JTextPane infoPanel;
    
    private JScrollPane infoPanelScrollPane;
    
    private InvisibleScrollBarUI scrollBarSkin;
    
    private JScrollBar verticalScrollBar;
    
    private final int width = 198;
    private final int height = 520;
    
    private int initialXCoordinate = 0;
    private int initialYCoordinate = 0;
    private int parentWidth;
    private int widthNotToMoveInto;
    
    private int currentXCoordinate;
    
    private boolean isCurrentlyVisible = true;
    
    private Timer timerSlideOut;
    private Timer timerSlideIn;
    
    @Override
    public Dimension getPreferredSize(){
        
        return new Dimension(width,height);
    }
    
    public SliderPanel( int widthNotToMoveInto, int parentWidth ) {
        
        //setBackground(new Color(0,0,0,0));
        
        prepareDisplayInfoPanel();
        prepareUpArrow();
        prepareDownArrow();
        prepareTips();
        prepareBackground();
        
        this.parentWidth = parentWidth > 0 ? parentWidth : 0;
        this.widthNotToMoveInto = widthNotToMoveInto > 0 ? widthNotToMoveInto : 0;
        
        setupTimerForSlideOut();
        setupTimerForSlideIn();
        
        setToInvisible();
    }
    
    private void setupTimerForSlideOut(){
        
        timerSlideOut = new Timer( 1, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if( currentXCoordinate == initialXCoordinate ){
                    
                    setToVisible();
                }
                
                setLocation(--currentXCoordinate, initialYCoordinate );
                
                if( timerSlideIn.isRunning() || currentXCoordinate <= widthNotToMoveInto ){
                    
                    timerSlideOut.stop();
                }
            }
            
        });
    }
    
    private void setupTimerForSlideIn(){
        
        timerSlideIn = new Timer( 1, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                setLocation(++currentXCoordinate, initialYCoordinate );
                
                if( currentXCoordinate >= parentWidth ){
                    
                    setToInvisible();
                    
                    timerSlideIn.stop();
                    
                    System.out.println( "Slide in stopped" );
                }
            }
            
        });
    }
    
    private void prepareUpArrow(){
        
        upArrow = new JLabel();
        upArrow.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/WhiteUpArrow.png")));
        upArrow.setBounds(94, 66, 20, 8);
        add(upArrow);
        hideUpArrow();
    }
    
    private void showUpArrow(){
        
        upArrow.setVisible(true);
    }
    
    private void hideUpArrow(){
        
        upArrow.setVisible(false);
    }
    
    private void prepareDownArrow(){
        
        downArrow = new JLabel();
        downArrow.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/WhiteDownArrow.png")));
        downArrow.setBounds(94, 430, 20, 8);
        add(downArrow);
        hideDownArrow();
    }
    
    private void showDownArrow(){
        
        downArrow.setVisible(true);
    }
    
    private void hideDownArrow(){
        
        downArrow.setVisible(false);
    }
    
    private void prepareTips(){
        
        tips = new JLabel();
        tips.setBounds(10, 452, 184, 67);
        add(tips);
        hideTip();
    }
    
    private void hideTip(){
        
        tips.setVisible(false);
    }
    
    private void showTip( TipType tipType ){
        
        tips.setVisible(true);
        
        switch(tipType){
        
            case UP_TIP:
                
                tips.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/Up.png")));
                
                break;
                
            case DOWN_TIP:
                
                tips.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/Down.png")));
                
                break;
                
            case UPDOWN_TIP:
                
                tips.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/UpAndDown.png")));
                
                break;
                
            default:
                
                hideTip();
                
                break;
        }
    }
    
    private void prepareBackground(){
        
        background = new JLabel();
        background.setIcon(new ImageIcon(SliderPanel.class.getResource("/planner/SliderPanel.png")));
        background.setBounds(0, 0, width, height);
        add(background);
    }
    
    private void prepareDisplayInfoPanel(){
        
        infoPanelScrollPane = new JScrollPane();
        infoPanelScrollPane.setBounds(10, 78, 182, 347);
        add(infoPanelScrollPane);
        
        infoPanel = new JTextPane();
        infoPanel.setBorder(null);
        infoPanel.setOpaque(false);
        infoPanel.setEditable(false);
        infoPanel.setHighlighter(null);
        infoPanel.setFocusable(false);
        
        infoPanelScrollPane.setViewportView(infoPanel);
        infoPanelScrollPane.setBorder(null);
        infoPanelScrollPane.setOpaque(false);
        infoPanelScrollPane.getViewport().setOpaque(false);
        infoPanelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        infoPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoPanelScrollPane.setFocusable(false);
        attachScrollAdjustmentListener(infoPanelScrollPane);
        
        verticalScrollBar = infoPanelScrollPane.getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        
        
        scrollBarSkin = new InvisibleScrollBarUI();
        verticalScrollBar.setUI( scrollBarSkin );
    }
    
    private void attachScrollAdjustmentListener( JScrollPane scrollPane ){
        
        if( scrollPane != null ){
            
            verticalScrollBar = scrollPane.getVerticalScrollBar();
            
            if( verticalScrollBar != null ){
                
                verticalScrollBar.addAdjustmentListener(new AdjustmentListener(){

                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent event) {

                        if( !verticalScrollBar.isVisible() ){
                            
                            hideDownArrow();
                            hideUpArrow();
                            hideTip();
                            
                        } else{
                            
                            int scrollBarExtent = verticalScrollBar.getModel().getExtent();
                            
                            if( verticalScrollBar.getValue() <= verticalScrollBar.getMinimum() ){
                                
                                hideUpArrow();
                                showDownArrow();
                                showTip( TipType.DOWN_TIP );
                                
                            } else if( verticalScrollBar.getValue() + scrollBarExtent >= verticalScrollBar.getMaximum() ){
                                
                                hideDownArrow();
                                showUpArrow();
                                showTip( TipType.UP_TIP );
                                
                            } else{
                                
                                showUpArrow();
                                showDownArrow();
                                showTip( TipType.UPDOWN_TIP );
                            }
                        }
                        
                    }
                    
                });
                
            }
        }
    }
    
    private void setToInvisible(){
        
        setVisible(false);
        
        isCurrentlyVisible = false;
    }
    
    private void setToVisible(){
        
        setVisible(true);
        
        isCurrentlyVisible = true;
    }
    
    public void slideOut( Task task ){
        
        if( task != null ){
            
            Point pointInParent = getLocation();
            initialXCoordinate = pointInParent != null ? pointInParent.x : 0;
            initialYCoordinate = pointInParent != null ? pointInParent.y : 0;
            
            //System.out.printf( "parentWidth = " + parentWidth + "\n" );
            
            if( initialXCoordinate <= widthNotToMoveInto ){
                
                return;
            }
            
            populateDisplay(task);
            
            currentXCoordinate = initialXCoordinate;
            
            timerSlideOut.start();
        }
    }
    
    public void slideIn(){
        
        if( currentXCoordinate >= parentWidth ){
            
            return;
        }
        
        timerSlideIn.start();
    }
    
    public boolean isVisible(){
        
        return isCurrentlyVisible;
    }
    
    public void hideScrollBar(){
        
        infoPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
    
    public void showScrollBar(){
        
        infoPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    public void populateDisplay( Task task ){
        
        infoPanel.setText("");
        
        if( task != null ){
            
            infoPanel.setForeground(new Color(255,255,255));
            
            Style bigBoldText = infoPanel.addStyle("big bold text", null);
            StyleConstants.setFontSize(bigBoldText, 15);
            
            Style smallText = infoPanel.addStyle("small text", null);
            StyleConstants.setFontSize(smallText, 10);
            
            StyledDocument doc = infoPanel.getStyledDocument();
            
            try{
                
                doc.insertString( doc.getLength(), "Task ", bigBoldText );
                
                doc.insertString( doc.getLength(), task.getID() + "\n\n", bigBoldText );
                
                doc.insertString( doc.getLength(), "Task Name:\n", bigBoldText );
                
                if( task.getName() != null && task.getName().length() > 0 ){
                    
                    doc.insertString( doc.getLength(), task.getName() + "\n\n", smallText );
                    
                } else{
                    
                    doc.insertString( doc.getLength(), "This task has no name :/\n\n", smallText );
                }
                
                doc.insertString( doc.getLength(), "Description:\n", bigBoldText );
                
                if( task.getDescription() != null && task.getDescription().length() > 0 ){
                    
                    doc.insertString( doc.getLength(), task.getDescription(), smallText );
                    
                } else{
                    
                    doc.insertString( doc.getLength(), "No description is entered for this task and testing wrapping text\n\n", smallText );
                }
                
                doc.insertString( doc.getLength(), "From: " + (task.getDueDate() != null ? task.getDueDate() : "null") + "\n\n", bigBoldText );
                doc.insertString( doc.getLength(), "To: " + (task.getEndDate() != null ? task.getEndDate() : "null") + "\n\n", bigBoldText );
                
                infoPanel.setCaretPosition(0);
                
            } catch( BadLocationException badLocationException ){}
              catch( IllegalArgumentException illegalArgumentException ){}
        }
    }
    
    public InvisibleButton getUpArrowComponent(){
        
        return scrollBarSkin != null ? scrollBarSkin.getUpButtonComponent() : null;
    }
    
    public InvisibleButton getDownArrowComponent(){
        
        return scrollBarSkin != null ? scrollBarSkin.getDownButtonComponent() : null;
    }
    
    public JTextPane getDisplayComponent(){
        
        return infoPanel;
    }
    
    public JScrollPane getDisplayScrollComponent(){
        
        return infoPanelScrollPane;
    }
}
