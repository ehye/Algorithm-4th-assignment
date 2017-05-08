package pattern_recognition;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	
	private Point[] copies;
	private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
	
	// finds all line segments containing 4 or more points
	public FastCollinearPoints(Point[] points) {
		checkCornerCases(points);
		
		copies = new Point[points.length];
		copies = points;
		
		Arrays.sort(copies);
		
		
				
	}

	// the number of line segments
	public int numberOfSegments() {
		return lineSegments.size();
	}

	// the line segments
	public LineSegment[] segments() {
		LineSegment[] result = new LineSegment[lineSegments.size()];
		for (int i = 0; i < lineSegments.size(); i++) {
			result[i] = lineSegments.get(i);
		}
		return result;
	}
	
	private void checkCornerCases(Point[] points) {
		if (points == null) 
			throw new java.lang.NullPointerException();
		for (int i = 0; i < points.length - 1; i++) {
			if (points[i].compareTo(points[i+1]) == 0)
				throw new java.lang.IllegalArgumentException();
		}
	}
}
