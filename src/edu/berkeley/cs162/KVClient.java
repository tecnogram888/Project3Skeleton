/**
 * Client component for generating load for the KeyValue store. 
 * This is also used by the Master server to reach the slave nodes.
 * 
 * @author Prashanth Mohan (http://www.cs.berkeley.edu/~prmohan)
 *
 * Copyright (c) 2011, University of California at Berkeley
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of University of California, Berkeley nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *    
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL PRASHANTH MOHAN BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.berkeley.cs162;

import java.io.Serializable;
import java.net.Socket;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


/**
 * This class is used to communicate with (appropriately marshalling and unmarshalling) 
 * objects implementing the {@link KeyValueInterface}.
 *
 * @param <K> Java Generic type for the Key
 * @param <V> Java Generic type for the Value
 */
public class KVClient<K extends Serializable, V extends Serializable> implements KeyValueInterface<K, V> {

	private String server = null;
	private int port = 0;
	
	/**
	 * @param server is the DNS reference to the Key-Value server
	 * @param port is the port on which the Key-Value server is listening
	 */
	public KVClient(String server, int port) {
		this.server = server;
		this.port = port;
	}
	
	@Override
	public boolean put(K key, V value) throws KVException {
			String keyString = KVMessage.marshall(key);
			String valueString = KVMessage.marshall(value);
			
			KVMessage message = new KVMessage("putreq", keyString, valueString);
			message = sendRecieve(message);
			// message.msgType should be "resp"
//			if (!message.getMsgType().equals("resp")) throw new KVException(new KVMessage("<KVClient> Unknown Error: response xml not a response!!"));
			// If the message is not "Success," it'll have an error message inside the return xml
			if (!"Success".equals(message.getMessage())) throw new KVException(new KVMessage(message.getMessage()));
			
			// return the boolean status
			return message.getStatus();
	}

	private KVMessage sendRecieve(KVMessage message) throws KVException {
		String xmlFile = message.toXML();
		
		
		Socket connection;
		PrintWriter out = null;
		InputStream in = null;
		try {
			connection = new Socket(server, port);
		} catch (UnknownHostException e) {
			throw new KVException(new KVMessage("Network Error: Could not connect"));
		} catch (IOException e) {
			throw new KVException(new KVMessage("Network Error: Could not create socket"));
		}
		try {
			connection.setSoTimeout(15000);
		} catch (SocketException e1) {
			throw new KVException(new KVMessage("Unknown Error: Could net set Socket timeout"));
		}
		try {
			out = new PrintWriter(connection.getOutputStream(),true);
			out.println(xmlFile);
			connection.shutdownOutput();
		} catch (IOException e) {
			throw new KVException(new KVMessage("Network Error: Could not send data"));
		}
		try {
		in = connection.getInputStream();
		message = new KVMessage(in);
		in.close();
		} catch (IOException e) {
			throw new KVException(new KVMessage("Network Error: Could not receive data"));
		}
		
		
		out.close();
		try {
			connection.close();
		} catch (IOException e) {
			throw new KVException(new KVMessage("Unknown Error: Could not close socket"));
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) throws KVException {
			String keyString = KVMessage.marshall(key);
			KVMessage message = new KVMessage("getreq", keyString);
			message = sendRecieve(message);			
			if (!message.getMsgType().equals("resp")) throw new KVException(new KVMessage("Unknown Error: response xml not a response!!"));
			
			if ("Does not exist".equals(message.getMessage())){
				System.out.println("Get: Does not exist");
				throw new KVException(new KVMessage(message.getMessage()));
			}
			else {
				return (V)message.unMarshallValue();
				}
		
	}

	@Override
	public void del(K key) throws KVException {		
			String keyString = KVMessage.marshall(key);
			KVMessage message = new KVMessage("delreq", keyString);
			message = sendRecieve(message);
			if (!message.getMsgType().equals("resp")) throw new KVException(new KVMessage("Unknown Error: response xml not a response!!"));
//			if ("Does not exist".equals(message.getMessage())) System.out.println("Del: Does not exist");
			if (!"Success".equals(message.getMessage())) throw new KVException(new KVMessage(message.getMessage()));
	
		
	}
}