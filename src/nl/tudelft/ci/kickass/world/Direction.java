package nl.tudelft.ci.kickass.world;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Direction {
	DIRECTION_NORTH(1),
	DIRECTION_EAST(0),
	DIRECTION_SOUTH(3),
	DIRECTION_WEST(2),
	INVALID_DIRECTION(-1);
	
	private int value;
	private static final Map<Integer, Direction> lookup = new HashMap<Integer, Direction>();
	
	static {
		for(Direction dir : EnumSet.allOf(Direction.class)) {
			lookup.put(dir.getValue(), dir);
		}
	}
	
	private Direction(int value) {
		this.value = value;
	}
	
	public static Direction valueOf(int value) {
		assert value >= 0 && value <= 3;
		return lookup.get(value);
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isValid() {
		return value != -1;
	}
	
	public Direction inverse() {
		return Direction.valueOf((value+2)%4);
	}
};
