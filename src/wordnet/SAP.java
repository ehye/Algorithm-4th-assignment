package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private final Digraph G;
    private int results[] = new int[2];
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    private void search(Object v, Object w, int type) {
        DeluxeBFS vBFS;
        DeluxeBFS wBFS;
        if (type == 1) {
            if ((int)v > G.V()-1 || (int)w > G.V()-1)
                throw new java.lang.IllegalArgumentException();            
            vBFS = new DeluxeBFS(G, (int)v);
            wBFS = new DeluxeBFS(G, (int)w);
        }
        else {
            vBFS = new DeluxeBFS(G, (Iterable<Integer>)v);
            wBFS = new DeluxeBFS(G, (Iterable<Integer>)w);            
        }
        
        int shortestPath = Integer.MAX_VALUE;
        int currentPath = Integer.MAX_VALUE;
        int shortestAncestor = Integer.MAX_VALUE;
        
        boolean[] vMarked = vBFS.getMarked();
        boolean[] wMarked = wBFS.getMarked();
        
        for (int i = 0; i < vMarked.length; i++) {
            if (vMarked[i] && wMarked[i]) {   // is same ancestral
                currentPath = vBFS.distTo(i) + wBFS.distTo(i);  // update shortest path
                if (currentPath < shortestPath) {
                    shortestPath = currentPath;                
                    shortestAncestor = i;                           // update shortest path
                }
            }
        }
        
        if (shortestPath == Integer.MAX_VALUE) {
            results[0] = -1;
            results[1] = -1;
        }
        else {
            results[0] = shortestPath;
            results[1] = shortestAncestor;
        }
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        search(v, w, 1);
        return results[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        search(v, w, 1);
        return results[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        search(v, w, 2);
        return results[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        search(v, w, 2);
        return results[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}