package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	public void refresh(String message) throws RemoteException;
	public String getPseudo() throws RemoteException;
	public void onDisconnect() throws RemoteException;
	public void addConnectedTopic(ITopic t) throws RemoteException;
	public void removeConnectedTopic(ITopic t) throws RemoteException;
}
