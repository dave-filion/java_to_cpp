package xtc.translator.translation;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import xtc.lang.JavaFiveParser;
import xtc.parser.ParseException;
import xtc.parser.Result;
import xtc.tree.Node;


public class PackageResolver {

	private String mainClass;
	private ProtoClass mainProtoClass;
	private String classpath;
	private ArrayList<ProtoClass> protoClasses;
	private FilenameFilter javaSrcFilter;
	
	public PackageResolver(String mainClass, String classpath){
		this.mainClass = mainClass;
		this.mainProtoClass = null;
		this.classpath = classpath;
		this.protoClasses = new ArrayList<ProtoClass>();
		
		this.javaSrcFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".java")) {
					return true;
				} else {
					return false;
				}
			}
		};
	}
	
	/**
	 * Visable method.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void collect() throws IOException, ParseException{
		collect(classpath);
	}
	
	/**
	 * Recursive version for traversing directories
	 * @param path
	 */
	private void collect(String path) throws IOException, ParseException {
		
		//TODO: might wanna remove the ./ before things if there is one
		File src = new File(path);
				
		for (File file : src.listFiles()) {				
			if (file.isDirectory()) {
				collect(file.toString());
			}
		}
		
		// Get java files
		for (File javaFile : src.listFiles(javaSrcFilter)) {
			String packageName = javaFile.getParent().toString().replace("/", ".");
			
			FileReader in = new FileReader(javaFile);
			JavaFiveParser parser = new JavaFiveParser(in, javaFile.toString(), (int) javaFile.length());
			Result result = parser.pCompilationUnit(0);
			Node node = (Node) parser.value(result);

			ProtoClass protoClass = new ProtoClass(packageName, node);
			
			if (path.equals(mainClass)) {
				this.mainProtoClass = protoClass;
			}
						
			protoClasses.add(new ProtoClass(packageName, node));
		}
		
	}

	public ArrayList<ProtoClass> getProtoClasses() {
		return protoClasses;
	}

	public ProtoClass getMainProtoClass() {
		return mainProtoClass;
	}

	public void setMainProtoClass(ProtoClass mainProtoClass) {
		this.mainProtoClass = mainProtoClass;
	}

	
}
