package edu.kpi.fbp.gui.primitives;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.gradle.messaging.remote.internal.OutgoingBroadcast;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.Parameter;

/** Scheme node. */
public class Node {
  /** Node type descriptor. */
  private ComponentDescriptor componentDescriptor;
  /** Unique name. */
  private String nodeName;
  /** Node type name. */
  private String className;
  
  /** mxGraph cell. */
  private mxCell cell;
  /** Node position. */
  private int x, y;
  /** Node color. */
  private String color = null;
  
  /** Array of ports. */
  private ArrayList<Port> ports = null;
  /** Array of outgoing links. */
  private ArrayList<Link> links = null;
  
  /** List of changed component attributes. */
  private List<Parameter> newAttribute = null;
  
  /**
   * Node constructor. 
   * @param id - unique node id
   * @param className - node type name
   * @param componentDescriptor - node type descriptor
   */
  public Node(int id, String className, ComponentDescriptor componentDescriptor) {
    String[] splitBuf = className.split("\\.");
    this.nodeName = splitBuf[splitBuf.length - 1] + "_" + id;
    this.className = className;
    this.componentDescriptor = componentDescriptor;
    
    this.ports = new ArrayList<Port>();
    this.links = new ArrayList<Link>();
    this.newAttribute = new ArrayList<Parameter>();
  }
  
  /** @return cell revelant to node */
  public mxCell getCell() {
    return cell;
  }
  
  /** Save node color. 
   * @param color - node color.
   */
  public void setColor(String color) {
    this.color = color;
  }
  
  /** @return node color. */
  public String getColor() {
    return color;
  }
  
  /** @return node position in Point. */
  public Point getPosition() {
    return new Point(this.x, this.y);
  }
  
  /**
   * Set new node position.
   * @param x - coordinate (k.o.)
   * @param y - coordinate (k.o.)
   */
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  /**
   * Set new node position.
   * @param pos - node position
   */
  public void setPosition(Point pos) {
    this.x = pos.x;
    this.y = pos.y;
  }
  
  /** @return Array of ports. */
  public ArrayList<Port> getPorts() {
    return ports;
  }
  
  /** @return Array of links. */
  public ArrayList<Link> getLinks() {
    return links;
  }
  
  /** @param link - removed link. */ 
  public void deleteLink(Link link) {
    links.remove(link);
  }
  
  /** 
   * Save new outgoing link.
   * @param link - {@link Link}
   */
  public void addLink(Link link) {
    links.add(link);
  }
  
  /** 
   * Set new outgoing links.
   * @param links - array of {@link Link}
   */
  public void setLinks(ArrayList<Link> links) {
    this.links = links;
  }
  
  /** @return node name. */
  public String getName() {
    return nodeName;
  }
  
  /** @return node class name. */
  public String getClassName() {
    return className;
  }
  
  /** @return component descriptor. */
  public ComponentDescriptor getComponentDescriptor() {
    return componentDescriptor;
  }
  
  /** @return component description. */
  public String getDescription() {
    return componentDescriptor.getDescription();
  }
  
  /** @return list of default component parameters. */
  public List<ComponentParameter> getAttributes() {
    return componentDescriptor.getParameters();
  }
  
  /** @return changes in component attributes. */
  public List<Parameter> getNewAttributes() {
    return newAttribute;
  }
  
  /** Save changes in component attributes.
   * @param newParameter - {@link Parameter} with new value
   */
  public void addNewAttribute(Parameter newParameter) {
    newAttribute.add(newParameter);
  }
  
  /** Save changes in component attributes.
   * @param newParameters - list of {@link Parameter} with new value
   */
  public void setNewAttributes(List<Parameter> newParameters) {
    newAttribute = newParameters;
  }
  
  /** Draw node on work field. 
   * @param graph mxGraph field
   * @param x - coordinate
   * @param y - coordinate
   */
  public void draw(final mxGraph graph, final int x, final int y) {
    int width = 60, height = 30;
    
    ArrayList<Object> cells = new ArrayList<Object>();
    
    this.x = x;
    this.y = y;
    
    /** Maximum ports size. */
    int heightKoef = componentDescriptor.getInPorts().size();
    if (heightKoef < componentDescriptor.getOutPorts().size()) {
      heightKoef = componentDescriptor.getOutPorts().size();
    }
    height *= heightKoef;
    int inPortHeight = 0;
    if (componentDescriptor.getInPorts().size() > 0) {
      inPortHeight = height / componentDescriptor.getInPorts().size();
    }
    int outPortHeight = 0;
    if (componentDescriptor.getOutPorts().size() > 0) {
      outPortHeight = height / componentDescriptor.getOutPorts().size();
    }
    
    //Creating new ports.
    Port bufPort;
    int yKoef = 0;
    for (int i = 0; i < componentDescriptor.getInPorts().size(); i++) {
      bufPort = new Port(componentDescriptor.getInPorts().get(i).value(), this);
      bufPort.setHeight(inPortHeight);
      bufPort.draw(graph, x - bufPort.getSize().width, y + yKoef);
      yKoef += bufPort.getSize().height;
      ports.add(bufPort);
      cells.add(bufPort.getCell());
    }
    
    yKoef = 0;
    for (int i = 0; i < componentDescriptor.getOutPorts().size(); i++) {
      bufPort = new Port(componentDescriptor.getOutPorts().get(i).value(), this);
      bufPort.setHeight(outPortHeight);
      bufPort.draw(graph, x + width, y + yKoef);
      yKoef += bufPort.getSize().height;
      ports.add(bufPort);
      cells.add(bufPort.getCell());
    }
    
    //Finally draw node.
    final Object parent = graph.getDefaultParent();
    cell = (mxCell) graph.insertVertex(parent, null, nodeName, x, y, width, height);
    cell.setConnectable(false);
    cell.setId("node");
    cells.add(cell);
      
    //Add color
    if (color != null) {
      graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, color, cells.toArray());
    }
  }
   
  /**
   * Used to debug.
   * @return string in xml style, consist of all node fields.
   */
  public String toString() {
    String res = null;
    
    res =  "<node name=\'" + nodeName + "\' className=\'" + className + "\'>\n";
    res +=  "   <geometry position=[" + x + ", " + y + "] color=\'" + color + "\'/>\n";
    res +=  "   <ports>\n";
    for (Port port : ports) {
      res += "      " + port.toString() + "\n";
    }
    res +=  "   </ports>\n";
    res +=  "   <attribute>\n";
    for (Parameter param : newAttribute) {
      res += "      " + param.toString() + "\n";
    }
    res +=  "   </attribute>\n";
    res +=  "   <links>\n";
    for (Link link : links) {
      res += "      " + link.toString() + "\n";
    }
    res +=  "   </links>\n";
    res += "</node>\n";
    
    return res;
  }
}
