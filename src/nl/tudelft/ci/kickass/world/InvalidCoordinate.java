package nl.tudelft.ci.kickass.world;

public class InvalidCoordinate extends Coordinate {
	
	InvalidCoordinate(World world) {
		super(world,0,0);
	}
	
	@Override
	public boolean isValid() {
		return false;
	}
	
	@Override
	public int getX() {
		throw new IllegalArgumentException();
	}
	
	@Override
	public int getY() {
		throw new IllegalArgumentException();
	}
	
	@Override
	public Coordinate getNorth() {
		throw new IllegalArgumentException();
	}
	
	@Override
	public Coordinate getEast() {
		throw new IllegalArgumentException();
	}
	
	@Override
	public Coordinate getSouth() {
		throw new IllegalArgumentException();
	}
	
	@Override
	public Coordinate getWest() {
		throw new IllegalArgumentException();
	}
}
