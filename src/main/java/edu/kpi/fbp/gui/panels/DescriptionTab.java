package edu.kpi.fbp.gui.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.kpi.fbp.gui.primitives.Node;

/** Component description. */
public class DescriptionTab {
  /** Panel for text area with component description. */
  private JPanel descriptionPanel;
  
  /** {@link DescriptionTab}. */
  public DescriptionTab() {
    descriptionPanel = new JPanel();
    descriptionPanel.setBackground(Color.white);
    descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
    
    createDescription(null);
  }
  
  /** @return JPanel with component description. */
  public JPanel getDescriptionPanel() {
    return descriptionPanel;
  }
  
  /** Read description from node and place it to text area.
   * @param node - last choosed node
   */
  public void createDescription(Node node) {
    descriptionPanel.removeAll();
    
    JTextArea textArea = new JTextArea("");
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    if (node != null) {
      textArea.setText(node.getDescription());
    } else {
      textArea.setText("Nothing choose.");
    }
    descriptionPanel.add(textArea);
    descriptionPanel.revalidate();
    descriptionPanel.repaint();
  }
}
