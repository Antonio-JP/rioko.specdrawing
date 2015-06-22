package rioko.grapht.linear;

public interface UndirectedGraphCreator {
	public UndirectedGraph create();
	
	public default UndirectedGraph disUnion(UndirectedGraph graph1, UndirectedGraph graph2) {
		return UndirectedGraph.disUnion(graph1,graph2);
	}

	public default UndirectedGraph pointUnion(UndirectedGraph graph1, UndirectedGraph graph2, int v1, int v2) {
		return UndirectedGraph.pointUnion(graph1, graph2, v1, v2);
	}

	public default UndirectedGraph join(UndirectedGraph graph1, UndirectedGraph graph2, int v1, int v2) {
		return UndirectedGraph.join(graph1, graph2, v1, v2);
	}

	public default UndirectedGraph join(UndirectedGraph graph1, UndirectedGraph graph2, int[] v1, int[] v2) {
		return UndirectedGraph.join(graph1, graph2, v1, v2);
	}

	public default UndirectedGraph joinAll(UndirectedGraph graph1, UndirectedGraph graph2, int[] v1, int[] v2) {
		return UndirectedGraph.joinAll(graph1, graph2, v1, v2);
	}

	public default UndirectedGraph complete(int size) {
		return UndirectedGraph.complete(size);
	}

	public default UndirectedGraph cycle(int size) {
		return UndirectedGraph.cycle(size);
	}
}
