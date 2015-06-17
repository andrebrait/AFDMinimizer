package com.lfa.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableListIterator;

@ToString(callSuper = false, of = "list")
@EqualsAndHashCode(callSuper = false, of = "set")
public final class ImmutableLinkedMap<K, E> implements Map<K, E>, Serializable {

	private static final long serialVersionUID = -4634534523581400045L;

	private final ImmutableSet<E> set;
	private final ImmutableList<E> list;

	public static final class Builder<K, V> {

		private final LinkedHashMap<K, V> listSet;

		public Builder() {
			this.listSet = new LinkedHashMap<K, V>();
		}

		public final Builder<K, V> put(K key, V value) {
			listSet.put(key, v);
			return this;
		}

		@SafeVarargs
		public final Builder<K, V> add(V... element) {
			for (V e : element) {
				listSet.add(e);
			}
			return this;
		}

		public final Builder<K, V> addAll(Collection<V> collection) {
			listSet.addAll(collection);
			return this;
		}

		public final ImmutableLinkedMap<K, V> build() {
			return new ImmutableLinkedMap<K, V>(ImmutableSet.<V> builder().addAll(listSet).build(), ImmutableList.<V> builder().addAll(listSet).build());
		}

	}

	private ImmutableLinkedMap(ImmutableSet<E> set, ImmutableList<E> list) {
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

	public static <K, E> Builder<K, E> builder() {
		return new Builder<K, E>();
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E put(K key, E value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends E> m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<E> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, E>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
