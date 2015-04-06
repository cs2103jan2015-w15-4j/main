package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class CustomNoWrapKit extends StyledEditorKit{

    private ViewFactory customViewFactory;
    
    public CustomNoWrapKit(){
        
        super();
        
        customViewFactory = new NoWrapTextLogic();
    }
    
    @Override
    public ViewFactory getViewFactory(){
        
        return customViewFactory;
    }
}
