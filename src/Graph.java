

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;


/**
 * Creates a graph data structure
 * @author Ashley Loe
 * @version CSC 212, April 22th, 2014
 * @param <V> vertices
 * @param <E> edges
 */

public class Graph<V,E> {

	//masterlist of edges
	@SuppressWarnings("rawtypes")
	protected LinkedList<Graph.Edge> edges;
	
	
	//masterlist of nodes
	@SuppressWarnings("rawtypes")
	protected LinkedList<Graph.Node> nodes;
	
	//list of nodes traversed by BFT
	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> visitedNodesBFT = new ArrayList<Graph.Node>(); 
	
	//list of nodes traversed by DFT
	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> visitedNodesDFT = new ArrayList<Graph.Node>(); 

	
	//arraylist to store visited nodes during traversal
	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> visitedDFT = new ArrayList<Graph.Node>(); 
	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> visitedBFT = new ArrayList<Graph.Node>(); 

	//arraylist to store unvisited nodes
	@SuppressWarnings("rawtypes")
	protected ArrayDeque<Graph.Node> unvisitedBFT = new ArrayDeque<Graph.Node>(); 

	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> unvisitedDij = new ArrayList<Graph.Node>(); 
	@SuppressWarnings("rawtypes")
	protected ArrayList<Graph.Node> visitedDij = new ArrayList<Graph.Node>(); 


	
	Graph.Node lowestNode; 

	
	//constructor for graph
	@SuppressWarnings("rawtypes")
	public Graph() {
		//masterlists of edges and nodes
		edges = new LinkedList<Graph.Edge>(); 
		nodes = new LinkedList<Graph.Node>(); 
		
	}
	/**
	 * accessor for masterlist of nodes
	 * @return all edges in graph
	 */
	@SuppressWarnings("rawtypes")
	public LinkedList<Graph.Node> getNodes() {
		return nodes; 
	}
	
	/**
	 * acessor for masterlist of edges
	 * @return all edges in graph
	 */
	public LinkedList<Graph.Edge> getEdges() {
		return edges; 
	}
	
	/**
	 * accessor for nodes
	 * @param i index of node
	 * @return node at specific index
	 */
	@SuppressWarnings("rawtypes")
	public Graph.Node getNode(int i) {
		Node node = nodes.get(i); 
		return node; 
	}
	
	/**
	 * accessor for edge
	 * @param i index of edge
	 * @return 
	 */
	
	@SuppressWarnings("rawtypes")
	public Graph.Edge getEdge(int i) {
		Edge edge = edges.get(i); 
		return edge; 
	}
	
	/**
	 * accessor for edges that have a given head or tail (undirected graph)
	 * @param head: head (or tail) of the edge
	 * @param tail: tail (or head) of the edge
	 * @return edge: edge with the given head and tail
	 */
	
	@SuppressWarnings("rawtypes")
	public Graph.Edge findEdge(Graph.Node head, Graph.Node tail) {
		Graph.Edge edge = null; 
		for (Graph.Edge e: edges) {
			//find the edge having the head or tail (could be either bc this is an undirected graph)
			if (((e.head == head) && (e.tail ==tail)) || (e.tail == head) && (e.head == tail) ) {
				edge = e; 
			}
		}
		return edge;
	}
	
	
	/**
	 * accessor for specific edges
	 * @param head: head of the edge
	 * @param tail: tail of the edge
	 * @return edge: edge with the given head and tail
	 */
	
	@SuppressWarnings("rawtypes")
	public Graph.Edge EdgeRef(Graph.Node head, Graph.Node tail) {
		Graph.Edge edge = null; 
		for (Graph.Edge e: edges) {
			if ((e.head == head) && (e.tail ==tail)) {
				edge = e; 
			}
		}
		return edge;
	}
	
	/**
	 * Accessor for number of edges
	 * @return numEdges the number of edges in the graph
	 */
	public int numEdges() {
		int numEdges = edges.size(); 
		return numEdges; 
	}
	
	/**
	 * Accessor for number of nodes
	 * @return numNodes the number of nodes in the graph
	 */
	public int numNodes() {
		int numNodes = nodes.size(); 
		return numNodes; 
	}
	
