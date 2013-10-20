package nl.tudelft.ci.kickass.world;

public class InvalidCoordinate extends Coordinate {
	
	private static InvalidCoordinate instance = new InvalidCoordinate();
	
	public static InvalidCoordinate getInstance() {
		return instance;
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
	public Coordinate getAdjacent(Direction d) {
		return this;
	}
	
	@Override
	public String toString() {
		return "<null>";
	}
}
