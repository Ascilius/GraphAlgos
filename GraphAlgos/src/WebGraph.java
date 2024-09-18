import java.awt.Dimension;
import java.util.ArrayList;

public class WebGraph extends Graph {

	public WebGraph() {
		super();
	}

	// TODO
	public void generate(int n, Dimension screenSize, int nodeSize) {
		// nodes
		super.V = n;
		for (int i = 0; i < V; ++i)
			super.newNode(screenSize, nodeSize);

		// edges
		ArrayList<Node> remNodes = new ArrayList<Node>(); // remaining nodes
		for (Node node : super.nodes)
			remNodes.add(node);
		while (!remNodes.isEmpty()) {

		}

	}

	public void regenerate(int n, Dimension screenSize, int node) {
		super.clear();
		generate(n, screenSize, node);
	}

}
