package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

import gui.ChatWindow;
import gui.TopicWindow;

// TODO: Auto-generated Javadoc
/**
 * The Interface IClient.
 */
public interface IClient extends Remote {
	
	/**
	 * Gets the pseudo.
	 *
	 * @return the pseudo
	 * @throws RemoteException the remote exception
	 */
	public String getPseudo() throws RemoteException;
	
	/**
	 * Post.
	 *
	 * @param msg the msg
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void post(String msg, String topicTitle) throws RemoteException;
	
	/**
	 * Refresh.
	 *
	 * @param message the message
	 * @throws RemoteException the remote exception
	 */
	public void refresh(String message) throws RemoteException;
	
	/**
	 * Sets the connected topic.
	 *
	 * @param t the new connected topic
	 * @throws RemoteException the remote exception
	 */
	public void setConnectedTopic(ITopic t) throws RemoteException;
	
	/**
	 * Removes the connected topic.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void removeConnectedTopic() throws RemoteException;	
	
	/**
	 * Adds the subscribed topic.
	 *
	 * @param t the t
	 * @throws RemoteException the remote exception
	 */
	public void addSubscribedTopic(String t) throws RemoteException;
	
	/**
	 * Removes the subscribed topic.
	 *
	 * @param t the t
	 * @throws RemoteException the remote exception
	 */
	public void removeSubscribedTopic(String t) throws RemoteException;
	
	/**
	 * On create topic.
	 *
	 * @param t the t
	 * @throws RemoteException the remote exception
	 */
	public void onCreateTopic(String t) throws RemoteException;
	
	/**
	 * On delete topic.
	 *
	 * @param t the t
	 * @throws RemoteException the remote exception
	 */
	public void onDeleteTopic(String t) throws RemoteException;
	
	/**
	 * On server down.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void onServerDown() throws RemoteException;
	
	/**
	 * Sets the chat window.
	 *
	 * @param cw the cw
	 * @param t the t
	 * @throws RemoteException the remote exception
	 */
	public void setChatWindow(ChatWindow cw, String t) throws RemoteException;
	
	/**
	 * Sets the topic window.
	 *
	 * @param tw the new topic window
	 * @throws RemoteException the remote exception
	 */
	public void setTopicWindow(TopicWindow tw) throws RemoteException;
	
}
