import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JComponent;


/**                                                                                                    
	 * Presents a views of the graph information stored 
	 * in the Graph class                                                                                              
	 *                                                                                                    
	 * @author Ashley Loe                                                          
	 * @version CSC212, 28 April 2014                                                                            
	 */

	

	public class GraphViewer extends JComponent{

	    /* Field that keeps track of the MapGrid to be displayed and additional fields 
	     * describing how the map will be drawn within the viewpoint
	     */
		Graph<Point, Double> graph; 
		LinkedList<Graph.Node> nodes; 	   
		LinkedList<Graph.Edge> edges; 	    	

	    
	    /**
	     * Constructor for MapViewer that sets the magnification and offset
	     * and calls setMinimumSize() and setPreferredSize()
	     * 
	     * @param the graph
	     */
	    public GraphViewer(Graph<Point, Double> graph){
	    	super(); 
	        this.graph = graph;
	    }

	   

	   /**
	    * this method ovverrides paintComponent of graphics
	    * in order to draw circles to represent nodes and lines to represent edges
	    */
	    public void paintComponent(Graphics g){
	    	nodes = graph.nodes; 
	    	//draws a black circle for each node
	    	for (Graph<Point, Integer>.Node<Point> node: nodes) {
	    		Point p = (Point)node.getData(); 
	    		int x = (int)p.getX(); 
	    		int y = (int)p.getY(); 
	    		g.setColor(Color.RED);
	    		g.fillOval(x-10, y-10, 20, 20);
	    		String data = ("Pt: [" +x+ "," + y + "]");  
	    		g.setColor(Color.BLACK);
	    		g.drawString(data, x, y);  
	    	}
	    	
	    	
	    	//draws a line from the head of an edge to its tail
	    	edges = graph.edges; 
	    	for (Graph<Point, Integer>.Edge<Integer> edge: edges) {
	    		Point p1 = (Point)edge.getHead().getData(); 
	    		int x1 = (int)p1.getX(); 
	    		int y1 = (int)p1.getY(); 
	    		
	    		Point p2 = (Point)edge.getTail().getData(); 
	    		int x2 = (int)p2.getX(); 
	    		int y2 = (int)p2.getY(); 
	    		g.setColor(Color.BLUE);
	    		g.drawLine(x1, y1, x2, y2);
	    		
	    		String data = String.format("%.2f", edge.getData());  
	    		g.setColor(Color.BLACK);
	    		g.drawString(data, (x1+x2)/2, (y1+y2)/2);
	    	}
	    	 
	    } 
	   
	

	

	/**
	 *  The component will look bad if it is sized smaller than this
	 *
	 *  @returns The minimum dimension
	 */
	public Dimension getMinimumSize() {
		return new Dimension(500,500);
	}

	/**
	 *  The component will look best at this size
	 *
	 *  @returns The preferred dimension
	 */
	public Dimension getPreferredSize() {
		return new Dimension(500,500);
	}
}
	
