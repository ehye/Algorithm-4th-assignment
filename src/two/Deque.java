package two;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> 
{
	private int N;
	private Node first, last;
	
	private class Node
	{
		Item item;
		Node prev;
		Node next;
	}

	// construct an empty deque
	public Deque()
	{
		first = last = null;
		N = 0;
	}                           

	// is the deque empty?
	public boolean isEmpty()
	{
		return N == 0;
	}
	
	// return the number of items on the deque
	public int size()
	{
		return N;
	}                        

	// add the item to the front
	public void addFirst(Item item)
	{
		if (item == null) throw new java.lang.NullPointerException();
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
	    if (isEmpty()) 	last = first;
	    else 			oldfirst.prev = first;    
	    N++;
	}          

	// add the item to the end
	public void addLast(Item item)
	{
		if (item == null) throw new java.lang.NullPointerException();
		Node oldlast = last;
		last = new Node();
		last.item = item;
  	    last.prev = oldlast;
  	    if (isEmpty()) 	first = last;
	    else 			oldlast.next = last;
  	    N++;
	}           

	// remove and return the item from the front
	public Item removeFirst()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		Item item = first.item;
		first = first.next;
		if (isEmpty()) 	last = null;
		else 			last.next = null;
		N--;
		return item;
	}                

	// remove and return the item from the end
	public Item removeLast()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		Item item = last.item;
		last = last.prev;
		if (isEmpty()) 	first = null;
		else 			last.next = null;
		N--;
		return item;
	}                 

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator()
	{
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item>
	{
		private Node current = first;
		
		public boolean hasNext() 
		{
			return current != null;
		}
		
		public void remove()
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public Item next() 
		{
			if (isEmpty()) throw new java.util.NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;			
		}
	}

	// unit testing (optional)
	public static void main(String[] args)
	{
//		   Deque<String> deque = new Deque<String> ();
//	       deque.addLast("aa");
//	       deque.addFirst("bb");
//	       deque.addFirst("cc");
//	       deque.addLast("dd");
//	       StdOut.println(deque.removeLast());
//	       StdOut.println(deque.removeFirst());
//	       StdOut.println(deque.removeFirst());
//	       StdOut.println(deque.removeLast());
//	       StdOut.print("size:"+deque.size());
	}   

}