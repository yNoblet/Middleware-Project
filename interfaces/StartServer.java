package interfaces;

import java.rmi.registry.Registry;

public class StartServer {
	public static void main(String[] args) {
		 try {
	            IServer obj = new Server();
	            Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1095);
	            // System.setSecurityManager(new java.rmi.RMISecurityManager());

	            reg.bind("obj", obj); 
	            System.out.println("Zob");
	            
	            
	        } catch (Exception e) {
	            System.err.println("Server exception: " + e.toString());
	            e.printStackTrace();
	        }
	}
}
