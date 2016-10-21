package client;

import java.util.Comparator;

import toxi.geom.Vec2D;

public class NodeDistComparator implements Comparator<Node>{
	
	private Node targetNode;
	
	public NodeDistComparator(Node target){
		this.setTargetNode(target);
	}

	@Override
	public int compare(Node a, Node b) {
		Vec2D vA = a.getPt();
		Vec2D vB = b.getPt();
		Vec2D vT = targetNode.getPt();
		
		if(vA.distanceTo(vT)<vB.distanceTo(vT)){
			return -1;
		}else if(vA.distanceTo(vT)>vB.distanceTo(vT)){
			return 1;
		}
		return 0;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}
	

}
