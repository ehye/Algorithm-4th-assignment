package pattern_recognition;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
//	private int num = 0;
//	private LineSegment[] lineSegments = new LineSegment[5];
	
	private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
	private Point[] points;
	
	// finds all line segments containing 4 or more points
	public FastCollinearPoints(Point[] points) {
		this.points = points;
		checkCornerCases(points);
		
		Arrays.sort(points);
		Point origin = points[0]; // can't do this
		Point[] others = new Point[points.length - 2];
		for (int i = 0; i < points.length - 2;) {
			others[i] = points[++i];			
		}
		
		Arrays.sort(others, origin.slopeOrder());
		
		// check if any 3 point with p are collinear
		int N = points.length - 1;
	    for (int iq = 1; iq < N; iq++) {
	        for (int ir = iq + 1; ir < N; ir++) {
	            for (int is = ir + 1; is < N; is++) {
	            	Point q = points[iq];
	            	Point r = points[ir];
	                Point s = points[is];
	                double slopeToQ = origin.slopeTo(q);
	                double slopeToR = origin.slopeTo(r);
	                double slopeToS = origin.slopeTo(s);
	                if (slopeToQ == slopeToR && slopeToQ == slopeToS)
	                	lineSegments.add(new LineSegment(origin, s));
	            }
	        }
	    }
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
		for (int i = 0; i < this.points.length - 1; i++) {
			if (this.points[i].compareTo(this.points[i+1]) == 0)
				throw new java.lang.IllegalArgumentException();
		}
	}
}
