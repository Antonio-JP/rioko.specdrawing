package rioko.zest.layouts;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.zest.layouts.geometry.DoubleRectangle;
import rioko.zest.layouts.geometry.Point;

public abstract class CenterLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	protected DoubleRectangle bounds = null;

	public CenterLayoutAlgorithm(int styles) {
		super(styles);
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
		// Centramos el contenido (El dibujo queda dibujado en el interior de un rectángulo contrado en el centro del hueco para el Layout

		//Calculamos el centro del dibujo
		double xMean = 0, yMean = 0;
		for(InternalNode node : entitiesToLayout) {
			Point point = this.getPosition(node);
			xMean += point.getX();
			yMean += point.getY();
		}
		
		Point offset = new Point(xMean/entitiesToLayout.length, yMean/entitiesToLayout.length);	//Posicion relativa del centro del dibujo

		//Calculamos el factor de expansión
		double factor = Double.MAX_VALUE;
		for(InternalNode node : entitiesToLayout) {
			Point position = this.getPosition(node).add(offset.opposite());	//Posicion relativa del nodo tras centrar
			
			double hCut = Math.abs(this.bounds.getHeight()/(2*position.getY()));
			double vCut = Math.abs(this.bounds.getWidth()/(2*position.getX()));
			
			factor = Math.min(factor, Math.min(hCut, vCut));
		}
		
		factor*=0.9;
		
		for(InternalNode node : entitiesToLayout) {
			Point position = this.getAbsolute(this.getPosition(node).add(offset.opposite()).scalar(factor));
			this.setLocation(node, position.getX(), position.getY());
		}
	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
		//Guardamos el tamaño de la zona de pintado en un Rectángulo
				this.bounds = new DoubleRectangle(x,y,width,height);
	}
	
	//Methods to get the limits of a node
	protected double getRightNode(InternalNode node) {
		return node.getXInLayout() + node.getWidthInLayout();
	}
	
	protected double getLeftNode(InternalNode node) {
		return node.getXInLayout();
	}
	
	protected double getUpNode(InternalNode node) {
		return node.getYInLayout();
	}
	
	protected double getDownNode(InternalNode node) {
		return node.getYInLayout() + node.getHeightInLayout();
	}
	
	protected void setLocation(InternalNode node, double x, double y) {
		node.setLocation(x,y);
		node.setLocationInLayout(x,y);
	}
	
	//Other methods
	protected Point getCenter() {
		return new Point((this.bounds.getX() + this.bounds.getWidth())/2, (this.bounds.getY() + this.bounds.getHeight())/2);
	}
	
	protected Point getPosition(InternalNode node) {
		return this.getRelative(new Point(node.getCurrentX(), node.getCurrentY()));
	}

	protected Point getAbsolute(Point relativePoint) {
		return relativePoint.add(this.getCenter());
	}
	
	protected Point getRelative(Point absolutePoint) {
		return absolutePoint.add(this.getCenter().opposite());
	}
}
