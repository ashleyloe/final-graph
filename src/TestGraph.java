import java.awt.Point;
import java.util.ArrayList;


public class TestGraph {

	public static void main(String[] args) {
		Graph<Point, Integer> graph = new Graph<Point, Integer>(); 

		Point p0 = new Point(0,0); 
		Point p1 = new Point(1, 1); 
		Point p2 = new Point(2,2); 
		Point p3 = new Point(3, 3); 
		Point p4 = new Point(4,4); 
		
		
		
		Graph.Node node0 = graph.addNode(p0); //0
		Graph.Node node1 = graph.addNode(p1); //1
		Graph.Node node2 = graph.addNode(p2); //2
		Graph.Node node3 = graph.addNode(p3); //3
		Graph.Node node4 = graph.addNode(p4); //4

		Graph.Edge edge1 = graph.addEdge(23, node0, node1);
		Graph.Edge edge2 = graph.addEdge(24, node2, node3);
		Graph.Edge edge3 = graph.addEdge(25, node4, node1);
		Graph.Edge edge4 = graph.addEdge(25, node0, node4);



		//graph.print(); 

		System.out.println("DFT: "); 
		graph.DFT(node0);
		ArrayList<Graph.Node> list = graph.visitedDFT; 
		for (Graph.Node node: list) {
			Point p = (Point)node.getData(); 
			System.out.print("Point: [" +p.getX() + "," +p.getY() +"]");
		}
		
		System.out.println(); 


		System.out.println("BFT: "); 
		graph.BFT(node0);
		ArrayList<Graph.Node> list2 = graph.visitedBFT; 
		for (Graph.Node node: list2) {
			Point p = (Point)node.getData(); 
			System.out.print("Point: [" +p.getX() + "," +p.getY() +"]");
		}
		System.out.println(); 
		
		
		System.out.println("Unreachable Nodes in DFT: ");
		for (Graph.Node node: graph.nodes) {
			if (!(graph.visitedDFT.contains(node))) {
				Point p = (Point)node.getData(); 
				System.out.println("Point: [" +p.getX() + "," +p.getY() +"], ");
			}
	
	}
	System.out.println("------------------------");

		
		
		
		
	}
}
