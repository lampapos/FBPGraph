package orc.gg.primitives;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class Port {
	public mxCell cell;
	public int cellId;
	public int parentId;
	public boolean side;
	/**
	 * Смещение x координаты относительно родительского элемента 
	 */
	public int x = 0;
	/**
	 * Смещение y координаты относительно родительского элемента 
	 */
	public int y = 0;
	/**
	 * Переменная для проверки имеет ли порт глобальные или смещенные относительно родителя координаты
	 */
	boolean offset = false;
	
	/**
	 * @param graph - mxGraph.
	 * @param cell_id - id порта во внутренних масивах родительской вершины
	 * @param id - id родительской вершины
	 * @param x - координата
	 * @param y - координата
	 * @param isOut - порт находится в массиве входящих или выходящих вершин
	 */
	public Port(mxGraph graph, int cell_id, int id, int x, int y, boolean isOut){
		Object parent = graph.getDefaultParent();
		cell = (mxCell) graph.insertVertex(parent, null, "", x, y, 20, 20,"shape=ellipse;perimter=ellipsePerimeter");
		cell.setConnectable(true);
		
		this.x = x;
		this.y = y;
		
		this.cellId = cell_id;
		this.parentId = id;
		this.side = isOut;
		
	}
	
	public void setOffset(int px, int py){
		this.x -= px;// - this.x;
		this.y -= py;// - this.y;
		
		offset = true;
	}
	
	public void cellMoved(int nx, int ny){
		cell.getGeometry().setX(x+nx);
		cell.getGeometry().setY(y+ny);
		//System.out.println( cell.getAttribute(cell.toString()) );
	}
}
