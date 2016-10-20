package client;

import java.util.List;


public interface Graph {
	
	Graph create();
	List<Connection> getConnections();
	List<Node> getNodes();
	boolean addNode(float x, float y);
	boolean addConnection(Node a, Node b);
	void draw(Main p);
	Node clickNearest(float x, float y, float radius);


}
