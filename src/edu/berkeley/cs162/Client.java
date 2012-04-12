package edu.berkeley.cs162;

public class Client {
	public static void main(String[] args){
		KVClient<String, String> client = new KVClient<String, String>("localhost", 8080);
		try {
			System.out.println("Put(\"Test1\", \"Test2\")");
			System.out.println(client.put("Test1", "Test2"));
			System.out.println(client.put("Test1", "Test3"));
			System.out.println(client.get("Test1"));
			client.del("Test1");
//			System.out.println(client.get("Test2"));
			client.del("Test3");
			System.out.println("whoot");
		} catch (KVException e) {
			System.out.println(e.getMsg().getMessage());
		}
		
		
	}

}
