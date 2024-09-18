import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {

	private Dimension screenSize;
	private int nodeSize;

	protected int V, n;
	protected ArrayList<Node> nodes = new ArrayList<Node>();

	protected double p;
	protected int E;
	protected ArrayList<Edge> edges = new ArrayList<Edge>();

	protected Node selected = null;
	protected ArrayList<Integer> componentSizes = new ArrayList<Integer>();

	public Graph() {
	}

	public Graph(int n, double p, Dimension screenSize, int nodeSize) {
		this.screenSize = screenSize;
		this.nodeSize = nodeSize;
		generate(n, p);
	}

	/*
	Erdös-Rényi undirected random graph
	 - n, p are for graph generation
	 - screenSize, nodeSize are for aesthetics and interaction
	 */
	public void generate(int n, double p) {
		// saving new values
		this.n = n;
		V = n; // redundant
		this.p = p;

		// generating nodes in a circular shape for easy viewing
		// determining center
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		int xo = screenWidth / 2;
		int yo = screenHeight / 2;
		// determining radius
		double r = yo * 0.75;
		// determining angle
		double theta = 2 * Math.PI / V; // in radians
		// actually creating nodes
		for (int i = 0; i < V; ++i) {
			double ux = Math.cos(theta * i);
			double uy = Math.sin(theta * i);
			int x = (int) (xo + ux * r);
			int y = (int) (yo + uy * r);
			nodes.add(new Node(x, y, ux, uy, nodeSize));
		}

		// generating random edges
		for (int i = 0; i < n; ++i) {
			for (int j = i + 1; j < n; ++j) {
				double q = Math.random();
				if (q < p)
					edges.add(new Edge(nodes.get(i), nodes.get(j)));
			}
		}
		E = edges.size();
	}

	public void clear() {
		V = 0;
		nodes.clear();
		E = 0;
		edges.clear();
		selected = null;
		componentSizes.clear();
	}

	public void regenerate(int n, double p) {
		clear();
		generate(n, p);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	// ---------------------------------------------------------------------------
	// BFS stuff

	public Node getSelected() {
		return selected;
	}

	public void selectRandom() {
		selected = nodes.get((int) (Math.random() * nodes.size()));
	}

	public int runBFS() {
		// selecting random color for component
		Color componentColor = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

		// starting at start
		selected.updateDist(0);

		// BFS loop
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(selected);
		int size = 0; // number of nodes in the component
		while (queue.isEmpty() == false) {
			// getting node
			Node tempSubject = queue.poll();
			tempSubject.setColor(componentColor);
			size++;

			// determining distance & updating queue
			int dist = tempSubject.getDist() + 1;
			ArrayList<Node> connectedNodes = tempSubject.getConnectedNodes(); // getting connected nodes
			for (Node node : connectedNodes) {
				if (node.getDist() != -1)
					continue;
				node.updateDist(dist);
				queue.add(node); // adding connected nodes to queue
			}
		}

		return size;
	}

	public void selectNode(Node node) {
		selected = node;
	}

	public boolean selectNode(Point click) {
		for (Node node : nodes) {
			if (node.clickedOn(click)) {
				selectNode(node);
				return true;
			}
		}
		return false;
	}

	// ---------------------------------------------------------------------------
	// Largest Connected Component (LCC) stuff

	public void importSizes(ArrayList<Integer> sizes) {
		componentSizes = sizes;
	}

	public ArrayList<Integer> getSizes() {
		return componentSizes;
	}

	public static int runLCC(Graph G, int t) {
		// resetting all distances
		ArrayList<Node> nodes = G.getNodes();
		for (Node node : nodes)
			node.updateDist(-1);

		// running BFS until all components found
		ArrayList<Integer> componentSizes = new ArrayList<Integer>();
		for (Node node : nodes) {
			if (node.getDist() == -1) {
				G.selectNode(node);
				componentSizes.add(G.runBFS()); // storing component sizes
			}
		}

		// checking component sizes
		G.importSizes(componentSizes);
		for (Integer size : componentSizes) {
			if (size >= t)
				return 1;
		}
		return 0;
	}

}
