package xtc.lang.blink;

import java.io.IOException;
import java.util.List;

import xtc.lang.blink.CallStack.NativeCallFrame;
import xtc.lang.blink.CallStack.LocalVariable;
import xtc.lang.blink.Event.BlinkEventSource;
import xtc.lang.blink.SymbolMapper.SourceFileAndLine;

/**
 * The exported feature from the native debugger controlled under the Blink.
 */
public interface NativeDebugger extends BlinkEventSource {

  /** Transition type between Java and native code. */
  static enum LanguageTransitionEventType {
    J2C_CALL, 
    J2C_RETURN, 
    C2J_CALL, 
    C2J_RETURN;
  }

  /** attaching and detaching. */
  public void attach(int pid) throws IOException;
  public void detach() throws IOException;
  public void quit() throws IOException;

  /** user level breakpoint and continuing. */
  public int createBreakpoint(String sourceFile, int line) throws IOException;
  public int createBreakpoint(String symbol) throws IOException;
  public void enableBreakpoint(int bpid) throws IOException;
  public void disableBreakpoint(int bpid) throws IOException;
  public void deleteBreakpoint(int bpid) throws IOException;
  
  /** Enable and disable the internal transition break point. */
  public int getLanguageTransitionCount() throws IOException;
  public void setTransitionBreakPoint(LanguageTransitionEventType bptype, int transitionCount)
    throws IOException;
  public void clearTransitionBreakPoint(LanguageTransitionEventType bptype)
    throws IOException;

  /** For transitions */
  public void callNative2Java() throws IOException;

  /** Continue the debuggee's execution. */
  public void cont() throws IOException;

  public void callJavaDummy() throws IOException;
  
  /** Calling context. */
  public List<NativeCallFrame> getFrames() throws IOException;
  /** Get current location. */
  public SourceFileAndLine getCurrentLocation() throws IOException;
  public String getSourceLines(String filename, int line, int count) throws IOException;
  public List <LocalVariable> getLocals(NativeCallFrame f) throws IOException;

  /** Stepping */
  public void step() throws IOException;
  public void next() throws IOException;
  
  /** Inspecting the memory. */
  public String getJNIEnv() throws IOException;
  public String eval(NativeCallFrame f, String expr) throws IOException;
  public void setVariable(NativeCallFrame f, String name, String expr) throws IOException;

  /** Obtain the type of the C expression including convenience variable. */
  public String whatis(NativeCallFrame f, String expr) throws IOException;

  /** Run the native debugger specific raw command line, and return the response. */
  public String runCommand(String command) throws IOException;

  /** Perform debugger's internal event handling. */
  public void dispatch(Event e);
  
  /** Get the logging message. */
  public String getLastOutputMessage();
}
