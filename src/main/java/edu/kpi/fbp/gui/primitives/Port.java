package edu.kpi.fbp.gui.primitives;

import java.awt.Dimension;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * Encapsulate all methods to work with ports.
 * @author Cheshire
 */
public class Port {
  /** Port name. */
  private String portName;
  /** Link to parent node. */
  private Node parentNode;
  /** Is port already connected. */
  private boolean isConnect;
  /** If this is array port. */
  private boolean isArray;
  
  /** mxGraph cell. */
  private mxCell cell;
  /** Default size. */
  private static final int DEFAULT_WIDTH = 20, DEFAULT_HEIGHT = 30;
  /** Default port size. */
  private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
  
  
  /**
   * Node constructor. 
   * @param name - port name
   * @param parentNode - link to parent node
   */
  public Port(String name, Node parentNode) {
    this.portName = name;
    this.parentNode = parentNode;
    this.isConnect = false;
    this.isArray = false;
  }
  
  /** @return true if cell connected. */
  public boolean isConnect() {
    return isConnect;
  }
  
  /** @param in - is port Array. */
  public void setArray(boolean in) {
    this.isArray = in;
  }
  
  /** @return true - if port Array. */
  public boolean isArray() {
    return isArray;
  }
  
  /** Set flag {@link isConnect} in opposite side. */
  public void switchConnect() {
    if (!isArray) {
      isConnect = !isConnect;
    }
  }
  
  /** @return cell revelant to port */
  public mxCell getCell() {
    return cell;
  }
  
  /** Use if need change port size.
   * @param width - width (k.o.)
   * @param height - height (k.o.)
   */
  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  /** Use if need change only port height.
   * @param height - height (k.o.)
   */
  public void setHeight(int height) {
    this.height = height;
  }
  
  /** @return port size. */
  public Dimension getSize() {
    return new Dimension(width, height);
  }
  
  /** @return parent node. */
  public Node getParent() {
    return parentNode;
  }
  
  /** @return port name. */
  public String getName() {
    return portName;
  }
  
  /** Draw port on work field. 
   * @param graph mxGraph field
   */
  public void draw(final mxGraph graph) {
    
    final Object parent = graph.getDefaultParent();
      cell = (mxCell) graph.insertVertex(parent, null, "", 0, 0, 0, 0);
      cell.setConnectable(true);
      cell.setId("port");
      
  }
  
  /**
   * Used to debug.
   * @return string in xml style, consist of all port fields.
   */
  public String toString() {
    return "<port name=\'" + portName + "\'>";
  }
}
