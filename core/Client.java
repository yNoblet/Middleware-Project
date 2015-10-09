package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import gui.ChatWindow;
import gui.TopicWindow;

public class Client extends UnicastRemoteObject implements IClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697099462906475057L;
	private String pseudo;
	private List<ITopic> connectedTopics;
	IServer server;
	IAccount account;
	ChatWindow cw;
	TopicWindow tw;
 	
	public Client(String p, IServer s) throws RemoteException {
		super();
		this.pseudo = p;
		connectedTopics = new ArrayList<ITopic>();
		server = s;
		account = (IAccount) s.getAccount(this);
		
		cw = new ChatWindow();
		tw = new TopicWindow();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
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

	@Override
	public void setChatWindow(ChatWindow cw) throws RemoteException {
		
	}

	@Override
	public void setTopicWindow(TopicWindow tw) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
