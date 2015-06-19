package rioko.zest.layouts;

import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.zest.layouts.geometry.Point;

public abstract class ExpandedCenterLayoutAlgorithm extends CenterLayoutAlgorithm {

	public ExpandedCenterLayoutAlgorithm(int styles) {
		super(styles);
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
		super.postLayoutAlgorithm(entitiesToLayout, relationshipsToConsider);
		
		//Calculamos el factor de homotopía necesario para encajar mejor el dibujo
		double factor = Double.MAX_VALUE;
		System.out.println("Posiciones en Expanded");
		for(InternalNode node : entitiesToLayout) {
			Point position = this.getPosition(node);
			System.out.println(position.toString());
			
			double hCut = Math.abs(this.bounds.getHeight()/(2*position.getY()));
			double vCut = Math.abs(this.bounds.getWidth()/(2*position.getX()));
			
			factor = Math.min(factor, Math.min(hCut, vCut));
		}
		
		//Ajustamos para que no esté demasiado pegado
		factor*= 1;
		
		System.out.println("Factor: " + factor);
		//Movemos todos los nodos lo que indique el factor
		for(InternalNode node : entitiesToLayout) {
			Point relFinPos = this.getPosition(node).scalar(factor);
			System.out.println(relFinPos.toString());
			
			Point finalPos = this.getAbsolute(relFinPos);
			this.setLocation(node, finalPos.getX(), finalPos.getY());
		}
	}
}
