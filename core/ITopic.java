package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ITopic extends Remote {
	public void subscribe(String pseudo) throws RemoteException;

	public void unsubscribe(String pseudo) throws RemoteException;

	public void post(String pseudo, String message) throws RemoteException;

	public void connectClient(IClient cl) throws RemoteException;

	public void disconnectClient(IClient cl) throws RemoteException;

	public String getAuthor() throws RemoteException;

	public Set<String> getClientList() throws RemoteException;

	public String getTitle() throws RemoteException;
}
