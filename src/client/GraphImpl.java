package client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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
			Connection reversed = new ConnImpl(b, a);
			if (!connections.contains(c) && !connections.contains(reversed)) {
				a.addNeighbor(b);
				b.addNeighbor(a);
				return this.connections.add(c);
			}

		}
		return false;
	}

	@Override
	public void draw(Main p) {
		for (Node n : this.nodes) {
			n.draw(p);
		}
		for (Connection c : this.connections) {
			c.draw(p);
		}

	}

	@Override
	public Node clickNearest(float x, float y, float radius) {
		Vec2D click = new Vec2D(x, y);
		float min = radius + 1.01f;
		Node closest = null;
		for (Node n : this.nodes) {
			float dis = n.getPt().distanceTo(click);
			if (dis < min) {
				min = dis;
				closest = n;
			}
		}
		if (closest != null) {
			return closest;
		}
		return null;
	}

	@Override
	public boolean deleteNodeAt(float x, float y, float radius) {
		Node n = clickNearest(x, y, radius);
		if (n != null && nodes.contains(n)) {
			return deleteNode(n);
		}
		return false;
	}

	@Override
	public boolean deleteNode(Node toRemove) {
		List<Connection> removedConnections = new ArrayList<Connection>();
		if (toRemove != null && nodes.contains(toRemove)) {
			for (Connection c : connections) {
				Node[] pair = c.getNodePair();
				for (int i = 0; i < pair.length; i++) {
					if (pair[i].equals(toRemove)) {
						removedConnections.add(c);
						break;
					}
				}
			}
			boolean success = true;
			if (!removedConnections.isEmpty()) {
				for (Connection c : removedConnections) {
					c.getNodeA().removeNeighbor(c.getNodeB());
					c.getNodeB().removeNeighbor(c.getNodeA());
				}
				success = success && connections.removeAll(removedConnections);
			}
			success = success && nodes.remove(toRemove);
			return success;
		}
		return false;
	}

	@Override
	public List<Node> traverseGraph(Node start, Node end) {
		// System.out.println(bfsMap(start, end));
		return dfs(start, end);
	}

	private List<Node> djikstra(Node a, Node b) {
		Comparator<Node> comparator = new NodeDistComparator(b);
		PriorityQueue<Node> q = new PriorityQueue<Node>(comparator);
		Map<Node, Node> predeccesors = new HashMap<Node, Node>();
		for (Node n : this.nodes) {
			if (!n.equals(a)) {
				q.add(n);
				predeccesors.put(n, null);
			}
		}
		while (!q.isEmpty()) {
			Node n = q.poll();
			List<Node> neighbors = getNeighbors(n);
			for (Node v : neighbors) {
				// if()
			}
		}

		return nodes;

	}

	private float distNodes(Node a, Node b) {
		if (a != null && b != null) {
			return a.getPt().distanceTo(b.getPt());
		}
		return Float.NaN;
	}

	// Fucking works now!
	private List<Node> dfs(Node a, Node b) {
		Stack<Node> stack = new Stack<Node>();
		List<Node> visited = new ArrayList<Node>();
		List<Node> path = new ArrayList<Node>();
		stack.push(a);
		while (!stack.isEmpty()) {
			Node n = stack.pop();
			if (!visited.contains(n)) {
				visited.add(n);
				path.add(n);
				if (n.equals(b)) {

					return path;
				}
				List<Node> neighbors = getNeighbors(n);

				boolean newNodesFound = false;
				for (Node v : neighbors) {
					if (!visited.contains(v) && !stack.contains(v)) {
						stack.push(v);
						newNodesFound = true;
					}
				}
				if (!newNodesFound) {
					if (!stack.isEmpty()) {
						Node top = stack.peek();
						List<Node> newPath = new ArrayList<Node>();
						for (Node v : path) {
							newPath.add(v);
							if (v.getNeighbors().contains(top)) {
								break;
							}
						}
						path = newPath;
					}
				}
			}
		}
		return path;
	}

	private Map<Node, Integer> dfsMap(Node a, Node b) {
		Stack<Node> stack = new Stack<Node>();
		List<Node> visited = new ArrayList<Node>();
		List<Node> path = new ArrayList<Node>();
		Map<Node, Integer> map = new HashMap<Node, Integer>();
		stack.push(a);
		map.put(a, 0);
		while (!stack.isEmpty()) {
			Node n = stack.pop();
			int step = stack.size();
			if (!visited.contains(n)) {
				visited.add(n);
				path.add(n);
				if (n.equals(b)) {
					System.out.println(map);
					return map;
				}
				List<Node> neighbors = getNeighbors(n);
				if (!neighbors.isEmpty()) {
					for (Node v : neighbors) {
						if (!visited.contains(v) && !stack.contains(v)) {
							stack.push(v);
							map.put(v, step + 1);
						}
					}
				} else {
					Node top = stack.peek();
					List<Node> newPath = new ArrayList<Node>();
					for (Node v : path) {
						newPath.add(v);
						if (v.getNeighbors().contains(top)) {
							break;
						}
					}
				}

			}

		}
		System.out.println(map);
		return map;
	}

	@Override
	public Map<Node, Integer> getTraversalMap(Node start, Node end) {
		return dfsMap(start, end);
		// return bfsMap(start, end);
	}

	private Map<Node, Integer> bfsMap(Node a, Node b) {
		Queue<Node> q = new LinkedList<Node>();
		Map<Node, Integer> visited = new HashMap<Node, Integer>();
		Map<Node, Node> predeccesors = new HashMap<Node, Node>();
		int step = 0;
		visited.put(a, step);
		predeccesors.put(a, null);
		q.add(a);
		if (a.equals(b)) {
			return visited;
		}
		while (!q.isEmpty()) {
			Node current = q.poll();
			visited.put(current, step);
			Node prev = predeccesors.get(current);
			if (prev != null && visited.containsKey(prev)) {
				step = visited.get(prev) + 1;
			} else {
				step = 0;
			}

			if (current.equals(b)) {
				return visited;
			}
			List<Node> nextNodes = getNeighbors(current);
			for (Node n : nextNodes) {
				if (!visited.containsKey(n)) {
					q.add(n);
					predeccesors.put(n, current);
				}

			}

		}
		return visited;
	}

	private List<Node> bfs(Node a, Node b) {
		Queue<Node> q = new LinkedList<Node>();
		List<Node> visited = new ArrayList<Node>();
		visited.add(a);
		q.add(a);
		if (a.equals(b)) {
			return visited;
		}
		while (!q.isEmpty()) {
			Node current = q.poll();
			visited.add(current);
			if (current != null) {
				if (current.equals(b)) {
					return visited;
				}
				List<Node> nextNodes = getNeighbors(current);
				for (Node n : nextNodes) {
					if (!visited.contains(n)) {
						q.add(n);
					}

				}
			}
		}
		return visited;
	}

	private List<Node> getNeighbors(Node n) {
		List<Node> connected = new ArrayList<Node>();
		if (nodes.contains(n)) {
			List<Node> neighborNodes = n.getNeighbors();
			if (neighborNodes != null && !neighborNodes.isEmpty()) {
				for (Connection c : connections) {
					Node[] pair = c.getNodePair();
					for (int i = 0; i < pair.length; i++) {
						if (pair[i].equals(n)) {
							// add the pair node that is not n
							connected.add(pair[(i + 1) % pair.length]);
							break;
							// TODO alt method to return list of Connections
						}
					}
				}
			}
		}
		return connected;
	}

}
