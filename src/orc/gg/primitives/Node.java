package orc.gg.primitives;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.sample.network.Generator;
import edu.kpi.fbp.sample.network.PrintResult;
import edu.kpi.fbp.sample.network.Summator;

public class Node {
	//Logical
	public String name;
	public int id;
	public ArrayList<Port> out_connect = new ArrayList<Port>(0);
	public ArrayList<Port> in_connect = new ArrayList<Port>(0);
	
	//Geometry
	public mxCell cell;
	public int x,y;
	public String color = "";
	
	/**
	 * Конструктор вершины сохраняющий данные блока 'Logical'.
	 * @param name - имя вершины.
	 * @param type - тип вершины [ sign | const | user | bd ].
	 * @param id - уникальный идентификатор вершины.
	 */
	public Node(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	/**
	 * Отрисовать вершину на рабочем поле.
	 * @param graph - рабочая область.
	 * @param x - x-координата.
	 * @param y - y-координата.
	 */
	public void draw(mxGraph graph, int x, int y){
		in_connect.clear();
		out_connect.clear();
		
		this.x = x;
		this.y = y;
		int W = 90, H = 0;
	
		int portIn = 0, portOut = 0;
		switch(name){
				case "Generator":
					portIn = ComponentDescriptor.getInputPorts(Generator.class).size();
					portOut = ComponentDescriptor.getOutputPorts(Generator.class).size();
					break;
				case "Summator":
					portIn = ComponentDescriptor.getInputPorts(Summator.class).size();
					portOut = ComponentDescriptor.getOutputPorts(Summator.class).size();
					break;
				case "PrintResult":
					portIn = ComponentDescriptor.getInputPorts(PrintResult.class).size();
					portOut = ComponentDescriptor.getOutputPorts(PrintResult.class).size();
					break;
		}
		
		H = portIn;
		if(H < portOut)
			H = portOut;
		
		H *= 30;
		
		Object parent = graph.getDefaultParent();
		cell = (mxCell) graph.insertVertex(parent, null, name, x, y, W, H);//,"shape=ellipse;perimter=ellipsePerimeter");
		cell.setConnectable(false);
		
		//Создание и добавление портов.
		for(int i=0; i<portIn; i++){
			Port in = new Port(graph, in_connect.size(), id, (x-10), (y+5+30*i), false);
			in.setOffset(x, y);
			in_connect.add(in);
		}
		
		for(int i=0; i<portOut; i++){
			Port out = new Port(graph, out_connect.size(), id, (x+80), (y+5+30*i), true);
			out.setOffset(x, y);
			out_connect.add(out);
		}
			
		if(!color.equals("")){
			graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, color, new Object[]{cell});
		}
	}
	
	public Port getInPort(Object cell){
		Port res = null;
		for(int i=0; i<in_connect.size(); i++){
			if(in_connect.get(i).cell.equals(cell)){
				res = in_connect.get(i);
			}
		}
		return res;
	}
	
	public Port getOutPort(Object cell){
		Port res = null;
		for(int i=0; i<out_connect.size(); i++){
			if(out_connect.get(i).cell.equals(cell)){
				res = out_connect.get(i);
			}
		}
		return res;
	}
	
	/**
	 * Первый этап удаления ячейки, удаление всех портов.
	 */
	public void deletePorts(mxGraphComponent mgp){
		ArrayList<Object> del = new ArrayList<Object>();
		for(int i=0; i<in_connect.size(); i++){
			del.add(in_connect.get(i).cell);
		}
		for(int i=0; i<out_connect.size(); i++){
			del.add(out_connect.get(i).cell);
		}
		mgp.getGraph().removeCells(del.toArray());
	}
	
	public String toString(){
		String res = "< "+name+" id='"+id+"' > : ["+x+","+y+"]";
		res+= "\ncell["+cell+"]";
		res += "\n   <p_in>";
		for(int i=0; i<in_connect.size(); i++){
			res += "\n      cell("+in_connect.get(i).cell+").id = "+in_connect.get(i).cellId;
		}
		res += "\n   <\\p_in>";
		res += "\n   <p_out>";
		for(int i=0; i<out_connect.size(); i++){
			res += "\n      cell("+out_connect.get(i).cell+").id = "+out_connect.get(i).cellId;
		}
		res += "\n   <\\p_out>";
		
		return res;
	}
}
