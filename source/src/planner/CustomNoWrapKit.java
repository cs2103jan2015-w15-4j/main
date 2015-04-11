//@author A0111333B

package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
* The CustomNoWrapKit class is a style editor kit used by text components to prevent text from being wrapped
* to the next line
*
* @author A0111333B
*/
public class CustomNoWrapKit extends StyledEditorKit{

    private ViewFactory customViewFactory;
    
    /**
    * Constructs a style editor kit using a custom view factory
    */
    public CustomNoWrapKit(){
        super();
        customViewFactory = new NoWrapTextLogic();
    }
    
    /**
     * Gets the view factory currently used by this editor kit
     * 
     * @return The view factory currently used by this editor kit
     */
    @Override
    public ViewFactory getViewFactory(){
        return customViewFactory;
    }
}
