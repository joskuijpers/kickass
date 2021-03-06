package nl.tudelft.ci.kickass.world;

public class Coordinate {
	
	private World	world;
	private int		x, y;
	
	protected Coordinate() {
		
	}
	
	public Coordinate(World world, int x, int y) {
		if (x >= world.getWidth() || y >= world.getHeight()
				|| x < 0 || y < 0)
			throw new IllegalArgumentException();
		
		this.world = world;
		this.x = x;
		this.y = y;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isObstacle() {
		return world.isObstacleAtCoordinate(this);
	}
	
	public Coordinate getAdjacent(Direction direction) {
		switch (direction) {
			case DIRECTION_NORTH:
				if (y == 0)
					return InvalidCoordinate.getInstance();
				return new Coordinate(world, x, y - 1);
			case DIRECTION_EAST:
				if (x == world.getWidth() - 1)
					return InvalidCoordinate.getInstance();
				return new Coordinate(world, x + 1, y);
			case DIRECTION_SOUTH:
				if (y == world.getHeight() - 1)
					return InvalidCoordinate.getInstance();
				return new Coordinate(world, x, y + 1);
			case DIRECTION_WEST:
				if (x == 0)
					return InvalidCoordinate.getInstance();
				return new Coordinate(world, x - 1, y);
			default:
				return InvalidCoordinate.getInstance();
		}
	}
	
	public boolean isValid() {
		return true;
	}
	
	public boolean isAdjacent(Coordinate other) {
		if (!other.world.equals(world) || !isValid() || !other.isValid())
			return false;
		
		int xdiff = Math.abs(other.x - x);
		int ydiff = Math.abs(other.y - y);
		
		if ((xdiff == 0 && ydiff == 1)
				|| (xdiff == 1 && ydiff == 0))
			return true;
		
		return false;
	}
	
	public Direction directionToAdjacent(Coordinate other) {
		if (!other.world.equals(world) || !isValid() || !other.isValid())
			return Direction.INVALID_DIRECTION;
		
		int xdiff = other.x - x;
		int ydiff = other.y - y;
		
		if (xdiff == 0 && ydiff == 1) return Direction.DIRECTION_SOUTH;
		else if (xdiff == 0 && ydiff == -1) return Direction.DIRECTION_NORTH;
		else if (xdiff == 1 && ydiff == 0) return Direction.DIRECTION_EAST;
		else if (xdiff == -1 && ydiff == 0)
			return Direction.DIRECTION_WEST;
		
		return Direction.INVALID_DIRECTION;
	}
	
	public Direction nearestDirectionToFar(Coordinate other) {
		return nearestDirectionToFar(other, Direction.values());
	}
	
	public Direction nearestDirectionToFar(Coordinate other, Direction[] allowed) {
		double verticalDelta, horizontalDelta;
		Direction[] favoritedDirections = new Direction[4];
		
		System.out.println("DIR FOR "+this);
		
		// Find vertical and horizontal distances
		verticalDelta = other.y - this.y; // - is up, + is down
		horizontalDelta = other.x - this.x; // - is left, + is right
		
		// Order the directions using distance
		if(Math.abs(verticalDelta) > Math.abs(horizontalDelta)) {
			favoritedDirections[0] = (verticalDelta < 0)?Direction.DIRECTION_NORTH:Direction.DIRECTION_SOUTH;
			favoritedDirections[1] = favoritedDirections[0].inverse();
			favoritedDirections[2] = (horizontalDelta < 0)?Direction.DIRECTION_WEST:Direction.DIRECTION_EAST;
			favoritedDirections[3] = favoritedDirections[2].inverse();
			
//			favoritedDirections[0] = (verticalDelta < 0)?Direction.DIRECTION_NORTH:Direction.DIRECTION_SOUTH;
//			favoritedDirections[1] = (horizontalDelta < 0)?Direction.DIRECTION_WEST:Direction.DIRECTION_EAST;
//			favoritedDirections[2] = favoritedDirections[0].inverse();
//			favoritedDirections[3] = favoritedDirections[1].inverse();
		} else {
			favoritedDirections[0] = (horizontalDelta < 0)?Direction.DIRECTION_WEST:Direction.DIRECTION_EAST;
			favoritedDirections[1] = favoritedDirections[0].inverse();
			favoritedDirections[2] = (verticalDelta < 0)?Direction.DIRECTION_NORTH:Direction.DIRECTION_SOUTH;
			favoritedDirections[3] = favoritedDirections[2].inverse();
			
//			favoritedDirections[0] = (horizontalDelta < 0)?Direction.DIRECTION_WEST:Direction.DIRECTION_EAST;
//			favoritedDirections[1] = (verticalDelta < 0)?Direction.DIRECTION_NORTH:Direction.DIRECTION_SOUTH;
//			favoritedDirections[2] = favoritedDirections[1].inverse();
//			favoritedDirections[3] = favoritedDirections[0].inverse();

		}
		
		

		// Only use directions that are allowed
		for (int i = 0; i < 3; i++) {
			Direction d = favoritedDirections[i];
			if (directionInList(d, allowed))
				return d;
		}

		// The least favorite but only allowed direction
		// This should actually never trigger, because with only 1 direction,
		// this method should not be called
		assert false;
		return favoritedDirections[3];
	}
	
	private boolean directionInList(Direction direction, Direction[] allowed) {
		for (int j = 0; j < 4; j++) {
			if (allowed[j].equals(direction))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns whether the object is equal to the receiver.
	 * 
	 * note that any invalid coordinate is not equal to any coordinate, valid
	 * or invalid. An invalid coordinate thus acts like NaN when it comes to
	 * comparison.
	 * 
	 * @param other
	 *            The other object
	 * @return true when always equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (!isValid())
			return false;
		
		if (!(other instanceof Coordinate))
			return false;
		Coordinate cother = (Coordinate) other;
		
		if (!cother.isValid())
			return false;
		if (cother.x != x || cother.y != y)
			return false;
		
		return true;
	}
	
	/**
	 * Gets the hashcode for this coordinate. The hashcode is unique only
	 * per world.
	 * 
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		return y * world.getWidth() + x;
	}
	
	@Override
	public String toString() {
		return "<" + x + "," + y + ";'" + world.getName() + "'>";
	}
}
