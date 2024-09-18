import java.awt.Dimension;
import java.util.ArrayList;

public class BipartiteGraph extends Graph {

	private ArrayList<Node> V1 = new ArrayList<Node>();
	private ArrayList<Node> V2 = new ArrayList<Node>();

	public BipartiteGraph() {
		super();
	}

	public BipartiteGraph(int n, Dimension screenSize, int nodeSize) {
		generate(n, screenSize, nodeSize);
	}

	public void generate(int n, Dimension screenSize, int nodeSize) {
		// nodes
		super.V = n;

		// V1 nodes
		int n1 = n / 2;
		int sectionWidth = (int) ((screenSize.getWidth() - nodeSize * 2) / 3);
		int sectionHeight = (int) ((screenSize.getHeight() - nodeSize * n1) / (n1 + 1));
		for (int i = 0; i < n1; ++i) {
			Node newNode = new Node(sectionWidth, sectionHeight * (i + 1) + nodeSize * i, nodeSize);
			V1.add(newNode);
			super.nodes.add(newNode);
		}

		// V2 nodes
		int n2 = n - n1;
		sectionHeight = (int) ((screenSize.getHeight() - nodeSize * n1) / (n1 + 1));
		for (int i = 0; i < n2; ++i) {
			Node newNode = new Node(sectionWidth * 2 + nodeSize, sectionHeight * (i + 1) + nodeSize * i, nodeSize);
			V2.add(newNode);
			super.nodes.add(newNode);
		}

		// edges
		super.E = n * 2;
		for (int i = 0; i < super.E; i++) {
			Edge newEdge = null;
			while (newEdge == null || super.edges.contains(newEdge)) {
				if (super.edges.contains(newEdge)) // debug
					System.out.println("Debug: Duplicate edge; regenerating edge...");
				int i1 = (int) (Math.random() * V1.size());
				int i2 = (int) (Math.random() * V2.size());
				newEdge = new Edge(V1.get(i1), V2.get(i2));
			}
			super.edges.add(newEdge);
		}
	}

	public void clear() {
		super.clear();
		V1.clear();
		V2.clear();
	}

	public void regenerate(int n, Dimension screenSize, int node) {
		clear();
		generate(n, screenSize, node);
	}

}
