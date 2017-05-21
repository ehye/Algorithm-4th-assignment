package kd_Trees;

import java.awt.Point;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
	
	private SET<Point2D> set;
	private ArrayList<Point2D> result;
	
	// construct an empty set of points 
	public PointSET() {
		set = new SET<Point2D>();
	}

	// is the set empty? 
	public boolean isEmpty() {
		return set.size() == 0;
	}

	// number of points in the set 
	public int size() {
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) 			throw new java.lang.NullPointerException();
		if (!set.contains(p)) 	set.add(p);
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		if (p == null) 
			throw new java.lang.NullPointerException();
		
		return set.contains(p);
	}

	// draw all points to standard draw 
	public void draw() {
		// drawing the points
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		
		for (Point2D point : set) {
			StdDraw.point(point.x(), point.y());
		}
	}

	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new java.lang.NullPointerException();
		}
		
		result = new ArrayList<Point2D>();
		
		for (Point2D point : set) {
			if (rect.contains(point))
				result.add(point);
		}
		return result;
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		if (p == null) 		throw new java.lang.NullPointerException();
		if (set.isEmpty()) 	return null;
		
		double min = Double.MAX_VALUE;
		Point2D result = null;
		
		for (Point2D point : set) {
			double distence = Point.distanceSq(p.x(), p.y(), point.x(), point.y()); 
			if (distence < min) {
				min = distence;
				result = point;		
			}
		}
		return result;
	}

	// unit testing of the methods (optional) 
	public static void main(String[] args) {}
}
