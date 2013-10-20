package nl.tudelft.ci.kickass.world;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import nl.tudelft.ci.kickass.pathing.Node;

public class World {
	
	private String name;
	
	private Path mazePath;
	private Path coordPath;
	
	private int width, height;
	
	private Coordinate startCoordinate;
	private Coordinate finishCoordinate;
	
	private boolean maze[][];
	
	public World(Path root, String name) throws IOException {
		this.name = name;
		mazePath = root.resolve(name);
		coordPath = root.resolve(name + " coordinates.txt");

		readMaze();
		readGoalCoords();
	}
	
	private void readMaze() throws IOException {
		Scanner scanner = new Scanner(mazePath);
		
		width = scanner.nextInt();
		height = scanner.nextInt();
		
		// Skip over line delimiter
		scanner.nextLine();
		
		maze = new boolean[height][width];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				maze[y][x] = (scanner.nextInt() == 1)?true:false;
				scanner.skip(" ");
			}
			scanner.nextLine();
		}
		
		scanner.close();
	}
	
	private void readGoalCoords() throws IOException {
		Scanner scanner = new Scanner(coordPath);
		int x, y;
		String tmp;
		
		tmp = scanner.next("([0-9]+),");
		x = Integer.valueOf(tmp.substring(0, tmp.length()-1));
		tmp = scanner.next("([0-9]+);");
		y = Integer.valueOf(tmp.substring(0, tmp.length()-1));
		startCoordinate = new Coordinate(this,x,y);
		
		scanner.nextLine();
		
		tmp = scanner.next("([0-9]+),");
		x = Integer.valueOf(tmp.substring(0, tmp.length()-1));
		tmp = scanner.next("([0-9]+);");
		y = Integer.valueOf(tmp.substring(0, tmp.length()-1));
		finishCoordinate = new Coordinate(this,x,y);
				
		scanner.close();
	}
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Coordinate getStartCoordinate() {
		return startCoordinate;
	}
	
	public Coordinate getFinishCoordinate() {
		return finishCoordinate;
	}
	
	public boolean isObstacleAtCoordinate(Coordinate c) {
		assert c.getX() <= width && c.getY() <= height;
		
		return !maze[c.getY()][c.getX()];
	}
	
	public Node generateTree() {
		ArrayList<Node> queue;
		HashMap<Coordinate,Node> nodes;
		Node root, current;
		
		root = new Node(startCoordinate);
		
		// Handle queue to solve recursion
		queue = new ArrayList<Node>();
		queue.add(root);
		
		// Node list to make a complex tree
		nodes = new HashMap<Coordinate,Node>();
		nodes.put(startCoordinate,root);
		
		while(queue.size() != 0) {
			current = queue.remove(0);

			for(Direction dir : Direction.values()) {
				Coordinate nextCoord;
				Node nextNode;
				boolean isNew = false;
				
				nextCoord = current.getCoordinate().getAdjacent(dir);
				if(!nextCoord.isValid() || nextCoord.isObstacle())
					continue;
				
				// Skip the parent node quickly
				if(nextCoord.equals(current.getCoordinate()))
					continue;
				
				// If node already exists, connect to it instead
				nextNode = nodes.get(nextCoord);
				if(nextNode == null) {
					nextNode = new Node(nextCoord);
					isNew = true;
				}

				// Add 'parent to child' directional relation
				current.setAdjacentNode(dir, nextNode);
				
				// Add 'child to parent' directional relation
				nextNode.setAdjacentNode(dir.inverse(), current);
				
				if(isNew) {
					queue.add(nextNode);
					nodes.put(nextCoord, nextNode);
				}
			}
		}

		return root;
	}
	
	public void printMaze() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				System.out.print(maze[y][x]?" ":"@");
			}
			System.out.println();
		}
	}
}
