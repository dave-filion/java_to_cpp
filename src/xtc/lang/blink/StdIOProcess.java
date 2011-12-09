package xtc.lang.blink;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import xtc.lang.blink.Event.BlinkEventSource;
import xtc.lang.blink.Event.DeathEvent;
import xtc.lang.blink.Event.RawTextMessageEvent;

/**
 * A process standard input/output handler. This handler is an interface between
 * the Blink debugger and its sub-process such as debugee, JDB and GDB. This
 * handler creates two threads to monitor the process stdout and stderr streams.
 */
abstract class StdIOProcess implements BlinkEventSource {

  /** For DOS/Window EOL (\r\n), we need to delete '\r' character. */
  private static final boolean needDOSEOLProcessing = System.getProperty("os.name")
      .startsWith("Windows");

  /** If the current system uses 2 bytes DOS EOL (\r\n), remove '\r'. */
  private static int read(BufferedReader br, char[] buf) throws IOException {
    int nRead = br.read(buf);
    if (nRead < 0) {return nRead;}
    int nEffectiveRead;
    if (needDOSEOLProcessing) {
      nEffectiveRead=0;
      for(int i=0;i < nRead;i++){
        if (buf[i] != '\r') {
          if (nEffectiveRead == i){
            nEffectiveRead++;
          } else {
            assert nEffectiveRead <= i;
            buf[nEffectiveRead++] = buf[i];
          }
        }
      }
    } else {
      nEffectiveRead = nRead;
    }
    return nEffectiveRead;
  }
  
  /** The Blink debugger. */
  protected final Blink dbg;

  /** The name of this handler. */
  private final String name;

  /** The command line to create the native process. */
  private String[] commandArray;

  /** The native process. */
  private Process process;

  /** The buffered writer to the process. */
  private BufferedWriter out;

  /** The thread group for the reader threads. */
  private final ThreadGroup tg; 
  
  /** A reader thread for the process output message. */
  private final Thread stdOutReader;

  /** A reader thread for the process error message. */
  private final Thread stdErrReader;

  /** The output stream log for debugging purpose. */
  private final BoundedLogQueue logStdOut;

  /** The error stream log for debugging purpose. */
  private final BoundedLogQueue logStderr;

  /**
   * @param name The name of the slave process.
   * @param commandArray The command array to create the slave process.
   */
  StdIOProcess(Blink dbg, String name) {
    this.dbg = dbg;
    this.name = name;
    this.logStdOut = 
      new BoundedLogQueue(4 * 1024);
    this.logStderr = 
      new BoundedLogQueue(4 * 1024);
    // initiate reader two threads for stdout and stderr
    this.tg = new ThreadGroup(getEventSourceName());
    stdOutReader = new Thread(tg, "stdout reader for " + getEventSourceName()) {
      public void run() {monitorStdOut();}
    };
    stdErrReader = new Thread(tg, "stderr reader for " + getEventSourceName()) {
      public void run() { monitorStdErr();}
    };
  }

  /** Getter method for the event source name. */
  public String getEventSourceName() {
    return name;
  }

  /**
   * Start the two monitoring threads.
   * 
   * @param cmdArray The micro debugger command.
   */
  protected void begin(String[] cmdArray) {
    this.commandArray = cmdArray;
    try {
      process = Runtime.getRuntime().exec(commandArray);
      OutputStream os = process.getOutputStream();
      OutputStreamWriter ow = new OutputStreamWriter(os);
      out = new BufferedWriter(ow);
    } catch (IOException e) {
      dbg.err("could not correctly run " + commandArray + "\n");
      e.printStackTrace();
      return;
    }
    assert process != null;

    stdOutReader.start();
    stdErrReader.start();
  }

  /**
   * Monitor the micro debugger stdout, and enque the micro debugger message
   * event.
   */
  private void monitorStdOut() {
    InputStream is = process.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    char[] buf = new char[1024];

    try {
      int nRead = read(br,buf);
      while (nRead > 0) {
        if (dbg.options.getVerboseLevel() >= 1) {
          String m=new String(buf, 0, nRead);
          dbg.out(m);
        }
        logStdOut.log(buf, 0, nRead);
        dbg.logQueue.log(buf, 0, nRead);
        RawTextMessageEvent e = new RawTextMessageEvent(this, buf, 0, nRead);

        dbg.enqueEvent(e);
        processMessageEvent(e);
        nRead = read(br,buf); // possibly blocking
      }
      assert nRead == -1; // EOF
    } catch (IOException e) {
      if (dbg.options.getVerboseLevel() >= 0) {
        dbg.err(name + ": " + " got an IO Exception in reading stderr, \n");
        dbg.err(name + ": " + " and finishing message reading thread\n");
      }
    }

    // wait until two reader threads are dead.
    try {stdErrReader.join();} catch(InterruptedException e) {}

    // notify my death.
    dbg.enqueEvent(new DeathEvent(this));
  }

  /**
   * Read error messages from the managed process, and show the error message 
   * to the user.
   */
  private void monitorStdErr() {
    InputStream is = process.getErrorStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    char[] buf = new char[1024];
    try {
      int nRead = read(br,buf);
      while (nRead > 0) {
        String m=new String(buf, 0, nRead);
        dbg.err(m);
        logStderr.log(buf, 0, nRead);
        nRead = read(br,buf); // possibly blocking
      }
      assert nRead == -1; // EOF
    } catch (IOException e) {
      if (dbg.options.getVerboseLevel() >= 0) {
        dbg.err(name + ": " + " got an IO Exception in reading stdout, ");
        dbg.err(name + ": " + " and finishing message reading thread\n");
      }
    }
  }

  /**
   * Internally process the raw message to generate macro event.
   * 
   * @param e The raw text message event.
   */
  abstract void processMessageEvent(RawTextMessageEvent e);
   

  /**
   * Send a message to the output stream.
   * 
   * @param msg The message.
   */
  public void sendMessage(String msg) throws IOException {
    if (dbg.options.getVerboseLevel() >= 1) {
      dbg.out(msg);
    }
    dbg.logQueue.log(msg);
    out.write(msg);
    out.flush();
  }

  /**
   * Get the last output message from this debugger. 
   * @return The message.
   */
  public String getLastOutputMessage() {
    return logStdOut.getLastTrace();
  }

}
