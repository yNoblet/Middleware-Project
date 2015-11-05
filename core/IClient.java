package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

import gui.ChatWindow;
import gui.TopicWindow;

/**
 * The Interface IClient.
 */
public interface IClient extends Remote {
	
	/**
	 * Gets the pseudo of the client.
	 *
	 * @return the pseudo
	 * @throws RemoteException the remote exception
	 */
	public String getPseudo() throws RemoteException;
	
	/**
	 * Posts the message in parameter.
	 *
	 * @param msg the message
	 * @param topicTitle the topic title
	 * @throws RemoteException the remote exception
	 */
	public void post(String msg, String topicTitle) throws RemoteException;
	
	/**
	 * Refreshes the UI with the new message
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
	 * Adds a topic to the subscribed list.
	 *
	 * @param t the topic
	 * @throws RemoteException the remote exception
	 */
	public void addSubscribedTopic(String t) throws RemoteException;
	
	/**
	 * Removes a topic from the subscribed list.
	 *
	 * @param t the topic
	 * @throws RemoteException the remote exception
	 */
	public void removeSubscribedTopic(String t) throws RemoteException;
	
	/**
	 * On create topic, display the topic in the UI list.
	 *
	 * @param t the topic
	 * @throws RemoteException the remote exception
	 */
	public void onCreateTopic(String t) throws RemoteException;
	
	/**
	 * On delete topic, remove the topic from the UI list.
	 *
	 * @param t the topic
	 * @throws RemoteException the remote exception
	 */
	public void onDeleteTopic(String t) throws RemoteException;
	
	/**
	 * On server down, handles the client activity.
	 *
	 * @throws RemoteException the remote exception
	 */
	public void onServerDown() throws RemoteException;
	
	/**
	 * Sets the chat window of the topic.
	 *
	 * @param cw the chat window
	 * @param t the topic
	 * @throws RemoteException the remote exception
	 */
	public void setChatWindow(ChatWindow cw, String t) throws RemoteException;
	
	/**
	 * Sets the topic window.
	 *
	 * @param tw the topic window
	 * @throws RemoteException the remote exception
	 */
	public void setTopicWindow(TopicWindow tw) throws RemoteException;
	
}
