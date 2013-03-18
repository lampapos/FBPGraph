package edu.kpi.fbp.gui.file;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.XmlIo;
import edu.kpi.fbp.gui.primitives.Link;
import edu.kpi.fbp.gui.primitives.Node;

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
  
  /** @return {@link NetworkModel}. */
  public NetworkModel getNetworkModel() {
    return netModel;
  }
  
  /**
   * Save {@link NetworkModel} to file.
   * @param nodes - array of all nodes
   * @param flag - work mode (true - ask save path, false - use default)
   */
  public void save(ArrayList<Node> nodes, boolean flag) {
    
    String fileName = "", fileDirectory = "";
    
    if (flag) {
      // Создаю диалог для загрузки (стандартный класс)
      FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
      // Задаю ему стартовую директорию
      fd.setDirectory("bin/temp/");
      // Показываю диалог.
      fd.show();
      fileName = fd.getFile();
      fileDirectory = fd.getDirectory();
    } else {
      fileName = "defaultRun.txt";
      fileDirectory = "bin/temp/";
    }

    String path =  fileDirectory + fileName;
    if (path != null) {
      boolean paramFlag = false;
      
      final List<ComponentModel> components = new ArrayList<ComponentModel>();
      final List<LinkModel> links = new ArrayList<LinkModel>();
      final Map<String, Object> extra = new HashMap<String, Object>();
      
      ParametersStore.Builder paramStoreBuilder = new ParametersStore.Builder();
      
      for (Node node : nodes) {
        //Don't work with port sizes.
        components.add(new ComponentModel(node.getClassName(), node.getName(), null, "http://example.com"));
        
        for (Link link : node.getLinks()) {
          links.add(new LinkModel(link.getSourceNodeName(), link.getSourcePortName(), link.getDestinationNodeName(), link.getDestinationPortName()));
        }
        
        extra.put(node.getName() + "|position", node.getPosition());
        extra.put(node.getName() + "|color", node.getColor());
        
        if (node.getNewAttributes().size() > 0) {
          paramStoreBuilder.addComponentConfiguration(node.getName(), node.getNewAttributes());
          paramFlag = true;
        }
      }
      
      netModel = new NetworkModel(fileName, components, links, extra);
      
      final String outNetwork = XmlIo.serialize(netModel);
      
      String outParams = null;
      if (paramFlag) {
        paramStore = paramStoreBuilder.build();
        outParams = XmlIo.serialize(paramStore);
      }
      
      try {
        FileWriter writeNetwork = new FileWriter(new File(path));
        writeNetwork.write(outNetwork);
        writeNetwork.close();
        
        if (paramFlag) {
          FileWriter writeParams = new FileWriter(new File(fileDirectory + "params_" + fileName));
          writeParams.write(outParams);
          writeParams.close();
        }
        
      } catch (IOException e) {
        e.printStackTrace();
      }
      
    }
    
  }
    
}