	/**
	 * Adds a node to the graph
	 * @param key data of the node to be added
	 * @return newNode the new node created
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Graph.Node addNode(V data) {
		Node newNode = new Graph.Node(data); 
		if (!nodes.contains(newNode)) {
		nodes.add(newNode);
		return newNode; 
		}
		return null; 
	}
	
	
	
	/**
	 * Adds an edge
	 * @param data key of the edge to be added
	 * @param head of the edge
	 * @param tail of the edge
	 * @return newEdge the new edge created
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Graph.Edge addEdge(E data, Graph.Node head, Graph.Node tail) {
		Graph.Edge newEdge = new Graph.Edge(data, head, tail);
		if (!edges.contains(newEdge)) {
			edges.add(newEdge); 
			head.nodeEdges.add(newEdge);
			tail.nodeEdges.add(newEdge); 
		}
		return null; 
	}
	
	/**
	 * Removes a node and all the edges attached to it
	 * @param node node to remove
	 */
	@SuppressWarnings("rawtypes")
	public void removeNode(Graph.Node node) {
		nodes.remove(node); 
		LinkedList<Graph.Edge> tempEdges = node.nodeEdges; 	
		for (Graph.Edge e: tempEdges) {
			removeEdge(e);
		}
		
	}
	
	/**
	 * removes an edge
	 * @param edge to be removed
	 */
	@SuppressWarnings("rawtypes")
	public void removeEdge(Graph.Edge edge) {
		edges.remove(edge); 
		for (Graph.Node n: nodes) {
			if (n.nodeEdges.contains(edge)) {
				n.removeEdgeRef(edge); 
			}
		}
	}
	
	
	/**
	 * finds the cost(weight) for each edge 
	 * (distance from head to node)
	 * @param head of the edge
	 * @param tail of the edge
	 * @return cost(weight) of the edge
	 */
	@SuppressWarnings("rawtypes")
	public double findCost(Graph.Node head, Graph.Node tail) {
		Point tailPoint = (Point) tail.getData(); 
		Point headPoint = (Point) head.getData(); 
		double cost = headPoint.distance(tailPoint);
		return cost; 

	}
	
	
	
	/**
	 * traverses the graph using breadth first traversal
	 * @param start node
	 */
	@SuppressWarnings("rawtypes")
	public void BFT(Graph.Node start) {
		visitedNodesBFT = BFTHelper(start); 
		visitedBFT = new ArrayList<Graph.Node>(); 
		unvisitedBFT.clear(); 
		
	}

	/**
	 * BFT helper method that is recursive
	 * @param start node
	 * @return list of nodes traversed (in order)
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<Graph.Node> BFTHelper(Graph.Node start) {
		
		//adds node to visited
		visitedBFT.add(start);	
		LinkedList<Graph.Node> neighbors = start.getNeighbors();
		if (!neighbors.isEmpty()) {
			
			//add all available neighbors to the list of unvisited nodes
			unvisitedBFT.addAll(neighbors); 
			while (!unvisitedBFT.isEmpty()) {
				Graph.Node first = unvisitedBFT.removeFirst(); 
				if (!visitedBFT.contains(first)) {
					
					//recursive call
					BFTHelper(first); 
				} 
			}

		}
		return visitedBFT; 
	}


	/**
	 * traverses the graph using depth first traversal
	 * @param start
	 */
	@SuppressWarnings("rawtypes")
	public void DFT(Graph.Node start) {
		visitedNodesDFT = DFTHelper(start); 
		//resets the list
		visitedDFT = new ArrayList<Graph.Node>();  
	}

	/**
	 * DFT helper method that is recursive
	 * @param start node
	 * @return list of nodes traversed (in order)
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<Graph.Node> DFTHelper(Graph.Node start) {
		
		//adds the node to the list of visited nodes
		visitedDFT.add(start); 
		LinkedList<Graph.Node> neighbors = start.getNeighbors(); 
		if (!neighbors.isEmpty()) {
			//traverse the neighbors
			for (Graph.Node node: neighbors) {
				while (!visitedDFT.contains(node)) {
					//recursive call
					DFTHelper(node); 
				}
			}

		}
		return visitedDFT; 

	}
	


	/**
	 * implementation of dijkstra's algorithm that finds the 
	 * shortest weight paths (least costly) to each of the nodes of the graph
	 * from the specified start node
	 *  
	 * @param start node
	 * @return list of distances
	 */

