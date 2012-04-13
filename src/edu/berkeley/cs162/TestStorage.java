package edu.berkeley.cs162;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestStorage {

	KVClient<String, String> client = new KVClient<String, String>("localhost", 8080);

	//@Test
	// should return a "Does not Exist" error when you get something that's not in the server
	public void testGetError() {
		for (int i = 0; i < 5; i++){
			try {
				client.get("Test"); // this should throw an exception
				System.out.println("this should not be printed");
				assertTrue(false);
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}
	
	@Test
	public void testDelError() {
		for (int i = 0; i < 5; i++){
			try {
				System.out.println(i);
				client.put("Test", "yo");
				client.del("Test");
				client.del("Test"); // this should throw an exception
				System.out.println("this should not be printed");
				assertTrue(false);
			} catch (KVException e) {
				assertEquals("Does not exist", e.getMsg().getMessage());
			}
		}
	}
	
	//@Test
	public void testPutGetSuccess() {
		for (int i = 0; i < 3; i++){
			try {
				assertFalse(client.put("Test1", "Test2"));
				assertTrue(client.put("Test1", "Test3"));
				assertEquals(client.get("Test1"), "Test3");
				assertEquals("Test3", client.get("Test1"));
				client.del("Test1");
				try {assertEquals(client.get("Test1"), "this assertion shhould not be tested");
				} catch (KVException e){
					assertEquals("Does not exist", e.getMsg().getMessage());
				}
			} catch (KVException e) {
				// if there's an exception thrown, fail
				System.out.println(e.getMsg().getMessage());
				assertTrue(false);
			}
		}
	}
	
//	@Test
	public void testEverything() {
		for (int i = 0; i < 3; i++){
		try{
			System.out.println(1);
			assertFalse(client.put("Test1", "Test2"));
			System.out.println(2);
			assertTrue(client.put("Test1", "Test3"));
			System.out.println(3);
			assertEquals("Test3", client.get("Test1"));
			System.out.println(4);
			client.del("Test1");
		} catch (KVException e) {
			// if there's an exception thrown, fail
			System.out.println(e.getMsg().getMessage());
			assertTrue(false);
		}
		try{
			client.del("Test1"); // this should throw an exception
			System.out.println("this should not be printed");
			assertTrue(false);
		} catch (KVException e) {
			assertEquals("Does not exist", e.getMsg().getMessage());
		}
/*
		try{
			client.get("Test1"); // this should throw an exception
			System.out.println("this should not be printed");
			assertTrue(false);
		} catch (KVException e) {
			assertEquals("Does not exist", e.getMsg().getMessage());
		}*/
		}
	}

}
