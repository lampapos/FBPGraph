package edu.kpi.fbp.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.gui.primitives.Link;
import edu.kpi.fbp.gui.primitives.Node;
import edu.kpi.fbp.gui.primitives.Port;


/** Patchbay of nodes. */
public class WorkField {

  /** Set true - activate debug mode. */
  boolean debugMode = false;
  /** Array of all nodes on scheme. */
  private ArrayList<Node> nodes = new ArrayList<Node>();
  /** Graphical representation of work field. */
  private mxGraph graph;
  /** Cast mxGrapx to awt Component. */
  private mxGraphComponent graphComponent;
  /** Maximum id of nodes. Used for create unique node name. */
  private int maxId = 0;
  
  /** Need to set last choosed node. */
  ColorTab colorTab;
  /** Need to set last choosed node. */
  DescriptionTab descriptionTab;
  /** Need to set last choosed node. */
  AttributeTab attributeTab;
  /** Last choosed node. */
  Node lastChoose = null;
  
  /** {@link WorkField}.
   * @param colorTab - used to change last choosed node.
   * @param descriptionTab - used to change last choosed node.
   * @param attributeTab - used to change last choosed node.
   */
  public WorkField(ColorTab colorTab, DescriptionTab descriptionTab, AttributeTab attributeTab) {
    this.colorTab = colorTab;
    this.descriptionTab = descriptionTab;
    this.attributeTab = attributeTab;
  }
  
  /** Return and increase maximum id of nodes.
   * @return maximum id of nodes
   */
  public int getMaxId() {
    int buf = maxId;
    maxId++;
    return buf;
  }
  
  /** @param maxId - maximum id of nodes.*/
  public void setMaxId(int maxId) {
    this.maxId = maxId;
  }
  
  /** @return mxGraph */
  public mxGraph getGraph() {
    return graph;
  }
  
  /** @return last choosed node. */
  public Node getLastChoosed() {
    return lastChoose;
  }
  
  /** @return graphComponent. */
  public mxGraphComponent getGraphComponent() {
    return graphComponent;
  }
  
  /** @return graphComponent size in Dimension*/
  public Dimension getGraphComponentSize() {
    return graphComponent.getSize();
  }

  /**
   * @param cell - graphical representation of node
   * @return node revelant to cell
   */
  public Node getNode(Object cell) {

    for (Node node : nodes) {
      if (node.getCell().equals(cell)) {
        return node;
      }
    }

    return null;
  }
  
  /**
   * @param x - coordinate of node
   * @param y - coordinate of node
   * @return node revelant to coordinates
   */
  public Node getNode(int x, int y) {
    return getNode(graphComponent.getCellAt(x, y));
  }
  
  /** @return array of all components on scheme. */
  public ArrayList<Node> getNodes() {
    return nodes;
  }
  
  /** Save array of all components.
   * @param nodes - array of nodes
   */
  public void setNodes(ArrayList<Node> nodes) {
    this.nodes = nodes;
  }
  
  /**  Notify tabs about change lastChoosed node.
   * @param node - lastChoosed node
   */
  public void updateLastChoosed(Node node) {
    lastChoose = node;
    colorTab.setNode(lastChoose);
    descriptionTab.createDescription(lastChoose);
    attributeTab.createAttributesPanel(lastChoose);
  }
  
  /** Saving new node and set it to {@link ColorTab.node}.
   * @param newNode - new node (k.o.)
   */
  public void addNode(Node newNode) {
    nodes.add(newNode);
    updateLastChoosed(newNode);
  }
  
  /** Used for debug. Print all nodes. */
  public void showNodes() {
    for (Node node : nodes) {
      System.out.println("//=======//" + node.toString() + "//=======//");
    }
  }
  
  /**
   * @param cell - graphical representation of port
   * @return port revelant to cell
   */
  public Port getPort(Object cell) {
    for (Node node : nodes) {
      for (Port port : node.getPorts()) {
        if (port.getCell().equals(cell)) {
          return port;
        }
      }
    }
    return null;
  }

