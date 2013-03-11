package edu.kpi.fbp.parse;

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

import edu.kpi.fbp.model.ComponentModel;
import edu.kpi.fbp.model.LinkModel;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.network.Connect;
import edu.kpi.fbp.params.Parameter;
import edu.kpi.fbp.params.ParameterBundle;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Save network via FBPCore.
 * @author Cheshire
 *
 */
public class AlternativeSLcore {
    /**
     * Set true to debag options.S
     */
    boolean debug = true;
    /**
     * Nodes loaded from xml.
     */
    public ArrayList<Node> loadNodes;
    /**
     * Connections loaded from xml.
     */
    public ArrayList<Connection> loadConnection;
    /**
     * Maximum id loaded from xml.
     */
    public int maxId = 0;
    /**
     * Used to set component descriptor to all loaded nodes.
     */
    private Connect connect;
    /**
     * 
     */
    public NetworkModel netModel = null;
    /**
     * 
     */
    public ParametersStore paramStore = null;
    
    public AlternativeSLcore(Connect c) {
      this.connect = c;
    }
    /**
     * Dump array of nodes.
     * @param nodes - array of nodes.
     */
    public void dump(ArrayList<Node> nodes) {
      for (int i = 0; i < nodes.size(); i++) {
        System.out.println(nodes.get(i));
      }
    }
    /**
     * Dump array of connections.
     * @param con - array of connections.
     */
    public void dumpC(ArrayList<Connection> con) {
      for (int i = 0; i < con.size(); i++) {
        System.out.println(con.get(i));
      }
    }
    /**
     * Load data from xml.
     */
    public void load() {
      maxId = 0;
      //clear arrays (на всякий)
      loadNodes = new ArrayList<Node>();
      loadConnection = new ArrayList<Connection>();
      
      //NetworkModel deserializedModel = null;
      
      // Создаю диалог для загрузки (стандартный класс)
      FileDialog fd = new FileDialog(new Frame(), "Открыть", FileDialog.LOAD);
      // Задаю ему стартовую директорию
      fd.setDirectory("./bin/");
      // Показываю диалог.
      fd.show();

      String path = fd.getDirectory() + fd.getFile();
      
      if (path != null) {
        netModel = XmlIo.deserialize(new File(path), NetworkModel.class);
        
        final List<ComponentModel> components = netModel.getComponents();
        final List<LinkModel> links = netModel.getLinks();
        final Map<String, Object> extra = netModel.getExtra();
        
        File paramsFile = new File(fd.getDirectory() + "params_" + fd.getFile());
        ParametersStore localParams = null;
        if (paramsFile.exists()) {
        	localParams = XmlIo.deserialize(paramsFile, ParametersStore.class);
        }
        
        for (int i = 0; i < components.size(); i++) {
          String globalName = components.get(i).getName();
          int newId = Integer.parseInt(globalName.split("_")[1]);
          Node newNode = new Node(
              globalName.split("_")[0],
              newId,
              connect.getComponentDescriptor("edu.kpi.fbp.network." + globalName.split("_")[0])
              );
          
          System.out.println("name - " + newNode.componentClassName + "_" + newNode.id);
          
          Point pos = (Point) extra.get(newNode.componentClassName + "_" + newNode.id + "|position");
          
          System.out.println("pos - " + pos);
          
          newNode.x = (int) pos.getX();
          newNode.y = (int) pos.getY();
          newNode.color = (String) extra.get(newNode.componentClassName + "_" + newNode.id + "|color");
          if (newNode.color == null) {
            newNode.color = "";
          }
          
          if (localParams != null) {
        	  getNodeParams(localParams, newNode);
          }
          
          loadNodes.add(newNode);
          
          if (maxId < newId) {
            maxId = newId;
          }
        }
        
        for (int i = 0; i < links.size(); i++) {
          LinkModel newLM = links.get(i);
          
          System.out.println("LinkModel(" + i + ") - " + newLM);
          
          // getLinkName(nodes, con.get(i).source),
          //con.get(i).s_side + "_" + con.get(i).p_source,
          //getLinkName(nodes, con.get(i).destination),
          //con.get(i).d_side + "_" + con.get(i).p_destination
          Connection newCon = new Connection(
              Integer.parseInt(newLM.getFromPort().split("_")[1]),
              Boolean.parseBoolean(newLM.getFromPort().split("_")[0]),
              Integer.parseInt(newLM.getFromComponent().split("_")[1]),
              Integer.parseInt(newLM.getToPort().split("_")[1]),
              Boolean.parseBoolean(newLM.getToPort().split("_")[0]),
              Integer.parseInt(newLM.getToComponent().split("_")[1]),
              null
              );
              
          loadConnection.add(newCon);
        }
        
      }
      
    }
    /**
     * Save data to xml.
     * @param nodes - array of nodes
     * @param con - array of connections
     * @param maxId - maximum node id
     * @param flag - save on default or user path
     */
    public void save(ArrayList<Node> nodes, ArrayList<Connection> con, int maxId, boolean flag) {
      
    	String fileName = "", fileDirectory = "";
    	
    	if (flag) {
	      // Создаю диалог для загрузки (стандартный класс)
	      FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
	      // Задаю ему стартовую директорию
	      fd.setDirectory("/");
	      // Показываю диалог.
	      fd.show();
	      fileName = fd.getFile();
	      fileDirectory = fd.getDirectory();
    	} else {
    		fileName = "defaultRun.txt";
    		fileDirectory = "temp/";
    	}

      String path =  fileDirectory + fileName;
      if (path != null) {
        
        final List<ComponentModel> components = new ArrayList<ComponentModel>();
        final Map<String, Object> extra = new HashMap<String, Object>();
        
        ParametersStore.Builder paramStoreBuilder = new ParametersStore.Builder();
        
        Node bufNode;
        
        for (int i = 0; i < nodes.size(); i++) {
          bufNode = nodes.get(i);
          /*
          Map<String, Integer> portSize = new HashMap<String, Integer>();
          for ( int i = 0; i< bufNode.comDes.getInPorts().size(); i++){
        	  if ( bufNode.comDes.getInPorts().get(i).arrayPort() ){
        		  portSize.put(bufNode.comDes.getInPorts().get(i).arrayPort(), bufNode.comDes.getInPorts().get(i));
        	  }
          }
          */
          //portSize.put("out", bufNode.out_connect.size());
          String[] splitTemp = nodes.get(i).componentClassName.split("\\.");
          String globalName = splitTemp[splitTemp.length - 1] + "_" + bufNode.id;
          components.add(new ComponentModel(bufNode.componentClassName, globalName, null , "http://example.com"));
          
          extra.put(globalName + "|position", new Point(bufNode.x, bufNode.y));
          extra.put(globalName + "|color", bufNode.color);
          
          if (bufNode.localParams.size() > 0) {
        	  /*
        	  if (paramStore == null) {
        	  
        		  paramStore = new ParametersStore.Builder();
        	  }
        	  */
        	  addNodeParams(paramStoreBuilder, bufNode);//paramStore.addComponentConfiguration(componentName, parameters)
          }
        }
        
        final List<LinkModel> links = new ArrayList<LinkModel>();
        
        for (Connection connection : con) {
          String inPortName = getNode(nodes, connection.source).out_connect.get(connection.p_source).portName;
          String outPortName = getNode(nodes, connection.destination).in_connect.get(connection.p_destination).portName;
          links.add(new LinkModel(
              getNodeName(nodes, connection.source),
              inPortName,//con.get(i).s_side + "_" + con.get(i).p_source,
              getNodeName(nodes, connection.destination),
              outPortName//con.get(i).d_side + "_" + con.get(i).p_destination
              ));
        }
        
        netModel = new NetworkModel(fileName, components, links, extra);
        
        final String outNetwork = XmlIo.serialize(netModel);
        String outParams = "";
        //if (paramStore == null) {
        	paramStore = paramStoreBuilder.build();
        	outParams = XmlIo.serialize(paramStore);
        //}
        
          try {
            FileWriter writeNetwork = new FileWriter(new File(path));
            writeNetwork.write(outNetwork);
            writeNetwork.close();
            
            if (paramStore != null) {
            	FileWriter writeParams = new FileWriter(new File(fileDirectory + "params_" + fileName));
            	writeParams.write(outParams);
            	writeParams.close();
            }
            
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

      }
        
    }
    
    /**
     * Create unique node name.
     * @param nodes - array list of all nodes;
     * @param id - unique identifier of node;
     * @return unique node name.
     */
    String getNodeName(ArrayList<Node> nodes, int id) {
      String res = "";
      
      for (int i = 0; i < nodes.size(); i++) {
        if (nodes.get(i).id == id) {
        	String[] splitTemp = nodes.get(i).componentClassName.split("\\.");
          res = splitTemp[splitTemp.length - 1] + "_" + id;
        }
      }
      return res;
    }
    
    void addNodeParams(ParametersStore.Builder store, Node node) {
    	String[] splitTemp = node.componentClassName.split("\\.");
    	String name = splitTemp[splitTemp.length - 1] + "_" + node.id;
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	
    	for (int i = 0; i < node.localParams.size(); i++) {
    		parameters.add(new Parameter(node.localParams.get(i).name, node.localParams.get(i).value));
    	}
    	
    	store.addComponentConfiguration(name, parameters);
    }
    
    void getNodeParams(ParametersStore store, Node node) {
    	//for (int i = 0; i < paramStore.)
    	ParameterBundle bundle = store.getComponentParameters(node.componentClassName + "_" + node.id, node.comDes);
    	for (int i = 0; i < node.localParams.size(); i++) {
    		node.localParams.get(i).value = bundle.getString(node.localParams.get(i).name);
    	}
    }
    
    Node getNode(ArrayList<Node> nodes, int id){

    	for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).id == id) {
            	return nodes.get(i);
            }
    	}
    	
    	return null;
    }
    
}
