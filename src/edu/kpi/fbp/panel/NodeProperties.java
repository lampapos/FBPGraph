package edu.kpi.fbp.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.sample.network.Generator;
import edu.kpi.fbp.sample.network.PrintResult;
import edu.kpi.fbp.sample.network.Summator;

public class NodeProperties {
  mxGraphComponent work;

  /**
   * Set true to debag options;
   */
  boolean D = false;

  public NodeProperties(mxGraphComponent g) {
    work = g;
  }

  public JPanel generateJP(final ArrayList<Node> nodes, final int iter) {
    JPanel P = new JPanel();
    com.jpmorrsn.fbp.engine.Component target = null;

    if (iter != -1) {

      switch (nodes.get(iter).name) {
      case "Generator":
        target = new Generator();
        break;
      case "Summator":
        target = new Summator();
        break;
      case "PrintResult":
        target = new PrintResult();
        break;
      }

      P.setLayout(new BoxLayout(P, BoxLayout.Y_AXIS));
      P.add(new JLabel("Name:"));
      final JTextField jtfN = new JTextField(nodes.get(iter).name);
      jtfN.setAlignmentX(Component.LEFT_ALIGNMENT);
      jtfN.setMinimumSize(new Dimension(150, 20));
      jtfN.setMaximumSize(new Dimension(150, 20));
      jtfN.setEditable(false);
      P.add(jtfN);

      String[] colors = { "black", "red", "green", "blue", "white", "default" };
      final JComboBox<String> jcb = new JComboBox<String>(colors);
      jcb.setAlignmentX(Component.LEFT_ALIGNMENT);
      jcb.setMinimumSize(new Dimension(150, 20));
      jcb.setMaximumSize(new Dimension(150, 20));
      P.add(new JLabel("Color:"));
      P.add(jcb);

      P.add(new JLabel("Number of ports in:"));
      final JTextField jtfPi = new JTextField("" + ComponentDescriptor.getInputPorts(target.getClass()).size());
      jtfPi.setEditable(false);
      jtfPi.setAlignmentX(Component.LEFT_ALIGNMENT);
      jtfPi.setMinimumSize(new Dimension(150, 20));
      jtfPi.setMaximumSize(new Dimension(150, 20));
      P.add(jtfPi);

      P.add(new JLabel("Number of ports out:"));
      final JTextField jtfPo = new JTextField("" + ComponentDescriptor.getOutputPorts(target.getClass()).size());
      jtfPo.setEditable(false);
      jtfPo.setAlignmentX(Component.LEFT_ALIGNMENT);
      jtfPo.setMinimumSize(new Dimension(150, 20));
      jtfPo.setMaximumSize(new Dimension(150, 20));
      P.add(jtfPo);

      P.add(new JLabel("Priority:"));
      final JTextField prior = new JTextField("" + ComponentDescriptor.getComponentPriority(target.getClass()));
      prior.setEditable(false);
      prior.setAlignmentX(Component.LEFT_ALIGNMENT);
      prior.setMinimumSize(new Dimension(150, 20));
      prior.setMaximumSize(new Dimension(150, 20));
      P.add(prior);

      P.add(new JLabel("Must run: " + ComponentDescriptor.isComponentMustRun(target.getClass())));

      P.add(new JLabel("Hint:"));
      JScrollPane hint = new JScrollPane(new JTextField(ComponentDescriptor.getComponentDescription(target.getClass())));
      hint.setAlignmentX(Component.LEFT_ALIGNMENT);
      hint.setMinimumSize(new Dimension(150, 260));
      hint.setMaximumSize(new Dimension(150, 260));
      P.add(hint);

      JButton jb = new JButton("Submit");
      jb.setAlignmentX(Component.LEFT_ALIGNMENT);
      jb.setMinimumSize(new Dimension(150, 20));
      jb.setMaximumSize(new Dimension(150, 20));
      jb.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

          String col = jcb.getSelectedItem().toString();
          if (col.equals("default")) {
            work.getGraph().setCellStyle(null, new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "";
          } else {
            work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, col, new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = col;
          }
        }
      });
      P.add(jb);

    } else {
      P.setLayout(null);
      JTextField t = new JTextField("Please select cell.");
      t.setEditable(false);
      t.setHorizontalAlignment(JTextField.CENTER);
      t.setBounds(0, 0, 150, 490);
      P.add(t);
    }

    return P;
  }
}
