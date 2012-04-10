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
			test = new KVMessage("messageType", KVMessage.marshall(keyTest), KVMessage.marshall(valueTest));
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		try {
			assertEquals(valueTest, test.unMarshallValue());
			assertEquals(keyTest, test.unMarshallKey());
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
		String xml = test.toXML();
		String x = "<KVMessage type=\"messageType\">\n<key>" 
				+ "key" + "</key>\n<value>"
				+ "value"
				+ "</value>\n</KVMessage>\n";
		assertEquals(x, xml);
	}
	
	@Test
	public void testXMLParsing2() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = null;
		try {
			test = new KVMessage("messageType", KVMessage.marshall(keyTest), KVMessage.marshall(valueTest));
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		String xml = test.toXML();
		String x = null;
		try {
			x = "<KVMessage type=\"messageType\">\n<key>" 
					+ KVMessage.marshall(keyTest) + "</key>\n<value>"
					+ KVMessage.marshall(valueTest)
					+ "</value>\n</KVMessage>\n";
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		assertEquals(x, xml);
	}

}
