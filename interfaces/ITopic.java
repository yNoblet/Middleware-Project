package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITopic extends Remote {
	public void subscribe(String pseudo) throws RemoteException;
	public void unsubscribe(String pseudo) throws RemoteException;
	public void post(String pseudo, String message) throws RemoteException;
	public void connect_client(IClient cl) throws RemoteException;
	public void deconnect_client(IClient cl) throws RemoteException;
}
