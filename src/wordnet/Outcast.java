package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // calculate every distance
        int[] sum = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i; j < sum.length; j++) {
                sum[i] += wordnet.distance(nouns[i], nouns[j]);
                if (i != j) {
                    sum[j] += wordnet.distance(nouns[i], nouns[j]);;
                }
            }
        }
        
        // select max
        int maxIndex = 0;
        int maxDistance = 0;
        for (int i=0; i<sum.length; i++){
            if (sum[i] > maxDistance){
                maxDistance = sum[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }
    
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
