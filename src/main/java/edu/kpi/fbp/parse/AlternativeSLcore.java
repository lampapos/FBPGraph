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
		  //clear arrays (на всякий)
		  loadNodes.clear();
		  loadConnection.clear();
		  
		  NetworkModel deserializedModel = null;
		  
		  // Создаю диалог для загрузки (стандартный класс)
		  FileDialog fd = new FileDialog(new Frame(), "Открыть", FileDialog.LOAD);
		  // Задаю ему стартовую директорию
		  fd.setDirectory("../res/");
		  // Показываю диалог.
		  fd.show();

		  String path = fd.getDirectory() + fd.getFile();
		  
		  if (path != null) {
			  deserializedModel = XmlIo.deserialize(new File(path), NetworkModel.class);
			  
			  final List<ComponentModel> components = deserializedModel.getComponents();
			  final List<LinkModel> links = deserializedModel.getLinks();
			  final Map<String, Object> extra = deserializedModel.getExtra();
			  
			  for (int i = 0; i < components.size(); i++) {
				  String globalName = components.get(i).getName();
				  Node newNode = new Node(
						  globalName.split("_")[0],
						  Integer.parseInt(globalName.split("_")[1]),
						  connect.getComponentDescriptor("edu.kpi.fbp.network."+globalName.split("_")[0])
						  );
				  
				  Point pos = (Point) extra.get(newNode.name+"|position");
				  newNode.x = pos.x;
				  newNode.y = pos.y;
				  newNode.color = (String) extra.get(newNode.name+"|color");
				  
				  loadNodes.add(newNode);
			  }
			  
			  for (int i = 0; i < links.size(); i++) {
				  LinkModel newLM = links.get(i);
				  // getLinkName(nodes, con.get(i).source),
				  //con.get(i).s_side + "_" + con.get(i).p_source,
				  //getLinkName(nodes, con.get(i).destination),
				  //con.get(i).d_side + "_" + con.get(i).p_destination
				  Connection newCon = new Connection(
						  Integer.parseInt(newLM.getFromPort().split("_")[1]),
						  Boolean.parseBoolean(newLM.getFromPort().split("_")[0]),
						  Integer.parseInt(newLM.getFromComponent()),
						  Integer.parseInt(newLM.getToPort().split("_")[1]),
						  Boolean.parseBoolean(newLM.getToPort().split("_")[0]),
						  Integer.parseInt(newLM.getToComponent()),
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
	   */
	  public void save(ArrayList<Node> nodes, ArrayList<Connection> con, int maxId) {
		  
		  // Создаю диалог для загрузки (стандартный класс)
		  FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
		  // Задаю ему стартовую директорию
		  fd.setDirectory("../res/");
		  // Показываю диалог.
		  fd.show();

		  String path = fd.getDirectory() + fd.getFile();
		  if (path != null) {
			  
			  final List<ComponentModel> components = new ArrayList<ComponentModel>();
			  final Map<String, Object> extra = new HashMap<String, Object>();
			  Node bufNode;
			  
			  for (int i = 0; i < nodes.size(); i++) {
				  bufNode = nodes.get(i);
				  
				  Map<String, Integer> portSize = new HashMap<String, Integer>();
				  portSize.put("in", bufNode.in_connect.size());
				  portSize.put("out", bufNode.out_connect.size());
				  String globalName = bufNode.name + "_" + bufNode.id;
				  components.add(new ComponentModel(bufNode.comDes.getClass().getCanonicalName(), globalName, portSize, "http://example.com"));
				  
				  extra.put(globalName + "|position", new Point(bufNode.x, bufNode.y));
				  extra.put(globalName + "|color", bufNode.color);
			  }
			  
			  final List<LinkModel> links = new ArrayList<LinkModel>();
			  
			  for (int i = 0; i < con.size(); i++) {
				  //links.add(new LinkModel("_Generate", "OUT", "_Sum", "IN"));
				  links.add(new LinkModel(
						  getLinkName(nodes, con.get(i).source),
						  con.get(i).s_side + "_" + con.get(i).p_source,
						  getLinkName(nodes, con.get(i).destination),
						  con.get(i).d_side + "_" + con.get(i).p_destination
						  ));
			  }
			  
			  final NetworkModel netModel = new NetworkModel(fd.getFile(), components, links, extra);
			  
			  final String outData = XmlIo.serialize(netModel);
		      try {
		        FileWriter wr = new FileWriter(new File(path));
		        wr.write(outData);
		        wr.close();
		      } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }

		  }
		    
	  }
	  
	  String getLinkName(ArrayList<Node> nodes, int id){
		  String res = "";
		  
		  for (int i = 0; i < nodes.size(); i++) {
			  if (nodes.get(i).id == id) {
				  res = nodes.get(i).name + "_" + id;
			  }
		  }
		  return res;
	  }

	  /* split
	  String[] parceName(String in){
		  String[] res = new String[2];
		  boolean lock = false;
		  in.s
		  	for (int i = 0; i < in.length(); i++){
		  		if(in.charAt(i) == '_'){
		  			lock = true;
		  			i++;
		  		}
		  	}
		  
		  return res;
	  }
	  */
}
