package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface ITopic.
 */
public interface ITopic extends Remote {
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 * @throws RemoteException the remote exception
	 */
	public String getTitle() throws RemoteException;
	
	/**
	 * Gets the author.
	 *
	 * @return the author
	 * @throws RemoteException the remote exception
	 */
	public String getAuthor() throws RemoteException;
	
	/**
	 * Gets the client list.
	 *
	 * @return the client list
	 * @throws RemoteException the remote exception
	 */
	public Map<String, Integer> getClientList() throws RemoteException;
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 * @throws RemoteException the remote exception
	 */
	public String getDate() throws RemoteException;
	
	/**
	 * Subscribe.
	 *
	 * @param pseudo the pseudo
	 * @throws RemoteException the remote exception
	 */
	public void subscribe(String pseudo) throws RemoteException;
	
	/**
	 * Unsubscribe.
	 *
	 * @param pseudo the pseudo
	 * @throws RemoteException the remote exception
	 */
	public void unsubscribe(String pseudo) throws RemoteException;

	/**
	 * Post.
	 *
	 * @param pseudo the pseudo
	 * @param message the message
	 * @throws RemoteException the remote exception
	 */
	public void post(String pseudo, String message) throws RemoteException;
	
	/**
	 * Connect client.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	public void connectClient(IClient cl) throws RemoteException;
	
	/**
	 * Disconnect client.
	 *
	 * @param cl the cl
	 * @throws RemoteException the remote exception
	 */
	public void disconnectClient(IClient cl) throws RemoteException;

	/**
	 * Adds the nb msg.
	 *
	 * @param client the client
	 * @throws RemoteException the remote exception
	 */
	public void addNbMsg(String client) throws RemoteException;
}
