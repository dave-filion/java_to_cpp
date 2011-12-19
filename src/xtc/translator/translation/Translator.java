package xtc.translator.translation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import xtc.parser.ParseException;

/**
 * A translator from (a subset of) Java to  (a subset of) C++.
 * 
 * @author Robert Grimm
 * @version $Revision$
 */
public class Translator {

	File sourceFile;

	public String ARG_ERROR = "Need Two Arguments ([Main Class] [Classpath])";

	/** Create a new translator. */
	public Translator() {
		// Nothing to do.
	}

	public void run(String[] args) {

		if (args.length == 2) {
			
			// arg[0] is mainClassPath, arg[1] is classPath
			String mainClassPath = args[0];
			String classPath = args[1];
			
			PackageResolver packageResolver = new PackageResolver(mainClassPath, classPath);

			try {
				// Collect files from packages
				packageResolver.collect();
				
				// Make protoClassManager with packageResolvers ProtoClasses
				ProtoManager protoManager = new ProtoManager(packageResolver.getProtoClasses());
				
				// Make new collector with classes from protoManager
				Collector collector = new Collector(protoManager.processProtoClasses());
				
				// Collect information and process classes to prepare for printing
				collector.collect();
				
				// Create new printHandler to handle printing
				PrintHandler printHandler = new PrintHandler(collector.classes, classPath);
				
				// Print c++ headers
				printHandler.printAllHeaders();
				
				// Print c++ .cc files
				printHandler.printAllImplementations();
				
				// Print main.cc file, using main class
				printHandler.printMainFile(mainClassPath);
				
				// Copy necessary dependencies to out file
				for (File file : new File("dependencies").listFiles()) {
					FileUtils.copyFileToDirectory(file, new File("out"));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(ARG_ERROR);
		}

	}
	
	
	/**
	 * Run the translator with the specified command line arguments and stuff.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		/*
		 * Run to create AST
		 */
		new Translator().run(args);
	}
}
