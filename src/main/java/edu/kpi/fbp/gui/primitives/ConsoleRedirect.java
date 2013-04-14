package edu.kpi.fbp.gui.primitives;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Utility class for console redirection.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public final class ConsoleRedirect implements Runnable {
  /** The pane which will show all console messages. */
  private final JTextArea displayPane;
  /** Reader which reads from system out. */
  private BufferedReader reader;

  private ConsoleRedirect(final JTextArea displayPane, final PipedOutputStream pos) {
      this.displayPane = displayPane;

      try {
          final PipedInputStream pis = new PipedInputStream(pos);
          reader = new BufferedReader(new InputStreamReader(pis));
      } catch (final IOException e) {
        e.printStackTrace();
      }
  }

  @Override
  public void run() {
      String line = null;

      try {
          while ((line = reader.readLine()) != null) {
              displayPane.append(line + "\n");
              displayPane.setCaretPosition(displayPane.getDocument().getLength());
          }

          System.err.println("im here");
      } catch (final IOException ioe) {
          JOptionPane.showMessageDialog(null, "Error redirecting output : " + ioe.getMessage());
      }
  }

  /**
   * @param displayPane the pane which will show all console messages
   */
  public static void redirectOut(final JTextArea displayPane) {
    final PipedOutputStream pos = new PipedOutputStream();
    System.setOut(new PrintStream(pos, true));

    final ConsoleRedirect console = new ConsoleRedirect(displayPane, pos);
    new Thread(console).start();
  }

  /**
   * @param displayPane the pane which will show all console messages
   */
  public static void redirectErr(final JTextArea displayPane) {
      final PipedOutputStream pos = new PipedOutputStream();
      System.setErr(new PrintStream(pos, true));

      final ConsoleRedirect console = new ConsoleRedirect(displayPane, pos);
      new Thread(console).start();
  }

  /**
   * @param displayPane the pane which will show all console messages
   */
  public static void redirectOutput(final JTextArea displayPane) {
    ConsoleRedirect.redirectOut(displayPane);
    ConsoleRedirect.redirectErr(displayPane);
  }


}
