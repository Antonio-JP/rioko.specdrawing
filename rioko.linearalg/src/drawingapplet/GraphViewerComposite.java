package drawingapplet;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import rioko.grapht.linear.SimpleVertex;
import rioko.grapht.linear.UndirectedGraph;
import rioko.zest.layouts.DrawingLayoutAlgorithm;

public class GraphViewerComposite extends Composite {
	
	Graph viewer;
	//GraphViewer viewer;
	Composite graphPart;
	int pageNum = 0;
	StackLayout layout = new StackLayout();

	public GraphViewerComposite(Composite parent, int style, UndirectedGraph graph, String title, DrawingLayoutAlgorithm algorithm) {
		super(parent, style);
		
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 3;
		this.setLayout(layout);
		
		Label label = new Label(this, SWT.CENTER);
		label.setText(title);
		label.setFont(new Font(null, "Arial", 12, SWT.BOLD));
		label.setBackground(new Color(null, 255,255,0));
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		graphPart = new Composite(this, SWT.BORDER);
		graphPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		graphPart.setLayout(this.layout);
		
		//Creamos el visualizador del grafo
		viewer = this.getGraph(graphPart, graph);
		viewer.setLayoutAlgorithm(algorithm, true);		
				
		viewer.applyLayout();
	}

	private Graph getGraph(Composite graphPart, UndirectedGraph graph) {
		this.pageNum++;
		Composite part = new Composite(graphPart, SWT.NONE);
		part.setLayout(new FillLayout());
		
		Graph viewer = new Graph(part, SWT.NONE);
		ArrayList<GraphNode> nodes = new ArrayList<>();
		
		for(SimpleVertex vertex : graph.vertexSet()) {
			GraphNode node = new GraphNode(viewer, SWT.NONE, ""+vertex.getId());
			nodes.add(node);
		}
		
		int i = 0;
		Iterator<SimpleVertex> first = graph.vertexSet().iterator();
		
		while(first.hasNext()) {
			SimpleVertex src = first.next();
			
			Iterator<SimpleVertex> second = graph.vertexSet().iterator();
			int j = 0;
			
			while(second.hasNext() && j < i) {
				SimpleVertex trg = second.next();
					if(graph.getEdge(src, trg) != null) {
						new GraphConnection(viewer, SWT.NONE, nodes.get(i), nodes.get(j));
					}
				j++;
			}
			
			i++;
		}
		
		viewer.setLocation(new Point(0,0));
		
		layout.topControl = part;
		graphPart.layout();
		
		return viewer;
	}

	public void applyLayout() {
		viewer.applyLayout();
	}

	public void changeGraph(UndirectedGraph graph, DrawingLayoutAlgorithm algorithm) {
		viewer.dispose();
		
		viewer = this.getGraph(graphPart, graph);
		viewer.setLayoutAlgorithm(algorithm, true);		
				
		viewer.applyLayout();
	}

}
