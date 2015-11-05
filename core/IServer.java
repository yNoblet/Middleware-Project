package core;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface IServer.
 */
public interface IServer extends Remote {
	
	/**
	 * New topic.
	 *
	 * @param topicTitle the topic title
	 * @param p the p
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean newTopic(String topicTitle, String p) throws RemoteException;
	
	/**
	 * Delete topic.
	 *
	 * @param topicTitle the topic title
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean deleteTopic(String topicTitle) throws RemoteException;
	
	/**
	 * Adds the topic.
	 *
	 * @param topicTitle the topic title
	 * @param author the author
	 * @throws RemoteException the remote exception
	 */
	public void addTopic(String topicTitle, String author) throws RemoteException;
	
	/**
	 * Removes the topic.
	 *
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void removeTopic(String topicTitle) throws RemoteException;
	
	/**
	 * Gets the topic.
	 *
	 * @param topicTitle the topic title
	 * @return the topic
	 * @throws RemoteException the remote exception
	 */
	public ITopic getTopic(String topicTitle) throws RemoteException;
	
	/**
	 * Gets the topic list.
	 *
	 * @return the topic list
	 * @throws RemoteException the remote exception
	 */
	public Map<String, ITopic> getTopicList() throws RemoteException;
	
	/**
	 * Gets the topic titles.
	 *
	 * @return the topic titles
	 * @throws RemoteException the remote exception
	 */
	public Collection<String> getTopicTitles() throws RemoteException;
	
	/**
	 * Subscribe.
	 *
	 * @param topicTitle the topic title
	 * @param pseudo the pseudo
	 * @throws RemoteException the remote exception
	 */
	public void subscribe(String topicTitle, String pseudo) throws RemoteException;
	
	/**
	 * Adds the client.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	public void addClient(IClient cl) throws RemoteException;
	
	/**
	 * Removes the client.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	public void removeClient(IClient cl) throws RemoteException;
	
	/**
	 * Adds the server.
	 *
	 * @param s the s
	 * @throws RemoteException the remote exception
	 * @throws NotBoundException the not bound exception
	 */
	public void addServer(IServer s) throws RemoteException, NotBoundException;
	
	/**
	 * Removes the server.
	 *
	 * @param s the s
	 * @throws RemoteException the remote exception
	 */
	public void removeServer(IServer s) throws RemoteException;
	
	/**
	 * Gets the server list.
	 *
	 * @return the server list
	 * @throws RemoteException the remote exception
	 */
	public Collection<IServer> getServerList() throws RemoteException;
	
	/**
	 * Adds the account.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	public void addAccount(String cl) throws RemoteException;
	
	/**
	 * Removes the account.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	void removeAccount(String cl) throws RemoteException;
	
	/**
	 * Gets the account.
	 *
	 * @param client the client
	 * @return the account
	 * @throws RemoteException the remote exception
	 */
	public IAccount getAccount(String client) throws RemoteException;
	
	/**
	 * Gets the account list.
	 *
	 * @return the account list
	 * @throws RemoteException the remote exception
	 */
	public Map<String, IAccount> getAccountList() throws RemoteException;
	
	/**
	 * Gets the connected client.
	 *
	 * @return the connected client
	 * @throws RemoteException the remote exception
	 */
	public Collection<IClient> getConnectedClient() throws RemoteException;
	
	/**
	 * Post message.
	 *
	 * @param topicTitle the topic title
	 * @param author the author
	 * @param msg the msg
	 * @throws RemoteException the remote exception
	 */
	public void postMessage(String topicTitle, String author, String msg) throws RemoteException;
	
	/**
	 * Check partition.
	 *
	 * @return the i server
	 * @throws RemoteException the remote exception
	 */
	public IServer checkPartition() throws RemoteException;

	/**
	 * Gets the port.
	 *
	 * @return the port
	 * @throws RemoteException the remote exception
	 */
	public String getPort() throws RemoteException;
	
	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 * @throws RemoteException the remote exception
	 */
	public String getIP() throws RemoteException;

}
