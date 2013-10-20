package nl.tudelft.ci.kickass.pathing;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

import nl.tudelft.ci.kickass.world.Coordinate;
import nl.tudelft.ci.kickass.world.Direction;
import nl.tudelft.ci.kickass.world.Route;
import nl.tudelft.ci.kickass.world.World;

public class AntPathing extends PathingAlgorithm {
	
	public final static int MAX_NUM_ITERATIONS = 10; // number of optimization iterations
	public final static int NUM_ANTS_PER_ITERATION = 25; // number of ants used per iteration
	public final static double PHEROMONE_DROPPED_BY_ANT = 10.0;
	public final static double PHEROMONE_EVAPORATION_PARAM = 0.1; // val = val*(1-PARAM)
	public final static double CONVERGENCE_CRITERION = 10;
	public final static int NUM_THREADS = 1;
	
	private final World world;
	private final Coordinate startCoordinate, endCoordinate;
	
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
		ArrayList<Node> nodes;
		
		// A flat list for very fast traversal
		nodes = start.makeFlat();

		while((iterations < MAX_NUM_ITERATIONS)) {
			doIteration(start,end);
			
			// Decrease all pheromones
			for(Node node : nodes)
				node.setValue(node.getValue()*(1.0-PHEROMONE_EVAPORATION_PARAM));
			
			iterations++;
		}
	}
	
	private void doIteration(Node start, Node end) {
		ArrayList<Ant> ants;
		ConcurrentLinkedQueue<Ant> queue;
		ThreadGroup antGrouping;
		AntExecutor executor;
		
		ants = new ArrayList<Ant>();
		queue = new ConcurrentLinkedQueue<Ant>();
		
		for(int i = 0; i < NUM_ANTS_PER_ITERATION; ++i)
			ants.add(new Ant(start,end));
		queue.addAll(ants);
		
		// Create threads for ant running
		antGrouping = new ThreadGroup("AntRunners");
		executor = new AntExecutor(antGrouping);
		for(int i = 0; i < NUM_THREADS-1; i++)
			executor.execute(new AntRunner(this,queue));
		
		// This thread must also do some math.
		AntRunner blockingRunner = new AntRunner(this,queue);
		blockingRunner.run();
		
		int f = 0, d = 0;
		for(Ant a : ants) {
			if(a.isFinished)
				f++;
			if(a.isAtDeadEnd)
				d++;
		}
		System.out.println("\nNum Finished: "+f+", num dead: "+d+", num with route: "+(f-d));
	}
	
	final class AntExecutor implements Executor {
		private final ThreadGroup group;
		
		AntExecutor(ThreadGroup group) {
			this.group = group;
		}
		
		public void execute(Runnable r) {
			new Thread(group,r).start();
		}
		
		void terminate() {
			System.out.println("Terminate");
		}
	}
	
	final class AntRunner extends Thread {
		
		private final AntPathing pathing;
		private final ConcurrentLinkedQueue<Ant> queue;
		private volatile boolean running = true;
		
		AntRunner(AntPathing pathing, ConcurrentLinkedQueue<Ant> queue) {
			this.pathing = pathing;
			this.queue = queue;
		}
		
		public void terminate() {
			running = false;
		}
		
		public void run() {
			Ant ant;
			while((ant = queue.poll()) != null) {
				// Change the above with correct termination
				// because now, when 1 thread takes the single ant, queue is empty and
				// all threads exit
				
				// All went well
				if(ant.doStep()) {
					queue.add(ant);
					continue;
				}
				
				// Something happened to the ant
				if(ant.isAtDeadEnd) { // It died
				} else {
					ant.dropPheromones(1.0);
				}
			}
		}
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
		
		synchronized void dropPheromones(double multiplier) {
			double added = PHEROMONE_DROPPED_BY_ANT / route.getLength(); 
			
			for(int i = 0; i < route.getLength(); i++) {
				Node node = route.getStep(i);
				node.setValue(node.getValue()*multiplier + added);
			}
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
			double totalPheromone = 0.0;

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
				totalPheromone += nextNode.getValue();
			}
			
			// Nowhere to go
			if(numberOfDirections == 0)
				return Direction.INVALID_DIRECTION;

			// Math to find the actual direction:
			double chances[] = new double[numberOfDirections];
			
			for(int i = 0; i < numberOfDirections; i++) {
				Node adjacent = currentNode.getAdjacentNode(directions[i]);
				chances[i] = adjacent.getValue() / totalPheromone;
			}
			
			double random = Math.random();
			double sliceStart = 0.0;
			for(int i = 0; i < numberOfDirections; i++) {
				if(sliceStart <= random && random <= sliceStart + chances[i])
					return directions[i];
				
				sliceStart += chances[i];
			}

			return null;
		}
		
		@Override
		public String toString() {
			return "{ant at "+currentNode+"}";
		}
	}
}
