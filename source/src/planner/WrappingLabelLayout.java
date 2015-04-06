package planner;

import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;

public class WrappingLabelLayout extends LabelView{

    public WrappingLabelLayout( Element element ){
        
        super(element);
    }
    
    @Override
    public float getMinimumSpan( int axis ){
        
        if( axis == View.Y_AXIS ){
            
            return super.getMinimumSpan(axis);
            
        } else{
            
            return 0.0f;
        }
    }
}
