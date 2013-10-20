package nl.tudelft.ci.kickass.pathing;

import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.Direction;
import nl.tudelft.ci.kickass.world.World;

public class Node {
	
	private Coordinate coordinate;
	private double value;
	
	private Node adjacentNodes[];
	
	protected Node() {
	}
	
	public Node(Coordinate coordinate) {
		this.adjacentNodes = new Node[4];
		for(int i = 0; i < 4; i++)
			this.adjacentNodes[i] = NullNode.getInstance();
		
		this.coordinate = coordinate;
		this.value = 0.0;
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
	
	public Node getAdjacentNode(Direction direction) {
		if(!direction.isValid())
			return NullNode.getInstance();
		return adjacentNodes[direction.getValue()];
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
	
	public boolean isValid() {
		return true;
	}
	
	@Override
	public String toString() {
		return "(node at "+coordinate+", number of directions: "+numberOfAdjacentNodes()+", value: "+value+")";
	}
}
