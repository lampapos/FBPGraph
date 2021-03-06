package edu.kpi.fbp.gui.file;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.gui.panels.ComponentTree;
import edu.kpi.fbp.gui.panels.WorkField;
import edu.kpi.fbp.gui.primitives.Link;
import edu.kpi.fbp.gui.primitives.Node;
import edu.kpi.fbp.gui.primitives.Port;
import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ComponentParameter;
import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Encapsulate methods to save/load {@link NetworkModel} to/from file.
 * @author Cheshire
 *
 */
public class SaveLoadCore {
  /** {@link NetworkModel}. */
  private NetworkModel netModel = null;
  /** {@link ParametersStore}. */
  private ParametersStore paramStore = null;
  /** Links to saved {@link NetworkModel} and {@link ParametersStore}. */
  private File modelFile = null, paramFile = null;

  /** @return {@link NetworkModel}. */
  public NetworkModel getNetworkModel() {
    return netModel;
  }

  /** @return {@link NetworkModel}. */
  public ParametersStore getParametersStore() {
    return paramStore;
  }

  /** Delete temporary files and set {@link NetworkModel} to null. */
  public void clean() {
    netModel = null;
    paramStore = null;
    modelFile.delete();
    modelFile = null;
    if (paramFile != null) {
      paramFile.delete();
      paramFile = null;
    }
  }

  /**
   * @param nodeName - unique node name.
   * @param portName - port name.
   * @return node cell.
   */
  private mxCell getPortCell(final String nodeName, final String portName, final ArrayList<Node> nodes) {
    for (final Node node : nodes) {
      if (node.getName().equals(nodeName)) {
        for (final Port port : node.getPorts()) {
          if (port.getName().equals(portName)) {
            port.switchConnect();
            return port.getCell();
          }
        }
      }
    }
    return null;
  }

  /**
   * Draw nodes on {@link mxGraph}.
   * @param graph - {@link mxGraph}.
   */
  private void drawNodes(final mxGraph graph, final ArrayList<Node> nodes) {
    final ArrayList<Link> allLinks = new ArrayList<Link>();

    //First step: draw nodes.
    for (final Node node : nodes) {
      node.draw(graph, node.getPosition().x, node.getPosition().y);
      allLinks.addAll(node.getLinks());
    }

    //Second step: link nodes.
    for (final Link link : allLinks) {
      link.setCell(getPortCell(link.getSourceNodeName(), link.getSourcePortName(), nodes), getPortCell(link.getDestinationNodeName(), link.getDestinationPortName(), nodes));
      link.draw(graph);
    }
  }

  /** Add links to node.
   * @param node - current node
   * @param links - all loaded links
   */
  private void setLinks(final Node node, final List<LinkModel> links) {
    String[] buf;
    String from, to;
    for (final LinkModel link : links) {
      if (link.getFromComponent().equals(node.getName())) {
        buf = link.getFromPort().split("\\[");
        from = buf[0];
        buf = link.getToPort().split("\\[");
        to = buf[0];
        node.addLink(new Link(link.getFromComponent(), from, link.getToComponent(), to));
      }
    }
  }

  /**
   * @param workField - link to {@link WorkField}.
   * @param compTree - {@link ComponentTree}.
   */
  public void load(final WorkField workField, final ComponentTree compTree) {
    final ArrayList<Node> res = new ArrayList<Node>();

    // Создаю диалог для загрузки (стандартный класс)
    final FileDialog fd = new FileDialog(new Frame(), "Открыть", FileDialog.LOAD);
    // Задаю ему стартовую директорию
    fd.setDirectory("bin/temp/");
    // Показываю диалог.
    fd.setVisible(true);

    final String path = fd.getDirectory() + fd.getFile();

    if (path != null) {
      netModel = XmlIo.deserialize(new File(path), NetworkModel.class);

      final List<ComponentModel> components = netModel.getComponents();
      final List<LinkModel> links = netModel.getLinks();
      final Map<String, Object> extra = netModel.getExtra();

      int maxId = 0;
      for (final ComponentModel comp : components) {
        final String[] splitBuf = comp.getName().split("_");
        final int newId = Integer.parseInt(splitBuf[1]);
        if (maxId < newId) {
          maxId = newId;
        }
        final Node bufNode = new Node(newId, comp.getClassName(), compTree.getComponentDescriptor(comp.getClassName()));
        bufNode.setPosition((Point)extra.get(comp.getName() + "|position"));
        bufNode.setColor((String)extra.get(comp.getName() + "|color"));
        setLinks(bufNode, links);
        res.add(bufNode);
      }
      //Load attributes

      final ParametersStore localParams = netModel.getParameters();
      if (localParams != null) {
        for (final Node node : res) {
          final ParameterBundle paramBundle = localParams.getComponentParameters(node.getName(), node.getComponentDescriptor());
          for (final ComponentParameter comPar : node.getAttributes()) {
            if (!paramBundle.get(comPar.port()).getValue().equals(comPar.defaultValue())) {
              node.addNewAttribute(new Parameter(comPar.port(), paramBundle.get(comPar.port()).getValue()));
            }
          }
        }
      }

      drawNodes(workField.getGraph(), res);
      workField.getGraph().refresh();
      workField.setMaxId(maxId);
      workField.setNodes(res);
    }
  }

