package xtc.lang.blink;

/**
 * A bounded byte sequence log. This is to support for storing and showing the
 * most recent K-bytes messages.
 * 
 * @author Byeongcheol Lee
 */
public class BoundedLogQueue {

  /** The internal log buffer. */
  private final char[] log;

  /** The log index for the most recent byte. */
  private int head = -1;

  /** The log index for the least recent byte. */
  private int tail = -1;

  /** The constructor. */
  BoundedLogQueue(int capacity) {
    assert capacity > 0;
    this.log = new char[capacity];
  }

  /** put a character into the log. */
  private void put(char c) {
    if (length() == 0) {
      head = tail = 0;
      log[0] = c;
    } else {
      head = (head + 1) % log.length;
      if (tail == head) {
        tail = (tail + 1) % log.length;
      }
      log[head] = c;
    }
  }

  /** compute the message size in the log. */
  private int length() {
    if (head > tail) {
      return head - tail + 1;
    } else if (head < tail) {
      return (head + 1) + (log.length - tail);
    } else {
      if (head == -1 && tail == -1 ) {
        return 0;
      } else {
        assert head == 0 && tail == 0;
        return 1;
      }
    }
  }

  /**
   * Append a string message to the log.
   * 
   * @param msg The string message
   */
  public synchronized void log(String msg) {
    char[] buf = msg.toCharArray();
    log(buf, 0, buf.length);
  }

  /**
   * Append a byte array message to the log.
   * 
   * @param buf The buffer array.
   * @param index The beginning of the message.
   * @param len The length.
   */
  public synchronized void log(char[] buf, int index, int len) {
    assert index >= 0 && len >= 0;
    for(int i = 0;i < len;i++) {
      put(buf[index+i]);
    }
  }

  /** Get the last messages. */
  public synchronized String getLastTrace() {
    int len = length();
    char[] msg = new char[len];
    for(int i=0; i < len;i++) {
      msg[i] = log[(tail + i) % log.length];
    }
    return new String(msg);
  }
}
