package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		server.getTopicTitles();
		
		account = (IAccount) s.getAccount(p);
		
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
		this.cw=cw;
	}

	@Override
	public void setTopicWindow(TopicWindow tw) throws RemoteException {
		this.tw =tw;
		tw.setPseudo(pseudo);
		tw.setClient(this);
		tw.setServer(server);
		tw.setSubscribedTopic(account.getSubscriptionList());
		ArrayList<String> at = server.getTopicTitles();
		at.removeAll(account.getSubscriptionList());
		tw.setAvailableTopic(at);
	}

	@Override
	public void addSubscribedTopic(String t) throws RemoteException {
		account.addSubscription(t);
	}

	@Override
	public void removeSubscribedTopic(String t) throws RemoteException {
		account.removeSubscription(t);
	}
}
