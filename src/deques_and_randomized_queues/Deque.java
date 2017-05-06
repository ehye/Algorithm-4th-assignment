package deques_and_randomized_queues;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Deque<Item> implements Iterable<Item> 
{
	private int n;
	private Node first, last;
	
	private class Node
	{
		private Item item;
		private Node prev;
		private Node next;
	}

	// construct an empty deque
	public Deque()
	{
		first = last = null;
		n = 0;
	}                           

	// is the deque empty?
	public boolean isEmpty()
	{
		return n == 0;
	}
	
	// return the number of items on the deque
	public int size()
	{
		return n;
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
	    n++;
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
  	    n++;
	}           

	// remove and return the item from the front
	public Item removeFirst()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		Node oldfirst = first;
		first = first.next;
		Item item = oldfirst.item;
		oldfirst = null;
		n--;
		return item;
	}                

	// remove and return the item from the end
	public Item removeLast()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		Node oldlast = last;
		last = last.prev;
		Item item = oldlast.item;
		oldlast = null;
		n--;
		return item;
	}                 

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator()
	{
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<Item>
	{
		private Node current;

		public Item next() 
		{
			if (isEmpty()) throw new java.util.NoSuchElementException();
			
			current = first;
			Item item = current.item;
			current = current.next;
			return item;

		}
		
		public void remove()
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public boolean hasNext() {

			return n > 0;
		}
	}

	// unit testing (optional)
	public static void main(String[] args)
	{
		   Deque<Integer> deque = new Deque<Integer> ();
		   
		   deque.addLast(1);
		   deque.addLast(2);
		   StdOut.println(deque.removeLast());
	}
}