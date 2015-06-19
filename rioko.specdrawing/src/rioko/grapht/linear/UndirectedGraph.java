package rioko.grapht.linear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import rioko.grapht.AbstractGraph;
import rioko.grapht.VisibleEdge;
import rioko.linearalg.RDouble;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.matrix.RealSqMatrix;

public class UndirectedGraph extends AbstractGraph<SimpleVertex, VisibleEdge<SimpleVertex>> {
	
	boolean hasChanged = true;
	RealSqMatrix adjacencyMatrix, degreeMatrix, laplacianMatrix;

	public UndirectedGraph() {
		super(VisibleEdge.class, SimpleVertex.class);
	}
	
	public UndirectedGraph(Class<?> edgeClass, Class<?> vertexClass) {
		this();
	}

	//AbstractGraph methods
	@Override
	public boolean addVertex(SimpleVertex arg0) {
		if(super.addVertex(arg0)) {
			this.hasChanged = true;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public VisibleEdge<SimpleVertex> addEdge(SimpleVertex arg0, SimpleVertex arg1) {
		if(this.getEdge(arg0, arg1) == null) {
			this.hasChanged = true;
		}
		
		super.addEdge(arg1, arg0);
		return super.addEdge(arg0, arg1);
	}
	
	@Override
	public boolean removeEdge(VisibleEdge<SimpleVertex> arg0) {
		if(super.removeEdge(arg0)) {
			this.hasChanged = true;
			return true;
		}
		
		return false;
	}

	@Override
	public VisibleEdge<SimpleVertex> removeEdge(SimpleVertex arg0, SimpleVertex arg1) {
		VisibleEdge<SimpleVertex> edge = super.removeEdge(arg0, arg1);
		
		if(edge != null) {
			this.hasChanged = true;
		}
		
		return edge;
	}

	@Override
	public boolean removeVertex(SimpleVertex arg0) {
		if(this.containsVertex(arg0)) {
			for(VisibleEdge<SimpleVertex> edge : this.edgesTo(arg0)) {
				if(!this.removeEdge(edge)) {
					System.out.println("ERRRRRORRRR");
				}
			}
			super.removeVertex(arg0);
			this.hasChanged = true;
				
			return true;
		}
		
		return false;
	}
	
	//Other methods
	public double degree(SimpleVertex vertex) {
		if(!this.containsVertex(vertex)) {
			return -1;
		}
		
		double res = 0;
		for(VisibleEdge<SimpleVertex> edge : this.edgesFrom(vertex)) {
			res += this.getEdgeWeight(edge);
		}
		
		return res;
	}
	
	public RealSqMatrix getAdjacencyMatrix() {
		if(hasChanged) {
			this.buildMatrices();
		}
		
		return this.adjacencyMatrix;
	}
	
	public RealSqMatrix getDegreeMatrix() {
		if(hasChanged) {
			this.buildMatrices();
		}
		
		return this.degreeMatrix;
	}
	
	public RealSqMatrix getLaplacianMatrix() {
		if(hasChanged) {
			this.buildMatrices();
		}
		
		return this.laplacianMatrix;
	}
	
	private void buildMatrices() {
		if(this.hasChanged) {
			try {
				this.adjacencyMatrix = new RealSqMatrix(this.vertexSet().size());
				this.degreeMatrix = new RealSqMatrix(this.vertexSet().size());
				this.laplacianMatrix = new RealSqMatrix(this.vertexSet().size());
			} catch (SizeArgumentException e1) {
				// Empty matrices
				e1.printStackTrace();
			}
			
			Iterator<SimpleVertex> firstIt = this.vertexSet().iterator();
			int i = 0;
			try {
				while(firstIt.hasNext()) {				
					SimpleVertex src = firstIt.next();
	
					double degree = this.degree(src);
					this.degreeMatrix.setValue(new RDouble(degree), i, i);
					this.laplacianMatrix.setValue(new RDouble(degree), i, i);
					
					Iterator<SimpleVertex> secIt = this.vertexSet().iterator();
					int j = 0;
					while(secIt.hasNext()) {
						SimpleVertex trg = secIt.next();
						
						VisibleEdge<SimpleVertex> edge = this.getEdge(src, trg);
						if(edge != null) {
							
								RDouble value = new RDouble(this.getEdgeWeight(edge));
								this.adjacencyMatrix.setValue(value, i, j);
								this.laplacianMatrix.setValue(value.opposite(), i, j);
						}
						j++;
					}
					i++;
				}
			} catch (SizeArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
			}
			
			this.hasChanged = false;
		}
	}

	//Static methods to build Undirected Graphs
	public static UndirectedGraph copy(UndirectedGraph graph) {
		UndirectedGraph res = new UndirectedGraph();
		HashMap<Integer, Integer> changes = new HashMap<>();
		
		int i = 0;
		for(SimpleVertex vertex : graph.vertexSet()) {
			res.addVertex(new SimpleVertex(i));
			changes.put(vertex.getId(), i);
			i++;
		}
		
		for(VisibleEdge<SimpleVertex> edge: graph.edgeSet()) {
			res.addEdge(new SimpleVertex(changes.get(edge.getSource().getId())), new SimpleVertex(changes.get(edge.getTarget().getId())));
		}
		
		return res;
	}
	
	public static UndirectedGraph union(UndirectedGraph graph1, UndirectedGraph graph2) {
		UndirectedGraph graph = new UndirectedGraph();
		
		for(SimpleVertex vertex : graph1.vertexSet()) {
			graph.addVertex(vertex.copy());
		}
		
		for(SimpleVertex vertex : graph2.vertexSet()) {
			graph.addVertex(vertex.copy());
		}

		for(VisibleEdge<SimpleVertex> edge: graph1.edgeSet()) {
			graph.addEdge(edge.getSource(), edge.getTarget());
		}
		
		for(VisibleEdge<SimpleVertex> edge: graph2.edgeSet()) {
			graph.addEdge(edge.getSource(), edge.getTarget());
		}
		
		
		return graph;
	}
	
	public static UndirectedGraph traslateId(UndirectedGraph graph, int offset) {
		UndirectedGraph res = new UndirectedGraph();
		
		for(SimpleVertex vertex : graph.vertexSet()) {
			SimpleVertex copy = new SimpleVertex(vertex.getId()+offset);
			res.addVertex(copy);
		}
		
		for(VisibleEdge<SimpleVertex> edge: graph.edgeSet()) {
			res.addEdge(new SimpleVertex(edge.getSource().getId() + offset), new SimpleVertex(edge.getTarget().getId() + offset));
		}
		
		return res;
	}
	
	public static UndirectedGraph disUnion(UndirectedGraph graph1, UndirectedGraph graph2) {
		return UndirectedGraph.union(graph1, UndirectedGraph.traslateId(graph2, graph1.vertexSet().size()));
	}
	
	public static UndirectedGraph glue(UndirectedGraph graph, int[] v) {		
		UndirectedGraph res = UndirectedGraph.copy(graph);
		
		if(v.length > 0) {
			SimpleVertex src = new SimpleVertex(v[0]);
			for(int node : v) {
				for(SimpleVertex ver : res.vertexFrom(new SimpleVertex(node))) {
					res.addEdge(src, ver);
				}
			}
			
			for(int i = 1; i < v.length; i++) {
				res.removeVertex(new SimpleVertex(v[i]));
			}
		}
		
		return UndirectedGraph.copy(res);
	}
	
	public static UndirectedGraph pointUnion(UndirectedGraph graph1, UndirectedGraph graph2, int v1, int v2) {
		return UndirectedGraph.glue(UndirectedGraph.disUnion(graph1, graph2), new int[]{v1, v2+graph1.vertexSet().size()});
	}
	
	public static UndirectedGraph join(UndirectedGraph graph1, UndirectedGraph graph2, int v1, int v2) {
		UndirectedGraph graph = UndirectedGraph.disUnion(graph1, graph2);
		
		graph.addEdge(new SimpleVertex(v1), new SimpleVertex(v2 + graph1.vertexSet().size()));
		
		return graph;
	}
	
	public static UndirectedGraph join(UndirectedGraph graph1, UndirectedGraph graph2, int[] v1, int[] v2) {
		if(v1.length != v2.length) {
			return null;
		}
		
		UndirectedGraph graph = UndirectedGraph.disUnion(graph1, graph2);
		
		for(int i = 0; i < v1.length; i++) {
			graph.addEdge(new SimpleVertex(v1[i]), new SimpleVertex(v2[i] + graph1.vertexSet().size()));
		}
		
		return graph;
	}
	
	public static UndirectedGraph joinAll(UndirectedGraph graph1, UndirectedGraph graph2, int[] v1, int[] v2) {
		UndirectedGraph graph = UndirectedGraph.disUnion(graph1, graph2);
		
		for(int i = 0; i < v1.length; i++) {
			for(int j = 0; j < v2.length; j++) {
				graph.addEdge(new SimpleVertex(v1[i]), new SimpleVertex(v2[j] + graph1.vertexSet().size()));
			}
		}
		
		return graph;
	}
	
	public static UndirectedGraph complete(int size) {
		UndirectedGraph graph = new UndirectedGraph();
		
		ArrayList<SimpleVertex> vertices = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			SimpleVertex vertex = new SimpleVertex(i);
			
			vertices.add(vertex);
			graph.addVertex(vertex);
		}
		
		for(int i = 1; i < size; i++) {
			for(int j = 0; j < i; j++) {
				graph.addEdge(vertices.get(i), vertices.get(j));
			}
		}
		
		return graph;
	}
	
	public static UndirectedGraph cycle(int size) {
		UndirectedGraph graph = new UndirectedGraph();
		
		ArrayList<SimpleVertex> vertices = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			SimpleVertex vertex = new SimpleVertex(i);
			
			vertices.add(vertex);
			graph.addVertex(vertex);
		}
		
		for(int i = 0; i < size -1; i++) {
			graph.addEdge(vertices.get(i), vertices.get(i+1));
		}
		
		graph.addEdge(vertices.get(size-1), vertices.get(0));
		
		return graph;
	}
}
