package drawingapplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

public class TestApplet extends SWTApplet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void createGUI(Shell shell) {
		shell.setText("GraphSnippet1");
 		shell.setLayout(new FillLayout());
 		shell.setSize(400, 400);
 		
 		Composite composite = new Composite(shell, SWT.NONE);
 		composite.setLayout(new FillLayout());
 
 		Graph g = new Graph(composite, SWT.NONE);
 		GraphNode n = new GraphNode(g, SWT.NONE, ""+0);
 		GraphNode n2 = new GraphNode(g, SWT.NONE, ""+1);
 		GraphNode n3 = new GraphNode(g, SWT.NONE, ""+2);
 		new GraphConnection(g, SWT.NONE, n, n2);
 		new GraphConnection(g, SWT.NONE, n2, n3);
 		new GraphConnection(g, SWT.NONE, n3, n);
 		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
 		
 		composite.setVisible(true);
	}

	@Override
	protected void destroyGUI() {}

}