  /** First part saving process.
   * @param name - model name
   * @param nodes - array of all nodes
   * @return true if there are parameters.
   */
  public boolean makeModel(final String name, final ArrayList<Node> nodes) {
    boolean paramFlag = false;

    final List<ComponentModel> components = new ArrayList<ComponentModel>();
    final List<LinkModel> linkModel = new ArrayList<LinkModel>();
    final ArrayList<Link> links = new ArrayList<Link>();
    final Map<String, Object> extra = new HashMap<String, Object>();

    final ParametersStore.Builder paramStoreBuilder = new ParametersStore.Builder(0);

    for (final Node node : nodes) {
      //Don't work with port sizes.
      components.add(new ComponentModel(node.getClassName(), node.getName(), null, "http://example.com"));
      links.addAll(node.getLinks());

      extra.put(node.getName() + "|position", node.getPosition());
      extra.put(node.getName() + "|color", node.getColor());

      if (node.getNewAttributes().size() > 0) {
        paramStoreBuilder.addComponentConfiguration(node.getName(), node.getNewAttributes());
        paramFlag = true;
      }
    }

    if (paramFlag) {
      paramStore = paramStoreBuilder.build();
    }

    //Multiconnection port indexation
    int inPortNum = 1, outPortNum = 1;

    for (int i = 0; i < links.size() - 1; i++) {
      for (int j = i + 1; j < links.size(); j++) {
        //Проверяем множественные выходные порты
        if (links.get(i).getSourceNodeName().equals(links.get(j).getSourceNodeName())) {
          if (links.get(i).getSourcePortName().equals(links.get(j).getSourcePortName())) {
            links.get(j).setSourcePortName(links.get(j).getSourcePortName() + "[" + inPortNum + "]");
            inPortNum++;
          }
        }
        //Проверяем множественные входные порты
        if (links.get(i).getDestinationNodeName().equals(links.get(j).getDestinationNodeName())) {
          if (links.get(i).getDestinationPortName().equals(links.get(j).getDestinationPortName())) {
            links.get(j).setDestinationPortName(links.get(j).getDestinationPortName() + "[" + outPortNum + "]");
            outPortNum++;
          }
        }
      }
      if (inPortNum != 1) {
        links.get(i).setSourcePortName(links.get(i).getSourcePortName() + "[0]");
      }
      if (outPortNum != 1) {
        links.get(i).setDestinationPortName(links.get(i).getDestinationPortName() + "[0]");
      }
      inPortNum = 1;
      outPortNum = 1;
    }

    for (final Link link : links) {
      linkModel.add(new LinkModel(link.getSourceNodeName(), link.getSourcePortName(), link.getDestinationNodeName(), link.getDestinationPortName()));
    }

    netModel = new NetworkModel(name, components, linkModel, paramStore, extra);

    return paramFlag;
  }

  /**
   * Save {@link NetworkModel} to file.
   * @param nodes - array of all nodes
   */
  public void save(final ArrayList<Node> nodes) {

    String fileName = "", fileDirectory = "";

    // Создаю диалог для загрузки (стандартный класс)
    final FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
    // Задаю ему стартовую директорию
    fd.setDirectory("/");
    // Показываю диалог.
    fd.setVisible(true);
    fileName = fd.getFile();
    fileDirectory = fd.getDirectory();

    final String path =  fileDirectory + fileName;
    if (path != null) {
      makeModel(fileName, nodes);

      final String outNetwork = XmlIo.serialize(netModel);

      try {
        modelFile = new File(path);
        final FileWriter writeNetwork = new FileWriter(modelFile);
        writeNetwork.write(outNetwork);
        writeNetwork.close();

      } catch (final IOException e) {
        e.printStackTrace();
      }

    }

  }

  /**
   * Save only parameters.
   * @param nodes - array of all nodes
   */
  public void saveParams(final ArrayList<Node> nodes) {

    // Создаю диалог для загрузки (стандартный класс)
    final FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
    // Задаю ему стартовую директорию
    fd.setDirectory("/");
    // Показываю диалог.
    fd.setVisible(true);

    final String path =  fd.getDirectory() + fd.getFile();
    if (path != null) {
      makeModel("param", nodes);

      try {
        final String outParams = XmlIo.serialize(paramStore);
        paramFile = new File(path);
        final FileWriter writeParams = new FileWriter(paramFile);
        writeParams.write(outParams);
        writeParams.close();
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
  }

}
