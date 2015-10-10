package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javax.sql.rowset.RowSetMetaDataImpl;

import gui.ChatWindow;
import gui.TopicWindow;

public interface IClient extends Remote {
	public void refresh(String message) throws RemoteException;
	public String getPseudo() throws RemoteException;
	public void onDisconnect() throws RemoteException;
	public void addConnectedTopic(ITopic t) throws RemoteException;
	public void removeConnectedTopic(ITopic t) throws RemoteException;
	public void removeConnectedTopic(String topicTitle) throws RemoteException;	
	public void addSubscribedTopic(String t) throws RemoteException;
	
	public void addTopic(String t) throws RemoteException;
	public void removeTopic(String t) throws RemoteException;
	
	public void removeSubscribedTopic(String t) throws RemoteException;
	public void setChatWindow(ChatWindow cw, String t) throws RemoteException;
	public void setTopicWindow(TopicWindow tw) throws RemoteException;
	public void post(String msg, String topicTitle) throws RemoteException;
}
