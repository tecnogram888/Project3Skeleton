package edu.berkeley.cs162;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

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
	public void testConstructorOneArgument() {
		KVMessage test = new KVMessage("messageError");
		assertEquals(test.getMsgType(), "resp");
		assertEquals(test.getKey(), null);
		assertEquals(test.getValue(), null);
		assertEquals(test.getMessage(), "messageError");
	}
	
	@Test
	public void testConstructorBoolString() {
		KVMessage test = new KVMessage(true,"messageError");
		assertEquals(test.getMsgType(), "resp");
		assertEquals(test.getKey(), null);
		assertEquals(test.getValue(), null);
		assertEquals(test.getMessage(), "messageError");
		try {
			String message = test.toXML();
			String x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
					+ "<KVMessage type=\"resp\">\n<Status>True</Status>\n<Message>" 
					+ "messageError" + "</Message>\n</KVMessage>\n";
			assertEquals(x, message);
		} catch (KVException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testConstructorBoolString2() {
		KVMessage test = new KVMessage(false,"LukeIsAwesome");
		assertEquals(test.getMsgType(), "resp");
		assertEquals(test.getKey(), null);
		assertEquals(test.getValue(), null);
		assertEquals(test.getMessage(), "LukeIsAwesome");
		try {
			String message = test.toXML();
			String x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
					+ "<KVMessage type=\"resp\">\n<Status>False</Status>\n<Message>" 
					+ "LukeIsAwesome" + "</Message>\n</KVMessage>\n";
			assertEquals(x, message);
		} catch (KVException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testMarshall() {
		BasicAttribute keyTest = new BasicAttribute("key");
		BasicAttribute valueTest = new BasicAttribute("value");
		KVMessage test = null;
		try {
			test = new KVMessage("messageType", KVMessage.encodeObject(keyTest), KVMessage.encodeObject(valueTest));
		} catch (KVException e) {
			// Auto-fail if an exception is thrown
			assertTrue(false);
			e.printStackTrace();
			System.exit(1);
		}
		try {
			assertEquals(valueTest, test.decodeValue());
			assertEquals(keyTest, test.decodeKey());
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
			test = new KVMessage("messageType", KVMessage.encodeObject(keyTest), KVMessage.encodeObject(valueTest));
		} catch (KVException e) {
			e.printStackTrace();
			fail();
		}
		String xml = null;
		try {
			xml = test.toXML();
		} catch (KVException e) {
			e.printStackTrace();
			fail();
		}
		String x = null;
		try {
			x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" 
					+ "<KVMessage type=\"messageType\">\n<Key>" 
					+ KVMessage.encodeObject(keyTest) + "</Key>\n<Value>"
					+ KVMessage.encodeObject(valueTest)
					+ "</Value>\n</KVMessage>\n";
		} catch (KVException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(x, xml);
	}
}
