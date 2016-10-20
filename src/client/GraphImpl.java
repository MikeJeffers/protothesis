package client;

import java.util.ArrayList;
import java.util.List;
import toxi.geom.Vec2D;

public class GraphImpl implements Graph {
	private List<Connection> connections;
	private List<Node> nodes;

	public GraphImpl() {
		nodes = new ArrayList<Node>();
		connections = new ArrayList<Connection>();
	}

	@Override
	public Graph create() {
		return new GraphImpl();
	}

	@Override
	public List<Connection> getConnections() {
		return this.connections;
	}

	@Override
	public List<Node> getNodes() {
		return this.nodes;
	}

	@Override
	public boolean addNode(float x, float y) {
		Node n = new NodeC(x, y);
		if (!nodes.contains(n)) {
			nodes.add(n);
			return true;
		}
		return false;
	}

	@Override
	public boolean addConnection(Node a, Node b) {
		if (!a.equals(b) && nodes.contains(a) && nodes.contains(b)) {
			Connection c = new ConnImpl(a, b);
			this.connections.add(c);
			return true;
		}
		return false;
	}

	@Override
	public void draw(Main p) {
		for(Node n: this.nodes){
			n.draw(p);
		}
		for (Connection c : this.connections) {
			c.draw(p);
		}
		

	}

	@Override
	public Node clickNearest(float x, float y, float radius) {
		Vec2D click = new Vec2D(x, y);
		float min = radius+1.01f;
		Node closest = null;
		for(Node n: this.nodes){
			float dis = n.getPt().distanceTo(click);
			if(dis<min){
				min = dis;
				closest = n;
			}
		}
		if(closest!=null){
			return closest;
		}
		return null;
	}

	@Override
	public boolean deleteNodeAt(float x, float y, float radius) {
		Node n = clickNearest(x, y, radius);
		if(n!=null && nodes.contains(n)){
			return deleteNode(n);
		}
		return false;
	}

	@Override
	public boolean deleteNode(Node toRemove) {
		List<Connection> removedConnections = new ArrayList<Connection>();
		if(toRemove!=null && nodes.contains(toRemove)){
			for(Connection c: connections){
				Node[] pair = c.getNodePair();
				for(int i=0; i<pair.length; i++){
					if(pair[i].equals(toRemove)){
						removedConnections.add(c);
						break;
					}
				}
			}
			boolean success = true;
			if(!removedConnections.isEmpty()){
				success = success && connections.removeAll(removedConnections);
			}
			success = success && nodes.remove(toRemove);
			return success;
		}
		return false;
	}

}
