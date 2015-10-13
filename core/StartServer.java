package core;

import java.rmi.registry.Registry;

public class StartServer {
	public static void main(String[] args) {
		 try {
	            IServer obj = new Server();
	            
	            /*
	            IAccount a = obj.getAccount("author1");
	            IAccount b = obj.getAccount("A");
	           
	            obj.newTopic("a", "author1");
	            obj.newTopic("b", "author1");
	            obj.newTopic("c", "author1");
	            obj.newTopic("d", "author1");
	           
	            b.addSubscription("b");
	            b.addSubscription("c");
	            */
	            
	            Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1097);

	            reg.bind("Server", obj);
	            
	            System.out.println("Server launched");
	            
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
	}
}
