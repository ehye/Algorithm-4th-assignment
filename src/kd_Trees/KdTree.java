package kd_Trees;

/*
 * http://blog.csdn.net/liuweiran900217/article/details/20917495
 * http://www.cnblogs.com/tiny656/p/3873510.html
 */

import java.util.TreeSet;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	
	private static class Node {
		private boolean isVertical;
		private Point2D p;
		// the axis-aligned rectangle corresponding to this node
//		private RectHV rect;
		// the left/bottom subtree
		private Node lb;
		// the right/top subtree
		private Node rt;
				
		public Node(Point2D p, boolean isVertical) {
			this.p = p;
			this.isVertical = isVertical;
			this.lb = null;
			this.rt = null;
		}
	}
	
	private int size;
	private Node root;
    private final RectHV RECT = new RectHV(0, 0, 1, 1);

	// construct an empty set of points 
	public KdTree() {
		this.root = null;
		this.size = 0;
	}

	// is the set empty? 
	public boolean isEmpty() {
		return this.size == 0;
	}

	// number of points in the set 
	public int size() {
		return this.size;		
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();
		this.root = insert(root, p, true);
		// 'true': the first point is vertical insert
	}
		
	private Node insert(Node node, Point2D p, boolean isVertical) {
		// if it is not in the set, create new node
		if (node == null) {
			size++;
			return new Node(p, isVertical);
		}
		
		// already in, return it
		if (node.p.equals(p))
			return node;
		
		// else insert it
		// isVertical = !parent.isVertical
//		int cmp = p.compareTo(node.p);
//		if (cmp < 0) {
//            node.lb = insert(node.lb, p, !node.isVertical);
//        }
//		if (cmp > 0){
//            node.rt = insert(node.rt, p, !node.isVertical);
//        }
//        return node;
		
		if (node.isVertical && p.x() < node.p.x() || !node.isVertical && p.y() < node.p.y()) {
			node.lb = insert(node.lb, p, !node.isVertical);
        } else {
            node.rt = insert(node.rt, p, !node.isVertical);
        }
        return node;
	}
	
	// does the set contain point p? 
	public boolean contains(Point2D p) {
		if (p ==null) return false;
		return contains(root, p, false);
	}
	
	private boolean contains(Node node, Point2D p, boolean orientation) {
        int cmp = p.compareTo(node.p);
        if      (cmp < 0) return contains(node.lb, p, !orientation);
        else if (cmp > 0) return contains(node.rt, p, !orientation);
        else return true;
	}
	
	// draw all points to standard draw 
	public void draw() {      
		StdDraw.setScale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
		
        RECT.draw();
		draw(root, RECT);
	}
	
	private void draw(Node node, RectHV rect) {
		if (node == null) return;
		
		// drawing the points
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);		
		StdDraw.point(node.p.x(), node.p.y());
		
		// drawing the splitting lines
		StdDraw.setPenRadius();
		if (node.isVertical) {
			// vertical
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(node.p.x(), rect.ymin(), node.p.x(), rect.ymax());
		}
		else {
			// horizontal
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(rect.xmin(), node.p.y(), rect.xmax(), node.p.y());
		}

		// recursively draw children
		draw(node.lb, leftRect(rect, node));
		draw(node.rt, rightRect(rect, node));
	}
	
	private RectHV leftRect(final RectHV rect, final Node node) {
        if (node.isVertical) {
            return new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
        }
    }

    private RectHV rightRect(final RectHV rect, final Node node) {
        if (node.isVertical) {
            return new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
        }
    }

	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
        final TreeSet<Point2D> rangeSet = new TreeSet<Point2D>();
        range(root, RECT, rect, rangeSet);
        return rangeSet;
	}
	
	private void range(final Node node, final RectHV qrect, final RectHV rect, final TreeSet<Point2D> rangeSet) {
        if (node == null) return;

        if (rect.intersects(qrect)) {									// if query rect is in rectangle
            final Point2D p = new Point2D(node.p.x(), node.p.y());
            if (rect.contains(p)) rangeSet.add(p);						// find the point

            // pruning rule
            if (node.isVertical) {
            	if (qrect.xmax() < p.x())
            		range(node.lb, leftRect(qrect, node), rect, rangeSet);
            	if (qrect.xmax() > p.x())
            		range(node.rt, rightRect(qrect, node), rect, rangeSet);
            	if (qrect.contains(p)) {
            		range(node.lb, leftRect(qrect, node), rect, rangeSet);
            		range(node.rt, rightRect(qrect, node), rect, rangeSet);
            	}
            }
            else {
            	if (qrect.ymax() < p.y())
            		range(node.lb, leftRect(qrect, node), rect, rangeSet);
            	if (qrect.ymax() > p.y())
            		range(node.rt, rightRect(qrect, node), rect, rangeSet);
            	if (qrect.contains(p)) {
            		range(node.lb, leftRect(qrect, node), rect, rangeSet);
            		range(node.rt, rightRect(qrect, node), rect, rangeSet);
            	}
            }
        }    
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
//        return nearest(root, RECT, p, null);
		if (root == null) return null;
        Point2D retp = null;
        double mindis = Double.MAX_VALUE;
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            double dis = p.distanceSquaredTo(x.p);
            if (dis < mindis) {
                retp = x.p;
                mindis = dis; 
            }
            if (x.lb != null && x.lb.p.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.lb);
            if (x.rt != null && x.rt.p.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.rt);
        }
        return retp;
	}
	
	private Point2D nearest(final Node node, final RectHV rect, final Point2D p, Point2D candidate) {
        if (node == null) return candidate;

        double dqn = 0.0;
        double drq = 0.0;
        RectHV leftRect = null;
        RectHV rigtRect = null;
        final Point2D query = new Point2D(p.x(), p.y());

        if (candidate != null) {
            dqn = query.distanceSquaredTo(candidate);
            drq = rect.distanceSquaredTo(query);
        }

        if (candidate == null || dqn > drq) {
            final Point2D point = new Point2D(node.p.x(), node.p.y());
            if (candidate == null || dqn > query.distanceSquaredTo(point))
                candidate = point;

            if (node.isVertical) {
            	// only p.x() changes
                leftRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                rigtRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                
                if (p.x() < node.p.x()) {
                    candidate = nearest(node.lb, leftRect, p, candidate);
                    candidate = nearest(node.rt, rigtRect, p, candidate);
                } 
                else {
                    candidate = nearest(node.rt, rigtRect, p, candidate);
                    candidate = nearest(node.lb, leftRect, p, candidate);
                }
            } 
            else {
            	// only p.y() changes
                leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                rigtRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());

                if (p.y() < node.p.y()) {
                    candidate = nearest(node.lb, leftRect, p, candidate);
                    candidate = nearest(node.rt, rigtRect, p, candidate);
                } else {
                    candidate = nearest(node.rt, rigtRect, p, candidate);
                    candidate = nearest(node.lb, leftRect, p, candidate);
                }
            }
        }
        return candidate;
	}
}