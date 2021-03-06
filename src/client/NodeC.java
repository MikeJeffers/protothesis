package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class NodeC implements Node{
	private float x, y;
	private Vec2D pt;
	private List<Node> neighbors;
	private float radius = 15;
	
	public NodeC(float _x, float _y){
		this.x = _x;
		this.y = _y;
		this.pt = new Vec2D(_x, _y);
		this.neighbors = new ArrayList<Node>();
	}


	@Override
	public void draw(Main p) {
		p.gfx.circle(new Vec2D(x, y), radius);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof NodeC){
			NodeC o  = (NodeC) other;
			if(o.x==this.x && o.y==this.y){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(x, y, radius);
	}

	@Override
	public Vec2D getPt() {
		return this.pt;
	}


	@Override
	public boolean addNeighbor(Node n) {
		if(!neighbors.contains(n)){
			return this.neighbors.add(n);
		}
		return false;
	}


	@Override
	public boolean removeNeighbor(Node n) {
		return this.neighbors.remove(n);
		
	}


	@Override
	public List<Node> getNeighbors() {
		return this.neighbors;
	}
	
	@Override
	public String toString(){
		return this.pt.toString();
	}

}
