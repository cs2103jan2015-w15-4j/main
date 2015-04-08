//@author A0111333B

package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class CustomWrapKit extends StyledEditorKit{

    private ViewFactory defaultViewFactory;
    
    public CustomWrapKit(){
        
        super();
        
        defaultViewFactory = new WrapTextLogic();
    }
    
    @Override
    public ViewFactory getViewFactory(){
        
        return defaultViewFactory;
    }
}