	@SuppressWarnings("rawtypes")
	public HashMap<Node, Double> distances(Graph.Node start) {
		HashMap<Graph.Node, Double> distances = new HashMap<Graph.Node, Double>(); 
		HashMap<Graph.Node, Graph.Node> previous = new HashMap<Graph.Node, Graph.Node>(); 

		unvisitedDij = new ArrayList<Node>(); 

		
		//initialize lowest cost
		double lowestCost = Double.POSITIVE_INFINITY; 

		//iterate through nodes and set distance to infinity
		for (Graph.Node node: nodes) {
			distances.put(node, Double.POSITIVE_INFINITY); 
		}
			//set the start value to zero
			distances.put(start, (double)0); 
			
			previous.put(start,  start); 
			
			//deep copy of nodes
			LinkedList<Graph.Node> unvisited = new LinkedList<Graph.Node>(); 
			ListIterator<Graph.Node> listIterator = nodes.listIterator(); 
			
			while (listIterator.hasNext()) {
				unvisited.add(listIterator.next()); 
				
			}
			
			
		//while there are still unvisited nodes
		while (!unvisitedDij.isEmpty()) {
			Graph.Node tempNode = null; 
			double tempCost; 
			
			
			
			for (Graph.Node n: unvisitedDij) {
				tempNode = n;
				tempCost = distances.get(n); 
				
				//comparison to find lowest cost
				if (tempCost <= lowestCost ) {
					lowestCost = tempCost; 
					lowestNode = tempNode; 

				}
			}
			unvisitedDij.remove(lowestNode); 

		}

		//check neighbors to find lowest cost
		LinkedList<Graph.Node> neighbors; 
		while (!unvisitedDij.isEmpty()) {
			neighbors =  lowestNode.getNeighbors();
			
			if (neighbors != null) {
				for (Graph.Node neighbor: neighbors) {
					double costN; 
					
					if (unvisitedDij.contains(neighbor)) {
						costN = distances.get(neighbor); 
						double weight = findCost(neighbor, lowestNode); 
						
						//compare cost
						if (costN > lowestCost + weight ) {
							costN = lowestCost + weight; 
							previous.put(neighbor, lowestNode); 
						}
					}



				}


			}

		}

		return distances; 

	}  

/**
 * hashmap<Graph.Node, double> cost
 * same prev
 * 
 * 
 *iterate 
 * 
 */

	/**
	 * method to print out a representation of the graph
	 */
	
	@SuppressWarnings("rawtypes")
	public void print() {
		for (Graph.Edge e: edges) {
			System.out.println("EDGE:"); 
			System.out.println("          Data:" +e.getData()); 
			System.out.println("          Head:" +e.getHead().getData()); 
			System.out.println("          Tail:" +e.getTail().getData()); 
			}
		for (Graph.Node n: nodes) {
			System.out.println("NODE:");
			System.out.println("          Data:" + n.getData()); 
		}
		
		
	}
	
	/**
	 * Checks for the consistency of the graph by
	 * checking that there are no duplicate edges
	 * and that every edge leads to/from a valid node
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkConsistency() {
		//checks that each edge has a head and a tail
		for (Graph.Edge e: edges) { 
			if (e.getHead() == null || e.getTail() ==null ) {
				System.out.println("inconsistent"); 
				return false; 

			} 
			//checks that each tail has that edge in its list of edges
			if (!e.getTail().nodeEdges.contains(e)) {
				System.out.println("inconsistent"); 
				return false; 

			} 
			//checks that each head has that edge in its list of edges
			if (!e.getHead().nodeEdges.contains(e)) {
				System.out.println("inconsistent");
				return false; 
			}
		}
		//check that every edge of a node is contained in the masterlist of edges
		for (Graph.Node n: nodes) {
			if (!edges.contains(n.nodeEdges)) {
				System.out.println("inconsistent"); 
				return false; 
			}
		}
		System.out.println("consistent"); 
		return true; 
	}

	
	
	/**
	 * Implements a node in a graph
	 * @author Ashley Loe
	 *
	 * @param <E> edges
	 */
	public class Edge<E> extends java.lang.Object{
		
		private E data;
		@SuppressWarnings("rawtypes")
		private Graph.Node head;
		@SuppressWarnings("rawtypes")
		private Graph.Node tail;
		
		
		//constructor for edge
		@SuppressWarnings("rawtypes")
		public Edge(E data, Graph.Node head, Graph.Node tail) {
			this.data = data; 
			this.head = head; 
			this.tail = tail; 
		
		}
		
		
		/**
		 * accessor for data of the edge
		 * @return data of the edge (key of the edge)
		 */
		public E getData() {
			return data; 
		}
		
		/**
		 * accessor for head node an edge
		 * @return head node of the edge
		 */
		@SuppressWarnings("rawtypes")
		public Graph.Node getHead() {
			return head; 
		}
		
		/**
		 * accessor for tail node of an edge
		 * @return tail node of the edge
		 */
		@SuppressWarnings("rawtypes")
		public Graph.Node getTail() {
			return tail; 
		}
		
