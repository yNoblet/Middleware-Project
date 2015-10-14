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
		pseudo = p;
		connectedTopics = new ArrayList<ITopic>();
		server = s;
		server.getTopicTitles();
		server.addClient(this);

		account = s.getAccount(p);

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
	public String getPseudo() throws RemoteException {
		return pseudo;
	}

	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public void refresh(String message) throws RemoteException {
		cw.displayMsg(message);
	}

	private void onDisconnect() throws RemoteException {
		for (ITopic t : connectedTopics) {
			t.disconnectClient(this);
		}
		server.removeClient(this);
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
	public void removeConnectedTopic(String topicTitle) throws RemoteException {
		removeConnectedTopic(server.getTopic(topicTitle));
	}

	@Override
	public void setChatWindow(ChatWindow cw, String topicTitle) throws RemoteException {
		this.cw = cw;
		cw.setIdentifiants(pseudo);
		cw.setServer(server);
		cw.setClient(this);
		cw.setTopic(topicTitle);
		server.getTopic(topicTitle).connectClient(this);
	}

	@Override
	public void setTopicWindow(TopicWindow tw) throws RemoteException {
		this.tw = tw;
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

	@Override
	public void post(String msg, String topicTitle) throws RemoteException {
		server.getTopic(topicTitle).post(pseudo, msg);
	}

	@Override
	public void addTopic(String t) throws RemoteException {
		if (tw != null) {
			tw.addAvailableTopic(t);
		}
	}

	@Override
	public void removeTopic(String t) throws RemoteException {
		if (tw != null) {
			tw.removeTopic(t);
		}
		if (cw != null && cw.getTopic().equals(t)) {
			System.out.println("a");
			cw.onDeleteTopic();
			System.out.println("b");
		}
	}

	@Override
	public void serverDown() throws Exception {
		System.out.println("Server down => exit");
		if (cw != null) {
			cw.serverDown();
		}
		if (tw != null) {
			tw.serverDown();
		}
	}

}
