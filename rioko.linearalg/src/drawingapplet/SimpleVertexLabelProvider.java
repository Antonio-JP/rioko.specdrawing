package drawingapplet;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;

import rioko.grapht.VisibleEdge;
import rioko.grapht.linear.SimpleVertex;

public class SimpleVertexLabelProvider extends LabelProvider implements IFigureProvider {
	
	
	//Funcion de texto
	@Override
	public String getText(Object element) {
	    if (element instanceof SimpleVertex) {
	    	SimpleVertex myNode = (SimpleVertex) element;
	    	return myNode.getId().toString();
	    }

		if (element instanceof VisibleEdge) {
	      return "";
	    }
	    
	    throw new RuntimeException("Wrong type: "+ element.getClass().toString());
	  }

	//Funcion de imágenes de nodos
	@Override
	public IFigure getFigure(Object element) {
		IFigure result = null;
		
		if (element instanceof SimpleVertex) {
			result = this.getIFigure((SimpleVertex) element);
		} else if (element instanceof VisibleEdge) {
			return null;
		} else {
			throw new RuntimeException("Wrong type: "+ element.getClass().toString());
		}
		
		return result;
	}

	private IFigure getIFigure(SimpleVertex element) {
		return new IDFigure(element);
	}
}
