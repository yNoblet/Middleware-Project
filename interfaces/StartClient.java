package interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartClient {
	public static void main(String[] args) {
		try {
			
			int remotePort = 1097;
			String remoteIp = "localhost"; // ou IP si distant
			String remoteObjectName = "obj";

			//System.setSecurityManager(new java.rmi.RMISecurityManager());

			Registry registry;
		
			registry = LocateRegistry.getRegistry("S046V7pc04", remotePort);
		
			IServer s;
			s = (IServer) registry.lookup(remoteObjectName);
			
			if(s != null){
				System.out.println("youpi");
			} else {
				System.out.println("merde");
			}
			//s.newTopic("Thibaut suxx","Bob");
			Client c1 = new Client("Bob");
			//System.out.println(s);
			//System.out.println(c1);
			s.getAccount(c1);
			//System.out.println(acc);System.out.println(acc.getPseudo());
			s.newTopic("Lalala", c1);
			s.goToTopic("Lalala", c1);
			ITopic t = s.getTopic("Lalala");
			//System.out.println(t);
			t.post(c1.getPseudo(), "Je suis Bob");
			t.post(c1.getPseudo(), "Ah non je suis Robert");
			
			
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