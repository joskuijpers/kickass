package nl.tudelft.ci.kickass.world;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.ci.kickass.pathing.Node;
import nl.tudelft.ci.kickass.pathing.NullNode;

public class Route {
	
	private Coordinate startCoordinate;
	private List<Node> steps;
	
	public Route(Coordinate startCoordinate) {
		this.startCoordinate = startCoordinate;
		steps = new ArrayList<Node>();
	}
	
	public int getLength() {
		return steps.size();
	}
	
	public void addStep(Node node) {
		steps.add(node);
	}
	
	public Node getStep(int index) {
		if(index < 0 || index >= steps.size())
			return NullNode.getInstance();
		return steps.get(index);
	}
	
	public boolean hasStep(Node node) {
		if(!node.isValid())
			return false;
		return steps.contains(node);
	}
	
	public void write(Path path) throws IOException {
		BufferedWriter writer;
		
		writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		
		writer.write(steps.size()+";\n");
		writer.write(startCoordinate.getX()+", "+startCoordinate.getY()+";\n");
		
		Coordinate previous = startCoordinate;
		for(Node node : steps) {
			Coordinate coord = node.getCoordinate();
			Direction d = previous.directionToAdjacent(coord);
			
			if(!d.isValid()) {
				System.err.println("Found invalid coord in route!");
			} else
				writer.write(d.getValue()+";");
			
			previous = coord;
		}
		
		writer.close();
	}
	
	@Override
	public String toString() {
		String route = "[route ("+steps.size()+") ";
		
		Coordinate prevCoord = startCoordinate;
		for(Node step : steps) {
			Coordinate nextCoord = step.getCoordinate();
			
			route += prevCoord.directionToAdjacent(nextCoord)+"; ";
			
			prevCoord = nextCoord;;
		}
		
		return route+"]";
	}
}
