package core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Account extends UnicastRemoteObject implements IAccount {
	/**
	 *
	 */
	private static final long serialVersionUID = 3768006366498340145L;
	private String pseudo;
	private ArrayList<String> subscriptionList;

	public Account(String p) throws RemoteException {
		pseudo = p;
		subscriptionList = new ArrayList<String>();
	}

	@Override
	public void addSubscription(String topictitle) throws RemoteException {
		subscriptionList.add(topictitle);
		// ENREGISTRER DANS FICHIER
	}

	@Override
	public void removeSubscription(String topictitle) throws RemoteException {
		subscriptionList.remove(topictitle);
	}

	@Override
	public String getPseudo() throws RemoteException {
		return pseudo;
	}

	@Override
	public void setPseudo(String pseudo) throws RemoteException {
		this.pseudo = pseudo;
	}

	@Override
	public ArrayList<String> getSubscriptionList() throws RemoteException {
		return subscriptionList;
	}
}
