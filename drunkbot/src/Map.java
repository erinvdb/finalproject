import java.util.Iterator;

public interface Map<K,V> 
{
	/** Looks for an entry with specied key in the hash table.
	If found, return the entry's value; if not, return null. */
	public V get(K key);
	/** If there is an entry with the given key already in the
	hash table, replace it with the with the new value, and
	return the old value. Otherwise, insert a new entry
	containing key and value into the table, and return null. */
	public V put(K key, V value);
	/** If there is an entry in the hash table with the given
	key, remove the entry from the hash table and return
	the value. Otherwise return null. */
	public V remove(K key);
	/** Returns the number of elements stored in the hash table. */
	public int size();
	/** Returns true if the hash table contains no elements, otherwise false. */
	public boolean isEmpty();
	/** Returns an Iterator that runs through all of the keys in the hash table. */
	public Iterator<K> keys();
	/** Returns an Iterator that runs through all of the values in the hash table. */
	public Iterator<V> values();
}