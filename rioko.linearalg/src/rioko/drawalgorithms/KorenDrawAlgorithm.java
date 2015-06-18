package rioko.drawalgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import rioko.grapht.Vertex;
import rioko.grapht.linear.SimpleVertex;
import rioko.grapht.linear.UndirectedGraph;
import rioko.linearalg.RDouble;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.matrix.RealSqMatrix;
import rioko.linearalg.vector.RVector;

public class KorenDrawAlgorithm implements DrawingAlgorithm {

	private HashMap<Vertex, Coordinate> coordinates;

	@Override
	public void buildCoordinates(UndirectedGraph graph) {
		coordinates = new HashMap<>();
		RealSqMatrix laplacian = graph.getLaplacianMatrix();
		
		//Calculamos los autovalores
		laplacian.run();
		
		//Construimos nuestras coordenadas
		HashMap<RDouble, ArrayList<RVector<RDouble>>> eigenvectors = laplacian.getEigenvectors();
		
		//Obtenemos el mínimo (no nulo) de los autovalores
		RDouble min = new RDouble(Double.MAX_VALUE);
		RDouble secMin = new RDouble(Double.MAX_VALUE);
		for(RDouble value : eigenvectors.keySet()) {
			if(value.getValue() < min.getValue() && !value.isZero()) {
				min = value;
			} else if(value.getValue() < secMin.getValue() && !value.isZero()) {
				secMin = value;
			}
		}
		
		RVector<RDouble> xCoor = eigenvectors.get(min).get(0), yCoor;
		if(laplacian.getEigenvalues().get(min) > 1) {
			yCoor = eigenvectors.get(min).get(1);
		} else {
			yCoor = eigenvectors.get(secMin).get(0);
		}
		
		int i = 0;
		Iterator<SimpleVertex> iterator = graph.vertexSet().iterator();
		
		try {
			while(iterator.hasNext()) {
				this.coordinates.put(iterator.next(), new Coordinate(xCoor.getValue(i), yCoor.getValue(i)));
				i++;
			}
		} catch (SizeArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
		}
		
		return;
	}

	@Override
	public Coordinate getCoordinate(Vertex v) {
		return this.coordinates.get(v);
	}

}
