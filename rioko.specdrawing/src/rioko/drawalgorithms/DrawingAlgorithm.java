package rioko.drawalgorithms;

import rioko.grapht.Vertex;
import rioko.grapht.linear.UndirectedGraph;

public interface DrawingAlgorithm {
	public void buildCoordinates(UndirectedGraph graph);
	public Coordinate getCoordinate(Vertex v);
}
