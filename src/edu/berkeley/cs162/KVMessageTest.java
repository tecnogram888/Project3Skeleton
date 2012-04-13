package edu.berkeley.cs162;

import static org.junit.Assert.*;

import javax.naming.directory.BasicAttribute;

import org.junit.Test;

public class KVMessageTest {
	
	@Test
	public void testConstructorThreeArguments() {
		KVMessage test = new KVMessage("messageType", "key", "value");
		assertEquals(test.getMsgType(), "messageType");
		assertEquals(test.getKey(), "key");
		assertEquals(test.getValue(), "value");
	}
	
	//TODO Doug! Do constructor tests!
	
	@Test
	public void testConstructorTwoArguments() {
		KVMessage test = new KVMessage("messageType", "key");
		assertEquals(test.getMsgType(), "messageType");
		assertEquals(test.getKey(), "key");
		assertEquals(test.getValue(), null);
	}
	
	@Test
	public void testMarshall() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = null;
		try {
			test = new KVMessage("messageType", KVMessage.serialize(keyTest), KVMessage.serialize(valueTest));
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		try {
			assertEquals(valueTest, test.deserializeValue());
			assertEquals(keyTest, test.deserializeKey());
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Test
	public void testXMLParsing1() {
		KVMessage test = new KVMessage("messageType", "key", "value");
		String xml = null;
		try {
			xml = test.toXML();
		} catch (KVException e) {
			// should NOT ever throw exception here
			e.printStackTrace();
			fail();
		}
		String x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
				+ "<KVMessage type=\"messageType\">\n<Key>" 
				+ "key" + "</Key>\n<Value>"
				+ "value"
				+ "</Value>\n</KVMessage>\n";
		assertEquals(x, xml);
	}
	
	@Test
	public void testXMLParsing2() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = null;
		try {
			test = new KVMessage("messageType", KVMessage.serialize(keyTest), KVMessage.serialize(valueTest));
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		String xml = null;
		try {
			xml = test.toXML();
		} catch (KVException e) {
			// should NOT ever throw exception here
			e.printStackTrace();
			fail();
		}
		String x = null;
		try {
			x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
					+ "<KVMessage type=\"messageType\">\n<Key>" 
					+ KVMessage.serialize(keyTest) + "</Key>\n<Value>"
					+ KVMessage.serialize(valueTest)
					+ "</Value>\n</KVMessage>\n";
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		assertEquals(x, xml);
	}

}
