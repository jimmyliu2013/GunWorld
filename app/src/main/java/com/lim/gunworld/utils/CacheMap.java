package com.lim.gunworld.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 软引用的map
 * @author Administrator
 * @param <K>
 * @param <V>
 */
@SuppressWarnings("serial")
public class CacheMap<K, V> extends HashMap<K, V> {

	private HashMap<K, SoftValue<K, V>> containerMap;
	private ReferenceQueue<V> queue;

	public CacheMap() {
		
		containerMap = new HashMap<K, SoftValue<K, V>>();
		queue = new ReferenceQueue<V>();
	}

	@Override
	public V put(K key, V value) {

		SoftValue<K, V> sr = new SoftValue<K, V>(key, value, queue);
		containerMap.put(key, sr);
		return null;
	}

	@Override
	public V get(Object key) {
		clearsr();
		SoftValue<K, V> sr = containerMap.get(key);
		if (sr != null) {
		
			return sr.get();
		}

		return null;
	}
	
	
	public void clear(){
		containerMap.clear();
	}
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	private void clearsr() {

		SoftValue<K, V> poll = (SoftValue<K, V>) queue.poll();
		while (poll != null) {
			containerMap.remove(poll.key);
			poll = (SoftValue<K, V>) queue.poll();
		}
	}


	private class SoftValue<K, V> extends SoftReference<V> {
		private Object key;

		public SoftValue(K key, V r, ReferenceQueue<? super V> q) {
			super(r, q);
			this.key = key;
		}

	}
}
