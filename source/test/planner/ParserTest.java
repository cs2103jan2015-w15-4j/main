package planner;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testAddCommand() {
        ParseResult result = Parser.parse("add important meeting with boss on 5 Mar 2018 priority 5 tag important");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.ADD, result.getCommandType());
        assertEquals("Mon Mar 05 00:00:00 SGT 2018", result.getDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(5, result.getPriorityLevel());
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("important", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {true, false, true, false, true, false, true};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("important meeting with boss", result.getName());
        
    }

    @Test
    public void testUpdateCommand() {
        ParseResult result = Parser.parse("update 123 date 23 June 2015");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.UPDATE, result.getCommandType());
        assertEquals("Tue Jun 23 00:00:00 SGT 2015", result.getDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(123, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {true, false, false, true, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testDeleteCommand() {
        ParseResult result = Parser.parse("remove 462 at 15 Nov 1995");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.DELETE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(462, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {false, false, false, true, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testShowAllTasksCommand() {
        ParseResult result = Parser.parse("display for the at 10 Jun 1999");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.SHOW_ALL, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());
    }

    @Test
    public void testShowSingleTaskCommand() {
        ParseResult result = Parser.parse("display 135135 on at by 1992");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.SHOW_ONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(135135, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {false, false, false, true, false, false, false};
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
        assertTrue(result.getDateToRemind() == null);
        assertEquals(347564, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {false, false, false, true, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName());        

    }
    
    @Test
    // test done command without a valid id immediately after 'done'
    public void testDoneCommandWithoutID() {
        ParseResult result = Parser.parse("done fishburger 234234 pig sandwich");
        assertEquals(Constants.RESULT_TYPE.INVALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.DONE, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_ID_SET, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("a number must be entered for the task id", result.getErrorMessage());
        boolean[] flags = {false, false, false, false, false, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("", result.getName()); 
    }

}
