package edu.kpi.fbp.panel;

import java.util.ArrayList;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.parse.Connection;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.primitives.Port;

public class WorkField {

  public ArrayList<Node> nodes = new ArrayList<Node>();
  public ArrayList<Connection> con = new ArrayList<Connection>();

  mxGraph graph;

  /**
   * Возвращает позицию элемента в массиве вершин. Также используется для определения вершина || связь,порт.
   * 
   * @param cell
   *          - выбранный графический элемент.
   */
  public int getNode(Object cell) {
    int res = -1;

    for (int i = 0; i < nodes.size(); i++) {
      if (nodes.get(i).cell.equals(cell)) {
        res = i;
      }
    }

    return res;
  }

  /**
   * Определение связь ли это.
   * 
   * @param cell
   *          - выбранный графический элемент.
   */
  public boolean isConnection(mxCell cell) {
    boolean res = false;

    for (int i = 0; i < con.size(); i++) {
      if (con.get(i).cell.equals(cell)) {
        res = true;
      }
    }

    return res;
  }

  public Port getPort(Object cell) {
    Port res = null;
    for (int i = 0; i < nodes.size(); i++) {
      // Port buf = null;
      res = nodes.get(i).getInPort(cell);
      if (res != null) {
        break;
      }
      res = nodes.get(i).getOutPort(cell);
      if (res != null) {
        break;
      }
    }
    return res;
  }

  public mxGraphComponent createMXGraph(ArrayList<Node> nodes) {

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

      public boolean isCellResizable(Object cell) {
        return false;
      }

      // Вот таким хитрым хреном запрещаем перемешение портов
      public boolean isCellMovable(Object cell) {
        if (((mxCell) cell).getGeometry().getHeight() > 20) {
          return true;
        } else {
          return false;
        }
      }

    };

    // Sets the default edge style
    Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
    style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);

    final mxGraphComponent graphComponent = new mxGraphComponent(graph);

    return graphComponent;
  }

}
