package core;

import java.rmi.registry.Registry;

public class StartServer {
	public static void main(String[] args) {
		 try {
	            IServer obj = new Server();
	            Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1097);
	            // System.setSecurityManager(new java.rmi.RMISecurityManager());

	            reg.bind("Server", obj); 
	            System.out.println("Server launched");
	            
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
	}
}
