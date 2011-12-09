package xtc.lang.blink;

import java.util.Stack;

import xtc.lang.JavaEntities;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Runtime;
import xtc.lang.jeannie.Utilities;

public final class CommandAstAnalyzer extends Visitor {
  enum Language { C, JAVA, DEBUGGER };
  
  private static class MiniVisitor_allNodesHaveLanguage extends Visitor {
    public final void visit(final Node n) {
      assert null != CommandAstAnalyzer.getNodeLanguage(n) : n.toString();
      for (int i=0; i<n.size(); i++)
        if (n.get(i) instanceof Node)
          dispatch(n.getNode(i));
    }
  }

  public static void assertAllNodesHaveLanguage(final Node n) {
    new MiniVisitor_allNodesHaveLanguage().dispatch(n);
  }

  public static Language getNodeLanguage(final Node n) {
    return (Language) n.getProperty("language");
  }

  public static Object getNodeVariableRemap(final Node n) {
    return (Object) n.getProperty("variableRemap");
  }

  private static Language setNodeLanguage(final Node n, final Language language) {
    n.setProperty("language", language);
    return language;
  }

  final Runtime _runtime;
  private final Stack<Language> _stack;

  public CommandAstAnalyzer(final Runtime runtime) {
    assert null != runtime ;
    _runtime = runtime;
    _stack = new Stack<Language>();
    _stack.push(Language.DEBUGGER);
  }

  private boolean assrt(final Node n, final boolean cond, final String msgFormat, final Object... msgArgs) {
    return JavaEntities.runtimeAssrt(_runtime, n, cond, msgFormat, msgArgs);
  }
  
  private final Language currentLanguage() {
    return _stack.peek();
  }

  private final Object dispatchInLanguage(final Node n, final Language language) {
    enterLanguage(language);
    final Object result = dispatch(n);
    exitLanguage(language);
    return result;
  }
  
  private final void enterLanguage(final Language language) {
    _stack.push(language);
  }
  
  private final void exitLanguage(final Language language) {
    assert currentLanguage() == language;
    _stack.pop();
  }
  
  private boolean setAndAssrtLanguage(final Node n, final Language language) {
    setNodeLanguage(n, language);
    return assrt(n, language == currentLanguage(), "expected language %s, current %s", language, currentLanguage());
  }
  
  public final void visit(final GNode n) {
    assrt(n, false, "the Jeannie debugger does not support the %s feature", n.getName());
  }
  
  /** Visit an AdditiveExpression = Expression ("+" / "-") Expression. */
  public final void visitAdditiveExpression(final GNode n) {
    final Language curr = currentLanguage();
    if (assrt(n, curr == Language.C || curr == Language.JAVA, "expected language C or Java, current %s", curr)) {
      setNodeLanguage(n, curr);
      dispatch(n.getNode(0));
      dispatch(n.getNode(2));
    }
  }

