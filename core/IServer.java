package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

public interface IServer extends Remote {
	public IServer checkPartition() throws RemoteException;
	
	public boolean newTopic(String title, String p) throws RemoteException;
	public boolean deleteTopic(String title) throws RemoteException;
	public void addTopic(String title, ITopic topic) throws RemoteException;
	public ITopic getTopic(String title) throws RemoteException;
	public Map<String, ITopic> getTopicList() throws RemoteException;
	public Collection<String> getTopicTitles() throws RemoteException;
	
	public void addClient(IClient cl) throws RemoteException;
	public void removeClient(IClient cl) throws RemoteException;
	
	public void addServer(IServer s) throws RemoteException;
	public void setOtherServers(Collection<IServer> otherServers, IServer server) throws RemoteException;
	
	public IAccount getAccount(String client) throws RemoteException;
	public void setAccountList(Map<String, IAccount> accounts) throws RemoteException;
	public Map<String, IAccount> getAccountList() throws RemoteException;
	
	public Collection<IClient> getConnectedClient() throws RemoteException;
}
