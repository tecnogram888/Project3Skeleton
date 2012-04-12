package edu.berkeley.cs162;
import java.io.*;
import java.net.*;
import java.util.*;

public class PingPong {
	
	static ServerSocket server;
	public static Socket client;
	static PrintWriter out;
	

	
	public static void listenSocket() throws IOException {

		//create new ServerSocket object
	    try{	
	      server = new ServerSocket(8081); 
	    } catch (IOException e) {
	      System.out.println("Could not listen on port 8081");
	      System.exit(-1);
	    }

	    //create new socket client
	    while (true) {
	    try{
	      client = server.accept();
	    } catch (IOException e) {
	      System.out.println("Accept failed: 8081");
	      System.exit(-1);
	    }
	    
	    out = new PrintWriter(client.getOutputStream(), true);

	    //Send data back to client
	    out.println("pong");
	    client.close();
	    }
	}
	
	public static void main(String[] args) {
		try {
			listenSocket();
		} catch (IOException e) {
			System.out.println("BOOOO" + e);
		}
	}
}
