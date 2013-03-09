package edu.kpi.fbp.panel;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}

	//@Override
	//public void init() {
	    // Размер
	    
	//}
	
	public void generateParams(Node target){
		textStore = new ArrayList<JTextField>();
		JPanel res = new JPanel();
		res.setLayout(new GridLayout(target.localParams.size(), 2));
		
		for (int i = 0; i < target.localParams.size(); i++) {
			
			res.add(new JLabel(target.localParams.get(i).name));
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
		                }else{
		                	bufText.setText("true");
		                }
		            }
		        });
				booleanPanel.add(check);
				
				res.add(booleanPanel);
			} else {
				res.add(bufText);
			}
		}
		
		this.add(res);
		this.revalidate();
		this.repaint();
	}
}
