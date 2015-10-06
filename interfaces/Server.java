package interfaces;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server extends UnicastRemoteObject implements IServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5348901788833610650L;
	private Map<String, Account> accounts;
	private Map<String, Topic> topics;
	
	public Server() throws RemoteException {
		accounts = new HashMap<String, Account>();
		topics = new HashMap<String, Topic>();
	}
	
	public Map<String, Account> getAccount_list() {
		return accounts;
	}
	public void setAccount_list(Map<String, Account> account_list) {
		this.accounts = account_list;
	}
	public Map<String, Topic> getTopic_list() {
		return topics;
	}
	public void setTopic_list(Map<String, Topic> topic_list) {
		this.topics = topic_list;
	}
	
	@Override
	public void getAccount(IClient cl) throws RemoteException {
		if(accounts.get(cl.getPseudo()) == null){
			accounts.put(cl.getPseudo(), new Account(cl.getPseudo()));
		}
		//return accounts.get(cl.getPseudo());
	}
	@Override
	public Set<String> getTopicTitles() throws RemoteException {
		return topics.keySet();
	}
	@Override
	public boolean newTopic(String title, IClient author) throws RemoteException {
		if(topics.get(title) == null){
			topics.put(title, new Topic(title, author.getPseudo()));
			accounts.get(author.getPseudo()).addSubscription(title);
			System.out.println("new topic : "+title);
			return true;
		}
		return false;
	}
	@Override
	public ITopic getTopic(String title) throws RemoteException {
		return topics.get(title);
	}
	@Override
	public void goToTopic(String topicTitle, IClient client) throws RemoteException {
		topics.get(topicTitle).connect_client(client);
	}
}
