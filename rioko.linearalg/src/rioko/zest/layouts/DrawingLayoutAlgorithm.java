package rioko.zest.layouts;

import java.util.Iterator;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.drawalgorithms.Coordinate;
import rioko.drawalgorithms.DrawingAlgorithm;
import rioko.grapht.linear.SimpleVertex;
import rioko.grapht.linear.UndirectedGraph;
import rioko.zest.layouts.geometry.DoubleRectangle;
import rioko.zest.layouts.geometry.Point;

public abstract class DrawingLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	protected DoubleRectangle bounds = null;
		
	DrawingAlgorithm algorithm;
	UndirectedGraph graph; 
	
	//Builders

	public DrawingLayoutAlgorithm(int styles, UndirectedGraph graph) {
		super(styles);

		this.layoutStopped = false;
		
		this.graph = graph;
	}
	
	//AbstractLayoutAlgorithm Methods
	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
		//Guardamos el tamaño de la zona de pintado en un Rectángulo
				this.bounds = new DoubleRectangle(x,y,width,height);
	}
	
	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX,
			double boundsY,	double boundsWidth, double boundsHeight) {
		
		this.algorithm.buildCoordinates(graph);
		
		//Calculamos el factor de expansión
		double factor = Double.MAX_VALUE;
		Iterator<SimpleVertex> iterator = this.graph.vertexSet().iterator();
		while(iterator.hasNext()) {
			Coordinate coord = this.algorithm.getCoordinate(iterator.next());
			Point position = new Point(coord.getFirst().getValue(), coord.getLast().getValue());	//Posicion relativa del nodo tras centrar
			
			double hCut = Math.abs(this.bounds.getHeight()/(2*position.getY()));
			double vCut = Math.abs(this.bounds.getWidth()/(2*position.getX()));
			
			factor = Math.min(factor, Math.min(hCut, vCut));
		}
		
		factor*=0.9;
		
		//Colocamos los vértices		
		int i = 0;
		iterator = this.graph.vertexSet().iterator();
		while(iterator.hasNext()) {
			InternalNode node = entitiesToLayout[i];
			
			Coordinate coord = algorithm.getCoordinate(iterator.next());
			
			
			Point position = this.getAbsolute(new Point(coord.getFirst().getValue()*factor, coord.getLast().getValue()*factor));
			this.setLocation(node, position.getX(), position.getY());
			
			i++;
		}

		this.layoutStopped = true;
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		// TODO Auto-generated method stub.
		if(this.internalAsynchronous == asynchronous && this.internalContinuous == continuous) {
			return true;
		}
		
		return false;
	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		// TODO Auto-generated method stub
	}
	
	//My methods
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
	
	protected void setLocation(InternalNode node, double x, double y) {
		node.setLocation(x,y);
		node.setLocationInLayout(x,y);
	}
}
