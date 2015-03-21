package planner;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testAddCommand() {
        ParseResult result = Parser.parse("add important meeting with boss on 5 Mar 2018 until 6 Mar 2018 priority 5 tag important");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.ADD, result.getCommandType());
        assertEquals("Mon Mar 05 00:00:00 SGT 2018", result.getDate().toString());
        assertEquals("Tue Mar 06 00:00:00 SGT 2018", result.getSecondDate().toString()); 
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
    public void testUpdateCommand() {
        ParseResult result = Parser.parse("update 123 date 23 June 2015");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.UPDATE, result.getCommandType());
        assertEquals("Tue Jun 23 00:00:00 SGT 2015", result.getDate().toString());
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
    public void testDeleteCommand() {
        ParseResult result = Parser.parse("remove 462 at 15 Nov 1995");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.DELETE, result.getCommandType());
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
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.SHOW_ALL, result.getCommandType());
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
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.SHOW_ONE, result.getCommandType());
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
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.DONE, result.getCommandType());
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
    // test done command without a valid id immediately after 'done' (incorrect usage)
    public void testDoneCommandWithoutID() {
        ParseResult result = Parser.parse("done fishburger 234234 pig sandwich");
        assertEquals(Constants.RESULT_TYPE.INVALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.DONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getSecondDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals(Constants.ERROR_TYPE.INVALID_TASK_ID, result.getErrorType());
        boolean[] flags = {false, false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testSearchCommand() {
        ParseResult result = Parser.parse("search sushi bar tag food description delicious on 10 Oct 1528 remind 9 Oct 1528");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.SEARCH, result.getCommandType());
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
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.HELP, result.getCommandType());
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

    // test help command used to show the usage of a specific command
    public void testSpecificHelpCommand() {
        ParseResult result = Parser.parse("help add search delete done at 24 Jan 1172");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.HELP_ADD, result.getCommandType());
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

}
