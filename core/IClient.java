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
	public void setChatWindow(ChatWindow cw) throws RemoteException;
	public void setTopicWindow(TopicWindow tw) throws RemoteException;
}
