package edu.kpi.fbp.gui.primitives;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * Primitive type Link consist of source [name, port] and destination [name, port].
 * @author Cheshire
 *
 */
public class Link {
  /** Source name. */
  private String sourceNodeName;
  /** Source port name. */
  private String sourcePortName;
  
  /** Destination name. */
  private String destinationNodeName;
  /** Destination port class name. */
  private String destinationPortName;
  
  /** {@link mxCell}. */
  private mxCell cell;
  /** {@link mxCell}. */
  private mxCell sourcePortCell;
  /** {@link mxCell}. */
  private mxCell destinationPortCell;
  
  /**
   * Link constructor.
   * @param sourceNodeName - name of link source
   * @param sourcePortName - name of link source port
   * @param destinationNodeName - name of link destination
   * @param destinationPortName - name of link destination port
   */
  public Link(String sourceNodeName, String sourcePortName, String destinationNodeName, String destinationPortName) {
    this.sourceNodeName = sourceNodeName;
    this.sourcePortName = sourcePortName;
    this.destinationNodeName = destinationNodeName;
    this.destinationPortName = destinationPortName;
  }
  
  /** @return Name of link destination node. */
  public String getDestinationNodeName() {
    return destinationNodeName;
  }
  
  /** @return Name of link source node. */
  public String getSourceNodeName() {
    return sourceNodeName;
  }
  
  /** @return Name of link destination port. */
  public String getDestinationPortName() {
    return destinationPortName;
  }
  
  /** @return Name of link source port. */
  public String getSourcePortName() {
    return sourcePortName;
  }
  
  /** 
   * @param cell - edge cell
   * @param sourcePortCell - source port cell
   * @param destinationPortCell - destination port cell
   */
  public void setCell(mxCell cell, mxCell sourcePortCell, mxCell destinationPortCell) {
    this.cell = cell;
    this.sourcePortCell = sourcePortCell;
    this.destinationPortCell = destinationPortCell;
  }
  
  /** 
   * @param sourcePortCell - source port cell
   * @param destinationPortCell - destination port cell
   */
  public void setCell(mxCell sourcePortCell, mxCell destinationPortCell) {
    this.sourcePortCell = sourcePortCell;
    this.destinationPortCell = destinationPortCell;
  }
  
  /** Draw link.
   * @param graph - {@link mxGraph}
   */
  public void draw(mxGraph graph) {
    cell = (mxCell) graph.insertEdge(graph.getDefaultParent(), null, null, sourcePortCell, destinationPortCell);
  }
  
  /**
   * Used to debug.
   * @return string in xml style, consist of all link fields.
   */
  public String toString() {
    return "<link source=[" + sourceNodeName + " : " + sourcePortName + "] destination=[" + destinationNodeName + " : " + destinationPortName + "] />";
  }
  
}
