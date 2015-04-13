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

/**
* The SliderPanel class is a component that will display additional task information. This display panel can slide
* in or out of view.
*
* @author A0111333B
*/
public class SliderPanel extends JComponent{
    
    private JLabel background_;
    private JLabel downArrow_;
    private JLabel upArrow_;
    private JLabel tips_;
    
    private JTextPane infoPanel_;
    
    private JScrollPane infoPanelScrollPane_;
    
    private InvisibleScrollBarUI scrollBarSkin_;
    
    private JScrollBar verticalScrollBar_;
    
    private int initialXCoordinate_ = 0;
    private int initialYCoordinate_ = 0;
    private int parentWidth_;
    private int widthNotToMoveInto_;
    private Integer firstXCoordinate_ = null;
    
    private int currentXCoordinate_;
    
    private boolean isCurrentlyVisible_ = true;
    
    private Timer timerSlideOut_;
    private Timer timerSlideIn_;
    
    private static final long NANOSECOND_PER_MILLISECOND = 1000000;
    private final long DEFAULT_INITIAL_START_TIME = -1;
    
    private final int MILLISECOND_TO_WAIT_BEFORE_NEXT_ANIMATION_FRAME = 2;
    private final int XCOORDINATE_INCREMENT_FOR_ANIMATION = 1;
    private final int MINIMUM_DISTANCE_MULTIPLIER = 0;
    private final int DEFAULT_SLIDERPANEL_WIDTH = 198;
    private final int DEFAULT_SLIDERPANEL_HEIGHT = 520;
    private final int DEFAULT_DISTANCE_MULTIPLIER = 1;
    private final int DEFAULT_UP_ARROW_RELATIVE_XCOORDINATE = 94;
    private final int DEFAULT_UP_ARROW_RELATIVE_YCOORDINATE = 66;
    private final int DEFAULT_UP_ARROW_WIDTH = 20;
    private final int DEFAULT_UP_ARROW_HEIGHT = 8;
    private final int DEFAULT_DOWN_ARROW_RELATIVE_XCOORDINATE = 94;
    private final int DEFAULT_DOWN_ARROW_RELATIVE_YCOORDINATE = 430;
    private final int DEFAULT_DOWN_ARROW_WIDTH = 20;
    private final int DEFAULT_DOWN_ARROW_HEIGHT = 8;
    private final int DEFAULT_TIPLABEL_RELATIVE_XCOORDINATE = 10;
    private final int DEFAULT_TIPLABEL_RELATIVE_YCOORDINATE = 452;
    private final int DEFAULT_TIPLABEL_WIDTH = 184;
    private final int DEFAULT_TIPLABEL_HEIGHT = 67;
    private final int MINIMUM_DOCUMENT_INDEX = 0;
    private final int DEFAULT_INVALID_CARET_POSITION = -1;
    private final int DEFAULT_BACKGROUND_RELATIVE_XCOORDINATE = 0;
    private final int DEFAULT_BACKGROUND_RELATIVE_YCOORDINATE = 0;
    private final int DEFAULT_INFOPANEL_RELATIVE_XCOORDINATE = 10;
    private final int DEFAULT_INFOPANEL_RELATIVE_YCOORDINATE = 78;
    private final int DEFAULT_INFOPANEL_WIDTH = 182;
    private final int DEFAULT_INFOPANEL_HEIGHT = 347;
    private final int DEFAULT_INTITIAL_XCOORDINATE_FOR_SLIDERPANEL = 0;
    private final int DEFAULT_INTITIAL_YCOORDINATE_FOR_SLIDERPANEL = 0;
    private final int DEFAULT_BOLD_TEXT_FONT_SIZE = 15;
    private final int DEFAULT_SMALL_TEXT_FONT_SIZE = 12;
    private final int EMPTY_STRING_LENGTH = 0;
    private final int MINIMUM_PARENT_WIDTH = 0;
    
