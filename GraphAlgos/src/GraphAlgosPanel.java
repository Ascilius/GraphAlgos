import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphAlgosPanel extends JPanel {

	// debug
	private final boolean debug = true;

	// screen
	private Dimension screenSize;
	private int screenWidth, screenHeight;
	private boolean invert = false;

	// input
	private KeyHandler keyHandler = new KeyHandler();
	private MouseHandler mouseHandler = new MouseHandler();

	// graphs
	private Graph graph;
	private int nodes = 20, nodeSize = 10;
	private Color pathColor = Color.RED;

	public GraphAlgosPanel(Dimension screenSize) {
		// getting screen dimensions
		this.screenSize = screenSize;
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();

		// setting up input handling
		addKeyListener(keyHandler);
		addMouseListener(mouseHandler);
		setFocusable(true); // ?
		// setFocusTraversalKeysEnabled(false); // ?

		// generating graphs
		graph = new Graph(nodes, screenSize, nodeSize);
	}

	// ---------------------------------------------------------------------------
	// drawing

	private void paintNodes(Graphics2D g, Graph graph) {
		g.setColor(Color.BLACK);
		if (invert)
			g.setColor(Color.WHITE);
		ArrayList<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			int x = node.getX();
			int y = node.getY();
			g.drawString(Integer.toString(node.getDist()), x, (int) (y + 2.5 * nodeSize)); // printing distance from subject node
			g.fillOval(x, y, nodeSize, nodeSize); // drawing node
		}
	}

	private void paintEdges(Graphics2D g, Graph graph) {
		g.setColor(Color.BLACK);
		if (invert)
			g.setColor(Color.WHITE);
		ArrayList<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			Node n1 = edge.getN1();
			int x1 = n1.getX() + nodeSize / 2;
			int y1 = n1.getY() + nodeSize / 2;
			Node n2 = edge.getN2();
			int x2 = n2.getX() + nodeSize / 2;
			int y2 = n2.getY() + nodeSize / 2;
			g.drawLine(x1, y1, x2, y2);
		}
	}

	private void paintPath(Graphics2D g, Graph graph) {
		g.setColor(pathColor);

		// nodes
		ArrayList<Node> pathNodes = graph.getPathNodes();
		for (Node pathNode : pathNodes) {
			int x = pathNode.getX();
			int y = pathNode.getY();
			g.drawString(Integer.toString(pathNode.getDist()), x, (int) (y + 2.5 * nodeSize)); // printing distance from subject node
			g.fillOval(x, y, nodeSize, nodeSize); // drawing node
		}

		// edges
		int size = pathNodes.size();
		for (int i = 1; i < size; ++i) {
			Node n1 = pathNodes.get(i - 1);
			int x1 = n1.getX() + nodeSize / 2;
			int y1 = n1.getY() + nodeSize / 2;
			Node n2 = pathNodes.get(i);
			int x2 = n2.getX() + nodeSize / 2;
			int y2 = n2.getY() + nodeSize / 2;
			g.drawLine(x1, y1, x2, y2);
		}

	}

	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics; // why do i do this again?

		// background
		g.setColor(Color.WHITE);
		if (invert)
			g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// graph
		paintEdges(g, graph);
		paintNodes(g, graph);

		// path
		paintPath(g, graph);
	}

	// ---------------------------------------------------------------------------
	// input handling

	class KeyHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			// run BFS algorithm
			if (keyCode == KeyEvent.VK_SPACE) {
				graph.selectRandom();
				graph.runBFS();
			}

			repaint();
		}

		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (debug)
				System.out.println("Debug: keyCode: " + e.getKeyCode());

			if (keyCode == KeyEvent.VK_CAPS_LOCK)
				invert = !invert;

			// quit
			else if (keyCode == KeyEvent.VK_ESCAPE)
				System.exit(0);

			// graph generation
			else if (keyCode == KeyEvent.VK_R)
				graph = new Graph(nodes, screenSize, nodeSize);
			else if (keyCode == KeyEvent.VK_B)
				graph = new BipartiteGraph(nodes, screenSize, nodeSize);
			else if (keyCode == KeyEvent.VK_UP) {
				nodes += 10;
				graph.regenerate(nodes, screenSize, nodeSize);
			} else if (keyCode == KeyEvent.VK_DOWN) {
				nodes -= 10;
				if (nodes < 10)
					nodes = 10;
				graph.regenerate(nodes, screenSize, nodeSize);
			}

			repaint();
		}

	}

	class MouseHandler extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			Point click = e.getLocationOnScreen();
			int button = e.getButton();

			boolean result = graph.selectNode(click, button);

			if (debug) {
				System.out.println("Debug: click: " + click.toString());
				System.out.println("Debug: result: " + result);
			}

			repaint();
		}

	}

}
