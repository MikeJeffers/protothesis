package client;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.kmeans.KMeansPDN;
import jsat.linear.DenseVector;
import jsat.linear.SparseVector;
import jsat.linear.Vec;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
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
	boolean compute = true;
	List<Node> mostRecentTraversal = new ArrayList<Node>();
	// Map<Node, Integer> mostRecentTraversal = new HashMap<Node, Integer>();

	List<List<Vec2D>> clusterFucks = new ArrayList<List<Vec2D>>();
	List<int[]> colors = new ArrayList<int[]>();

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
		if (mostRecentTraversal != null && !mostRecentTraversal.isEmpty()) {
			// for(Entry<Node, Integer> entry: mostRecentTraversal.entrySet()){
			for (int i = 0; i < mostRecentTraversal.size(); i++) {
				fill(0, 200, 0);
				stroke(255, 0, 0);
				mostRecentTraversal.get(i).draw(this);
				// entry.getKey().draw(this);
				// inPath.draw(this);
				fill(0, 255, 255);

				text(i, mostRecentTraversal.get(i).getPt().x, mostRecentTraversal.get(i).getPt().y);
			}
		}

		if (!clusterFucks.isEmpty() && colors.size()==clusterFucks.size()) {
			for (int i=0; i<clusterFucks.size(); i++) {
				List<Vec2D> vecList = clusterFucks.get(i);
				int[] c = colors.get(i);
				stroke(c[0], c[1], c[2], 155);
				fill(c[0], c[1], c[2], 155);
				strokeWeight(5);
				for (Vec2D v : vecList) {
					gfx.circle(v, 15);
				}
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
						// mostRecentTraversal = g.getTraversalMap(prevActive,
						// active);
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
		case 'c':
		case 'C':
			compute = true;
			clusterFucks.clear();
			colors.clear();
			System.out.println("Clearing clusters");
			break;
		case 't':
		case 'T':
			traverse = !traverse;
			if (!traverse) {
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

		if (0 < charToInt(key) && charToInt(key) < 10 ) {
			if(compute){
				if(g.getNodes().size()>10){
					int numClusters = charToInt(key);
					System.out.println("Looking for "+numClusters+" clusters");
					clusterFucks = getAllClusters(g.getNodes(), numClusters);
					colors = createColorMarkers(clusterFucks.size());
					System.out.println("Found "+clusterFucks.size()+" Clusters!");
					compute = false;
				}else{
					System.out.println("Too few nodes to run clustering");
				}
			}
			
		}

	}

	private int charToInt(char c) {
		return Character.getNumericValue(c);
	}
	
	private List<List<Vec2D>> getAllClusters(List<Node> nodes, int numClusters) {
		List<List<Vec2D>> clusters = new ArrayList<List<Vec2D>>();
		KMeansPDN km = new KMeansPDN();
		List<DataPoint> pts = new ArrayList<DataPoint>();
		for(Node n: nodes){
			pts.add(vec2Data(n.getPt()));
		}
		DataSet<SimpleDataSet> dSet = new SimpleDataSet(pts);
		
		List<List<DataPoint>>listOfClusters = km.cluster(dSet);
		for(List<DataPoint> listPts: listOfClusters){
			List<Vec2D> vec2DList = new ArrayList<Vec2D>();
			for(DataPoint p: listPts){
				vec2DList.add(data2Vec(p));
			}
			clusters.add(vec2DList);
		}
		
		return clusters;
	}
	

	private List<int[]> createColorMarkers(int numUnique){
		List<int[]> cList = new ArrayList<int[]>();
		for(int i=0; i<numUnique; i++){
			cList.add(colorRand());
		}
		return cList;
	}
	
	private int[] colorRand(){
		return new int[]{(int) random(0, 255), (int) random(0, 255), (int) random(0, 255)};
	}
	
	private DataPoint vec2Data(Vec2D v){
		return new DataPoint(new DenseVector(new double[]{v.x, v.y}));
	}
	
	private Vec2D data2Vec(DataPoint p){
		Vec v = p.getNumericalValues();
		float x =(float) v.get(0);
		float y =(float) v.get(1);
		return new Vec2D(x, y);
	}


}
