//@author A0111333B

package planner;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
* The NoWrapTextLogic class handles the logic of non wrapping text for the editor kit implementing non wrapping text behavior
*
* @author A0111333B
*/
public class NoWrapTextLogic implements ViewFactory{

    /**
     * Returns the view required for the document containing the text to be displayed
     * 
     * @param element     An object representing the document containing the text to be displayed
     * @return            The view required for the document containing the text to be displayed
     */
    @Override
    public View create( Element element ){
        
        if( element == null ){
            return null;
        }
        
        String property = element.getName();
        if( property != null ){
            if( property.equals( AbstractDocument.ParagraphElementName ) ){
                return new NonWrappingParagraphLayout(element);
            } else if( property.equals( AbstractDocument.SectionElementName ) ){
                return new BoxView(element, View.Y_AXIS );
            }
        }
        return new LabelView(element);
    }
}
