package edu.berkeley.cs162;

public class Client2 {
	public static void main(String[] args){
		KVClient<String, String> client = new KVClient<String, String>("localhost", 8080);
		while (true){
			try {
				System.out.println("Should be Equal to \"Test2\": " + client.get("Test1"));
				System.out.println("Should be Equal to \"isCool\": " + client.get("Luke"));
				System.out.println("Should be Equal to \"isAmazing\": " + client.get("LukeLu"));
				client.del("Test1");
				client.del("Luke");
				client.del("LukeLu");
				System.out.println("Done deleting");
			} catch (KVException e) {
				System.out.println(e.getMsg().getMessage());
			}
		}
	}
}