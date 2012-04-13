package edu.berkeley.cs162;

public class Client1 {
	public static void main(String[] args){
		KVClient<String, String> client = new KVClient<String, String>("ec2-23-22-30-1.compute-1.amazonaws.com", 8080);
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