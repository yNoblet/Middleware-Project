package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5697099462906475057L;

	protected Client() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private String pseudo;

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	@Override
	public void refresh(String message) throws RemoteException {
		System.out.println(pseudo+" a reçu "+message);
	}
}
