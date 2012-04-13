package edu.berkeley.cs162;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client1 {
	public static void main(String[] args){
		KVClient<String, String> client = Client.initClient();
		while (true){
			try {
				System.out.println("Should be False: " + client.put("Test1", "Test2"));
				System.out.println("Should be False: " + client.put("Luke", "isCool"));
				System.out.println("Should be False: " + client.put("LukeLu", "isAmazing"));
			} catch (KVException e) {
				System.out.println(e.getMsg().getMessage());
			}
		}
	}

}