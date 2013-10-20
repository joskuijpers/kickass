package nl.tudelft.ci.kickass.pathing;

import java.util.ArrayList;

import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.Direction;
import nl.tudelft.ci.kickass.world.Route;
import nl.tudelft.ci.kickass.world.World;

public class AntPathing extends PathingAlgorithm {
	
	public final static int MAX_NUM_ITERATIONS = 10;
	public final static int NUM_ANTS_PER_ITERATION = 100;
	public final static double PHEROMONE_DROPPED_BY_ANT = 1;
	public final static double PHEROMONE_EVAPORATION_PARM = 10;
	public final static double CONVERGENCE_CRITERION = 10;
	
	private final World world;
	private Coordinate startCoordinate, endCoordinate;
	
	public AntPathing(World world, Coordinate start, Coordinate end) {
		this.world = world;
		this.startCoordinate = start;
		this.endCoordinate = end;
	}
	
	@Override
	public Route findRoute() {
		Node rootNode, endNode;
		
		// Generate the node tree
		rootNode = world.generateTree();
		endNode = rootNode.findNode(endCoordinate);
		
		// Optimize the maze
		optimizeMaze(rootNode,endNode);
		
		// Walk over the tree to find the shortest route
		Ant kingAnt = new Ant(rootNode,endNode);
		while(!kingAnt.isFinished())
			kingAnt.doStep();
		
		// If at dead end, the maze has no possible route or
		// the math is wrong
		assert !kingAnt.isAtDeadEnd();
		
		Route route = kingAnt.getRoute();
		
		return route;
	}
	
	private void optimizeMaze(Node start, Node end) {
		int iterations = 0;
		
		while((iterations < MAX_NUM_ITERATIONS)) {
			doIteration(start,end);
			iterations++;
		}
	}
	
	private void doIteration(Node start, Node end) {
		ArrayList<Ant> ants, antsQueue;
		
		ants = new ArrayList<Ant>();
		antsQueue = new ArrayList<Ant>();
		
		for(int i = 0; i < NUM_ANTS_PER_ITERATION; ++i)
			ants.add(new Ant(start,end));
		antsQueue.addAll(ants);
		
		while(antsQueue.size() > 0) {
			Ant ant = antsQueue.remove(0);
			
			// All went well
			if(ant.doStep()) {
				antsQueue.add(ant);
				continue;
			}
			
			// Something happened to the ant
			if(ant.isAtDeadEnd) { // It died
				System.out.print("D");
			} else {
				System.out.print("F("+ant.getRoute().getLength()+")");
			}
		}
		
		int f = 0, d = 0;
		for(Ant a : ants) {
			if(a.isFinished)
				f++;
			if(a.isAtDeadEnd)
				d++;
		}
		System.out.println("\nNum Finished: "+f+", num dead: "+d+", num with route: "+(f-d));
	}
	
	final class Ant {
		
		private Node startNode;
		private Node finishNode;
		
		private Node currentNode;
		private Route route;
		
		private boolean isFinished = false;
		private boolean isAtDeadEnd = false;
		
		Ant(Node startNode, Node finishNode) {
			this.startNode = startNode;
			this.finishNode = finishNode;
			this.currentNode = startNode;
			
			route = new Route(startNode.getCoordinate());
			route.addStep(startNode);
		}
		
		Route getRoute() {
			return route;
		}
		
		boolean isFinished() {
			return isFinished;
		}
		
		boolean isAtDeadEnd() {
			return isAtDeadEnd;
		}
		
		void dropPheromones() {
			double pheromones = currentNode.getValue();
			pheromones += 1.0;
			currentNode.setValue(pheromones);
		}
		
		/**
		 * Perform a step
		 * @return true on success, false on finishing/failure
		 */
		boolean doStep() {
			Direction direction;

			direction = chooseDirection();
			if(!direction.isValid()) {
				isFinished = true;
				isAtDeadEnd = true;
				return false;
			}
			
			currentNode = currentNode.getAdjacentNode(direction);
			route.addStep(currentNode);
			
			if(currentNode.equals(finishNode)) {
				isFinished = true;
				return false;
			}
			
			return true;
		}
		
		private Direction chooseDirection() {
			int numberOfDirections = 0;
			Node previousStep = route.getStep(route.getLength()-2); // last is currentNode, so -2 is prev 
			Direction directions[] = new Direction[4];

			for(Direction dir : Direction.values()) {
				Node nextNode = currentNode.getAdjacentNode(dir);
				
				// Can't go here...
				if(!nextNode.isValid())
					continue;

				// ...there now...
				if(nextNode == previousStep)
					continue;
				
				// ...been there.
				if(route.hasStep(nextNode))
					continue;
				
				directions[numberOfDirections++] = dir;
			}
			
			// Nowhere to go
			if(numberOfDirections == 0)
				return Direction.INVALID_DIRECTION;

			// Math to find the actual direction:
			int y = (int)Math.round(Math.random()*(numberOfDirections-1));
			return directions[y];
		}
		
		@Override
		public String toString() {
			return "{ant at "+currentNode+"}";
		}
	}
}
