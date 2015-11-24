package fr.univnantes.middleware.core;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

/**
 * The Interface IServer.
 */
public interface IServer extends Remote {
	
	/**
	 * Gets the port of connection of the server.
	 *
	 * @return the port
	 * @throws RemoteException the remote exception
	 */
	public String getPort() throws RemoteException;
	
	/**
	 * Gets the IP address of the server.
	 *
	 * @return the IP
	 * @throws RemoteException the remote exception
	 */
	public String getIP() throws RemoteException;
	
	/**
	 * Checks the distribution of clients among servers.
	 *
	 * @return the server picked
	 * @throws RemoteException the remote exception
	 */
	public IServer checkDistribution() throws RemoteException;
	
	/**
	 * Posts a message on a topic.
	 *
	 * @param topicTitle the topic title
	 * @param author the author of the message
	 * @param msg the message
	 * @throws RemoteException the remote exception
	 */
	public void postMessage(String topicTitle, String author, String msg) throws RemoteException;
	
	/**
	 * Adds a server to the list.
	 *
	 * @param server the server added
	 * @throws RemoteException the remote exception
	 * @throws NotBoundException the not bound exception
	 */
	public void addServer(IServer server) throws RemoteException, NotBoundException;
	
	/**
	 * Removes the server.
	 *
	 * @param server the server removed
	 * @throws RemoteException the remote exception
	 */
	public void removeServer(IServer server) throws RemoteException;
	
	/**
	 * Gets the server list.
	 *
	 * @return the server list
	 * @throws RemoteException the remote exception
	 */
	public Collection<IServer> getServerList() throws RemoteException;
	
	/**
	 * Create new topic and notify other servers.
	 *
	 * @param topicTitle the topic title
	 * @param author the author of the topic
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean onCreateNewTopic(String topicTitle, String author) throws RemoteException;
	
	/**
	 * Delete topic and notify other servers.
	 *
	 * @param topicTitle the topic title
	 * @return true, if successful
	 * @throws RemoteException the remote exception
	 */
	public boolean onDeleteTopic(String topicTitle) throws RemoteException;
	
	/**
	 * Adds the topic to the list of the server.
	 *
	 * @param topicTitle the topic title
	 * @param author the author of the topic
	 * @throws RemoteException the remote exception
	 */
	public void addTopic(String topicTitle, String author) throws RemoteException;
	
	/**
	 * Removes the topic from the list of the server.
	 *
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void removeTopic(String topicTitle) throws RemoteException;
	
	/**
	 * Gets the topic associate to a single title.
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
	 * Gets the topic titles as a list.
	 *
	 * @return the topic titles list
	 * @throws RemoteException the remote exception
	 */
	public Collection<String> getTopicTitles() throws RemoteException;
	
	/**
	 * Adds the client to the list of connected.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void addClient(IClient client) throws RemoteException;
	
	/**
	 * Removes the client from the list of connected.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void removeClient(IClient client) throws RemoteException;
	
	/**
	 * Gets the connected clients.
	 *
	 * @return the connected clients
	 * @throws RemoteException the remote exception
	 */
	public Collection<IClient> getConnectedClients() throws RemoteException;
	
	/**
	 * Creates and adds an account for a client to the list of the server.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void createAccount(String client) throws RemoteException;
	
	/**
	 * Removes an account from the list of the server.
	 *
	 * @param pseudo the pseudonym account to remove
	 * @throws RemoteException the remote exception
	 */
	void removeAccount(String pseudo) throws RemoteException;
	
	/**
	 * Gets the account associate to the pseudonym, or creates it if nonexistent.
	 *
	 * @param pseudo the account pseudonym
	 * @return the account
	 * @throws RemoteException the remote exception
	 */
	public IAccount getAccount(String pseudo) throws RemoteException;
	
	/**
	 * Gets the account list of the server.
	 *
	 * @return the account list
	 * @throws RemoteException the remote exception
	 */
	public Map<String, IAccount> getAccountList() throws RemoteException;
	
	/**
	 * Subscribe an account to a topic.
	 *
	 * @param topicTitle the topic title
	 * @param pseudo the account pseudonym
	 * @throws RemoteException the remote exception
	 */
	public void subscribe(String topicTitle, String pseudo) throws RemoteException;
	
	/**
	 * Unsubscribe an account from a topic.
	 *
	 * @param topicTitle the topic title
	 * @param pseudo the account pseudonym
	 * @throws RemoteException the remote exception
	 */
	public void unsubscribe(String topicTitle, String pseudo) throws RemoteException;

}
