package drawingapplet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.layouts.LayoutStyles;

import rioko.grapht.linear.UndirectedGraph;
import rioko.swt.dialogs.InputDialog;
import rioko.zest.layouts.IterativeLayoutAlgorithm;
import rioko.zest.layouts.KorenLayoutAlgorithm;

public class DrawingApplet extends SWTApplet {

	private static final long serialVersionUID = -1873670901952601186L;
	
	private UndirectedGraph graph;

	private GraphViewerComposite viewerKoren;

	private GraphViewerComposite viewerIterative;
	
	private JavaFileGraphCreator fileCreator = new JavaFileGraphCreator();
	
	
	public DrawingApplet() {
		super();
	}

	public DrawingApplet(Display display, Shell shell, JavaFileGraphCreator fileCreator) {
		super(display, shell);
		this.fileCreator = fileCreator;
//		graph = UndirectedGraph.cycle(15);
//		graph = UndirectedGraph.complete(7);
//		graph = UndirectedGraph.pointUnion(UndirectedGraph.cycle(3), UndirectedGraph.cycle(3), 1, 1);
//		graph = UndirectedGraph.joinAll(UndirectedGraph.cycle(4), UndirectedGraph.cycle(1), new int[]{0,1,2,3},new int[]{0,0,0,0});
//		graph = UndirectedGraph.join(UndirectedGraph.joinAll(UndirectedGraph.cycle(4), UndirectedGraph.cycle(1), new int[]{0,1,2,3},new int[]{0,0,0,0}), UndirectedGraph.cycle(3), 1, 1);
//		graph = UndirectedGraph.pointUnion(UndirectedGraph.joinAll(UndirectedGraph.cycle(4), UndirectedGraph.cycle(1), new int[]{0,1,2,3},new int[]{0,0,0,0}), UndirectedGraph.cycle(3), 1, 1);
		graph = UndirectedGraph.joinAll(UndirectedGraph.complete(4), UndirectedGraph.complete(5), new int[]{0,1,2,3},new int[]{0,0,1,2});
	}

	@Override
	protected void createGUI(Shell shell) {
		this.shell = shell;
		
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 3;
		layout.numColumns = 1;
	    shell.setLayout(layout);
	    
	    ////////////TOP PART //////////////////////////
	    Label title = new Label(shell, SWT.CENTER);
	    title.setText("Graph Drawing");
	    title.setFont(new Font(null, "Arial", 16, SWT.BOLD));
	    title.setBackground(new Color(null, 255, 0, 0));
	    title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    
	    //////////// BODY PART //////////////////////////
	    Composite body = new Composite(shell, SWT.NONE);
	    body.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout bodyLayout = new GridLayout();
	    bodyLayout.numColumns = 2;
	    body.setLayout(bodyLayout);
		
		//Create the viewers
	    viewerKoren = new GraphViewerComposite(body, SWT.NONE, this.graph, "Koren algorithm", new KorenLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));

	    viewerIterative = new GraphViewerComposite(body, SWT.NONE, this.graph, "Iterative algorithm", new IterativeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));
		
	    ////////////DOWN PART //////////////////////////
		Composite down = new Composite(shell, SWT.NONE);
		down.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout downLayout = new GridLayout();
		downLayout.numColumns = 2;
		down.setLayout(downLayout);
		
		Button reDraw = new Button(down, SWT.PUSH);
		reDraw.setText("Redraw");
		reDraw.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		reDraw.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}

			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseUp(MouseEvent arg0) {
				updateViewers();
				System.out.println("Fin ejecución del botón");
			}
		});
		
		Button newGraph = new Button(down, SWT.PUSH);
		newGraph.setText("Change Graph");
		newGraph.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		newGraph.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}

			@Override
			public void mouseDown(MouseEvent arg0) {}

			@Override
			public void mouseUp(MouseEvent arg0) {
				InputDialog dialog = new InputDialog(getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, "Generate Graph", "Add the code to generate a new Graph");
				
				String code = dialog.open();
				
				try {
					graph = fileCreator.generateNextFile(code).create();
					updateViewers();
				} catch(NullPointerException e) {
					System.out.println("No graph created");
				}
				
				System.out.println("End button proccessing");
			}
		});
	}

	private void updateViewers() {
		this.viewerKoren.changeGraph(this.graph, new KorenLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));
		this.viewerIterative.changeGraph(this.graph, new IterativeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));
		
		this.viewerKoren.applyLayout();
		this.viewerIterative.applyLayout();
	}

	@Override
	protected void destroyGUI() { }
	
	//Runnable method
	public static void main(String[] args) {		
		JavaFileGraphCreator fileCreator = new JavaFileGraphCreator(args);
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setText("Graph Drawing");
		
		DrawingApplet applet = new DrawingApplet(display, shell, fileCreator);
		applet.createGUI(shell);
		
		shell.pack();
		shell.setSize(1000, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		applet.destroyGUI();
		display.dispose();
	}	
}
