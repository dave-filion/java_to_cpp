package xtc.lang.blink;

import java.io.IOException;

import xtc.lang.blink.Event.RawTextMessageEvent;
import xtc.lang.blink.agent.AgentCommandOptions;

/** A debuggee JVM class. */
public class DebugeeJVM extends StdIOProcess implements AgentCommandOptions {

  /** 
   * Constructor.
   * @param dbg The debugger.
   * @param name The name.
   */
  public DebugeeJVM(Blink dbg, String name) {
    super(dbg, name);
  }

  /**
   * Launch the debuggee JVM and return the JPDA listener address.
   * 
   * @param argument The arguments.
   * @param address The listener address.
   */
  public void beginDebugSession(String argument, String address) throws IOException {

    final String transport;
    if (System.getProperty("os.name").startsWith("Windows")) {
      transport="dt_shmem";
    } else {
      transport="dt_socket";
    }

    final StringBuffer agentOptions = new StringBuffer();
    agentOptions.append("=bia=y");
    if (dbg.options.isJniCheck()) {
      agentOptions.append("," + JNICHECK + "=y");
    } else {
    	agentOptions.append("," + JNICHECK + "=n");
    }

    final String jvmCommand = "java " 
      + "-Xdebug -agentlib:jdwp=server=n,suspend=y" 
      + ",transport=" + transport  
      + ",address=" + address + " "
      + "-agentpath:" + Blink.ensureAgentLibraryPath() 
      + agentOptions.toString()
      + " " + argument;

    if (dbg.options.getVerboseLevel() >= 1) {
      dbg.out("executing: " + jvmCommand + "\n");
    }
    begin(jvmCommand.split("\\s+"));;
  }

  /**
   * Internally process the raw message to generate macro event.
   * 
   * @param e The raw text message event.
   */
  void processMessageEvent(RawTextMessageEvent e) {
    dbg.out(e.getMessage()); // bypass the message from the JVM.
  }
}
