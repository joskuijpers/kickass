package nl.tudelft.ci.kickass.world;

public class Coordinate {
	
	private World world;
	private int x, y;
	
	public Coordinate(World world, int x, int y) {
		if(x >= world.getWidth() || y >= world.getHeight()
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
	
	public Coordinate getNorth() {
		if(y == 0)
			return new InvalidCoordinate(world);
		return new Coordinate(world,x,y-1);
	}
	
	public Coordinate getEast() {
		if(x == world.getWidth()-1)
			return new InvalidCoordinate(world);
		return new Coordinate(world,x+1,y);
	}
	
	public Coordinate getSouth() {
		if(y == world.getHeight()-1)
			return new InvalidCoordinate(world);
		return new Coordinate(world,x+1,y);
	}
	
	public Coordinate getWest() {
		if(x == 0)
			return new InvalidCoordinate(world);
		return new Coordinate(world,x-1,y);
	}
	
	public boolean isValid() {
		return true;
	}
	
	public boolean isAdjacent(Coordinate other) {
		if(!other.world.equals(world) || !isValid() || !other.isValid())
			return false;
		
		int xdiff = Math.abs(other.x - x);
		int ydiff = Math.abs(other.y - y);
	
		if((xdiff == 0 && ydiff == 1) 
				|| (xdiff == 1 && ydiff == 0))
			return true;
		
		return false;
	}
	
	public Direction directionToAdjacent(Coordinate other) {
		if(!other.world.equals(world) || !isValid() || !other.isValid())
			return Direction.INVALID_DIRECTION;
		
		int xdiff = other.x - x;
		int ydiff = other.y - y;
	
		if(xdiff == 0 && ydiff == 1)
			return Direction.DIRECTION_SOUTH;
		else if(xdiff == 0 && ydiff == -1)
			return Direction.DIRECTION_NORTH;
		else if(xdiff == 1 && ydiff == 0)
			return Direction.DIRECTION_EAST;
		else if(xdiff == -1 && ydiff == 0)
			return Direction.DIRECTION_WEST;
		
		return Direction.INVALID_DIRECTION;
	}
	
	/**
	 * Returns whether the object is equal to the receiver.
	 * 
	 * note that any invalid coordinate is not equal to any coordinate, valid
	 * or invalid. An invalid coordinate thus acts like NaN when it comes to
	 * comparison.
	 * 
	 * @param other The other object
	 * @return true when always equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!isValid())
			return false;
		
		if(!(other instanceof Coordinate))
			return false;
		Coordinate cother = (Coordinate)other;
		
		if(!cother.isValid())
			return false;
		if(cother.x != x || cother.y != y)
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
		return y*world.getWidth()+x;
	}
	
	@Override
	public String toString() {
		return "<"+x+","+y+";'"+world.getName()+"'>";
	}
}
