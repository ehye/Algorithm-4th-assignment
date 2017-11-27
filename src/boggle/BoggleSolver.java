package boggle;

import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    
//    private SET<String> dictionary;
    private BoggleTrieST<String> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
//        this.dictionary = new SET<>();
        this.dictionary = new BoggleTrieST<>();
        for (String word : dictionary) {
            this.dictionary.put(word, "1");
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> words = new TreeSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] visited = new boolean[board.rows()][board.cols()];
                dfs(board, i, j, words, visited, "");
            }
        }
        return words;
    }

    private void dfs(BoggleBoard board, int i, int j, TreeSet<String> words, boolean[][] visited, String prefix) {
        if (visited[i][j]) return;

        char letter = board.getLetter(i, j);
        prefix = prefix + (letter == 'Q' ? "QU" : letter); // upcase

        if (prefix.length() > 2 && dictionary.contains(prefix))
            words.add(prefix);
        
        if (!dictionary.isPrefix(prefix))
            return;

        visited[i][j] = true;

        // do a DFS for all adjacent cells
        if (i > 0) {
            dfs(board, i - 1, j, words, visited, prefix);
            if (j > 0)
                dfs(board, i - 1, j - 1, words, visited, prefix);
            if (j < board.cols() - 1)
                dfs(board, i - 1, j + 1, words, visited, prefix);
        }
        
        if (j > 0)
            dfs(board, i, j - 1, words, visited, prefix);
        
        if (j < board.cols() - 1)
            dfs(board, i, j + 1, words, visited, prefix);
        
        if (i < board.rows() - 1) {
            if (j > 0) dfs(board, i + 1, j - 1, words, visited, prefix);
            if (j < board.cols() - 1) dfs(board, i + 1, j + 1, words, visited, prefix);
            dfs(board, i + 1, j, words, visited, prefix);
        }
        
        visited[i][j] = false;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionary.contains(word))
        switch (word.length()) {
            case 0 :
            case 1 :
            case 2 :
                return 0;
            case 3 :
            case 4 :
                return 1;
            case 5 :
                return 2;
            case 6 :
                return 3;
            case 7 :
                return 5;
            default :
                return 11;
        }
        return 0;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
