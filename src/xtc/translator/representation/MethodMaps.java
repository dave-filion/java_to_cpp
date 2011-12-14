package xtc.translator.representation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodMaps {

	public static Map<String, Map<String, List<Method>>> methodMaps = new HashMap<String, Map<String, List<Method>>>();
	
	public static Map getMethodMapForClass(String className) {
		return methodMaps.get(className);
	}
	
	public static void addMethodMapForClass(String className, Map methodMap) {
		methodMaps.put(className, methodMap);
	}
	
}
