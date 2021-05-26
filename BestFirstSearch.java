import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BestFirstSearch {
	General g = new General(); 
	public PriorityQueue<Quartet> pq = new PriorityQueue<BestFirstSearch.Quartet>(new heuristicComp());  // the q will contain the locations and will be sorted by the heuristic 
	// comparator which we built
	public Quartet[][] parent; // keep track of the route and the cell each state arrived from
	boolean[][] isAddedToQ; //checking if the state was visited already and if so not to add it again
	int counter=0; //counting the amount of cells the algorithm visited 
	
	// initializing all the vars
	public void init (int x, int y) {  
		parent = new Quartet[x][y];
		isAddedToQ = new boolean[x][y];
		
		for (int i=0; i<x; i++) {
			for (int j=0; j<y; j++) {
				isAddedToQ[i][j]=false;
			}
		}
	}
	
	/// inner class which we will use the first = X, second = Y, heuristic = heuristic by Manhattan distance, direction = we arrived from
	public class Quartet {
		int first, second, heuristic, direction; //for direction: up=1, right=2, down=3, left=4

		public Quartet() {
		}
		
		public Quartet(int first, int second) {
			this.first = first;
			this.second = second;
		}

		public Quartet(int first, int second, int heuristic) {
			this.first = first;
			this.second = second;
			this.heuristic = heuristic;
		}
		
		public Quartet(int first, int second, int heuristic, int direction) {
			this.first = first;
			this.second = second;
			this.heuristic = heuristic;
			this.direction = direction;
		}

		@Override
		public String toString() {
			String s = "";
			s += "(" + first + ")(" + second +") ";
			return s; 
		}
	}
	
	//comparator, first will order by the heuristic, if its equal we will sort by the direction as requested in the instructions
	class heuristicComp implements Comparator<Quartet> {
		public int compare (Quartet t1, Quartet t2) {
			if (t1.heuristic > t2.heuristic) return 1;
			if (t1.heuristic < t2.heuristic) return -1;
			if (t1.direction > t2.direction) return 1;
			if (t1.direction < t2.direction) return -1;
			return 0;
		}
	}
	
	
	// Function to perform the best first search traversal
	public void bestFirstSearch (char grid[][], int startRow, int startCol, int totalRow, int totalCol, int goalRow, int goalCol) {
		init(totalRow*2, totalCol*2);
		
		pq.add(new Quartet(startRow, startCol, General.getInstance().calcHeuristic(startRow, startCol))); //calculating the heuristic of the starting location 
		isAddedToQ[startRow][startCol] = true; 
		
		while (!pq.isEmpty()) { // the algorithm runs till the Q is empty or we reach the goal location
			Quartet point = pq.peek();
			int x = point.first;
			int y = point.second;

			pq.remove();
			counter++;
			
			//checking if we reached the goal
			if (g.goalTest(x, y, goalRow, goalCol)) {
				break;
			}
			
			// Go to the adjacent cells
			ArrayList<Cell> nextStates = General.getInstance().successor(x, y);
			for (Cell c : nextStates) { // checking each time if the states we have received from the successor were visited, if not we will add them to the Q
				if (!isAddedToQ[c.getX()][c.getY()]) {
					pq.add(new Quartet(c.getX(), c.getY(), c.getHeuristic(), c.getDirection()));
					isAddedToQ[c.getX()][c.getY()] = true;
					parent[c.getX()][c.getY()] = new Quartet(x, y, 0, c.getDirection());  //filling the parent matrix with the location we have arrived from
				}
			}
		}
		
		//printing all the needed details 
		ArrayList<Quartet> route = route(goalRow, goalCol, startRow, startCol);
		Collections.reverse(route);
		/*System.out.println("Alg Name : Best First Search");
		System.out.println("Input : " + Main.getInstance().);*/
		System.out.print("Path : ");
		printRoute(route);
		System.out.println("Cost : " + (route.size())); //we count the start state and the goal state
		System.out.println("Visit Count: " + counter ); //we count the start state and the goal state
	}
	
	//this method gets: goal location and start location and returns: the route that the algorithm found
	public ArrayList<Quartet> route (int goalRow, int goalCol, int startRow, int startCol) {
		ArrayList<Quartet> path = new ArrayList<Quartet>();
		
		Quartet point = new Quartet(goalRow, goalCol);
		
		while (point != null) {
			Quartet t = new Quartet();
			t.first = (point.first)/2;
			t.second = (point.second)/2;
			t.direction = point.direction;
			path.add(t);
			point = parent[point.first][point.second];
		}
		
		return path;
	}
	
	public void printRoute (ArrayList<Quartet> route) { 
		String dir ="";
		
		for (int i=0; i<route.size(); i++) {
			
			switch (route.get(i).direction) {
			case 1:
				dir = "UP";
				break;
			case 2:
				dir = "RIGHT";
				break;
			case 3:
				dir = "DOWN";
				break;
			case 4:
				dir = "LEFT";
				break;
			default:
				dir="";
				break;
			}

			if (dir != "") {
				System.out.print(route.get(i).toString() + " -> " + dir + " -> ");
			}
			else {
				System.out.println(route.get(i).toString());
			}
		}
	}

}
