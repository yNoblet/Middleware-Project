package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
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
		this.pseudo = p;
		this.connectedTopics = new ArrayList<ITopic>();
		this.server = s;
		this.server.getTopicTitles();
		this.server.addClient(this);
		this.account = s.getAccount(p);

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
	
	@Override
	public void post(String msg, String topicTitle) throws RemoteException {
		this.server.getTopic(topicTitle).post(this.pseudo, msg);
	}
	
	@Override
	public void refresh(String message) throws RemoteException {
		this.cw.displayMsg(message);
	}

	private void onDisconnect() throws RemoteException {
		for (ITopic t : this.connectedTopics) {
			t.disconnectClient(this);
		}
		this.server.removeClient(this);
	}
	
	@Override
	public void addConnectedTopic(ITopic t) throws RemoteException {
		this.connectedTopics.add(t);
	}
	
	@Override
	public void removeConnectedTopic(String topicTitle) throws RemoteException {
		this.connectedTopics.remove(this.server.getTopic(topicTitle));
	}

	@Override
	public void setChatWindow(ChatWindow cw, String topicTitle) throws RemoteException {
		this.cw = cw;
		cw.setIdentifiants(this.pseudo);
		cw.setServer(this.server);
		cw.setClient(this);
		cw.setTopic(topicTitle);
		this.server.getTopic(topicTitle).connectClient(this);
	}

	@Override
	public void setTopicWindow(TopicWindow tw) throws RemoteException {
		this.tw = tw;
		tw.setPseudo(this.pseudo);
		tw.setClient(this);
		tw.setServer(this.server);
		tw.setSubscribedTopic(this.account.getSubscriptionList());
		Collection<String> at = this.server.getTopicTitles();
		at.removeAll(this.account.getSubscriptionList());
		tw.setAvailableTopic(at);
	}

	@Override
	public void addSubscribedTopic(String t) throws RemoteException {
		this.account.addSubscription(t);
	}

	@Override
	public void removeSubscribedTopic(String t) throws RemoteException {
		this.account.removeSubscription(t);
	}

	@Override
	public void addTopic(String t) throws RemoteException {
		if (this.tw != null) {
			this.tw.addAvailableTopic(t);
		}
	}

	@Override
	public void removeTopic(String t) throws RemoteException {
		if (this.tw != null) {
			this.tw.removeTopic(t);
		}
		if (this.cw != null && this.cw.getTopic().equals(t)) {
			this.cw.onDeleteTopic();
		}
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public void serverDown() throws Exception {
		System.out.println("Server down => exit");
		if (this.cw != null) {
			this.cw.serverDown();
		}
		if (this.tw != null) {
			this.tw.serverDown();
		}
	}

}
