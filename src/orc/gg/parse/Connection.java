package orc.gg.parse;

import java.util.ArrayList;

import orc.gg.primitives.Node;
import orc.gg.primitives.Port;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class Connection {
	public int source, destination;
	public int p_source, p_destination;
	public boolean s_side, d_side;
	public mxCell cell;
	
	/**
	 * @param p_source - индекс порта-источника в массиве портов родительской вершины
	 * @param s_side - в каком именно массиве портов находится источник (входящих|исходящих)
	 * @param source - id родительской вершины источника
	 * @param p_destination - индекс порта-приемника в массиве портов родительской вершины
	 * @param d_side - в каком именно массиве портов находится источник (входящих|исходящих)
	 * @param destination - id родительской вершины приемника
	 * @param cell - графических эквивалент связи
	 */
	public Connection(int p_source,
						boolean s_side,
						int source,
						int p_destination,
						boolean d_side,
						int destination,
						mxCell cell)
	{
		this.p_source = p_source;
		this.p_destination = p_destination;
		this.s_side = s_side;
		this.d_side = d_side;
		this.source = source;
		this.destination = destination;
		this.cell = cell;
	}
	
	
	public void drawCon(mxGraph graph, ArrayList<Node> N){
		mxCell cS = null, cD = null;
		
		for(int i=0; i<N.size(); i++){
			if(N.get(i).id == source){
				if(s_side){
					cS = N.get(i).out_connect.get(p_source).cell;
				}else{
					cS = N.get(i).in_connect.get(p_source).cell;
				}
			}
			if(N.get(i).id == destination){
				if(d_side){
					cD = N.get(i).out_connect.get(p_destination).cell;
				}else{
					cD = N.get(i).in_connect.get(p_destination).cell;
				}
			}
		}
		graph.getModel().beginUpdate();
		try	{
			System.out.println("Connect");
			cell = (mxCell) graph.insertEdge(graph.getDefaultParent(), null, "", cS, cD);
			System.out.println("edge - "+cell);
		}finally{
			graph.getModel().endUpdate();
		}
				
		System.out.println("cS - "+cS+"; cD - "+cD);
	}
	
	public String toString(){
		return "cell("+cell+"):\ns("+source+").port["+p_source+"] -> d("+destination+").port["+p_destination+"];";
	}
}
