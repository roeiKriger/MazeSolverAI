import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Astart {
	public ArrayList<Fiver> pq = new ArrayList<Fiver>();
	public Fiver[][] parent; //each cell in the parent matrix contains the parent coordinates of the specific cell we are in
	boolean[][] isPopped;
	
	// initializing all the vars
	public void init (int x, int y) {
		parent = new Fiver[x][y];
		isPopped = new boolean[x][y];
		
		for (int i=0; i<x; i++) {
			for (int j=0; j<y; j++) {
				isPopped[i][j]=false;
			}
		}
	}
	
	//comparator, first will order by the f function, if its equal we will sort by the direction as requested in the instructions
	class fComp implements Comparator<Fiver> {
		public int compare (Fiver t1, Fiver t2) {
			if (t1.f > t2.f) return 1;
			if (t1.f < t2.f) return -1;
			if (t1.direction > t2.direction) return 1;
			if (t1.direction < t2.direction) return -1;
			return 0;
		}
	}
	
	// Function to perform the A* search traversal
	public void aStart (char grid[][], int startRow, int startCol, int totalRow, int totalCol, int goalRow, int goalCol) {
		init(totalRow*2, totalCol*2);
		
		pq.add(new Fiver(startRow, startCol, 0)); //adding the first cell to the Q
		
		while (!pq.isEmpty()) { // the algorithm runs till the Q is empty or we reach the goal location
			Fiver point = pq.get(0);
			int x = point.first;
			int y = point.second;
			int g = point.g;

			pq.remove(0);
			isPopped[x][y] = true; //after popping the location we will mark it as we visited the cell
			
			if (General.getInstance().goalTest(x, y, goalRow, goalCol)) {
				break;
			}
			
			// Go to the adjacent cells
			ArrayList<Cell> nextStates = General.getInstance().successor(x, y);
			
			for (Cell c : nextStates) {
				if (!isPopped[c.getX()][c.getY()]) {   // checking each time if the states we have received from the successor were visited, if not we will add them to the Q
					if (pq.contains(new Fiver(c.getX(), c.getY()))) {  //if it is in the Q we will compare the heuristic and the f length and will the the lower one
						int i = pq.indexOf(new Fiver(c.getX(), c.getY()));
						if (pq.get(i).f > g+1+c.getHeuristic()) {
							pq.get(i).f = g+1+c.getHeuristic();
							pq.get(i).direction = c.getDirection();
							parent[c.getX()][c.getY()] = new Fiver(x, y, 0, 0, c.getDirection());
						}
					}
					else {  // else we will add the new location to the Q
						pq.add(new Fiver(c.getX(), c.getY(), g+1, g+1+c.getHeuristic(), c.getDirection()));
						parent[c.getX()][c.getY()] = new Fiver(x, y, 0, 0, c.getDirection());
					}
				}
			}
			
			Collections.sort(pq, new fComp());  // sorting the arraylist each time
			//System.out.println(pq);
		}
		
		//printing all the needed details
		ArrayList<Fiver> route = route(goalRow, goalCol, startRow, startCol);
		Collections.reverse(route);
		/*System.out.println("Alg Name : A*");
		System.out.println("Input : " + Main.fileName);*/
		System.out.print("Path : ");
		printRoute(route);
		System.out.println("Cost : " + (route.size())); //we count the start state and the goal state
		System.out.println("Visit Count : " + (calcNumOfVisitedNodes(totalRow*2, totalCol*2))); //we count the start state and the goal state
	}
	
	
	//this method gets: goal location and start location and returns: the route that the algorithm found
	public ArrayList<Fiver> route (int goalRow, int goalCol, int startRow, int startCol) {
		ArrayList<Fiver> path = new ArrayList<Fiver>();		
		Fiver point = new Fiver(goalRow, goalCol);
		
		while (point != null) {
			Fiver t = new Fiver();
			t.first = (point.first)/2;
			t.second = (point.second)/2;
			t.direction = point.direction;
			path.add(t);
			point = parent[point.first][point.second];
		}
		
		return path;
	}
	
	// counting the number of nodes visited
	public int calcNumOfVisitedNodes (int totalRow, int totalCol) {
		int counter = 0;
		for (int i=0; i<totalRow; i++) {
			for (int j=0; j<totalCol; j++) {
				if (isPopped[i][j] == true) {
					counter++;
				}
			}
		}
		
		return counter;
	}
	
	public void printRoute (ArrayList<Fiver> route) { 
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
