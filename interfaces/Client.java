package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697099462906475057L;
	private String pseudo;
	
	protected Client(String p) throws RemoteException {
		super();
		this.pseudo = p;
	}
	
	public String getPseudo() throws RemoteException {
		return pseudo;
	}

	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public void refresh(String message) throws RemoteException {
		System.out.println(pseudo+" a reçu "+message);
	}
}
