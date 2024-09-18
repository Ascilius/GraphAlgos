import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GraphAlgosDriver {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Graph Algorithms");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.add(new GraphAlgosPanel(screenSize));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(1080, 720));
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}
