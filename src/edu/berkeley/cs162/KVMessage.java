/**
 * 
 * XML Parsing library for the key-value store
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StringWriter;

import org.w3c.dom.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


/**
 * This is the object that is used to generate messages the XML based messages 
 * for communication between clients and servers. Data is stored in a 
 * marshalled String format in this object.
 */
public class KVMessage{
	private String msgType = null;
	private String key = null;
	private String value = null;
	private boolean status = false;
	private String message = null;

	public KVMessage(String msgType, String key, String value) {
		this.msgType = msgType;
		this.key = key;
		this.value = value;
	}
	
	// added by luke
	Text text;
	
	public KVMessage(String msgType, String key) {
		this.msgType = msgType;
		this.key = key;
		this.value = null;
	}
	
	//For constructing error messages and successful delete messages
	public KVMessage(String message){
		this.msgType = "resp";
		this.message = message;
	}
	
	//For successful put message
	public KVMessage(boolean status, String message){
		this.status = status;
		this.message = message;
		this.msgType = "resp";
	}
	
	public String getMsgType(){
		return msgType;
	}
	
	public String getKey(){
		return key;
	}
	
	public String getValue(){
		return value;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public String getMessage(){
		return message;
	}
	
	/* Hack for ensuring XML libraries does not close input stream by default.
	 * Solution from http://weblogs.java.net/blog/kohsuke/archive/2005/07/socket_xml_pitf.html */
	private class NoCloseInputStream extends FilterInputStream {
	    public NoCloseInputStream(InputStream in) {
	        super(in);
	    }
	    
	    public void close() {} // ignore close
	}
	

	  private static String getTagValue(String sTag, Element eElement) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		 
		        Node nValue = (Node) nlList.item(0);
		 
			return nValue.getNodeValue();
		  }

	
	/**
	 * Sites used:
	 * http://www.developerfusion.com/code/2064/a-simple-way-to-read-an-xml-file-in-java/
	 * http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	 * @param input
	 */	
	public KVMessage(InputStream input) throws KVException{
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(input);
			
			doc.getDocumentElement().normalize();
			System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
	         
			// messageList should be a NodeList with only ONE Node
			NodeList typeList = doc.getElementsByTagName("KVMessage");
			
			Node typeNode = typeList.item(0);
			Element typeElement = (Element) typeNode;

			msgType = typeElement.getAttribute("type");
			System.out.println("MsgType: "+msgType);
			if (msgType == "resp"){ // KVMessage is an incoming response from the server
				NodeList statusList = typeElement.getElementsByTagName("Status");
				if (statusList.getLength() != 0){ // some xmls don't have a Status field
/*					Node statusNode = statusList.item(0);
					if (statusNode.getNodeType() != Node.ELEMENT_NODE) {} // TODO throw an exception?
					Element statusElement = (Element)statusList.item(0);*/
					String temp = getTagValue("Status", typeElement);
					if (temp.equals("True")){ status = true;}
					else{ status = false;}
				}
				message = getTagValue("Message", typeElement);

/*				NodeList messageList = typeElement.getElementsByTagName("Message");
				Node messageNode = messageList.item(0);
				if (messageNode.getNodeType() != Node.ELEMENT_NODE){} // TODO throw an exception?
				Element messageElement = (Element)messageList.item(0);
				message = getTagValue("Message", messageElement);*/

				
			} else { // KVMessage is an outgoing message to the server
				key = getTagValue("Key", typeElement);
				/*NodeList keyList = typeElement.getElementsByTagName("Key");
				Node keyNode = keyList.item(0);
				System.out.println("KeyNode.getChildNodes has length: " + keyNode.getChildNodes().getLength());
				if (keyNode.getNodeType() != Node.ELEMENT_NODE) {} // TODO throw an exception?
				Element keyElement = (Element)keyList.item(0);
				
				NodeList nlList = keyElement.getChildNodes();
				System.out.println("nlList has length: " + nlList.getLength());
				
				Node nValue = (Node) nlList.item(0);
			 
				key = nValue.getNodeValue().trim();
				*/
				System.out.println("Key: "+ key);
				NodeList valueList = typeElement.getElementsByTagName("Value");
				if (valueList.getLength() != 0){
					value = getTagValue("Value", typeElement);
					System.out.println("Value: "+value);
/*					Node valueNode = valueList.item(0);
					if (valueNode.getNodeType() != Node.ELEMENT_NODE) {} // TODO throw an exception?
					Element valueElement = (Element)valueList.item(0);
					value = getTagValue("Value", valueElement);*/
				}
				
				if (msgType == "putreq" && value == null) throw new KVException (new KVMessage("XML Eror: Received unparseable message"));
			}
	         
		}catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
		}catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		}catch (Throwable t) {
			t.printStackTrace ();
		}
	}
	
	/**
	 * Generate the XML representation for this message.
	 * @return the XML String
	 */
	public String toXML() {
		String rtn = null;
		try {
			/////////////////////////////
            //Creating an empty XML Document
            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

        	/////////////////////////////
            //Creating the XML tree
            //create the root element and add it to the document
            Element root = doc.createElement("KVMessage");
            doc.appendChild(root);
            root.setAttribute("type", msgType);

            //create child element, add an attribute, and add to root
            Element keyElement = doc.createElement("Key");
            root.appendChild(keyElement);

            //add a text element to the child
            text = doc.createTextNode(key);
            keyElement.appendChild(text);
            
            if (value != null){
            	//create child element, add an attribute, and add to root
                Element valueElement = doc.createElement("Value");
                root.appendChild(valueElement);

                //add a text element to the child
                text = doc.createTextNode(value);
                valueElement.appendChild(text);
            }
            /////////////////////////////
            //Output the XML
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            rtn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString;

            //print xml
            System.out.println("Here's the xml:\n\n" + rtn);
            
		}catch (Throwable t) {
			t.printStackTrace ();
		}
		return rtn;
	}
	/**
	 * http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
	 * http://stackoverflow.com/questions/20778/how-do-you-convert-binary-data-to-strings-and-back-in-java
	 * @throws KVException Over Sized Key
	 */
	public static String marshall(Object input) throws KVException{
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oout = new ObjectOutputStream(bos);
			oout.writeObject(input);
			byte[] inputByteArray = bos.toByteArray();
			if (inputByteArray.length > 256) {
				throw new KVException(new KVMessage ("Over sized key"));
			}
			
			String marshalled = DatatypeConverter.printBase64Binary(inputByteArray);

			oout.close();
			bos.close();
			return marshalled;
		} catch (IOException i) {
			i.printStackTrace();
		}
		return null;
	}
	
	/**
	 * http://stackoverflow.com/questions/20778/how-do-you-convert-binary-data-to-strings-and-back-in-java
	 * http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
	 * @throws KVException Over Sized Value
	 */
	public Object unMarshallValue() throws KVException{
		try {
			byte[] unMarshalled = DatatypeConverter.parseBase64Binary(value);

			// make sure the length of the byte array is less than 128KB
			if (unMarshalled.length > 128 * java.lang.Math.pow(2,10)) {
				throw new KVException(new KVMessage("Over sized value"));
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(unMarshalled);
		    ObjectInput in = new ObjectInputStream(bis);
		    Object deserialized = in.readObject();
//		    System.out.println("u="+ deserialized);
		    bis.close();
		    in.close();
		    return deserialized;
			
	/*		FileInputStream fileInputStream = new FileInputStream("foo.ser");
			ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
			Object one = oInputStream.readObject();
			
			
			ByteArrayInputStream fileIn = new ByteArrayInputStream(value.getBytes());
			if (unMarshalled.length > 131072) {
				// TODO throw exception
			}
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object rtn = in.readObject();
			in.close();
			fileIn.close();
			return rtn;*/
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Object class not found");
			c.printStackTrace();
		}
		// this is to make Eclipse happy
		return null;
	}
	
	/**
	 * http://stackoverflow.com/questions/20778/how-do-you-convert-binary-data-to-strings-and-back-in-java
	 * http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
	 * @throws KVException Over Sized Value
	 */
	public Object unMarshallKey() throws KVException{
		try {
			byte[] unMarshalled = DatatypeConverter.parseBase64Binary(key);

			// make sure the length of the byte array is less than 128KB
			if (unMarshalled.length > 256) {
				throw new KVException(new KVMessage("Over sized value"));
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(unMarshalled);
		    ObjectInput in = new ObjectInputStream(bis);
		    Object deserialized = in.readObject();
//		    System.out.println("u="+ deserialized);
		    bis.close();
		    in.close();
		    return deserialized;
			
	/*		FileInputStream fileInputStream = new FileInputStream("foo.ser");
			ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
			Object one = oInputStream.readObject();
			
			
			ByteArrayInputStream fileIn = new ByteArrayInputStream(value.getBytes());
			if (unMarshalled.length > 131072) {
				// TODO throw exception
			}
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object rtn = in.readObject();
			in.close();
			fileIn.close();
			return rtn;*/
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Object class not found");
			c.printStackTrace();
		}
		// this is to make Eclipse happy
		return null;
	}
	
}
