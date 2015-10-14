package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements IServer {
	/**
	 *
	 */
	private static final long serialVersionUID = 5348901788833610650L;
	private Map<String, Account> accounts;
	private Map<String, Topic> topics;
	private Collection<IClient> connectedClient;

	public Server() throws RemoteException {
		accounts = new HashMap<String, Account>();
		topics = new HashMap<String, Topic>();
		connectedClient = new ArrayList<IClient>();

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

	public void onDisconnect() throws RemoteException {
		for (IClient c : connectedClient) {
			try {
				c.serverDown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Map<String, Account> getAccountList() {
		return accounts;
	}

	public void setAccount_list(Map<String, Account> accountList) {
		accounts = accountList;
	}

	public Map<String, Topic> getTopicList() {
		return topics;
	}

	public void setTopic_list(Map<String, Topic> topicList) {
		topics = topicList;
	}

	@Override
	public synchronized IAccount getAccount(String cl) throws RemoteException {
		if (accounts.get(cl) == null) {
			accounts.put(cl, new Account(cl));
		}
		return accounts.get(cl);
	}

	@Override
	public synchronized ArrayList<String> getTopicTitles() throws RemoteException {
		// String s=topics.keySet().toString();
		// s = s.substring(1,s.length()-1);
		ArrayList<String> l = new ArrayList<String>();
		l.addAll(topics.keySet());
		return l;
	}

	@Override
	public synchronized boolean newTopic(String title, String p) throws RemoteException {
		if (topics.get(title) == null) {
			topics.put(title, new Topic(title, p));
			accounts.get(p).addSubscription(title);
			for (IClient c : connectedClient) {
				if (!c.getPseudo().equals(p)) {
					c.addTopic(title);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public synchronized ITopic getTopic(String title) throws RemoteException {
		return topics.get(title);
	}

	@Override
	public synchronized boolean deleteTopic(String title) throws RemoteException {
		Topic t = topics.get(title);
		for (String client : t.getClientList()) {
			accounts.get(client).removeSubscription(title);
		}
		topics.remove(title);
		for (IClient c : connectedClient) {
			c.removeTopic(title);
		}
		return true;
	}

	@Override
	public synchronized void addClient(IClient cl) throws RemoteException {
		connectedClient.add(cl);
	}

	@Override
	public synchronized void removeClient(IClient cl) throws RemoteException {
		connectedClient.remove(cl);
	}

}
