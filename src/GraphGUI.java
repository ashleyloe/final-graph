import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.*;




/**                                                                                                    
 * Presents a GUI mockup of a graph class that displays nodes and edges
 * added user interaction includes the ability to add/remove nodes and edges
 * as well as call DFS, BFS, Dijskstra's Algorithm on the graph                                                                                            
 *                                                                                                    
 * @author Ashley Loe                                                          
 * @version CSC212, 28 April 2014                                                                            
 */



public class GraphGUI {

	public GraphGUI() {

	}

	//holds information about the node under the mouse
	Graph<Point, Double>.Node<Point> nodeUnderMouse;


	//constructs a new graph
	private Graph<Point, Double> graph = new Graph<Point, Double>(); 


	//view of graph to be displayed 
	private GraphViewer view;


	//Label for the input mode instructions
	private JLabel instr;

	//The input mode
	InputMode mode = InputMode.ADD_NODE;


	//keep track of edge data
	Double edgeCost; 

	//keeps track of node that is nearby for mouse events
	Graph<Point, Double>.Node<Point> nearbyNode;



	/**
	 *  Schedules a job for the event-dispatching thread
	 *  creating and showing this application's GUI.
	 */
	public static void main(String[] args) {
		final GraphGUI GUI = new GraphGUI();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI.createAndShowGUI();
			}
		});
	}



	/** Sets up the GUI window */
	public void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Graph GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add components
		createComponents(frame);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}


	/**
	 * Creates the pane that will hold the panel of buttons, 
	 * canvas for the graph to be drawn on, and text 
	 * to be displayed to the user
	 * @param frame to draw the GUI in
	 */

	public void createComponents(JFrame frame){
		Container pane = frame.getContentPane();
		view = new GraphViewer(graph);

		PointMouseListener pml = new PointMouseListener();
		view.addMouseListener(pml);
		view.addMouseMotionListener(pml);


		pane.setLayout(new FlowLayout());
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());

		//displays instructions to the user
		instr = new JLabel("Click to add new points; drag to move.");
		panel1.add(instr,BorderLayout.NORTH);


		//controls--creates the various buttons
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0,1));

		JButton addNodeButton = new JButton("Add Node");
		panel2.add(addNodeButton);
		addNodeButton.addActionListener(new addNodeListener());

		JButton addEdgeButton = new JButton("Add Edge");
		panel2.add(addEdgeButton);
		addEdgeButton.addActionListener(new addEdgeListener());


		JButton removeNodeButton = new JButton("Remove Node");
		panel2.add(removeNodeButton);
		removeNodeButton.addActionListener(new removeNodeListener());

		JButton removeEdgeButton = new JButton("Remove Edge");
		panel2.add(removeEdgeButton);
		removeEdgeButton.addActionListener(new removeEdgeListener());

		JButton DFTButton = new JButton("DFT"); 
		panel2.add(DFTButton); 
		DFTButton.addActionListener(new DFTListener()); 

		JButton BFTButton = new JButton("BFT"); 
		panel2.add(BFTButton); 
		BFTButton.addActionListener(new BFTListener()); 


		JButton DijButton = new JButton("Dijkstra's Algorithm"); 
		panel2.add(DijButton); 
		DijButton.addActionListener(new DijListener()); 


		//adds all the components to the main pane
		panel1.add(view); 
		pane.add(panel1);
		pane.add(panel2); 


	}


	/** 
	 * Returns a node found within the drawing radius of the given location, 
	 * or null if none
	 *
	 *  @param x  the x coordinate of the location
	 *  @param y  the y coordinate of the location
	 *  @return  a bide from the canvas if there is one covering this location, 
	 *  or a null reference if not
	 */
	@SuppressWarnings("unchecked")
	public Graph<Point, Double>.Node<Point> findNearbyNode(int x, int y) {
		Point p= null; 

		/*iterates through all the nodes in the graph to check if one of them
		has the same data at this point*/
		for (Graph<Point, Double>.Node<Point> node: graph.getNodes()) {
			p = node.getData(); 

			//if less than the radius, it means it must occupy the same point
			if (p.distance(x,y) < 10) {
				return node; 
			}
		}

		//return null if node is not found
		return null;
	}



	/** Constants for recording the input mode */
	enum InputMode {
		ADD_NODE, RMV_NODE, ADD_EDGE, RMV_EDGE, DIJ, DFT, BFT
	}


	public class addNodeListener implements ActionListener {
		/**
		 *  ActionListener for addNode
		 *  changes the input mode and prints text to the user
		 *  about how to add a node
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ADD_NODE;
			instr.setText("Click to add new points or click and drag change their location."); 
		}

	}

	class addEdgeListener implements ActionListener {
		/**
		 *  ActionListener for add edge
		 *  changes the input mode and prints text to the user
		 *  about how to add an edge
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.ADD_EDGE; 
			instr.setText("Click and drag from one node to another to add an edge.");
		}

	}

	class removeNodeListener implements ActionListener {
		/**
		 *  ActionListener for remove node
		 *  changes the input mode and prints text to the user
		 *  about how to remove a node
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_NODE; 
			instr.setText("Click on a node to remove it.");
		}

	}

	class removeEdgeListener implements ActionListener {
		/**
		 *  Actionlistener for remove edge
		 *  changes the input mode and prints text to the user
		 *  about how to remove an edge
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.RMV_EDGE; 
			instr.setText("Click from one node to another to remove edge.");
		}

	}

	public class DFTListener implements ActionListener {
		/**
		 *  Actionlistener for depth first traversal
		 *  changes the input mode and prints text to the user
		 *  about how to call DFT on a node
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.DFT;
			instr.setText("Click on a node to use DFT on it."); 
		}

	}


	public class BFTListener implements ActionListener {
		/**
		 *  actionlistener for breadth first traversal
		 *  changes the input mode and prints text to the user
		 *  about how to call BFT on a node
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.BFT;
			instr.setText("Click on a node to use BFT on it"); 
		}

	}


	public class DijListener implements ActionListener {
		/**
		 *  actionlistener for dijkstra's algorithm
		 *  changes the input mode
		 *
		 *  @param e  Holds information about the button-push event
		 */
		public void actionPerformed(ActionEvent e) {
			mode = InputMode.DIJ;
			instr.setText("Click on a node to use Dijakstra's Algorithm on it."); 
		}

	}



	/** Mouse event handlers */
	private class PointMouseListener extends MouseAdapter
	implements MouseMotionListener {

		/** Responds to click event depending on mode */
		public void mouseClicked(MouseEvent e) {
			int x = e.getX(); 
			int y = e.getY(); 
			switch (mode) {
			case ADD_NODE:
				//checks if a node already exists
				if (findNearbyNode(x, y) == null) {
					Point newPoint = new Point(x, y); 
					graph.addNode(newPoint); 
					view.repaint(); 
				} else {
					// If the click is not on top of an existing point, create a new one and add it to the canvas.
					// Otherwise, emit a beep, as shown below:
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case RMV_NODE:

				Graph<Point, Double>.Node<Point> nearbyNode = findNearbyNode(x,y);

				//check if node already exists
				if (nearbyNode != null) { 
					graph.removeNode(nearbyNode); 
					view.repaint(); 
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;


			case BFT:
				nodeUnderMouse = findNearbyNode(x,y); 
				if (nodeUnderMouse != null) {
					System.out.println("Breadth First Traversal: "); 
					
					//call BFT on that node clicked on
					graph.BFT(nodeUnderMouse);
					ArrayList<Graph.Node> BFTlist = graph.visitedNodesBFT; 
					
					//prints out nodes traversed (in order)
					for (Graph.Node node: BFTlist) {
						Point p = (Point)node.getData(); 
						System.out.println("Point: [" +p.getX() + "," +p.getY() +"] ");				
					}

					System.out.println(); 
					System.out.println("------------------------"); 
					System.out.println("Unreachable Nodes in BFT:"); 	
					for (Graph.Node node: graph.getNodes()) {
						if (!(BFTlist.contains(node))) {
							Point p = (Point)node.getData(); 
							System.out.println("Point: [" +p.getX() + "," +p.getY() +"] ");
						}


					}
					System.out.println("------------------------");

				}
				break; 

			case DFT: {
				nodeUnderMouse = findNearbyNode(x,y); 
				if (nodeUnderMouse !=null) {
					System.out.println("Depth First Traversal: "); 

					//calls DFT on the node clicked on
					graph.DFT(nodeUnderMouse);
					ArrayList<Graph.Node> DFTlist = graph.visitedNodesDFT; 
					for (Graph.Node node: DFTlist) {
						Point p = (Point)node.getData(); 
						System.out.println("Point: [" +p.getX() + "," +p.getY() +"] ");				
					}

					System.out.println(); 
					System.out.println("------------------------");
					System.out.println("Unreachable Nodes in DFT: ");
					for (Graph.Node node: graph.getNodes()) {
						if (!(DFTlist.contains(node))) {
							Point p = (Point)node.getData(); 
							System.out.println("Point: [" +p.getX() + "," +p.getY() +"] ");
						}

					}
					System.out.println("------------------------");

				}
			}
			break; 

			case DIJ: 
				nodeUnderMouse = findNearbyNode(x, y); 
				if (nodeUnderMouse != null) {
					System.out.println("Dijkstra's Algorithm"); 

					HashMap<Graph.Node, Double> algorithmMap = graph.distances(nodeUnderMouse); 

					for (Entry<Graph.Node, Double> entry: algorithmMap.entrySet()) {
						Graph.Node key = entry.getKey(); 
						Point p = (Point)key.getData(); 
						
						Double value = entry.getValue(); 
						System.out.println("Entry: key (" + p.getX()+ "," +p.getY()+")"+ "Value: " + value); 
					}


				}

				break; 

			}
		}
		//Record point under mouse, if any
		/** Records point under mousedown event in anticipation of possible drag */
		public void mousePressed(MouseEvent e) {
			int x = e.getX(); 
			int y = e.getY(); 
			switch(mode) {
			case ADD_NODE:
				if (findNearbyNode(x,y) != null) {
				nodeUnderMouse = findNearbyNode(x, y); 
				}
				break; 
			case ADD_EDGE: 
				if (findNearbyNode(x,y) != null) {
					nodeUnderMouse = findNearbyNode(x, y);
				}
				break;
			case RMV_EDGE:
				if (findNearbyNode(x, y) != null) {
					nodeUnderMouse = findNearbyNode(x,y); 
					view.repaint(); 
				}
				break;
			}


		}

		/** Responds to mouseup event */
		//Clear record of point under mouse, if any
		public void mouseReleased(MouseEvent e) {
			int x = e.getX(); 
			int y = e.getY(); 
			switch(mode) {
			case ADD_NODE:
				//reset nodeUnderMouse
				nodeUnderMouse = null; 
				break; 
			case ADD_EDGE:
				nearbyNode = findNearbyNode(x, y); 
				if ((nodeUnderMouse != null) && (nearbyNode !=null)) {
					
					//finds the distance between the head and tail 
					edgeCost = graph.findCost(nodeUnderMouse, nearbyNode); 
					
					//checks to see if an edge connecting these two nodes already exists
					if (!nodeUnderMouse.isNeighbor(nearbyNode)) {
						graph.addEdge(edgeCost, nodeUnderMouse, nearbyNode); 
						nodeUnderMouse = null; 
						view.repaint(); 
					}
				}
			case RMV_EDGE:
				if (nodeUnderMouse != null) {
					nearbyNode = findNearbyNode(x,y); 
					Graph<Point, Double>.Edge<Double> edgeToRemove = graph.findEdge(nodeUnderMouse, nearbyNode); 
					graph.removeEdge(edgeToRemove);
					view.repaint(); 
					break;

				}

			}

		}


		/** Responds to mouse drag event */
		public void mouseDragged(MouseEvent e) {
			int x = e.getX(); 
			int y = e.getY();
			Point point = new Point(x, y); 
			switch(mode) {
			case ADD_NODE:
				if (nodeUnderMouse != null) {
					nodeUnderMouse.setData(point); 
				}
				break; 
			}

			view.repaint(); 

		}

		// Empty but necessary to comply with MouseMotionListener interface.
		public void mouseMoved(MouseEvent e) {
		}


	}


}







	
	



	
	