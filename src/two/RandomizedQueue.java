package two;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
	private int N = 0;
	private Item[] items;
	
	// construct an empty randomized queue
	public RandomizedQueue()
	{
		items = (Item[]) new Object[10];
	}

	// is the queue empty?
	public boolean isEmpty()
	{
		return N == 0;
	}

	// return the number of items on the queue
	public int size()
	{
		return N;
	}

	// add the item
	public void enqueue(Item item)
	{
		if (item == null) throw new java.lang.NullPointerException();
		if (N == items.length) resize(2 * items.length);
		items[N++] = item;
	}

	// remove and return a random item
	public Item dequeue()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		Item item = items[--N];
		items[N] = null;
		if (N > 0 && N == items.length/4) resize(items.length/2);
		return item;
	}

	// return (but do not remove) a random item
	public Item sample()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		items[StdRandom.uniform(N) + 1] = null;
//		int i = StdRandom.uniform(N) + 1;
//		Item temp = items[i];
//		StdOut.println("i = " + i);
//		items[i] = null;
//		N--;
//		if (N > 0 && N == items.length/4) resize(items.length/2);
		
		return temp;
	}
	
	private void resize(int capacity)
	{
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++)
		{
			copy[i] = items[i];
		}
		items = copy;
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator()
	{
		return new ListIterator();
	}

	private class ListIterator implements Iterator<Item>
	{
		private int i = N;
		
		public boolean hasNext() 
		{
			return i > 0;
		}
		
		public void remove()
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public Item next() 
		{
			if (isEmpty()) throw new java.util.NoSuchElementException();
			return items[--i];			
		}
	}
	
	// unit testing (optional)
	public static void main(String[] args)
	{
		
		RandomizedQueue<String> deque = new RandomizedQueue<String> ();
		deque.enqueue("aa");
		deque.enqueue("bb");
		deque.enqueue("cc");
		deque.enqueue("dd");
		StdOut.println(deque.sample());
		StdOut.println(deque.sample());
		StdOut.println(deque.sample());
		StdOut.println(deque.sample());
//		deque.dequeue();
//		deque.dequeue();
//		deque.dequeue();
//		deque.dequeue();
		StdOut.print("size:"+deque.size());
	}
}