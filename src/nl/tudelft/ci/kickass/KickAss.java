package nl.tudelft.ci.kickass;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import nl.tudelft.ci.kickass.pathing.Node;
import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.World;

public class KickAss {
	
	public static void main(String[] args) {
		
		try {
			World myWorld = new World(FileSystems.getDefault().getPath(""),
					"GradingMaze1 easy");
			//myWorld.printMaze();
			
			//System.out.println("start "+myWorld.getStartCoordinate()+" finish "+myWorld.getFinishCoordinate());
			
			ArrayList<Node> todo = new ArrayList<Node>();
			Node current, root = myWorld.generateTree();
			
			todo.add(root);
			while(todo.size() != 0) {
				current = todo.remove(0);
				
				System.out.println("Visit node "+current);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
