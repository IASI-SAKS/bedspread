package it.cnr.iasi.leks.bedspread.util;

import it.cnr.iasi.leks.bedspread.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RevisedHashSet implements Set<Node> {

	private Set<Node> internalSet;
	
	public RevisedHashSet(){
// This statement ensure the class is Thread-safe		
		this.internalSet = Collections.synchronizedSet(new HashSet<Node>());
	}

	public boolean add(Node e) {
		boolean isPresent = false;		
		for (Iterator<Node> iterator = this.internalSet.iterator(); iterator.hasNext() && (!isPresent);) {
			Node n = (Node) iterator.next();
			isPresent = n.equals(e);		
		}
		if (!isPresent){
			this.internalSet.add(e);
		}

		return (!isPresent);
	}

	public boolean addAll(Collection<? extends Node> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		this.internalSet.clear();
	}

	public boolean contains(Object o) {
		return this.internalSet.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return this.internalSet.containsAll(c);
	}

	public boolean isEmpty() {
		return this.internalSet.isEmpty();
	}

	public Iterator<Node> iterator() {
		return this.internalSet.iterator();
	}

	public boolean remove(Object o) {
		return this.internalSet.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return this.internalSet.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return this.retainAll(c);
	}

	public int size() {
		return this.internalSet.size();
	}

	public Object[] toArray() {
		return this.internalSet.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return this.internalSet.toArray(a);
	}
	
}
