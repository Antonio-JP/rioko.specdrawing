package drawingapplet;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import rioko.grapht.linear.SimpleVertex;

public class IDFigure extends Figure {
	
	public IDFigure(SimpleVertex vertex) {
		super();
		
		this.add(new Label(""+vertex.getId()));
		
		this.setBackgroundColor(new Color(null, 170,160,130));
		this.setSize(-1, -1);
	}
}
