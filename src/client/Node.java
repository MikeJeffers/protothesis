package client;


import java.util.List;

import toxi.geom.Vec2D;


public interface Node{


	Vec2D getPt();
	boolean addNeighbor(Node n);
	boolean removeNeighbor(Node n);
	List<Node> getNeighbors();
	void draw(Main p);
	

}
