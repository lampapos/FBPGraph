package edu.kpi.fbp.gui.primitives;

import java.awt.Point;
import java.util.ArrayList;

import org.gradle.messaging.remote.internal.OutgoingBroadcast;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.javafbp.ComponentDescriptor;

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
  private String color;
  
  /** Array of ports. */
  private ArrayList<Port> ports = new ArrayList<Port>();
  /** Array of outgoing links. */
  private ArrayList<Link> links = new ArrayList<Link>();
  
  /**
   * Node constructor. 
   * @param id - unique node id
   * @param className - node type name
   * @param componentDescriptor - node type descriptor
   */
  public Node(int id, String className, ComponentDescriptor componentDescriptor) {
    String[] splitBuf = className.split("\\.");
    this.nodeName = splitBuf[splitBuf.length - 1] + "-" + id;
    this.className = className;
    this.componentDescriptor = componentDescriptor;
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
  
  /** @return Array of ports. */
  public ArrayList<Port> getPorts() {
    return ports;
  }
  
  /** 
   * Save new outgoing link.
   * @param link - {@link Link}
   */
  public void addLink(Link link) {
    links.add(link);
  }
  
  /** @return node name. */
  public String getName() {
    return nodeName;
  }
  
  /** @return node class name. */
  public String getClassName() {
    return className;
  }
  
  /** Draw node on work field. 
   * @param graph mxGraph field
   * @param x - coordinate
   * @param y - coordinate
   */
  public void draw(final mxGraph graph, final int x, final int y) {
    int width = 60, height = 30;
    
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
    }
    
    yKoef = 0;
    for (int i = 0; i < componentDescriptor.getOutPorts().size(); i++) {
      bufPort = new Port(componentDescriptor.getOutPorts().get(i).value(), this);
      bufPort.setHeight(outPortHeight);
      bufPort.draw(graph, x + width, y + yKoef);
      yKoef += bufPort.getSize().height;
      ports.add(bufPort);
    }
    
    //Finally draw node.
    final Object parent = graph.getDefaultParent();
      cell = (mxCell) graph.insertVertex(parent, null, nodeName, x, y, width, height);
      cell.setConnectable(false);
      cell.setId("node");
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
    res +=  "   <links>\n";
    for (Link link : links) {
      res += "      " + link.toString() + "\n";
    }
    res +=  "   </links>\n";
    res += "</node>\n";
    
    return res;
  }
}
