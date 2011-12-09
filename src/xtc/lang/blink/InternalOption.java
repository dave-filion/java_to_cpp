package xtc.lang.blink;

import java.util.LinkedList;
import java.util.List;

/**
 * A Blink internal debugger options.
 */
class InternalOption {

  /** The library path for the agent. */
  private String agentLibrarypath;

  /** The verbose level */
  private int verboseLevel = 0;

  /** The initial blink command list to run when the Blink starts. */
  private final List<String> initialBlinkCommands = new LinkedList<String>();

  /** Jeannie expression evaluation specific verbose leve. */
  private int verboseExprEvaluation = 0;

  /** Check JNI misuse errors. */
  private boolean jniAssert = false;
  
  public final boolean isJniCheck() {
    return jniAssert ;
  }

  public final void setJniCheck(boolean jniCheck) {
    this.jniAssert = jniCheck;
  }

  /** Getter method for the VerboseLevel. */
  public int getVerboseLevel() {
    return verboseLevel;
  }

  /** Setter method for the VerboseLevel. */
  public void setVerboseLevel(int verboseLevel) {
    this.verboseLevel = verboseLevel;
  }

  /** Increase the verbose level. */
  public void moreVerbose() {
    this.verboseLevel++;
  }

  /** Decrease the verbose level. */
  public void lessVerbose() {
    this.verboseLevel--;
  }

  /**
   * Add a Blink user command to the initial command list.
   * 
   * @param cmd The command.
   */
  public void addInitialBlinkCommand(String cmd) {
    initialBlinkCommands.add(cmd);
  }

  /** Getter method for a list of the intial blink commands. */
  public List<String> getInitialBlinkCommandList() {
    return initialBlinkCommands;
  }

  /** Getter method for verboseExprEvaluation. */
  public int getVerboseExprEvaluation() {
    return verboseExprEvaluation;
  }

  /** Setter method for verboseExprEvaluation. */
  public void setVerboseExprEvaluation(int verboseExprEvaluation) {
    this.verboseExprEvaluation = verboseExprEvaluation;
  }

  /** Getter method for agentLibrarypath. */
  public String getAgentLibrarypath() {
    return agentLibrarypath;
  }

  /** Setter method for agentLibrarypath. */
  public void setAgentLibrarypath(String agentLibrarypath) {
    this.agentLibrarypath = agentLibrarypath;
  }
}
