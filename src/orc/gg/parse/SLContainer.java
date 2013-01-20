package orc.gg.parse;

import java.util.ArrayList;

import orc.gg.primitives.Node;

public class SLContainer {
	ArrayList<Node> data;
	ArrayList<Connection> con;
	int max_id;
	
	SLContainer(ArrayList<Node> a, ArrayList<Connection> c, int max){
		this.data = a;
		this.con = c;
		this.max_id = max;
	}
}