    private final String EMPTY_STRING = "";
    private final String DEFAULT_BOLD_TEXT_STYLE_NAME = "big bold text";
    private final String DEFAULT_SMALL_TEXT_STYLE_NAME = "small text";
    private final String TASK_HEADER_STRING = "Task ";
    private final String TASK_NAME_HEADER_STRING = "Task Name:\n";
    private final String TASK_INFORMATION_SEPARATOR = "\n\n";
    private final String DEFAULT_TASK_NAME = "This task has no name :/\n\n";
    private final String TASK_DESCRIPTION_HEADER_STRING = "Description:\n";
    private final String DEFAULT_TASK_DESCRIPTION = "No description is entered for this task\n\n";
    private final String FLOATING_TASK_STRING = "This is a floating task";
    private final String COMPLETED_TASK_STRING = "Completed on:\n";
    private final String FROM_DATE_STRING = "From:\n";
    private final String TO_DATE_STRING = "To:\n";
    private final String BY_DATE_STRING = "By:\n";
    private final String UP_ARROW_IMAGE_RESOURCE_LINK = "/planner/WhiteUpArrow.png";
    private final String DOWN_ARROW_IMAGE_RESOURCE_LINK = "/planner/WhiteDownArrow.png";
    private final String UP_TIP_IMAGE_RESOURCE_LINK = "/planner/Up.png";
    private final String DOWN_TIP_IMAGE_RESOURCE_LINK = "/planner/Down.png";
    private final String UPANDDOWN_TIP_IMAGE_RESOURCE_LINK = "/planner/UpAndDown.png";
    private final String BACKGROUND_IMAGE_RESOURCE_LINK = "/planner/SliderPanel.png";
    
    /**
     * Constructs a SliderPanel component that will display additional task information.
     *
     * @param widthNotToMoveInto    The x coordinate in the parent frame that the SliderPanel will have to stop moving upon reaching
     * @param parentWidth           The width of the parent frame
     */
    public SliderPanel( int widthNotToMoveInto, int parentWidth ) {
        prepareDisplayInfoPanel();
        prepareUpArrow();
        prepareDownArrow();
        prepareTips();
        prepareBackground();
        this.parentWidth_ = parentWidth > MINIMUM_PARENT_WIDTH ? parentWidth : MINIMUM_PARENT_WIDTH;
        this.widthNotToMoveInto_ = widthNotToMoveInto > MINIMUM_PARENT_WIDTH ? widthNotToMoveInto : MINIMUM_PARENT_WIDTH;
        setupTimerForSlideOut();
        setupTimerForSlideIn();
        setToInvisible();
    }
    
