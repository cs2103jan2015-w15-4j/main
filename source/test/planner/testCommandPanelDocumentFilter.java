//@author A0111333B

package planner;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class testCommandPanelDocumentFilter {

    @Test
    public void testRegularStringArray() {
       
        CustomTextFieldDocumentFilter commandPanelDocumentFilter = new CustomTextFieldDocumentFilter();
        
        String []keywords = { "nice", "done" };
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(keywords);
        
        assertEquals( regexOutput, "(\\bnice\\b|\\bdone\\b)" );
    }
    
    @Test
    public void testStringArrayContainingNull() {
        
        CustomTextFieldDocumentFilter commandPanelDocumentFilter = new CustomTextFieldDocumentFilter();
        
        String []keywords = { "nice", null, "done" };
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(keywords);
        
        assertEquals( regexOutput, "(\\bnice\\b|\\bdone\\b)" );
    }

    @Test
    public void testNullStringArray(){
        
        CustomTextFieldDocumentFilter commandPanelDocumentFilter = new CustomTextFieldDocumentFilter();
        
        String regexOutput = commandPanelDocumentFilter.generateRegex(null);
        
        assertEquals( regexOutput, "()" );
    }
}
