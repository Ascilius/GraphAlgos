import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Node {

	private int x, y; // screen coordinates
	private ArrayList<Node> connectedNodes = new ArrayList<Node>();

	// BFS stuff
	private int dist = -1;
	private Rectangle hitbox;

	// randomly located node
	public Node(Dimension screenSize, int nodeSize) {
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		x = (int) (Math.random() * screenWidth * 0.75 + screenWidth * 0.125);
		y = (int) (Math.random() * screenHeight * 0.75 + screenHeight * 0.125);
		hitbox = new Rectangle(x, y, nodeSize, nodeSize);
	}

	// location specified node
	public Node(int x, int y, int nodeSize) {
		this.x = x;
		this.y = y;
		hitbox = new Rectangle(x, y, nodeSize, nodeSize);
	}

	public void connectNode(Node otherNode) {
		connectedNodes.add(otherNode);
	}

	public ArrayList<Node> getConnectedNodes() {
		return connectedNodes;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// ---------------------------------------------------------------------------
	// BFS stuff

	public void updateDist(int newDist) {
		dist = newDist;
	}

	public int getDist() {
		return dist;
	}

	public boolean clickedOn(Point click) {
		if (hitbox.contains(click))
			return true;
		return false;
	}

}
