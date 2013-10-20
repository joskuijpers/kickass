package nl.tudelft.ci.kickass.pathing;

import nl.tudelft.ci.kickass.world.Direction;

public class NullNode extends Node {
	
	private static NullNode instance = new NullNode();
	
	public static NullNode getInstance() {
		return instance;
	}
	
	@Override
	public void setAdjacentNode(Direction direction, Node node) {
	}
	
	@Override
	public boolean hasAdjacentNode(Direction direction) {
		return false;
	}
	
	@Override
	public boolean hasAdjacentNode() {
		return false;
	}
	
	@Override
	public int numberOfAdjacentNodes() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "(null)";
	}
	
	@Override
	public boolean isValid() {
		return false;
	}
}
