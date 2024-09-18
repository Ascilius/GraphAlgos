import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Node {

	// screen
	private int x, y; // screen coordinates
	private double ux, uy; // direction unit vector
	private ArrayList<Node> connectedNodes = new ArrayList<Node>();
	private Color color = null;

	// BFS stuff
	private int dist = -1;
	private Rectangle hitbox;

	// location specified node
	public Node(int x, int y, double ux, double uy, int nodeSize) {
		this.x = x;
		this.y = y;
		this.ux = ux;
		this.uy = uy;
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

	public double getUX() {
		return ux;
	}

	public double getUY() {
		return uy;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color newColor) {
		color = newColor;
	}

	public void resetColor() {
		color = null;
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
		if (hitbox.contains(click)) {
			return true;
		}
		return false;
	}

}
