package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Client extends UnicastRemoteObject implements IClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697099462906475057L;
	private String pseudo;
	private List<ITopic> connectedTopics;
 	
	protected Client(String p) throws RemoteException {
		super();
		this.pseudo = p;
		connectedTopics = new ArrayList<ITopic>();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			
			@Override
			public void run() {
				try {
					System.out.println("zob");
					onDisconnect();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
				}
			}
		}));
	}
	
	public String getPseudo() throws RemoteException {
		return pseudo;
	}

	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public void refresh(String message) throws RemoteException {
		System.out.println(pseudo+" a reçu "+message);
	}

	@Override
	public void onDisconnect() throws RemoteException {
		for (ITopic t: connectedTopics){
			t.disconnectClient(this);
		}
	}

	@Override
	public void addConnectedTopic(ITopic t) throws RemoteException {
		connectedTopics.add(t);
	}

	@Override
	public void removeConnectedTopic(ITopic t) throws RemoteException {
		connectedTopics.remove(t);
	}
}
