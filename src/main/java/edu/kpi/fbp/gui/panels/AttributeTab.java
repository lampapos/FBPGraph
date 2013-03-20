package edu.kpi.fbp.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

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
  private JPanel attributePanel;
  /** Links needed to read attribute from panel. */
  private ArrayList<JTextField> attributeField;
  /** Panel sizes. */
  private static final int PANEL_WIDTH = 150, COL_HEIGHT = 20, NAME_LABEL_WIDTH = 40;
  
  /** {@link AttributeTab}. */
  public AttributeTab() {
    attributePanel = new JPanel();
    attributePanel.setBackground(Color.white);
    attributePanel.setLayout(new MigLayout("", "[::50px][::100px]", "[]"));
    attributePanel.setMaximumSize(new Dimension(PANEL_WIDTH, Integer.MAX_VALUE));
    
    createAttributesPanel(null);
  }
  
  /** @return null if attribute value not changed, else - return new value. */
  private String isNewValueSet(Node node, String paramName) {
    for (Parameter newParam : node.getNewAttributes()) {
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
  private void submitAction(Node node) {
    List<Parameter> bufArray = new ArrayList<Parameter>();
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
            int test = Integer.parseInt(attributeField.get(i).getText());
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
            float test = Integer.parseInt(attributeField.get(i).getText());
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
      }
    }
    
    if (!errorFlag) {
      node.setNewAttributes(bufArray);
    }
    //====================================
    System.out.println(node.toString());
    //====================================
    
  }
  
  /** Show new node attribute fields.
   * @param node - new node.
   */
  public void createAttributesPanel(final Node node) {
    
    attributePanel.removeAll();
    
    if (node != null) {
      if (node.getAttributes().size() > 0) {
        attributeField = new ArrayList<JTextField>();
        
        int i = 0;
        for (ComponentParameter comParam : node.getAttributes()) {
          
          //Component name
          JLabel bufLabel = new JLabel(comParam.name());
          attributePanel.add(bufLabel, "cell 0 " + i);
          
          //Component value
          String value = comParam.defaultValue();
          String newValue = isNewValueSet(node, comParam.name());
          if (newValue != null) {
            value = newValue;
          }
          final JTextField bufText = new JTextField(value);
          attributeField.add(bufText);
          
          //Component type
          if (comParam.type() == ParameterType.BOOLEAN) {
            JPanel bufPanel = new JPanel();
            final JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(Boolean.getBoolean(value));
            checkBox.addChangeListener(new ChangeListener() {
  
              @Override
              public void stateChanged(ChangeEvent e) {
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
        
        
        //Submit button
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
          
          @Override
          public void actionPerformed(ActionEvent e) {
            submitAction(node);
          }
          
        });
        JPanel placeHolder = new JPanel();
        placeHolder.setBackground(Color.white);
        attributePanel.add(placeHolder, "cell 0 " + i + " 1 " + i + ", grow, push, span");
        i++;
        attributePanel.add(submit, "cell 0 " + i + " 1 " + i + ", grow, span");
        
        
      } else {
        attributePanel.add(new JLabel("This component haven't"), "cell 0 0 1 0");
        attributePanel.add(new JLabel("parameters."), "cell 0 1 1 1");
      }
    } else {
      attributePanel.add(new JLabel("Nothing choose."), "cell 0 0 1 0");
    }
    
    attributePanel.revalidate();
    attributePanel.repaint();
    
  }
}
