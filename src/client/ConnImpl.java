package client;

import java.util.Objects;

import processing.core.PApplet;
import toxi.geom.Line2D;
import toxi.processing.ToxiclibsSupport;

public class ConnImpl implements Connection{
	Node a, b;

	
	public ConnImpl(Node _a, Node _b){
		this.a = _a;
		this.b = _b;
	}
	

	@Override
	public Node[] getNodePair() {
		Node[] nArray = new Node[]{a, b};
		return nArray;
	}

	@Override
	public Node getNodeA() {
		return this.a;
	}

	@Override
	public Node getNodeB() {
		return this.b;
	}

	@Override
	public void draw(Main p) {
		Line2D l = new Line2D(a.getPt(), b.getPt());
		p.gfx.line(l);
		this.a.draw(p);
		this.b.draw(p);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof ConnImpl){
			ConnImpl o  = (ConnImpl) other;
			if(o.a.equals(this.a) && o.b.equals(this.b)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(this.a, this.b);
	}

}
