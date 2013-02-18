package edu.kpi.fbp.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.network.Connect;
import edu.kpi.fbp.primitives.Node;

public class NodeProperties {
  mxGraphComponent work;
  Connect conDes;

  /**
   * Set true to debag options;
   */
  boolean D = false;

  public NodeProperties(mxGraphComponent g, Connect con) {
	  this.work = g;
	  this.conDes = con;
  }

  public JPanel generateJP(final ArrayList<Node> nodes, final int iter) {
    JPanel P = new JPanel();
    ComponentDescriptor target = null;

    if (iter != -1) {

        //obtain a certain descriptor
        target = conDes.getComponentDescriptor("edu.kpi.fbp.network."+nodes.get(iter).name);


	  P.setLayout(new BoxLayout(P, BoxLayout.Y_AXIS));
	  P.add(new JLabel("Name:"));
	  final JTextField jtfN = new JTextField(nodes.get(iter).name);
	  jtfN.setAlignmentX(Component.LEFT_ALIGNMENT);
	  jtfN.setMinimumSize(new Dimension(150, 20));
	  jtfN.setMaximumSize(new Dimension(150, 20));
	  jtfN.setEditable(false);
	  P.add(jtfN);
	
	  P.add(new JLabel("Number of ports in:"));
	  final JTextField jtfPi = new JTextField("" + target.getInPorts().size());
	  jtfPi.setEditable(false);
	  jtfPi.setAlignmentX(Component.LEFT_ALIGNMENT);
	  jtfPi.setMinimumSize(new Dimension(150, 20));
	  jtfPi.setMaximumSize(new Dimension(150, 20));
	  P.add(jtfPi);
	
	  P.add(new JLabel("Number of ports out:"));
	  final JTextField jtfPo = new JTextField("" + target.getOutPorts().size());
	  jtfPo.setEditable(false);
	  jtfPo.setAlignmentX(Component.LEFT_ALIGNMENT);
	  jtfPo.setMinimumSize(new Dimension(150, 20));
	  jtfPo.setMaximumSize(new Dimension(150, 20));
	  P.add(jtfPo);
	
	  P.add(new JLabel("Priority:"));
	  final JTextField prior = new JTextField("" + target.getPriority());
	  prior.setEditable(false);
	  prior.setAlignmentX(Component.LEFT_ALIGNMENT);
	  prior.setMinimumSize(new Dimension(150, 20));
	  prior.setMaximumSize(new Dimension(150, 20));
	  P.add(prior);
	
	  P.add(new JLabel("Must run: " + target.isMustRun()));
	  
	  	JPanel colorButtons = new JPanel();
		colorButtons.setLayout(null);
		
		JButton red = new JButton();
		red.setBackground(Color.red);
		red.setBounds(20, 0, 20, 20);
		red.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "red";	
			}
		});
		
		JButton green = new JButton();
		green.setBackground(Color.green);
		green.setBounds(60, 0, 20, 20);
		green.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "green", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "green";	
			}
		});
		
		JButton black = new JButton();
		black.setBackground(Color.black);
		black.setBounds(120, 0, 20, 20);
		black.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "black", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "black";	
			}
		});
		
		JButton blue = new JButton();
		blue.setBackground(Color.blue);
		blue.setBounds(80, 0, 20, 20);
		blue.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyle(null, new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "";
			}
		});
		
		JButton orange = new JButton();
		orange.setBackground(Color.orange);
		orange.setBounds(40, 0, 20, 20);
		orange.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "orange";	
			}
		});
		
		JButton magenta = new JButton();
		magenta.setBackground(Color.magenta);
		magenta.setBounds(100, 0, 20, 20);
		magenta.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "magenta", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "magenta";	
			}
		});
		
		JButton white = new JButton();
		white.setBackground(Color.white);
		white.setBounds(0, 0, 20, 20);
		white.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				work.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "white", new Object[] { nodes.get(iter).cell });
		        nodes.get(iter).color = "white";	
			}
		});

		colorButtons.add(white);
		colorButtons.add(red);
		colorButtons.add(orange);
		colorButtons.add(green);
		colorButtons.add(blue);
		colorButtons.add(magenta);
		colorButtons.add(black);
		
		colorButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
		colorButtons.setMinimumSize(new Dimension(150, 20));
		colorButtons.setMaximumSize(new Dimension(150, 20));
		
		P.add(new JLabel("Color:"));
		P.add(colorButtons);
	
	  P.add(new JLabel("Hint:"));
	  JScrollPane hint = new JScrollPane(new JTextField(target.getDescription()));
	  hint.setAlignmentX(Component.LEFT_ALIGNMENT);
	  hint.setMinimumSize(new Dimension(150, 280));
	  hint.setMaximumSize(new Dimension(150, 280));
	  P.add(hint);

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
