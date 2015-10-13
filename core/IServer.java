package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IServer extends Remote {
	public IAccount getAccount(String client) throws RemoteException;

	public ArrayList<String> getTopicTitles() throws RemoteException;

	public boolean newTopic(String title, String p) throws RemoteException;

	public boolean deleteTopic(String title) throws RemoteException;

	public ITopic getTopic(String title) throws RemoteException;

	public void addClient(IClient cl) throws RemoteException;

	public void removeClient(IClient cl) throws RemoteException;

}
