package edu.kpi.fbp.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.app.Gui;
import edu.kpi.fbp.parse.Component;
import edu.kpi.fbp.primitives.Node;

public class Components {
  /** Logger instance. */
  private final Logger logger = LoggerFactory.getLogger(Components.class);

  // FIXME: public fields isn't acceptable
  /**
   * Элемент который был выбран.
   */
  public String choose = "";

  // FIXME: make all field private, plz
  DefaultMutableTreeNode root;
  int insertIndex;
  ArrayList<Component> comp = new ArrayList<Component>();
  Gui G;

  // FIXME: param name which consists from single letter - bad style
  public Components(final Gui g) {
    G = g;
  }

  /**
   * @return сформированое дерево компонентов
   */
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

          logger.debug("Mouse - " + choose);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      logger.info("target - " + e.getPoint());
      //160, 5, 580, 490
      final int local_x = e.getX() - 160, local_y = e.getY() - 5;

      if( (local_x > 0 && local_x < 580) && (local_y > 0 && local_y < 490) ){
            final int buf = G.workField.getNode( G.work.getCellAt(e.getX(), e.getY()) );

        if ((buf == -1) && (!choose.equals(""))) {
                final mxGraph gf = G.work.getGraph();
                gf.getModel().beginUpdate();
                try {
                  G.maxId++;
                  final Node n = new Node(choose, G.maxId, G.connect.getComponentDescriptor(choose));
                  n.draw(gf, local_x, local_y);
                  G.workField.nodes.add(n);
                } finally {
                  gf.getModel().endUpdate();
                }

            }
      }

      //delete visual selection
      res.setSelectionPath(null);
    }
    });

    return res;
  }

  public DefaultMutableTreeNode buildTree() {
    root = new DefaultMutableTreeNode("Components");

    final Object[] comp = G.connect.getAvailableComponentsList();

    for(int i=0; i<comp.length; i++){
      root.add(new DefaultMutableTreeNode(comp[i].toString()));
    }

    return root;
  }

  /**
   * @param in - full unique name with absolute path (e.g. edu.kpi.fbp.network.Summator)
   * @return - local name (e.g. Summator)
   */
  public String cutName(final String in){
    final String res[] = in.split("\\.");
    return res[res.length-1];
  }

}
