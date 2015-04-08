//@author A0111333B

package planner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class testCommandPanelDocumentFilter {

    @Test
    public void testRegularStringArray() {
       
        CommandPanelDocumentFilter commandPanelDocumentFilter = new CommandPanelDocumentFilter();
        
        String []keywords = { "nice", "done" };
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(keywords);
        
        assertEquals( regexOutput, "(\\bnice\\b|\\bdone\\b)" );
    }
    
    @Test
    public void testStringArrayContainingNull() {
        
        CommandPanelDocumentFilter commandPanelDocumentFilter = new CommandPanelDocumentFilter();
        
        String []keywords = { "nice", null, "done" };
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(keywords);
        
        assertEquals( regexOutput, "(\\bnice\\b|\\bdone\\b)" );
    }

    @Test
    public void testNullStringArray(){
        
        CommandPanelDocumentFilter commandPanelDocumentFilter = new CommandPanelDocumentFilter();
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(null);
        
        assertEquals( regexOutput, "()" );
    }
}
