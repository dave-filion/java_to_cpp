package xtc.lang.blink.agent;

/**
 * The Blink support for the convenience variables in the Java
 * debugger. This class in particular represent a poly-morphic
 * convenience variable to hold both the java primitive and the
 * reference values.
 *
 * @author Byeongcheol Lee 
 */
public class AgentVariable {

  /** The unique identifier. */
  private int id;  

  /** The value. */
  private Object value;

  /** A Java expression to read the value from the Java debugger. */
  private String expr;

  /** The JNI type representation. */
  private String jniType;

  /** The Java type representation. */
  private String javaType;

  /** Constructor. */
  private AgentVariable(int id) {this.id = id;}
  
  /** The Blink Java multi-typed variable array. */
  private static AgentVariable[] jvars;

  /** The next unique variable identifier. */
  private static int nextVJIdentifier;

  /** The Java expression for the "jvars." */
  private static final String JVARS_EXPRESSION = "xtc.lang.blink.agent.AgentVariable.jvars";

  /** Perform initialization. */
  public static void init() {
    jvars = new AgentVariable[100];
    nextVJIdentifier = 0;
  }

  /** Create a new instance of the convenience variable. */
  private static AgentVariable createVariable() {
    if (nextVJIdentifier < jvars.length) {
      AgentVariable vj = new AgentVariable(nextVJIdentifier);
      jvars[nextVJIdentifier] = vj;
      nextVJIdentifier++;
      return vj;
    } else {
      AgentVariable[] newJVars = new AgentVariable[jvars.length * 2];
      System.arraycopy(jvars, 0, newJVars, 0, nextVJIdentifier);
      jvars = newJVars;
      return createVariable();
    }
  }

  /** clean up the temp variables table. */
  public static void cleanTempVars() {
    for(int i = 0; i < jvars.length;i++) {
      if( jvars[i] != null ) {
        jvars[i] = null;
      }
    }
    nextVJIdentifier = 0;
  }

  /**
   * Take a boolean primitive value, create convenience variable, and
   * assign the boolean value to the convenience variable.
   *
   * @param value
   * @return The convenience variable identifier.
   */
  public static int setVjFromJavaExpr(boolean value) {
    AgentVariable vj = createVariable();
    vj.value = new Boolean(value);
    vj.expr = "((Boolean)" + JVARS_EXPRESSION + "[" + vj.id + "].value)" 
            + ".getBoolean()";
    vj.jniType = "jboolean";
    vj.javaType = "boolean";
    return vj.id;
  }

  /**
   * Take an integer primitive value, create convenience variable, and
   * assign the integer value to the convenience variable.
   *
   * @param value
   * @return The convenience variable identifier.
   */
  public static int setVjFromJavaExpr(int value) {
    AgentVariable vj = createVariable();
    vj.value = new Integer(value);
    vj.expr = "((Integer)" + JVARS_EXPRESSION + "[" + vj.id + "].value)"
            + ".intValue()";
    vj.jniType = "jint";
    vj.javaType = "int";
    return vj.id;
  }

  /**
   * Take an double primitive value, create convenience variable, and
   * assign the double value to the convenience variable.
   *
   * @param value
   * @return The convenience variable identifier.
   */
  public static int setVjFromJavaExpr(double value) {
    AgentVariable vj = createVariable();
    vj.value = new Double(value);
    vj.expr = "((Double)" + JVARS_EXPRESSION + "[" + vj.id + "].value)" 
            + ".doubleValue()";
    vj.jniType = "jdouble";
    vj.javaType = "double";
    return vj.id;
  }

  /**
   * Take an reference value, create convenience variable, and assign
   * the reference value to the convenience variable.
   *
   * @param obj The reference.
   * @return The convenience variable identifier.
   */
  public static int setVjFromJavaExpr(Object obj) {
    AgentVariable vj = createVariable();
    if (obj != null) {
      vj.value = obj;
      String type = obj.getClass().getName();
      if (type.equals("[D")) {
        vj.expr = "((double[])" + JVARS_EXPRESSION + "[" + vj.id + "].value)";
      } else {
        vj.expr = "((" + type + ")" +  JVARS_EXPRESSION + "[" + vj.id + "].value)";
      }
      vj.javaType = type;
    } else {
      vj.value = null;
      vj.expr="null";
      vj.javaType = "NULL";
    }
    vj.jniType = "jobject";

    return vj.id;
  }

  /** 
   * Create a Java expression so that the Java debugger will read the
   * value of the convenience variable. 
   * @param vjid The convenience variable identifier.
   * @return The Java expression.
   */
  public static String getVJExpr(int vjid) {
    return jvars[vjid].expr;
  }

  /** 
   * Get the Java type representation of a convenience variable.
   * @param vjid The convenience variable identifier.
   * @return The Java expression.
   */
  public static String get_java_type(int vjid) {
    return jvars[vjid].javaType;
  }

  /** 
   * Get the JNI type representation of a convenience variable.
   * @param vjid The convenience variable identifier.
   * @return The JNI representation.
   */
  public static String get_vj_jni_type(int vjid) {
    return jvars[vjid].jniType;
  }
  
  /** 
   * Get the boolean value of the convience variable.
   * @param vjid The convenience variable identifier.
   * @return The boolean value.
   */
  public static boolean get_vj_jboolean(int vjid) {
    Object obj = jvars[vjid].value;
    Boolean bobj = (Boolean)obj;
    return bobj.booleanValue();
  }

  /** 
   * Get the integer value of the convience variable.
   * @param vjid The convenience variable identifier.
   * @return The integer value.
   */
  public static int get_vj_jint(int vjid) {
    Object obj = jvars[vjid].value;
    Integer bobj = (Integer)obj;
    return bobj.intValue();
  }

  /** 
   * Get the double value of the convience variable.
   * @param vjid The convenience variable identifier.
   * @return The double value.
   */
  public static double get_vj_jdouble(int vjid) {
    Object obj = jvars[vjid].value;
    Double bobj = (Double)obj;
    return bobj.doubleValue();
  }

  /** 
   * Get the reference value of the convience variable.
   * @param vjid The convenience variable identifier.
   * @return The reference value.
   */
  public static Object get_vj_jobject(int vjid) {
    Object obj = jvars[vjid].value;
    Object bobj = (Object)obj;
    return bobj;
  }
}
