//@author A0111333B

package planner;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;

/**
* The NonWrappingParagraphLayout class handles the layout of the editor kit used for non wrapping text
*
* @author A0111333B
*/
public class NonWrappingParagraphLayout extends ParagraphView{

    /**
     * Constructs the layout for the editor kit used for non wrapping text
     */
    public NonWrappingParagraphLayout( Element element ){
        super(element);
    }
    
    /**
     * Sets the width and height of the layout for the editor kit used for non wrapping text
     * 
     * @param width     The width of the layout to be set
     * @param height    The height of the layout to be set
     */
    @Override
    public void layout( int width, int height ){
        super.layout( Integer.MAX_VALUE, height );
    }
    
    /**
     * Returns the minimum span of the layout
     * 
     * @param axis     The orientation of the layout
     * @return         The minimum span of the layout
     */
    @Override
    public float getMinimumSpan( int axis ){
        return super.getPreferredSpan(axis);
    }
}
