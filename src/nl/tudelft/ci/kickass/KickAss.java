package nl.tudelft.ci.kickass;

import java.io.IOException;
import java.nio.file.FileSystems;

import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.World;

public class KickAss {
	
	public static void main(String[] args) {
		
		try {
			World myWorld = new World(FileSystems.getDefault().getPath(""),
					"GradingMaze1 easy");
			myWorld.printMaze();
			
			System.out.println("start "+myWorld.getStartCoordinate()+" finish "+myWorld.getFinishCoordinate());

			Coordinate c1 = new Coordinate(myWorld, 4, 5);
			Coordinate c2 = new Coordinate(myWorld, 4, 6);
			
			System.out.println(c2.directionToAdjacent(c1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
