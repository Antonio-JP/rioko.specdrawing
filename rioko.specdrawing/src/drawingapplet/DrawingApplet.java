package drawingapplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

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
import rioko.grapht.linear.UndirectedGraphCreator;
import rioko.swt.dialogs.InputDialog;
import rioko.zest.layouts.IterativeLayoutAlgorithm;
import rioko.zest.layouts.KorenLayoutAlgorithm;

public class DrawingApplet extends SWTApplet {

	private static final long serialVersionUID = -1873670901952601186L;
	
	private UndirectedGraph graph;

	private GraphViewerComposite viewerKoren;

	private GraphViewerComposite viewerIterative;
	
	private int auxNum = 0;
	
	
	public DrawingApplet() {
		super();
	}

	public DrawingApplet(Display display, Shell shell) {
		super(display, shell);
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
	    
	    //Create the layout algorithms
		
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
				swapSystemToJDK();
				String className = "AuxiliaryGraphCreator_"+auxNum;
				URI uriForFile = URI.create(pathToCode + className.replace('.','/') + Kind.SOURCE.extension);
				File[] code =  getStringCode(className, uriForFile);
				
				if(code != null) {
					JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
					
					Writer out = null;
				    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
					DiagnosticListener<? super JavaFileObject> diagnosticListener = null;
					Iterable<String> options = null;
					Iterable<String> classes = null;
					Iterable<? extends JavaFileObject> compilationUnits =
					           fileManager.getJavaFileObjectsFromFiles(Arrays.asList(code));
					try {
						if(!compiler.getTask(out, fileManager, diagnosticListener, options, classes, compilationUnits).call()) {
							throw new RuntimeException("Error while compiling the class " + className);
						}
						auxNum++;
						
						UndirectedGraphCreator creator;
					
						URLClassLoader loader = new URLClassLoader(new URL[]{new URL(pathToCode)});
						loader.loadClass(className);
						creator = (UndirectedGraphCreator) loader.loadClass(className).newInstance();
						loader.close();
						
						graph = creator.create();
						updateViewers();
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | RuntimeException | IOException e) {
						// Curious Exception
						e.printStackTrace();
					}
				}
				System.out.println("Fin ejecución del botón");
				swapSystemToJDK();
			}
		});
	}

	protected File[] getStringCode(String className, URI dest) {
		
		InputDialog dialog = new InputDialog(this.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, "Generate Graph", "Add the code to generate a new Graph");
		
		String codeFromInput = dialog.open();
		
		if(codeFromInput != null && !codeFromInput.isEmpty()) {
			String finalCode = "import rioko.grapht.linear.UndirectedGraphCreator;\n" + 
					"import rioko.grapht.linear.UndirectedGraph;\n" +
					"public class " + className + " implements UndirectedGraphCreator {\n\n" +
						"\tpublic UndirectedGraph create() {\n" + 
							"\t\treturn " + codeFromInput + ";\n" +
						"\t}\n" + 
					"}\n";
			
			File newFile = new File(dest);
			PrintStream stream;
			try {
				stream = new PrintStream(newFile);
			
			stream.print(finalCode);
			
			stream.close();
		
			return new File[]{newFile};
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} 
		
		return new File[]{};
	}

	private void updateViewers() {
		this.viewerKoren.changeGraph(this.graph, new KorenLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));
		this.viewerIterative.changeGraph(this.graph, new IterativeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING, graph));
		
		this.viewerKoren.applyLayout();
		this.viewerIterative.applyLayout();
	}

	@Override
	protected void destroyGUI() {		
	}
	
	//Runnable method
	private static String pathToCode = "file:///Users/Antonio/Desktop/aux_files/";
	private static String pathToJDK = "C:\\Program Files\\Java\\jdk1.8.0_45\\jre";
	public static void main(String[] args) {
//		System.out.println("Size of args: " + args.length);
//		for(int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}
		
		if(args.length > 0) {
			pathToCode = "file:///"+args[0].replace('\\', '/');
		}
		if(args.length > 1) {
			pathToJDK = args[1];
		}
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setText("Graph Drawing");
		
		DrawingApplet applet = new DrawingApplet(display, shell);
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
	
	private static String auxString = null;
	private static void swapSystemToJDK() {
		if(auxString == null) {
			auxString = new String(pathToJDK);
		}
		
		String aux = System.getProperty("java.home");
		System.out.println("Changing from \"" + aux + "\" to \"" + auxString + "\"");
		
		System.setProperty("java.home", auxString);
		auxString = aux;
	}
}
