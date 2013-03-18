package edu.kpi.fbp.gui.primitives;

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
   * Used to debug.
   * @return string in xml style, consist of all link fields.
   */
  public String toString() {
    return "<link source=[" + sourceNodeName + " : " + sourcePortName + "] destination=[" + destinationNodeName + " : " + destinationPortName + "] />";
  }
  
}
