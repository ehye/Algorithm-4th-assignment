import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] results;
    private int count = 0;
    private double mean = 0;
    private double stddev = 0;
    private double confidenceHi = 0;
    private double confidenceLo = 0;
    private Percolation perc;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials)  
    {
        if (n <= 0 || trials <= 0)
        {
            throw new java.lang.IllegalArgumentException();
        }
        
        results = new double[trials];
        
        for (int i = 0; i < trials; i++, count = 0)
        {
            perc = new Percolation(n);
            while (!perc.percolates())
            {
                int row = StdRandom.uniform(n)+1;
                int col = StdRandom.uniform(n)+1;
                while (perc.isOpen(row, col))
                {
                    // regenerate random
                    row = StdRandom.uniform(n)+1;
                    col = StdRandom.uniform(n)+1;
                }
                perc.open(row, col);
                count++;
            }
            perc = null;
            results[i] = count/(n * n * 1.0);
        }
        
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(trials);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(trials);
    }

    // sample mean of peration threshold
    public double mean()
    {
        return mean;
    }
    
    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return stddev;
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return confidenceLo;
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return confidenceHi;
    }
    
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(n, trials);
        
        System.out.println("mean\t= " + stats.mean());
        System.out.println("stddev\t= " + stats.stddev());
        System.out.println("95% confidence interval = [" 
                + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
        
    }
}
