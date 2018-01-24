import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 24.01.2018.
 */
public class CountableList<E> extends ArrayList<E> {
	private Map<E, Integer> counts = new HashMap<>();

	public boolean add(E e) {
		if (!counts.containsKey(e)) {
			counts.put(e, 1);
		} else {
			counts.put(e, counts.get(e) + 1);
		}

		return super.add(e);
	}

	public boolean remove(Object o) {
		counts.remove(o);
		return super.remove(o);
	}

	public void clear() {
		counts.clear();
		super.clear();
	}

	public Map<E, Integer> getCounts() {
		return counts;
	}

	public void add(int index, E element) {
		throw new NotImplementedException();
	}

	public E set(int index, E element) {
		throw new NotImplementedException();
	}

	public E remove(int index) {
		throw new NotImplementedException();
	}
}
