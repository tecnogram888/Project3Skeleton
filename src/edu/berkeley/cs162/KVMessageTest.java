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
		KVMessage test = new KVMessage("messageType", KVMessage.serialize(keyTest), KVMessage.serialize(valueTest));
		System.out.println(test.getMsgType());
		System.out.println(test.getKey());
		System.out.println(test.getValue());
		assertEquals(valueTest, test.deserializeValue());
	}

}
