package nl.tudelft.ci.kickass.world;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Route {
	
	private int length;
	private Coordinate startCoordinate;
	
	public Route() {
		
	}
	
	public void write(Path path) throws IOException {
		BufferedWriter writer;
		
		writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		
		writer.write(length+";\n");
		writer.write(startCoordinate.getX()+", "+startCoordinate.getY()+";\n");
		
		for(int i = 0; i < length; i++) {
			// TODO
			Direction d = Direction.INVALID_DIRECTION;
			writer.write(d.getWritableValue()+";");
		}
		
		writer.close();
	}
}
