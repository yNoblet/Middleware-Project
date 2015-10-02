package interfaceMiddleware;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IServer extends Remote {
	public Account getAccount(String pseudo) throws RemoteException;
	public ArrayList<String> getTopicTitles() throws RemoteException;
	public void newTopic(String title) throws RemoteException;
	public Topic getTopic(String title) throws RemoteException;
}
