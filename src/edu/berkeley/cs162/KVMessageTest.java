package edu.berkeley.cs162;

import static org.junit.Assert.*;

import org.junit.Test;

public class KVMessageTest {

	public void testConstructorRegular() {
		KVMessage test = new KVMessage("messageType", "key");
		assertEquals(test.getMsgType(), "messageType");
		assertEquals(test.getKey(), "key");
		assertEquals(test.getValue(), null);
	}

}
