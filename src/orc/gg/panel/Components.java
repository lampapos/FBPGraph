package orc.gg.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import orc.gg.parse.Component;


public class Components {
	/**
	 * Элемент который был выбран
	 */
	public String choose = "";
	
	String pathToXml = "";
	DefaultMutableTreeNode root;
	int insertIndex;
	ArrayList<Component> comp = new ArrayList<Component>();
	
	public Components(String s){
		pathToXml = s;
	}
	
	boolean D = true;
	
	/**
	 * @return сформированое дерево компонентов
	 */
	public JTree build() {
				
		final JTree res = new JTree(buildTree());
        res.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);	
		res.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath tPath = res.getPathForLocation(e.getX(), e.getY());
				if(tPath != null){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tPath.getLastPathComponent();
			        // if nothing is selected
			        if (node == null){
			        	choose = "";
			        }
	
			        // retrieve the node that was selected
			        Object nodeInfo = node.getUserObject();
			        if (node.isLeaf()) {
			            choose = nodeInfo.toString();
			        } else {
			            choose = "";
			        }
			        
				}else{
					choose = "";
				}
				
				System.out.println("Mouse - "+choose);
			}
		});
		
		return res;
	}
	
	public DefaultMutableTreeNode buildTree(){		
		root = new DefaultMutableTreeNode("Components");
		
		root.add(new DefaultMutableTreeNode("PrintResult"));
		root.add(new DefaultMutableTreeNode("Generator"));
		root.add(new DefaultMutableTreeNode("Summator"));
			
		return root;
	}

	public void addUserComponent(String name){
		comp.add(new Component(name, "user"));
		if(D)System.out.println("insertIndex - "+insertIndex);
		root.insert(new DefaultMutableTreeNode(name), insertIndex);
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(pathToXml));
			
			out.write("<Component>");
				for(int i=0; i<comp.size(); i++){
					out.write("<node type=\""+comp.get(i).type+"\">\""+comp.get(i).name+"\"</node>");
				}
			out.write("</Component>");
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