  /** Visit an AddressExpression = Expression. */
  public final void visitAddressExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C))
      dispatch(n.getNode(0));
  }

  /** Visit Arguments = Expression*. */
  public final void visitArguments(final GNode n) {
    if (setAndAssrtLanguage(n, Language.JAVA))
      for (int i=0; i<n.size(); i++)
        dispatch(n.getNode(i));
  }

  /** Visit an AssignmentExpression = Expression ("=" / "+=" / "-=" / "*=" / "/=" / "%=" / "&lt;&lt;=" / "&gt;&gt;=" / "&amp;=" / "^=" / "|=") Expression. */
  public final void visitAssignmentExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C)) {
      dispatch(n.getNode(0));
      dispatch(n.getNode(2));
    }
  }

  /** Visit a BreakClassCommand = QualifiedIdentifier LineNumber. */
  public final void visitBreakClassCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER)) {
      @SuppressWarnings("unused")
      final String className = (String) dispatchInLanguage(n.getNode(0), Language.JAVA);
    }
  }

  /** Visit a BreakClassCommand = QualifiedIdentifier. */
  public final void visitBreakClassMethodCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER)) {
      @SuppressWarnings("unused")
      final String className = (String) dispatchInLanguage(n.getNode(0), Language.JAVA);
    }
  }

  /** Visit a BreakFileCommand = FileName LineNumber. */
  public final void visitBreakFileCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a BreakFileCommand = FileName LineNumber. */
  public final void visitBreakFunctionCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a C2jCommand = (no children). */
  public final void visitC2jCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a CallExpression = [Expression] null MethodName Arguments. */
  public final void visitCallExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.JAVA)) {
      if (null != n.get(0))
        dispatch(n.getNode(0));
      @SuppressWarnings("unused")
      final String methodName = n.getString(2);
      dispatch(n.getNode(3));
    }
  }

  /** Visit a CInJavaExpression = C.UnaryExpression. */
  public final void visitCInJavaExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.JAVA))
      dispatchInLanguage(n.getNode(0), Language.C);
  }

  /** Visit a ContinueCommand = (no children). */
  public final void visitContinueCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a DeleteCommand = IntegerLiteral. */
  public final void visitDeleteCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n.getNode(0), Language.JAVA);
  }

  /** Visit a DirectComponentSelection = Expression String. */
  public final void visitDirectComponentSelection(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C))
      dispatch(n.getNode(0));
  }

  /** Visit a DownCommand = IntegerLiteral. */
  public final void visitDownCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n.getNode(0), Language.JAVA);
  }

  /** Visit a EqualityExpression = Expression ("==" / "!=") Expression. */
  public final void visitEqualityExpression(final GNode n) {
    final Language curr = currentLanguage();
    if (assrt(n, curr == Language.C || curr == Language.JAVA, "expected language C or Java, current %s", curr)) {
      setNodeLanguage(n, curr);
      dispatch(n.getNode(0));
      dispatch(n.getNode(2));
    }
  }
  
  /** Visit a ExitCommand = (no children). */
  public final void visitExitCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a Expression = Expression ("=" / "+=" / "-=" / "*=" / "/=" / "&amp;=" / "|=" / "^=" / "%=" / "&lt;&lt;=" / "&gt;&gt;=" / "&gt;&gt;&gt;=") Expression. */
  public final void visitExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.JAVA)) {
      dispatch(n.getNode(0));
      dispatch(n.getNode(2));
    }
  }

  /** Visit ExpressionList = Expression+. */
  public final void visitExpressionList(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C))
      for (int i=0; i<n.size(); i++)
        dispatch(n.getNode(i));
  }

  /** Visit a FunctionCall = Expression [ExpressionList]. */
  public final void visitFunctionCall(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C)) {
      dispatch(n.getNode(0));
      if (null != n.get(1))
        dispatch(n.getNode(1));
    }
  }

  /** Visit a GdbCommand = RestOfLine. */
  public final void visitGdbCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a HelpCommand = (no children). */
  public final void visitHelpCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit an IndirectionExpression = Expression. */
  public final void visitIndirectionExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C))
      dispatch(n.getNode(0));
  }

  /** Visit a InfoBreakCommand = (no children). */
  public final void visitInfoBreakCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a InfoWatchCommand = (no children). */
  public final void visitInfoWatchCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit an InitJCommand = (no children). */
  public final void visitInitJCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit an IntegerConstant = String. */
  public final void visitIntegerConstant(final GNode n) {
    setAndAssrtLanguage(n, Language.C);
  }
  
  /** Visit an IntegerLiteral = String. */
  public final void visitIntegerLiteral(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
  }
  
  /** Visit a J2cCommand = (no children). */
  public final void visitJ2cCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a JavaInCExpression = Java.UnaryExpression. */
  public final void visitJavaInCExpression(final GNode n) {
    if (setAndAssrtLanguage(n, Language.C))
      dispatchInLanguage(n.getNode(0), Language.JAVA);
  }
  
  /** Visit a JdbCommand = RestOfLine. */
  public final void visitJdbCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit a JRetCommand = (no children). */
  public final void visitJRetCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a ListCommand = (no children). */
  public final void visitListCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit a LocalsCommand = (no children). */
  public final void visitLocalsCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit a MetaVariable = Identifier. */
  public final void visitMetaVariable(final GNode n) {
    setNodeLanguage(n, Language.DEBUGGER);
    final Language curr = currentLanguage();
    assrt(n, curr == Language.C || curr == Language.JAVA, "expected language C or Java, current %s", curr);
  }

  /** Visit a NextCommand = (no children). */
  public final void visitNextCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a RunCommand = (no children). */
  public final void visitRunCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit an StringLiteral = (no children). */
  public final void visitNullLiteral(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
  }
  
  /** Visit a PrimaryIdentifier = String. */
  public void visitPrimaryIdentifier(final GNode n) {
    final Language curr = currentLanguage();
    if (assrt(n, curr == Language.C || curr == Language.JAVA, "expected language C or Java, current %s", curr)) {
      setNodeLanguage(n, curr);
    }
  }
  
  /** Visit a PrintCExpressionCommand = C.Expression. */
  public final void visitPrintCExpressionCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n.getNode(0), Language.C);
  }

  /** Visit a PrintJavaExpressionCommand = Java.Expression. */
  public final void visitPrintJavaExpressionCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n.getNode(0), Language.JAVA);
  }

  /** Visit a QualifiedIdentifier = Identifier+. */
  public final String visitQualifiedIdentifier(final GNode n) {
    if (!setAndAssrtLanguage(n, Language.JAVA))
      return null;
    return Utilities.qualifiedIdentifierToString(n);
  }

  /** Visit a SelectionExpression = Expression Identifier. */
  public final void visitSelectionExpression(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
    dispatch(n.getNode(0));
    //dispatch(n.getNode(1)); // BK - this is string.
  }
  
  /** Visit a StatCommand = (no children). */
  public final void visitStatCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }

  /** Visit a StepCommand = (no children). */
  public final void visitStepCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
  
  /** Visit an StringConstant = String. */
  public final void visitStringConstant(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
  }
  
  /** Visit an StringLiteral = String. */
  public final void visitStringLiteral(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
  }
  
  /** Visit a SubscriptExpression = Expression Expression. */
  public final void visitSubscriptExpression(final GNode n) {
    final Language curr = currentLanguage();
    if (assrt(n, curr == Language.C || curr == Language.JAVA, "expected language C or Java, current %s", curr)) {
      setNodeLanguage(n, curr);
      dispatch(n.getNode(0));
      dispatch(n.getNode(1));
    }
  }
  
  /** Visit a ThisExpression = [Expression]. */
  public final void visitThisExpression(final GNode n) {
    setAndAssrtLanguage(n, Language.JAVA);
    assrt(n, null == n.get(0), "the Jeannie debugger does not support qualified this in Java expressions");
  }
  
  /** Visit an UpCommand = IntegerLiteral. */
  public final void visitUpCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n.getNode(0), Language.JAVA);
  }

  /** Visit a WatchCExpressionCommand = C.Expression. */
  public final void visitWatchCExpressionCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER))
      dispatchInLanguage(n, Language.C);
  }
  
  /** Visit a WatchJavaFieldCommand = [WatchKind] QualifiedIdentifier. */
  public final void visitWatchJavaFieldCommand(final GNode n) {
    if (setAndAssrtLanguage(n, Language.DEBUGGER)) {
      @SuppressWarnings("unused")
      final String className = (String) dispatchInLanguage(n.getNode(1), Language.JAVA);
    }
  }
  
  /** Visit a WhereCommand = (no children). */
  public final void visitWhereCommand(final GNode n) {
    setAndAssrtLanguage(n, Language.DEBUGGER);
  }
}
