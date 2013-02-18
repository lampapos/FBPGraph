package edu.kpi.fbp.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.app.Gui;
import edu.kpi.fbp.parse.Component;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.utils.ComponentsObserver;

public class Components {
  /**
   * Элемент который был выбран
   */
  public String choose = "";

  DefaultMutableTreeNode root;
  int insertIndex;
  ArrayList<Component> comp = new ArrayList<Component>();
  Gui G;

  public Components(Gui g) {
	  G = g;
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
		public void mousePressed(MouseEvent e) {
			final TreePath tPath = res.getPathForLocation(e.getX(), e.getY());
			
			//add visual selection to tree node
			res.setSelectionPath(tPath);
			
	        if (tPath != null) {
	          final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tPath.getLastPathComponent();
	          // if nothing is selected
	          if (node == null) {
	            choose = "";
	          }

	          // retrieve the node that was selected
	          final Object nodeInfo = node.getUserObject();
	          if (node.isLeaf()) {
	            choose = nodeInfo.toString();
	          } else {
	            choose = "";
	          }

	        } else {
	          choose = "";
	        }
	        
	        //spawn arrow image
	        G.showArrow(true);

	        if(D)System.out.println("Mouse - " + choose);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("target - " + e.getPoint());
			//160, 5, 580, 490
			int local_x = e.getX() - 160, local_y = e.getY() - 5;
			
			if( (local_x > 0 && local_x < 580) && (local_y > 0 && local_y < 490) ){
		        int buf = G.W.getNode( G.work.getCellAt(e.getX(), e.getY()) );
		        
				if ((buf == -1) && (!choose.equals(""))) {
		            final mxGraph gf = G.work.getGraph();
		            gf.getModel().beginUpdate();
		            try {
		            	G.max_id++;
		            	final Node n = new Node(choose, 0, G.connect.getComponentDescriptor("edu.kpi.fbp.network."+choose));
		            	n.draw(gf, local_x, local_y);
		            	G.W.nodes.add(n);
		            } finally {
		            	gf.getModel().endUpdate();
		            }
	
		        }
			}
			
			//remove arrow image
	        G.showArrow(false);
			
			//delete visual selection
			res.setSelectionPath(null);
		}
    });

    return res;
  }

  public DefaultMutableTreeNode buildTree() {
    root = new DefaultMutableTreeNode("Components");
    
    Object[] comp = G.connect.getComponentKeySet();

    for(int i=0; i<comp.length; i++){
    	root.add(new DefaultMutableTreeNode(cutName(comp[i].toString())));
    }

    return root;
  }
  
  /**
   * @param in - full unique name with absolute path (e.g. edu.kpi.fbp.network.Summator)
   * @return - local name (e.g. Summator)
   */
  public String cutName(String in){
	  String res[] = in.split("\\.");
	  return res[res.length-1];
  }

  /* Not in client
  public void addUserComponent(final String name) {
    comp.add(new Component(name, "user"));
    if (D)
      System.out.println("insertIndex - " + insertIndex);
    root.insert(new DefaultMutableTreeNode(name), insertIndex);

    try {
      final BufferedWriter out = new BufferedWriter(new FileWriter(pathToXml));

      out.write("<Component>");
      for (int i = 0; i < comp.size(); i++) {
        out.write("<node type=\"" + comp.get(i).type + "\">\"" + comp.get(i).name + "\"</node>");
      }
      out.write("</Component>");

      out.close();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  */
}
