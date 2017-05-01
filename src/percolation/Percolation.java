package percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int countOpen = 0;
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf1;
    
    // create n-by-n grid, with all sites blocked     
    public Percolation(int n)
    {
        if (n < 1)
        {
            throw new java.lang.IllegalArgumentException();
        }
        this.n = n;
        grid = new boolean[n+1][n+1];
        uf = new WeightedQuickUnionUF(n*n + 2); // pdf 58
        uf1 = new WeightedQuickUnionUF(n*n + 1);
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (row < 1 || col < 1 || row > n || col > n)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        if(!isOpen(row, col))
        {
	        // first row
	        if (row == 1)
	        {
	            uf.union(0, col);
	            uf1.union(0, col);
	        }
	        // last row
	        if (row == n)
	        {
	            uf.union(n * n + 1, (row - 1) * n + col);
	        }
	        // else, set this site open
	        grid[row][col] = true;
	        countOpen++;
	        
	        System.out.println("*-"+countOpen+"-*");
	        System.out.println("*-"+numberOfOpenSites()+"-*");
	        
	        ///// and see if its neighbors is full ////
	        	        
	        // left neighbor
	        if (col > 1 && isOpen(row, col-1))
	        {
	            uf.union((row-1) * n + col, (row-1) * n + col - 1);
	            uf1.union((row-1) * n + col, (row-1) * n + col - 1);
	        }
	        // right neighbor
	        if (col < n && grid[row][col+1])
	        {
	            uf.union((row-1) * n + col, (row-1) * n + col + 1);
	            uf1.union((row-1) * n + col, (row-1) * n + col + 1);
	        }
	        // up neighbor
	        if (row > 1 && isOpen(row-1, col))
	        {
	            uf.union((row-1) * n + col, (row-2) * n + col);
	            uf1.union((row-1) * n + col, (row-2) * n + col);
	        }
	        // down neighbor
	        if (row < n && isOpen(row+1, col))
	        {
	            uf.union((row-1) * n + col, row * n + col);
	            uf1.union((row-1) * n + col, row * n + col);
	        }
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row < 1 || col < 1 || row > n || col > n)
        {
             throw new java.lang.IndexOutOfBoundsException();
        }
        
        if (grid[row][col])
        {
        	return true;        	
        }
        else
        {
        	return false;
        }
    }
    
    // is site (row, col) full?
    public boolean isFull(int row, int col) 
    {
        if (row < 1 || col < 1 || row > n || col > n)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        if (grid[row][col])
        {
            if (uf1.connected(0, (row-1) * n + col))
            {
                return true;
            }
        }
		return false;
    }
    
    // number of open sites
    public int numberOfOpenSites()
    {
        return countOpen;
    }     
    
    // does the system percolate?
    public boolean percolates()
    {
        return uf.connected(0, n * n + 1);
    }
}
