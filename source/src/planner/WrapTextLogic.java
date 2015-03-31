package planner;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class WrapTextLogic implements ViewFactory{

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
