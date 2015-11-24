package fr.univnantes.middleware.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * The Interface ITopic.
 */
public interface ITopic extends Remote {
	
	/**
	 * Gets the title of the topic.
	 *
	 * @return the title of the topic
	 * @throws RemoteException the remote exception
	 */
	public String getTitle() throws RemoteException;
	
	/**
	 * Gets the author of the topic.
	 *
	 * @return the author of the topic
	 * @throws RemoteException the remote exception
	 */
	public String getAuthor() throws RemoteException;
	
	/**
	 * Gets the creation date of the topic.
	 *
	 * @return the date of creation
	 * @throws RemoteException the remote exception
	 */
	public String getDate() throws RemoteException;
	
	/**
	 * Gets the list of clients who subscribed.
	 *
	 * @return the client list
	 * @throws RemoteException the remote exception
	 */
	public Map<String, Integer> getClientList() throws RemoteException;

	/**
	 * Posts a message on the topic.
	 *
	 * @param pseudo the author pseudonym of the message
	 * @param message the content of the message
	 * @throws RemoteException the remote exception
	 */
	public void post(String pseudo, String message) throws RemoteException;
	
	/**
	 * Refreshes the number of messages of the client.
	 *
	 * @param client the affected client
	 * @throws RemoteException the remote exception
	 */
	public void refreshNbMsg(String client) throws RemoteException;
	
	/**
	 * Subscribe an account to the topic.
	 *
	 * @param pseudo the account pseudonym
	 * @throws RemoteException the remote exception
	 */
	public void subscribe(String pseudo) throws RemoteException;
	
	/**
	 * Unsubscribe an account from the topic.
	 *
	 * @param pseudo the account pseudonym
	 * @throws RemoteException the remote exception
	 */
	public void unsubscribe(String pseudo) throws RemoteException;
	
	/**
	 * Connect a client to the topic.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void connectClient(IClient client) throws RemoteException;
	
	/**
	 * Disconnect a client from the topic.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void disconnectClient(IClient client) throws RemoteException;
	
}
