package planner;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import planner.Constants.CommandType;
import planner.Constants.DisplayStateFlag;

// This class handles all GUI logic and processing
public class UserInterface extends JFrame {

    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS START HERE
    ///////////////////////////////////////////////////////////////////// 
    public void processCommand( String input ){
        
        planner.Constants.CommandType commandType = Engine.process(input);
        
        switch( commandType ){
        
            case ADD:
                
                handleAddOperation();
                
                break;
                
            case ADD_CLASH:
                
                handleAddClashOperation();
                
                break;
                
            case UPDATE:
                
                handleUpdateOperation();
                
                break;
                
            case DELETE:
                
                handleDeleteOperation();
                
                break;
                
            case DONE:
                    
                handleDoneOperation();
                
                break;
                
            case INVALID:
                
                handleInvalidOperation();
                
                break;
                
            case SEARCH:
                
                handleSearch( input );
                
                break;
            
            case SETNOTDONE:
                
                handleSetNotDone();
                
                break;
                
            case UNDO:
                
                handleUndo();
                
                break;
                
            case CONVERT_FLOATING:
                
                handleConvertToFloatingTaskOperation();
                
                break;
                
            case CONVERT_DEADLINE:
                
                handleConvertToDeadlineTaskOperation();
                
                break;
                
            case CONVERT_TIMED:
                
                handleConvertToTimedTaskOperation();
                
                break;
                
            case SAVEWHERE:
                
                handleSaveWhere(input);
                
                break;
                
            case SAVEHERE:
                
                handleSaveHere();
                
                break;
            
            case HELP:
                
                showTutorialScreen( -1, input );
                
                break;
                
            case HELP_ADD:
            
                showTutorialScreen( Constants.ADD_TUTORIAL, input );
                
                break;
                
            case HELP_DELETE:
                
                showTutorialScreen( Constants.DELETE_TUTORIAL, input );
                
                break;
                
            case HELP_UPDATE:
                
                showTutorialScreen( Constants.UPDATE_TUTORIAL, input );
                
                break;
                
            case HELP_DONE:
                
                showTutorialScreen( Constants.DONE_TUTORIAL, input );
                
                break;
                
            case HELP_SEARCH:
                
                showTutorialScreen( Constants.SEARCH_TUTORIAL, input );
                
                break;
                
            case HELP_UNDO:
                
                showTutorialScreen( Constants.UNDO_TUTORIAL, input );
                
                break;
             
            case EXIT:
                
                handleExit();
                
                break;
            
            default:
                
                handleUnexpectedOperation();
            
                break;
        }
    }
    
    private void handleExit(){
        
        Engine.exit();
        System.exit(0);
    }
    
