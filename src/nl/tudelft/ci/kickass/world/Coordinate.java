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
	
	@Override
	public String toString() {
		return "<"+x+","+y+";'"+world.getName()+"'>";
	}
}
