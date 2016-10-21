package client;

import java.util.List;
import java.util.Map;


public interface Graph {
	
	Graph create();
	List<Connection> getConnections();
	List<Node> getNodes();
	boolean addNode(float x, float y);
	boolean deleteNodeAt(float x, float y, float radius);
	boolean deleteNode(Node toRemove);
	boolean addConnection(Node a, Node b);
	void draw(Main p);
	Node clickNearest(float x, float y, float radius);
	List<Node> traverseGraph(Node start, Node end);
	Map<Node, Integer> getTraversalMap(Node start, Node end);
	
	


}
