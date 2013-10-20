package nl.tudelft.ci.kickass.world;

public enum Direction {
	DIRECTION_NORTH(1),
	DIRECTION_EAST(0),
	DIRECTION_SOUTH(3),
	DIRECTION_WEST(2),
	INVALID_DIRECTION(-1);
	
	private int value;
	
	private Direction(int value) {
		this.value = value;
	}
	
	public int getWritableValue() {
		return value;
	}
	
	public boolean isValid() {
		return value != -1;
	}
};
