package nl.tudelft.ci.kickass;

import java.io.IOException;
import java.nio.file.FileSystems;

import nl.tudelft.ci.kickass.pathing.AntPathing;
import nl.tudelft.ci.kickass.world.Route;
import nl.tudelft.ci.kickass.world.World;

public class KickAss {
	
	public static void main(String[] args) {
		
		try {
			World myWorld = new World(FileSystems.getDefault().getPath(""),
//					"GradingMaze3 hard");
					"GradingMaze2 medium");
//					"GradingMaze1 easy");
			
			AntPathing pathing = new AntPathing(myWorld,myWorld.getStartCoordinate(),myWorld.getFinishCoordinate());
			Route result = pathing.findRoute();
			
			if(result != null) {
				result.write(FileSystems.getDefault().getPath(myWorld.getName()+" route.txt"));
			}
			
			System.out.println("Result: "+result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
