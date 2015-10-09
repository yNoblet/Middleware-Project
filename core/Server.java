package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
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
	public IAccount getAccount(String cl) throws RemoteException {
		if(accounts.get(cl) == null){
			accounts.put(cl, new Account(cl));
		}
		return accounts.get(cl);
	}
	@Override
	public ArrayList<String> getTopicTitles() throws RemoteException {
		//String s=topics.keySet().toString();
		//s = s.substring(1,s.length()-1);
		ArrayList<String> l = new ArrayList<String>();
		l.addAll(topics.keySet());
		return l;
	}
	
	@Override
	public boolean newTopic(String title, String p) throws RemoteException {
		if(topics.get(title) == null){
			topics.put(title, new Topic(title, p));
			accounts.get(p).addSubscription(title);
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
