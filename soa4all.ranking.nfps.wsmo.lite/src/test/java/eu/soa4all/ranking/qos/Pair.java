/**
 * 
 */
package eu.soa4all.ranking.qos;

/**
 * @author Benjamin Hiltpolt
 *
 */
public class Pair<T,V> {

	protected T first;
	protected V second;
	
	public Pair(T first, V second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}
	
	
}
