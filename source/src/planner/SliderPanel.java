//@author A0111333B

package planner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
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
    private Integer firstXCoordinate = null;
    
    private int currentXCoordinate;
    
    private boolean isCurrentlyVisible = true;
    
    private Timer timerSlideOut;
    private Timer timerSlideIn;
    
    private static final long NANOSECOND_PER_MILLISECOND = 1000000;
    
    @Override
    public Dimension getPreferredSize(){
        
        return new Dimension(width,height);
    }
    
    public SliderPanel( int widthNotToMoveInto, int parentWidth ) {
        
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
        
        timerSlideOut = new Timer( 2, new ActionListener(){

            private long time = -1;
            
            @Override
            public void actionPerformed(ActionEvent arg0) {

                if( currentXCoordinate == initialXCoordinate ){
                    
                    setToVisible();
                }
                
                int increments = 1;
                
                if( time <= -1 ){
                    
                    time = System.nanoTime();
                    
                } else{
                    
                    long timeDifference = System.nanoTime() - time;
                    
                    int multiplier = (int)(timeDifference/(NANOSECOND_PER_MILLISECOND*2));
                    
                    multiplier = (multiplier > 0 ? multiplier : 1);
                    
                    increments = Math.min(increments*multiplier, currentXCoordinate-widthNotToMoveInto);
                }
                
                currentXCoordinate = currentXCoordinate - increments;
                
                setLocation(currentXCoordinate, initialYCoordinate );
                
                if( timerSlideIn.isRunning() || currentXCoordinate <= widthNotToMoveInto ){
                    
                    timerSlideOut.stop();
                    
                    time = -1;
                }
            }
            
        });
    }
    
    private void setupTimerForSlideIn(){
        
        timerSlideIn = new Timer( 2, new ActionListener(){

            private long time = -1;
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                int increments = 1;
                
                if( time <= -1 ){
                    
                    time = System.nanoTime();
                    
                } else{
                    
                    long timeDifference = System.nanoTime() - time;
                    
                    int multiplier = (int)(timeDifference/(NANOSECOND_PER_MILLISECOND*2));
                    multiplier = (multiplier > 0 ? multiplier : 1);
                    
                    increments = Math.min(increments*multiplier, parentWidth-currentXCoordinate);
                }
                
                currentXCoordinate = currentXCoordinate + increments;
                
                setLocation(currentXCoordinate, initialYCoordinate );
                
                if( currentXCoordinate >= parentWidth ){
                    
                    setToInvisible();
                    
                    timerSlideIn.stop();
                    
                    time = -1;
                    
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
    
    public void insertStringToDisplay( int position, String str, SimpleAttributeSet style ){
        
        if( infoPanel != null && str != null ){
            
            StyledDocument doc = infoPanel.getStyledDocument();
            
            if( doc != null ){
                
                try {
                    
                    doc.insertString(position, str, style);
                    
                } catch (BadLocationException e) {}
            }
        }
    }
    
    public int getDisplayCaretPosition(){
        
        if( infoPanel != null ){
            
            return infoPanel.getCaretPosition();
            
        } else{
            
            return -1;
        }
    }
    
    public void setDisplayCaretPosition( int caretPosition ){
        
        try{
            
            if( infoPanel != null ){
            
                String currentTxt = infoPanel.getText();
                
                if( caretPosition >= 0 && caretPosition <= currentTxt.length() ){
                
                    infoPanel.setCaretPosition(caretPosition);
                }
            }
            
        } catch( IllegalArgumentException illegalArgumentException ){}
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
        infoPanel.setEditorKit(new CustomWrapKit());
        
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
    
    public void slideOut( Task task, boolean canOverrideContents ){
        
        Point pointInParent = getLocation();
        
        if( firstXCoordinate != null ){
            
            initialXCoordinate = firstXCoordinate;
            
        } else{
            
            firstXCoordinate = (pointInParent != null ? pointInParent.x : null);
            initialXCoordinate = firstXCoordinate != null ? firstXCoordinate : 0;
        }
        
        initialYCoordinate = pointInParent != null ? pointInParent.y : 0;
        
        if( initialXCoordinate <= widthNotToMoveInto ){
            
            return;
        }
        
        if( canOverrideContents ){
            
            populateDisplay(task);
        }
        
        currentXCoordinate = initialXCoordinate;
        
        if( timerSlideOut.isRunning() ){
            timerSlideOut.stop();
        }
        
        setLocation( currentXCoordinate, initialYCoordinate );
        
        timerSlideOut.start();
    }
    
    public void slideIn(){
        
        if( currentXCoordinate >= parentWidth ){
            
            return;
        }
        
        setLocation( parentWidth, initialYCoordinate );
        
        if( timerSlideIn.isRunning() ){
            timerSlideIn.stop();
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
            StyleConstants.setFontSize(smallText, 12);
            
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
                    
                    doc.insertString( doc.getLength(), task.getDescription() + "\n\n", smallText );
                    
                } else{
                    
                    doc.insertString( doc.getLength(), "No description is entered for this task\n\n", smallText );
                }
                
                if( task.isFloating() ){
                    
                    doc.insertString( doc.getLength(), "This is a floating task", bigBoldText );
                    
                } else if( task.isDone() ){
                    
                    doc.insertString( doc.getLength(), "Completed on:\n", bigBoldText );
                    doc.insertString( doc.getLength(), task.getDateCompleted().toString(), smallText );
                    
                } else{
                    
                    if( task.getDueDate() != null && task.getEndDate() != null ){
                        
                        doc.insertString( doc.getLength(), "From:\n", bigBoldText );
                        doc.insertString( doc.getLength(), task.getDueDate() + "\n\n", smallText );
                        
                        doc.insertString( doc.getLength(), "To:\n", bigBoldText );
                        doc.insertString( doc.getLength(), task.getEndDate().toString(), smallText );
                        
                    } else if( task.getEndDate() != null ){
                        
                        doc.insertString( doc.getLength(), "By:\n", bigBoldText );
                        doc.insertString( doc.getLength(), task.getEndDate().toString(), smallText );
                        
                    } else if( task.getDueDate() != null ){
                        
                        doc.insertString( doc.getLength(), "By:\n", bigBoldText );
                        doc.insertString( doc.getLength(), task.getDueDate().toString(), smallText );
                        
                    } else{
                        
                        doc.insertString( doc.getLength(), "This is a floating task", bigBoldText );
                    }
                }
                
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
