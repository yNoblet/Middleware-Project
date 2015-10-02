package interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartClient {
	public static void main(String[] args) {
		try {
			
			int remotePort = 1095;
			String remoteIp = "localhost"; // ou IP si distant
			String remoteObjectName = "obj";

			//System.setSecurityManager(new java.rmi.RMISecurityManager());

			Registry registry;
		
			registry = LocateRegistry.getRegistry(remoteIp, remotePort);
		
			IServer s;
			s = (IServer) registry.lookup(remoteObjectName);
			
			if(s != null){
				System.out.println("youpi");
			} else {
				System.out.println("merde");
			}
			s.newTopic("Thibaut suxx");
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}