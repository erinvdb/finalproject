import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;


public class DictSkipList<K,V> extends ConcurrentSkipListMap<K,V>
{
	public HashTable<K,V> collisions;			//hash table to handle collisions
	
	public DictSkipList()						//constructor that instantiates collisions hash table
	{
		super();
		collisions = new HashTable<K,V>(50);	//arbitrary size of 50 
	}
	public V put(K k, V v)						//override default put()
	{
		if (this.get(k) == null)				//if this key does not have an associated value,
		{
			super.put(k, v);					//use default put method
			return v;							//return value
		}
		
		collisions.put(k, v);					//if a value already exists at key k, put new value in hash table
		
		return v;								//return value
		
	}

	public V removeAll(K k)						//remove value associated with k from SkipList and collisions hash table
	{
		collisions.remove(k);					//remove from hash table
		return super.remove(k);					//remove from skip list
	}
	
	public LinkedList<V> getAll(K k)			//return all values associated with key k
	{
		LinkedList<V> vals = new LinkedList<V>();	//initialize LinkedList that will be returned
		vals.add(get(k));							//add the one value stored in the SkipList
		
		if (collisions.get(k) != null)				//if other values exist in hash table,
		{
			Iterator<V> col = collisions.getCollisions(k);	//get those values
			while (col.hasNext())							//add those values to vals
			{
				vals.add(col.next());
			}
		}
		return vals;								//return all of the values stored at key k
	}

}
