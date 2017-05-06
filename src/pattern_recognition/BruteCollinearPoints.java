package pattern_recognition;

import java.util.Arrays;

public class BruteCollinearPoints {

	private int num, count_same = 0, i = 1;
	private Point[] points;
	private double[] slopes = new double[5];
	private boolean same_line = false;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		checkCornerCases(points);
		
		// calculate all slopes
		for (int i = 0; i < points.length-1;) {
			double temp = points[0].slopeTo(points[i+1]);
			slopes[i] = temp; 
			i++;
		}
		Arrays.sort(slopes);
		// check slopes
		for (int i = 0; i < points.length-1; i++) {
			for (int j = i+1; j < points.length-1; j++) {
				if (slopes[i] == slopes[j]) 
					count_same++;
			}
			if (count_same >= 3) 
				same_line = true;
		}
		
		this.points = points;
	}

	// the number of line segments
	public int numberOfSegments() {
		return num;
	}

	// the line segments
	public LineSegment[] segments() {
		return null;
	}
	
	private void checkCornerCases(Point[] points) {
		if (points == null) 
			throw new java.lang.NullPointerException();
		for (int i = 0; i < points.length - 1; i++) {
			if (points[i] == null )
				throw new java.lang.NullPointerException();
			else
				for (int j = i+1; j < points.length - 1; j++)
					if (points[i] == points[j])
						throw new java.lang.IllegalArgumentException();
		}
	}

}
