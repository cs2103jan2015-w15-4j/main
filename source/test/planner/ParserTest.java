package planner;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testAddCommand() {
        ParseResult result = Parser.parse("add anniversary outing with my waifu at 3 May 2015");
        assertEquals(Constants.RESULT_TYPE.VALID, result.getResultType());
        assertEquals(Constants.COMMAND_TYPE.ADD, result.getCommandType());
        assertEquals("Sun May 03 00:00:00 SGT 2015", result.getDate().toString());
        assertTrue(result.getDateToRemind() == null);
        assertEquals(Constants.NO_PRIORITY_LEVEL, result.getPriorityLevel());
        assertEquals(0, result.getId());
        assertEquals("", result.getDescription());
        assertEquals("", result.getTag());
        assertEquals("", result.getErrorMessage());
        boolean[] flags = {true, false, false, false, true, false, false};
        assertTrue(Arrays.equals(flags, result.getCommandFlags()));
        assertEquals("anniversary outing with my waifu ", result.getName());
        
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
        assertEquals(Constants.COMMAND_TYPE.SHOW, result.getCommandType());
        assertTrue(result.getDate() == null);
        assertTrue(result.getDateToRemind() == null);
        assertEquals(0, result.getId());
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
        assertEquals(Constants.COMMAND_TYPE.SHOW, result.getCommandType());
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

}
