package xtc.lang.blink;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xtc.tree.GNode;
import xtc.tree.Visitor;
import xtc.lang.jeannie.Utilities;
import xtc.lang.blink.CommandAstAnalyzer.Language;
import xtc.lang.blink.CallStack.ICallFrame;
import xtc.lang.blink.CallStack.JavaCallFrame;
import xtc.lang.blink.CallStack.JeannieCallFrame;
import xtc.lang.blink.CallStack.NativeCallFrame;
import xtc.lang.blink.SymbolMapper.SourceFileAndLine;
import xtc.lang.blink.SymbolMapper.VariableRemapEntry;
import xtc.lang.blink.Event.Java2NativeReturnEvent;
import xtc.lang.blink.Event.JavaBreakPointHitEvent;
import xtc.lang.blink.Event.JavaStepCompletionEvent;
import xtc.lang.blink.Event.Native2JavaReturnEvent;
import xtc.lang.blink.Event.NativeBreakPointHitEvent;
import xtc.lang.blink.Event.NativeStepCompletionEvent;
import xtc.lang.blink.Event.SessionFinishRequestEvent;


/**
 * The Blink user command line interpreter. See Debugger.rats, DebuggerC.rats
 * and DebuggerJava.rats for formal syntax.
 *
 * @author Byeongcheol Lee
 */
public final class CommandInterpreter extends Visitor {

  /**
   * The blink debugger for the communication with jdb, gdb and the Blink Debug
   * Agent (BDA).
   */
  private final Blink dbg;

  /** The break point manager. */
  private final BreakPointManager breakpointManager;

  /** The Jeannie symbol remapper. */
  private final SymbolMapper jeannieSymbolRemapper;

  /** The current context for debugging. */
  private DebuggerContext debuggerContext;

  /**
   * Constructor.
   *
   * @param dbg The blink debugger.
   * @param breakpointManager The break point manager.
   */
  CommandInterpreter(final Blink dbg, BreakPointManager breakpointManager) {
    this.dbg = dbg;
    this.breakpointManager = breakpointManager;
    this.jeannieSymbolRemapper = new SymbolMapper();
  }

  /** Implement BlinkEventSource. */
  public String getEventSourceName() {
    return "CommandRunner";
  }
  
  /** Visit an InitJCommand = (no children). */
  public final void visitInitJCommand(final GNode n) {
    dbg.initj();
    dbg.ensurePureContext();
  }

