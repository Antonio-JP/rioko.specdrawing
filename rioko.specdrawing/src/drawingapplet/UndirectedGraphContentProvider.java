package drawingapplet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import rioko.grapht.VisibleEdge;
import rioko.grapht.linear.SimpleVertex;
import rioko.grapht.linear.UndirectedGraph;

public class UndirectedGraphContentProvider implements IGraphContentProvider  {
	
	UndirectedGraph graph;
	
	public UndirectedGraphContentProvider(UndirectedGraph graph) {
		this.graph = graph;
	}

	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(newInput != null && !(newInput.equals(oldInput))) {
			if(newInput instanceof UndirectedGraph) {
				this.graph = (UndirectedGraph)newInput;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDestination(Object rel) {
		if(rel instanceof VisibleEdge) {
			return ((VisibleEdge<SimpleVertex>) rel).getTarget();
		}
		
		return null;
	}

	@Override
	public Object[] getElements(Object input) {
		return this.graph.edgeSet().toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getSource(Object rel) {
		if(rel instanceof VisibleEdge) {
			return ((VisibleEdge<SimpleVertex>) rel).getSource();
		}
		
		return null;
	}
}
