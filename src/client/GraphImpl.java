package client;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

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

}
