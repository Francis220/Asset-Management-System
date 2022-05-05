package com.iffi;

import java.util.Iterator;

/**
 * Iterator to allow the SortedList to be used in for each loops.
 * 
 * @author sinezanz && eallen
 *
 * @param <T>
 */

public class ListIterator<T> implements Iterator<T> {

	private SortedList<T> list;
	private int position;

	public ListIterator(SortedList<T> list) {
		this.list = list;
		this.position = 0;
	}

	public boolean hasNext() {
		return (this.position < list.getSize());
	}

	public T next() {
		return list.getElementAt(this.position++);
	}

}
