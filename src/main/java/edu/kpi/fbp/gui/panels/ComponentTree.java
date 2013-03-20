package edu.kpi.fbp.gui.panels;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jpmorrsn.fbp.engine.Component;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.gui.app.MainWindow;
import edu.kpi.fbp.gui.primitives.Node;
import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;

/**
 * Component palette.
 * @author Cheshire
 */
public class ComponentTree {
  /** Short name of choosed node type. */
  private String choose = null;
  /** Need to get main window methods. */
  MainWindow linkToMainWindow = null;
  /** Node types. */
  Map<String, ComponentClassDescriptor> components;
  /** Tree root. */
  DefaultMutableTreeNode root;
  
  /** 
   * Get components map.
   * @param mainWindow link to main window
   */
  public ComponentTree(MainWindow mainWindow) {
    this.linkToMainWindow = mainWindow;
    components = mainWindow.getAvailableComponents();
  }
  
  /** 
   * @param className full class name.
   * @return cast ComponentClassDescriptor to ComponentDescriptor
   */
  public ComponentDescriptor getComponentDescriptor(String className) {
      ComponentClassDescriptor ccd = components.get(className);
      final Class<? extends Component> clazz = ccd.getComponentClass();
      final ComponentDescriptor desc = ComponentDescriptor.buildDescriptor(clazz);
      
      return desc;
  }

  /** @return Component tree. */
  public JTree build() {

    final JTree res = new JTree(buildTree());
    
    res.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    res.addMouseListener(new MouseAdapter() {
  
      @Override
      public void mousePressed(final MouseEvent e) {
        final TreePath tPath = res.getPathForLocation(e.getX(), e.getY());
      
          //add visual selection to tree node
          res.setSelectionPath(tPath);
      
          if (tPath != null) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tPath.getLastPathComponent();
            // if nothing is selected
            if (node == null) {
              choose = null;
            }
      
            // retrieve the node that was selected
            final Object nodeInfo = node.getUserObject();
            if (node.isLeaf()) {
            final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
              choose = parentNode.getUserObject().toString() + nodeInfo.toString();
            } else {
              choose = null;
            }
      
          } else {
            choose = null;
          }
      
      }
      
      @Override
      public void mouseReleased(final MouseEvent e) {
        
        int[] dimension = linkToMainWindow.getWorkFieldCoordinates();
        //Read more about this weird offset.
        int offsetX = linkToMainWindow.getPanelComponentTreeSize().width;
        int mouseX = e.getX(), mouseY = e.getY();
        int x0 = dimension[0], y0 = dimension[1], x1 = x0 + dimension[2], y1 = y0 + dimension[3];
  
        if ((mouseX > x0 && mouseX < x1) && (mouseY > y0 && mouseY < y1)) {
          final Node bufNode = linkToMainWindow.getClassWorkField().getNode(e.getPoint());
  
          if ((bufNode == null) && (choose != null)) {
            final mxGraph graph = linkToMainWindow.getClassWorkField().getGraph();
            graph.getModel().beginUpdate();
            try {
              final Node newNode = new Node(linkToMainWindow.getClassWorkField().getMaxId(), choose, getComponentDescriptor(choose));
              newNode.draw(graph, mouseX - offsetX, mouseY);
              linkToMainWindow.getClassWorkField().addNode(newNode);
            } finally {
              graph.getModel().endUpdate();
            }
  
          }
        }
  
        //delete visual selection
        res.setSelectionPath(null);
      }
    });

    return res;
  }
  
  /** @return tree from components. */
  private DefaultMutableTreeNode buildTree() {
    root = new DefaultMutableTreeNode("Components");

    Object[] names = components.keySet().toArray();
    ArrayList<DefaultMutableTreeNode> packageName = new ArrayList<DefaultMutableTreeNode>();
    String[] complexName;
    DefaultMutableTreeNode bufNode;
    
    for (int i = 0; i < names.length; i++) {
      complexName = splitName(names[i].toString());
      bufNode = inPackageName(complexName[0], packageName);
      if (bufNode != null) {
        bufNode.add(new DefaultMutableTreeNode(complexName[1]));
      } else {
        bufNode = new DefaultMutableTreeNode(complexName[0]);
        bufNode.add(new DefaultMutableTreeNode(complexName[1]));
        packageName.add(bufNode);
      }
    }
    
    for (DefaultMutableTreeNode node : packageName) {
        root.add(node);
    }

    return root;
  }
  
  /** 
   * Split class name to package and component name.
   * @return String[2] = [package name, component name].
   */
  private String[] splitName(String in) {
    String[] res = new String[2];
    String[] splitBuf = in.split("\\.");
    res[1] = splitBuf[splitBuf.length - 1];
    res[0] = in.substring(0, in.length() - res[1].length());
        
    return res;
  }
  
  /** @return DefaultMutableTreeNode if it already in array. */
  private DefaultMutableTreeNode inPackageName(String name, ArrayList<DefaultMutableTreeNode> packageName) {
    for (int i = 0; i < packageName.size(); i++) {
      if (packageName.get(i).getUserObject().toString().equals(name)) {
        return packageName.get(i);
      }
    }
    return null;
  }
  
}
