import java.util.Iterator;
import java.util.LinkedList;


public class HashTable<K,V> implements Map<K,V> 
{
	private Node[] table;
	private int size;
	
	public HashTable(int s)
	{
		table = new Node[s];		//make hash array of size given by the user
		size = 0;					//initial size is 0
	}
	
	public V get(K key) {					//return value based on key entered
		int hash = key.hashCode();
		int comp = compress(hash);
		return (V) table[comp].getVal();	//if collision, only returns first value in linked list
	}
	
	public Iterator<V> getCollisions(K key)	//get all of the values of a key
	{
		int hash = key.hashCode();
		int comp = compress(hash);
		LinkedList l = new LinkedList();
		
		Node<K,V> temp = table[comp];
		
		while (temp != null)				//store all of the values at the key into LinkedList
		{
			l.addLast(temp.getVal());
			temp = temp.next;
		}
		return l.iterator();
		
		
	}
	
	private int compress(int hash)		//compression function
	{
		return (Math.abs(hash) % table.length);		//return the hash code compressed to an index of table
	}
	
	public V put(K key, V value) 
	{
		int index = compress(key.hashCode());		//get index of key
		Node<K,V> n = new Node<K,V>(key, value);	//new node to be inserted
		if (table[index] == null)					//if no previous nodes at this key,
		{
			table[index] = n;						//set this as the only node
		}
		else
		{
			Node<K,V> temp = table[index];			//temporary cursor
			while (temp.next != null)				//while cursor is not at the last node,
			{
				temp = temp.next;					//cursor is cursor's next node
			}
			temp.next = n;							//n becomes last node
		}
		size++;										//iterate on size
		return (V) n.getVal();						//return value stored
	}


	public V remove(K key) 
	{
		int comp = compress(key.hashCode());
		
		if (table[comp] != null)
		{
			int i = 0;
			Node<K,V> temp = table[comp];
			while (temp != null)						//get the number of values stored at that key
			{
				i++;
				temp = temp.next;
			}
			size -= i;									//subtract the number of entries to be deleted from that key
			V val = (V) table[comp].getVal();			//save value stored in the first node to be returned
			
			table[comp] = null;							//make the entry equal to null
			return val;
		}
		return null;									//no value stored at this key
	}

	public int size() {									//returns the number of values stored in hash table
		return size;
	}

	public boolean isEmpty() {
		if (size() == 0)
		{
			return true;
		}
		return false;
	}

	public Iterator<K> keys() {
		LinkedList l = new LinkedList();			//linked list to store all the keys
		for (int i = 0; i < table.length; i++)		//input keys into l
		{
			l.addLast(i);
		}
		return l.iterator();						//return an iterator of the keys
	}

	public Iterator<V> values() 
	{
		LinkedList l = new LinkedList();			//linked list to store all the values
		for (int i = 0; i < table.length; i++)		//input values for each key into l
		{
			if (table[i] != null)					//if the key has values, insert them into the list
			{
				Node<K,V> temp = table[i];
				while (temp != null)
				{
					l.addLast(temp.getVal());
					temp = temp.next;
				}
			}
		}
		return l.iterator();						//return an iterator of the values
	}
	
	private static class Node<K,V>					//internal class that stores collisions and entries
	{
		private K key;
		private V val;
		private Node<K,V> next;
		
		public Node(K k, V v)
		{
			key = k;
			val = v;
			next = null;
		}
		
		public K getKey()
		{
			return key;
		}
		public V getVal()
		{
			return val;
		}
	}

}
