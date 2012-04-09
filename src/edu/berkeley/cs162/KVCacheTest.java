package edu.berkeley.cs162;

import static org.junit.Assert.*;

import org.junit.Test;

public class KVCacheTest {
	
	/** 
	 * Correctness Constraints:
Cache only stores the specified number of key value pairs.
Cache removes the correct key value pair according to LRU.
Cache works--returns a value that it has in the cache faster than if it had to fetch it.

Testing Plan:
call put() more times than the size was initialized to be, then peek at the linkedList.size().
place N items in a N sized cache, then call all N of them again and make sure the response time is faster than a call to KVStore.  Then the N+1th item should take longer
place N items in a N sized cache in order, then place an N+1th item, and then ask for the first item inserted: it should take the same amount of time as a call to KVStore. 

	 */
	String key = "hello";
	String value = "world";
	String key1 = "good";
	String value1 = "bye";
	String key2 = "roses";
	String value2 = "red";

	@Test
	public void test_initialize_Small() {
		System.out.println("Create KVCache of size 2");
		KVCache cache = new KVCache(2);
		assertTrue(cache != null);
	}

	@Test
	public void test_put1_Small() {
		System.out.println("Test if put works");
		KVCache cache = new KVCache(2);
		cache.put(key, value);
		assertTrue(cache != null);
	}
	
	@Test
	public void test_put2_Small() {
		System.out.println("Test putting the same <k, v> in twice doesn't make a duplicate");
		KVCache cache = new KVCache(2);
		assertTrue(cache != null);
		cache.put(key, value);
		cache.put(key1, value1);
		cache.put(key1, value1);
		assertTrue(cache.get(key) == value);
	}
	
	@Test
	public void test_put3_Small() {
		System.out.println("Test put(k, v) then put(k, v1) has get(k) == v1");
		KVCache cache = new KVCache(2);
		assertTrue(cache != null);
		cache.put(key, value);
		cache.put(key, value1);
		assertTrue(cache.get(key) == value1);
	}
	
	@Test
	public void test_put4_Small() {
		System.out.println("Test if the right value is evicted when cache is overloaded");
		KVCache cache = new KVCache(2);
		assertTrue(cache != null);
		cache.put(key, value);
		cache.put(key1, value1);
		cache.put(key2 ,value2);
		assertTrue(cache.get(key) == null);
		assertTrue(cache.get(key1) == value1);
		assertTrue(cache.get(key2) == value2);
	}
	
	@Test
	public void test_get() {
	
	}
	
	@Test 
	public void test_del() {
		
	}
	
	/*
	@Test
	public void test_initializeBig() {
		System.out.println("Create KVCache of size 1000");
		KVCache cache2 = new KVCache(1000);
	}
	 */
	
}
