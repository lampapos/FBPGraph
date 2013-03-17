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
  
  /**
   * Used to debug.
   * @return string in xml style, consist of all link fields.
   */
  public String toString() {
    return "<link source=[" + sourceNodeName + " : " + sourcePortName + "] destination=[" + destinationNodeName + " : " + destinationPortName + "] />";
  }
  
}
