package edu.kpi.fbp.panel;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;

import edu.kpi.fbp.network.Connect;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.primitives.ParamTextField;

public class NodeParams {
  // FIXME: make all field private, plz
  mxGraphComponent work;
  Connect conDes;

  ArrayList<ParamTextField> textArray = new ArrayList<ParamTextField>();

  int iterator = -1;

  /**
   * Set true to debug options.
   */
  boolean debug = false;

  public NodeParams(final mxGraphComponent comp, final Connect con) {
    this.work = comp;
    this.conDes = con;
  }

  private JPanel generateParams(final Node target) {
    int height = 0;
    //java.util.List<ComponentParameter> targetParams = target.paramList;

    final JPanel res = new JPanel();
    res.setLayout(new BoxLayout(res, BoxLayout.Y_AXIS));

    for (int i = 0; i < target.localParams.size(); i++) {

      final JLabel name = new JLabel(target.localParams.get(i).name);
      name.setMinimumSize(new Dimension(150, 20));
      name.setMaximumSize(new Dimension(150, 20));
      height += 20;
      name.setAlignmentX(Component.LEFT_ALIGNMENT);
      res.add(name);

      final JTextField bufField = new JTextField();
      switch (target.localParams.get(i).type) {
        case "integer":
          bufField.setMinimumSize(new Dimension(150, 20));
          bufField.setMaximumSize(new Dimension(150, 20));
          height += 20;
          bufField.setAlignmentX(Component.LEFT_ALIGNMENT);
          bufField.setText(target.localParams.get(i).value);
          textArray.add(new ParamTextField(bufField, "integer"));
          res.add(bufField);
        break;
        case "float":
          bufField.setMinimumSize(new Dimension(150, 20));
          bufField.setMaximumSize(new Dimension(150, 20));
          height += 20;
          bufField.setAlignmentX(Component.LEFT_ALIGNMENT);
          bufField.setText(target.localParams.get(i).value);
          textArray.add(new ParamTextField(bufField, "float"));
          res.add(bufField);
        break;
        case "string":
          bufField.setMinimumSize(new Dimension(150, 60));
          bufField.setMaximumSize(new Dimension(150, 60));
          height += 60;
          bufField.setAlignmentX(Component.LEFT_ALIGNMENT);
          bufField.setText(target.localParams.get(i).value);
          textArray.add(new ParamTextField(bufField, "string"));
          res.add(bufField);
        break;
        case "boolean":
          final JPanel booleanPanel = new JPanel();
          booleanPanel.setPreferredSize(new Dimension(150, 20));
          height += 20;
          booleanPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
          booleanPanel.setLayout(new BoxLayout(booleanPanel, BoxLayout.X_AXIS));
          bufField.setEditable(false);
          bufField.setPreferredSize(new Dimension(120, 20));
          booleanPanel.add(bufField);
          final JCheckBox c = new JCheckBox();
          c.setPreferredSize(new Dimension(20, 20));
          c.addChangeListener(new ChangeListener() {

                  @Override
                  public void stateChanged(final ChangeEvent e) {
                      if (bufField.getText().equals("true")) {
                        bufField.setText("false");
                      }else{
                        bufField.setText("true");
                      }
                  }
              });

          booleanPanel.add(new Checkbox());
          textArray.add(new ParamTextField(bufField, "boolean"));
          res.add(booleanPanel);
        break;
      }
    }

    final JButton sabButton = new JButton("Submit");
    sabButton.setMinimumSize(new Dimension(60, 20));
    sabButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    sabButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        // TODO Auto-generated method stub
        for (int i = 0; i < textArray.size(); i++) {
          switch (textArray.get(i).type) {
            case "string":
              target.localParams.get(i).value = textArray.get(i).textField.getText();
            break;
            case "integer":
              try {
                final int test = Integer.parseInt(textArray.get(i).textField.getText());
                target.localParams.get(i).value = "" + test;
              } catch (final NumberFormatException ie) {
                  textArray.get(i).textField.setText("invalid format");
              }
            break;
            case "float":
              try {
                final float test = Float.parseFloat(textArray.get(i).textField.getText());
                target.localParams.get(i).value = "" + test;
              } catch (final NumberFormatException ie) {
                  textArray.get(i).textField.setText("invalid format");
              }
            break;
            case "boolean":
              target.localParams.get(i).value = textArray.get(i).textField.getText();
            break;
          }
        }
      }
    });
    res.add(sabButton);
    res.setMinimumSize(new Dimension(150, height));

    return res;
  }

  public JPanel generateJP(final ArrayList<Node> nodes, final int iter) {
    final JPanel res = new JPanel();

    Node target = null;

    this.iterator = iter;

    if (iter != -1) {
      //obtain a certain descriptor
    target = nodes.get(iter);//conDes.getComponentDescriptor("edu.kpi.fbp.network." + nodes.get(iter).name);

    //System.out.println(target.getParameters());

    res.setLayout(new BoxLayout(res, BoxLayout.Y_AXIS));

    if (target.localParams.size() > 0) {
      res.add(generateParams(target));
    }

    //Color panel
    final JPanel colorButtons = new JPanel();
    colorButtons.setLayout(new GridLayout(2, 4));

    final JButton red = new JButton();
    red.setBackground(Color.red);
    //red.setBounds(20, 0, 20, 20);
    red.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "red";
      }
    });

    final JButton pink = new JButton();
    pink.setBackground(Color.pink);
    //pink.setBounds(40, 0, 20, 20);
    pink.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "pink", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "pink";
      }
    });

    final JButton green = new JButton();
    green.setBackground(Color.green);
    //green.setBounds(0, 20, 20, 20);
    green.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "green", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "green";
      }
    });

    final JButton black = new JButton();
    black.setBackground(Color.black);
    //black.setBounds(60, 20, 20, 20);
    black.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "black", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "black";
      }
    });

    final JButton blue = new JButton();
    blue.setBackground(Color.blue);
    //blue.setBounds(20, 20, 20, 20);
    blue.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyle(null, new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "";
      }
    });

    final JButton orange = new JButton();
    orange.setBackground(Color.orange);
    //orange.setBounds(60, 0, 20, 20);
    orange.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "orange";
      }
    });

    final JButton magenta = new JButton();
    magenta.setBackground(Color.GRAY);
    //magenta.setBounds(40, 20, 20, 20);
    magenta.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "gray", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "gray";
      }
    });

    final JButton white = new JButton();
    white.setBackground(Color.white);
    //white.setBounds(0, 0, 20, 20);
    white.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "white", new Object[] { nodes.get(iter).cell });
            nodes.get(iter).color = "white";
      }
    });

    colorButtons.add(white);
    colorButtons.add(red);
    colorButtons.add(pink);
    colorButtons.add(orange);
    colorButtons.add(green);
    colorButtons.add(blue);
    colorButtons.add(magenta);
    colorButtons.add(black);

    colorButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
    colorButtons.setMinimumSize(new Dimension(150, 40));
    colorButtons.setMaximumSize(new Dimension(150, 40));

    //res.add(new JSeparator(SwingConstants.HORIZONTAL));
    res.add(colorButtons);
    //res.add(new JSeparator(SwingConstants.HORIZONTAL));
    }

    //res.setMinimumSize(new Dimension(150, 80));
    res.setAlignmentX(Component.LEFT_ALIGNMENT);

    return res;
  }
}