    private void handleSaveWhere(String userInput){
        
        displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.SETTINGS, 
                "YOPO's Settings", 
                userInput, null ) );
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, null );
        
        String fileSavePath = Engine.getStoragePath();
        
        displayPane.showMessageOnDisplay(" YOPO's Current File Storage Location:\n\n " + fileSavePath);
        
        commandPanel.setText( "Enter commands here", true );
    }
    
    private void handleSaveHere(){
        
        displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.SETTINGS, 
                "YOPO's Settings", 
                "savewhere", null ) );
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, null );
        
        String fileSavePath = Engine.getStoragePath();
        
        displayPane.showMessageOnDisplay(" YOPO's Current File Storage Location:\n\n " + fileSavePath);
        
        commandPanel.setText( "YOPO's file storage location changed successfully", true );
    }
    
    private void handleUndo(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
            
            displayStateStack.push(upcomingDisplayState);
        }
        
        updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, null );
        
        commandPanel.setText( "Previous keyboard command undone successfully", true );
    }
    
    private void handleSetNotDone(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Fail to mark task as not done", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Fail to mark task as not done", true );
                
                return;
            }
        }

        commandPanel.setText( "Task #" + modifiedTaskID + " marked not done successfully", true );
    }
    
    private void handleSearch( String userInput ){
        
        Set<Map.Entry<Date, DisplayTaskList>> tempList = Engine.getSearchResult();
        
        DisplayTaskList tempTaskList = convertToDisplayTaskList(tempList);
        Task tempTask;
        
        if( tempTaskList != null ){
            
            currentList = tempTaskList;
            currentDisplayListForDate = tempList;
            currentDisplayListForPriority = null;
            
            displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.WORD_SEARCH, userInput, userInput, null ));
            
            tempTask = displayPane.getCurrentSelectedTask() != null ? displayPane.getCurrentSelectedTask().getParent() : null;
            
            updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, userInput, tempTask);
           
            if( displayPane.hasTasksDisplayed() ){
                
                commandPanel.setText( "Search success", true );
                
                return;
            }
        }
        
        displayPane.clearDisplay();
        
        commandPanel.setText( "We cannot find any task containing the search phrase :/", true );
    }
    
    private void updateGUIView( Set<Map.Entry<Date, DisplayTaskList>> dateDisplayList, 
                                Set<Map.Entry<Integer, DisplayTaskList>> priorityDisplayList,
                                String sectionTitleString, Task task){
        
        displayPane.clearDisplay();
        
        if( dateDisplayList != null ){
            
            displayPane.displayByDate(dateDisplayList);
            
        } else if( priorityDisplayList != null ){
            
            displayPane.displayByPriority(priorityDisplayList);
        } 
        
        updateGUIView( sectionTitleString, task );
    }
    
    private void updateGUIView( String sectionTitleString, Task task ){
        
        displayPane.selectGivenTask(task);
        
        currentNavigationBars = generateContentForNavigationBars( displayStateStack.peek() );
        addNavigationBarsToPanel(currentNavigationBars);
        
        if( task != null ){
            
            slidePanel.populateDisplay(task);
            
        } else{
            
            if( displayPane.getCurrentSelectedTask() != null && displayPane.getCurrentSelectedTask().getParent() != null ){
                
                slidePanel.populateDisplay(displayPane.getCurrentSelectedTask().getParent());
            }
        }
        
        if(sectionTitleString != null){
            
            sectionTitle.setText(sectionTitleString);
        }
        
        if( !displayPane.hasTasksDisplayed() ){
            
            slidePanel.slideIn();
        }
    }
    
    private <T>DisplayTaskList convertToDisplayTaskList( Set<Map.Entry<T, DisplayTaskList>> displayList ){
        
        DisplayTaskList newDisplayTaskList = new DisplayTaskList();
        
        if( displayList != null ){
            
            DisplayTaskList tempDisplayTaskList;
            
            for( Map.Entry<T, DisplayTaskList> entry : displayList ){
                
                tempDisplayTaskList = entry.getValue();
                
                if( tempDisplayTaskList != null ){
                    
                    for( DisplayTask tempDisplayTask : tempDisplayTaskList ){
                        
                        if( tempDisplayTask != null ){
                            
                            newDisplayTaskList.add(tempDisplayTask);
                        }
                    }
                }
            }
        }
        
        return newDisplayTaskList;
    }
    
    private <T>DisplayTask getDisplayTaskByParentID( Set<Map.Entry<T, DisplayTaskList>> displayList, int parentID ){
        
        if( displayList != null ){
            
            DisplayTaskList tempDisplayTaskList;
            
            for( Map.Entry<T, DisplayTaskList> entry : displayList ){
                
                tempDisplayTaskList = entry.getValue();
                
                if( tempDisplayTaskList != null ){
                    
                    for( DisplayTask tempDisplayTask : tempDisplayTaskList ){
                        
                        if( tempDisplayTask != null && tempDisplayTask.getParent() != null &&
                            tempDisplayTask.getParent().getID() == parentID){
                            
                            return tempDisplayTask;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    private DisplayState updateCurrentList( DisplayState currentDisplayState ){
        
        if( currentDisplayState != null ){
            
            DisplayStateFlag currentDisplayStateFlag  = currentDisplayState.getdisplayStateFlag();
            
            String searchString;
            
            switch(currentDisplayStateFlag){
            
                case TENTATIVE:
                    
                    currentDisplayListForPriority = Engine.getFloatingTasks();
                    currentDisplayListForDate = null;
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForPriority );
                    
                    return currentDisplayState;
                    
                case TODAY:
                    
                    currentDisplayListForPriority = Engine.getTodayTasks();
                    currentDisplayListForDate = null;
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForPriority );
                    
                    return currentDisplayState;
                    
                case OVERDUE:
                    
                    currentDisplayListForPriority = null;
                    currentDisplayListForDate = Engine.getOverdueTasks();
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForDate );
                    
                    return currentDisplayState;
                    
                case UPCOMING:
                    
                    currentDisplayListForPriority = null;
                    currentDisplayListForDate = Engine.getUpcomingTasks();
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForDate );
                    
                    return currentDisplayState;
                    
                case NOTDONE:
                    
                    currentDisplayListForPriority = null;
                    currentDisplayListForDate = Engine.getUndoneTasks();
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForDate );
                    
                    return currentDisplayState;
                
                case DONE:
                    
                    currentDisplayListForPriority = null;
                    currentDisplayListForDate = Engine.getDoneTasks();
                    
                    currentList = convertToDisplayTaskList( currentDisplayListForDate );
                    
                    return currentDisplayState;
                    
                case WORD_SEARCH:
                    
                    searchString = currentDisplayState.getCommand();
                    
                    if ( Engine.process(searchString) == CommandType.SEARCH ){
                        
                        currentDisplayListForPriority = null;
                        currentDisplayListForDate = Engine.getSearchResult();
                        
                        currentList = convertToDisplayTaskList(currentDisplayListForDate);

                        return currentDisplayState;
                    } 

                    break;
                    
                case HELP:
                case HELP_ADD:
                case HELP_UPDATE:
                case HELP_DELETE:
                case HELP_DONE:
                case HELP_UNDO:
                case HELP_SEARCH:
                case HELP_PRIORITY_SEARCH:
                case SETTINGS:
                    
                    currentDisplayListForPriority = null;
                    currentDisplayListForDate = null;
                    
                    currentList = null;
                    
                    return currentDisplayState;
                    
                default:
                    
                    break;
            }
        }
            
        currentDisplayListForPriority = null;
        currentDisplayListForDate = Engine.getAllTasks();
        
        currentList = convertToDisplayTaskList(currentDisplayListForDate);
        
        return new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, 
                                 new KeyEvent( displayPane, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                                 0, KeyEvent.VK_F5, '\0', KeyEvent.KEY_LOCATION_STANDARD) ); 
    }
    
    private void handleAddOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to add task", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Failed to add task", true );
                
                return;
            }
        }

        commandPanel.setText( "Task #"+ modifiedTaskID + " added successfully", true );
    }
    
    private void handlePreviousViewOperation( DisplayStateStack displayStateStack ){
        
        if( displayStateStack != null && displayStateStack.size() > 1 ){
            
            DisplayState currentDisplayState = displayStateStack.pop();
            DisplayState previousDisplayState = displayStateStack.pop();
            displayStateStack.push(currentDisplayState);
            
            if( previousDisplayState != null ){
                
                boolean isValidDisplayState = false;
                
                KeyEvent keyEvent = previousDisplayState.getKeyEvent();
                String userCommand = previousDisplayState.getCommand();
                
                if( keyEvent != null ){
                    
                    handleKeyEvent(keyEvent);
                    
                    isValidDisplayState = true;
                    
                } else if( userCommand != null ){
                    
                    updateCurrentList(previousDisplayState);
                    
                    switch( Engine.process(userCommand) ){
                    
                        case SEARCH:
                            
                            handleSearch(userCommand);
                            
                            break;
                            
                        case SAVEWHERE:
                            
                            handleSaveWhere(userCommand);
                            
                            break;
                            
                        case SAVEHERE:
                            
                            handleSaveWhere(userCommand);
                            
                            break;
                            
                        case HELP:
                            
                            showTutorialScreen(-1, userCommand );
                            
                            break;
                            
                        case HELP_ADD:
                            
                            showTutorialScreen(Constants.ADD_TUTORIAL, userCommand );
                            
                            break;
                            
                        case HELP_UPDATE:
                            
                            showTutorialScreen(Constants.UPDATE_TUTORIAL, userCommand );
                            
                            break;
                            
                        case HELP_DELETE:
                            
                            showTutorialScreen(Constants.DELETE_TUTORIAL, userCommand );
                            
                            break;
                            
                        case HELP_DONE:
                            
                            showTutorialScreen(Constants.DONE_TUTORIAL, userCommand );
                            
                            break;
                            
                        case HELP_UNDO:
                            
                            showTutorialScreen(Constants.UNDO_TUTORIAL, userCommand );
                            
                            break;
                            
                        case HELP_SEARCH:
                            
                            showTutorialScreen(Constants.SEARCH_TUTORIAL, userCommand );
                            
                            break;
                            
                        default:
                            
                            break;
                    }
                    
                    isValidDisplayState = true;
                }
                
                if( isValidDisplayState ){
                    
                    DisplayState newCurrentDisplayState = displayStateStack.pop();
                    
                    // Removed old current state
                    displayStateStack.pop();
                    
                    if( newCurrentDisplayState != null ){
                        
                        displayStateStack.push(newCurrentDisplayState);
                    }
                    
                    currentNavigationBars = generateContentForNavigationBars( displayStateStack.peek() );
                    addNavigationBarsToPanel(currentNavigationBars);
                }
            }
        }
    }
    
    private void handleAddClashOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to add task", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Failed to add task", true );
                
                return;
            }
        }

        int conflictTaskID = Engine.getClashingTask();
        
        commandPanel.setText( "Task #" + modifiedTaskID + " conflicts with task #" + conflictTaskID, true );
    }
    
    private void handleConvertToTimedTaskOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to convert task into a deadline task", true );
                    
                    return;
                }
            }
        }
        
        commandPanel.setText( "Task #" + modifiedTaskID + " converted into a deadline task successfully", true );
    }
    
    private void handleConvertToDeadlineTaskOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to convert task into a deadline task", true );
                    
                    return;
                }
            }
        }
        
        commandPanel.setText( "Task #" + modifiedTaskID + " converted into a deadline task successfully", true );
    }
    
    private void handleConvertToFloatingTaskOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to convert task into a floating task", true );
                    
                    return;
                }
            }
        }
        
        commandPanel.setText( "Task #" + modifiedTaskID + " converted into a floating task successfully", true );
    }
    
    private void handleUpdateOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Failed to update task", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Failed to update task", true );
                
                return;
            }
        }

        commandPanel.setText( "Task #" + modifiedTaskID + " updated successfully", true );
    }
    
    private void handleDoneOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        int modifiedTaskID = Engine.lastModifiedTask();
        DisplayTask modifiedTask = (currentList != null ? currentList.getTaskByParentID(modifiedTaskID) : null);
        
        if( modifiedTask != null ){
            
            if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
                
                displayStateStack.push(upcomingDisplayState);
            }
            
            updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, modifiedTask.getParent() );
            
        } else{
            
            if( currentDisplayState.getdisplayStateFlag() != DisplayStateFlag.ALL ){
                
                Set<Map.Entry<Date, DisplayTaskList>> tempTaskList = Engine.getAllTasks();
                
                modifiedTask = getDisplayTaskByParentID(tempTaskList, modifiedTaskID);
                
                if( modifiedTask != null ){
                    
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(upcomingDisplayState);
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", modifiedTask.getParent() );
                    
                } else{
                    
                    commandPanel.setText( "Fail to mark task as done", true );
                    
                    return;
                }
                
            } else{
                
                commandPanel.setText( "Fail to mark task as done", true );
                
                return;
            }
        }

        commandPanel.setText( "Task #" + modifiedTaskID + " marked done successfully", true );
    }
    
    private void handleDeleteOperation(){
        
        DisplayState currentDisplayState =  displayStateStack.peek();
        DisplayState upcomingDisplayState = updateCurrentList( currentDisplayState );
        
        String sectionTitleString = upcomingDisplayState.getTitle();
        
        if( currentDisplayState.getdisplayStateFlag() != upcomingDisplayState.getdisplayStateFlag() ){
            
            displayStateStack.push(upcomingDisplayState);
        }
        
        updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, sectionTitleString, null );
        
        commandPanel.setText( "Task deleted successfully", true );
    }
    
    private void handleInvalidOperation(){
        
        commandPanel.setText( "Invalid Command", true );
    }
    
    public void handleUnexpectedOperation(){
        
        commandPanel.setText( "Feature not supported in V0.4", true );
    }
    ///////////////////////////////////////////////////////////////////// 
    //  PROCESS COMMANDS FUNCTIONS END HERE
    /////////////////////////////////////////////////////////////////////
    
    private JPanel contentPane;
    private JPanel slidePanelFrame;
    
    private SliderPanel slidePanel;
    
    //private JTextField command;
    private CommandTextbox commandPanel;
    private JTextPane commandInputField;
    
    private DisplayPane displayPane;
    
    private JLabel closeButton;
    private JLabel minimiseButton;
    private JLabel dragPanel;
    private JLabel sectionTitle;
    private JLabel sectionTitleLine;
    private JLabel background;
    
    private InvisibleButton upArrowButton;
    private InvisibleButton downArrowButton;
    
    private JTextPane taskDisplayPanel;
    private JTextPane navigationPanel;
    
    // Used for drag logic
    private int mouseXCoordinate;
    private int mouseYCoordinate;
    
    private DisplayTaskList currentList;
    
    private Set<Map.Entry<Integer, DisplayTaskList>> currentDisplayListForPriority;
    private Set<Map.Entry<Date, DisplayTaskList>> currentDisplayListForDate;
    
    private boolean isMessageDisplayed;
    
    private ArrayList<NavigationBar> currentNavigationBars;
    
    private final static Logger userInterfaceLogger = Logger.getLogger(UserInterface.class.getName());
    
    private char characterToTransfer;
    private boolean isBackspacePressed;
    
    private JComboBox<String> popupBox;
    
    private DisplayStateStack displayStateStack;
    private final int maxNumOfDisplayStates = 100;
    
    public static void main(String[] args) {
        
        userInterfaceLogger.setLevel(java.util.logging.Level.SEVERE);
        
        EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                
                try {
                    
                    UserInterface frame = new UserInterface();
                    frame.setVisible(true);
                    
                } catch (Exception e) {
                    
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });
    }

    public UserInterface() {
        
        // Initialise engine
        if( !Engine.init() ){
            
            JOptionPane.showMessageDialog(null, "Engine failed to initialise :(", "Error Message", JOptionPane.ERROR_MESSAGE);
            
            userInterfaceLogger.severe("Fail to intialise engine");
            
            System.exit(1);
        }
        
        isMessageDisplayed = true;
        
        // Main frame
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Adding paintable component to main frame
        setBounds(100, 100, 867, 587);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        prepareCommandTextField();
        prepareDisplay();
        prepareCloseButton();
        prepareMinimiseButton();
        prepareSlidePanel();
        prepareNavigationPanel();
        prepareDragPanel();
        prepareSectionTitle();
        prepareSectionTitleLine();
        prepareBackground();
        
        if( displayPane != null ){
            
            prepareUpArrowButton(displayPane);
            
            prepareDownArrowButton(displayPane);
            
            prepareTaskDisplayPanel(displayPane);
        }
        
        setUndecorated(true);
        setLocationRelativeTo(null);
    }
    
    private void showTutorialScreen( int tutorialIndex, String userInput ){
        
        currentDisplayListForDate = null;
        currentDisplayListForPriority = null;
        
        switch( tutorialIndex ){
        
            case Constants.ADD_TUTORIAL:

                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_ADD, 
                        Constants.HELP_CONTENT[Constants.ADD_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.ADD_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.ADD_TUTORIAL);
                
                break;
                
            case Constants.DELETE_TUTORIAL:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_DELETE, 
                        Constants.HELP_CONTENT[Constants.DELETE_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.DELETE_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.DELETE_TUTORIAL);
                
                break;
                
            case Constants.UPDATE_TUTORIAL:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_UPDATE, 
                        Constants.HELP_CONTENT[Constants.UPDATE_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.UPDATE_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.UPDATE_TUTORIAL);
                
                break;
            
            case Constants.DONE_TUTORIAL:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_DONE, 
                        Constants.HELP_CONTENT[Constants.DONE_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.DONE_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.DONE_TUTORIAL);
                
                break;
                
            case Constants.SEARCH_TUTORIAL:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_SEARCH, 
                        Constants.HELP_CONTENT[Constants.SEARCH_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.SEARCH_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.SEARCH_TUTORIAL);
                
                break;
                
            case Constants.UNDO_TUTORIAL:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP_UNDO, 
                        Constants.HELP_CONTENT[Constants.UNDO_TUTORIAL][0], 
                        userInput, null ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, Constants.HELP_CONTENT[Constants.UNDO_TUTORIAL][0], null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, Constants.UNDO_TUTORIAL);
                
                break;
            
            default:
                
                displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.HELP, "Tutorial", null, 
                        new KeyEvent( displayPane, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                                      0, KeyEvent.VK_F3, '\0', KeyEvent.KEY_LOCATION_STANDARD) ) );
                
                updateGUIView(currentDisplayListForDate, currentDisplayListForPriority, "Tutorial", null);
                
                displayPane.addInfoToDisplay(Constants.HELP_CONTENT, -1);
                
                break;
        }
    }
    
    private void prepareTaskDisplayPanel( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            taskDisplayPanel = displayPanel.getDisplayComponent();
            
            addKeyBindingsToTextView(taskDisplayPanel);
            
            userInterfaceLogger.info( "Task display panel set up successfully" );
            
        } else{
            
            userInterfaceLogger.severe("Task display panel fail to initialise");
        }
    }
    
    private void prepareDownArrowButton( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            downArrowButton = displayPanel.getDownButtonComponent();
            
            addKeyBindingsToButton(downArrowButton);
        }
    }
    
    private void prepareUpArrowButton( DisplayPane displayPanel ){
        
        if( displayPanel != null ){
            
            upArrowButton = displayPanel.getUpButtonComponent();
            
            addKeyBindingsToButton(upArrowButton);
        }
    }
    
    private void addKeyBindingsToTextView( JTextPane currentTextView ){
        
        if( currentTextView != null ){
            
            currentTextView.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {
                    
                    handleKeyEvent(event);
                }
            });
        }
    }
    
    private void addKeyBindingsToButton( InvisibleButton currentArrowButton ){
        
        if( currentArrowButton != null ){
            
            currentArrowButton.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {
                    
                    handleKeyEvent(event);
                }
            });
        }
    }
    
    private void updateNavigationBarTaskInfoContents( JTextPane inputField, ArrayList<NavigationBar> navigationBars, DisplayTask currentTask ){
        
        if( inputField != null && navigationBars != null && !navigationBars.isEmpty() && 
            currentTask != null && currentTask.getParent() != null ){
            
            NavigationBar tempNavigationBar = navigationBars.get(0);
            
            if( tempNavigationBar != null ){
                
                String additonalkey = (!inputField.isFocusOwner() ? "/Enter" : "");
                
                tempNavigationBar.setMessageToView(planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + currentTask.getParent().getID(), 
                        "F1"+additonalkey);
            }
        }
    }
    
    private char filterKeys( char keyCode ){
        
        if( (keyCode >= 32 && keyCode <= 126) || keyCode == 8 ){
            
            return keyCode;
            
        } else{
            
            return '\0';
        }
    }
    
    private void handleKeys( char keyTyped ){
        
        if( keyTyped >= 32 && keyTyped <= 126){
            
            characterToTransfer = keyTyped;
            isBackspacePressed = false;
            
        } else if( keyTyped == 8 ){
            
            characterToTransfer = '\0';
            isBackspacePressed = true;
            
        } else{
            
            characterToTransfer = '\0';
            isBackspacePressed = false;
        }
    }
    
    private boolean handleNavigationKeys(KeyEvent event){
        
        if( event != null ){
            
            if( event.getKeyCode() == KeyEvent.VK_F10 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.NOTDONE ){
                        
                    Set<Map.Entry<Date, DisplayTaskList>>tempTaskList = Engine.getUndoneTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.NOTDONE, "Ongoing Tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Ongoing Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F9 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.DONE ){
                        
                    Set<Map.Entry<Date, DisplayTaskList>>tempTaskList = Engine.getDoneTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.DONE, "Completed Tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Completed Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F8 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.OVERDUE ){
                        
                    Set<Map.Entry<Date, DisplayTaskList>>tempTaskList = Engine.getOverdueTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.OVERDUE, "Overdue Tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Overdue Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F7 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TENTATIVE ){
                        
                    Set<Map.Entry<Integer, DisplayTaskList>>tempTaskList = Engine.getFloatingTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = null;
                    currentDisplayListForPriority = tempTaskList;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.TENTATIVE, "Floating Tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Floating Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F6 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.UPCOMING ){
                        
                    Set<Map.Entry<Date, DisplayTaskList>>tempTaskList = Engine.getUpcomingTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.UPCOMING, "Upcoming Tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Upcoming Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F5 ){
                
                DisplayState currentDisplayState = displayStateStack.peek();
                
                System.out.println( "entered f5" + (displayStateStack.isEmpty() ? "empty" : "not empty") );
                
                if( currentDisplayState != null &&
                    currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.ALL ){
                    
                    Set<Map.Entry<Date, DisplayTaskList>>tempTaskList = Engine.getAllTasks();
                    currentList = convertToDisplayTaskList(tempTaskList);
                    currentDisplayListForDate = tempTaskList;
                    currentDisplayListForPriority = null;
                    
                    displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, event ));
                    
                    updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "All Tasks", null );
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_F4 ){
                
                if( !event.isAltDown() ){
                    
                    DisplayState currentDisplayState = displayStateStack.peek();
                    
                    if( currentDisplayState != null &&
                        currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TODAY ){
                        
                        Set<Map.Entry<Integer, DisplayTaskList>>tempTaskList = Engine.getTodayTasks();
                        currentList = convertToDisplayTaskList(tempTaskList);
                        currentDisplayListForDate = null;
                        currentDisplayListForPriority = tempTaskList;
                        
                        displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.TODAY, "Tasks due today", null, event ));
                        
                        updateGUIView( currentDisplayListForDate, currentDisplayListForPriority, "Tasks due today", null );
                        
                        event.consume();
                        
                        return true;
                    }
                    
                } else{
                    
                    handleExit();
                    
                    event.consume();
                    
                    return true;
                }
                
            } else if(event.getKeyCode() == KeyEvent.VK_F3){
                
                showTutorialScreen( -1, null );
                
                event.consume();
                        
                return true;
                
            } else if( event.getKeyCode() == KeyEvent.VK_F2 ){
                
                handlePreviousViewOperation(displayStateStack);
                
                event.consume();
                
                return true;
                
            }
        }
        
        return false;
    }
    
    private void handleKeyEvent(KeyEvent event){
        
        if( event != null ){
            
            if(  commandPanel != null ){
                
                if( commandPanel.handleKeyEvent(event) ){
                   
                    return;
                }
            }
            
            if( handleNavigationKeys(event) ){
                
                return;
            }
            
            if( event.getKeyCode() == KeyEvent.VK_PAGE_UP ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( displayPane.hasTasksDisplayed() ){
                    
                    displayPane.selectTaskRelativeToCurrentSelectedTask(-8);
                    
                    event.consume();
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_PAGE_DOWN ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( displayPane.hasTasksDisplayed() ){
                
                    displayPane.selectTaskRelativeToCurrentSelectedTask(8);
                    
                    event.consume();
                }
                
            }  else if( event.getKeyCode() == KeyEvent.VK_ESCAPE ){
         
                if( slidePanel.isVisible()){
                    
                    slidePanel.slideIn();
                    
                    event.consume();
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_UP ){
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( slidePanel.isVisible() && event.isControlDown() ){
                    
                    JScrollPane tempScrollPane = slidePanel.getDisplayScrollComponent();
                    JScrollBar verticalScrollBar = tempScrollPane != null ? tempScrollPane.getVerticalScrollBar() : null;
                    
                    if( verticalScrollBar != null && verticalScrollBar.isVisible() ){
                        
                        int currentScrollValue = verticalScrollBar.getValue();
                        int tempScrollUnitDifference = -verticalScrollBar.getUnitIncrement(-1);
                        verticalScrollBar.setValue(currentScrollValue + tempScrollUnitDifference);
                        
                        event.consume();
                       
                        return;
                    }
                }
                
                displayPane.selectPreviousTask();
                
                if( currentNavigationBars != null && 
                    currentNavigationBars.size() > 0 && 
                    currentNavigationBars.get(0).isVisible() ){
                    
                    DisplayTask currentTask = displayPane.getCurrentSelectedTask();
                    
                    if( currentTask != null && currentTask.getParent() != null ){
                        
                        updateNavigationBarTaskInfoContents( commandInputField, currentNavigationBars, currentTask );
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    DisplayTask tempTask = displayPane.getCurrentSelectedTask();
                    
                    if( tempTask != null && tempTask.getParent() != null ){
                        
                        slidePanel.populateDisplay(tempTask.getParent());
                    }
                }
                
                if( displayPane.hasTasksDisplayed() ){
                    
                    event.consume();
                }
                
            } else if( event.getKeyCode() == KeyEvent.VK_DOWN ){
                
                
                
                if( !displayPane.isFocusOwner() ){
                    
                    displayPane.requestFocusInWindow();
                }
                
                if( slidePanel.isVisible() && event.isControlDown() ){
                    
                    JScrollPane tempScrollPane = slidePanel.getDisplayScrollComponent();
                    JScrollBar verticalScrollBar = tempScrollPane != null ? tempScrollPane.getVerticalScrollBar() : null;
                    
                    if( verticalScrollBar != null && verticalScrollBar.isVisible() ){
                    
                        int currentScrollValue = verticalScrollBar.getValue();
                        int tempScrollUnitDifference = verticalScrollBar.getUnitIncrement(1);
                        verticalScrollBar.setValue(currentScrollValue + tempScrollUnitDifference);
                        
                        event.consume();
                        
                        return;
                    }
                }
                
                displayPane.selectNextTask();
                
                if( currentNavigationBars != null && 
                        currentNavigationBars.size() > 0 && 
                        currentNavigationBars.get(0).isVisible() ){
                    
                    DisplayTask currentTask = displayPane.getCurrentSelectedTask();
                    
                    if( currentTask != null && currentTask.getParent() != null ){
                        
                        updateNavigationBarTaskInfoContents( commandInputField, currentNavigationBars, currentTask );
                    }
                }
                
                if( slidePanel.isVisible() ){
                    
                    DisplayTask tempTask = displayPane.getCurrentSelectedTask();
                    
                    if( tempTask != null && tempTask.getParent() != null ){
                        
                        slidePanel.populateDisplay(tempTask.getParent());
                    }
                }
                
                if( displayPane.hasTasksDisplayed() ){
                    event.consume();
                }
                
            } else{
                
                if( !commandInputField.isFocusOwner() ){
                    
                    if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                        
                        DisplayTask tempTask = displayPane.getCurrentSelectedTask();
                        
                        if( tempTask != null && tempTask.getParent() != null ){
                            
                            slidePanel.slideOut( tempTask.getParent() );
                        }
                        
                        event.consume();
                        
                        return;
                        
                    } else{
                        
                        characterToTransfer = '\0';
                        isBackspacePressed = false;
                        
                        handleKeys( filterKeys(event.getKeyChar()) );
                        
                        commandInputField.requestFocusInWindow();
                        
                        event.consume();
                    }
                }
                
                if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
                    
                    if( commandInputField.getText().length() <= 0 ){
                        
                        event.consume();
                    } 
                    
                } else if( event.getKeyCode() == KeyEvent.VK_ENTER ){
                       
                    
                    
                    String input = commandInputField.getText();
                    
                    if( input.length() > 0 ){
                        
                        processCommand(input);
                        
                        displayPane.requestFocusInWindow();
                        
                        isMessageDisplayed = true;
                        
                    } else{
                        
                        event.consume();
                    }
                    
                }
            }
        }
    }
    
    private void prepareSectionTitle(){
        
        sectionTitle = new JLabel();
        sectionTitle.setBounds(6, 30, 497, 33);
        sectionTitle.setFont( new Font( "Arial", Font.BOLD, 24 ) );
        sectionTitle.setForeground( new Color( 255,255,255,200 ) );
        sectionTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(sectionTitle);
        sectionTitle.setText("All Tasks");
    }
    
    private void prepareSectionTitleLine(){
        
        sectionTitleLine = new JLabel();
        sectionTitleLine.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/titleLine.png")));
        sectionTitleLine.setBounds(-142, 28, 643, 33);
        contentPane.add(sectionTitleLine);
    }
    
    private void prepareSlidePanel(){
        
        slidePanelFrame = new JPanel();
        slidePanelFrame.setBounds(670, 45, 396, 520);
        contentPane.add(slidePanelFrame);
        slidePanelFrame.setLayout(null);
        slidePanelFrame.setOpaque(false);
        //slidePanelFrame.setFocusable(false);
        
        slidePanel = new SliderPanel(0, 198);
        slidePanel.setBounds(198, 0, 202, 520);
        slidePanelFrame.add(slidePanel);
    }
    
    private void prepareNavigationPanel(){
        
        navigationPanel = new JTextPane();
        navigationPanel.setBounds(10, 0, 188, 519);
        slidePanelFrame.add(navigationPanel);
        
        navigationPanel.setEditable(false);
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(null);
        navigationPanel.setHighlighter(null);
        navigationPanel.setFocusable(false);
        navigationPanel.setFont( new Font( "Arial", Font.PLAIN, 2 ) );
        
        navigationPanel.setText( "\n\n\n\n\n\n\n\n\n\n\n\n\n" );
        
        currentNavigationBars = generateContentForNavigationBars( displayStateStack.peek() );
        addNavigationBarsToPanel(currentNavigationBars);
    }
    
    private ArrayList<NavigationBar> generateContentForNavigationBars( DisplayState currentDisplayState ){
        
        ArrayList<NavigationBar> tempList = new ArrayList<NavigationBar>();
        
        if( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS_SIZE == planner.Constants.NAVIGATION_BAR_STRING_CONTENTS.length ){
            
            int key = 1;
            
            // More info on current task
            if( currentList != null && currentList.size() > 0 ){
                
                DisplayTask tempTask = displayPane.getCurrentSelectedTask();
                          
                if( tempTask != null && tempTask.getParent() != null ){
                    
                    String additonalkey = (!commandInputField.isFocusOwner() ? "/Enter" : "");
                    
                    tempList.add( new NavigationBar( 
                            planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + tempTask.getParent().getID(), 
                            "F" + key + additonalkey ) );
                    
                } else{
                    
                    tempList.add( new NavigationBar(null) );
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // Previous view
            if( displayStateStack.size() > 1 ){
                
                tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[1], "F" + key ) );
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // tutorial
            if( currentDisplayState != null ){
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.HELP ){
                    
                    tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[2], "F" + key ));
                    
                } else{
                    
                    tempList.add( new NavigationBar( planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[2] ) );
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // Today tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getTodayTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TODAY ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[3], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[4], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[3] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[4] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // all tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getAllTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.ALL ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[5], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[6], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[5] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[6] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar(null) );
            }
            ++key;
            
            // Upcoming Tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getUpcomingTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.UPCOMING ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[7], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[8], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[7] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[8] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Tentative tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getFloatingTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.TENTATIVE ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[9], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[10], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[9] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[10]));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Overdue tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getOverdueTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.OVERDUE ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[11], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[12], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[11] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[12] ));
                    }
                }

            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            // Done tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getDoneTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.DONE ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[13], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[14], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[13] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[14] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
            
            //Undone Tasks
            if( currentDisplayState != null ){
                
                int taskListSize = Engine.getUndoneTasksSize();
                
                if( currentDisplayState.getdisplayStateFlag() != planner.Constants.DisplayStateFlag.NOTDONE ){
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[15], "F" + key ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[16], "F" + key ));
                    }
                    
                } else{
                    
                    if( taskListSize == 1 ){
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[15] ));
                        
                    } else{
                        
                        tempList.add( new NavigationBar( taskListSize + planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[16] ));
                    }
                }
                
            } else{
                
                tempList.add( new NavigationBar( null ) );
            }
            ++key;
        }
        
        return tempList;
    }
    
    private void addNavigationBarsToPanel( ArrayList<NavigationBar> listOfNavigationBarComponents ){
        
        try{
            
            if( listOfNavigationBarComponents != null && listOfNavigationBarComponents.size() > 0 ){

                navigationPanel.setText("\n\n\n\n\n\n\n\n\n\n\n\n\n");
                
                StyledDocument styledDocument = navigationPanel.getStyledDocument();
                
                Iterator<NavigationBar> iterator = listOfNavigationBarComponents.iterator();
                
                NavigationBar tempNavigationBar;
                
                while( iterator.hasNext() ){
                    
                    tempNavigationBar = iterator.next();
                    
                    if( tempNavigationBar.isVisible() ){
                        
                        Style style = styledDocument.addStyle("component", null);
                        StyleConstants.setComponent(style, tempNavigationBar);
                        
                        styledDocument.insertString( styledDocument.getLength(), "component", style );
                    }
                }
            }
            
        } catch( BadLocationException badLocationException ){}
    }
    
    private void prepareBackground(){
        
        // Adding UI background
        background = new JLabel();
        background.setIcon(new ImageIcon(UserInterface.class.getResource("/planner/UI_Pic.png")));
        background.setBounds(0, 0, 867, 587);
        contentPane.add(background);
    }
    
    private void prepareDisplay(){
        
        displayPane = new DisplayPane();
        displayPane.setBounds(25, 83, 630, 430);
        
        contentPane.add(displayPane);
        
        displayStateStack = new DisplayStateStack(maxNumOfDisplayStates);
        
        // Copying of all tasks
        currentDisplayListForDate = Engine.getAllTasks();
        currentDisplayListForPriority = null;
        
        currentList = convertToDisplayTaskList( currentDisplayListForDate );
        
        // Display all tasks as default screen for now
        //displayPane.addTasksToDisplay(currentList);
        displayPane.displayByDate(currentDisplayListForDate);
        
        displayPane.requestFocusInWindow();
        
        addKeyBindingsToDisplay(displayPane);
        
        displayStateStack.push(new DisplayState( planner.Constants.DisplayStateFlag.ALL, "All tasks", null, 
                                                new KeyEvent( displayPane, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                                                              0, KeyEvent.VK_F5, '\0', KeyEvent.KEY_LOCATION_STANDARD) ) );
    }
    
    private void addKeyBindingsToDisplay( DisplayPane currentDisplayPane ){
        
        if( currentDisplayPane != null ){
            
            currentDisplayPane.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent event) {
                    
                    handleKeyEvent(event);
                    
                }
            });
        }
    }
    
    private ArrayList<Map.Entry<String, ArrayList<String>>> generateTutorialStringMappings( String [][]possibleCommands ){
        
        ArrayList<Map.Entry<String, ArrayList<String>>> mapping = new ArrayList<Map.Entry<String, ArrayList<String>>>();
        
        if( possibleCommands != null ){
            
            String sectionHeaderString;
            String []tempStringArray;
            ArrayList<String> newList = null;
            for( int i = 0; i < possibleCommands.length; ++i ){
                
                tempStringArray = possibleCommands[i];
                
                for( int j = 0; j < tempStringArray.length; ++j ){
                    
                    if( j > 0 ){
                        
                        newList.add(tempStringArray[j]);
                        
                    } else{
                        
                        newList = new ArrayList<String>();
                        
                        sectionHeaderString = tempStringArray[j];
                        
                        if( sectionHeaderString != null ){
                            
                            mapping.add( new AbstractMap.SimpleEntry<String, ArrayList<String>>( sectionHeaderString, newList ) );
                            
                        } else{
                            
                            break;
                        }
                    }
                }
            }
        }
        
        return mapping;
    }
    
    private void prepareCommandTextField(){
        
        // New command
        ArrayList<Map.Entry<String, ArrayList<String>>> possibleCommands = generateTutorialStringMappings( Constants.POSSIBLE_COMMANDS );
        
        commandPanel = new CommandTextbox( Constants.COMMAND_KEYWORDS, Constants.NONCOMMAND_KEYWORDS, possibleCommands );
        commandPanel.setBounds(34, 530, 610, 60);
        contentPane.add(commandPanel);
        commandInputField = commandPanel.getTextDisplay();
        commandPanel.setFontAttributes(new Font( "Arial", Font.BOLD, 20 ));
        commandPanel.setForegroundColor(new Color( 128,128,128 ));
        commandPanel.setText("Enter commands here", true);
        
        addKeyBindingsToCommandTextField(commandInputField);
        addFocusListenerToCommandTextField( commandPanel, commandInputField );
        
        popupBox = commandPanel.getPopupBox();
        
        addKeyBindingsToPopupBox(popupBox);
        
        characterToTransfer = '\0';
        isBackspacePressed = false;
    }
    
    private void addKeyBindingsToPopupBox( final JComboBox<String> currentPopupBox ){
        
        if( currentPopupBox != null ){
            
            currentPopupBox.addKeyListener(new KeyListener(){

                @Override
                public void keyPressed(KeyEvent e) {
                    
                    if( handleNavigationKeys(e) ){
                        
                        return;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    
                    if( handleNavigationKeys(e) ){
                        
                        return;
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    
                    if( handleNavigationKeys(e) ){
                        
                        return;
                    }
                }
                
            });
        }
    }
    
    private void addKeyBindingsToCommandTextField( JTextPane currentCommand ){
        
        if( currentCommand != null ){
            
            currentCommand.addKeyListener(new KeyListener(){
                
                @Override
                public void keyPressed( KeyEvent e ){
                        
                    handleKeyEvent(e);
                    
                    characterToTransfer = '\0';
                    isBackspacePressed = false;
                    
                    return;
                }
        
                @Override
                public void keyTyped(KeyEvent e) {
                    
                    if( characterToTransfer != '\0' ){
                        
                        e.consume();
                    }
                    
                    return;
                }
        
                @Override
                public void keyReleased(KeyEvent e) {
                    
                    e.consume();
                    
                    return;
                }
            });
        }
    }
    
    private void addFocusListenerToCommandTextField( final CommandTextbox commandTextbox, final JTextPane currentCommand ){
        
        if(currentCommand != null && commandTextbox != null){
            
            currentCommand.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    
                    commandTextbox.setSyntaxFilterOn();
                    
                    commandTextbox.setForegroundColor( new Color( 0,0,0 ) );
                    
                    String additionalCharacterToAdd = (characterToTransfer != '\0' ? characterToTransfer + "" : "");
                    
                    if( isMessageDisplayed ){
                        
                        commandTextbox.setText(additionalCharacterToAdd, false);
                        
                        isMessageDisplayed = false;
                        
                    } else{
                        
                        String finalString = currentCommand.getText() + additionalCharacterToAdd;
                        
                        if( isBackspacePressed && finalString.length() > 0 ){
                            
                            finalString = finalString.substring(0, finalString.length()-1);
                        }
                        
                        commandTextbox.setText(finalString, false);
                    }
                    
                    if( currentNavigationBars.get(0).isVisible() 
                        && displayPane.getCurrentSelectedTask() != null 
                        && displayPane.getCurrentSelectedTask().getParent() != null ){
                           
                        updateNavigationBarTaskInfoContents( commandInputField, currentNavigationBars, displayPane.getCurrentSelectedTask() );
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    
                    commandTextbox.setSyntaxFilterOff();
                    
                    commandTextbox.hidePopupBox();
                    
                    commandTextbox.setForegroundColor( new Color( 128,128,128 ) );
                    
                    String input = currentCommand.getText();
                    
                    if( !isMessageDisplayed && input.length() <= 0 ){
                        
                        commandTextbox.setText("Enter commands here", true);
                        
                        isMessageDisplayed = true;
                        
                    } else{
                        
                        commandTextbox.setText(input, true);
                    }
                    
                    characterToTransfer = '\0';
                    isBackspacePressed = false;
                    
                    if( currentNavigationBars.get(0).isVisible() 
                            && displayPane.getCurrentSelectedTask() != null 
                            && displayPane.getCurrentSelectedTask().getParent() != null ){
                           
                           String messageTitle = planner.Constants.NAVIGATION_BAR_STRING_CONTENTS[0] + displayPane.getCurrentSelectedTask().getParent().getID();
                           
                           currentNavigationBars.get(0).setMessageToView(messageTitle, "F1/Enter" );
                       }
                }
            });
        }
    }

    private void prepareCloseButton(){
        
        closeButton = new JLabel();
        closeButton.setBounds(819, 13, 27, 27);
        contentPane.add(closeButton);
        
        closeButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        addMouseActionListenersToCloseButton(closeButton);
    }

    private void addMouseActionListenersToCloseButton( JLabel currentCloseButton ){
        
        if( currentCloseButton != null ){
            
            currentCloseButton.addMouseListener( new MouseInputAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                        
                        handleExit();
                    }
                }
            });
        }
    }
    
    private void prepareMinimiseButton(){
        
        minimiseButton = new JLabel();
        minimiseButton.setBounds(782, 12, 28, 28);
        contentPane.add(minimiseButton);
        
        minimiseButton.setCursor(new Cursor( Cursor.HAND_CURSOR ));
        
        addMouseActionListenersToMinimiseButton(minimiseButton);
    }

    private void addMouseActionListenersToMinimiseButton( JLabel currentMinimiseButton ){
        
        if( currentMinimiseButton != null ){
            
            currentMinimiseButton.addMouseListener( new MouseInputAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if( javax.swing.SwingUtilities.isLeftMouseButton(e) ){
                        
                        setState( UserInterface.ICONIFIED );
                    }
                }
                
            } );
        }
    }
    
    private void prepareDragPanel(){
        
        dragPanel = new JLabel();
        dragPanel.setBounds(0, 0, 867, 587);
        contentPane.add(dragPanel);
        
        addMouseMovementBindingsToDragPanel(dragPanel);
        addMouseActionListenersToDragPanel(dragPanel);
    }
    
    private void addMouseMovementBindingsToDragPanel( JLabel currentDragPanel ){
        
        if( currentDragPanel != null ){
            
            currentDragPanel.addMouseMotionListener( new MouseMotionAdapter(){ 
    
                @Override
                public void mouseDragged(MouseEvent e) {
                    
                    int mouseDragXCoordinate = e.getXOnScreen();
                    int mouseDragYCoordinate = e.getYOnScreen();
                    
                    setLocation( mouseDragXCoordinate - mouseXCoordinate,
                                 mouseDragYCoordinate - mouseYCoordinate);
                }                
            });
        }
    }
    
    private void addMouseActionListenersToDragPanel( JLabel currentDragPanel ){
        
        if( currentDragPanel != null ){
            
            currentDragPanel.addMouseListener( new MouseInputAdapter(){ 

                @Override
                public void mousePressed(MouseEvent e) {
                    
                    mouseXCoordinate = e.getX();
                    mouseYCoordinate = e.getY();
                }
            });
        }
    }
}
