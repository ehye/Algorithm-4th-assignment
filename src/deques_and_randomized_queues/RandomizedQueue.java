package deques_and_randomized_queues;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
	private int n = 0;
	private Item[] items;
	
	// construct an empty randomized queue
	public RandomizedQueue()
	{
		items = (Item[]) new Object[2];
	}

	// is the queue empty?
	public boolean isEmpty()
	{
		return n == 0;
	}

	// return the number of items on the queue
	public int size()
	{
		return n;
	}

	// add the item
	public void enqueue(Item item)
	{
		if (item == null) throw new java.lang.NullPointerException();
		if (n == items.length) resize(2 * items.length);
		items[n++] = item;
	}

	// remove and return a random item
	public Item dequeue()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		
		int index = StdRandom.uniform(n);
		Item item = items[index];
		items[index] = items[n-1];
		items[n-1] = null;
		n--;
		if (n > 0 && n == items.length/4) resize(items.length/2);
		return item;
	}

	// return (but do not remove) a random item
	public Item sample()
	{
		if (isEmpty()) throw new java.util.NoSuchElementException();
		int index = StdRandom.uniform(n);
		return items[index];
	}
	
	private void resize(int capacity)
	{
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < n; i++)
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
		private int i = n;
		
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
			
			int index = StdRandom.uniform(n);
			return items[index];
		}
	}
	
	// unit testing (optional)
	public static void main(String[] args)
	{
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer> ();
		Iterator<Integer> it = rq.iterator();		
		rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);
        StdOut.println("Size: " + rq.size());
        StdOut.println(rq.dequeue());
        StdOut.println("Size: " + rq.size());
        StdOut.println(it.next());
//        StdOut.println(rq.sample()); 
//        StdOut.println(rq.sample());
	}
}