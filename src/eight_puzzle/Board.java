package eight_puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board {

	private final int[][] blocks;
	private final int N;
	
	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		
		N = blocks.length;
		this.blocks = new int[N][N];
		
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				this.blocks[i][j] = blocks[i][j];
		}
	
	// board dimension n
	public int dimension() {
		return N;
	}                 

	// number of blocks out of place
	public int hamming() {
		
		int hammingDis = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (isSpace(blocks[i][j])) 				continue;
				if (blocks[i][j] != i * N + j + 1) 	hammingDis++;
			}
		}
		
		return hammingDis;
	}                   

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		
		int manhattanDis = 0;
		int[] location = new int[2];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (isSpace(blocks[i][j]))
					continue;
				if (blocks[i][j] != i * N + j + 1) {
					location = findGoalLocation(blocks[i][j]);
					manhattanDis += Math.abs(i - location[0]) + Math.abs(j - location[1]);
				}
			}
		}
		
		return manhattanDis;
	}
	
	private int[] findGoalLocation(int data) {
		
		int[] ij = new int[2];
		
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++) 
				if (data == N * i + j + 1) 
				{ ij[0] = i; ij[1] = j; }
		
		return ij;
	}

	// is this board the goal board?
	public boolean isGoal() {
		
		for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) 				continue;
                if (blocks[i][j] != i * N + j + 1) 	return false;
            }
        }
		
		return true;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length - 1; col++)
                if (!isSpace(blocks[row][col]) && !isSpace(blocks[row][col + 1]))
                    return new Board(swap(row, col, row, col + 1));
        throw new RuntimeException();
	}                    

	// does this board equal y?
	public boolean equals(Object y) {
		
		if (y == null) return false;
		if (y.getClass() != this.getClass()) return false;
		if (y == this) return true;
		
		Board that = (Board) y;
	    if (this.dimension() != that.dimension()) return false;
	    
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
            }
        }
        
        return true;
	}        

	// all neighboring boards
	public Iterable<Board> neighbors() {
		
		Queue<Board> neighbors = new Queue<Board>();
		int[] location = spaceLocation();
	    int spaceRow = location[0];
	    int spaceCol = location[1];

	    if (spaceRow > 0)               neighbors.enqueue(new Board(swap(spaceRow, spaceCol, spaceRow - 1, spaceCol)));
	    if (spaceRow < dimension() - 1) neighbors.enqueue(new Board(swap(spaceRow, spaceCol, spaceRow + 1, spaceCol)));
	    if (spaceCol > 0)               neighbors.enqueue(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol - 1)));
	    if (spaceCol < dimension() - 1) neighbors.enqueue(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol + 1)));

		return neighbors;
	}
	
	private int[][] swap(int row1, int col1, int row2, int col2) {
		int[][] copy = new int[N][N];
		
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				copy[i][j] = blocks[i][j];
		
	    int tmp = copy[row1][col1];
	    copy[row1][col1] = copy[row2][col2];
	    copy[row2][col2] = tmp;

	    return copy;
	}

	private int[] spaceLocation() {
	    for (int row = 0; row < blocks.length; row++)
	        for (int col = 0; col < blocks.length; col++)
	            if (isSpace(blocks[row][col])) {
	                int[] location = new int[2];
	                location[0] = row;
	                location[1] = col;

	                return location;
	            }
	    throw new RuntimeException();
	}
	
	private boolean isSpace(int block) {
        return block == 0;
    }


	// string representation of this board (in the output format specified below)
	public String toString() {
		StringBuilder s = new StringBuilder();
	    s.append(N + "\n");
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            s.append(String.format("%2d ", blocks[i][j]));
	        }
	        s.append("\n");
	    }
	    return s.toString();
	}
		
	// unit tests (not graded)
	public static void main(String[] args) {
		
	}
}
