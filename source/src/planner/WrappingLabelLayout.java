//@author A0111333B

package planner;

import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;

/**
* The WrappingLabelLayout class handles the layout of the editor kit used for wrapping text
*
* @author A0111333B
*/
public class WrappingLabelLayout extends LabelView{

    private final float DEFAULT_MINIMUM_SPAN = 0.0f;
    
    /**
     * Constructs the layout for the editor kit used for wrapping text
     */
    public WrappingLabelLayout( Element element ){
        super(element);
    }
    
    /**
     * Returns the minimum span of the layout
     * 
     * @param axis     The orientation of the layout
     * @return         The minimum span of the layout
     */
    @Override
    public float getMinimumSpan( int axis ){
        if( axis == View.Y_AXIS ){
            return super.getMinimumSpan(axis);
        } else{
            return DEFAULT_MINIMUM_SPAN;
        }
    }
}
