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
	
	@Test
	public void testConstructorTwoArguments() {
		KVMessage test = new KVMessage("messageType", "key");
		assertEquals(test.getMsgType(), "messageType");
		assertEquals(test.getKey(), "key");
		assertEquals(test.getValue(), null);
	}
	
	@Test
	public void testSerialize() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = new KVMessage("messageType", KVMessage.marshall(keyTest), KVMessage.marshall(valueTest));
//		System.out.println("msgType = " + test.getMsgType());
//		System.out.println("key = " + test.getKey());
//		System.out.println("value = " + test.getValue());
//		System.out.println(valueTest);
//		System.out.println(test.unMarshallValue());
		assertEquals(valueTest, test.unMarshallValue());
	}
	
	@Test
	public void testXMLParsing() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = new KVMessage("messageType", "key", "value");
		String xml = test.toXML();
		String x = "<KVMessage type=\"messageType\">\n<key>" 
				+ "key" + "</key>\n<value>"
				+ "value"
				+ "</value>\n</KVMessage>\n";
		System.out.println(x);
//		assertTrue(x.equals(xml));
		assertEquals(x, xml);
	}
	
	

}
