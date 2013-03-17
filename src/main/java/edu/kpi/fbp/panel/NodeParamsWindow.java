package edu.kpi.fbp.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.kpi.fbp.primitives.Node;

public class NodeParamsWindow extends JFrame{
  /**
   * Position of new window.
   */
  int x = 0, y = 0;
  /**
   * Size of new window.
   */
  int width = 0, height = 0;
  /**
   * Connection to changeable fields.
   */
  private ArrayList<JTextField> textStore;
  /**
   * Console text field.
   */
  JTextField consoleText = new JTextField();
  
  private JPanel attributePanel = new JPanel();
  private JPanel consolePanel = new JPanel();
  /**
   * Is property tab exist?
   */
  boolean isPropetryTabExist = false;
  /**
   * Tabbed pane for property and console.
   */
  JTabbedPane tabs;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public NodeParamsWindow(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    
    setSize(width, height);
      setLocation(x, y);
      //setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
      tabs = new JTabbedPane();
      consolePanel.setLayout(new BoxLayout(consolePanel, BoxLayout.X_AXIS));
      consolePanel.add(consoleText);
      tabs.addTab("Console", null, consolePanel, "System log.");
      add(tabs);
  }
  
  public void generateParams(final Node target){
    
    if (!isPropetryTabExist) {
        tabs.addTab("Properties", null, attributePanel, "Component attributes.");
        isPropetryTabExist = true;
    }
    
    textStore = new ArrayList<JTextField>();
    attributePanel.removeAll();// = new JPanel();
    attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.X_AXIS));// 
    
    JPanel options = new JPanel();
    options.setLayout(new GridLayout(target.localParams.size(), 2));
    int optionsHeight = 0;
    
    for (int i = 0; i < target.localParams.size(); i++) {
      
      options.add(new JLabel(target.localParams.get(i).name));
      final JTextField bufText = new JTextField(target.localParams.get(i).value);
      textStore.add(bufText);
      
      if (target.localParams.get(i).type.equals("boolean")) {
        
        JPanel booleanPanel = new JPanel();
        booleanPanel.setLayout(new BoxLayout(booleanPanel, BoxLayout.X_AXIS));
        
        bufText.setEditable(false);
        booleanPanel.add(bufText);
        
        JCheckBox check = new JCheckBox();
        check.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (bufText.getText().equals("true")) {
                      bufText.setText("false");
                    } else {
                      bufText.setText("true");
                    }
                }
            });
        booleanPanel.add(check);
        
        options.add(booleanPanel);
      } else {
        options.add(bufText);
      }
      optionsHeight += 20;
    }
    JPanel atPos = new JPanel();
    atPos.setLayout(new BoxLayout(atPos, BoxLayout.Y_AXIS));
    options.setMinimumSize(new Dimension(200, optionsHeight));
    //options.setPreferredSize(new Dimension(200, optionsHeight));
    options.setMaximumSize(new Dimension(Integer.MAX_VALUE, optionsHeight));
    options.setAlignmentY(Component.TOP_ALIGNMENT);
    options.setAlignmentX(Component.LEFT_ALIGNMENT);
    atPos.add(options);
    
    JPanel submitPanel = new JPanel();
    //int rows = 2, cols = 3;
    submitPanel.setLayout(new BoxLayout(submitPanel, BoxLayout.X_AXIS));//new GridLayout(rows, cols));
    
    //create submit button
    JButton submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        for (int i = 0; i < textStore.size(); i++) {
              switch (target.localParams.get(i).type) {
                case "string":
                  target.localParams.get(i).value = textStore.get(i).getText();
                break;
                case "integer":
                  try {
                    final int test = Integer.parseInt(textStore.get(i).getText());
                    target.localParams.get(i).value = "" + test;
                  } catch (final NumberFormatException ie) {
                      textStore.get(i).setText("invalid format");
                  }
                break;
                case "float":
                  try {
                    final float test = Float.parseFloat(textStore.get(i).getText());
                    target.localParams.get(i).value = "" + test;
                  } catch (final NumberFormatException ie) {
                      textStore.get(i).setText("invalid format");
                  }
                break;
                case "boolean":
                  target.localParams.get(i).value = textStore.get(i).getText();
                break;
              }
            }
      }
    });
    /*
    //creating place holders for empty grid cells
    JPanel[][] placeHolder = new JPanel[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        placeHolder[i][j] = new JPanel();
        submitPanel.add(placeHolder[i][j]);
      }
    }
    */
    //add submit button
    submit.setMinimumSize(new Dimension(50, 20));
    submit.setMaximumSize(new Dimension(50, 20));
    submit.setAlignmentX(Component.RIGHT_ALIGNMENT);
    //placeHolder[rows - 1][cols - 1].add(submit);
    
    submitPanel.setMinimumSize(new Dimension(200, 20));
    //submitPanel.setPreferredSize(new Dimension(200, 60));
    submitPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    
    submitPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    submitPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    submitPanel.add(new JPanel());
    submitPanel.add(submit);
    atPos.add(new JPanel());
    atPos.add(submitPanel);
    
    attributePanel.add(new JScrollPane(atPos));
    
    JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
    separator.setMinimumSize(new Dimension(2, this.getHeight()));
    separator.setPreferredSize(new Dimension(2, this.getHeight()));
    separator.setMaximumSize(new Dimension(2, this.getHeight()));
    attributePanel.add(separator);
    
    attributePanel.add(new JScrollPane(new JTextArea("Description:\n" + target.comDes.getDescription())));
    attributePanel.revalidate();
    attributePanel.repaint();
  }
}