		/**
		 * finds the cost(weight) for each edge 
		 * (distance from head to node)
		 * @param edge to find the weight of
		 * @return cost(weight) of the edge
		 */
		@SuppressWarnings("rawtypes")
		public double findCost(Graph.Edge edge) {
			Graph.Node head = edge.getHead(); 
			Graph.Node tail = edge.getTail(); 
			Point tailPoint = (Point) tail.getData(); 
			Point headPoint = (Point) head.getData(); 
			double cost = headPoint.distance(tailPoint);
			return cost; 

		}
		
		public void setCost(Graph.Edge edge, E cost) {
			this.data = cost; 
		}
			
		/**
		 * Accessor for opposite node
		 * @param node
		 * @return head if the given node is the tail; 
		 * 		   tail if the given node is the head
		 */
		public Graph.Node oppositeTo(Graph.Node node) {
			if (node.equals(head)) {
				return tail; 
			} else {
				return head; 
			}
		
		}
		
		/**
		 * manipulator for data of a node
		 * @param data: the new data (key) for the node
		 */
		public void setData(E data) {
			this.data = data; 
		}
		
		
		/**
		 * Two edges are equal if they connect the
		 *  same endpoints regardless of the data they carry. 
		 *  This definition is used by LinkedList.contains() and 
		 *  LinkedList.remove()
		 *  @return true if the two edges are the same; false if they aren't 
		 */
		@SuppressWarnings("rawtypes")
		public boolean equals(Object o) {
			Edge other = this; 
			if (o instanceof Edge) {
				other = (Edge)o; 
				return (head.equals(other.head) && tail.equals(other.tail) )
						|| (head.equals(tail) && tail.equals(head)) ; 
			} 
			return false; 
		}
		
		

		/**
		 * 
		 * @return hashcode: a number that acts as unique key for each edge
		 */
		public int hashCode() {
			return getHead().hashCode()*getTail().hashCode(); 
		}
		
		
	
	}
	
	
	/**
	 * Node class implements a node in a graph
	 * @author Ashley Loe
	 *
	 * @param <V>
	 */
	public class Node<V> {
		
		@SuppressWarnings("rawtypes")
		protected LinkedList<Graph.Edge> nodeEdges; 
		
		protected V data; 
		
		int x; 
		int y;

		private Point point; 
		
		/* constructor for a new node object */
		public Node(V data) {
			
			nodeEdges = new LinkedList<Graph.Edge>(); 
			
			this.data = data; 
		}
		
		public Node(V data, Point point) {
			nodeEdges = new LinkedList<Graph.Edge>(); 
			this.data = data; 
		}
		
		/**
		 * Accessor for data of a node
		 * @return data of the given node
		 */
		public V getData() {
			return data; 
		}
		
		
		
		/**
		 * Accessor for data of a node
		 * @param data of the given node
		 */
		public void setData(V data) {
			this.data = data; 
		}
		
		
		
		/**
		 * Finds a list of nodes connected to a given node
		 * @return neighbors: a list of nodes connected to the given node
		 */
		public LinkedList<Graph.Node> getNeighbors() {
			LinkedList<Graph.Node>  neighbors = new LinkedList<Graph.Node>(); 
			for (Graph.Edge e: nodeEdges) {
				neighbors.add(e.oppositeTo(this));			
			}
			return neighbors; 
		}

		/**
		 * Checks if there is an edge to the node in question
		 * @param node the node to check for an edge
		 * @return true if there is an edge; false if there isn't
		 */
		public boolean isNeighbor(Graph.Node node) {
			if (getNeighbors().contains(node)) {
				return true; 
			} 
			return false; 

		}

		/**
		 * Checks for and finds the edge connecting a node to a neighbor
		 * @param neighbor: the node to check for a connected edge        
		 * @return edge: the edge to the neighbor if there is one; 
		 * 			null if there isn't
		 */
		public Graph.Edge edgeTo(Graph.Node neighbor) {
			Graph.Edge edge = null; 
			LinkedList<Graph.Edge> neighborEdges = neighbor.nodeEdges; 
			for (Graph.Edge e: neighborEdges) {
				if (nodeEdges.contains(e)) {
					edge = e; 
				}
			}
			return edge; 
		}
		
		/**
		 * adds an edge to the masterlist of all the edges
		 * @param edge: the edge to be added the list of edges
		 */
		protected void addEdgeRef(Graph.Edge edge) {
			nodeEdges.add(edge); 
		}
		
		/**
		 * removes an edge from the masterlist of all the edges
		 * @param edge: the edge to be removed from the list of edges
		 */
		protected void removeEdgeRef(Graph.Edge edge) {
			nodeEdges.remove(edge); 
		}
		
		
		
	}
	
}
