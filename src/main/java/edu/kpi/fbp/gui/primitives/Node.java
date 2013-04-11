package edu.kpi.fbp.gui.primitives;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.gradle.messaging.remote.internal.OutgoingBroadcast;

import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
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
    ArrayList<Object> cells = new ArrayList<Object>();
    int height = 20;
    
    this.x = x;
    this.y = y;
    
    int maxWidth = 0, outWidth = 0, inWidth = 0;
    Port bufPort;
    mxCell bufCell;
    mxGeometry bufGeometry;
    
    //Create inPorts and save it width.
    for (InPort inPort : componentDescriptor.getInPorts()) {
      bufPort = new Port(inPort.value(), this);
      bufPort.draw(graph);
      bufPort.setArray(inPort.arrayPort());
      bufCell = bufPort.getCell();
      ports.add(bufPort);
      cells.add(bufCell);
      bufCell.setValue(inPort.value());
      graph.updateCellSize(bufCell);
      bufGeometry = graph.getCellGeometry(bufCell);
      inWidth += bufGeometry.getWidth();
    }
    maxWidth = inWidth;
    //Create node and save it width.
    cell = (mxCell) graph.insertVertex(graph.getDefaultParent(), null, "", x, y, 0, 0);
    cell.setConnectable(false);
    cell.setId("node");
    cell.setValue(nodeName);
    graph.updateCellSize(cell);
    cells.add(cell);
    bufGeometry = graph.getCellGeometry(cell);
    if (bufGeometry.getWidth() > maxWidth) {
      maxWidth = (int) bufGeometry.getWidth();
    }
    //Create outPorts and save it width.
    for (OutPort outPort : componentDescriptor.getOutPorts()) {
      bufPort = new Port(outPort.value(), this);
      bufPort.draw(graph);
      bufPort.setArray(outPort.arrayPort());
      bufCell = bufPort.getCell();
      ports.add(bufPort);
      cells.add(bufCell);
      bufCell.setValue(outPort.value());
      graph.updateCellSize(bufCell);
      bufGeometry = graph.getCellGeometry(bufCell);
      outWidth += bufGeometry.getWidth();
    }
    if (outWidth > maxWidth) {
      maxWidth = outWidth;
    }
    
    //Set size and position.
    //In ports.
    int offcet = 0, i = 0;
    while (i < componentDescriptor.getInPorts().size()) {
      bufGeometry = graph.getCellGeometry(cells.get(i));
      bufGeometry.setHeight(height);
      int freeSpace = (maxWidth - inWidth) / componentDescriptor.getInPorts().size();
      bufGeometry.setWidth(bufGeometry.getWidth() + freeSpace);
      bufGeometry.setX(x + offcet);
      bufGeometry.setY(y - height);
      offcet += bufGeometry.getWidth() + maxWidth / inWidth;
      graph.getModel().setGeometry(cells.get(i), bufGeometry);
      i++;
    }
    //Node.
    bufGeometry = graph.getCellGeometry(cells.get(i));
    bufGeometry.setHeight(height);
    bufGeometry.setWidth(maxWidth);
    offcet = 0;
    graph.getModel().setGeometry(cells.get(i), bufGeometry);
    i++;
    //Out Ports
    while (i < cells.size()) {
      bufGeometry = graph.getCellGeometry(cells.get(i));
      bufGeometry.setHeight(height);
      int freeSpace = (maxWidth - outWidth) / componentDescriptor.getOutPorts().size();
      bufGeometry.setWidth(bufGeometry.getWidth() + freeSpace);
      bufGeometry.setX(x + offcet);
      bufGeometry.setY(y + height);
      offcet += bufGeometry.getWidth() + maxWidth / outWidth;
      graph.getModel().setGeometry(cells.get(i), bufGeometry);
      i++;
    }
    //I hope it will works someday.
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
