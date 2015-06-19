package com.lfa.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;

/**
 * The Class ImmutableLinkedSet. Works as an implementation of Google's
 * {@link ImmutableSet} which maintains iteration order by storing a
 * {@link ImmutableList} along with its set. Iteration order and criteria for
 * insertion of elements is the same as in {@link LinkedHashSet} and its Builder
 * works in a similar fashion to {@link ImmutableSet}'s Builder.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 *
 * @param <E>
 *            the element type
 */
@ToString(callSuper = false, of = "list")
@EqualsAndHashCode(callSuper = false, of = "set")
public final class ImmutableLinkedSet<E> implements Set<E>, Serializable {

	private static final long serialVersionUID = -4634534523581400045L;

	private final ImmutableSet<E> set;
	private final ImmutableList<E> list;

	public static final class Builder<E> {

		private final LinkedHashSet<E> listSet;

		public Builder() {
			this.listSet = new LinkedHashSet<E>();
		}

		public final Builder<E> add(E element) {
			listSet.add(element);
			return this;
		}

		@SafeVarargs
		public final Builder<E> add(E... element) {
			for (E e : element) {
				listSet.add(e);
			}
			return this;
		}

		public final Builder<E> addAll(Collection<E> collection) {
			listSet.addAll(collection);
			return this;
		}

		public final int size() {
			return listSet.size();
		}

		public final ImmutableLinkedSet<E> build() {
			return new ImmutableLinkedSet<E>(ImmutableSet.<E> builder().addAll(listSet).build(), ImmutableList.<E> builder().addAll(listSet).build());
		}

	}

	private ImmutableLinkedSet(ImmutableSet<E> set, ImmutableList<E> list) {
		this.set = set;
		this.list = list;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@Override
	public UnmodifiableIterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	public E get(int index) {
		return list.get(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public UnmodifiableListIterator<E> listIterator() {
		return list.listIterator();
	}

	public UnmodifiableListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Deprecated
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public static <E> Builder<E> builder() {
		return new Builder<E>();
	}

}
