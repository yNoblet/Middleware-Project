package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ITopic extends Remote {
	public void subscribe(String pseudo) throws RemoteException;

	public void unsubscribe(String pseudo) throws RemoteException;

	public void post(String pseudo, String message) throws RemoteException;

	public void connectClient(IClient cl) throws RemoteException;

	public void disconnectClient(IClient cl) throws RemoteException;

	public String getAuthor() throws RemoteException;

	public String getDate() throws RemoteException;

	public Map<String, Integer> getClientList() throws RemoteException;

	public String getTitle() throws RemoteException;

	public void addNbMsg(String client) throws RemoteException;
}
