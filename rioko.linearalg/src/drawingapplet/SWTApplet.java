package drawingapplet;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JApplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class SWTApplet extends JApplet {

	private static final long serialVersionUID = 2130286504305508325L;

	static {
		System.setProperty("sun.awt.xembedserver", "true");
	}
	
	protected Display display;
	protected Shell shell;
	private Canvas canvas;
	private Control focusControl;
	
	public SWTApplet() {
		super();
	}
	
	public SWTApplet(Display display, Shell shell) {
		this.display = display;
		this.shell = shell;
	}
	
	protected Display getDisplay() {
		return display;
	}
	
	abstract protected void createGUI(Shell shell);
	
	abstract protected void destroyGUI();
	
	private void buildSWTBase() {
		display = new Display();
		
		// activar soporte para TAB y SHIF-TAB
		display.addFilter(SWT.FocusIn, new Listener() {
			@Override
			public void handleEvent(Event event) {
				focusControl = (Control)event.widget;
			}
		});
		// -----------------------------------
		
		shell = SWT_AWT.new_Shell(display, canvas);
		System.out.println("Antes de crear la GUI");
		createGUI(shell);
		shell.pack();
		shell.setSize(1000, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		destroyGUI();
		display.dispose();
	}
	
	@Override
	public void start() {
		this.setVisible(true);
		// agregar contenedor Swing
		canvas = new Canvas();
		canvas.setFocusable(true);
		this.setLayout(new BorderLayout());
		this.add(canvas, BorderLayout.CENTER);
		this.setSize(1000, 500);
		// ------------------------
		
		// crear interface SWT
		new Thread() {
			@Override
			public void run() {
				buildSWTBase();
			};
		}.start();
		// -------------------
		
		// activar soporte para TAB y SHIF-TAB
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				final KeyEvent _e = e;
				display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (_e.getKeyCode() == KeyEvent.VK_TAB
							&& _e.getModifiers() == 0)
						focusControl.traverse(SWT.TRAVERSE_TAB_NEXT);
					else if (_e.getKeyCode() == KeyEvent.VK_TAB
							&& _e.getModifiers() == KeyEvent.SHIFT_MASK)
						focusControl.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
					else if (_e.getKeyCode() == KeyEvent.VK_ENTER
							&& _e.getModifiers() == 0)
						if (shell.getDefaultButton() != null &&
								shell.getDefaultButton().isVisible())
							shell.getDefaultButton().notifyListeners(
								SWT.Selection, null);
				}
				});
			}
		});
		// -----------------------------------
		this.canvas.setVisible(true);
	}
}
