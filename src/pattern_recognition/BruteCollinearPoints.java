package pattern_recognition;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
//	private int num = 0;
//	private LineSegment[] lineSegments = new LineSegment[5];
	private Point[] points;
	private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {		
		//checkCornerCases(points);
//		this.points = new Point[points.length];
		this.points = points;
		if (points == null) 
			throw new java.lang.NullPointerException();
		for (int i = 0; i < this.points.length - 1; i++) 
			if (this.points[i].compareTo(this.points[i+1]) == 0)
				throw new java.lang.IllegalArgumentException();
		
		this.points = points;
		
		int N = points.length;
		Arrays.sort(this.points);
		for (int ip = 0; ip < N; ip++) {
		    for (int iq = ip + 1; iq < N; iq++) {
		        for (int ir = iq + 1; ir < N; ir++) {
		            for (int is = ir + 1; is < N; is++) {
		            	Point p = this.points[ip];
		            	Point q = this.points[iq];
		            	Point r = this.points[ir];
		                Point s = this.points[is];
		                double slopeToQ = p.slopeTo(q);
		                double slopeToR = p.slopeTo(r);
		                double slopeToS = p.slopeTo(s);
		                if (slopeToQ == slopeToR && slopeToQ == slopeToS)
		                		lineSegments.add(new LineSegment(p, s));
		            }
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
		for (int i = 0; i < this.points.length - 1; i++) 
			if (this.points[i].compareTo(this.points[i+1]) == 0)
				throw new java.lang.IllegalArgumentException();
	}
}
