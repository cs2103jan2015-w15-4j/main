package planner;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testAddCommandWithDatesTime1PriorityTag() {
        ParseResult result = Parser.parse("add important meeting with boss on 5 Mar 2018 am 9.30 until 6 Mar 2018 priority 5 tag important");
        assertEquals(Constants.CommandType.ADD, result.getCommandType());
        assertEquals("Mon Mar 05 09:30:00 SGT 2018", result.getDate().toString());
        assertEquals("Tue Mar 06 23:59:00 SGT 2018", result.getSecondDate().toString()); 
        assertTrue(result.getDateToRemind() == null);
        assertEquals(5, result.getPriorityLevel());
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("important", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, true, false, true, false, true, true};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("important meeting with boss", result.getName());        
    }

    @Test
    public void testUpdateCommandWithDateTime() {
        ParseResult result = Parser.parse("update 123 date 23 June 2015 pm 7.46");
        assertEquals(Constants.CommandType.UPDATE, result.getCommandType());
        assertEquals("Tue Jun 23 19:46:00 SGT 2015", result.getDate().toString());
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(123, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testNextMonth(){
        ParseResult result = Parser.parse("edit 322 date next month");
        assertEquals(Constants.CommandType.UPDATE, result.getCommandType());
        assertEquals("Wed Apr 01 00:00:00 SGT 2015", result.getDate().toString());
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(322, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testDeleteCommand() {
        ParseResult result = Parser.parse("remove 462 at 15 Nov 1995");
        assertEquals(Constants.CommandType.DELETE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(462, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testShowAllTasksCommand() {
        ParseResult result = Parser.parse("display for the at 10 Jun 1999");
        assertEquals(Constants.CommandType.SHOW_ALL, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testShowSingleTaskCommand() {
        ParseResult result = Parser.parse("display 135135 on at by 1992");
        assertEquals(Constants.CommandType.SHOW_ONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(135135, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    // test correct usage of done
    public void testDoneCommand() {
        ParseResult result = Parser.parse("done 347564 date 111 Mar 3917");
        assertEquals(Constants.CommandType.DONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(347564, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());        

    }
    
    @Test
    // test correct usage of undone
    public void testNotDoneCommand() {
        ParseResult result = Parser.parse("setnotdone 322 date 111 Mar 3917");
        assertEquals(Constants.CommandType.SETNOTDONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(322, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName()); 
    }
    
    @Test
    // test done command without a valid id immediately after 'done' (incorrect usage)
    public void testDoneCommandWithoutID() {
        ParseResult result = Parser.parse("done fishburger 234234 pig sandwich");
        assertEquals(Constants.CommandType.INVALID, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals(Constants.ErrorType.INVALID_TASK_ID, result.getErrorType());
        boolean[] flags = {false, false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testSearchNameTagDescDateReminddate() {
        ParseResult result = Parser.parse("search sushi bar tag food description delicious on 10 Oct 1528 remind 9 Oct 1528");
        assertEquals(Constants.CommandType.SEARCH, result.getCommandType());
        assertEquals("Sat Oct 10 00:00:00 SGT 1528", result.getDate().toString());
        assertTrue(result.getSecondDate() == null);
        assertEquals("Fri Oct 09 00:00:00 SGT 1528", result.getDateToRemind().toString());
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("delicious", result.getDescription());
        assertEquals("food", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, true, false, false, true, true, true, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("sushi bar", result.getName());
    }

    @Test
    // test help command used to show all available commands
    public void testGeneralHelpCommand() {
        ParseResult result = Parser.parse("help me find my fish at 13 Dec 1287");
        assertEquals(Constants.CommandType.HELP, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    // test help command used to show the usage of a specific command
    public void testSpecificHelpCommand() {
        ParseResult result = Parser.parse("help add search delete done at 24 Jan 1172");
        assertEquals(Constants.CommandType.HELP_ADD, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {false, false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testConvertToTimedCommand() {
        ParseResult result = Parser.parse("convert 275 timed date 15 Jun 1992 am 3.50 until 27 Jun 1992 am 2.29");
        assertEquals(Constants.CommandType.CONVERT_TIMED, result.getCommandType());
        assertEquals("Mon Jun 15 03:50:00 SGT 1992", result.getDate().toString());
        assertEquals("Sat Jun 27 02:29:00 SGT 1992", result.getSecondDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(275, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, false, true, false, false, false, true};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testConvertToTimedWithOnlyOneDate() {
        ParseResult result = Parser.parse("convert 12616 timed date 15 Jun 1992");
        assertEquals(Constants.CommandType.INVALID, result.getCommandType());
        assertEquals("Mon Jun 15 00:00:00 SGT 1992", result.getDate().toString());
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(12616, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals(Constants.ErrorType.INVALID_ARGUMENTS, result.getErrorType());
        boolean[] flags = {true, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testConvertToFloatingCommand() {
        ParseResult result = Parser.parse("convert 592 floating date 17 Jun 1992 to 28 Jun 1992");
        assertEquals(Constants.CommandType.CONVERT_FLOATING, result.getCommandType());
        assertEquals("Wed Jun 17 00:00:00 SGT 1992", result.getDate().toString());
        assertEquals("Sun Jun 28 00:00:00 SGT 1992", result.getSecondDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(592, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, false, true, false, false, false, true};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }    
    
    @Test
    public void testConvertToDeadlineCommand() {
        ParseResult result = Parser.parse("convert 491 deadline date 19 Jun 1992 am 4.20 until 24 Jun 1992");
        assertEquals(Constants.CommandType.CONVERT_DEADLINE, result.getCommandType());
        assertEquals("Fri Jun 19 04:20:00 SGT 1992", result.getDate().toString());
        assertEquals("Wed Jun 24 00:00:00 SGT 1992", result.getSecondDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(491, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertTrue(result.getErrorType() == null);
        boolean[] flags = {true, false, false, true, false, false, false, true};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
    
    @Test
    public void testConvertToDeadLineWithNoDate() {
        ParseResult result = Parser.parse("convert 3516 deadline");
        assertEquals(Constants.CommandType.INVALID, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(3516, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals(Constants.ErrorType.INVALID_ARGUMENTS, result.getErrorType());
        boolean[] flags = {false, false, false, true, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }
}
