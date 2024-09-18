import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {

	protected int V;
	protected ArrayList<Node> nodes = new ArrayList<Node>();

	protected int E;
	protected ArrayList<Edge> edges = new ArrayList<Edge>();

	protected Node start = null; // one of the existing nodes
	protected Node end = null;
	protected ArrayList<Node> pathNodes = new ArrayList<Node>();
	// protected ArrayList<Edge> pathEdges = new ArrayList<Edge>(); // TOREMOVE: unused

	public Graph() {
	}

	public Graph(int n, Dimension screenSize, int nodeSize) {
		generate(n, screenSize, nodeSize);
	}

	public void generate(int n, Dimension screenSize, int nodeSize) {
		V = n;

		// generating random nodes
		for (int i = 0; i < V; ++i)
			newNode(screenSize, nodeSize);

		// generating random edges
		E = (int) (1.5 * n);
		for (int i = 0; i < E; ++i)
			newEdge();

		/*
		for (int n1 = 0; n1 < V; ++n1) {
			// getting second node
			int n2 = (int) (Math.random() * (V - 1));
			if (n2 <= n1)
				n2++;
		
			// generating edge; TOFIX: may be duplicate
			Node node1 = nodes.get(n1);
			Node node2 = nodes.get(n2);
			edges.add(new Edge(node1, node2));
		}
		*/
	}

	public void clear() {
		V = 0;
		nodes.clear();
		E = 0;
		edges.clear();
		start = null;
		end = null;
		pathNodes.clear();
	}

	public void regenerate(int n, Dimension screenSize, int node) {
		clear();
		generate(n, screenSize, node);
	}

	// generates a random node
	public void newNode(Dimension screenSize, int nodeSize) {
		nodes.add(new Node(screenSize, nodeSize));
	}

	// generates a random edge
	public void newEdge() {
		Edge newEdge = null;
		while (newEdge == null || edges.contains(newEdge)) {
			if (edges.contains(newEdge)) // debug
				System.out.println("Debug: Duplicate edge; regenerating edge...");
			int i1 = (int) (Math.random() * V);
			int i2 = i1;
			while (i1 == i2)
				i2 = (int) (Math.random() * V);
			newEdge = new Edge(nodes.get(i1), nodes.get(i2));
		}
		edges.add(newEdge);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	// ---------------------------------------------------------------------------
	// BFS stuff

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	public ArrayList<Node> getPathNodes() {
		return pathNodes;
	}

	public void selectRandom() {
		selectStart(nodes.get((int) (Math.random() * nodes.size())));
	}

	public void selectStart(Node selected) {
		pathNodes.clear();
		start = selected;
		pathNodes.add(start);
		runBFS();
	}

	public void selectEnd(Node selected) {
		if (start == null)
			selectStart(selected);
		else {
			pathNodes.clear();
			pathNodes.add(start);
			end = selected;
			tracePath();
		}
	}

	public void runBFS() {
		// resetting all distances
		for (Node node : nodes)
			node.updateDist(-1);

		// starting at start
		start.updateDist(0);

		// BFS loop
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(start);
		while (queue.isEmpty() == false) {
			// getting node
			Node tempSubject = queue.poll();

			// determining distance & updating queue
			int dist = tempSubject.getDist() + 1;
			ArrayList<Node> connectedNodes = tempSubject.getConnectedNodes();
			for (Node node : connectedNodes) {
				if (node.getDist() != -1)
					continue;
				node.updateDist(dist);
				queue.add(node);
			}
		}
	}

	public void tracePath() {
		// run BFS
		runBFS();

		// starting at end
		Node next = end;

		// tracing back to start
		while (next != start) {
			// adding to path
			pathNodes.add(1, next);

			// finding next node
			ArrayList<Node> connectedNodes = next.getConnectedNodes();
			int min = next.getDist();
			Node nextNode = null;
			for (Node node : connectedNodes) {
				int newMin = node.getDist();
				if (newMin < min) {
					min = newMin;
					nextNode = node;
				}
			}

			// TOFIX: no next node was found; break
			/*
			if (min != 0 && nextNode == null)
				break;
			*/

			next = nextNode;
		}
	}

	public boolean selectNode(Point click, int button) {
		for (Node node : nodes) {
			if (node.clickedOn(click)) {
				if (button == MouseEvent.BUTTON1)
					selectStart(node);
				else if (button == MouseEvent.BUTTON3)
					selectEnd(node);
				return true;
			}
		}
		return false;
	}

}
