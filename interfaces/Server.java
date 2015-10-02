package interfaces;

import java.rmi.RemoteException;
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
	public Account getAccount(String pseudo) throws RemoteException {
		return accounts.get(pseudo);
	}
	@Override
	public Set<String> getTopicTitles() throws RemoteException {
		return topics.keySet();
	}
	@Override
	public void newTopic(String title) throws RemoteException {
		topics.put(title, new Topic(title));
		System.out.println("new topic : "+title);
	}
	@Override
	public Topic getTopic(String title) throws RemoteException {
		return topics.get(title);
	}
}
