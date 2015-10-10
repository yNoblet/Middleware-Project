package core;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gui.SignInWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartClient extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			
			int remotePort = 1097;
			String remoteIp = "localhost"; // ou IP si distant
			String remoteObjectName = "Server";

			//System.setSecurityManager(new java.rmi.RMISecurityManager());

			Registry registry;
		
			registry = LocateRegistry.getRegistry(remoteIp, remotePort);
		
			IServer s;
			s = (IServer) registry.lookup(remoteObjectName);
			
			if(s != null){
				
				SignInWindow sw = new SignInWindow();
				sw.setServer(s);
				sw.start(primaryStage);
				
				/*
				System.out.println("Server reached");
				//s.newTopic("Thibaut suxx","Bob");
				IClient c1 = new Client("Bob");
				//System.out.println(s);
				//System.out.println(c1);
				IAccount a = (IAccount) s.getAccount(c1);
				//System.out.println(acc);System.out.println(acc.getPseudo());
				s.newTopic("Lalala", c1);
				
				ITopic t = s.getTopic("Lalala");
				t.connectClient(c1);
				//System.out.println(t);
				t.post(c1.getPseudo(), "Je suis Bob");
				t.post(c1.getPseudo(), "Ah non je suis Robert");
				*/
				
				
				
			} else {
				System.out.println("No server!!!");
			}
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