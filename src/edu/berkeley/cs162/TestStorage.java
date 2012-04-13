package edu.berkeley.cs162;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestStorage {

	KVClient<String, String> client = new KVClient<String, String>("localhost", 8080);

	@Test
	// should return a "Does not Exist" error when you get something that's not in the server
	public void testGetError() {
		for (int i = 0; i < 3; i++){
			try {
				client.get("Test"); // this should throw an exception
				System.out.println("this should not be printed");
				fail();
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}
	
	@Test
	public void testDelError() {
		for (int i = 0; i < 3; i++){
			try {
				client.del("Test"); // this should throw an exception
				System.out.println("this should not be printed");
				fail();
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}
	
	@Test
	public void testPutGetSuccess() {
		for (int i = 0; i < 3; i++){
			try {
				assertFalse(client.put("Test1", "Test2"));
				assertTrue(client.put("Test1", "Test3"));
				assertEquals(client.get("Test1"), "Test3");
				assertEquals("Test3", client.get("Test1"));
				client.del("Test1");
			} catch (KVException e) {
				// if there's an exception thrown, fail
				System.out.println(e.getMsg().getMessage());
				fail();
			}
		}
	}
	
	@Test
	public void testPutDelGet() {
		for (int i = 0; i < 3; i++){
			try{
				assertFalse(client.put("Test1", "Test2"));
				assertTrue(client.put("Test1", "Test3"));
				client.del("Test1");
				client.get("Test1");
				System.out.println("this should not print");
				fail();
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}

	@Test
	public void testPutDelDel() {
		for (int i = 0; i < 3; i++){
			try{
				System.out.println(i);
				assertFalse(client.put("Test1", "Test2"));
				assertTrue(client.put("Test1", "Test3"));
				client.del("Test1");
				client.del("Test1"); // this should throw an exception
				System.out.println("this should not be printed");
				assertTrue(false);
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}
	
	@Test
	public void testAll() {
		for (int i = 0; i < 3; i++){
			try{
				System.out.println(i);
				assertFalse(client.put("Test1", "Test2"));
				assertTrue(client.put("Test1", "Test3"));
				assertFalse(client.put("Luke", "isCool"));
				assertTrue(client.put("Luke", "isCool"));
				assertFalse(client.put("LukeLu", "isAmazing"));
				assertEquals(client.get("Luke"), "isCool");
				assertEquals(client.get("LukeLu"), "isAmazing");
				client.del("Luke");
				client.del("LukeLu");
				client.del("Test1");
			} catch (KVException e) {
				// this test should not throw an exception
				fail();
			}
		}
	}
	
	@Test
	public void testNullKey() {
		for (int i = 0; i < 3; i++){
			try{
				System.out.println(i);
				client.put("", "Test2");
				// should throw KVException
				fail();
			} catch (KVException e) {
				assertEquals(e.getMsg().getMessage(), "Empty key");
			}
		}
	}
	
	@Test
	public void testNullValue() {
		for (int i = 0; i < 3; i++){
			try{
				System.out.println(i);
				client.put("asdf", "");
				// should throw KVException
				fail();
			} catch (KVException e) {
				assertEquals(e.getMsg().getMessage(), "Empty value");
			}
		}
	}
}
