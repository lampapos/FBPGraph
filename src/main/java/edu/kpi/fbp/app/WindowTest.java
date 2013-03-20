package edu.kpi.fbp.app;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WindowTest extends JFrame {
  private MigLayout mig;

  private JPanel contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          WindowTest frame = new WindowTest();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public WindowTest() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    mig = new MigLayout("", "[]", "[][]");
    contentPane.setLayout(mig);
    JButton btnAdd = new JButton("Add");
    btnAdd.addActionListener(new ActionListener() {
      int i = 2;
      
      @Override
      public void actionPerformed(ActionEvent e) {
        contentPane.add(new JLabel("O_o"), "cell 0 " + i);
        i++;
        contentPane.revalidate();
        contentPane.repaint();
      }
    });
    contentPane.add(btnAdd, "cell 0 1");
    //JButton button2 = new JButton("b2");
    //contentPane.add(bu)
  }

}
