import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int N;
	private boolean[][] grid;
	private WeightedQuickUnionUF UF;
	private WeightedQuickUnionUF UF1;
	
	// create n-by-n grid, with all sites blocked     
	public Percolation(int N)
	{
		if (N < 1)
		{
			throw new java.lang.IllegalArgumentException();
		}
		this.N = N;
		grid = new boolean[N+1][N+1];
		UF = new WeightedQuickUnionUF(N*N + 2); // pdf 58
		UF1 = new WeightedQuickUnionUF(N*N + 1);
	}
	
	// open site (row, col) if it is not open already
	public void open(int row, int col)
	{
		if (row < 1 || col < 1 || row > N || col > N)
		{
			throw new java.lang.IndexOutOfBoundsException();
		}
		
		// first row
		if (row == 1)
		{
	        UF.union(0, col);
	        UF1.union(0, col);
		}
		// last row
		if (row == N)
		{
			UF.union(N * N + 1, (row - 1) * N + col);
		}
		// else, set this site open
		grid[row][col] = true;
		
		///// and see if its neighbors is full ////
		
		// left neighbor
		if (col > 1 && isOpen(row, col-1))
		{
			UF.union((row-1) * N + col, (row-1) * N + col - 1);
		}
		// right neighbor
		if (col < N && grid[row][col+1])
		{
			UF.union((row-1) * N + col, (row-1) * N + col + 1);
		}
		// up neighbor
		if (row > 1 && isOpen(row-1, col))
		{
			UF.union((row-1) * N + col, (row-2) * N + col);
		}
		// down neighbor
		if (row < N && isOpen(row+1, col))
		{
			UF.union((row-1) * N + col, row * N + col);
		}

	}
	
	// is site (row, col) open?
	public boolean isOpen(int row, int col)
	{
		if (row < 1 || col < 1 || row > N || col > N)
		{
             throw new java.lang.IndexOutOfBoundsException();
		}
		return grid[row][col];
	}
	
	// is site (row, col) full?
	public boolean isFull(int row, int col) 
	{
		if (row < 1 || col < 1 || row > N || col > N)
		{
		    throw new java.lang.IndexOutOfBoundsException();
		}
		
		if (grid[row][col])
		{
			if (UF.connected(0, (row-1) * N + col))
			{
				return true;
			}
		}
		return false;
	}
	
	// number of open sites
	public int numberOfOpenSites()
	{
		return UF.count();
	}     
   	
	// does the system percolate?
	public boolean percolates()
	{
		return UF.connected(0, N * N + 1);
	}
	
}
