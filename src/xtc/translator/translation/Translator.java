package xtc.translator.translation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.List;

import xtc.translator.*;
import xtc.translator.representation.CompilationUnit;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.Node;

import xtc.lang.JavaFiveParser;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
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
			PackageResolver packageResolver = new PackageResolver(args[0], args[1]);

			try {
				packageResolver.collect();
				
				ProtoManager protoManager = new ProtoManager(packageResolver.getProtoClasses());
								
				Collector collector = new Collector(protoManager.processProtoClasses());

				collector.collect();
				
				PrintHandler printHandler = new PrintHandler(collector.classes, args[1]);
				
				printHandler.printAllHeaders();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
