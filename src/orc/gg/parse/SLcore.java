package orc.gg.parse;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import orc.gg.primitives.Node;

import edu.kpi.fbp.utils.XmlIo;

/**
 * Save|Load core
 * @author cheshire
 *
 */
public class SLcore {
	/**
	 * Set true to debag options;
	 */
	boolean D = true;
	
	public ArrayList<Node> N;
	public ArrayList<Connection> C;
	
	public int max_id = 0;
	
	public void dump(ArrayList<Node> nodes){
		for(int i=0; i<nodes.size(); i++){
			System.out.println(nodes.get(i));
		}
	}
	
	public void dumpC(ArrayList<Connection> nodes){
		for(int i=0; i<nodes.size(); i++){
			System.out.println(nodes.get(i));
		}
	}
	
	public void load(){
		SLContainer res = null;
		
		//Создаю диалог для загрузки (стандартный класс)
		FileDialog fd = new FileDialog(new Frame(), "Открыть", FileDialog.LOAD);
		//Задаю ему стартовую директорию
		fd.setDirectory("../res/");
		//Показываю диалог.
		fd.show();
		
		String path = fd.getDirectory()+fd.getFile();
		if(D)System.out.println(path);
		
		if(path != null){
			res = XmlIo.deserialize(new File(path), SLContainer.class);
		}
		
		N = res.data;
		C = res.con;
		max_id = res.max_id;
	}

	public void save(ArrayList<Node> N, ArrayList<Connection> C, int max){
		
		//Создаю диалог для загрузки (стандартный класс)
		FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
		//Задаю ему стартовую директорию
		fd.setDirectory("../res/");
		//Показываю диалог.
		fd.show();
		
		String path = fd.getDirectory()+fd.getFile();
		if(path != null){
			final String out_data = XmlIo.serialize(new SLContainer(N, C, max));
			try {
				FileWriter wr = new FileWriter(new File(path));
				wr.write(out_data);
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
