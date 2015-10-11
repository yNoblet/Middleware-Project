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

			Registry registry;
		
			registry = LocateRegistry.getRegistry(remoteIp, remotePort);
		
			IServer s;
			s = (IServer) registry.lookup(remoteObjectName);
			
			if(s != null){
				
				SignInWindow sw = new SignInWindow();
				sw.setServer(s);
				sw.start(primaryStage);
				
			} else {
				System.out.println("No server!!!");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (NotBoundException e) {
			e.printStackTrace();
		}

	}
}