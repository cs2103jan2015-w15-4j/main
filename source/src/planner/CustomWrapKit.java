//@author A0111333B

package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
* The CustomWrapKit class is a style editor kit used by text components to allow for strings with widths exceeding the 
* width of the text component to wrap around to the next line
*
* @author A0111333B
*/
public class CustomWrapKit extends StyledEditorKit{

    private ViewFactory defaultViewFactory_;
    
    /**
     * Constructs a style editor kit using a custom view factory that handles wrap text logic
     */
    public CustomWrapKit(){
        super();
        defaultViewFactory_ = new WrapTextLogic();
    }
    
    /**
     * Retrieves the view factory currently used by this editor kit
     * 
     * @return The view factory currently used by this editor kit to handle wrap text logic
     */
    @Override
    public ViewFactory getViewFactory(){
        return defaultViewFactory_;
    }
}
