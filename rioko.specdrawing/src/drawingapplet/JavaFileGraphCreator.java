package drawingapplet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject.Kind;

import rioko.grapht.linear.UndirectedGraphCreator;

public class JavaFileGraphCreator {
	private String nameOfClass = "AuxiliaryGraphCreator";
	
	private int numOfClasses = 0;
	
	private String pathToCode = getURLToCodeFolder();
	private String pathToJDK = getPathToLatestJDK();
	
	private String nextJavaHome = pathToJDK;
	
	public JavaFileGraphCreator() {	}

	public JavaFileGraphCreator(String[] args) { }
	
	public void swapSystemToJDK() {
		String aux = System.getProperty("java.home");
		System.out.println("Changing from \"" + aux + "\" to \"" + this.nextJavaHome + "\"");
		
		System.setProperty("java.home", this.nextJavaHome);
		this.nextJavaHome = aux;
	}
	
	public UndirectedGraphCreator generateNextFile(String code) {
		UndirectedGraphCreator res = null;
		swapSystemToJDK();
		String className = this.nameOfClass+"_"+this.numOfClasses;
		URI uriForFile = URI.create(this.pathToCode + className.replace('.','/') + Kind.SOURCE.extension);
		File[] files =  getStringCode(className, code, uriForFile);
		
		if(files != null && files.length > 0) {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			try {
				if(compiler == null) {
					throw new RuntimeException("No compiler found: probably no JDK installed");
				}
				
				Writer out = null;
			    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
				DiagnosticListener<? super JavaFileObject> diagnosticListener = null;
				Iterable<String> options = null;
				Iterable<String> classes = null;
				Iterable<? extends JavaFileObject> compilationUnits =
				           fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
			
				if(!compiler.getTask(out, fileManager, diagnosticListener, options, classes, compilationUnits).call()) {
					throw new RuntimeException("Error while compiling the class " + className);
				}
				this.numOfClasses++;
			
				URLClassLoader loader = new URLClassLoader(new URL[]{new URL(pathToCode)});
				loader.loadClass(className);
				res = (UndirectedGraphCreator) loader.loadClass(className).newInstance();
				
				loader.close();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | RuntimeException | IOException e) {
				// Curious Exception
				e.printStackTrace();
			}
		}
		
		swapSystemToJDK();
		
		return res;
	}
	
	private File[] getStringCode(String className, String code, URI dest) {
		
		if(code != null && !code.isEmpty()) {
			String finalCode = "import rioko.grapht.linear.UndirectedGraphCreator;\n" + 
					"import rioko.grapht.linear.UndirectedGraph;\n\n" +
					"public class " + className + " implements UndirectedGraphCreator {\n" +
						"\tpublic UndirectedGraph create() {\n" + 
							"\t\treturn " + code + ";\n" +
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
	
	private String getURLToCodeFolder() {
		try {
			String absolutePathToMe = (new File(JavaFileGraphCreator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).getAbsolutePath().replace("\\", "/");
			
			/* We check if we are debugging or inside a Jar file */
			if(absolutePathToMe.endsWith(".jar")) {
				int index = absolutePathToMe.lastIndexOf("/");
				absolutePathToMe = absolutePathToMe.substring(0, index);
			}
			
			return "file:///"+ absolutePathToMe + "/code/";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getPathToLatestJDK() {
		File currentJavaHome = new File(System.getProperty("java.home"));
		
		/* We check that this is a directory */
		if(!currentJavaHome.isDirectory()) {
			return null;
		}
		
		/* We go up until find "Java" folder */
		File aux = currentJavaHome;
		while((aux != null) && (!aux.getName().contains("Java"))) {
			aux = aux.getParentFile();
		}
		
		/* We check we have found Java folder */
		if(aux == null) {
			return null;
		}
		
		File[] listOfJDK = aux.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if(file.getName().contains("jdk")) {
					return true;
				}
				
				return false;
			}
			
		});
		
		return getGreaterVersion(listOfJDK).getAbsolutePath();
	}

	private static File getGreaterVersion(File[] listOfJDK) {
		File res = null;
		
		if(listOfJDK.length > 0) {
			res = listOfJDK[0];
			JavaVersion currentVersion = new JavaVersion(res);
			
			for(int i = 1; i < listOfJDK.length; i++) {
				JavaVersion aux = new JavaVersion(listOfJDK[i]);
				
				if(aux.isGreater(currentVersion)) {
					res = listOfJDK[i];
					currentVersion = aux;
				}
			}
		}
		
		return res;
	}
	
	private static class JavaVersion {
		private int distribution, version, release, update;
		private boolean correct = false;
		
		JavaVersion(File file) {
			if(file.isDirectory() && file.getName().startsWith("jdk")) {
				String name = file.getName();
				
				String subName = name.substring(3);
				
				String[] tokens1 = subName.split("_");
				if(tokens1.length == 2) {
					String[] tokens2 = tokens1[0].split("[.]");
					if(tokens2.length == 3) {
						try {
							this.distribution = Integer.parseInt(tokens2[0]);
							this.version = Integer.parseInt(tokens2[1]);
							this.release = Integer.parseInt(tokens2[2]);
							this.update = Integer.parseInt(tokens1[1]);
							
							correct = true;
						} catch(NumberFormatException e) {}
					}
				}
			}
		}

		boolean isGreater(JavaVersion currentVersion) {
			if(!this.correct) {
				return false;
			}
			
			if(!currentVersion.correct) {
				return true;
			}
			
			if(this.distribution > currentVersion.distribution) {
				return true;
			}
			
			if(this.version > currentVersion.version) {
				return true;
			}
			
			if(this.release > currentVersion.release) {
				return true;
			}
			
			if(this.update > currentVersion.update) {
				return true;
			}
			
			return false;
		}
	}
}
