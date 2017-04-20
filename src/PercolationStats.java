
public class PercolationStats {

	public PercolationStats(int n, int trials)  
	{
		if (n <= 0 || trials <= 0)
		{
			throw new java.lang.IllegalArgumentException();
		}
	}
	
	// perform trials independent experiments on an n-by-n grid
	public double mean()
	{
		return 0.0;
	}
	
	// sample mean of percolation threshold
   	public double stddev()
   	{
   		return 0.0;
   	}
   	
   	// sample standard deviation of percolation threshold
   	public double confidenceLo()
   	{
   		return 0.0;
   	}
   	
   	// low  endpoint of 95% confidence interval
   	public double confidenceHi()
   	{
   		return 0.0;
   	}
   	
   	public static void main(String[] args)
   	{
   		
   	}
}
