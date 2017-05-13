package eight_puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

	private MinPQ<SearchNode> pq;
	private Stack<Board> solutions;
	private int Moves = -1;
	private boolean solved;
	
    private class SearchNode implements Comparable<SearchNode>{  
        private final Board board;  
        private final int move;  
        private final int priority;  
        private final SearchNode parent;  
        private final boolean isTwin;  
          
        public SearchNode(Board board, int move, SearchNode parent, boolean isTwin){  
            this.board = board;  
            this.move = move;  
            this.priority = board.manhattan() + move;  
            this.parent = parent;  
            this.isTwin = isTwin;
        }  
  
        public int compareTo(SearchNode that) {  
            if (this.board.equals(that.board)) return 0;  
            if (this.priority < that.priority) return -1;  
            else return 1;  
        }  
    }
	
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if (initial == null)
			throw new java.lang.NullPointerException();
		
		Board initialTwin = initial.twin();
		pq = new MinPQ<SearchNode>();
		solutions = new Stack<Board>();
		SearchNode initialNode = new SearchNode(initial, 0, null, false);
		SearchNode twinNode = new SearchNode(initialTwin, 0, null, true);
		pq.insert(initialNode);
		pq.insert(twinNode);
		
		
		while(true){  
            //solve for original  
            SearchNode searchNode = pq.delMin();  
            if (searchNode.board.isGoal()) {  
                if (!searchNode.isTwin) {
                	this.solved = true;
                    Moves = searchNode.move;  
                    this.solutions.push(searchNode.board);  
                    while(searchNode.parent != null){  
                        searchNode = searchNode.parent;  
                        this.solutions.push(searchNode.board);  
                    }  
                }  
                break;
            }
            else {  
                for (Board neiborBoard: searchNode.board.neighbors()){  
                    SearchNode neiborNode = new SearchNode(neiborBoard, searchNode.move+1, searchNode, searchNode.isTwin);  
                    if (searchNode.parent == null){  
                        pq.insert(neiborNode);  
                    } else if (!searchNode.parent.board.equals(neiborNode.board)){  
                        pq.insert(neiborNode);  
                    }  
                }  
            }  
        }
		
		
//		while (true) {
//			SearchNode currentNode = pq.delMin();
//			if (currentNode.board.isGoal()) {
//				if (!currentNode.isTwin) {
//					solutions.push(currentNode.board);
//					Moves = currentNode.move;
//					while (currentNode.parent != null) {
//						currentNode = currentNode.parent;
//						solutions.push(currentNode.board);
//					}
//				}
//				break;
//			}
//			else {
//				for (Board neighbor : currentNode.board.neighbors()) {
//					if (currentNode.parent == null || !neighbor.equals(currentNode.parent.board))
//						pq.insert(new SearchNode(neighbor, 0, currentNode, true));;
//				}
//			}
//		}
	}
	
//	private reconstructPath()

	// is the initial board solvable?
	public boolean isSolvable() {
		return solved;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
//		return solutions.size();
		return Moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (isSolvable()) 
			return solutions;
		else
			return null;
	}
	
	public static void main(String[] args) {

	    // create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}
