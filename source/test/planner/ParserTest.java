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
        ParseResult result2 = Parser.parse("update 123 date 23 June 2015");
        assertEquals(Constants.RESULT_TYPE.VALID, result2.getResultType());
        assertEquals(Constants.COMMAND_TYPE.UPDATE, result2.getCommandType());
        assertEquals("Tue Jun 23 00:00:00 SGT 2015", result2.getDate().toString());
        assertTrue(result2.getDateToRemind() == null);
        assertEquals(123, result2.getId());
        assertEquals("", result2.getDescription());
        assertEquals("", result2.getTag());
        assertEquals("", result2.getErrorMessage());
        boolean[] flags = {true, false, false, true, false, false, false};
        assertTrue(Arrays.equals(flags, result2.getCommandFlags()));
        assertEquals("", result2.getName());
    }

}
