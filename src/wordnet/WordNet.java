package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    
    private final SAP sap;
    private final Digraph G;
    private final ArrayList<String> synonymList;
    private final SET<Hypernym> hypernymsSET; // SET通过compareTo来比较
    
    private class Hypernym implements Comparable<Hypernym> {
        private final String nounId;
        private final ArrayList<Integer> hypernymsId;
        
        public Hypernym(String nounId) {
            if (nounId == null)
                throw new IllegalArgumentException();
            hypernymsId = new ArrayList<>();
            this.nounId = nounId; 
        }
        
        @Override
        public int compareTo(Hypernym that) {
            return this.nounId.compareTo(that.nounId);
        }
        
        private void addId(int id) {
            this.hypernymsId.add(id);
        }
        
    }
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException();
        
        synonymList = new ArrayList<String>();
        hypernymsSET = new SET<Hypernym>();
        readSynsets(synsets);
        G = new Digraph(synonymList.size());
        readHypernyms(hypernyms);
        sap = new SAP(G);
    }

    /*
     * The first field is the synset id (an integer)
     * the second field is the synonym set (or synset)
     */
    private void readSynsets(String synsets) {              
        In inSynsets = new In(synsets);
        while (inSynsets.hasNextLine()) {
            String[] line = inSynsets.readLine().split(",");
            Integer id = Integer.parseInt(line[0]);
            String synsets_nouns = line[1];                     // get nouns
            String[] nouns = synsets_nouns.split(" ");
            for (String noun : nouns){
                Hypernym hypernym = new Hypernym(noun);
                if (hypernymsSET.contains(hypernym)){           // if exist
                    hypernym = hypernymsSET.ceiling(hypernym);  // find ceiling
                    hypernym.addId(id);                         // add this id
                } else {                                            // else
                    hypernym.addId(id);                         // add this new hypernym
                    hypernymsSET.add(hypernym);
                }
            }
            synonymList.add(synsets_nouns);
        }
        
    }
    
    /*
     * The first field is a synset id
     * subsequent fields are the id numbers of the synset's hypernyms. 
     */
    private void readHypernyms(String hypernyms) {
        boolean[] isRoot = new boolean[synonymList.size()];
        for (boolean b : isRoot) b = true;
        
        In inHypernyms = new In(hypernyms);
        while (inHypernyms.hasNextLine()){
            String[] line = inHypernyms.readLine().split(",");
            int nounId = Integer.parseInt(line[0]);
            isRoot[nounId] = false;
            for (int i = 1; i < line.length; i++) {
                G.addEdge(nounId, Integer.parseInt(line[i]));
            }
        }
        
        checkDAG(isRoot);
    }
    
    private void checkDAG(boolean[] isRoot) {
        // test for root: no cycle
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle()){
            throw new java.lang.IllegalArgumentException();
        }
        // test for root: no more than one candidate root
        int rootCount = 0;
        for (boolean root : isRoot){
            if (root){
                rootCount++;
            }
        }
        if (rootCount > 1){
            throw new java.lang.IllegalArgumentException();
        }
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return synonymList.iterator();
            }
        }; 
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return hypernymsSET.contains(new Hypernym(word));
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            Hypernym a = hypernymsSET.ceiling(new Hypernym(nounA));
            Hypernym b = hypernymsSET.ceiling(new Hypernym(nounB));
            return sap.length(a.hypernymsId, b.hypernymsId);
        }
        throw new IllegalArgumentException();
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            Hypernym a = hypernymsSET.ceiling(new Hypernym(nounA));
            Hypernym b = hypernymsSET.ceiling(new Hypernym(nounB));
            return synonymList.get(sap.ancestor(a.hypernymsId, b.hypernymsId));
        }
        throw new IllegalArgumentException();
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        String na = "Brown_Swiss";
        String nb = "barrel_roll";
//        StdOut.println(wn.isNoun("anamorphosis"));
        StdOut.println(wn.distance(na, nb));
        StdOut.println(wn.sap(na, nb));
    }

}
