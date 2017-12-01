package burrows_wheeler;

import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    
    private static final int R = 256;
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
//        String s = "ABCDEF";
        char[] chars = new char[R];
        for (char i = 0; i < R; i++)
            chars[i] = i;
        String s = String.copyValueOf(chars);
        
        StringBuilder sb = new StringBuilder(s);
        // read input
        String in = BinaryStdIn.readString();
        for (int i = 0; i < in.length(); i++) {
            int index = s.indexOf(in.charAt(i));

            BinaryStdOut.write(index, 8);
            
            // repeatedly move n-1 to n
            for (int j = index; j > 0;)
                sb.setCharAt(j, sb.charAt(--j));
            // and set the first char directly
            sb.setCharAt(0, in.charAt(i));
            // update sequence
            s = sb.toString();
        }
        BinaryStdOut.close();
        /*// build ordered sequence of the characters in the alphabet
        char[] chars = new char[R];
        for (char i = 0; i < R; i++)
            chars[i] = i;
        String s = String.copyValueOf(chars);
        
        // read input
//        BinaryStdIn.readByte();
        String in = "CAAABCCCACCF";
        for (int i = 0; i < s.length(); i++) {
//        while (!BinaryStdIn.isEmpty()) {

            // print char
            char selectChar = in.charAt(i);
            StdOut.print(chars[selectChar]);
            // print index
//            char selectChar = BinaryStdIn.readChar();
//            BinaryStdOut.write(chars[selectChar], 8);
            
            char temp = chars[selectChar];
            // repeatedly move n-1 to n
            while (selectChar > 0)
                chars[selectChar] = chars[--selectChar];
            // and set the first char directly
            chars[0] = temp;
        }
//        BinaryStdOut.close();
*/    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++)
            chars[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            // print char
            char selectChar = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[selectChar], 8);
            
            char temp = chars[selectChar];
            // repeatedly move n-1 to n
            while (selectChar > 0)
                chars[selectChar] = chars[--selectChar];
            // and set the first char directly
            chars[0] = temp;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }

}
