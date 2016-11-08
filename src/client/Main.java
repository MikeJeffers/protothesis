package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.*;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class Main extends PApplet {
	public ToxiclibsSupport gfx;
	Graph g;
	Node active;
	Node prevActive;
	boolean delete = false;
	boolean traverse = false;
	List<Node> mostRecentTraversal = new ArrayList<Node>();
	//Map<Node, Integer> mostRecentTraversal = new HashMap<Node, Integer>();

	public static void main(String[] args) {
		PApplet.main("client.Main");
	}

	public void settings() {
		size(800, 600, "processing.javafx.PGraphicsFX2D");
	}

	public void setup() {
		g = new GraphImpl();
		background(0);
		stroke(255);
		gfx = new ToxiclibsSupport(this);
		textSize(24);
	}

	public void draw() {
		background(0);
		stroke(255);
		strokeWeight(1);
		fill(111, 5, 5);
		g.draw(this);
		if (active != null) {
			strokeWeight(3);
			fill(222, 5, 5);
			active.draw(this);
		}
		if (prevActive != null) {
			strokeWeight(2);
			fill(222, 5, 5);
			active.draw(this);
		}
		if(mostRecentTraversal!=null&& !mostRecentTraversal.isEmpty()){
			//for(Entry<Node, Integer> entry: mostRecentTraversal.entrySet()){
			for(int i=0; i<mostRecentTraversal.size(); i++){
				fill(0, 200, 0);
				stroke(255, 0, 0);
				mostRecentTraversal.get(i).draw(this);
				//entry.getKey().draw(this);
				//inPath.draw(this);
				fill(0, 255, 255);
				
				text(i, mostRecentTraversal.get(i).getPt().x, mostRecentTraversal.get(i).getPt().y);
			}
		}

	}

	public void mouseClicked() {
		float mX = mouseX;
		float mY = mouseY;
		Node closest = g.clickNearest(mX, mY, 15);
		if (closest != null) {
			if (delete) {
				g.deleteNode(closest);
			} else {
				prevActive = active;
				active = closest;
				if (prevActive != null && active != null) {
					if (traverse) {
						mostRecentTraversal = g.traverseGraph(prevActive, active);
						//mostRecentTraversal = g.getTraversalMap(prevActive, active);
					} else {
						g.addConnection(active, prevActive);
					}
					prevActive = null;
					active = null;
				}
			}
		} else {
			// no node is within threshold for selection
			System.out.format("adding node at: %f, %f ....", mX, mY);
			if (g.addNode(mX, mY)) {
				System.out.println("success!");
			} else {
				System.out.println("FAIL!");
			}

		}

	}

	public void keyPressed() {
		switch (key) {
		case 't':
		case 'T':
			traverse = !traverse;
			if(!traverse){
				mostRecentTraversal.clear();
			}
			break;
		case 'd':
		case 'D':
			delete = !delete;
			break;
		default:
			System.out.println("key pressed:" + key);
			break;
		}

	}

}
