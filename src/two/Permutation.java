package two;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Permutation {
	public static void main(String[] args)
	{
		Deque<String> deque = new Deque<String>();
		deque.addLast("one");
		deque.addLast("two");
		deque.addLast("three");
		deque.addLast("four");
		deque.removeLast();
		Iterator<String> i = deque.iterator();
		while (i.hasNext())
		{
			String s = i.next();
			StdOut.println(s);
		}
	}
}
