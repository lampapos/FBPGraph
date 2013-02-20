package edu.kpi.fbp.primitives;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.javafbp.ComponentDescriptor;

public class Node {
  // Logical
  public String name;
  public int id;
  public ComponentDescriptor comDes;

  // TODO are you sure that this public field is good idea? and they should be named in camel case style
  public ArrayList<Port> out_connect = new ArrayList<Port>(0);
  public ArrayList<Port> in_connect = new ArrayList<Port>(0);

  // Geometry
  public mxCell cell;
  public int x, y;
  public String color = "";

  /**
   * Конструктор вершины сохраняющий данные блока 'Logical'.
   *
   * @param name
   *          - имя вершины.
   * @param type
   *          - тип вершины [ sign | const | user | bd ].
   * @param id
   *          - уникальный идентификатор вершины.
   */
  public Node(final String name, final int id, ComponentDescriptor des) {
    this.name = name;
    this.id = id;
    this.comDes = des;
  }

  /**
   * Отрисовать вершину на рабочем поле.
   *
   * @param graph
   *          - рабочая область.
   * @param x
   *          - x-координата.
   * @param y
   *          - y-координата.
   */
  public void draw(final mxGraph graph, final int x, final int y) {
    in_connect.clear();
    out_connect.clear();

    this.x = x;
    this.y = y;
    final int W = 90;
    int H = 0;

    int portIn = 0, portOut = 0;
    
    //get number of ports
    portIn = comDes.getInPorts().size();
    portOut = comDes.getOutPorts().size();
    
    H = portIn;
    if (H < portOut)
      H = portOut;

    H *= 30;

    final Object parent = graph.getDefaultParent();
    cell = (mxCell) graph.insertVertex(parent, null, name, x, y, W, H);// ,"shape=ellipse;perimter=ellipsePerimeter");
    cell.setConnectable(false);

    // Создание и добавление портов.
    for (int i = 0; i < portIn; i++) {
      final Port in = new Port(graph, in_connect.size(), id, (x - 10), (y + 5 + 30 * i), false);
      in.setOffset(x, y);
      in_connect.add(in);
    }

    for (int i = 0; i < portOut; i++) {
      final Port out = new Port(graph, out_connect.size(), id, (x + 80), (y + 5 + 30 * i), true);
      out.setOffset(x, y);
      out_connect.add(out);
    }

    if (!color.equals("")) {
      graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, color, new Object[] {cell});
    }
  }

  public Port getInPort(final Object cell) {
    Port res = null;
    for (int i = 0; i < in_connect.size(); i++) {
      if (in_connect.get(i).cell.equals(cell)) {
        res = in_connect.get(i);
      }
    }
    return res;
  }

  public Port getOutPort(final Object cell) {
    Port res = null;
    for (int i = 0; i < out_connect.size(); i++) {
      if (out_connect.get(i).cell.equals(cell)) {
        res = out_connect.get(i);
      }
    }
    return res;
  }

  /**
   * Первый этап удаления ячейки, удаление всех портов.
   */
  public void deletePorts(final mxGraphComponent mgp) {
    final ArrayList<Object> del = new ArrayList<Object>();
    for (int i = 0; i < in_connect.size(); i++) {
      del.add(in_connect.get(i).cell);
    }
    for (int i = 0; i < out_connect.size(); i++) {
      del.add(out_connect.get(i).cell);
    }
    mgp.getGraph().removeCells(del.toArray());
  }

  @Override
  public String toString() {
    String res = "< " + name + " id='" + id + "' > : [" + x + "," + y + "]";
    res += "\ncell[" + cell + "]";
    res += "\n   <p_in>";
    for (int i = 0; i < in_connect.size(); i++) {
      res += "\n      cell(" + in_connect.get(i).cell + ").id = " + in_connect.get(i).cellId;
    }
    res += "\n   <\\p_in>";
    res += "\n   <p_out>";
    for (int i = 0; i < out_connect.size(); i++) {
      res += "\n      cell(" + out_connect.get(i).cell + ").id = " + out_connect.get(i).cellId;
    }
    res += "\n   <\\p_out>";

    return res;
  }
}
