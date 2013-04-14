package edu.kpi.fbp.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;

import edu.kpi.fbp.gui.primitives.Node;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParameterType;

/**
 * Panel which include all component attribute.
 * @author Cheshire
 *
 */
public class AttributeTab {
  /** {@link AttributeTab}. */
  private final JPanel attributePanel;
  /** Links needed to read attribute from panel. */
  private ArrayList<JTextField> attributeField;
  /** Panel sizes. */
  private static final int PANEL_WIDTH = 150;

  /** {@link AttributeTab}. */
  public AttributeTab() {
    attributePanel = new JPanel();
    attributePanel.setBackground(Color.white);
    attributePanel.setLayout(new MigLayout("", "[::50px][grow]", "[]"));
    attributePanel.setMaximumSize(new Dimension(PANEL_WIDTH, Integer.MAX_VALUE));

    createAttributesPanel(null);
  }

  /** @return null if attribute value not changed, else - return new value. */
  private String isNewValueSet(final Node node, final String paramName) {
    for (final Parameter newParam : node.getNewAttributes()) {
      if (newParam.getName().equals(paramName)) {
        return newParam.getValue();
      }
    }
    return null;
  }

  /** @return attribute panel. */
  public JPanel getAttributePanel() {
    return attributePanel;
  }

  /** Submit button event. */
  private void submitAction(final Node node) {
    final List<Parameter> bufArray = new ArrayList<Parameter>();
    boolean errorFlag = false;

    for (int i = 0; i < attributeField.size(); i++) {

      switch (node.getAttributes().get(i).type()) {
        case BOOLEAN:
          if (!attributeField.get(i).getText().equals(node.getAttributes().get(i).defaultValue())) {
            bufArray.add(new Parameter(node.getAttributes().get(i).name(), attributeField.get(i).getText()));
          }
        break;
        case INTEGER:
          try {
            Integer.parseInt(attributeField.get(i).getText());
            if (!attributeField.get(i).getText().equals(node.getAttributes().get(i).defaultValue())) {
              bufArray.add(new Parameter(node.getAttributes().get(i).name(), attributeField.get(i).getText()));
            }
          } catch (final NumberFormatException ie) {
            attributeField.get(i).setText("Invalid format [INTEGER]");
            errorFlag = true;
          }
        break;
        case FLOAT:
          try {
            Integer.parseInt(attributeField.get(i).getText());
            if (!attributeField.get(i).getText().equals(node.getAttributes().get(i).defaultValue())) {
              bufArray.add(new Parameter(node.getAttributes().get(i).name(), attributeField.get(i).getText()));
            }
          } catch (final NumberFormatException ie) {
            attributeField.get(i).setText("Invalid format [FLOAT]");
            errorFlag = true;
          }
        break;
        case STRING:
          if (!attributeField.get(i).getText().equals(node.getAttributes().get(i).defaultValue())) {
            bufArray.add(new Parameter(node.getAttributes().get(i).name(), attributeField.get(i).getText()));
          }
        break;
        default:
        break;
      }
    }

    if (!errorFlag) {
      node.setNewAttributes(bufArray);
    }

  }

  /** Show new node attribute fields.
   * @param node - new node.
   */
  public void createAttributesPanel(final Node node) {

    attributePanel.removeAll();

    if (node != null) {
      String[] splitBuf;

      if (node.getAttributes().size() > 0) {
        attributeField = new ArrayList<JTextField>();

        int i = 0;
        for (final ComponentParameter comParam : node.getAttributes()) {

          //Component name
          final JLabel bufLabel = new JLabel(comParam.name());
          attributePanel.add(bufLabel, "cell 0 " + i);

          //Component value
          String value = comParam.defaultValue();
          final String newValue = isNewValueSet(node, comParam.name());
          if (newValue != null) {
            value = newValue;
          }
          final JTextField bufText = new JTextField(value);
          attributeField.add(bufText);

          //Component type
          if (comParam.type() == ParameterType.BOOLEAN) {
            final JPanel bufPanel = new JPanel();
            final JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(Boolean.getBoolean(value));
            checkBox.addChangeListener(new ChangeListener() {

              @Override
              public void stateChanged(final ChangeEvent e) {
                bufText.setText("" + checkBox.isSelected());
              }
            });
            bufPanel.add(checkBox);

            bufText.setVisible(false);
            bufPanel.add(bufText);

            attributePanel.add(bufPanel, "cell 1 " + i + ",grow");
          } else {

            attributePanel.add(bufText, "cell 1 " + i + ",grow");

          }

          i++;
        }

        if (node.getComponentDescriptor().getInPorts().size() > 0) {
          attributePanel.add(new JLabel("In ports name, type:"), "cell 0 " + i + ",grow");
          i++;
          for (final InPort inPort : node.getComponentDescriptor().getInPorts()) {
            attributePanel.add(new JLabel(inPort.value()), "cell 0 " + i + ",grow");
            splitBuf = inPort.type().toString().split("\\.");
            attributePanel.add(new JLabel(splitBuf[splitBuf.length - 1]), "cell 1 " + i + ",grow");
            i++;
          }
        }
        if (node.getComponentDescriptor().getOutPorts().size() > 0) {
          attributePanel.add(new JLabel("Out ports name, type:"), "cell 0 " + i + ",grow");
          i++;
          for (final OutPort outPort : node.getComponentDescriptor().getOutPorts()) {
            attributePanel.add(new JLabel(outPort.value()), "cell 0 " + i + ",grow");
            splitBuf = outPort.type().toString().split("\\.");
            attributePanel.add(new JLabel(splitBuf[splitBuf.length - 1]), "cell 1 " + i + ",grow");
            i++;
          }
        }

        //Submit button
        final JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(final ActionEvent e) {
            submitAction(node);
          }

        });
        final JPanel placeHolder = new JPanel();
        placeHolder.setBackground(Color.white);
        attributePanel.add(placeHolder, "cell 0 " + i + " 1 " + i + ", grow, push, span");
        i++;
        attributePanel.add(submit, "cell 0 " + i + " 1 " + i + ", grow, span");


      } else {
        attributePanel.add(new JLabel("This component haven't"), "cell 0 0 1 0");
        attributePanel.add(new JLabel("parameters."), "cell 0 1 1 1");

        int i = 2;
        if (node.getComponentDescriptor().getInPorts().size() > 0) {
          attributePanel.add(new JLabel("In ports name, type:"), "cell 0 " + i + ",grow");
          i++;
          for (final InPort inPort : node.getComponentDescriptor().getInPorts()) {
            attributePanel.add(new JLabel(inPort.value()), "cell 0 " + i + ",grow");
            splitBuf = inPort.type().toString().split("\\.");
            attributePanel.add(new JLabel(splitBuf[splitBuf.length - 1]), "cell 1 " + i + ",grow");
            i++;
          }
        }
        if (node.getComponentDescriptor().getOutPorts().size() > 0) {
          attributePanel.add(new JLabel("Out ports name, type:"), "cell 0 " + i + ",grow");
          i++;
          for (final OutPort outPort : node.getComponentDescriptor().getOutPorts()) {
            attributePanel.add(new JLabel(outPort.value()), "cell 0 " + i + ",grow");
            splitBuf = outPort.type().toString().split("\\.");
            attributePanel.add(new JLabel(splitBuf[splitBuf.length - 1]), "cell 1 " + i + ",grow");
            i++;
          }
        }

      }
    } else {
      attributePanel.add(new JLabel("Nothing choose."), "cell 0 0 1 0");
    }

    attributePanel.revalidate();
    attributePanel.repaint();

  }
}
