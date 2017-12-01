package burrows_wheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.LSD;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    private static final int R = 256; // Radix of a byte.
//    private static StringBuilder tranString;
    
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        int first = 0;
        String in = BinaryStdIn.readString();
//        String in = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(in);
        while (csa.index(first) != 0)
            first++;
        
        BinaryStdOut.write(first);
//        StdOut.println(first);
        for (int i = 0; i < csa.length(); i++) {
            // ?
            BinaryStdOut.write(in.charAt((csa.index(i) + in.length() - 1) % in.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
//        int first = 3;
//        String s = "ARD!RCAAAABB";
        int n = s.length();
        int[] next = new int[n];
        
        /* use LSD algorithm for char */
        // compute frequency counts
        int[] count = new int[R + 1];
        for (int i = 0; i < n; i++)
            count[s.charAt(i) + 1]++;
        
        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        
        // move data
        for (int i = 0; i < n; i++)
            next[count[s.charAt(i)]++] = i;
        
        // print out
        for (int j = 0, k = next[first]; j < n; j++, k = next[k])
//            StdOut.print(s.charAt(k));
            BinaryStdOut.write(s.charAt(k));
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
//        Test();
    }
    
}
