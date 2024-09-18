
public class Edge {

	private Node n1, n2;
	
	public Edge(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
		n1.connectNode(n2);
		n2.connectNode(n1);
	}
	
	// TOFIX
	public Node getN1() {
		return n1;
	}
	
	public Node getN2() {
		return n2;
	}
	
	// checking if edges are equivalent (same end nodes)
	public boolean equals(Edge e1, Edge e2) {
		Node n11 = e1.getN1();
		Node n12 = e1.getN2();
		Node n21 = e2.getN1();
		Node n22 = e2.getN2();
		
		if ((n11 == n21 || n11 == n22) && (n12 == n21 || n12 == n22))
			return true;
		return false;
	}
	
}
