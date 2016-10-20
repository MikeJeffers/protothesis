package client;

import processing.core.*;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class Main extends PApplet {
	public ToxiclibsSupport gfx;
	Graph g;
	Node active;
	Node prevActive;

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
	}

	public void draw() {
		background(0);
		stroke(255);
		strokeWeight(1);
		fill(111, 5, 5);
		g.draw(this);
		if(active!=null){
			strokeWeight(3);
			fill(222, 5, 5);
			active.draw(this);
		}
		if(prevActive!=null){
			strokeWeight(2);
			fill(222, 5, 5);
			active.draw(this);
		}

	}
	
	
	public void mouseClicked(){
		float mX = mouseX;
		float mY = mouseY;
		Node closest = g.clickNearest(mX, mY, 15);
		if(closest!=null){
			prevActive = active;
			active = closest;
			if(prevActive!=null && active!=null){
				g.addConnection(active, prevActive);
				prevActive = null;
				active = null;
			}
		}else{
			//no node is within threshold for selection
			System.out.format("adding node at: %f, %f ...." ,mX, mY);
			if(g.addNode(mX, mY)){
				System.out.println("success!");
			}else{
				System.out.println("FAIL!");
			}
			
		}
		
	}

}
