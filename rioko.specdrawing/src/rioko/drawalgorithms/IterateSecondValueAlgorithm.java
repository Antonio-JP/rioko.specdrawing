package rioko.drawalgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import rioko.grapht.Vertex;
import rioko.grapht.linear.SimpleVertex;
import rioko.grapht.linear.UndirectedGraph;
import rioko.linearalg.RDouble;
import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.linearalg.matrix.RealSqMatrix;
import rioko.linearalg.vector.RVector;
import rioko.linearalg.vector.VectorList;
import rioko.utilities.ListSet;

public class IterateSecondValueAlgorithm implements DrawingAlgorithm {
	
	private HashMap<Vertex, Coordinate> coordinates;

	@Override
	public void buildCoordinates(UndirectedGraph graph) {		
		this.coordinates = new HashMap<>();
		
		RVector<RDouble> xCoor = this.getFirstEigenvector(graph);
		RVector<RDouble> yCoor = this.getSecondCoordinate(graph, xCoor);
		
		int i = 0;
		Iterator<SimpleVertex> iterator = graph.vertexSet().iterator();
		
		try {
			while(iterator.hasNext()) {
				RDouble normalizer = (new RDouble(Math.sqrt(2))).inverse();
				this.coordinates.put(iterator.next(), new Coordinate(xCoor.getValue(i), yCoor.getValue(i).prod(normalizer)));
				i++;
			}
		} catch (SizeArgumentException | BadClassArgumentException | ZeroNumberException e) {
			// Impossible Exception
			e.printStackTrace();
		}
	}

	@Override
	public Coordinate getCoordinate(Vertex v) {
		return this.coordinates.get(v);
	}
	
	//Private methods
	private RVector<RDouble> getFirstEigenvector(UndirectedGraph graph) {
		RealSqMatrix laplacian = graph.getLaplacianMatrix();
		
		//Calculamos los autovalores
		laplacian.run();
		
		//Construimos nuestras coordenadas
		HashMap<RDouble, ArrayList<RVector<RDouble>>> eigenvectors = laplacian.getEigenvectors();
		
		//Obtenemos el mínimo (no nulo) de los autovalores
		RDouble min = this.getMinimalEigenValues(eigenvectors.keySet());
		
		if(min == null) {
			return new VectorList<RDouble>(RDouble.zero(), graph.vertexSet().size());
		}
		
		return eigenvectors.get(min).get(0);
	}
	
	private RDouble getMinimalEigenValues(Set<RDouble> set) {
		RDouble min = new RDouble(Double.MAX_VALUE);
		
		for(RDouble value : set) {
			if(value.getValue() < min.getValue() && !value.isZero()) {
				min = value;
			}
		}
		
		if(min.equals(new RDouble(Double.MAX_VALUE))) {
			return null;
		}
		
		return min;
	}
	
	private RVector<RDouble> getSecondCoordinate(UndirectedGraph graph, RVector<RDouble> xCoor) {
		ListSet<SimpleVertex> positiveVertex = new ListSet<>();
		ListSet<SimpleVertex> negativeVertex = new ListSet<>();
		
		Iterator<SimpleVertex> iterator = graph.vertexSet().iterator();
		int i = 0;
		while(iterator.hasNext()) {
			try {
				if(xCoor.getValue(i).getValue() >= 0) {
					positiveVertex.add(iterator.next());
				} else {
					negativeVertex.add(iterator.next());
				}
			} catch (SizeArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
			}
			
			i++;
		}
		
		UndirectedGraph positive = (UndirectedGraph) graph.inducedSubgraph(positiveVertex);
		UndirectedGraph negative = (UndirectedGraph) graph.inducedSubgraph(negativeVertex);
		
		RVector<RDouble> posCoord = this.getFirstEigenvector(positive);
		RVector<RDouble> negCoord = this.getFirstEigenvector(negative);
		
		//Creamos el nuevo vector de coordenadas para el grafo global
		RVector<RDouble> yCoor = new VectorList<RDouble>(RDouble.zero(), posCoord.size()+negCoord.size());
		iterator = graph.vertexSet().iterator();
		i = 0;
		
		int posIndex = 0, negIndex = 0;
		while(iterator.hasNext()) {
			try {
				iterator.next();
				
				if(xCoor.getValue(i).getValue() >= 0) {
					yCoor.setValue(posCoord.getValue(posIndex), i);
					posIndex++;
				} else {
					yCoor.setValue(negCoord.getValue(negIndex), i);
					negIndex++;
				}
			
				i++;
			} catch (SizeArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
			}
		}
		
		return yCoor;
	}

}
