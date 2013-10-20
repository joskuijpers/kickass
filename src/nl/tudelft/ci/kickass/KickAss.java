package nl.tudelft.ci.kickass;

import java.io.IOException;
import java.nio.file.FileSystems;

import nl.tudelft.ci.kickass.world.World;

public class KickAss {
	
	public static void main(String[] args) {
		
		try {
			World myWorld = new World(FileSystems.getDefault().getPath(""),
					"GradingMaze1 easy");
			myWorld.printMaze();
			
			System.out.println("start "+myWorld.getStartCoordinate()+" finish "+myWorld.getFinishCoordinate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
