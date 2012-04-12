package edu.berkeley.cs162;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestStorage {

	KVClient<String, String> client = new KVClient<String, String>("localhost", 8080);

	@Test
	public void test1() {

		try {
			client.get("Test"); // this should throw an exception
			System.out.println("this should not be printed");
			assertTrue(false);
		} catch (KVException e) {
			assertEquals("Does not exist", e.getMsg().getMessage());
		}
		try{
			assertFalse(client.put("Test1", "Test2"));
			assertTrue(client.put("Test1", "Test3"));
			assertEquals("Test3", client.get("Test1"));
			client.del("Test1");
		} catch (KVException e) {
			// if there's an exception thrown, fail
			assertTrue(false);
		}
		try{
			client.del("Test1"); // this should throw an exception
			System.out.println("this should not be printed");
			assertTrue(false);
		} catch (KVException e) {
			assertEquals("Does not exist", e.getMsg().getMessage());
		}
		
		try{
			client.get("Test1"); // this should throw an exception
			System.out.println("this should not be printed");
			assertTrue(false);
		} catch (KVException e) {
			assertEquals("Does not exist", e.getMsg().getMessage());
		}
	}

}
