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
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class GraphAlgosPanel extends JPanel {

	// version
	private final String VER = "hw3.1.0";

	// debug
	private boolean debug = true;

	// screen
	private Dimension screenSize;
	private int screenWidth, screenHeight;
	private boolean invert = false;
	private long startTime = System.currentTimeMillis(), endTime, frameTime, totalTime = 0;
	private int totalFrames = 0;
	private double currentFPS = 0.0;

	// input
	private KeyHandler keyHandler = new KeyHandler();

	// graphs
	private Graph graph;
	private int n = 40, nodeSize = 20;
	private double p = 0.04;
	private int t = 30;
	private Color pathColor = Color.RED;

	public GraphAlgosPanel(Dimension screenSize) {
		// getting screen dimensions
		this.screenSize = screenSize;
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();

		// setting up input handling
		addKeyListener(keyHandler);
		setFocusable(true); // ?

		// generating graphs
		graph = new Graph(n, p, screenSize, nodeSize);
		Graph.runLCC(graph, t);
	}

	// ---------------------------------------------------------------------------
	// drawing

	private void setColor(Graphics2D g) {
		g.setColor(Color.BLACK);
		if (invert)
			g.setColor(Color.WHITE);
	}

	private void paintNodes(Graphics2D g, Graph graph) {
		ArrayList<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			// setting color
			setColor(g);
			Color color = node.getColor();
			if (color != null)
				g.setColor(color);

			// node
			int x = node.getX();
			int y = node.getY();
			g.fillOval(x, y, nodeSize, nodeSize);

			// distance
			double ux = node.getUX();
			double uy = node.getUY();
			x = (int) (x + ux * nodeSize * 1.5) + 7;
			y = (int) (y + uy * nodeSize * 1.5) + 15;
			g.drawString(Integer.toString(node.getDist()), x, y);
		}
	}

	private void paintEdges(Graphics2D g, Graph graph) {
		setColor(g);
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

	private void paintText(Graphics2D g) {
		setColor(g);
		ArrayList<String> text = new ArrayList<String>();
		text.add("Graph Algorithms");
		text.add("ver. " + VER);
		if (debug) {
			text.add("");
			text.add("Debug Mode:");
			text.add("n = " + n);
			text.add("p = " + p);
			text.add("n(n-1)/2 = " + (n * (n - 1) / 2));
			text.add("          |E| = " + graph.E);
			text.add("t = " + t);
			text.add("Component Sizes: " + graph.getSizes().toString());
			text.add("totalTime: " + totalTime);
			text.add("totalFrames: " + totalFrames);
			text.add("currentFPS: " + currentFPS);
		}

		int size = text.size();
		for (int i = 0; i < size; ++i) {
			g.drawString(text.get(i), 5, (i + 1) * 20);
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
		// paintPath(g, graph);

		// text
		paintText(g);

		// frame end
		endTime = System.currentTimeMillis();
		frameTime = endTime - startTime;
		startTime = System.currentTimeMillis();
		// calculating fps
		totalTime += frameTime;
		totalFrames++;
		if (totalFrames == 100) {
			currentFPS = totalFrames / (totalTime / 1000.0);
			totalTime = 0;
			totalFrames = 0;
		}
		// next frame
		repaint();
	}

	// ---------------------------------------------------------------------------
	// input handling

	class KeyHandler extends KeyAdapter {

		public void regenerateGraph() {
			graph.regenerate(n, p);
			Graph.runLCC(graph, t);
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			// toggle debug mode
			if (keyCode == KeyEvent.VK_F3)
				debug = !debug;

			// run LCC algorithm
			else if (keyCode == KeyEvent.VK_L)
				Graph.runLCC(graph, t);

			// graph generation
			else if (keyCode == KeyEvent.VK_R)
				regenerateGraph();

			// adjusting n value
			else if (keyCode == KeyEvent.VK_UP) {
				n += 1;
				regenerateGraph();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				n -= 1;
				if (n < 2)
					n = 2;
				regenerateGraph();
			}

			// adjusting p value
			else if (keyCode == KeyEvent.VK_LEFT) {
				p -= 0.005;
				if (p < 0.01)
					p = 0.01;
				regenerateGraph();
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				p += 0.005;
				regenerateGraph();
			}
		}

		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			// invert colors
			if (keyCode == KeyEvent.VK_CAPS_LOCK)
				invert = !invert;

			// run large-scale test
			else if (keyCode == KeyEvent.VK_SPACE) {
				n = 40;
				for (double c = 0.2; c <= 3.1; c += 0.2) { // TOFIX: floating point error :(
					p = c / n;
					int pass = 0;
					for (int i = 0; i < 500; ++i) {
						graph = new Graph(n, p, screenSize, nodeSize);
						if (Graph.runLCC(graph, t) == 1)
							pass++;
					}
					if (debug)
						System.out.println("Debug: c: " + (Math.round(c * 10) / 10.0) + "; pass rate: " + (pass / 5.0) + "%");
				}
			}

			// quit
			else if (keyCode == KeyEvent.VK_ESCAPE)
				System.exit(0);
		}

	}

}
