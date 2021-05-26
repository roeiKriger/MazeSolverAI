import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

// based on algorithm from https://www.geeksforgeeks.org/breadth-first-traversal-bfs-on-a-2d-array/
// This code is contributed by 29AjayKumar

public class Bfs {
	public Triple[][] parent; // the parent Matrix will be used in order to keep track of each route and from which point it has started
	boolean[][] isAddedToQ;  // the isAddedtoQ Matrix will check each time if the specific state has been visited already, if so it will not be visited again, as requested
	// in the instructions, to avoid loops
	int counter=0;
	
	// this method will initialize all the matrixes 
	public void init (int x, int y) {  
		parent = new Triple[x][y];
		isAddedToQ = new boolean[x][y];
		
		for (int i=0; i<x; i++) {
			for (int j=0; j<y; j++) {
				isAddedToQ[i][j]=false;
			}
		}
	}
	
	// inner class which we will use the first = X, second = Y, level = the level of the state in comparison to the starting point, as there is in BFS
	public class Triple {
		int first, second, level, direction;
		
		public Triple() {
		}

		public Triple(int first, int second) {
			this.first = first;
			this.second = second;
		}

		public Triple(int first, int second, int level) {
			this.first = first;
			this.second = second;
			this.level = level;
		}
		
		public Triple(int first, int second, int level, int direction) {
			this.first = first;
			this.second = second;
			this.level = level;
			this.direction =  direction;
		}

		@Override
		public String toString() {
			String s = "";
			s += "(" + first + ")(" + second +")";
			return s;
		}
	}

	// Function to perform the BFS traversal
	public void BFS(char grid[][], int startRow, int startCol, int totalRow, int totalCol, int goalRow, int goalCol) {
		
		init(totalRow*2, totalCol*2);
		int level = 0;  // the first cell of the maze is in level 0
		
		// Stores indices of the matrix cells
		Queue<Triple> q = new LinkedList<>();

		// Mark the starting cell as visited
		// and push it into the queue
		q.add(new Triple(startRow, startCol, level));
		isAddedToQ[startRow][startCol] = true;

		// Iterate while the queue
		// is not empty
		while (!q.isEmpty()) {
			Triple point = q.peek();
			int x = point.first;
			int y = point.second;

			q.remove();
			counter++;  // we are using a counter to check how many cells were visited in the algorithm
			
			// checking goal test
			if (General.getInstance().goalTest(x, y, goalRow, goalCol)) {
				break;
			}
			
			// adjusting each time the level
			if(point.level == level) {
				level++;
			}
			
			// Go to the adjacent cells
			ArrayList<Cell> nextStates = General.getInstance().successor(x, y);
			
			for (Cell c : nextStates) {   // checking each time if the states we have received from the successor were visited, if not we will add them to the Q
				if (!isAddedToQ[c.getX()][c.getY()]) {
					q.add(new Triple(c.getX(), c.getY(), level));
					isAddedToQ[c.getX()][c.getY()] = true;
					parent[c.getX()][c.getY()] = new Triple(x, y, 0, c.getDirection());  // filling the parent matrix with the location we have arrived from
				}
			}
		}
		
		// printing results
		ArrayList<Triple> route = route(goalRow, goalCol, startRow, startCol);
		Collections.reverse(route);
		/*System.out.println("Alg Name : BFS");
		System.out.println("Input : " + Main.fileName);*/
		System.out.print("Path : ");
		printRoute(route);
		System.out.println("Cost : " + (route.size())); //we count the start state and the goal state
		System.out.println("Visit Cost: " + counter); //we count the start state and the goal state
	}
	
	//this method gets: goal location and start location and returns: the route that the algorithm found
	public ArrayList<Triple> route (int goalRow, int goalCol, int startRow, int startCol) {
		ArrayList<Triple> path = new ArrayList<Triple>();	
		Triple point = new Triple(goalRow, goalCol);
		
		while (point != null) {
			Triple t = new Triple();
			t.first = (point.first)/2;  // the real size of the matrix is bigger than the amount of cells which there are in the maze, so we divide by 2
			t.second = (point.second)/2;
			t.direction = point.direction;
			path.add(t);
			point = parent[point.first][point.second];
		}
		
		return path;
	}
	
	public void printRoute (ArrayList<Triple> route) { 
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