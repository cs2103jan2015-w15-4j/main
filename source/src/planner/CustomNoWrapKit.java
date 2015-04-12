//@author A0111333B

package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
* The CustomNoWrapKit class is a style editor kit used by text components to prevent strings with widths exceeding the 
* width of the text component from being wrapped to the next line but enables the view of the text component to move
* along with the updated string
*
* @author A0111333B
*/
public class CustomNoWrapKit extends StyledEditorKit{

    private ViewFactory customViewFactory_;
    
    /**
    * Constructs a style editor kit using a custom view factory that handles the logic of scrolling of the text component's
    * view in response to increasing string length
    */
    public CustomNoWrapKit(){
        super();
        customViewFactory_ = new NoWrapTextLogic();
    }
    
    /**
     * Gets the view factory currently used by this editor kit
     * 
     * @return The view factory currently used by this editor kit to handle the logic of scrolling of the text component's
     *         view in response to increasing string length
     */
    @Override
    public ViewFactory getViewFactory(){
        return customViewFactory_;
    }
}
