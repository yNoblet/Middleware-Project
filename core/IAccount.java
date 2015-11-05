package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The Interface IAccount.
 */
public interface IAccount extends Remote {
	
	/**
	 * Gets the pseudo.
	 *
	 * @return the pseudo
	 * @throws RemoteException the remote exception
	 */
	public String getPseudo() throws RemoteException;
	
	/**
	 * Gets the number of messages.
	 *
	 * @return the number of messages
	 * @throws RemoteException the remote exception
	 */
	public int getNbMsg() throws RemoteException;
	
	/**
	 * Gets the subscription list.
	 *
	 * @return the subscription list
	 * @throws RemoteException the remote exception
	 */
	public ArrayList<String> getSubscriptionList() throws RemoteException;
	
	/**
	 * Adds the subscription to a topic.
	 *
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void addSubscription(String topicTitle) throws RemoteException;
	
	/**
	 * Removes the subscription to a topic.
	 *
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void removeSubscription(String topicTitle) throws RemoteException;
	
	/**
	 * Increments the number of messages.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void incrementsNbMsg() throws RemoteException;
	
}
