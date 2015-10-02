package interfaceMiddleware;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	public void refresh(String message) throws RemoteException;
}
