package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface IServer extends Remote {
	public void getAccount(IClient client) throws RemoteException;
	public Set<String> getTopicTitles() throws RemoteException;
	public boolean newTopic(String title, IClient author) throws RemoteException;
	public ITopic getTopic(String title) throws RemoteException;
	public void goToTopic(String title, IClient cl) throws RemoteException;
}
