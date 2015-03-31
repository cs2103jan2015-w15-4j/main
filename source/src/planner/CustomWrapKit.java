package planner;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class CustomWrapKit extends StyledEditorKit{

    private ViewFactory customViewFactory;
    
    public CustomWrapKit(){
        
        super();
        
        customViewFactory = new WrapTextLogic();
    }
    
    @Override
    public ViewFactory getViewFactory(){
        
        return customViewFactory;
    }
}
