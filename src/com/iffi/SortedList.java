package com.iffi;

import java.util.Comparator;
import java.util.Iterator;

/**
 * An array-based implementation for <code>T</code> instances.
 * 
 * @param <T>
 * @author sinezanz && eallen
 */

public class SortedList<T> implements Iterable<T> {

	private T array[];
	private int size;
	private int maxSize = 1;
	private Comparator<T> cmp = null;

	public SortedList() {
		this.array = (T[]) new Object[maxSize];
		this.size = 0;
	}

	public SortedList(Comparator<T> cmp) {
		this.array = (T[]) new Object[maxSize];
		this.size = 0;
		this.cmp = cmp;
	}

	public int getSize() {
		return this.size;
	}

	/**
	 * Removes all entries from the list.
	 */
	public void clear() {
		this.size = 0;
	}

	/**
	 * Determines whether the list is empty.
	 * 
	 * @return true if the list is empty, or false if not
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Adds elements to the list ordered using a comparator
	 * 
	 * @param item element to add
	 */
	public void insert(T item) {
		if (this.size == 0) {
			this.insertAt(0, item);
			return;
		}
		if (this.cmp == null) {
			this.insertAt(size, item);
			return;
		}

		int position = 0;
		while ((position < this.size) && (this.cmp.compare(item, array[position]) <= 0)) {
			position++;
		}
		this.insertAt(position, item);

	}

	/**
	 * Removes element at specified position
	 * 
	 * @param position
	 */
	public void remove(int position) {
		if ((position < 0) || (position >= this.size)) {
			throw new IllegalArgumentException("Index out of Bounds");
		}
		for (int i = position; i < size - 1; i++) {
			this.array[i] = this.array[i + 1];
		}
		this.size--;
		this.decreaseCapacity();
	}

	/**
	 * Adds an element at the specified position
	 * 
	 * @param position: position to add the element at
	 * @param item:     element to add
	 */
	private void insertAt(int position, T item) {
		if ((position < 0) || (position > size)) {
			throw new IllegalArgumentException("Index out of Bounds");
		}

		if (this.size == maxSize) {
			this.increaseCapacity();
		}

		for (int i = this.size; i > position; i--) {
			this.array[i] = this.array[i - 1];
		}
		this.array[position] = item;
		this.size++;

	}

	/**
	 * Increases the maximum size of the array
	 */
	private void increaseCapacity() {
		T newArray[] = (T[]) new Object[this.maxSize + 1];
		for (int i = 0; i < maxSize; i++) {
			newArray[i] = array[i];
		}
		this.array = newArray;
		this.maxSize += 1;
	}

	/**
	 * Decreases the maximum size of the array
	 */
	private void decreaseCapacity() {
		T newArray[] = (T[]) new Object[this.maxSize - 1];
		for (int i = 0; i < maxSize - 1; i++) {
			newArray[i] = array[i];
		}
		this.array = newArray;
		this.maxSize -= 1;
	}

	/**
	 * Method that returns the position of the specified element in the list.
	 * Returns null(-1) if the element is not in list.
	 * 
	 * @param element
	 * @return index
	 */
	public int getIndexOf(T element) {
		if (this.cmp == null) {
			for (int i = 0; i < this.size; i++) {
				if (element == array[i]) {
					return i;
				}
			}
			return -1;
		}

		for (int i = 0; i < this.size; i++) {
			if (cmp.compare(element, array[i]) == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets an element at a specified position
	 * 
	 * @param position
	 * @return
	 */

	public T getElementAt(int position) {
		return this.array[position];
	}

	/**
	 * Print out every element in the array on a new line
	 */

	public void print() {
		String s = "";
		for (int i = 0; i < this.size; i++) {
			s += array[i].toString();

		}
		System.out.println(s);
	}

	/**
	 * Iterator to allow the use of enhanced loops.
	 */

	@Override
	public Iterator<T> iterator() {
		return new ListIterator<T>(this);

	}

}
