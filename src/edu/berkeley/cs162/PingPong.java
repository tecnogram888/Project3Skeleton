package edu.berkeley.cs162;
import java.io.*;
import java.net.*;
import java.util.*;

public class PingPong {
	
	ServerSocket server;
	public Socket client;
	BufferedReader in;
	String line;
	PrintWriter out;
	
	public void listenSocket() throws IOException {

		//create new ServerSocket object
	    try{
	      server = new ServerSocket(8081); 
	    } catch (IOException e) {
	      System.out.println("Could not listen on port 4444");
	      System.exit(-1);
	    }

	    //create new socket client
	    try{
	      client = server.accept();
	    } catch (IOException e) {
	      System.out.println("Accept failed: 8081");
	      System.exit(-1);
	    }
	    
	    //new BufferedReader to read input, PrintWriter to write output
	    try{
	      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	      out = new PrintWriter(client.getOutputStream(), true); //do we need this?
	    } catch (IOException e) {
	      System.out.println("Accept failed: 8081");
	      System.exit(-1);
	    }
	 
	    while(true){
	      try{
	        line = in.readLine();
	//Send data back to client
	        out.println("pong");
	      } catch (IOException e) {
	        System.out.println("Read failed");
	        System.exit(-1);
	      }
	    }
	  }
}
