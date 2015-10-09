package core;

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
	
	public Map<String, Account> getAccountList() {
		return accounts;
	}
	public void setAccount_list(Map<String, Account> accountList) {
		this.accounts = accountList;
	}
	public Map<String, Topic> getTopicList() {
		return topics;
	}
	public void setTopic_list(Map<String, Topic> topicList) {
		this.topics = topicList;
	}
	
	@Override
	public IAccount getAccount(IClient cl) throws RemoteException {
		if(accounts.get(cl.getPseudo()) == null){
			accounts.put(cl.getPseudo(), new Account(cl.getPseudo()));
		}
		return accounts.get(cl.getPseudo());
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
	
}
