package planner;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class WrapTextLogic implements ViewFactory{

    @Override
    public View create( Element element ){
        
        String viewType = element.getName();
        
        if( viewType != null ){
            
            if( viewType.equals( AbstractDocument.ParagraphElementName ) ){
                
                return new ParagraphView(element);
                
            } else if( viewType.equals( AbstractDocument.ContentElementName ) ){
                
                return new WrappingLabelLayout(element);
                
            } else if( viewType.equals( AbstractDocument.SectionElementName ) ){
                
                return new BoxView( element, View.Y_AXIS );
                
            } else if( viewType.equals( StyleConstants.IconElementName ) ){
                
                return new IconView(element);
                
            } else if( viewType.equals( StyleConstants.ComponentElementName ){
                
                return new ComponentView(element);
            }
        }
        
        return new LabelView(element);
    }
}
