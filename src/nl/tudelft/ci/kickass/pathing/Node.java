package nl.tudelft.ci.kickass.pathing;

import java.util.ArrayList;

import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.Direction;
import nl.tudelft.ci.kickass.world.InvalidCoordinate;
import nl.tudelft.ci.kickass.world.World;

public class Node {
	
	private final Coordinate coordinate;
	private double value;
	
	private Node adjacentNodes[];
	
	public enum NodeType {
		PATH,
		FIELD,
		UNKNOWN
	}
	
	private NodeType type = NodeType.UNKNOWN;
	
	protected Node() {
		coordinate = InvalidCoordinate.getInstance();
	}
	
	public Node(Coordinate coordinate) {
		this.adjacentNodes = new Node[4];
		for(int i = 0; i < 4; i++)
			this.adjacentNodes[i] = NullNode.getInstance();
		
		this.coordinate = coordinate;
		this.value = 1.0; // TODO this is Ant specific
	}
	
	public World getWorld() {
		return coordinate.getWorld();
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public NodeType getType() {
		return type;
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}
	
	public Node getAdjacentNode(Direction direction) {
		if(!direction.isValid())
			return NullNode.getInstance();
		return adjacentNodes[direction.getValue()];
	}
	
	public Node[] getAdjacentNodes() {
		ArrayList<Node> res = new ArrayList<Node>();
		
		for(Node node : adjacentNodes) {
			if(node.isValid())
				res.add(node);
		}
		
		Node[] retType = {};
		return res.toArray(retType);
	}
	
	public void setAdjacentNode(Direction direction, Node node) {
		if(direction.isValid())
			adjacentNodes[direction.getValue()] = node;
	}
	
	public boolean hasAdjacentNode(Direction direction) {
		if(!direction.isValid())
			return false;
		return adjacentNodes[direction.getValue()].isValid();
	}
	
	public boolean hasAdjacentNode() {
		return numberOfAdjacentNodes() > 0;
	}
	
	public int numberOfAdjacentNodes() {
		int num = 0;
		
		if(adjacentNodes[Direction.DIRECTION_NORTH.getValue()].isValid())
			num++;
		if(adjacentNodes[Direction.DIRECTION_EAST.getValue()].isValid())
			num++;
		if(adjacentNodes[Direction.DIRECTION_SOUTH.getValue()].isValid())
			num++;
		if(adjacentNodes[Direction.DIRECTION_WEST.getValue()].isValid())
			num++;
		
		return num;
	}
	
	public final Node findNode(Coordinate coordinate) {
		if(this.coordinate.equals(coordinate))
			return this;
		
		ArrayList<Node> queue, done;

		queue = new ArrayList<Node>();
		done = new ArrayList<Node>();
		
		queue.add(this);
		while(queue.size() > 0) {
			Node current = queue.remove(0);
			
			for(Node n : current.adjacentNodes) {
				if(!n.isValid() || done.contains(n))
					continue;
				
				if(n.coordinate.equals(coordinate))
					return n;

				queue.add(n);
			}
			
			done.add(current);
		}
		
		return null;
	}
	
	public void remove() {
		System.out.println("Remove node at "+coordinate);
		for(Direction dir : Direction.values()) {
			if(!dir.isValid())
				continue;
			
			Node adjacent = adjacentNodes[dir.getValue()];
			if(!adjacent.isValid())
				continue;
			
			adjacent.adjacentNodes[dir.inverse().getValue()] = NullNode.getInstance();
			adjacentNodes[dir.getValue()] = NullNode.getInstance();
		}
	}
	
	public boolean isCenterOfIntersection() {
		if(numberOfAdjacentNodes() != 4)
			return false;
	
		for(Node adjacent : adjacentNodes) {
			Direction dirToAdjacent = this.getCoordinate().directionToAdjacent(adjacent.getCoordinate()); 
			
			for(Direction dir : Direction.values()) {
				if(!dir.isValid())
					continue;
				
				if(dir == dirToAdjacent.inverse()
						|| dir == dirToAdjacent)
					continue;
				
				if(adjacent.hasAdjacentNode(dir))
					return false;
			}
		}
		
		return true;
	}
	
	public ArrayList<Node> makeFlat() {
		ArrayList<Node> queue, list;
	
		list = new ArrayList<Node>();
		list.add(this);
		queue = new ArrayList<Node>();
		queue.add(this);
		
		while(queue.size() > 0) {
			Node n = queue.remove(0);
			
			for(Node next : n.adjacentNodes) {
				if(!next.isValid())
					continue;
				
				if(list.contains(next))
					continue;
				
				queue.add(next);
				list.add(next);	
			}
		}
		
		return list;
	}
	
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Node))
			return false;
		Node otherNode = (Node)other;
		
		if(!otherNode.coordinate.equals(coordinate))
			return false;
		
		if(otherNode.value != value)
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "(node at "+coordinate+", number of directions: "+numberOfAdjacentNodes()+", value: "+value+")";
	}
}
