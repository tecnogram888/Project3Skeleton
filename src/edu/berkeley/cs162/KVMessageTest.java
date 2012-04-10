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
		KVMessage test = null;
		try {
			test = new KVMessage("messageType", KVMessage.marshall(keyTest), KVMessage.marshall(valueTest));
		} catch (KVException e) {
			e.printStackTrace();
			System.exit(1);
		}
		try {
			assertEquals(valueTest, test.unMarshallValue());
		} catch (KVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
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
		System.out.println(128 * java.lang.Math.pow(2,10));
//		assertTrue(x.equals(xml));
		assertEquals(x, xml);
	}
	
	

}