    /**
     * Returns A Dimension container containing the preferred width and height of this SliderPanel component
     *
     * @return A Dimension container containing the preferred width and height of this SliderPanel component
     */
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DEFAULT_SLIDERPANEL_WIDTH,DEFAULT_SLIDERPANEL_HEIGHT);
    }
    
    /**
     * Sets up the timer for the slide out animation for this SliderPanel component 
     */
    private void setupTimerForSlideOut(){
        timerSlideOut_ = new Timer( MILLISECOND_TO_WAIT_BEFORE_NEXT_ANIMATION_FRAME, new ActionListener(){
            private long time = DEFAULT_INITIAL_START_TIME;
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if( currentXCoordinate_ == initialXCoordinate_ ){
                    setToVisible();
                }
                int increments = XCOORDINATE_INCREMENT_FOR_ANIMATION;
                
                if( time <= DEFAULT_INITIAL_START_TIME ){
                    time = System.nanoTime();
                } else{
                    long timeDifference = System.nanoTime() - time;
                    int multiplier = (int)(timeDifference/(NANOSECOND_PER_MILLISECOND*MILLISECOND_TO_WAIT_BEFORE_NEXT_ANIMATION_FRAME));
                    multiplier = (multiplier > MINIMUM_DISTANCE_MULTIPLIER ? multiplier : DEFAULT_DISTANCE_MULTIPLIER);
                    increments = Math.min(increments*multiplier, currentXCoordinate_-widthNotToMoveInto_);
                }
                currentXCoordinate_ = currentXCoordinate_ - increments;
                setLocation(currentXCoordinate_, initialYCoordinate_ );
                if( timerSlideIn_.isRunning() || currentXCoordinate_ <= widthNotToMoveInto_ ){
                    timerSlideOut_.stop();
                    time = DEFAULT_INITIAL_START_TIME;
                }
            }
        });
    }

    /**
     * Sets up the timer for the slide in animation for this SliderPanel component 
     */
    private void setupTimerForSlideIn(){
        timerSlideIn_ = new Timer( MILLISECOND_TO_WAIT_BEFORE_NEXT_ANIMATION_FRAME, new ActionListener(){
            private long time = DEFAULT_INITIAL_START_TIME;
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                
                int increments = XCOORDINATE_INCREMENT_FOR_ANIMATION;
                if( time <= DEFAULT_INITIAL_START_TIME ){
                    time = System.nanoTime();
                } else{
                    long timeDifference = System.nanoTime() - time;
                    int multiplier = (int)(timeDifference/(NANOSECOND_PER_MILLISECOND*MILLISECOND_TO_WAIT_BEFORE_NEXT_ANIMATION_FRAME));
                    multiplier = (multiplier > MINIMUM_DISTANCE_MULTIPLIER ? multiplier : DEFAULT_DISTANCE_MULTIPLIER);
                    increments = Math.min(increments*multiplier, parentWidth_-currentXCoordinate_);
                }
                currentXCoordinate_ = currentXCoordinate_ + increments;
                setLocation(currentXCoordinate_, initialYCoordinate_ );
                if( currentXCoordinate_ >= parentWidth_ ){
                    setToInvisible();
                    timerSlideIn_.stop();
                    time = DEFAULT_INITIAL_START_TIME;
                    System.out.println( "Slide in stopped" );
                }
            }
        });
    }
    
    /**
     * Initializes and set attributes for the up arrow component.
     */
    private void prepareUpArrow(){
        upArrow_ = new JLabel();
        upArrow_.setIcon(new ImageIcon(SliderPanel.class.getResource(UP_ARROW_IMAGE_RESOURCE_LINK)));
        upArrow_.setBounds(DEFAULT_UP_ARROW_RELATIVE_XCOORDINATE, DEFAULT_UP_ARROW_RELATIVE_YCOORDINATE, 
                          DEFAULT_UP_ARROW_WIDTH, DEFAULT_UP_ARROW_HEIGHT);
        add(upArrow_);
        hideUpArrow();
    }
    
    /**
     * Displays the up arrow component.
     */
    private void showUpArrow(){
        upArrow_.setVisible(true);
    }
    
    /**
     * Hides the up arrow component from view.
     */
    private void hideUpArrow(){
        upArrow_.setVisible(false);
    }
    
    /**
     * Initializes and set attributes for the down arrow component.
     */
    private void prepareDownArrow(){
        downArrow_ = new JLabel();
        downArrow_.setIcon(new ImageIcon(SliderPanel.class.getResource(DOWN_ARROW_IMAGE_RESOURCE_LINK)));
        downArrow_.setBounds(DEFAULT_DOWN_ARROW_RELATIVE_XCOORDINATE, DEFAULT_DOWN_ARROW_RELATIVE_YCOORDINATE, 
                            DEFAULT_DOWN_ARROW_WIDTH, DEFAULT_DOWN_ARROW_HEIGHT);
        add(downArrow_);
        hideDownArrow();
    }
    
    /**
     * Displays the down arrow component.
     */
    private void showDownArrow(){
        downArrow_.setVisible(true);
    }
    
    /**
     * Hides the down arrow component from view.
     */
    private void hideDownArrow(){
        downArrow_.setVisible(false);
    }

    /**
     * Initializes and set attributes for the tips label component.
     */
    private void prepareTips(){
        tips_ = new JLabel();
        tips_.setBounds(DEFAULT_TIPLABEL_RELATIVE_XCOORDINATE, DEFAULT_TIPLABEL_RELATIVE_YCOORDINATE,
                       DEFAULT_TIPLABEL_WIDTH, DEFAULT_TIPLABEL_HEIGHT);
        add(tips_);
        hideTip();
    }
    
    /**
     * Hides the tips label component from view.
     */
    private void hideTip(){
        tips_.setVisible(false);
    }
    
    /**
     * Inserts the given string into the document displayed by the infopanel at the position given and 
     * rendered using the given style. 
     *
     * @param position  The position in the document that the given string is going to be inserted at. If the position
     *                  given is invalid, the string will not be inserted into the document.
     * @param str       The string to be inserted into the document
     * @param style     The style that the inserted string is going to be rendered in
     */
    public void insertStringToDisplay( int position, String str, SimpleAttributeSet style ){
        if( infoPanel_ != null && str != null ){
            StyledDocument doc = infoPanel_.getStyledDocument();
            if( doc != null ){
                try {
                    doc.insertString(position, str, style);
                } catch (BadLocationException e) {}
            }
        }
    }
    
    /**
     * Returns the current caret position of the document displayed by the infopanel
     *
     * @return The current caret position of the document displayed by the infopanel
     */
    public int getDisplayCaretPosition(){
        if( infoPanel_ != null ){
            return infoPanel_.getCaretPosition();
        } else{
            return DEFAULT_INVALID_CARET_POSITION;
        }
    }
    
    /**
     * Sets the current caret position of the document displayed by the infopanel. If the caret position
     * is invalid, the current caret position will remain the same as before.
     *
     * @param caretPosition The caret position to be set
     */
    public void setDisplayCaretPosition( int caretPosition ){
        try{
            if( infoPanel_ != null ){
                String currentTxt = infoPanel_.getText();
                if( caretPosition >= MINIMUM_DOCUMENT_INDEX && caretPosition <= currentTxt.length() ){
                    infoPanel_.setCaretPosition(caretPosition);
                }
            }
        } catch( IllegalArgumentException illegalArgumentException ){}
    }
    
    /**
     * Display the tip message corresponding to the ENUM tipType value
     *
     * @param tipType   The type of tip message to show to the user
     */
    private void showTip( TipType tipType ){
        tips_.setVisible(true);
        switch(tipType){
            case UP_TIP:
                tips_.setIcon(new ImageIcon(SliderPanel.class.getResource(UP_TIP_IMAGE_RESOURCE_LINK)));
                break;
                
            case DOWN_TIP:
                tips_.setIcon(new ImageIcon(SliderPanel.class.getResource(DOWN_TIP_IMAGE_RESOURCE_LINK)));
                break;
                
            case UPDOWN_TIP:
                tips_.setIcon(new ImageIcon(SliderPanel.class.getResource(UPANDDOWN_TIP_IMAGE_RESOURCE_LINK)));
                break;
                
            default:
                hideTip();
                break;
        }
    }

    /**
     * Initializes and set attributes for the background component.
     */
    private void prepareBackground(){
        background_ = new JLabel();
        background_.setIcon(new ImageIcon(SliderPanel.class.getResource(BACKGROUND_IMAGE_RESOURCE_LINK)));
        background_.setBounds(DEFAULT_BACKGROUND_RELATIVE_XCOORDINATE, DEFAULT_BACKGROUND_RELATIVE_YCOORDINATE, DEFAULT_SLIDERPANEL_WIDTH, DEFAULT_SLIDERPANEL_HEIGHT);
        add(background_);
    }
    
    /**
     * Initializes and set attributes for the display component for this sliderpanel
     */
    private void prepareDisplayInfoPanel(){
        infoPanelScrollPane_ = new JScrollPane();
        infoPanelScrollPane_.setBounds(DEFAULT_INFOPANEL_RELATIVE_XCOORDINATE, DEFAULT_INFOPANEL_RELATIVE_YCOORDINATE, 
                                      DEFAULT_INFOPANEL_WIDTH, DEFAULT_INFOPANEL_HEIGHT);
        add(infoPanelScrollPane_);
        infoPanel_ = new JTextPane();
        infoPanel_.setBorder(null);
        infoPanel_.setOpaque(false);
        infoPanel_.setEditable(false);
        infoPanel_.setHighlighter(null);
        infoPanel_.setFocusable(false);
        infoPanel_.setEditorKit(new CustomWrapKit());
        infoPanelScrollPane_.setViewportView(infoPanel_);
        infoPanelScrollPane_.setBorder(null);
        infoPanelScrollPane_.setOpaque(false);
        infoPanelScrollPane_.getViewport().setOpaque(false);
        infoPanelScrollPane_.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        infoPanelScrollPane_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoPanelScrollPane_.setFocusable(false);
        attachScrollAdjustmentListener(infoPanelScrollPane_);
        verticalScrollBar_ = infoPanelScrollPane_.getVerticalScrollBar();
        verticalScrollBar_.setOpaque(false);
        scrollBarSkin_ = new InvisibleScrollBarUI();
        verticalScrollBar_.setUI( scrollBarSkin_ );
    }
    
    /**
     * Set up a listener for the scroll bar attached to the display component for this SliderPanel so that
     * appropriate tips can be shown depending on the position of the scroll bar thumb.
     * 
     * @param scrollPane The ScrollPane which the display component for this SliderPanel is embedded within
     */
    private void attachScrollAdjustmentListener( JScrollPane scrollPane ){
        if( scrollPane != null ){
            verticalScrollBar_ = scrollPane.getVerticalScrollBar();
            if( verticalScrollBar_ != null ){
                verticalScrollBar_.addAdjustmentListener(new AdjustmentListener(){
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent event) {
                        if( !verticalScrollBar_.isVisible() ){
                            hideDownArrow();
                            hideUpArrow();
                            hideTip();
                        } else{
                            int scrollBarExtent = verticalScrollBar_.getModel().getExtent();
                            if( verticalScrollBar_.getValue() <= verticalScrollBar_.getMinimum() ){
                                hideUpArrow();
                                showDownArrow();
                                showTip( TipType.DOWN_TIP );
                            } else if( verticalScrollBar_.getValue() + scrollBarExtent >= verticalScrollBar_.getMaximum() ){
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
    
    /**
     * Make this SliderPanel invisible.
     */
    private void setToInvisible(){
        setVisible(false);
        isCurrentlyVisible_ = false;
    }
    
    /**
     * Make this SliderPanel visible.
     */
    private void setToVisible(){
        setVisible(true);
        isCurrentlyVisible_ = true;
    }
    
    /**
     * This method will start the slide out (moving into view) animation for this SliderPanel component. It will display the information
     * found in task by overriding previous display contents if canOverrideContents is true or append this information to 
     * the previous display contents if canOverrideContents is false.
     * 
     * @param task                  The task in which its information will be displayed by the infoPanel of this SliderPanel component.
     *                              It the task is null, all previous display contents will be cleared if canOverrideContents is true;
     *                              otherwise all previous display contents will remain if canOverrideContents is false
     * @param canOverrideContents   Flag to indicate if all previous display contents can be overwritten.
     */
    public void slideOut( Task task, boolean canOverrideContents ){
        Point pointInParent = getLocation();
        if( firstXCoordinate_ != null ){
            initialXCoordinate_ = firstXCoordinate_;
        } else{
            firstXCoordinate_ = (pointInParent != null ? pointInParent.x : null);
            initialXCoordinate_ = firstXCoordinate_ != null ? firstXCoordinate_ : DEFAULT_INTITIAL_XCOORDINATE_FOR_SLIDERPANEL;
        }
        initialYCoordinate_ = pointInParent != null ? pointInParent.y : DEFAULT_INTITIAL_YCOORDINATE_FOR_SLIDERPANEL;
        if( initialXCoordinate_ <= widthNotToMoveInto_ ){
            return;
        }
        if( canOverrideContents ){
            populateDisplay(task);
        }
        currentXCoordinate_ = initialXCoordinate_;
        if( timerSlideOut_.isRunning() ){
            timerSlideOut_.stop();
        }
        setLocation( currentXCoordinate_, initialYCoordinate_ );
        timerSlideOut_.start();
    }
    
    /**
     * This method will start the slide in (moving out of view) animation for this SliderPanel component.
     */
    public void slideIn(){
        if( currentXCoordinate_ >= parentWidth_ ){
            return;
        }
        setLocation( parentWidth_, initialYCoordinate_ );
        if( timerSlideIn_.isRunning() ){
            timerSlideIn_.stop();
        }
        timerSlideIn_.start();
    }
    
    /**
     * Returns true if this SliderPanel is currently in view; False otherwise.
     * 
     * @return TRUE if this SliderPanel is currently in view; False otherwise.
     */
    public boolean isVisible(){
        return isCurrentlyVisible_;
    }
    
    /**
     * Set the policy of the vertical scrollbar of the infoPanel to never show itself regardless of how tall the contents
     * within the infoPanel is
     */
    public void hideScrollBar(){
        infoPanelScrollPane_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
    
    /**
     * Set the policy of the vertical scrollbar of the infoPanel to show itself if the contents displayed within the infoPanel
     * is too tall to fit into the infoPanel
     */
    public void showScrollBar(){
        infoPanelScrollPane_.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    /**
     * This method will display task name and description
     * 
     * @param doc           The document where it will insert the task name and description to
     * @param task          The task object where it will retrieve information to display
     * @param bigBoldText   The bold and larger sized text font style
     * @param smallText     The smaller sized text font style
     */
    private void displayBasicTaskInfomation( StyledDocument doc, Task task,
                                             Style bigBoldText, Style smallText) throws BadLocationException{
        if( doc != null && task != null ){
            doc.insertString( doc.getLength(), TASK_HEADER_STRING, bigBoldText );
            doc.insertString( doc.getLength(), task.getID() + TASK_INFORMATION_SEPARATOR, bigBoldText );
            doc.insertString( doc.getLength(), TASK_NAME_HEADER_STRING, bigBoldText );
            if( task.getName() != null && task.getName().length() > EMPTY_STRING_LENGTH ){
                doc.insertString( doc.getLength(), task.getName() + TASK_INFORMATION_SEPARATOR, smallText );
            } else{
                doc.insertString( doc.getLength(), DEFAULT_TASK_NAME, smallText );
            }
            doc.insertString( doc.getLength(), TASK_DESCRIPTION_HEADER_STRING, bigBoldText );
            if( task.getDescription() != null && task.getDescription().length() > EMPTY_STRING_LENGTH ){
                doc.insertString( doc.getLength(), task.getDescription() + TASK_INFORMATION_SEPARATOR, smallText );
            } else{
                doc.insertString( doc.getLength(), DEFAULT_TASK_DESCRIPTION, smallText );
            }
        }
    }
    
    /**
     * This method will display timed task dates
     * 
     * @param doc           The document where it will insert the timed task dates to
     * @param task          The task object where it will retrieve information to display
     * @param bigBoldText   The bold and larger sized text font style
     * @param smallText     The smaller sized text font style
     */
    private void displayTimedTask( StyledDocument doc, Task task,
                                   Style bigBoldText, Style smallText ) throws BadLocationException {
        if( doc != null && task != null ){
            if( task.getDueDate() != null && task.getEndDate() != null ){
                doc.insertString( doc.getLength(), FROM_DATE_STRING, bigBoldText );
                doc.insertString( doc.getLength(), task.getDueDate() + TASK_INFORMATION_SEPARATOR, smallText );
                doc.insertString( doc.getLength(), TO_DATE_STRING, bigBoldText );
                doc.insertString( doc.getLength(), task.getEndDate().toString(), smallText );
            } else if( task.getEndDate() != null ){
                doc.insertString( doc.getLength(), BY_DATE_STRING, bigBoldText );
                doc.insertString( doc.getLength(), task.getEndDate().toString(), smallText );
            } else if( task.getDueDate() != null ){
                doc.insertString( doc.getLength(), BY_DATE_STRING, bigBoldText );
                doc.insertString( doc.getLength(), task.getDueDate().toString(), smallText );
            } else{
                doc.insertString( doc.getLength(), FLOATING_TASK_STRING, bigBoldText );
            }
        }
    }
    
    /**
     * This method will display task information
     * 
     * @param doc           The document where it will insert the task information to
     * @param task          The task object where it will retrieve information to display
     * @param bigBoldText   The bold and larger sized text font style
     * @param smallText     The smaller sized text font style
     */
    public void populateDisplay( Task task ){
        
        infoPanel_.setText(EMPTY_STRING);
        if( task != null ){
            infoPanel_.setForeground(Color.WHITE);
            Style bigBoldText = infoPanel_.addStyle(DEFAULT_BOLD_TEXT_STYLE_NAME, null);
            StyleConstants.setFontSize(bigBoldText, DEFAULT_BOLD_TEXT_FONT_SIZE);
            Style smallText = infoPanel_.addStyle(DEFAULT_SMALL_TEXT_STYLE_NAME, null);
            StyleConstants.setFontSize(smallText, DEFAULT_SMALL_TEXT_FONT_SIZE);
            StyledDocument doc = infoPanel_.getStyledDocument();
            try{
                displayBasicTaskInfomation( doc, task, bigBoldText, smallText );
                if( task.isFloating() ){
                    doc.insertString( doc.getLength(), FLOATING_TASK_STRING, bigBoldText );
                } else if( task.isDone() ){
                    doc.insertString( doc.getLength(), COMPLETED_TASK_STRING, bigBoldText );
                    doc.insertString( doc.getLength(), task.getDateCompleted().toString(), smallText );
                } else{
                    displayTimedTask( doc, task, bigBoldText, smallText );
                }
                infoPanel_.setCaretPosition(MINIMUM_DOCUMENT_INDEX);
            } catch( BadLocationException badLocationException ){}
              catch( IllegalArgumentException illegalArgumentException ){}
        }
    }
    
    /**
     * Returns the up arrow component used by the scroll bar attached to the infoPanel which displays information
     * 
     * @return The up arrow component used by the scroll bar attached to the infoPanel which displays information
     */
    public InvisibleButton getUpArrowComponent(){
        return scrollBarSkin_ != null ? scrollBarSkin_.getUpButtonComponent() : null;
    }
    
    /**
     * Returns the down arrow component used by the scroll bar attached to the infoPanel which displays information
     * 
     * @return The down arrow component used by the scroll bar attached to the infoPanel which displays information
     */
    public InvisibleButton getDownArrowComponent(){
        return scrollBarSkin_ != null ? scrollBarSkin_.getDownButtonComponent() : null;
    }
    
    /**
    * Returns the display component which this sliderPanel use to display information
    * 
    * @return The display component which this sliderPanel use to display information
    */
    public JTextPane getDisplayComponent(){
        return infoPanel_;
    }
    
    /**
     * Returns the scrollPane which the display component of this sliderPanel is embedded within
     * 
     * @return The scrollPane which the display component of this sliderPanel is embedded within
     */
    public JScrollPane getDisplayScrollComponent(){
        return infoPanelScrollPane_;
    }
}
