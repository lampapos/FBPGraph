package edu.kpi.fbp.primitives;

import javax.swing.JTextField;

public class ParamTextField {
  
  public JTextField textField;
  public String type;

  public ParamTextField(JTextField tf, String type) {
    this.textField = tf;
    this.type = type;
  }
}
