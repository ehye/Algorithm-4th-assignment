package burrows_wheeler;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private String s = "";
//    private StringBuilder sb;
//    private final ArrayList<String> sm;
    private final Integer[] index;
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;

        // quit genius!
        // algorithm: not to store strings; just compare them using number of shifts
        // sort: key => char; value => index
        index = new Integer[length()];
        for (int i = 0; i < index.length; i++) 
           index[i] = i;
        
        Arrays.sort(index, new Comparator<Integer>() {
           @Override
           public int compare(Integer first, Integer second) {
              // get start indexes of chars to compare
              int firstIndex = first;
              int secondIndex = second;
              // for all characters
              for (int i = 0; i < s.length(); i++) {
                 // if out of the last char then start from beginning
                 if (firstIndex > s.length() - 1) 
                    firstIndex = 0;
                 if (secondIndex > s.length() - 1) 
                    secondIndex = 0;
                 // if first string > second
                 if (s.charAt(firstIndex) < s.charAt(secondIndex)) 
                    return -1;
                 else if (s.charAt(firstIndex) > s.charAt(secondIndex)) 
                    return 1;
                 // watch next chars
                 firstIndex++;
                 secondIndex++;
              }
              // equal strings
              return 0;
           }
        });
        
        /*sm = new ArrayList<>(s.length() + 1);
        sm.add(s);
        for (int i = 1; i < s.length(); i++) {
            sb = new StringBuilder(s.length() + 1);
            // add string
            sb.append(sm.get(i-1));
            // add first char to tail
            sb.append(sb.charAt(0));
            // suffix
            sb.replace(0, sb.capacity() - 1, sb.substring(1, sb.length()));
            // replace index to tail - fucked
//            sb.setCharAt(sb.length()-1, (char)Integer.valueOf(i-1));
            sm.add(sb.substring(0, sb.length()-1));
        }
        sm.sort(sortByFirstChar());*/
    }
 
    public static Comparator<String> sortByChar() {
        Comparator<String> comp = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };
        return comp;
    }
    
    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix   
    public int index(int i) {
        if (i < 0 || i > length() - 1) throw new IllegalArgumentException();
        return index[i];
    }
    
    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        StdOut.println(csa.length());
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i));
        }
    }
    
}
