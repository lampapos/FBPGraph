package edu.kpi.fbp.gui.primitives;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ConsoleRedirect implements Runnable{
  JTextArea displayPane;
  BufferedReader reader;

  private ConsoleRedirect(JTextArea displayPane, PipedOutputStream pos) {
      this.displayPane = displayPane;

      try
      {
          PipedInputStream pis = new PipedInputStream(pos);
          reader = new BufferedReader(new InputStreamReader(pis));
      }
      catch(IOException e) {
        e.printStackTrace();
      }
  }

  public void run() {
      String line = null;

      try {
          while ((line = reader.readLine()) != null) {
//            displayPane.replaceSelection( line + "\n" );
              displayPane.append(line + "\n");
              displayPane.setCaretPosition(displayPane.getDocument().getLength());
          }

          System.err.println("im here");
      } catch (IOException ioe) {
          JOptionPane.showMessageDialog(null, "Error redirecting output : " + ioe.getMessage());
      }
  }
  
  public static void redirectOut(JTextArea displayPane) {
    PipedOutputStream pos = new PipedOutputStream();
    System.setOut(new PrintStream(pos, true));

    ConsoleRedirect console = new ConsoleRedirect(displayPane, pos);
    new Thread(console).start();
  }
  
  public static void redirectErr(JTextArea displayPane) {
      PipedOutputStream pos = new PipedOutputStream();
      System.setErr(new PrintStream(pos, true));

      ConsoleRedirect console = new ConsoleRedirect(displayPane, pos);
      new Thread(console).start();
  }

  public static void redirectOutput(JTextArea displayPane) {
    ConsoleRedirect.redirectOut(displayPane);
    ConsoleRedirect.redirectErr(displayPane);
  }

  
}
