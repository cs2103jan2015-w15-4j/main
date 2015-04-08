//@author A0111333B

package planner;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;

public class NonWrappingParagraphLayout extends ParagraphView{

    public NonWrappingParagraphLayout( Element element ){
        
        super(element);
    }
    
    @Override
    public void layout( int width, int height ){
        
        super.layout( Integer.MAX_VALUE, height );
    }
    
    @Override
    public float getMinimumSpan( int axis ){
        
        return super.getPreferredSpan(axis);
    }
}