  /** Delete choosed cell and all links to it. */
  public void deleteCell() {
    
    if (lastChoose != null) {
      ArrayList<Object> bufArray = new ArrayList<Object>();
      //Delete node
      bufArray.add(lastChoose.getCell());
      //Delete ports
      for (Port port : lastChoose.getPorts()) {
        bufArray.add(port.getCell());
      }
      //Find and delete links to this nodes
      //Need to avoid java.util.ConcurrentModificationException
      ArrayList<Link> bufLinks = new ArrayList<Link>();
      for (Node node : nodes) {
        //if (node.getLinks() != null) {
        for (Link link : node.getLinks()) {
          if (!link.getDestinationNodeName().equals(lastChoose.getName())) {
            bufLinks.add(link);
          }
        }
        node.setLinks(bufLinks);
      }
      
      graph.removeCells(bufArray.toArray());
      updateLastChoosed(null);
    }
  }
  
  /** Clear all field. */
  public void deleteAll() {
    graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
    nodes.clear();
    maxId = 0;
    
    updateLastChoosed(null);
  }
  
  /**
   * @return new work field.
   */
  public mxGraphComponent createMXGraph() {

    graph = new mxGraph() {

      // Ports are not used as terminals for edges, they are
      // only used to compute the graphical connection point
      public boolean isPort(Object cell) {
        mxGeometry geo = getCellGeometry(cell);

        return (geo != null) ? geo.isRelative() : false;
      }

      // Implements a tooltip that shows the actual
      // source and target of an edge
      public String getToolTipForCell(Object cell) {
        if (model.isEdge(cell)) {
          return convertValueToString(model.getTerminal(cell, true)) + " -> "
              + convertValueToString(model.getTerminal(cell, false));
        }

        return super.getToolTipForCell(cell);
      }

      // Removes the folding icon and disables any folding
      public boolean isCellFoldable(Object cell, boolean collapse) {
        return false;
      }
      
      //Disable cell resizing
      public boolean isCellResizable(Object cell) {
        return false;
      }

      /** Новые порты - новые проблемы, вот таким хитрым хреном запрещаем перемешение портов. */
      public boolean isCellMovable(Object cell) {
        if (!"port".equals(((mxCell)cell).getId())) {
          return true;
        }
        return false;
      }
    
    };

    // Sets the default edge style
    Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
    style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
       
    graphComponent = new mxGraphComponent(graph);
    
    graphComponent.getViewport().setOpaque(false);
    graphComponent.setOpaque(true);
    graphComponent.setBackground(Color.WHITE);
    
    //Create listener to save new position of moved cell
    graph.addListener(mxEvent.CELLS_MOVED, new mxIEventListener() {
        @Override
        public void invoke(final Object arg0, final mxEventObject arg1) {

          final Object[] cells = (Object[]) arg1.getProperty("cells");
          Node movedNode = getNode(cells[0]);
          
          if (movedNode != null) {
            final int oldX = movedNode.getPosition().x, oldY = movedNode.getPosition().y;

            // Сохраняем новое положение вершины
            int newX = (int) (oldX + Double.parseDouble("" + arg1.getProperty("dx"))), newY = (int) (oldY + Double.parseDouble("" + arg1.getProperty("dy")));
            movedNode.setPosition(newX, newY);

            // Смещаем порты относительно новой позиции вершины
            for (Port port : movedNode.getPorts()) {
              graph.moveCells(new Object[] {port.getCell()}, newX - oldX, newY - oldY);
            }
          }
          
        }
    });
    
    //Create mouse listener to save last clicked node
    graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
      @Override
        public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          updateLastChoosed(getNode(e.getX(), e.getY()));
        }
      }
    });
    
    //Create listener to save connections
    graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener() {
        @Override
        public void invoke(final Object arg0, final mxEventObject arg1) {
          // Создаем обьект типа 'связь'
          final mxCell edge = (mxCell) arg1.getProperty("cell");
          // Добавляем связь к соедененным обьектам
          // сохраняем в ноде-источнике
          if (edge.getTarget() != null) {
            Port sourcePort = getPort(edge.getSource()), destinationPort = getPort(edge.getTarget());
            Link newLink = new Link(sourcePort.getParent().getName(), sourcePort.getName(), destinationPort.getParent().getName(), destinationPort.getName());
            newLink.setCell(edge, sourcePort.getCell(), destinationPort.getCell());
            sourcePort.getParent().addLink(newLink);
          } else {
            graph.removeCells(new Object[] {edge});
          }
          
          if (debugMode) {
            showNodes();
          }
          
        }
    });

    return graphComponent;
  }

}
