package com.lfa.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * The Class ImmutableLinkedMap. A very precarious implementation of an
 * Immutable linked map, which maintains iteration order. Similar to
 * {@link LinkedHashMap} with building system similar to that of Google's
 * {@link ImmutableMap}.
 *
 * @author Andre Brait (andrebrait@gmail.com)
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
@ToString(callSuper = false, of = "map")
@EqualsAndHashCode(callSuper = false, of = { "map" })
public final class ImmutableLinkedMap<K, V> implements Map<K, V>, Serializable {

	private static final long serialVersionUID = -4634534523581400045L;

	private final ImmutableMap<K, V> map;
	private final ImmutableLinkedSet<K> keySet;
	private final ImmutableLinkedSet<Map.Entry<K, V>> entrySet;
	private final ImmutableList<V> values;

	public static final class Builder<K, V> {

		private final LinkedHashMap<K, V> linkedMap;

		public Builder() {
			this.linkedMap = new LinkedHashMap<>();
		}

		public final Builder<K, V> put(K key, V value) {
			linkedMap.put(key, value);
			return this;
		}

		public final Builder<K, V> addAll(Map<K, V> collection) {
			linkedMap.putAll(collection);
			return this;
		}

		public final ImmutableLinkedMap<K, V> build() {
			ImmutableMap<K, V> finalMap = ImmutableMap.<K, V> builder().putAll(linkedMap).build();

			ImmutableLinkedSet.Builder<K> keySet = ImmutableLinkedSet.<K> builder();
			ImmutableLinkedSet.Builder<Map.Entry<K, V>> entrySet = ImmutableLinkedSet.<Map.Entry<K, V>> builder();
			ImmutableList.Builder<V> values = ImmutableList.<V> builder();

			HashMap<K, Map.Entry<K, V>> entryMap = new HashMap<>();
			for (Map.Entry<K, V> entry : finalMap.entrySet()) {
				entryMap.put(entry.getKey(), entry);
			}

			for (K key : linkedMap.keySet()) {
				keySet.add(key);
				entrySet.add(entryMap.get(key));
				values.add(finalMap.get(key));
			}

			return new ImmutableLinkedMap<K, V>(finalMap, keySet.build(), entrySet.build(), values.build());
		}
	}

	private ImmutableLinkedMap(ImmutableMap<K, V> map, ImmutableLinkedSet<K> keySet, ImmutableLinkedSet<Map.Entry<K, V>> entrySet, ImmutableList<V> values) {
		this.map = map;
		this.keySet = keySet;
		this.entrySet = entrySet;
		this.values = values;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Deprecated
	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		return keySet;
	}

	@Override
	public Collection<V> values() {
		return values;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet;
	}

	public static <K, V> Builder<K, V> builder() {
		return new Builder<>();
	}

}