  /** Visit a J2cCommand = (no children). */
  public final void visitJ2cCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    dbg.j2c();
  }

  /** Visit a C2jCommand = (no children). */
  public final void visitC2jCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    dbg.c2j();
  }

  /** Visit a JRetCommand = (no children). */
  public final void visitJRetCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    dbg.jret();
  }

  /** Visit a JdbCommand = RestOfLine. */
  public final void visitJdbCommand(final GNode n) {
    String cmd = n.getString(0);
    try {
      String output = dbg.jdb.runCommand(cmd);
      if (dbg.options.getVerboseLevel() < 1) {
        dbg.out(output);
      }
    } catch (IOException e) {
      dbg.err("can not successfully run the jdb command");
    }
  }

  /** Visit a GdbCommand = RestOfLine. */
  public final void visitGdbCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.out("gdb is not activated yet.\n");
      return;
    }
    String cmd = n.getString(0);
    try {
      String output = dbg.ndb.runCommand(cmd); 
      if (dbg.options.getVerboseLevel() < 1) {
        dbg.out(output);
      }
    } catch (IOException e) {
      dbg.err("can not successfully native debugger command");
    }
  }

  /** Visit a HelpCommand = (no children). */
  public final void visitHelpCommand(final GNode n) {
    dbg.help();
    dbg.ensurePureContext();
  }

  /** Visit a ExitCommand = (no children). */
  public final void visitExitCommand(final GNode n) {

    switch(dbg.getDebugControlStatus()) {
    case NONE:
      dbg.out("can not process user exit command\n");
      break;
    case JDB:
    case GDB:
      dbg.exit();
      dbg.enqueEvent(new SessionFinishRequestEvent("user request"));
      break;
    case JDB_IN_GDB:
    case GDB_IN_JDB:
      dbg.out("can not process use exit in the nested debug context!\n");
      break;
    }  
  }

  /** Visit a BreakClassCommand = QualifiedIdentifier LineNumber. */
  public final void visitBreakClassCommand(final GNode n) {
    String className = n.getNode(0).getString(0);
    int lineNumber = Integer.parseInt( n.getString(1));
    breakpointManager.setJavaBreakPoint(className, lineNumber);
    dbg.ensurePureContext();
  }

  /** Visit a BreakClassCommand = QualifiedIdentifier. */
  public final void visitBreakClassMethodCommand(final GNode n) {
    String classAndMethod= Utilities.qualifiedIdentifierToString(n.getGeneric(0));
    Pattern p = Pattern.compile("^(.+)\\.([^\\.]+)$");
    Matcher m = p.matcher(classAndMethod);
    if (m.matches()) {
      String cname = m.group(1);
      String mname = m.group(2);
      breakpointManager.setJavaBreakPoint(cname, mname);
      dbg.ensurePureContext();
    } else {
      dbg.err("can not recognize as <class>.<method>\n");
    }
  }

  /** Visit a BreakFileCommand = FileName LineNumber. */
  public final void visitBreakFileCommand(final GNode n) {
    String fileName = n.getString(0);
    int lineNumber = Integer.parseInt(n.getString(1));
    try {
      breakpointManager.setNativeBreakpoint(fileName, lineNumber);
    } catch(IOException  e) {
      dbg.err("Can not set breakpoint.");
    }
    dbg.ensurePureContext();
  }

  /** Visit a BreakFunctionCommand = Identifier. */
  public final void visitBreakFunctionCommand(final GNode n) {
    String identifier = n.getString(0);
    try {
      breakpointManager.setNativeBreakpoint(identifier);
    } catch(IOException e) {
      dbg.err("Can not set breakpoint."); 
    }
    dbg.ensurePureContext();
  }
  
  /** Visit a InfoBreakCommand = (no children). */
  public final void visitInfoBreakCommand(final GNode n) {
    breakpointManager.showUserBreakPointList();
    dbg.ensurePureContext();
  }

  /** Visit a InfoWatchCommand = (no children). */
  public final void visitInfoWatchCommand(final GNode n) {
    assert false : "not yet implemented";
  }

  /** Visit a DeleteCommand = IntegerLiteral. */
  public final void visitDeleteCommand(final GNode n) {
    int id = Integer.parseInt(n.getNode(0).getString(0));
    breakpointManager.clearBreakpoint(id);
    dbg.ensurePureContext();
  }

  /** Visit a NextCommand = (no children). */
  public final void visitRunCommand(final GNode n) {
    dbg.run();
  }

  /** Visit a StepCommand = (no children). */
  public final void visitStepCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
     dbg.err("Blink is not initialized to run this command\n");
     return;
    }
    try {
      clearDebuggerContext();
      dbg.step();
      SourceFileAndLine loc = dbg.getCurrentSourceLevelLocation();
      String line = dbg.getCurrentSourceLine();
      assert loc != null : "step must be completed at the source line";
      dbg.out("Step completed: " + loc + "\n" + line + "\n");
    } catch(IOException e) {
      dbg.err("could not sucessfully perform stepping\n");
    }
  }

  /** Visit a NextCommand = (no children). */
  public final void visitNextCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      clearDebuggerContext();
      Event e = dbg.next();
      if (e instanceof NativeStepCompletionEvent
          || e instanceof JavaStepCompletionEvent
          || e instanceof Java2NativeReturnEvent
          || e instanceof Native2JavaReturnEvent) {
        SourceFileAndLine loc = dbg.getCurrentSourceLevelLocation();
        String line = dbg.getCurrentSourceLine();
        assert loc != null : "step must be completed at the source line";
        dbg.out("Step completed: " + loc + "\n" + line + "\n");        
      } else if ( e instanceof NativeBreakPointHitEvent) {
        EventLoop.reportEvent(dbg, (NativeBreakPointHitEvent)e);
      } else if ( e instanceof JavaBreakPointHitEvent) {
        EventLoop.reportEvent(dbg, (JavaBreakPointHitEvent)e);
      } else {
        assert false: "unrecognized event during stepping :" + e;
      }      
    } catch (IOException e) {
      dbg.err("could not correctly perform the next command.\n");
    }
  }

  /** Visit a ContinueCommand = (no children). */
  public final void visitContinueCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    clearDebuggerContext();
    dbg.cont();
  }

  /** Visit a LocalsCommand = (no children). */
  public final void visitLocalsCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      ensureDebuggerContext();
      debuggerContext.showLocals(dbg, jeannieSymbolRemapper);
    } catch(IOException e) {
      dbg.err("could not perform list command.\n");
    } finally {
      dbg.ensurePureContext();
    }
  }

  /** Visit a ListCommand = (no children). */
  public final void visitListCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      ensureDebuggerContext();
      debuggerContext.showSourceCode(dbg);
    } catch(IOException e) {
      dbg.err("could not perform the list command.\n");
    } finally {
      dbg.ensurePureContext();
    }
  }

  /** Visit a WhereCommand = (no children). */
  public final void visitWhereCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      ensureDebuggerContext();
      debuggerContext.showWhere(dbg);
    } catch(IOException e) {
      dbg.err("could not perform where command.\n");
    } finally {
      dbg.ensurePureContext();
    }
  }

  /** Visit an UpCommand = IntegerLiteral. */
  public final void visitUpCommand(final GNode n) {
    int steps = Integer.parseInt( n.getNode(0).getString(0));
    try {
      ensureDebuggerContext();
      debuggerContext.unWindStack(dbg, steps);
    } catch (IOException e) {
      dbg.err("could not perform up\n");
    } finally {
      dbg.ensurePureContext();
    }
  }

  /** Visit a DownCommand = IntegerLiteral. */
  public final void visitDownCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    int steps = Integer.parseInt( n.getNode(0).getString(0));
    try {
      ensureDebuggerContext();
      debuggerContext.windStack(dbg, steps);
    } catch (IOException e) {
      dbg.err("could not perform up\n");
    } finally {
      dbg.ensurePureContext();
    }
  }

  /** Visit a StatCommand = (no children). */
  public final void visitStatCommand(final GNode n) {
    dbg.out("control: " + dbg.getDebugControlStatus() + "\n");
  }

  /** Visit a WatchCExpressionCommand = C.Expression. */
  public final void visitWatchCExpressionCommand(final GNode n) {
    assert false : "not yet implemented";
  }

  /** Visit a WatchJavaFieldCommand = [WatchKind] QualifiedIdentifier. */
  public final void visitWatchJavaFieldCommand(final GNode n) {
    assert false : "not yet implemented";
  }

  /** Visit a PrintCExpressionCommand = C.Expression. */
  public final void visitPrintCExpressionCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      breakpointManager.freezeActiveBreakPoints();
      // Run the evaluation and get a gdb variable holding the result.
      Object obj = dispatch(n.getNode(0));
      dbg.out("====> " + cprint((CExpr)obj) + "\n");
    } catch(IOException e) {
      dbg.err("could not correctly print the final result\n");
    } finally {
      breakpointManager.unfreezeAllBreakpoints();
      cleanVJandVC();
      dbg.ensurePureContext();
    }
  }

  /** Visit a PrintJavaExpressionCommand = Java.Expression. */
  public final void visitPrintJavaExpressionCommand(final GNode n) {
    if (!dbg.IsNativeDebuggerAttached()) {
      dbg.err("Blink is not initialized to run this command\n");
      return;
     }
    try {
      breakpointManager.freezeActiveBreakPoints();
      //Run the evaluation and get a jdb variable holding the result.
      Object obj = dispatch(n.getNode(0));
      if (obj == null) {return;}
      dbg.out("=====> " + jprint((JExpr)obj)+ "\n");
    } catch(IOException e) {
      dbg.err("could not correctly print the final result\n");
    } finally {
      breakpointManager.unfreezeAllBreakpoints();
      cleanVJandVC();
      dbg.ensurePureContext();
    }
  }

  /** Visit a JavaInCExpression = Java.UnaryExpression. */
  public final CExpr visitJavaInCExpression(final GNode n) throws IOException {
    return CV_assign_JExpr((JExpr)dispatch(n.getNode(0)));
  }

  /** Visit a CInJavaExpression = C.UnaryExpression. */
  public final JExpr visitCInJavaExpression(final GNode n)throws IOException {
    return JV_assign_CExpr((CExpr)dispatch(n.getNode(0)));
  }

  /** Visit an IntegerLiteral = String. */
  public final JExpr visitIntegerLiteral(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    return new JExpr(n.getString(0));
  }

  /** Visit an IntegerConstant = String. */
  public final CExpr visitIntegerConstant(final GNode n) {
    assert getLanguage(n) == Language.C;
    return new CExpr(p(n.getString(0)));
  }

  /** Visit an StringConstant = String. */
  public final JExpr visitStringConstant(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    return new JExpr(q(n.getString(0)));
  }

  /** Visit an StringLiteral = String. */
  public final JExpr visitStringLiteral(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    return new JExpr(q(n.getString(0)));
  }

  /** Visit an NullLiteral = (no children). */
  public final JExpr visitNullLiteral(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    return new JExpr(p("null"));
  }

  /** Visit a MetaVariable = Identifier. */
  public final BExpr visitMetaVariable(final GNode n) throws IOException {
    assert false : "not implemented yet";
    return null;
  }

  /** Visit a PrimaryIdentifier = String. */
  public BExpr visitPrimaryIdentifier(final GNode n) throws IOException {
    String idName = (String)n.get(0);

    //try id remapping for the Jeannie frame.
    ensureDebuggerContext();
    SourceFileAndLine loc = debuggerContext.getCurrentLocation();
    VariableRemapEntry idRemap = jeannieSymbolRemapper.lookUpVariableRemap(idName, loc
        .getSourceFile(), loc.getSourceLine());
    String idExpr = null;
    if (idRemap != null) {
      idExpr = idRemap.targetLanguageExpression();
    }
    if (idExpr == null) {
      idExpr = idName;
    }

    switch(getLanguage(n)) {
      case C: return eval(n, new CExpr(idExpr));
      case JAVA: return eval(n, new JExpr(idExpr));
    }
    assert false : "not reachable"; 
    return null;
  }

  /** Visit a ThisExpression = [Expression]. */
  public final void visitThisExpression(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    assert false : "The Blink debugger does not support" + 
    "qualified this in Java expression.";
  }

  /** Visit an AdditiveExpression = Expression ("+" / "-") Expression. */
  public final BExpr visitAdditiveExpression(final GNode n) throws IOException {
    switch(getLanguage(n)) {
     case C: return eval(n, CExpr.binop(
           (CExpr)dispatch(n.getNode(0)), 
           n.getString(1), 
           (CExpr)dispatch(n.getNode(2))));
     case JAVA: return eval(n, JExpr.binop(
         (JExpr)dispatch(n.getNode(0)), 
         (String)(n.get(1)),
         (JExpr)dispatch(n.getNode(2))
     ));
    }
    assert false : "not reachable"; 
    return null;
  }

  /** Visit a EqualityExpression = Expression ("==" / "!=") Expression. */
  public final BExpr visitEqualityExpression(final GNode n) throws IOException {
    assert !isLValue(n) : "the binary operation never generages l-value.";
    switch(getLanguage(n)) {
    case C: return eval(n, CExpr.binop(
        (CExpr)dispatch(n.getNode(0)), 
        n.getString(1), 
        (CExpr)dispatch(n.getNode(2)))
    ); 
    case JAVA: return eval(n, JExpr.binop(
        (JExpr)dispatch(n.getNode(0)),
        n.getString(1), 
        (JExpr)dispatch(n.getNode(2))
    ));
    }
    assert false : "not reachable"; 
    return null;
  }

  /** Visit a FunctionCall = Expression [ExpressionList]. */
  public final CExpr visitFunctionCall(final GNode n) throws IOException {
    assert getLanguage(n) == Language.C;
    assert !isLValue(n) : "function call can never be l-value.";
    return eval(n, CExpr.funcCall(
        (CExpr)dispatch(n.getNode(0)), 
        (CExpr[])dispatch(n.getNode(1))));
  }

  /** Visit ExpressionList = Expression+. */
  public final CExpr[] visitExpressionList(final GNode n) {
    assert getLanguage(n) == Language.C;
    assert !isLValue(n) : "function arguments can never be l-value.";
    CExpr[] exprs= new CExpr[n.size()];
    for (int i=0; i<n.size(); i++) {
        exprs[i] = (CExpr)dispatch(n.getNode(i));
    }
    return exprs;
  }

  /** Visit a CallExpression = [Expression] null MethodName Arguments. */
  public JExpr visitCallExpression(final GNode n) throws IOException {
    assert getLanguage(n) == Language.JAVA;
    assert !isLValue(n) : "method call never gernates l-value.";

    return eval(n, JExpr.methodCall(
        (JExpr) dispatch(n.getNode(0)), 
        n.getString(2), 
        (JExpr[]) dispatch(n.getNode(3))
    ));
  }

  /** Visit Arguments = Expression*. */
  public final JExpr[] visitArguments(final GNode n) {
    assert getLanguage(n) == Language.JAVA;
    JExpr[] args = new JExpr[n.size()];
    for (int i=0; i<n.size(); i++)
      args[i] = (JExpr)dispatch(n.getNode(i));
    return args;
  }

  /** Visit an AddressExpression = Expression. */
  public final CExpr visitAddressExpression(final GNode n) {
    assert getLanguage(n) == Language.C;
    assert !isLValue(n) : "& never gernates l-value.";
    return CExpr.address((CExpr)dispatch(n.getNode(0)));
  }

  /** Visit a Expression = Expression ("=" / "+=" / "-=" / "*=" / "/=" / "&amp;=" / "|=" / "^=" / "%=" / "&lt;&lt;=" / "&gt;&gt;=" / "&gt;&gt;&gt;=") Expression. */
  public final JExpr visitExpression(final GNode n) throws IOException {
    assert getLanguage(n) == Language.JAVA;
    assert !isLValue(n) : "the assignment never gernates l-value.";

    // mark the LHS is the l-value expression generator. 
    setLValue((GNode)n.getNode(0));
    return eval(n, JExpr.binop(
        (JExpr)dispatch(n.getNode(0)), 
        n.getString(1), 
        (JExpr)dispatch(n.getNode(2))
    ));
  }

  /** Visit an AssignmentExpression = Expression ("=" / "+=" / "-=" / "*=" / "/=" / "%=" / "&lt;&lt;=" / "&gt;&gt;=" / "&amp;=" / "^=" / "|=") Expression. */
  public final CExpr visitAssignmentExpression(final GNode n) throws IOException {
    assert getLanguage(n) == Language.C;
    assert !isLValue(n) : "the assignment never gernates l-value.";

    //  mark the LHS is the l-value expression generator. 
    setLValue((GNode)n.getNode(0));
    return eval(n, CExpr.binop(
        (CExpr)dispatch(n.getNode(0)), 
        n.getString(1), 
        (CExpr)dispatch(n.getNode(2))
    ));
  }

  /** Visit an IndirectionExpression = Expression. */
  public final CExpr visitIndirectionExpression(final GNode n) throws IOException {
    assert getLanguage(n) == Language.C;
    return eval(n, CExpr.indirect((CExpr)dispatch(n.getNode(0))));
  }

  /** Visit a QualifiedIdentifier = Identifier+. */
  public final JExpr visitQualifiedIdentifier(final GNode n) throws IOException {
    assert getLanguage(n) == Language.JAVA;
    return eval(n, new JExpr(Utilities.qualifiedIdentifierToString(n)));
  }

  /** Visit a SelectionExpression = Expression Identifier. */
  public final JExpr visitSelectionExpression(final GNode n) throws IOException {
    assert getLanguage(n) == Language.JAVA;
    return eval(n, JExpr.field((JExpr)dispatch(n.getNode(0)),n.getString(1)));
  }

  /** Visit a DirectComponentSelection = Expression String. */
  public final CExpr visitDirectComponentSelection(final GNode n) throws IOException {
    assert getLanguage(n) == Language.C;
    return eval(n, CExpr.fieldDirect((CExpr)dispatch(n.getNode(0)), n.getString(1)));
  }

  /** Visit a SubscriptExpression = Expression Expression. */
  public final BExpr visitSubscriptExpression(final GNode n) throws IOException {

    switch(getLanguage(n)) {
    case JAVA:
      return eval(n, JExpr.arraySelect(
          (JExpr)dispatch(n.getNode(0)), 
          (JExpr)dispatch(n.getNode(1))));
    case C:
      return eval(n, CExpr.arraySelect(
          (CExpr)dispatch(n.getNode(0)), 
          (CExpr)dispatch(n.getNode(1))));
    }
    assert false : "not reachable";
    return null;
  }

  /**
   * jprint <JExpr>
   * 
   * This is one of the six Blink expression evaluation primitives.
   * @param jexpr The expression.
   * @return The result of evluating the <JExpr>
   */
  private String jprint(JExpr jexpr) throws IOException {
    dbg.ensureJDBContext();
    DebuggerContext c =  ensureDebuggerContext();
    JavaCallFrame jf = c.getCurrentJavaFrame();
    String result = dbg.jdb.print(jf, jexpr.getValueExpr()); 
    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\tjprint " + jexpr.getValueExpr() + "\n");
    }
    return result;
  }

  /**
   * cprint <CExpr>
   * 
   * This is one of the six Blink expression evaluation primitives.
   * @param cexpr The expression.
   * @return The result of evaluating the <CExpr>
   */
  private String cprint(CExpr cexpr) throws IOException {
    // talk to the GDB to print the final result.
    DebuggerContext c =  ensureDebuggerContext();
    NativeCallFrame nframe = c.getCurrentNativeFrame();
    dbg.ensureGDBContext();
    String result = dbg.ndb.eval(nframe, cexpr.getValueExpr());
    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\tcprint " + cexpr.getValueExpr() + "\n");
    }
    return result;
  }

  /**
   * <JV> := <JExpr>
   * 
   * This is one of the six Blink expression evaluation primitives.
   * 
   * @param jexpr The Java expression.
   * @return The JV variable.
   */
  private JV JV_assign_JExpr(JExpr jexpr) throws IOException {
    // get the java expression for the new VJ.
    DebuggerContext c =  ensureDebuggerContext();
    JavaCallFrame jf = c.getCurrentJavaFrame();
    dbg.ensureJDBContext();
    String vjid = dbg.jdb.newConvenienceVariable(jf, jexpr.getValueExpr()); 
    String vjexpr = dbg.jdb.getConvenienceVariableRValueExpression(vjid);
    String javaType = dbg.jdb.getConvenienceVariableJavaType(vjid);
    JV jv = new JV( "vj" + vjid, javaType, vjexpr);
    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\t" + jv.getVjID() + "(" + jv.getType() + ") := "
          + jexpr.getValueExpr() + "\n");
    }
    return jv; 
  }

  /**
   *  <JV> := <CExpr>.
   *
   * This is one of the six Blink expression evaluation primitives.
   * @param cexpr The C expression.
   * @return The jv variable.
   */
  private JV JV_assign_CExpr(CExpr cexpr) throws IOException {

    //get the expected type of the RHS.
    DebuggerContext c =  ensureDebuggerContext();
    NativeCallFrame nframe = c.getCurrentNativeFrame();
    dbg.ensureGDBContext();
    String eType = dbg.ndb.whatis(nframe,cexpr.getValueExpr());

    //choose the right c2j value conversion debug agent function.
    String dbaExprFunc;
    if (eType.equals("int")) {
      dbaExprFunc = "bda_set_vj_from_cexpr_jint";
    } else if (eType.equals("long")) {
      dbaExprFunc = "bda_set_vj_from_cexpr_jlong";
    } else if (eType.equals("float")) {
      dbaExprFunc = "bda_set_vj_from_cexpr_jfloat";
    } else if (eType.equals("double")) {
      dbaExprFunc = "bda_set_vj_from_cexpr_jdouble";
    } else if (eType.equals("jobject")) {
      dbaExprFunc = "bda_set_vj_from_cexpr_jobject";
    } else {
      dbaExprFunc = "bda_set_vj_from_cexpr_jobject";
    }

    //evaluate the RHS and convert the result into the VJ in the LHS.
    String jnienv = dbg.ensureJNIENV();
    String vjid = dbg.ndb.eval(nframe,
        dbaExprFunc + "(" + jnienv + "," + cexpr.getValueExpr() + ")");

    // get the java expression for the new VJ.
    dbg.ensureJDBContext();
    String vjexpr = dbg.jdb.getConvenienceVariableRValueExpression(vjid); 
    String javaType = dbg.jdb.getConvenienceVariableJavaType(vjid); 
    JV jv = new JV( "vj" + vjid, javaType, vjexpr);

    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\t" + jv.getVjID() + "(" + jv.getType() + ") := "
          + cexpr.getValueExpr() + "\n");
    }

    return jv; 
  }

  /**
   * <VC> : = <JExpr> 
   * 
   * This is one of the six Blink expression evaluation primitives.
   * @param jexpr The Java expression.
   * @return The vc variable.
   */
  private CV CV_assign_JExpr(JExpr jexpr) throws IOException {

    //ensure jdb access.
    dbg.ensureJDBContext();
    DebuggerContext c =  ensureDebuggerContext();
    JavaCallFrame jframe = c.getCurrentJavaFrame();

    // ask the jdb to evalute the RHS, and store the result into the Blink agent
    // variable table. For the type of the RHS, we rely on the JDB's method
    // overloading. Like the GDB, the JDB does not tell us an static expression
    // type without actually running the expression.
    String vjid = dbg.jdb.newConvenienceVariable(jframe, jexpr.getValueExpr());

    // Now ask the debug agent to tell us the JNI-equivalent type for the CV.
    String jniType = dbg.jdb.getConvenienceVariableJNIType(vjid);; 
    
    // Now, let the gdb to obtain the new CV value.
    NativeCallFrame nframe = c.getCurrentNativeFrame();
    dbg.ensureGDBContext();
    String jnienv = dbg.ensureJNIENV();
    String vcid = getNewCTmpVarIdentifier();
    dbg.ndb.setVariable(nframe,
        vcid, 
        "bda_get_cvalue_from_vj_" + jniType + "("+ jnienv + "," + vjid + ")");

    CV cv = new CV(vcid, jniType);
    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\t" + cv.getVcId() + "(" + cv.getType() + ") := "
          + jexpr.getValueExpr() + "\n");
    }
    return cv;
  }

  /**
   * Implements <VC> : = <CExpr>. 
   * 
   * @param cexpr The C expression
   * @return The cv variable. 
   */
  private CV CV_assign_CExpr(CExpr cexpr) throws IOException {

    // Now, let the gdb to obtain the new CV value.
    String vcid = getNewCTmpVarIdentifier();
    DebuggerContext c =  ensureDebuggerContext();
    NativeCallFrame nframe = c.getCurrentNativeFrame();
    dbg.ensureGDBContext();
    dbg.ndb.setVariable(nframe, vcid, cexpr.getValueExpr());

    //get the expected type of the RHS.
    String ctype = dbg.ndb.whatis(nframe, cexpr.getValueExpr());
    CV cv = new CV(vcid, ctype); 
    if (dbg.options.getVerboseExprEvaluation() >= 1) {
      dbg.out("\t" + cv.getVcId() + "(" + cv.getType() + ") := "
          + cexpr.getValueExpr() + "\n");
    }
    return cv;
  }

  /** Finish the expression evaluation */
  private final void cleanVJandVC() {

    //handle jdb part.
    try {
      dbg.ensureJDBContext();
      dbg.jdb.resetConvenienceVariables();
    } catch (IOException e) {
      dbg.err("could not successfully clean JDB temp varaibles.\n");
    }

    //handle gdb part.
    nextVCIdentifier = 0;
  }

  /** Ensure the debugger context is available at the return of this method. */
  private final DebuggerContext ensureDebuggerContext() throws IOException {
    if (debuggerContext != null ) {
      return debuggerContext;
    }

    // obtain the current debugger context.
    dbg.ensurePureContext();
    CallStack callStack = CallStack.extractCallStack(dbg,
        jeannieSymbolRemapper);
    debuggerContext = new DebuggerContext(callStack);
    return debuggerContext;
  }

  /** Clear the current debugger context. */
  private final void clearDebuggerContext() {
    debuggerContext = null;
  }

  /** Getter the langg property of the node. */
  private static Language getLanguage(final GNode n) {
    return (Language) n.getProperty("language");
  }

  /** Setter of the l-value marker of the node. */
  private static void setLValue(final GNode n) {
    n.setProperty("blink.lvalue", new Boolean("true"));
  }

  /** Getter of the l-value marker of the node. */
  private boolean isLValue(final GNode n) {
    Object o = n.getProperty("blink.lvalue");
    if (o instanceof Boolean ) {
      Boolean b = (Boolean)o;
      return b.booleanValue();
    } else {
      return false;
    }
  }

  /** The next CV unique identifier number. */
  private int nextVCIdentifier = 0;

  /** Allocate a new gdb meta variable name. */
  private String getNewCTmpVarIdentifier() {
    return "$vc" + nextVCIdentifier++;
  }

  /** do l-value sensitive indirection. */
  private CExpr eval(final GNode n, final CExpr e) throws IOException {
    if (isLValue(n)) {
      return e;
    } else {
      return CV_assign_CExpr(e);
    }
  }

  /** do l-value sensitive indirection. */
  private JExpr eval(final GNode n, final JExpr e) throws IOException {
    if (isLValue(n)) {
      return e;
    } else {
      return JV_assign_JExpr(e);
    }
  }

  /** Qoute a string. */
  private static String q(String s) {
    return "\"" + s + "\"";
  }

  /** Surround the straing with paranthesis. */
  private static String p(String s) {
    return "(" + s + ")";
  }

  /** join a number of expressions by inserting a string between them. */
  private static String join(String s, BExpr[] exprs) {
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < exprs.length;i++) {
      if (i == 0) {
        sb.append(exprs[i].getValueExpr());
      } else {
        sb.append(s).append(exprs[i].getValueExpr());
      }
    }
    return sb.toString();
  }

  /**
   * The Blink expression.
   */
  private static abstract class BExpr {
    final String val;
    BExpr(String val) {
      this.val = val;
    }
    /** The getter method for val. */
    String getValueExpr() {
      return val;
    }

    /** The string representation. */
    public String toString() {
      return getValueExpr();
    }
  }

  /** The Java expression. */
  private static class JExpr extends BExpr {

    /** The constructor. */
    JExpr(String val) {
      super(val); 
    }

    /** a number of utility methods. */
    static JExpr field(JExpr expr, String s) {
      return new JExpr("(" + expr.getValueExpr() + "." + s + ")");
    }
    static JExpr binop(JExpr expr0, String operator, JExpr expr1) {
      return new JExpr("(" + expr0.getValueExpr() + operator + expr1.getValueExpr() + ")");
    }
    static JExpr methodCall(JExpr expr0, String meth, JExpr[] args ) {
      return new JExpr(
        "(" + expr0.getValueExpr() + "." + meth + "(" + join(",", args) + "))"
      );
    }
    static JExpr arraySelect(JExpr exprArray, JExpr exprIndex) {
      return new JExpr(
        "(" + exprArray.getValueExpr() + "[" + exprIndex.getValueExpr() + "])"
      );
    }
  }

  /** The C Expression. */
  private static class CExpr extends BExpr {

    /** The constructor. */
    CExpr(String val) {
      super(val); 
    }
    /** a number of utility methods. */
    static CExpr address(CExpr expr) {
      return new CExpr("(&"+ expr.getValueExpr() + ")");
    }
    static CExpr indirect(CExpr expr) {
      return new CExpr("(*" + expr.getValueExpr() + ")");
    }
    static CExpr binop(CExpr expr0, String operator, CExpr expr1) {
      return new CExpr("(" + expr0.getValueExpr() + operator + expr1.getValueExpr() + ")");
    }
    static CExpr fieldDirect(CExpr expr, String comp) {
      return new CExpr("(" + expr.getValueExpr() + "." + comp + ")");
    }
    static CExpr funcCall(CExpr exprFunc, CExpr[] args ) {
      return new CExpr(exprFunc.getValueExpr() + "(" + join(",", args) + ")");
    }
    static CExpr arraySelect(CExpr exprArray, CExpr exprIndex) {
      return new CExpr(
        "(" + exprArray.getValueExpr() + "[" + exprIndex.getValueExpr() + "])"
      );
    }
  }

  /**
   * The Java psuduo variable inside the Blink.
   */
  private static class JV extends JExpr {
    private final String vjId;
    private final String type;

    /** Constructors. */
    JV(String vjId, String type, String expr) {
      super(expr);
      this.type = type;
      this.vjId = vjId;
    }

    /** Getter methods. */
    public String getVjID() {return vjId;}
    public String getType() {return type;}
  }

  /**
   * The C pseudo variable inside the Blink.
   */
  private static class CV extends CExpr {
    private final String vcId;
    private final String type;

    /** Constructors. */
    CV(String vcId, String type) {
      super("(" + vcId + ")");
      this.vcId = vcId;
      this.type = type;
    }

    /** Getter methods. */
    public String getType() {return type;}
    public String getVcId() {return vcId;}
  }
}
